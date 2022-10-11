package com.daniel.rpc.common.net.impl;

import com.daniel.rpc.common.discoverer.ServiceInfo;
import com.daniel.rpc.common.net.NetClient;
import com.daniel.rpc.common.net.handler.SendHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Netty网络客户端
 *
 * @author daniel
 */
public class NettyNetClient implements NetClient {

    private static final Logger logger = LoggerFactory.getLogger(NettyNetClient.class);

    @Override
    public byte[] sendRequest(byte[] data, ServiceInfo serviceInfo) {
        String[] addressInfoArray = serviceInfo.getAddress().split(":");
        SendHandler sendHandler = new SendHandler(data);
        byte[] respData = null;
        //配置客户端
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(sendHandler);
                        }
                    });
            //启动客户端连接
            bootstrap.connect(addressInfoArray[0], Integer.parseInt(addressInfoArray[1])).sync();
            respData = (byte[]) sendHandler.rspData();
            logger.info("send request get reply:" + Arrays.toString(respData));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
        return respData;
    }
}
