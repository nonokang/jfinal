package com.jfinal.network.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import com.jfinal.network.bio.Calculator;

/**
 * <b>版权信息:</b> 广州智数信息科技有限公司<br>
 * <b>功能描述:</b> NIO服务端处理器<br>
 * <b>版本历史:</b>
 * 
 * @author wpk | 2017年12月7日 下午10:21:27 |创建
 */
public class ServerHandle implements Runnable {
	
	private Selector selector;
	private boolean start = true;
	private String expression;
	
	public ServerHandle(){}
	
	public ServerHandle(Selector selector){
		this.selector = selector;
	}

	public void run() {
		// 循环遍历selector
		while (start) {
			try {
				// 无论是否有读写事件发生，selector每隔1s被唤醒一次
				int readyChannels = selector.select(1000);
				if(readyChannels == 0) continue;
				// 阻塞,只有当至少一个注册的事件发生的时候才会继续.
				// selector.select();
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> it = keys.iterator();
				SelectionKey key = null;
				while (it.hasNext()) {
					key = it.next();
					it.remove();
					// 处理通道
					selectorHandle(key);
				}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}
	
	/**
	 * <b>描述：</b> 选择器事件处理
	 * @author wpk | 2017年12月9日 下午11:00:46 |创建
	 * @param key
	 * @return void
	 */
	private void selectorHandle(SelectionKey key){
		try {
			if (key.isValid()) {
				// 处理新接入的请求消息
				if(key.isAcceptable()) {
					acceptHandle(key);
				}
				// 读取事件处理（主要用于获取客户端发送过来的消息，处理消息后是否返回客户端）
				if(key.isReadable()){
					readHandle(key);
				}
				// 写事件处理（主要用于客户端连接后不断的给客户端发送消息）
				if(key.isWritable()){
					Thread.sleep(3000);
					writHandle(key);
				}
			}
		} catch (Exception e) {
			if (key != null) {
				key.cancel();
				if (key.channel() != null) {
					try {
						key.channel().close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * <b>描述：</b> 接收新的客户端连接请求处理器
	 * @author wpk | 2017年12月9日 下午11:00:23 |创建
	 * @param key
	 * @throws IOException
	 * @return void
	 */
	private void acceptHandle(SelectionKey key) throws IOException{
		ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
		// 通过ServerSocketChannel的accept创建SocketChannel实例
		// 完成该操作意味着完成TCP三次握手，TCP物理链路正式建立
		SocketChannel sc = ssc.accept();
		// 设置为非阻塞的
		sc.configureBlocking(false);
		// 注册新接入通道为读事件，只要客户端有新的消息传递过来，则触发服务端的isReadable()
		sc.register(selector, SelectionKey.OP_READ);
		// 注册新接入通道为写事件，服务端会一直触发isWritable()
//		sc.register(selector, SelectionKey.OP_WRITE);
	}
	
	/**
	 * <b>描述：</b> 读取事件处理器
	 * @author wpk | 2017年12月9日 下午11:02:38 |创建
	 * @param key
	 * @throws IOException
	 * @return void
	 */
	private void readHandle(SelectionKey key) throws IOException{
		SocketChannel sc = (SocketChannel) key.channel();
		// 创建ByteBuffer，并开辟一个1M的缓冲区
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		// 读取请求码流，返回读取到的字节数
		int readBytes = sc.read(buffer);
		// 读取到字节，对字节进行编解码
		if (readBytes > 0) {
			// 将缓冲区当前的limit设置为position=0，用于后续对缓冲区的读取操作
			buffer.flip();
			// 根据缓冲区可读字节数创建字节数组
			byte[] bytes = new byte[buffer.remaining()];
			// 将缓冲区可读字节数组复制到新建的数组中
			buffer.get(bytes);
			expression = new String(bytes, "UTF-8");
			System.out.println("服务器收到消息：" + expression);
			// 发送应答消息
			sc.register(selector, SelectionKey.OP_WRITE);
		}
		// 没有读取到字节 忽略
		// else if(readBytes==0);
		// 链路已经关闭，释放资源
		else if (readBytes < 0) {
			key.cancel();
			sc.close();
		}
	}
	
	/**
	 * <b>描述：</b> 写入事件处理器
	 * @author wpk | 2017年12月9日 下午11:05:46 |创建
	 * @param key
	 * @throws IOException
	 * @return void
	 */
	private void writHandle(SelectionKey key) throws IOException{
		SocketChannel sc = (SocketChannel) key.channel();
		// 处理数据
		String result = null;
		try {
			result = Calculator.cal(expression).toString();
		} catch (Exception e) {
			result = "计算错误：" + e.getMessage();
		}

		// 将消息编码为字节数组
		byte[] bytes = result.getBytes();
		// 根据数组容量创建ByteBuffer
		ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
		// 将字节数组复制到缓冲区
		writeBuffer.put(bytes);
		// flip操作
		writeBuffer.flip();
		// 发送缓冲区的字节数组
		sc.write(writeBuffer);
		// ****此处不含处理“写半包”的代码

		sc.register(selector, SelectionKey.OP_READ);
	}
	
	/**
	 * <b>描述：</b> 关闭
	 * @author wpk | 2017年12月9日 下午11:09:07 |创建
	 * @return void
	 */
	public void stop() {
		start = false;
		// selector关闭后会自动释放里面管理的资源
		if (selector != null)
			try {
				selector.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}
