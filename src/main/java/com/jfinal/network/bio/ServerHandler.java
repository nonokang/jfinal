package com.jfinal.network.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * <b>版权信息:</b> 广州智数信息科技有限公司<br>
 * <b>功能描述:</b> 用于处理一个客户端的Socket链路 <br>
 * <b>版本历史:</b>
 * 
 * @author wpk | 2017年12月7日 下午9:53:22 |创建
 */
public class ServerHandler extends Thread {

	private Socket socket;

	public ServerHandler(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			in = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			String expression;
			String result;
			while (true) {
				// 通过BufferedReader读取一行
				// 如果已经读到输入流尾部，返回null,退出循环
				// 如果得到非空值，就尝试计算结果并返回
				if ((expression = in.readLine()) == null)
					break;
				System.out.println("服务器收到消息：" + expression);
				try {
					result = Calculator.cal(expression).toString();
				} catch (Exception e) {
					result = "计算错误：" + e.getMessage();
				}
				out.println(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 一些必要的清理工作
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
