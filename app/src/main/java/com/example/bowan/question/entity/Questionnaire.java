package com.example.bowan.question.entity;

import com.google.gson.annotations.SerializedName;


import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Objects;


public class Questionnaire extends DataSupport implements Serializable{
    @SerializedName("id")
    private int mid;
    private int status;
    private String title;
    @SerializedName("root_group")
    private RootGroup rootGroup;

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public RootGroup getRootGroup() {
        return rootGroup;
    }

    public void setRootGroup(RootGroup rootGroup) {
        this.rootGroup = rootGroup;
    }

    @Override
    public String toString() {
        return "Questionnaire{" +
                "id='" + mid + '\'' +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", rootGroup=" + rootGroup +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Questionnaire that = (Questionnaire) o;
        return mid == that.mid;
    }

    @Override
    public int hashCode() {

        return Objects.hash(mid);
    }
}
