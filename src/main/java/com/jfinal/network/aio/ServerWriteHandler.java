package com.jfinal.network.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * <b>版权信息:</b> 广州智数信息科技有限公司<br>
 * <b>功能描述:</b> <br>
 * <b>版本历史:</b>
 * @author  wpk | 2017年12月7日 下午10:54:51 |创建
 */
public class ServerWriteHandler implements CompletionHandler<Integer, ByteBuffer>{
	// 用于读取半包消息和发送应答
	private AsynchronousSocketChannel channel;
	
	public ServerWriteHandler(AsynchronousSocketChannel channel){
		this.channel = channel;
	}

	public void completed(Integer result, ByteBuffer buffer) {
		// 如果没有发送完，就继续发送直到完成
		if (buffer.hasRemaining())
			channel.write(buffer, buffer, this);
		else {
			// 创建新的Buffer
			ByteBuffer readBuffer = ByteBuffer.allocate(1024);
			// 异步读 第三个参数为接收消息回调的业务Handler
			channel.read(readBuffer, readBuffer,
					new ServerReadHandler(channel));
		}
	}

	public void failed(Throwable exc, ByteBuffer attachment) {
		try {
			channel.close();
		} catch (IOException e) {
		}
	}
}
