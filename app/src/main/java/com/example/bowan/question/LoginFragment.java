package com.example.bowan.question;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bowan.question.entity.DBManager;
import com.example.bowan.question.entity.Questionnaire;
import com.example.bowan.question.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * 登录界面的Fragment碎片
 */
public class LoginFragment extends Fragment {

    private Button mCommitButton;
    private Button mOffLineButton;
    private EditText mLoginUsername;
    private EditText mLoginPassword;
    private User mUser;
    private boolean mLoginStatus = false;
    private Runnable mLoginTask;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        mUser = new User();

        mLoginTask = new Runnable() {
        @Override
        public void run() {
            InputStream input = null;
            OutputStream output = null;
            try {
                String loginUrl = "http://qv4.bwing.com.cn/api/login";
                URL url = new URL(loginUrl);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");

                PrintWriter pw = new PrintWriter(connection.getOutputStream());
                String param = "username="+ mUser.getUsername()+ "&password=" + mUser.getPassword() + "&domain=q.bwing.club";
                pw.print(param);
                pw.flush();

                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = null;
                StringBuffer sb = new StringBuffer();
                while((line = br.readLine()) != null) {
                    sb.append(line);
                }
                JSONObject jsonObject = new JSONObject(sb.toString());
                int code = jsonObject.getInt("code");
                if (code == 1) {
                    JSONObject data = jsonObject.getJSONObject("data");
                    mUser.setToken(data.getString("token"));
                    mUser.setMid(data.getJSONObject("user").getInt("id"));
                    mLoginStatus = true;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (input != null && output != null) {
                    try {
                        input.close();
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        };
    }

    /**
     *  加载此碎片的布局文件
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);


        /**
         * 用户名输入框
         */
        mLoginUsername = view.findViewById(R.id.login_username);
        mLoginUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUser.setUsername(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /**
         * 密码输入框
         */
        mLoginPassword = view.findViewById(R.id.login_password);
        mLoginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mUser.setPassword(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /**
         * 登录按钮
         */
        mCommitButton = view.findViewById(R.id.login_commit);
        mCommitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUser.getUsername() != null && mUser.getPassword() != null) {
                    // 启动一个线程执行登录任务, 利用join()阻塞, 等待结果返回后继续执行.
                    try {
                        Thread thread = new Thread(mLoginTask);
                        thread.start();
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (mLoginStatus) {
                        User oldUser = DBManager.getDBManager(getContext()).getUserById(mUser.getMid());
                        if (oldUser == null) {
                            mUser.save();
                        }
                        Intent intent = LoadUpdateActivity.newIntent(getActivity(), mUser);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getActivity(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "请输入用户名和密码", Toast.LENGTH_SHORT).show();
                }


            }

        });

        /**
         * 离线按钮
         */
        mOffLineButton = view.findViewById(R.id.login_off_line);
        mOffLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Questionnaire> questionnaires = DBManager.getDBManager(getContext()).getQuestionnaires();
                if (questionnaires.isEmpty()) {
                    Toast.makeText(getActivity(), "请登录后下载问卷使用", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = ChoiceActivity.newIntent(getActivity(), false, null);
                    startActivity(intent);
                }
            }
        });

        return view;
    }










}
