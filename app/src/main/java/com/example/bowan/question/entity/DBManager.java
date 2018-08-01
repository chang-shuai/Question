package com.example.bowan.question.entity;

import android.content.Context;

import org.litepal.crud.DataSupport;

import java.util.List;

public class DBManager {

    private static DBManager sDBManager;

    private Context mContext;

    public static DBManager getDBManager() {
        if (sDBManager == null) {
            sDBManager = new DBManager();
        }
        return sDBManager;
    }

    private DBManager() {

    }

    public List<Dealer> getDealers() {
         return DataSupport.findAll(Dealer.class);
    }

    public Dealer getDealerById(int answerId) {
        return DataSupport.where("answerid=?", String.valueOf(answerId)).findFirst(Dealer.class);
    }

    public List<Questionnaire> getQuestionnaires() {
        return DataSupport.findAll(Questionnaire.class);
    }


    public List<Question> getQuestionsByQuestionnaire(int questionnaireId) {
        return DataSupport
                .where("questionnaireid=?", String.valueOf(questionnaireId))
                .find(Question.class);
    }


    public List<Question> getQuestions() {
        return DataSupport.findAll(Question.class);
    }

    public List<Option> getOptionsByQuestionId(int questionId) {
        return DataSupport
                .where("qid=?", String.valueOf(questionId))
                .find(Option.class);
    }

    public Option getOptionById(int id) {
        return DataSupport
                .where("mid=?", String.valueOf(id))
                .findFirst(Option.class);
    }

    public User getUserById(int userId) {
        return DataSupport
                .where("mid=?", String.valueOf(userId))
                .findFirst(User.class);
    }

    public AnswerDealer getAnswerDealerByAnswerId(int answerId) {
        return DataSupport
                .where("answerid=?", String.valueOf(answerId))
                .findFirst(AnswerDealer.class);
    }

    public AnswerQuestion getAnswerQuestionByAnswerIdMid(int answerId, int mid) {
        return DataSupport
                .where("answerid=? and mid=?", String.valueOf(answerId), String.valueOf(mid))
                .findFirst(AnswerQuestion.class);
    }

    public AnswerOption getAnswerOptionByQidMid(int qid, int mid) {
        return DataSupport.where("qid=? and mid=?", String.valueOf(qid), String.valueOf(mid))
                .findFirst(AnswerOption.class);
    }

    public AnswerOption getAnswerOptionByQid(int qid) {
        return DataSupport.where("qid=?", String.valueOf(qid))
                .findFirst(AnswerOption.class);
    }

}
