package com.example.bowan.question.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

public class AnswerOption extends DataSupport implements Serializable{
    private int id;
    private int mid;
    private int qid;
    private boolean selected;
    private List<String> imagePaths;

    public List<String> getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
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

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "AnswerOption{" +
                "id=" + id +
                ", mid=" + mid +
                ", qid=" + qid +
                ", selected=" + selected +
                '}';
    }
}
