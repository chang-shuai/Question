package com.example.bowan.question.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

public class AnswerImage extends DataSupport implements Serializable{
    private int id;
    private int omid;
    private int oid;
    private String imagePath;
    private boolean uploaded;

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    public int getOmid() {
        return omid;
    }

    public void setOmid(int omid) {
        this.omid = omid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
