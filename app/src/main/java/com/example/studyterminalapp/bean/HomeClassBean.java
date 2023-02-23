package com.example.studyterminalapp.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class HomeClassBean implements Serializable {

    private static final long serialVersionUID = 1L; //序列化时保持ShopBean类版本的兼容性

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("className")
    private String className;
    @JsonProperty("studentNum")
    private Integer studentNum;
    @JsonProperty("subject")
    private String subject;
    @JsonProperty("grade")
    private String grade;
    @JsonProperty("school")
    private String school;

    public String getClassPic() {
        return classPic;
    }

    public void setClassPic(String classPic) {
        this.classPic = classPic;
    }

    @JsonProperty("school")
    private String classPic;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(Integer studentNum) {
        this.studentNum = studentNum;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public HomeClassBean() {
    }

    public HomeClassBean(Integer id, String className, Integer studentNum, String subject, String grade, String school, String classPic) {
        this.id = id;
        this.className = className;
        this.studentNum = studentNum;
        this.subject = subject;
        this.grade = grade;
        this.school = school;
        this.classPic = classPic;
    }
}
