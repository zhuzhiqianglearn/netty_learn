package No3.netty_easy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
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
            //添加handler 拆包粘包
            ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
            ch.pipeline().addLast(new StringDecoder());
             ch.pipeline().addLast(new NettyTimeServerHander());
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
