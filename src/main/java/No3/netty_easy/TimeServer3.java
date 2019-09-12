package No3.netty_easy;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

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
            System.out.println(11111);
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
