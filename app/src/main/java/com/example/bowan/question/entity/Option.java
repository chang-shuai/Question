package com.example.bowan.question.entity;

import com.google.gson.annotations.SerializedName;


import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

public class Option extends DataSupport implements Serializable{
    @SerializedName("id")
    private int mid;
    private String cid;
    private String title;
    @SerializedName("desc")
    private String description;
    private int qid;
    @SerializedName("is_open")
    private int isOpen;
    @SerializedName("open_empty")
    private int openEmpty;
    @SerializedName("exclusive")
    private int exclusive;
    @SerializedName("canuplaod")
    private int canUpLaod;
    @SerializedName("avoidflag")
    private int avoidFlag;
    private int isSelected;     // 此选项是否被选中, 0未被选中, 1被选中
    private List<String> imagePaths;

    public int getAvoidFlag() {
        return avoidFlag;
    }

    public void setAvoidFlag(int avoidFlag) {
        this.avoidFlag = avoidFlag;
    }

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }


    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    public int getOpenEmpty() {
        return openEmpty;
    }

    public void setOpenEmpty(int openEmpty) {
        this.openEmpty = openEmpty;
    }

    public int getExclusive() {
        return exclusive;
    }

    public void setExclusive(int exclusive) {
        this.exclusive = exclusive;
    }

    public int getCanUpLaod() {
        return canUpLaod;
    }

    public void setCanUpLaod(int canUpLaod) {
        this.canUpLaod = canUpLaod;
    }

}
