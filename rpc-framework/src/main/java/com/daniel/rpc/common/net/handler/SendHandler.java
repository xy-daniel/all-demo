package com.daniel.rpc.common.net.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * netty客户端处理器
 * @author daniel
 */
public class SendHandler extends ChannelInboundHandlerAdapter {

    private CountDownLatch cdl = null;
    private Object readMsg = null;
    private byte[] data;

    public SendHandler(byte[] data) {
        cdl = new CountDownLatch(1);
        this.data = data;
    }

    /**
     * 连接服务器成功后发送请求数据
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("成功连接到服务端:ctx="+ctx);
        ByteBuf byteBuf = Unpooled.buffer(data.length);
        byteBuf.writeBytes(data);
        System.out.println("客户端发送请求信息:" + byteBuf);
        ctx.writeAndFlush(byteBuf);
    }

    /**
     * 读取响应数据
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("客户端读取响应信息:" + msg);
        ByteBuf msgBuf = (ByteBuf) msg;
        byte[] resp = new byte[msgBuf.readableBytes()];
        msgBuf.readBytes(resp);
        readMsg = resp;
        cdl.countDown();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        System.out.println("产生异常,异常信息为:" + cause.getMessage());
        ctx.close();
    }

    /**
     * 等待读取数据完成
     */
    public Object rspData() throws InterruptedException {
        cdl.await();
        return readMsg;
    }
}
