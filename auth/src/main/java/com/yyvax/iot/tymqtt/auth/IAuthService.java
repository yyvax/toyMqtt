package com.yyvax.iot.tymqtt.auth;

public interface IAuthService {
    boolean check(String username, String password);
}
