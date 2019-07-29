package com.yyvax.iot.toymqtt.broker;

import com.yyvax.iot.tymqtt.broker.util.ClientUtil;
import org.junit.Assert;
import org.junit.Test;

public class ClientUtilTest {
    @Test
    public void testValidClientId() {
        String clientId = "abc123FFF";
        Assert.assertTrue(ClientUtil.checkClientId(clientId));
    }

    @Test
    public void testInvalidClientId() {
        String invalid = "123--ff..";
        Assert.assertFalse(ClientUtil.checkClientId(invalid));
    }

    @Test
    public void testZeroByteClientId() {
        String nullId = "";
        Assert.assertTrue(ClientUtil.checkClientId(nullId));
    }
}
