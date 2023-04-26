package com.example.studyterminalapp.bean.vo;

import java.io.Serializable;
import java.util.List;

public class QuestionVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer qid;

    private String questionType;

    private String questionContent;

    private String questionSolution;

    private String questionDifficulty;

    private String questionAnalysis;

    private String courseName;

    private List<String> choiceSelectionList;

    private String grade;

    private Integer visible;

    private String keyword;

    private String imgUrl;

    private Integer score;

    public QuestionVo() {
    }

    public QuestionVo(Integer qid, String questionType, String questionContent, String questionSolution,
                      String questionDifficulty, String questionAnalysis, String courseName,
                      List<String> choiceSelectionList, String grade, Integer visible, String keyword,
                      String imgUrl, Integer score) {
        this.qid = qid;
        this.questionType = questionType;
        this.questionContent = questionContent;
        this.questionSolution = questionSolution;
        this.questionDifficulty = questionDifficulty;
        this.questionAnalysis = questionAnalysis;
        this.courseName = courseName;
        this.choiceSelectionList = choiceSelectionList;
        this.grade = grade;
        this.visible = visible;
        this.keyword = keyword;
        this.imgUrl = imgUrl;
        this.score = score;
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

    public Integer getVisible() {
        return visible;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getQuestionAnalysis() {
        return questionAnalysis;
    }

    public void setQuestionAnalysis(String questionAnalysis) {
        this.questionAnalysis = questionAnalysis;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "QuestionVo{" +
                "qid=" + qid +
                ", questionType='" + questionType + '\'' +
                ", questionContent='" + questionContent + '\'' +
                ", questionSolution='" + questionSolution + '\'' +
                ", questionDifficulty='" + questionDifficulty + '\'' +
                ", questionAnalysis='" + questionAnalysis + '\'' +
                ", courseName='" + courseName + '\'' +
                ", choiceSelectionList=" + choiceSelectionList +
                ", grade='" + grade + '\'' +
                ", visible=" + visible +
                ", keyword='" + keyword + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", score=" + score +
                '}';
    }
}
