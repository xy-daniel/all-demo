package com.daniel.rpc.server.child;

import com.daniel.rpc.server.handler.RequestHandler;
import com.daniel.rpc.server.RpcServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Netty RPC服务端，提供Netty网络服务开启、关闭的能力
 *
 * @author zarlic
 * @date 2021.12.15 20:35
 */
public class NettyRpcServer extends RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyRpcServer.class);

    private Channel channel;

    public NettyRpcServer(int port, String protocol, RequestHandler handler) {
        super(port, protocol, handler);
    }

    @Override
    public void start() {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ChannelRequestHandler());
                        }
                    });
            // 启动服务
            ChannelFuture f = b.bind(port).sync();
            logger.info("Server started successfully.");
            channel = f.channel();
            // 等待服务通道关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放线程组资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void stop() throws IOException {
        this.channel.close();
    }

    private class ChannelRequestHandler extends ChannelInboundHandlerAdapter {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            logger.info("Channel active：" + ctx);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            logger.info("The server receives a message: {}", msg);
            ByteBuf msgBuf = (ByteBuf) msg;
            byte[] req = new byte[msgBuf.readableBytes()];
            msgBuf.readBytes(req);
            byte[] res = handler.handleRequest(req);
            logger.info("Send response：{}", msg);
            ByteBuf respBuf = Unpooled.buffer(res.length);
            respBuf.writeBytes(res);
            ctx.write(respBuf);
        }

        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
            ctx.flush();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            logger.error("Exception occurred：{}", cause.getMessage());
            ctx.close();
        }
    }

}
