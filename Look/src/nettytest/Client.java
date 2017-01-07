package nettytest;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.bytes.ByteArrayEncoder;


public class Client {

	private final LittleMessage littleMessage;

	private Client(final LittleMessage littleMessage) {
		super();
		this.littleMessage = littleMessage;
	}

	public static Client createClient(final LittleMessage littleMessage) {
		Client client = new Client(littleMessage);
		return client;
	}

	public void connect(final int port, final String host) throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		try {
			b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
					.handler(new ClientChildChannelHandler());
			ChannelFuture f = b.connect(host, port).sync();
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}

	private class ClientChildChannelHandler extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(final SocketChannel ch) throws Exception {
			ch.pipeline().addLast(new LengthFieldPrepender(4));
			ch.pipeline().addLast(new ByteArrayEncoder());
			ch.pipeline().addLast(ClientHandler.ctrateClientHandler(littleMessage));
		}
	}

}
