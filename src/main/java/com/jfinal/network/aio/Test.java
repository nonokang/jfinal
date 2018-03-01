package com.jfinal.network.aio;

import java.util.Scanner;

/**
 * <b>版权信息:</b> 广州智数信息科技有限公司<br>
 * <b>功能描述:</b> <br>
 * <b>版本历史:</b>
 * 
 * @author wpk | 2017年12月7日 下午11:02:01 |创建
 */
public class Test {
	public static void main(String[] args) throws Exception {
		// 运行服务器
		Server.start();
		// 避免客户端先于服务器启动前执行代码
		Thread.sleep(100);
		// 运行客户端
		Client.start();
		System.out.println("请输入请求消息：");
		Scanner scanner = new Scanner(System.in);
		while (Client.sendMsg(scanner.nextLine()))
			;
	}
}
