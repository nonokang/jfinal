package com.jfinal.network.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * <b>版权信息:</b> 广州智数信息科技有限公司<br>
 * <b>功能描述:</b> <br>
 * <b>版本历史:</b>
 * @author  wpk | 2017年12月7日 下午11:00:08 |创建
 */
public class ClientAsyncHandler implements
		CompletionHandler<Void, ClientAsyncHandler>, Runnable {
	private AsynchronousSocketChannel clientChannel;
	private String host;
	private int port;
	private CountDownLatch latch;

	public ClientAsyncHandler(String host, int port) {
		this.host = host;
		this.port = port;
		try {
			// 创建异步的客户端通道
			clientChannel = AsynchronousSocketChannel.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		// 创建CountDownLatch等待
		latch = new CountDownLatch(1);
		// 发起异步连接操作，回调参数就是这个类本身，如果连接成功会回调completed方法
		clientChannel.connect(new InetSocketAddress(host, port), this, this);
		try {
			latch.await();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			clientChannel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 连接服务器成功
	// 意味着TCP三次握手完成
	public void completed(Void result, ClientAsyncHandler attachment) {
		System.out.println("客户端成功连接到服务器...");
	}

	// 连接服务器失败
	public void failed(Throwable exc, ClientAsyncHandler attachment) {
		System.err.println("连接服务器失败...");
		exc.printStackTrace();
		try {
			clientChannel.close();
			latch.countDown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// 向服务器发送消息
	public void sendMsg(String msg) {
		byte[] req = msg.getBytes();
		ByteBuffer writeBuffer = ByteBuffer.allocate(req.length);
		writeBuffer.put(req);
		writeBuffer.flip();
		// 异步写
		clientChannel.write(writeBuffer, writeBuffer, new ClientWriteHandler(
				clientChannel, latch));
	}
}
