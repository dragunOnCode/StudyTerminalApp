package com.example.studyterminalapp.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Correct {

    @JsonProperty("comment")
    private String comment;
    @JsonProperty("qid")
    private Integer qid;
    @JsonProperty("score")
    private Integer score;
    @JsonProperty("uid")
    private Integer uid;

    public Correct() {
    }

    public Correct(String comment, Integer qid, Integer score, Integer uid) {
        this.comment = comment;
        this.qid = qid;
        this.score = score;
        this.uid = uid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getQid() {
        return qid;
    }

    public void setQid(Integer qid) {
        this.qid = qid;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "Correct{" +
                "comment='" + comment + '\'' +
                ", qid=" + qid +
                ", score=" + score +
                ", uid=" + uid +
                '}';
    }
}
