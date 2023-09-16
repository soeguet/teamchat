package com.soeguet.properties;

import sun.swing.StringUIClientPropertyKey;

public class CustomUserProperties {

    private String username;
    private String nickname;

    public CustomUserProperties() {


    }

    public CustomUserProperties(String username) {

        this.username = username;
    }


    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getNickname() {

        return nickname;
    }

    public void setNickname(String nickname) {

        this.nickname = nickname;
    }
}
