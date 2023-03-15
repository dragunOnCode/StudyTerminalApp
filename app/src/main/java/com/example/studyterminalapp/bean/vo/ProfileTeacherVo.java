package com.example.studyterminalapp.bean.vo;

public class ProfileTeacherVo {
    private Integer tid;

    private String username;

    private String email;

    private String gender;

    private String nickname;

    private String school;


    private String profileImg;

    public ProfileTeacherVo() {
    }

    public ProfileTeacherVo(Integer tid, String username, String email, String gender, String nickname, String school, String profileImg) {
        this.tid = tid;
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.nickname = nickname;
        this.school = school;
        this.profileImg = profileImg;
    }

    @Override
    public String toString() {
        return "ProfileTeacherVo{" +
                "tid=" + tid +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", nickname='" + nickname + '\'' +
                ", school='" + school + '\'' +
                ", profileImg='" + profileImg + '\'' +
                '}';
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

}
