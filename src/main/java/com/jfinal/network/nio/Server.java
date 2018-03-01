package com.jfinal.network.nio;

import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * <b>版权信息:</b> 广州智数信息科技有限公司<br>
 * <b>功能描述:</b> NIO服务端<br>
 * <b>版本历史:</b>
 * @author  wpk | 2017年12月9日 下午11:13:43 |创建
 */
public class Server {
	
	private static int DEFAULT_PORT = 12345;
	
	private volatile static Server server = null;
	
	public static Server me(){
		if(null == server){
			synchronized(Server.class){
				if(null == server){
					server = new Server();
				}
			}
		}
		return server;
	}

	public static void start() {
		Server.me().start(DEFAULT_PORT);
	}
	
	private void start(int port) {
		try {
			// 创建选择器
			Selector selector = Selector.open();
			// 打开监听通道
			ServerSocketChannel serverChannel = ServerSocketChannel.open();
			// 如果为 true，则此通道将被置于阻塞模式；如果为 false，则此通道将被置于非阻塞模式
			serverChannel.configureBlocking(false);// 开启非阻塞模式
			// 绑定端口 backlog设为1024
			serverChannel.socket().bind(new InetSocketAddress(port), 1024);
			// 监听客户端连接请求
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
			// 启动服务端线程处理器，处理客户端请求
			new Thread(new ServerHandle(selector), "NIO-SERVER").start();
			
			System.out.println("服务器已启动，端口号：" + port);
		} catch (Exception e) {
			
		}
	}
	
	public static void stop(){
		new ServerHandle().stop();
	}
	
	public static void main(String[] args) {
		start();
	}
}
