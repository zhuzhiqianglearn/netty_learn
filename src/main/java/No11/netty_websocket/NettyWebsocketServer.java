package No11.netty_websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class NettyWebsocketServer{
    public void run(int port) throws Exception{
        EventLoopGroup master=new NioEventLoopGroup();
        EventLoopGroup worker=new NioEventLoopGroup();
        try {
            ServerBootstrap server=new ServerBootstrap();
            server.group(master,worker)
                  .channel(NioServerSocketChannel.class)
                  .option(ChannelOption.SO_BACKLOG,1024)
                  .childHandler(new ChannelInitializer<SocketChannel>() {
                      @Override
                      protected void initChannel(SocketChannel ch) throws Exception {
                          ChannelPipeline pipeline = ch.pipeline();
                          pipeline.addLast("http-codec",new HttpServerCodec());
                          pipeline.addLast("aggregator",new HttpObjectAggregator(65536));
                          pipeline.addLast("http-chunked",new ChunkedWriteHandler());
//                          pipeline.addLast(new WebSocketServerProtocolHandler("/im"));
                          pipeline.addLast("myWebSocketHander",new MyWebSocketHander());
                      }
                  });
            ChannelFuture sync = server.bind(port).sync();
            System.out.println("服务开始：端口："+port);
            sync.channel().closeFuture().sync();
        }finally {
            master.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyWebsocketServer().run(8088);
    }
}
