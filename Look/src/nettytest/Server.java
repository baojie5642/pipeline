package nettytest;



import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;

public class Server {
	
	private final int port;

	private Server(final int port) {
		super();
		this.port = port;
	}

	public static Server createServer(final int port) {
		Server server = new Server(port);
		return server;
	}

	public void bind() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workeGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		try {
			b.group(bossGroup, workeGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
					.childHandler(new ServerChildChannelHandler());
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workeGroup.shutdownGracefully();
		}
	}

	private static class ServerChildChannelHandler extends ChannelInitializer<SocketChannel> {
		@Override
		protected void initChannel(final SocketChannel ch) throws Exception {
			ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1048576, 0, 4, 0, 4));
			ch.pipeline().addLast(new ByteArrayDecoder());
			ch.pipeline().addLast(ServerHandler.createServerHandler());
		}
	}

	public static void main(String[] args) throws Exception {
		
	}
}
