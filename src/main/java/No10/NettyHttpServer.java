package No10;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class NettyHttpServer {
    public static void main(String[] args) {
        EventLoopGroup mater=new NioEventLoopGroup();
        EventLoopGroup worker=new NioEventLoopGroup();
        try{
            ServerBootstrap server=new ServerBootstrap();
            server.group(mater,worker)
                  .channel(NioServerSocketChannel.class)
                  .option(ChannelOption.SO_BACKLOG,1024)
                  .childHandler(new ChannelInitializer<SocketChannel>() {
                      @Override
                      protected void initChannel(SocketChannel ch) throws Exception {
                          ChannelPipeline pipeline = ch.pipeline();
                          pipeline.addLast("http-decoder",new HttpRequestDecoder());
                          //HttpObjectAggregator 作用是将多个消息转换为单一的FullHttpRequset获取FullHttpRespone
                          pipeline.addLast("http-aggregator",new HttpObjectAggregator(65536));
                          pipeline.addLast("http-encoder",new HttpResponseEncoder());
                          pipeline.addLast("http-chunked",new ChunkedWriteHandler());
                          pipeline.addLast("fileServerHander",new HttpHandler());

                      }
                  });
            ChannelFuture future = server.bind("localhost", 8088).sync();
            System.out.println("服务开启-----------");
            future.channel().closeFuture().sync();
        }catch (Exception e){

        }finally {
            mater.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
