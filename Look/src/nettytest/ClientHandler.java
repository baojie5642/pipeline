package nettytest;


import java.util.concurrent.atomic.AtomicBoolean;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class ClientHandler extends SimpleChannelInboundHandler {

	private final LittleMessage littleMessage;
	public static final String receiveFromServer = "success";
	private final AtomicBoolean isChannelGood = new AtomicBoolean(false);

	private ClientHandler(final LittleMessage littleMessage) {
		super();
		this.littleMessage = littleMessage;
	}

	public static ClientHandler ctrateClientHandler(final LittleMessage littleMessage) {
		ClientHandler clientHandler = new ClientHandler(littleMessage);
		return clientHandler;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		byte[] bytes = ObjectToByte.toByte(littleMessage);
		ByteBuf byteBuf = Unpooled.copiedBuffer(bytes);
		try {
			ctx.writeAndFlush(byteBuf);
		} finally {
			if (null != byteBuf) {
				byteBuf.clear();
				byteBuf.release();
				byteBuf = null;
			}
			bytes = null;
		}
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		final byte[] bytes = new byte[buf.readableBytes()];
		try {
			buf.readBytes(bytes);
		} finally {
			if (null != buf) {
				buf.clear();
				buf.release();
				buf = null;
			}
		}
		final String receiveFromServer = new String(bytes, "UTF-8");
		boolean isSuccess = false;
		if (receiveFromServer.equals(receiveFromServer)) {
			isSuccess = true;
		} else {
			isSuccess = false;
		}
		ChannelFuture channelFuture = ctx.close();
		channelFuture.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) {
				isChannelGood.set(true);
			}
		});
		if (isSuccess && isChannelGood.get()&&channelFuture.isSuccess()) {
			System.out.println("消息成功发送，可以删除消息");
		} else {
			System.out.println("消息发送失败，要重新发送");
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("Client  :  exceptionCaught !");
		cause.printStackTrace();
		ctx.close();
	}
}
