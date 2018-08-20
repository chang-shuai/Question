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
import android.widget.Toast;

import com.example.bowan.question.entity.AnswerImage;
import com.example.bowan.question.entity.AnswerOption;
import com.example.bowan.question.entity.AnswerQuestion;
import com.example.bowan.question.entity.DBManager;
import com.example.bowan.question.entity.Option;
import com.example.bowan.question.entity.Question;
import com.example.bowan.question.entity.ResultData;
import com.example.bowan.question.util.AnswerResult;
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
        settings.setDomStorageEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.addJavascriptInterface(this, "AndroidWebView");
        mWebView.loadUrl("file:///android_asset/result_show.html");

        return view;
    }
    @JavascriptInterface
    private String resultShow() {
        AnswerResult result = new AnswerResult();
        return result.getJSON(mDealerId, getContext());
    }

}
