package com.jfinal.network.bio;

import java.io.IOException;
import java.util.Random;

/**
 * <b>版权信息:</b> 广州智数信息科技有限公司<br>
 * <b>功能描述:</b> 测试<br>
 * <b>版本历史:</b>
 * 
 * @author wpk | 2017年12月7日 下午9:58:09 |创建
 */
public class Test {
	// 测试主方法
	public static void main(String[] args) throws InterruptedException {
		// 运行服务器
		new Thread(new Runnable() {
			public void run() {
				try {
					Server.start();//BIO原始处理
					
//					ServerThreadPool.start();//BIO优化（解决创建多线程消耗性能问题）
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
		// 避免客户端先于服务器启动前执行代码
		Thread.sleep(100);
		// 运行客户端
		final char operators[] = { '+', '-', '*', '/' };
		final Random random = new Random(System.currentTimeMillis());
		new Thread(new Runnable() {
			@SuppressWarnings("static-access")
			public void run() {
				while (true) {
					// 随机产生算术表达式
					String expression = random.nextInt(10) + ""
							+ operators[random.nextInt(4)]
							+ (random.nextInt(10) + 1);
					Client.send(expression);
					try {
						Thread.currentThread().sleep(random.nextInt(5000));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
