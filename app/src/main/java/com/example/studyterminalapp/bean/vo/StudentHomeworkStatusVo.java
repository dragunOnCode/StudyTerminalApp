package com.example.studyterminalapp.bean.vo;

import java.io.Serializable;

public class StudentHomeworkStatusVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer uid;

    private String nickname;

    private String studentNumber;

    private Integer questionTotalNumber;

    private Integer questionFinishNumber;

    private Integer questionNoCorrectNumber;

    public StudentHomeworkStatusVo() {
    }

    public StudentHomeworkStatusVo(Integer uid, String nickname, String studentNumber,
                                   Integer questionTotalNumber, Integer questionFinishNumber, Integer questionNoCorrectNumber) {
        this.uid = uid;
        this.nickname = nickname;
        this.studentNumber = studentNumber;
        this.questionTotalNumber = questionTotalNumber;
        this.questionFinishNumber = questionFinishNumber;
        this.questionNoCorrectNumber = questionNoCorrectNumber;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(String studentNumber) {
        this.studentNumber = studentNumber;
    }

    public Integer getQuestionTotalNumber() {
        return questionTotalNumber;
    }

    public void setQuestionTotalNumber(Integer questionTotalNumber) {
        this.questionTotalNumber = questionTotalNumber;
    }

    public Integer getQuestionFinishNumber() {
        return questionFinishNumber;
    }

    public void setQuestionFinishNumber(Integer questionFinishNumber) {
        this.questionFinishNumber = questionFinishNumber;
    }

    public Integer getQuestionNoCorrectNumber() {
        return questionNoCorrectNumber;
    }

    public void setQuestionNoCorrectNumber(Integer questionNoCorrectNumber) {
        this.questionNoCorrectNumber = questionNoCorrectNumber;
    }

    @Override
    public String toString() {
        return "StudentHomeworkStatusVo{" +
                "uid=" + uid +
                ", nickname='" + nickname + '\'' +
                ", studentNumber='" + studentNumber + '\'' +
                ", questionTotalNumber=" + questionTotalNumber +
                ", questionFinishNumber=" + questionFinishNumber +
                ", questionNoCorrectNumber=" + questionNoCorrectNumber +
                '}';
    }
}
