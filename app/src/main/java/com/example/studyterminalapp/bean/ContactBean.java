package com.example.studyterminalapp.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ContactBean implements Serializable {

    private static final long serialVersionUID = 1L; //序列化时保持ShopBean类版本的兼容性

    @JsonProperty("receiver")
    private String receiver;
    @JsonProperty("telephone")
    private String telephone;
    @JsonProperty("address")
    private String address;

    public ContactBean() {
    }

    public ContactBean(String receiver, String telephone, String address) {
        this.receiver = receiver;
        this.telephone = telephone;
        this.address = address;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
