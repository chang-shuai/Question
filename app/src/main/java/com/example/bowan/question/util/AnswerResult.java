package com.example.bowan.question.util;

import android.content.Context;
import android.os.Environment;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.example.bowan.question.entity.AnswerImage;
import com.example.bowan.question.entity.AnswerOption;
import com.example.bowan.question.entity.AnswerQuestion;
import com.example.bowan.question.entity.DBManager;
import com.example.bowan.question.entity.Option;
import com.example.bowan.question.entity.Question;
import com.example.bowan.question.entity.ResultData;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class AnswerResult {


    public String getJSON(int mDealerId, Context context){
        DBManager dbManager = DBManager.getDBManager(context);
        int answerId = 0;
        List<Question> questions = dbManager.getQuestions();
        for (Question question : questions) {
            List<Option> options = dbManager.getOptionsByQuestionId(question.getMid());
            AnswerQuestion answerQuestion = dbManager.getAnswerQuestionByAnswerIdMid(mDealerId, question.getMid());
            if (answerQuestion == null) {
                continue;
            }
            answerId = answerQuestion.getAnswerId();
            question.setAnswerDesc(answerQuestion.getAnswerDesc());
            for (Option option : options) {
                AnswerOption answerOption = dbManager.getAnswerOptionByQidMid(answerQuestion.getId(), option.getMid());
                if (answerOption != null) {
                    option.setIsSelected(1);
                    option.setDescription(answerOption.getTips());
                    List<AnswerImage> answerImages = dbManager.getAnswerImageByOid(answerOption.getId());
                    List<String> imagePaths = new ArrayList<>();
                    for (AnswerImage answerImage : answerImages) {
                        imagePaths.add(answerImage.getImagePath());
                    }
                    option.setImagePaths(imagePaths);
                }
            }
            question.setOptions(options);
        }

        Gson gson = new Gson();
        ResultData resultData = new ResultData(answerId, questions);
        String json = gson.toJson(resultData);
        //String json = gson.toJson(questions, new TypeToken<List<Question>>() {}.getType());
        File file = createImageFile();
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(file));
            pw.println(json);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return json;
    }

    private File createImageFile() {
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "问卷";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".txt";
        return new File(appDir, fileName);
    }


}
