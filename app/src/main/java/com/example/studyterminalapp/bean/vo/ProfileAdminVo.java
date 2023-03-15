package com.example.studyterminalapp.bean.vo;

public class ProfileAdminVo {
    private Integer aid;

    private String username;

    private String email;

    public ProfileAdminVo() {
    }

    public ProfileAdminVo(Integer aid, String username, String email) {
        this.aid = aid;
        this.username = username;
        this.email = email;
    }

    public Integer getAid() {
        return aid;
    }

    public void setAid(Integer aid) {
        this.aid = aid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
