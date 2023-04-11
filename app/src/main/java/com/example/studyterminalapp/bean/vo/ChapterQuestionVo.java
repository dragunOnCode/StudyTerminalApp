package com.example.studyterminalapp.bean.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class ChapterQuestionVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer cid;

    private Integer qid;

    private String questionType;

    private String questionContent;

    private String questionSolution;

    private String questionDifficulty;

    private String questionAnalysis;

    private String updateDate;

    private String courseName;

    private List<String> choiceSelectionList;

    private String grade;

    public ChapterQuestionVo() {
    }

    public ChapterQuestionVo(Integer id, Integer cid, Integer qid, String questionType, String questionContent,
                             String questionSolution, String questionDifficulty, String questionAnalysis,
                             String updateDate, String courseName, List<String> choiceSelectionList,
                             String grade) {
        this.id = id;
        this.cid = cid;
        this.qid = qid;
        this.questionType = questionType;
        this.questionContent = questionContent;
        this.questionSolution = questionSolution;
        this.questionDifficulty = questionDifficulty;
        this.questionAnalysis = questionAnalysis;
        this.updateDate = updateDate;
        this.courseName = courseName;
        this.choiceSelectionList = choiceSelectionList;
        this.grade = grade;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getQid() {
        return qid;
    }

    public void setQid(Integer qid) {
        this.qid = qid;
    }

    public String getQuestionType() {
        return questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getQuestionSolution() {
        return questionSolution;
    }

    public void setQuestionSolution(String questionSolution) {
        this.questionSolution = questionSolution;
    }

    public String getQuestionDifficulty() {
        return questionDifficulty;
    }

    public void setQuestionDifficulty(String questionDifficulty) {
        this.questionDifficulty = questionDifficulty;
    }

    public String getQuestionAnalysis() {
        return questionAnalysis;
    }

    public void setQuestionAnalysis(String questionAnalysis) {
        this.questionAnalysis = questionAnalysis;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public List<String> getChoiceSelectionList() {
        return choiceSelectionList;
    }

    public void setChoiceSelectionList(List<String> choiceSelectionList) {
        this.choiceSelectionList = choiceSelectionList;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "ChapterQuestionVo{" +
                "id=" + id +
                ", cid=" + cid +
                ", qid=" + qid +
                ", questionType='" + questionType + '\'' +
                ", questionContent='" + questionContent + '\'' +
                ", questionSolution='" + questionSolution + '\'' +
                ", questionDifficulty='" + questionDifficulty + '\'' +
                ", questionAnalysis='" + questionAnalysis + '\'' +
                ", updateDate='" + updateDate + '\'' +
                ", courseName='" + courseName + '\'' +
                ", choiceSelectionList=" + choiceSelectionList +
                ", grade='" + grade + '\'' +
                '}';
    }
}
