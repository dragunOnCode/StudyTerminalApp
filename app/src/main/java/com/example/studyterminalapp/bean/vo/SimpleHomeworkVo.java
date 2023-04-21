package com.example.studyterminalapp.bean.vo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SimpleHomeworkVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer hid;

    private String homeworkName;

    private Integer classId;

    private Integer chapterId;

    private String chapterName;

    private LocalDateTime deadline;

    private LocalDateTime openDate;

    public SimpleHomeworkVo() {}

    public SimpleHomeworkVo(Integer hid, String homeworkName, Integer classId, Integer chapterId,
                            String chapterName, LocalDateTime deadline, LocalDateTime openDate) {
        this.hid = hid;
        this.homeworkName = homeworkName;
        this.classId = classId;
        this.chapterId = chapterId;
        this.chapterName = chapterName;
        this.deadline = deadline;
        this.openDate = openDate;
    }

    public Integer getHid() {
        return hid;
    }

    public void setHid(Integer hid) {
        this.hid = hid;
    }

    public String getHomeworkName() {
        return homeworkName;
    }

    public void setHomeworkName(String homeworkName) {
        this.homeworkName = homeworkName;
    }

    public Integer getClassId() {
        return classId;
    }

    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    public Integer getChapterId() {
        return chapterId;
    }

    public void setChapterId(Integer chapterId) {
        this.chapterId = chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDateTime openDate) {
        this.openDate = openDate;
    }

    @Override
    public String toString() {
        return "SimpleHomeworkVo{" +
                "hid=" + hid +
                ", homeworkName='" + homeworkName + '\'' +
                ", classId=" + classId +
                ", chapterId=" + chapterId +
                ", chapterName='" + chapterName + '\'' +
                ", deadline=" + deadline +
                ", openDate=" + openDate +
                '}';
    }
}
