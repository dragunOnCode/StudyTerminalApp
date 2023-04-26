package com.example.studyterminalapp.bean;

import android.net.Uri;

public class Answer {
    private int qid;
    private String answer;
    private Uri fileUri;

    public Answer() {
        answer = "";
    }

    public Answer(int qid, String answer, Uri fileUri) {
        this.qid = qid;
        this.answer = answer;
        this.fileUri = fileUri;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Uri getFileUri() {
        return fileUri;
    }

    public void setFileUri(Uri fileUri) {
        this.fileUri = fileUri;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "qid=" + qid +
                ", answer='" + answer + '\'' +
                ", fileUri=" + fileUri +
                '}';
    }
}
