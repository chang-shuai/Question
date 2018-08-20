package com.example.bowan.question.entity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.bowan.question.util.DBHelper;
import com.example.bowan.question.util.ExcelHelper;
import com.example.bowan.question.util.QuestionCursorWrapper;

import org.litepal.crud.DataSupport;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBManager {

    private static DBManager sDBManager;
    private static final String TAG = "DBManager";
    private Context mContext;
    private SQLiteDatabase db;

    public static DBManager getDBManager(Context context) {
        if (sDBManager == null) {
            sDBManager = new DBManager(context);
        }
        return sDBManager;
    }

    private DBManager(Context context) {
//        mContext = context;
//        DBHelper dbHelper = new DBHelper(context);
//        db = dbHelper.getWritableDatabase();
    }

    public SQLiteDatabase getDB() {

        return db;
    }

    public void selectGGGG(int answerId) {
        String sql = "SELECT\n" +
                "\t*, (\n" +
                "\t\tCASE bonuspoint\n" +
                "\t\tWHEN 0 THEN\n" +
                "\t\t\tpoints\n" +
                "\t\tEND\n" +
                "\t) AS score_base,\n" +
                "\t(\n" +
                "\t\tCASE bonuspoint\n" +
                "\t\tWHEN 1 THEN\n" +
                "\t\t\tpoints\n" +
                "\t\tEND\n" +
                "\t) AS score_extra\n" +
                "FROM\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\taq.mid AS qid,\n" +
                "\t\t\tao.mid AS oid,\n" +
                "\t\t\tsubstr(q.cid, 1, 3) AS ccid,\n" +
                "\t\t\tq.cid,\n" +
                "\t\t\to.exclusive_lpcolumn AS paita,\n" +
                "\t\t\to.avoidflag AS guibi,\n" +
                "\t\t\tq.score,\n" +
                "\t\t\tq.bonuspoint,\n" +
                "\t\t\t(\n" +
                "\t\t\t\tCASE q.bonuspoint\n" +
                "\t\t\t\tWHEN 0 THEN\n" +
                "\t\t\t\t\tq.score\n" +
                "\t\t\t\tEND\n" +
                "\t\t\t) AS type_base,\n" +
                "\t\t\t(\n" +
                "\t\t\t\tCASE q.bonuspoint\n" +
                "\t\t\t\tWHEN 1 THEN\n" +
                "\t\t\t\t\tq.score\n" +
                "\t\t\t\tEND\n" +
                "\t\t\t) AS type_extra,\n" +
                "\t\t\tai.images,\n" +
                "\t\t\tao.tips,\n" +
                "\t\t\t(\n" +
                "\t\t\t\tCASE\n" +
                "\t\t\t\tWHEN o.avoidflag = 1 THEN\n" +
                "\t\t\t\t\t'NA'\n" +
                "\t\t\t\tWHEN o.avoidflag = 0\n" +
                "\t\t\t\tAND o.exclusive_lpcolumn = 1 THEN\n" +
                "\t\t\t\t\tq.score\n" +
                "\t\t\t\tELSE\n" +
                "\t\t\t\t\t0\n" +
                "\t\t\t\tEND\n" +
                "\t\t\t) AS points\n" +
                "\t\tFROM\n" +
                "\t\t\tansweroption ao\n" +
                "\t\tLEFT JOIN answerquestion aq ON aq.id = ao.qid\n" +
                "\t\tLEFT JOIN option o ON ao.mid = o.mid\n" +
                "\t\tLEFT JOIN (\n" +
                "\t\t\tSELECT\n" +
                "\t\t\t\toid,\n" +
                "\t\t\t\tomid,\n" +
                "\t\t\t\tgroup_concat(imagepath, '|') AS images\n" +
                "\t\t\tFROM\n" +
                "\t\t\t\tanswerimage\n" +
                "\t\t\tGROUP BY\n" +
                "\t\t\t\tomid\n" +
                "\t\t) ai ON ao.id = ai.oid\n" +
                "\t\tLEFT JOIN question q ON aq.mid = q.mid\n" +
                "\t\tWHERE\n" +
                "\t\t\taq.answerid = "+answerId+"\n" +
                "\t\tAND ao.selected != 0\n" +
                "\t) xxx";


        QuestionCursorWrapper cursor = new QuestionCursorWrapper(DataSupport.findBySQL(sql));
        //test();

        List<GGGG> list = new ArrayList<>();
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {

                list.add(cursor.getGGGG());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

    }
    public List<Summary> getSummaries() {
        List<Summary> list = new ArrayList<>();
        String sql = "select ccid, sum(type_base) as sum_tb ,sum(type_extra) as sum_te ,sum(score_base) as sum_sb ,sum(score_extra) as sum_se from gggg group by ccid;";
        QuestionCursorWrapper cursor = new QuestionCursorWrapper(DataSupport.findBySQL(sql));

        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                list.add(cursor.getSummery());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    public List<Detail> getDetails() {
        String sql = "select cid, max(score_base) as max_sb,max(score_extra) as max_se,max(points) as max_points,max(tips) as max_tips from gggg group by cid;";
        QuestionCursorWrapper cursor = new QuestionCursorWrapper(DataSupport.findBySQL(sql));
        List<Detail> list = new ArrayList<>();
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                list.add(cursor.getDetails());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return list;
    }

    public List<Map<String,String>> getImages() {
        String sql = "select cid, group_concat(images,'|') as images from gggg group by cid;";
        QuestionCursorWrapper cursor = new QuestionCursorWrapper(DataSupport.findBySQL(sql));
        List<Map<String, String>> list = new ArrayList<>();
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                list.add(cursor.getImage());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return list;

    }

    public void test() {
        String sql = "SELECT\n" +
                "\t\t\t\tomid,\n" +
                "\t\t\t\tgroup_concat(imagepath, '|') AS images\n" +
                "\t\t\tFROM\n" +
                "\t\t\t\tanswerimage\n" +
                "\t\t\tGROUP BY\n" +
                "\t\t\t\tomid";
        QuestionCursorWrapper cursor = new QuestionCursorWrapper(DataSupport.findBySQL(sql));
        while (cursor.moveToNext()) {
            String imagePath = cursor.getString(cursor.getColumnIndex("images"));
            String omid = cursor.getString(cursor.getColumnIndex("omid"));
            Log.d(TAG, imagePath);
        }
        cursor.close();

    }

    public void clearGGGG() {
        DataSupport.deleteAll("gggg");

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

    public List<AnswerQuestion> getAnswerQuestion() {
        return DataSupport.findAll(AnswerQuestion.class);
    }

    public List<AnswerQuestion> getAnswerQuestionsByAnswerId(int answerId) {
        return DataSupport.where("answerid=?", answerId+"")
                .find(AnswerQuestion.class);
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

    public AnswerOption getAnswerOptionByMid(int mid) {
        return DataSupport.where("mid=?",String.valueOf(mid))
                .findFirst(AnswerOption.class);
    }


    public List<AnswerOption> getAnswerOptionByQid(int qid) {
        return DataSupport.where("qid=?", String.valueOf(qid))
                .find(AnswerOption.class);
    }

    public List<AnswerImage> getAnswerImageByOid(int oid) {
        return DataSupport.where("oid=?", oid+"")
                .find(AnswerImage.class);
    }

    public void deleteAnswerImageById(int id) {
        DataSupport.delete(AnswerImage.class, id);
    }

    public List<AnswerImage> getAnswerImagesByOmid(int omid) {
        return DataSupport.where("omid=?", omid+"")
                .find(AnswerImage.class);
    }

    public void deleteAnswerOption(int id) {
        DataSupport.delete(AnswerOption.class, id);
    }

    public void deleteAnswerQuestion(int id) {
        DataSupport.delete(AnswerQuestion.class, id);
    }

    public List<AnswerImageUpload> getAnswerImageUploads(int answerId) {
        return DataSupport.where("answerid=?", answerId+"")
                .find(AnswerImageUpload.class);
    }

}
