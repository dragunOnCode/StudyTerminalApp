package com.example.studyterminalapp.bean.vo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class UserAnswerAndQuestionVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    //question部分

    private Integer qid;

    private String questionType;

    private String questionContent;

    private String questionSolution;

    private String questionDifficulty;

    private String questionAnalysis;

    private String imgUrl;

    private Integer questionScore;

    private List<String> choiceSelectionList;


    //answer部分
    private Integer finishedStatus;

    private Integer userScore;

    private String answer;

    private String answerUrl;

    private String incorrectReason;

    private Integer finishTime;

    private LocalDateTime finishDate;

    private String comment;

    private Integer correctStatus;

    public UserAnswerAndQuestionVo() {
    }

    public UserAnswerAndQuestionVo(Integer id, Integer qid, String questionType, String questionContent,
                                   String questionSolution, String questionDifficulty, String questionAnalysis,
                                   String imgUrl, Integer questionScore, List<String> choiceSelectionList,
                                   Integer finishedStatus, Integer userScore, String answer, String answerUrl,
                                   String incorrectReason, Integer finishTime, LocalDateTime finishDate,
                                   String comment, Integer correctStatus) {
        this.id = id;
        this.qid = qid;
        this.questionType = questionType;
        this.questionContent = questionContent;
        this.questionSolution = questionSolution;
        this.questionDifficulty = questionDifficulty;
        this.questionAnalysis = questionAnalysis;
        this.imgUrl = imgUrl;
        this.questionScore = questionScore;
        this.choiceSelectionList = choiceSelectionList;
        this.finishedStatus = finishedStatus;
        this.userScore = userScore;
        this.answer = answer;
        this.answerUrl = answerUrl;
        this.incorrectReason = incorrectReason;
        this.finishTime = finishTime;
        this.finishDate = finishDate;
        this.comment = comment;
        this.correctStatus = correctStatus;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public List<String> getChoiceSelectionList() {
        return choiceSelectionList;
    }

    public void setChoiceSelectionList(List<String> choiceSelectionList) {
        this.choiceSelectionList = choiceSelectionList;
    }

    public Integer getFinishedStatus() {
        return finishedStatus;
    }

    public void setFinishedStatus(Integer finishedStatus) {
        this.finishedStatus = finishedStatus;
    }

    public Integer getQuestionScore() {
        return questionScore;
    }

    public void setQuestionScore(Integer questionScore) {
        this.questionScore = questionScore;
    }

    public Integer getUserScore() {
        return userScore;
    }

    public void setUserScore(Integer userScore) {
        this.userScore = userScore;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getAnswerUrl() {
        return answerUrl;
    }

    public void setAnswerUrl(String answerUrl) {
        this.answerUrl = answerUrl;
    }

    public String getIncorrectReason() {
        return incorrectReason;
    }

    public void setIncorrectReason(String incorrectReason) {
        this.incorrectReason = incorrectReason;
    }

    public Integer getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Integer finishTime) {
        this.finishTime = finishTime;
    }

    public LocalDateTime getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDateTime finishDate) {
        this.finishDate = finishDate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getCorrectStatus() {
        return correctStatus;
    }

    public void setCorrectStatus(Integer correctStatus) {
        this.correctStatus = correctStatus;
    }

    @Override
    public String toString() {
        return "UserAnswerAndQuestionVo{" +
                "id=" + id +
                ", qid=" + qid +
                ", questionType='" + questionType + '\'' +
                ", questionContent='" + questionContent + '\'' +
                ", questionSolution='" + questionSolution + '\'' +
                ", questionDifficulty='" + questionDifficulty + '\'' +
                ", questionAnalysis='" + questionAnalysis + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", questionScore=" + questionScore +
                ", choiceSelectionList=" + choiceSelectionList +
                ", finishedStatus=" + finishedStatus +
                ", userScore=" + userScore +
                ", answer='" + answer + '\'' +
                ", answerUrl='" + answerUrl + '\'' +
                ", incorrectReason='" + incorrectReason + '\'' +
                ", finishTime=" + finishTime +
                ", finishDate=" + finishDate +
                ", comment='" + comment + '\'' +
                ", correctStatus=" + correctStatus +
                '}';
    }
}
