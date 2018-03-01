package com.jfinal.network.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * <b>版权信息:</b> 广州智数信息科技有限公司<br>
 * <b>功能描述:</b> 阻塞式I/O创建的客户端 <br>
 * <b>版本历史:</b>
 * 
 * @author wpk | 2017年12月7日 下午9:55:13 |创建
 */
public class Client {
	// 默认的端口号
	private static int DEFAULT_SERVER_PORT = 12345;
	private static String DEFAULT_SERVER_IP = "127.0.0.1";

	public static void send(String expression) {
		send(DEFAULT_SERVER_PORT, expression);
	}

	public static void send(int port, String expression) {
		System.out.println("___客户端发送算术表达式为：" + expression);
		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			socket = new Socket(DEFAULT_SERVER_IP, port);
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			out.println(expression);
			String msg = in.readLine();//客户端没有返回收到消息时，此处将会一直阻塞
			System.out.println("___客户端收到结果为：" + msg);
			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 一下必要的清理工作
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				in = null;
			}
			if (out != null) {
				out.close();
				out = null;
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				socket = null;
			}
		}
	}
}
