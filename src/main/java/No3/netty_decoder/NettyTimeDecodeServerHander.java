package No3.netty_decoder;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyTimeDecodeServerHander extends ChannelInboundHandlerAdapter {
    int count=1;
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf buf=(ByteBuf)msg;
//        byte[] req = new byte[buf.readableBytes()];
//        buf.readBytes(req);
        String body=(String) msg;
        System.out.println("client:"+msg+"====="+count);
        count++;
        ctx.writeAndFlush(Unpooled.copiedBuffer((System.currentTimeMillis()+"").getBytes()));

    }
}