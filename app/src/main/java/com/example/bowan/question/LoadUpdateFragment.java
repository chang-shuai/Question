package com.example.bowan.question;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.bowan.question.entity.DBManager;
import com.example.bowan.question.entity.Data;
import com.example.bowan.question.entity.Dealer;
import com.example.bowan.question.entity.JsonData;
import com.example.bowan.question.entity.Option;
import com.example.bowan.question.entity.Question;
import com.example.bowan.question.entity.Questionnaire;
import com.example.bowan.question.entity.RootGroup;
import com.example.bowan.question.entity.User;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * 下载问题和上传结果展示页面
 */
public class LoadUpdateFragment extends Fragment {

    private static final String ARG_USER_INSTANCE = "argUserInstance";

    private Button mLoadButton;
    private Button mUpdateButton;
    private User mUser;
    private String mJsonData;
    private ProgressDialog mProgressDialog;


    private static final int SAVE_ACCOMPLISH = 0;   //accomplish
    private static final int SAVE_FAILURE = 1;       //failure
    private static final int JSON_PARSE_FAILURE = 2;

    /**
     * 封装获得LoadUpdateFragment实例静态方法, 生成实例时传入用户实例
     * @param user
     * @return
     */
    public static LoadUpdateFragment newInstance(User user) {
        Bundle args = new Bundle();
        LoadUpdateFragment fragment = new LoadUpdateFragment();
        args.putSerializable(ARG_USER_INSTANCE, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        mUser = (User) args.getSerializable(ARG_USER_INSTANCE);
    }

    /**
     * 生成整个页面视图
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frangment_load_update, container, false);

        /**
         * 下载问卷按钮
         */
        mLoadButton = view.findViewById(R.id.load_update_load);
        mLoadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressDialog = ProgressDialog.show(getActivity(), null, "正在加载...");

                LitePal.getDatabase();
                getJsonData();
                mProgressDialog.dismiss();
                int isSave = dealerJsonString();
                if (isSave == SAVE_ACCOMPLISH) {
                    Toast.makeText(getActivity(), "下载完成", Toast.LENGTH_SHORT).show();
                } else if (isSave == JSON_PARSE_FAILURE) {
                    Toast.makeText(getActivity(), "该用户无问卷可下载", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "问卷已存在，请不要重复下载", Toast.LENGTH_SHORT).show();
                }

            }
        });

        /**
         * 上传数据按钮
         */
        mUpdateButton = view.findViewById(R.id.load_update_update);
        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ChoiceActivity.newIntent(getActivity(), true, mUser);
                startActivity(intent);
            }
        });

        return view;
    }

    /**
     * 启用线程, 并将线程设置为守护线程
     */
    private void getJsonData() {
        try {
            Thread thread = new Thread(task);
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载问卷子线程任务
     */
    Runnable task = new Runnable() {
        @Override
        public void run() {
            InputStream input = null;
            try {
                String path = "http://qv4.bwing.com.cn/api/answer/getdata";
                path = path + "?token=Bearer" + mUser.getToken();
                URL url = new URL(path);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                StringBuffer sb = new StringBuffer();
                while ((line=br.readLine()) != null) {
                    sb.append(line);
                }
                mJsonData = sb.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    /**
     * 将下载的问卷json数据解析后存入数据库
     */
    private int dealerJsonString() {
        try {
            JSONObject jsonObject = new JSONObject(mJsonData);
            int code = jsonObject.getInt("code");
            if (code == 1) {
                Gson gson = new Gson();
                JsonData jsonData = gson.fromJson(mJsonData, JsonData.class);
                Data data = jsonData.getData();


                List<Dealer> dealers = data.getDealers();
                List<Dealer> oldDealers = DBManager.getDBManager(getContext()).getDealers();
                for (Dealer dealer : dealers) {
                    if (!oldDealers.contains(dealer)) {
                        dealer.save();
                    }
                }

                Questionnaire questionnaire = data.getQuestionnaire();
                List<Questionnaire> oldQuestionnaires = DBManager.getDBManager(getContext()).getQuestionnaires();
                if (!oldQuestionnaires.contains(questionnaire)) {
                    questionnaire.save();
                } else {
                    return SAVE_FAILURE;
                }

                RootGroup rootGroup = questionnaire.getRootGroup();

                List<Question> questionList = rootGroup.getQuestions();
                for (Question question : questionList) {
                    question.setQuestionnaireId(questionnaire.getMid());
                    question.save();

                    List<Option> optionList = question.getOptions();
                    for (Option option : optionList) {
                        option.save();
                    }
                }
                return SAVE_ACCOMPLISH;
            }
        } catch (Exception e) {

        }
        return JSON_PARSE_FAILURE;
    }

}
