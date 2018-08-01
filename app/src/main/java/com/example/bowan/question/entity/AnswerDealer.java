package com.example.bowan.question.entity;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

public class AnswerDealer extends DataSupport implements Serializable{
    private int id;
    private int answerId;
    private String dealerName;
    private int questionnaireId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public int getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(int questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    @Override
    public String toString() {
        return "AnswerDealer{" +
                "id=" + id +
                ", answerId=" + answerId +
                ", dealerName='" + dealerName + '\'' +
                ", questionnaireId=" + questionnaireId +
                '}';
    }
}
