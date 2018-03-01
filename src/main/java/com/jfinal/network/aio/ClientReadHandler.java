package com.jfinal.network.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * <b>版权信息:</b> 广州智数信息科技有限公司<br>
 * <b>功能描述:</b> <br>
 * <b>版本历史:</b>
 * 
 * @author wpk | 2017年12月7日 下午11:01:31 |创建
 */
public class ClientReadHandler implements CompletionHandler<Integer, ByteBuffer> {
	private AsynchronousSocketChannel clientChannel;
	private CountDownLatch latch;

	public ClientReadHandler(AsynchronousSocketChannel clientChannel,
			CountDownLatch latch) {
		this.clientChannel = clientChannel;
		this.latch = latch;
	}

	public void completed(Integer result, ByteBuffer buffer) {
		buffer.flip();
		byte[] bytes = new byte[buffer.remaining()];
		buffer.get(bytes);
		String body;
		try {
			body = new String(bytes, "UTF-8");
			System.out.println("客户端收到结果:" + body);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void failed(Throwable exc, ByteBuffer attachment) {
		System.err.println("数据读取失败...");
		try {
			clientChannel.close();
			latch.countDown();
		} catch (IOException e) {
		}
	}
}
