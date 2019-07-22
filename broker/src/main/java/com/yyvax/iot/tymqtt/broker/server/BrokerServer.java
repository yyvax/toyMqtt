package com.yyvax.iot.tymqtt.broker.server;

import com.yyvax.iot.tymqtt.broker.config.BrokerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.net.ssl.SSLEngine;

@Component
public class BrokerServer {
    private static Logger LOGGER = LoggerFactory.getLogger(BrokerServer.class);

    @Autowired
    private BrokerConfig brokerConfig;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private Channel channel;

    private SslContext sslContext;

    @PostConstruct
    public void start() throws InterruptedException {
        LOGGER.info("Broker started...");
        LOGGER.info("id: {}", brokerConfig.getId());
        LOGGER.info("host: {}", brokerConfig.getHost());
        LOGGER.info("host: {}", brokerConfig.getPort());
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        // bootstrap
        ServerBootstrap sb = new ServerBootstrap().group(bossGroup, workerGroup);
        sb.channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline channelPipeline = socketChannel.pipeline();
                        // Netty提供的心跳检测
                        channelPipeline.addFirst("idle",
                                new IdleStateHandler(0, 0, 60));
                        // Netty提供的SSL处理
                        SSLEngine sslEngine = sslContext.newEngine(socketChannel.alloc());
                        sslEngine.setUseClientMode(false);        // 服务端模式
                        sslEngine.setNeedClientAuth(false);        // 不需要验证客户端
                        channelPipeline.addLast("ssl", new SslHandler(sslEngine));
                        channelPipeline.addLast("decoder", new MqttDecoder());
                        channelPipeline.addLast("encoder", MqttEncoder.INSTANCE);
                    }
                });
        channel = sb.bind(brokerConfig.getPort()).sync().channel();
    }

    @PreDestroy
    public void stop() throws InterruptedException {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        channel.closeFuture().sync();
        LOGGER.info("Broker shut down successfully.");
    }


}
