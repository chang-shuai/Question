package com.example.bowan.question.entity;

import com.google.gson.annotations.SerializedName;


import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.util.List;

public class Question extends DataSupport implements Serializable{
    @SerializedName("id")
    private int mid;
    private String cid;
    private String title;
    @SerializedName("desc")
    private String description;
    @SerializedName("disp_type")
    private String type;
    @SerializedName("bonuspoint")
    private int bonusPoint;         // 加分标识 1:加分 其他:基本得分
    private String score;
    @SerializedName("must_answer")
    private String mustAnswer;      //  必须回答 1:必答题
    @SerializedName("canuplaod1")
    private int canUpLoad;          //  是否上传 1:必须上传
    @SerializedName("suploadmax1")
    private int supLoadMax;         //  最多上传几张图片
    @SerializedName("suploadmin1")
    private int supLoadMin;         //  最少上传几张图片
    @SerializedName("nhorivert1")
    private int horiVert;           //  纵向或横向 0不控制，1横2竖
    @SerializedName("nminpicsize1")
    private String minPicSize;      //  图片最小尺寸
    @SerializedName("nmaxpicsize1")
    private String maxPicSize;      //  图片最大尺寸
    @SerializedName("option_list")
    private List<Option> options;
    private int questionnaireId;
    private String answerDesc;

    public String getAnswerDesc() {
        return answerDesc;
    }

    public void setAnswerDesc(String answerDesc) {
        this.answerDesc = answerDesc;
    }

    public int getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(int questionnaireId) {
        this.questionnaireId = questionnaireId;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getBonusPoint() {
        return bonusPoint;
    }

    public void setBonusPoint(int bonusPoint) {
        this.bonusPoint = bonusPoint;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getMustAnswer() {
        return mustAnswer;
    }

    public void setMustAnswer(String mustAnswer) {
        this.mustAnswer = mustAnswer;
    }

    public int getCanUpLoad() {
        return canUpLoad;
    }

    public void setCanUpLoad(int canUpLoad) {
        this.canUpLoad = canUpLoad;
    }

    public int getSupLoadMax() {
        return supLoadMax;
    }

    public void setSupLoadMax(int supLoadMax) {
        this.supLoadMax = supLoadMax;
    }

    public int getSupLoadMin() {
        return supLoadMin;
    }

    public void setSupLoadMin(int supLoadMin) {
        this.supLoadMin = supLoadMin;
    }

    public int getHoriVert() {
        return horiVert;
    }

    public void setHoriVert(int horiVert) {
        this.horiVert = horiVert;
    }

    public String getMinPicSize() {
        return minPicSize;
    }

    public void setMinPicSize(String minPicSize) {
        this.minPicSize = minPicSize;
    }

    public String getMaxPicSize() {
        return maxPicSize;
    }

    public void setMaxPicSize(String maxPicSize) {
        this.maxPicSize = maxPicSize;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Question{" +
                "mid=" + mid +
                ", cid='" + cid + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", type='" + type + '\'' +
                ", bonusPoint=" + bonusPoint +
                ", score='" + score + '\'' +
                ", mustAnswer='" + mustAnswer + '\'' +
                ", canUpLoad=" + canUpLoad +
                ", supLoadMax=" + supLoadMax +
                ", supLoadMin=" + supLoadMin +
                ", horiVert=" + horiVert +
                ", minPicSize='" + minPicSize + '\'' +
                ", maxPicSize='" + maxPicSize + '\'' +
                ", options=" + options +
                ", questionnaireId=" + questionnaireId +
                '}';
    }
}
