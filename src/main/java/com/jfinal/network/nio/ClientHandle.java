package com.jfinal.network.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * <b>版权信息:</b> 广州智数信息科技有限公司<br>
 * <b>功能描述:</b> <br>
 * <b>版本历史:</b>
 * @author  wpk | 2017年12月7日 下午10:25:00 |创建
 */
public class ClientHandle implements Runnable {
	
	private Selector selector;
	private SocketChannel socketChannel;
	private boolean started = true;
	
	public ClientHandle(){}

	public ClientHandle(Selector selector, SocketChannel socketChannel){
		this.selector = selector;
		this.socketChannel = socketChannel;
	}

	public void run() {
		// 循环遍历selector
		while (started) {
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
					selectorHandle(key);
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	/**
	 * <b>描述：</b> 选择器事件处理
	 * @author wpk | 2017年12月10日 下午9:54:33 |创建
	 * @param key
	 * @return void
	 */
	private void selectorHandle(SelectionKey key){
		try {
			if (key.isValid()) {
				// 连接事件处理
				if(key.isConnectable()){
					connectHandle(key);
					System.out.println("客户端新连接成功。。。");
				}
				// 读取事件处理（主要用于获取客户端发送过来的消息，处理消息后是否返回客户端）
				if(key.isReadable()){
					readHandle(key);
				}
				// 写事件处理（主要用于客户端连接后不断的给客户端发送消息）
				if(key.isWritable()){
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
	 * <b>描述：</b> 连接事件处理器
	 * @author wpk | 2017年12月10日 下午9:54:48 |创建
	 * @param key
	 * @throws ClosedChannelException
	 * @throws IOException
	 * @return void
	 */
	private void connectHandle(SelectionKey key) throws ClosedChannelException, IOException{
		SocketChannel sc = (SocketChannel) key.channel();
		if (sc.finishConnect())
			//当客户端连接成功时，给通道指定只读事件，方便处理从服务端返回来的数据
			socketChannel.register(selector, SelectionKey.OP_READ);
		else
			System.exit(1);
		
	}
	
	/**
	 * <b>描述：</b> 读取事件处理器
	 * @author wpk | 2017年12月10日 下午9:55:25 |创建
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
			String result = new String(bytes, "UTF-8");
			System.out.println("客户端收到消息：" + result);
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
	 * @author wpk | 2017年12月10日 下午9:55:38 |创建
	 * @param key
	 * @return void
	 * @throws IOException 
	 */
	private void writHandle(SelectionKey key) throws IOException{
//		doWrite(socketChannel, msg);
//		socketChannel.register(selector, SelectionKey.OP_READ);
	}
	
	public void stop() {
		started = false;
		// selector关闭后会自动释放里面管理的资源
		if (selector != null)
			try {
				selector.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	// 异步发送消息
	private void doWrite(SocketChannel channel, String request) throws IOException {
		// 将消息编码为字节数组
		byte[] bytes = request.getBytes();
		// 根据数组容量创建ByteBuffer
		ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
		// 将字节数组复制到缓冲区
		writeBuffer.put(bytes);
		// flip操作
		writeBuffer.flip();
		// 发送缓冲区的字节数组
		channel.write(writeBuffer);
		// ****此处不含处理“写半包”的代码
		//注意“粘包”、“半包”情况
	}

	public void sendMsg(String msg) throws Exception {
		//注册通道管理对象的事件为写入时，客户端只负责往服务端发送消息
//		socketChannel.register(selector, SelectionKey.OP_WRITE);
		//注册通道管理对象的事件为读取时，客户端将获取服务端返回的消息
//		socketChannel.register(selector, SelectionKey.OP_READ);
		doWrite(socketChannel, msg);
	}
}
