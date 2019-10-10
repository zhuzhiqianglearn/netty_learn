package No3.netty_decoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeFixDecoderServer {
    public void bind(int port) throws Exception{
        EventLoopGroup boss=new NioEventLoopGroup();
        EventLoopGroup work=new NioEventLoopGroup();
        try {
            ServerBootstrap b=new ServerBootstrap();
            b.group(boss,work)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childHandler(new ChildHander());
            ChannelFuture f = b.bind(port).sync();
            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        }finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }

    }

    private class ChildHander extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            //添加handler 拆包粘包
            ch.pipeline().addLast(new FixedLengthFrameDecoder(20));
            ch.pipeline().addLast(new StringDecoder());
            ch.pipeline().addLast(new NettyTimeDecodeServerHander());
        }
    }


    public static void main(String[] args) {
        try {
            new TimeFixDecoderServer().bind(8080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
