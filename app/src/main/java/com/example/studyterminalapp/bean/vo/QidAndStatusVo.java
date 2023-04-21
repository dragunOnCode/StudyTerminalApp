package com.example.studyterminalapp.bean.vo;

public class QidAndStatusVo {
    private Integer id;

    private Integer qid;

    private Integer finishedStatus;

    public QidAndStatusVo() {
    }

    public QidAndStatusVo(Integer id, Integer qid, Integer finishedStatus) {
        this.id = id;
        this.qid = qid;
        this.finishedStatus = finishedStatus;
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

    public Integer getFinishedStatus() {
        return finishedStatus;
    }

    public void setFinishedStatus(Integer finishedStatus) {
        this.finishedStatus = finishedStatus;
    }
}
