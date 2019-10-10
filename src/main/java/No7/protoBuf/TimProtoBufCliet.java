package No7.protoBuf;

import No7.messagepack.MessagePackEncoderHander;
import No7.messagepack.MsgPackDecoder;
import No7.messagepack.MsgPackEncoder;
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
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class TimProtoBufCliet {

    public void connect(int port,String host) throws Exception {
        EventLoopGroup group=new NioEventLoopGroup();
        try {
            Bootstrap bootstrap=new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                     .option(ChannelOption.TCP_NODELAY,true)
                     .handler(new ChannelInitializer<SocketChannel>() {
                         @Override
                         protected void initChannel(SocketChannel ch) throws Exception {
                             ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
                             ch.pipeline()
                                     .addLast(
                                             new ProtobufDecoder(
                                                     PersonModel.Person.getDefaultInstance()));
                             ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
                             ch.pipeline().addLast(new ProtobufEncoder());
                             ch.pipeline().addLast(new MyProtoBufClientHander());
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
        new TimProtoBufCliet().connect(8080,"localhost");
    }
}
