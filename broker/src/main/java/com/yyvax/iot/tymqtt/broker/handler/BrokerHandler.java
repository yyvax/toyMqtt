package com.yyvax.iot.tymqtt.broker.handler;

import com.yyvax.iot.tymqtt.broker.behavior.Processor;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.mqtt.*;
import org.springframework.stereotype.Component;

@Component
public class BrokerHandler extends SimpleChannelInboundHandler<MqttMessage> {

    private Processor mqttProcessor;

    public BrokerHandler(Processor processor) {
        mqttProcessor = processor;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MqttMessage msg) {
         switch (msg.fixedHeader().messageType()) {
             case CONNECT:
                 mqttProcessor.connect().processConnect(ctx.channel(), (MqttConnectMessage) msg);
                 break;
             case CONNACK:
                 break;
             case PUBLISH:
                 mqttProcessor.publish().processPublish(ctx.channel(), (MqttPublishMessage) msg);
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
                 mqttProcessor.subscribe().processSubscribe(ctx.channel(), (MqttSubscribeMessage) msg);
                 break;
             case SUBACK:
                 break;
             case UNSUBSCRIBE:
                 mqttProcessor.unsubscribe().processUnsubscribe(ctx.channel(), (MqttUnsubscribeMessage) msg);
                 break;
             case UNSUBACK:
                 break;
             case PINGREQ:
                mqttProcessor.pingReq().processPingReq(ctx.channel());
                 break;
             case DISCONNECT:
                 break;
             default:
                 break;
         }
    }


}
