package com.example.bowan.question.entity;

import com.google.gson.annotations.SerializedName;


import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Dealer extends DataSupport implements Serializable{

    private int sid;
    @SerializedName("answerid")
    private int answerId;
    @SerializedName("dealername")
    private String dealerName;

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
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


    @Override
    public String toString() {
        return "Dealer{" +
                "sid=" + sid +
                ", answerId=" + answerId +
                ", dealerName='" + dealerName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dealer dealer = (Dealer) o;
        return answerId == dealer.answerId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(answerId);
    }
}
