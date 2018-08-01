package com.example.bowan.question.entity;

import com.google.gson.annotations.SerializedName;


import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

public class RootGroup extends DataSupport implements Serializable{
    private String title;
    @SerializedName("sub_list")
    private List<Question> questions;



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "RootGroup{" +
                "title='" + title + '\'' +
                ", questions=" + questions +
                '}';
    }
}
