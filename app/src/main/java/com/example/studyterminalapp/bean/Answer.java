package com.example.studyterminalapp.bean;

import android.net.Uri;

import com.example.studyterminalapp.utils.QuestionConstant;

public class Answer {
    private int qid;
    private String answer;
    private Uri fileUri;
    private String type;

    public Answer() {
        answer = "";
    }

    public Answer(int qid, String answer, Uri fileUri, String type) {
        this.qid = qid;
        this.answer = answer;
        this.fileUri = fileUri;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "qid=" + qid +
                ", answer='" + answer + '\'' +
                ", fileUri=" + fileUri +
                ", type='" + type + '\'' +
                '}';
    }
}
