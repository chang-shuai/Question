package com.example.bowan.question.entity;

public class Detail {
    private String cid;
    private String maxSB;
    private String maxSE;
    private String maxPoints;
    private String maxTips;

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getMaxSB() {
        return maxSB;
    }

    public void setMaxSB(String maxSB) {
        this.maxSB = maxSB;
    }

    public String getMaxSE() {
        return maxSE;
    }

    public void setMaxSE(String maxSE) {
        this.maxSE = maxSE;
    }

    public String getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(String maxPoints) {
        this.maxPoints = maxPoints;
    }

    public String getMaxTips() {
        return maxTips;
    }

    public void setMaxTips(String maxTips) {
        this.maxTips = maxTips;
    }
}
    //select cid, max(score_base) as max_sb,max(score_extra) as max_se,max(points) as max_points,max(tips) as max_tips from gggg group by cid;
