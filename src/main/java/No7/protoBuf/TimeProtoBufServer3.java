package No7.protoBuf;

import No7.messagepack.MessagePackHander;
import No7.messagepack.MsgPackDecoder;
import No7.messagepack.MsgPackEncoder;
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
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class TimeProtoBufServer3 {
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
            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
            ch.pipeline()
                    .addLast(
                            new ProtobufDecoder(
                                    PersonModel.Person.getDefaultInstance()));
            ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
            ch.pipeline().addLast(new ProtobufEncoder());
             ch.pipeline().addLast(new MyProtoBufHander());
        }
    }


    public static void main(String[] args) {
        try {
            new TimeProtoBufServer3().bind(8080);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
