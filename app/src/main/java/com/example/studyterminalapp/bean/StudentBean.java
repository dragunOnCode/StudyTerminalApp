package com.example.studyterminalapp.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import lombok.Data;

@Data
public class StudentBean implements Serializable {

    private static final long serialVersionUID = 1L; //序列化时保持ShopBean类版本的兼容性


    @JsonProperty("uid")
    private Integer uid;
    @JsonProperty("username")
    private String username;
    @JsonProperty("email")
    private String email;
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("gender")
    private String gender;
    @JsonProperty("studentNumber")
    private String studentNumber;
    @JsonProperty("school")
    private String school;
    @JsonProperty("profileImg")
    private String profileImg;

    public StudentBean() {
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public StudentBean(Integer uid, String username, String email, String nickname, String gender,
                       String studentNumber, String school, String profileImg) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
        this.studentNumber = studentNumber;
        this.school = school;
        this.profileImg = profileImg;
    }

    @Override
    public String toString() {
        return "StudentBean{" +
                "uid=" + uid +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", gender='" + gender + '\'' +
                ", studentNumber=" + studentNumber +
                ", school='" + school + '\'' +
                ", profileImg='" + profileImg + '\'' +
                '}';
    }
}
