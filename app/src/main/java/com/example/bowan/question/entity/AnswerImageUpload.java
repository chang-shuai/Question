package com.example.bowan.question.entity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.Objects;

public class AnswerImageUpload extends DataSupport implements Serializable{
    private int answerId;
    private int quid;
    private int optid;
    private String fileName;
    private String imagePath;
    private boolean uploaded;

    public int getAnswerId() {
        return answerId;
    }

    public void setAnswerId(int answerId) {
        this.answerId = answerId;
    }

    public int getQuid() {
        return quid;
    }

    public void setQuid(int quid) {
        this.quid = quid;
    }

    public int getOptid() {
        return optid;
    }

    public void setOptid(int optid) {
        this.optid = optid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isUploaded() {
        return uploaded;
    }

    public void setUploaded(boolean uploaded) {
        this.uploaded = uploaded;
    }

    @Override
    public String toString() {
        return "AnswerImageUpload{" +
                "answerId=" + answerId +
                ", quid=" + quid +
                ", optid=" + optid +
                ", fileName='" + fileName + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", uploaded=" + uploaded +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerImageUpload that = (AnswerImageUpload) o;
        return answerId == that.answerId &&
                quid == that.quid &&
                optid == that.optid &&
                Objects.equals(fileName, that.fileName) &&
                Objects.equals(imagePath, that.imagePath);
    }

    @Override
    public int hashCode() {

        return Objects.hash(answerId, quid, optid, fileName, imagePath);
    }
}
