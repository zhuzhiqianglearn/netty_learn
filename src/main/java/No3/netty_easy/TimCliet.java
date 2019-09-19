package No3.netty_easy;

import No2.BIO.TimeClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimCliet {

    public void connect(int port,String host) throws Exception {
        EventLoopGroup group=new NioEventLoopGroup();
        try {
            Bootstrap bootstrap=new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                     .option(ChannelOption.TCP_NODELAY,true)
                     .handler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         protected void initChannel(SocketChannel ch) throws Exception {
                             //添加handler 拆包粘包
                             ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                             ch.pipeline().addLast(new StringDecoder());
                             ch.pipeline().addLast(new NettyTimeClientHander());
                         }
                     });
            //发起异步链接操作
            ChannelFuture sync = bootstrap.connect(host, port).sync();
            sync.channel().closeFuture().sync();
        }finally {
             group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new TimCliet().connect(8080,"localhost");
    }
}
