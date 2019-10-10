package No7.messagepack;

import No3.netty_easy.NettyTimeServerHander;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeServer3 {
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
            ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535,0,
                    2,0,2));
            ch.pipeline().addLast(new MsgPackDecoder());
            ch.pipeline().addLast(new LengthFieldPrepender(2));
            ch.pipeline().addLast(new MsgPackEncoder());
             ch.pipeline().addLast(new MessagePackHander());
        }
    }


    public static void main(String[] args) {
        try {
            new TimeServer3().bind(8080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
