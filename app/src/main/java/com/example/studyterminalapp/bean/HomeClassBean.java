package com.example.studyterminalapp.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

import lombok.Data;

@Data
public class HomeClassBean implements Serializable {

    private static final long serialVersionUID = 1L; //序列化时保持ShopBean类版本的兼容性

    @JsonProperty("cid")
    private Integer cid;
    @JsonProperty("className")
    private String className;
    @JsonProperty("owner")
    private Integer owner;
    @JsonProperty("courseName")
    private String courseName;
    @JsonProperty("grade")
    private String grade;
    @JsonProperty("schoolName")
    private String schoolName;

    public String getClassPic() {
        return classPic;
    }

    public void setClassPic(String classPic) {
        this.classPic = classPic;
    }

    @JsonProperty("school")
    private String classPic;

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public HomeClassBean() {
    }

    public HomeClassBean(Integer cid, String className, Integer owner, String courseName, String grade, String schoolName, String classPic) {
        this.cid = cid;
        this.className = className;
        this.owner = owner;
        this.courseName = courseName;
        this.grade = grade;
        this.schoolName = schoolName;
        this.classPic = classPic;
    }

    @Override
    public String toString() {
        return "HomeClassBean{" +
                "cid=" + cid +
                ", className='" + className + '\'' +
                ", owner=" + owner +
                ", courseName='" + courseName + '\'' +
                ", grade='" + grade + '\'' +
                ", schoolName='" + schoolName + '\'' +
                ", classPic='" + classPic + '\'' +
                '}';
    }
}
