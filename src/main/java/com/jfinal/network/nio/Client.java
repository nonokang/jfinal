package com.jfinal.network.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * <b>版权信息:</b> 广州智数信息科技有限公司<br>
 * <b>功能描述:</b> <br>
 * <b>版本历史:</b>
 * 
 * @author wpk | 2017年12月7日 下午10:23:29 |创建
 */
public class Client {
	
	private static String DEFAULT_HOST = "127.0.0.1";
	private static int DEFAULT_PORT = 12345;
	private volatile static Client client = null;
	private static ClientHandle clientHandle;
	
	public static Client me(){
		if(null == client){
			synchronized (Client.class) {
				if(null == client){
					client = new Client();
				}
			}
		}
		return client;
	}

	public static void start() {
		Client.me().start(DEFAULT_HOST, DEFAULT_PORT);
	}
	
	private void start(String ip, int port){
		try {
			// 创建选择器
			Selector selector = Selector.open();
			// 打开监听通道
			SocketChannel socketChannel = SocketChannel.open();
			// 如果为 true，则此通道将被置于阻塞模式；如果为 false，则此通道将被置于非阻塞模式
			socketChannel.configureBlocking(false);// 开启非阻塞模式
			socketChannel.connect(new InetSocketAddress(ip, port));
			socketChannel.register(selector, SelectionKey.OP_CONNECT);
			clientHandle = new ClientHandle(selector, socketChannel);
			new Thread(clientHandle, "NIO-CLIENT").start();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	// 向服务器发送消息
	public static boolean sendMsg(String msg) throws Exception {
		if (msg.equals("q"))
			return false;
		clientHandle.sendMsg(msg);
		return true;
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		start();
        while(Client.sendMsg(new Scanner(System.in).nextLine()));  
	}
}
