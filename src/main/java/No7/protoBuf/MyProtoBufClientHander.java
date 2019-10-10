package No7.protoBuf;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;
public class MyProtoBufClientHander  extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        PersonModel.Person.Builder builder=PersonModel.Person.newBuilder();
        builder.setId(1);
        builder.setEmail("3530****@qq.com");
        builder.setName("zzq");
        ctx.writeAndFlush(builder.build());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PersonModel.Person person= (PersonModel.Person) msg;
        System.out.println(person.toString());
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
