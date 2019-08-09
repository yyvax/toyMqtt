package com.yyvax.iot.tymqtt.broker.behavior;

import com.yyvax.iot.tymqtt.broker.persistence.session.ClientSession;
import com.yyvax.iot.tymqtt.broker.persistence.session.ClientSessionStore;
import com.yyvax.iot.tymqtt.broker.util.ClientUtil;
import com.yyvax.iot.tymqtt.common.SnowFlake;
import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.*;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Connect {
    private static final Logger LOGGER = LoggerFactory.getLogger(Connect.class);

    private ClientSessionStore clientSessionStore;

    public Connect(ClientSessionStore clientSessionStore) {
        this.clientSessionStore = clientSessionStore;
    }

    public void processConnect(Channel channel, MqttConnectMessage connectMessage) {
        LOGGER.info("Start connecting to broker");
        boolean cleanSession = connectMessage.variableHeader().isCleanSession();
        boolean sessionPresent = !cleanSession;
        // TODO: clean session, will msg, retain msg, username & password
        // support mqtt 3.1 and 3.1.1 , 3.1.1 --> 4, 3.1 --> 3
        if (connectMessage.variableHeader().version() != 4 && connectMessage.variableHeader().version() != 3) {
            LOGGER.error("Unsupported mqtt version!");
            write(channel, MqttConnectReturnCode.CONNECTION_REFUSED_UNACCEPTABLE_PROTOCOL_VERSION);
            channel.close();
            return;
        }
        // check client id
        String clientId = connectMessage.payload().clientIdentifier();
        if (!ClientUtil.checkClientId(clientId)) {
            LOGGER.error("Invalid client id!");
            write(channel, MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED);
            channel.close();
            return;
        }
        // [MQTT-3.1.3-7] If the Client supplies a zero-byte ClientId, the Client MUST also set CleanSession to 1.
        // [MQTT-3.1.3-8] If the Client supplies a zero-byte ClientId with CleanSession set to 0,
        // the Server MUST respond to the CONNECT Packet with a CONNACK return code 0x02 (Identifier rejected)
        // and then close the Network Connection.
        if (clientId.length() == 0) {
            if (!cleanSession) {
                LOGGER.error("When client supplies a 0-byte clientId, the client must also set cleanSession to 1.");
                write(channel, MqttConnectReturnCode.CONNECTION_REFUSED_IDENTIFIER_REJECTED);
                channel.close();
                return;
            } else {
                SnowFlake snowFlake = new SnowFlake(2L, 1L);
                clientId = String.valueOf(snowFlake.nextId());
                LOGGER.info("Assign a new id {} to the client.", clientId);
            }
        }

        // [MQTT-3.1.3-11] If the User Name Flag is set to 1, this is the next field in the payload.
        // The User Name MUST be a UTF-8 encoded string
        if (connectMessage.variableHeader().hasUserName()) {
            // TODO: username auth
        }
        channel.attr(AttributeKey.valueOf("clientId")).set(connectMessage.payload().clientIdentifier());
        ClientSession clientSession = new ClientSession(clientId, channel, cleanSession);
        clientSessionStore.put(clientId, clientSession);
        // return connAck msg
        write(channel, MqttConnectReturnCode.CONNECTION_ACCEPTED, sessionPresent);
        LOGGER.info("Client {} connected.", clientSessionStore.get(clientId).getClientId());
    }

    private void write(Channel channel, MqttConnectReturnCode connectReturnCode) {
        write(channel, connectReturnCode, false);
    }

    private void write(Channel channel, MqttConnectReturnCode connectReturnCode, boolean sessionPresent) {
        MqttConnAckMessage connAckMessage =
                createConnAckMsg(connectReturnCode, sessionPresent);
        channel.writeAndFlush(connAckMessage);
    }

    private MqttConnAckMessage createConnAckMsg(MqttConnectReturnCode connectReturnCode, boolean sessionPresent) {
        MqttFixedHeader mqttFixedHeader = new MqttFixedHeader(MqttMessageType.CONNACK, false,
                MqttQoS.AT_MOST_ONCE, false, 0);
        MqttConnAckVariableHeader variableHeader = new MqttConnAckVariableHeader(connectReturnCode, sessionPresent);
        return new MqttConnAckMessage(mqttFixedHeader, variableHeader
        );
    }

}
