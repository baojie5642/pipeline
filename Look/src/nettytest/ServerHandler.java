package nettytest;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler {
	
	private ServerHandler(){
		super();
	}
	public static ServerHandler createServerHandler(){
		ServerHandler serverHandler=new ServerHandler();
		return serverHandler;
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		System.out.println("channelActive");
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, Object object) throws Exception {
		ByteBuf byteBuf=(ByteBuf)object;
		byte[] bytes=new byte[byteBuf.readableBytes()];
		byteBuf.readBytes(bytes);
		final LittleMessage littleMessage=ByteToObject.toObject(LittleMessage.class, bytes);
		if (null==littleMessage) {
			throw new NullPointerException("对象反序列化失败。");
		}
		final MyChannel myChannel=MyChannel.create(ctx, littleMessage.getMessageID());
		final MyTask myTask=MyTask.create(littleMessage.getMessageID(), littleMessage.getRequest());
		//具体如何启动由你决定
		MyCache.MyCache.put(littleMessage.getMessageID(), myChannel);
		MyCache.CacheThreadPoolForWork.submit(myTask);
		myChannel.startMonitor();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// 一个连接调用一次，在最后,链接断了还是会打印
		System.out.println("channelInactive");
		//ctx.close();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		// 每次read结束都会调用
		 System.out.println("传输结束channelReadComplete");
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("Server  :  exceptionCaught !");
		cause.printStackTrace();
		ctx.close();
	}
}
