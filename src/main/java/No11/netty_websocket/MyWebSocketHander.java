package No11.netty_websocket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

import static io.netty.handler.codec.http.HttpUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpUtil.setContentLength;

public class MyWebSocketHander extends SimpleChannelInboundHandler {

    private WebSocketServerHandshaker handshaker;
    private static ChannelGroup onlineUsers = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);



    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest){
            //第一次握手请求 由http协议承载
            handerHttpRequest(ctx,(FullHttpRequest)msg);
        }else if(msg instanceof WebSocketFrame) {
            handerWebSocketFrame(ctx,(WebSocketFrame)msg);
        }
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    private void handerHttpRequest(ChannelHandlerContext ctx, FullHttpRequest req) {
        //如果请求头没有包含 Upgrad字段 或者他的值不是websocket，则返回400
        if(!req.getDecoderResult().isSuccess()
            ||(!"websocket".equals(req.headers().get("Upgrade")))){
            sendHttpResponse(ctx,req,new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
            return;
        }
        //简单的校验通过，开始构建握手工厂，构建捂手处理类 WebSocketServerHandshaker，
        // 通过它构造握手消息返回客户端，同事将websocket的编码和解码类动态添加到channelPipeline中
        WebSocketServerHandshakerFactory wsFactory=new WebSocketServerHandshakerFactory("ws://localhost:8088/websocket",null,false);
        handshaker=wsFactory.newHandshaker(req);
        if(handshaker==null){
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        }else{
            handshaker.handshake(ctx.channel(),req);
        }
        onlineUsers.add(ctx.channel());

    }

    private void handerWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {

        //判断是否是关闭链路的指令
//        if(frame instanceof CloseWebSocketFrame){
//            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
//        }
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            onlineUsers.remove(ctx.channel());
            return;
        }
        //判断是否是Ping消息
        if(frame instanceof PingWebSocketFrame){
            ctx.channel().write(frame.content().retain());
            return;
        }
        //例子只支持文本消息，不支持二进制消息
        if(!(frame instanceof TextWebSocketFrame)){
            throw new UnsupportedOperationException("111111111");
        }
        //返回应答消息
        String request=((TextWebSocketFrame)frame).text();
        for (Channel onlineUser : onlineUsers) {
            onlineUser.writeAndFlush(new TextWebSocketFrame(request));
//            ctx.channel().writeAndFlush(new TextWebSocketFrame(request));
        }
    }

    private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest req, FullHttpResponse res){
        if(res.getStatus().code()!=200){
            ByteBuf buf= Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            setContentLength(res,res.content().readableBytes());
        }
        //如果是非 keep-Alive 关闭连接
        ChannelFuture future = ctx.channel().writeAndFlush(req);
        if(!isKeepAlive(req)||res.getStatus().code()!=200){
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
