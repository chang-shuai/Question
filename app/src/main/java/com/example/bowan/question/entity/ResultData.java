package com.example.bowan.question.entity;

import java.util.List;

public class ResultData {
    private int answerId;
    private List<Question> questions;

    public ResultData(int answerId, List<Question> questions) {
        this.answerId = answerId;
        this.questions = questions;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        questions = questions;
    }
}
