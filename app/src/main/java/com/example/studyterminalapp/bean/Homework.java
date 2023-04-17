package com.example.studyterminalapp.bean;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Homework implements Serializable {

    // todo: LocalDateTime该不该用
    private static final long serialVersionUID = 1L;

    private Integer hid;

    private String homeworkName;

    /**
     * 0未批改 1已经批改
     */
    private Integer finishStatus;

    private Integer classId;

    private Integer chapterId;

    private LocalDateTime deadline;

    /**
     * 开放时间
     */
    private LocalDateTime openDate;

    /**
     * 题目数量
     */
    private Integer totalCount;

    /**
     * 班级学生数量
     */
    private Integer studentCount;

    /**
     * 完成学生数量
     */
    private Integer finishedCount;

    /**
     * 批改数量
     */
    private Integer correctCount;

    /**
     * 全班作业最高分
     */
    private Integer maxScore;

    /**
     * 全班作业最低分
     */
    private Integer minScore;

    /**
     * 全班平均分
     */
    private Float averageScore;

    public Homework() {
    }

    public Homework(Integer hid, String homeworkName, Integer finishStatus, Integer classId,
                    Integer chapterId, LocalDateTime deadline, LocalDateTime openDate,
                    Integer totalCount, Integer studentCount, Integer finishedCount, Integer correctCount,
                    Integer maxScore, Integer minScore, Float averageScore) {
        this.hid = hid;
        this.homeworkName = homeworkName;
        this.finishStatus = finishStatus;
        this.classId = classId;
        this.chapterId = chapterId;
        this.deadline = deadline;
        this.openDate = openDate;
        this.totalCount = totalCount;
        this.studentCount = studentCount;
        this.finishedCount = finishedCount;
        this.correctCount = correctCount;
        this.maxScore = maxScore;
        this.minScore = minScore;
        this.averageScore = averageScore;
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

    public Integer getFinishStatus() {
        return finishStatus;
    }

    public void setFinishStatus(Integer finishStatus) {
        this.finishStatus = finishStatus;
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

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getStudentCount() {
        return studentCount;
    }

    public void setStudentCount(Integer studentCount) {
        this.studentCount = studentCount;
    }

    public Integer getFinishedCount() {
        return finishedCount;
    }

    public void setFinishedCount(Integer finishedCount) {
        this.finishedCount = finishedCount;
    }

    public Integer getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(Integer correctCount) {
        this.correctCount = correctCount;
    }

    public Integer getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(Integer maxScore) {
        this.maxScore = maxScore;
    }

    public Integer getMinScore() {
        return minScore;
    }

    public void setMinScore(Integer minScore) {
        this.minScore = minScore;
    }

    public Float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(Float averageScore) {
        this.averageScore = averageScore;
    }

    @Override
    public String toString() {
        return "Homework{" +
                "hid=" + hid +
                ", homeworkName='" + homeworkName + '\'' +
                ", finishStatus=" + finishStatus +
                ", classId=" + classId +
                ", chapterId=" + chapterId +
                ", deadline=" + deadline +
                ", openDate=" + openDate +
                ", totalCount=" + totalCount +
                ", studentCount=" + studentCount +
                ", finishedCount=" + finishedCount +
                ", correctCount=" + correctCount +
                ", maxScore=" + maxScore +
                ", minScore=" + minScore +
                ", averageScore=" + averageScore +
                '}';
    }
}
