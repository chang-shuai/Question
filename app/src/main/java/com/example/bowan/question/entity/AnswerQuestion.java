package com.example.bowan.question.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

public class AnswerQuestion extends DataSupport implements Serializable{
    private int id;
    private int mid;
    private int answerId;            // 经销商回答id;
    private String answerDesc;

    public String getAnswerDesc() {
        return answerDesc;
    }

    public void setAnswerDesc(String answerDesc) {
        this.answerDesc = answerDesc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    @Override
    public String toString() {
        return "AnswerQuestion{" +
                "id=" + id +
                ", mid=" + mid +
                ", answerId=" + answerId +
                '}';
    }
}
