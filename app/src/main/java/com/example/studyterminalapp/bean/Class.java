package com.example.studyterminalapp.bean;


import java.io.Serializable;

public class Class implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer cid;

    /**
     * 名字中体现上下 以及学年
     */
    private String className;

    private String schoolName;

    /**
     * 教师id
     */
    private Integer owner;

    private String courseDescription;

    private String courseName;

    private String grade;

    private Integer textbookId;

    /**
     * 学生数量
     */
    private Integer studentCount;

    public Class() {
    }

    public Class(Integer cid, String className, String schoolName, Integer owner,
                 String courseDescription, String courseName, String grade, Integer studentCount, Integer textbookId) {
        this.cid = cid;
        this.className = className;
        this.schoolName = schoolName;
        this.owner = owner;
        this.courseDescription = courseDescription;
        this.courseName = courseName;
        this.grade = grade;
        this.studentCount = studentCount;
        this.textbookId = textbookId;
    }

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

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Integer getOwner() {
        return owner;
    }

    public void setOwner(Integer owner) {
        this.owner = owner;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
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

    public Integer getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
    }

    public Integer getTextbookId() {
        return textbookId;
    }

    public void setTextbookId(Integer textbookId) {
        this.textbookId = textbookId;
    }
}