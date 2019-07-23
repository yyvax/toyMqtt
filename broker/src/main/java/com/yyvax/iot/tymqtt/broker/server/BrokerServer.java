package com.yyvax.iot.tymqtt.broker.server;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import com.yyvax.iot.tymqtt.broker.config.BrokerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.mqtt.MqttDecoder;
import io.netty.handler.codec.mqtt.MqttEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.ClientAuth;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.net.ssl.KeyManagerFactory;
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
    public void start() throws Exception {
        LOGGER.info("Broker started...");
        LOGGER.info("id: {}", brokerConfig.getId());
        LOGGER.info("host: {}", brokerConfig.getHost());
        LOGGER.info("host: {}", brokerConfig.getPort());
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        // sslContext init
        setSslContext();
        // bootstrap
        ServerBootstrap sb = new ServerBootstrap().group(bossGroup, workerGroup);
        sb.channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline channelPipeline = socketChannel.pipeline();
                        // heart beat
                        channelPipeline.addFirst("idle",
                                new IdleStateHandler(0, 0, 60));
                        SSLEngine sslEngine = sslContext.newEngine(socketChannel.alloc());
                        sslEngine.setUseClientMode(false);
                        sslEngine.setNeedClientAuth(false);
                        channelPipeline.addLast("ssl", new SslHandler(sslEngine));
                        channelPipeline.addLast("decoder", new MqttDecoder());
                        channelPipeline.addLast("encoder", MqttEncoder.INSTANCE);
                    }
                }).option(ChannelOption.SO_BACKLOG, 1024);
        channel = sb.bind(brokerConfig.getPort()).sync().channel();
    }

    @PreDestroy
    public void stop() throws InterruptedException {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        channel.closeFuture().sync();
        LOGGER.info("Broker shut down successfully.");
    }

    private void setSslContext() throws Exception {
        KeyStore ks = KeyStore.getInstance("PKCS12");
        String password = "123456";
        InputStream inputStream = BrokerServer.class.getClassLoader().getResourceAsStream("keystore/server.pfx");
        try {
            ks.load(inputStream, password.toCharArray());
            String algorithm = "SunX509";
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(algorithm);
            keyManagerFactory.init(ks, password.toCharArray());
            sslContext = SslContextBuilder.forServer(keyManagerFactory).clientAuth(ClientAuth.NONE).build();
        } catch (KeyStoreException e) {
            LOGGER.error("Cannot get keystore type PKCS12");
        } catch (IOException e) {
            LOGGER.error("Cannot load the keystore file.", e);
        } catch (CertificateException e) {
            LOGGER.error("Cannot get the certificate.", e);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Something wrong with the SSL algorithm.", e);
        } catch (UnrecoverableKeyException e) {
            LOGGER.error("KeyManagerFactory cannot init.");
        }
    }

}
