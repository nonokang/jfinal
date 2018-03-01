package com.jfinal.network.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * <b>版权信息:</b> 广州智数信息科技有限公司<br>
 * <b>功能描述:</b> <br>
 * <b>版本历史:</b>
 * 
 * @author wpk | 2017年12月7日 下午11:00:46 |创建
 */
public class ClientWriteHandler implements CompletionHandler<Integer, ByteBuffer> {
	private AsynchronousSocketChannel clientChannel;
	private CountDownLatch latch;

	public ClientWriteHandler(AsynchronousSocketChannel clientChannel,
			CountDownLatch latch) {
		this.clientChannel = clientChannel;
		this.latch = latch;
	}

	public void completed(Integer result, ByteBuffer buffer) {
		// 完成全部数据的写入
		if (buffer.hasRemaining()) {
			clientChannel.write(buffer, buffer, this);
		} else {
			// 读取数据
			ByteBuffer readBuffer = ByteBuffer.allocate(1024);
			clientChannel.read(readBuffer, readBuffer, new ClientReadHandler(
					clientChannel, latch));
		}
	}

	public void failed(Throwable exc, ByteBuffer attachment) {
		System.err.println("数据发送失败...");
		try {
			clientChannel.close();
			latch.countDown();
		} catch (IOException e) {
		}
	}
}
