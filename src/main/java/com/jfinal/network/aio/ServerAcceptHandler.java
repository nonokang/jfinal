package com.jfinal.network.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * <b>版权信息:</b> 广州智数信息科技有限公司<br>
 * <b>功能描述:</b> 接收客户端<br>
 * <b>版本历史:</b>
 * 
 * @author wpk | 2017年12月7日 下午10:51:00 |创建
 */
public class ServerAcceptHandler implements CompletionHandler<AsynchronousSocketChannel, ServerAsyncHandler> {
	
	public void completed(AsynchronousSocketChannel channel,
			ServerAsyncHandler serverHandler) {
		// 继续接受其他客户端的请求
		Server.clientCount++;
		System.out.println("连接的客户端数：" + Server.clientCount);
		serverHandler.channel.accept(serverHandler, this);
		// 创建新的Buffer
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		// 异步读 第三个参数为接收消息回调的业务Handler
		channel.read(buffer, buffer, new ServerReadHandler(channel));
	}

	public void failed(Throwable exc, ServerAsyncHandler serverHandler) {
		exc.printStackTrace();
		serverHandler.latch.countDown();
	}
}
