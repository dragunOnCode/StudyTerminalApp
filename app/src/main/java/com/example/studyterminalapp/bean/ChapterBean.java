package com.example.studyterminalapp.bean;

import java.io.Serializable;

public class ChapterBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer cid;

    private String chapterName;

    private String textbookId;

    private Integer chapterNum;

    private Integer isChild;

    private Integer childChapter;

    public ChapterBean() {
    }

    public ChapterBean(Integer cid, String chapterName, String textbookId, Integer chapterNum, Integer isChild, Integer childChapter) {
        this.cid = cid;
        this.chapterName = chapterName;
        this.textbookId = textbookId;
        this.chapterNum = chapterNum;
        this.isChild = isChild;
        this.childChapter = childChapter;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getTextbookId() {
        return textbookId;
    }

    public void setTextbookId(String textbookId) {
        this.textbookId = textbookId;
    }

    public Integer getChapterNum() {
        return chapterNum;
    }

    public void setChapterNum(Integer chapterNum) {
        this.chapterNum = chapterNum;
    }

    public Integer getIsChild() {
        return isChild;
    }

    public void setIsChild(Integer isChild) {
        this.isChild = isChild;
    }

    public Integer getChildChapter() {
        return childChapter;
    }

    public void setChildChapter(Integer childChapter) {
        this.childChapter = childChapter;
    }
}
