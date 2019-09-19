package No3.netty_decoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyTimeDecodeClientHander extends ChannelInboundHandlerAdapter {
    private byte[] req;

    public NettyTimeDecodeClientHander() {
        req = ("QUERY TIME ORDER"+System.getProperty("line.separator")).getBytes();
    }

    int count=1;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        ByteBuf message = null;
//        for (int i = 0; i < 100; i++) {
//            message = Unpooled.buffer(req.length);
//            message.writeBytes(req);
//            ctx.writeAndFlush(message);
//        }
        ByteBuf buf=null;
                    byte[] req1="abc".getBytes();

        for (int i = 0; i < 100; i++) {
//            byte[] req1=(System.currentTimeMillis() + "").getBytes();
            buf=Unpooled.buffer(req.length);
            buf.writeBytes(req);
            ctx.writeAndFlush(buf);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf buf=(ByteBuf)msg;
//        byte[] req = new byte[buf.readableBytes()];
//        buf.readBytes(req);
//        System.out.println("server:"+new String(req,"UTF-8")+"---"+count);
//        count++;
//        ctx.write(Unpooled.copiedBuffer((System.currentTimeMillis()+"").getBytes()));
        String body=(String) msg;
        System.out.println("server:"+body);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    public static void main(String[] args) {
        String property = System.getProperty("line.separator");
        System.out.println(property);
    }
}
