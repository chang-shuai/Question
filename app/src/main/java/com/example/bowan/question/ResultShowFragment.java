package com.example.bowan.question;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.bowan.question.entity.AnswerImage;
import com.example.bowan.question.entity.AnswerOption;
import com.example.bowan.question.entity.AnswerQuestion;
import com.example.bowan.question.entity.DBManager;
import com.example.bowan.question.entity.Option;
import com.example.bowan.question.entity.Question;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ResultShowFragment extends Fragment {
    private static final String ARG_DEALER_ID = "arg_dealer_id";

    private WebView mWebView;
    private int mDealerId;


    public static ResultShowFragment newInstance(int answerId) {
        Bundle args = new Bundle();
        args.putInt(ARG_DEALER_ID, answerId);
        ResultShowFragment fragment = new ResultShowFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDealerId = getArguments().getInt(ARG_DEALER_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result_show, container, false);

        mWebView = view.findViewById(R.id.result_show_web_view);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.addJavascriptInterface(this, "AndroidWebView");
        mWebView.loadUrl("file:///android_asset/result_show.html");

        return view;
    }

    @JavascriptInterface
    public String resultShow(){
        List<Question> questions = DBManager.getDBManager().getQuestions();
        for (Question question : questions) {
            List<Option> options = DBManager.getDBManager().getOptionsByQuestionId(question.getMid());
            AnswerQuestion answerQuestion = DBManager.getDBManager().getAnswerQuestionByAnswerIdMid(mDealerId, question.getMid());
            if (answerQuestion == null) {
                continue;
            }
            for (Option option : options) {
                AnswerOption answerOption = DBManager.getDBManager().getAnswerOptionByQidMid(answerQuestion.getId(), option.getMid());
                if (answerOption != null) {
                    option.setIsSelected(1);
                    option.setDescription(answerOption.getTips());
                    List<AnswerImage> answerImages = DBManager.getDBManager().getAnswerImagesByOmid(option.getMid());
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
        String json = gson.toJson(questions, new TypeToken<List<Question>>() {}.getType());
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
