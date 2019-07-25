package com.yyvax.iot.tymqtt.broker.handler;

import com.yyvax.iot.tymqtt.broker.mqttbehavior.Processor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.MqttConnectMessage;
import io.netty.handler.codec.mqtt.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;

public class BrokerHandler extends SimpleChannelInboundHandler<MqttMessage> {

    @Autowired
    Processor mqttProcessor;

    protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) throws Exception {
         switch (msg.fixedHeader().messageType()) {
             case CONNECT:
                 mqttProcessor.connect().processConnect(ctx.channel(), (MqttConnectMessage) msg);
                 break;
             case CONNACK:
                 break;
             case PUBLISH:
                 break;
             case PUBACK:
                 break;
             case PUBREC:
                 break;
             case PUBREL:
                 break;
             case PUBCOMP:
                 break;
             case SUBSCRIBE:
                 break;
             case SUBACK:
                 break;
             case UNSUBSCRIBE:
                 break;
             case UNSUBACK:
                 break;
             case PINGREQ:
                 break;
             case PINGRESP:
                 break;
             case DISCONNECT:
                 break;
             default:
                 break;
         }
    }


}
