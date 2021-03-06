package No3.netty_decoder;

import No3.netty_easy.NettyTimeClientHander;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimDecoderCliet {

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
                             ByteBuf delimiter= Unpooled.copiedBuffer("$_".getBytes());
                             ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024,delimiter));
                            ch.pipeline().addLast(new StringDecoder());
                             ch.pipeline().addLast(new NettyTimeDecodeClientHander());
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
        new TimDecoderCliet().connect(8080,"localhost");
    }
}
