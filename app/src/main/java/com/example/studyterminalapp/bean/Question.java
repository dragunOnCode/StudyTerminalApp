package com.example.studyterminalapp.bean;

public class Question {
    int type; //0 1 2
    String content; //
    String answer; //

    public Question() {
    }

    public Question(int type, String content, String answer) {
        this.type = type;
        this.content = content;
        this.answer = answer;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "Question{" +
                "type=" + type +
                ", content='" + content + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
