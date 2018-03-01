package com.jfinal.network.aio;

/**
 * <b>版权信息:</b> 广州智数信息科技有限公司<br>
 * <b>功能描述:</b> AIO服务端<br>
 * <b>版本历史:</b>
 * 
 * @author wpk | 2017年12月7日 下午10:49:18 |创建
 */
public class Server {
	private static int DEFAULT_PORT = 12345;
	private static ServerAsyncHandler serverHandle;
	public volatile static long clientCount = 0;

	public static void start() {
		start(DEFAULT_PORT);
	}

	public static synchronized void start(int port) {
		if (serverHandle != null)
			return;
		serverHandle = new ServerAsyncHandler(port);
		new Thread(serverHandle, "Server").start();
	}

	public static void main(String[] args) {
		Server.start();
	}
}
