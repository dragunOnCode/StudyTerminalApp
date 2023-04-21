package com.example.studyterminalapp.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SaTokenInfo implements Serializable {
    private static final long serialVersionUID = 1L; //序列化时保持ShopBean类版本的兼容性

    @JsonProperty("tokenName")
    private String tokenName;
    @JsonProperty("tokenValue")
    private String tokenValue;
    @JsonProperty("isLogin")
    private Boolean isLogin;
    @JsonProperty("loginId")
    private String loginId;
    @JsonProperty("loginType")
    private String loginType;
    @JsonProperty("tokenTimeout")
    private Integer tokenTimeout;
    @JsonProperty("sessionTimeout")
    private Integer sessionTimeout;
    @JsonProperty("tokenSessionTimeout")
    private Integer tokenSessionTimeout;
    @JsonProperty("tokenActivityTimeout")
    private Integer tokenActivityTimeout;
    @JsonProperty("loginDevice")
    private String loginDevice;
    @JsonProperty("tag")
    private Object tag;

    public String getTokenName() {
        return tokenName;
    }

    public void setTokenName(String tokenName) {
        this.tokenName = tokenName;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public Boolean isIsLogin() {
        return isLogin;
    }

    public void setIsLogin(Boolean isLogin) {
        this.isLogin = isLogin;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public Integer getTokenTimeout() {
        return tokenTimeout;
    }

    public void setTokenTimeout(Integer tokenTimeout) {
        this.tokenTimeout = tokenTimeout;
    }

    public Integer getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(Integer sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public Integer getTokenSessionTimeout() {
        return tokenSessionTimeout;
    }

    public void setTokenSessionTimeout(Integer tokenSessionTimeout) {
        this.tokenSessionTimeout = tokenSessionTimeout;
    }

    public Integer getTokenActivityTimeout() {
        return tokenActivityTimeout;
    }

    public void setTokenActivityTimeout(Integer tokenActivityTimeout) {
        this.tokenActivityTimeout = tokenActivityTimeout;
    }

    public String getLoginDevice() {
        return loginDevice;
    }

    public void setLoginDevice(String loginDevice) {
        this.loginDevice = loginDevice;
    }

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "SaTokenInfo{" +
                "tokenName='" + tokenName + '\'' +
                ", tokenValue='" + tokenValue + '\'' +
                ", isLogin=" + isLogin +
                ", loginId='" + loginId + '\'' +
                ", loginType='" + loginType + '\'' +
                ", tokenTimeout=" + tokenTimeout +
                ", sessionTimeout=" + sessionTimeout +
                ", tokenSessionTimeout=" + tokenSessionTimeout +
                ", tokenActivityTimeout=" + tokenActivityTimeout +
                ", loginDevice='" + loginDevice + '\'' +
                ", tag=" + tag +
                '}';
    }
}
