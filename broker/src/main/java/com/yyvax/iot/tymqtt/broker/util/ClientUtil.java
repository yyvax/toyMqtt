package com.yyvax.iot.tymqtt.broker.util;

import java.util.regex.Pattern;

public class ClientUtil {
    public static boolean checkClientId(String clientId) {
        return Pattern.matches("[a-zA-Z1-9]*\\.?", clientId);
    }
}
