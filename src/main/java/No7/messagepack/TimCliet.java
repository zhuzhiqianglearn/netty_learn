package No7.messagepack;

import No3.netty_easy.NettyTimeClientHander;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
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
                             ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(65535,0,
                                     2,0,2));
                             ch.pipeline().addLast(new MsgPackDecoder());
                             ch.pipeline().addLast(new LengthFieldPrepender(2));
                             ch.pipeline().addLast(new MsgPackEncoder());
                             ch.pipeline().addLast(new MessagePackEncoderHander());
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
