package No7.messagepack;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

public class MessagePackEncoderHander extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
          User[] users=new User[10];
        for (int i = 0; i < 10; i++) {
//            users[i]=new User(i*10,"name"+i);
            ctx.writeAndFlush(new User(i*10,"name"+i));
//            User user=new User();
//            user.setAge(i);
//            user.setName("name"+i);
//            ctx.writeAndFlush(user);
//            ctx.writeAndFlush();
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(111);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
