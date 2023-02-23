package com.example.studyterminalapp.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import lombok.Data;

@Data
public class StudentBean implements Serializable {

    private static final long serialVersionUID = 1L; //序列化时保持ShopBean类版本的兼容性

    @JsonProperty("username")
    private String username;
    @JsonProperty("className")
    private String email;
    @JsonProperty("studentName")
    private String studentName;
    @JsonProperty("studentNumber")
    private String studentNumber;
    @JsonProperty("school")
    private String school;
    @JsonProperty("profilePicUrl")
    private String profilePicUrl;

    public StudentBean() {
    }

    public StudentBean(String username, String email, String studentName, String studentNumber,
                       String school, String profilePicUrl) {
        this.username = username;
        this.email = email;
        this.studentName = studentName;
        this.studentNumber = studentNumber;
        this.school = school;
        this.profilePicUrl = profilePicUrl;
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

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
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

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    @Override
    public String toString() {
        return "StudentBean{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", studentName='" + studentName + '\'' +
                ", studentNumber='" + studentNumber + '\'' +
                ", school='" + school + '\'' +
                ", profilePicUrl='" + profilePicUrl + '\'' +
                '}';
    }
}
