package com.example.bowan.question.entity;

import com.google.gson.annotations.SerializedName;


import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

public class Data extends DataSupport implements Serializable{
    @SerializedName("dealer")
    private List<Dealer> dealers;
    @SerializedName("survey")
    private Questionnaire questionnaire;

    public List<Dealer> getDealers() {
        return dealers;
    }

    public void setDealers(List<Dealer> dealers) {
        this.dealers = dealers;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }

    @Override
    public String toString() {
        return "Data{" +
                "dealers=" + dealers +
                ", questionnaire=" + questionnaire +
                '}';
    }
}
