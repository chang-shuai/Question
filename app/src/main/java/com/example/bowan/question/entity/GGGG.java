package com.example.bowan.question.entity;

import org.litepal.crud.DataSupport;

public class GGGG extends DataSupport{
    private int qid;
    private int oid;
    private String ccid;
    private String cid;
    private int paita;
    private int guibi;
    private String score;
    private int bonuspoint;
    private String type_base;
    private String type_extra;
    private String images;
    private String tips;
    private int points;
    private String score_base;
    private String score_extra;

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public String getCcid() {
        return ccid;
    }

    public void setCcid(String ccid) {
        this.ccid = ccid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getPaita() {
        return paita;
    }

    public void setPaita(int paita) {
        this.paita = paita;
    }

    public int getGuibi() {
        return guibi;
    }

    public void setGuibi(int guibi) {
        this.guibi = guibi;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getBonuspoint() {
        return bonuspoint;
    }

    public void setBonuspoint(int bonuspoint) {
        this.bonuspoint = bonuspoint;
    }

    public String getType_base() {
        return type_base;
    }

    public void setType_base(String type_base) {
        this.type_base = type_base;
    }

    public String getType_extra() {
        return type_extra;
    }

    public void setType_extra(String type_extra) {
        this.type_extra = type_extra;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getScore_base() {
        return score_base;
    }

    public void setScore_base(String score_base) {
        this.score_base = score_base;
    }

    public String getScore_extra() {
        return score_extra;
    }

    public void setScore_extra(String score_extra) {
        this.score_extra = score_extra;
    }
}
