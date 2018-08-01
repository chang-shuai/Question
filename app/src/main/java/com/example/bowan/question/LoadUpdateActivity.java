package com.example.bowan.question;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.bowan.question.entity.User;

import java.io.Serializable;

/**
 * 下载上传页面的活动
 */
public class LoadUpdateActivity extends SingleFragmentActivity {

    public static final String EXTRA_USER_INSTANCE = "com.bowan.question.UserInstance";


    @Override
    protected Fragment createFragment() {
        User user = (User) getIntent().getSerializableExtra(EXTRA_USER_INSTANCE);
        return LoadUpdateFragment.newInstance(user);
    }

    public static Intent newIntent(Context packageContext, User user) {
        Intent intent = new Intent(packageContext, LoadUpdateActivity.class);
        intent.putExtra(EXTRA_USER_INSTANCE, user);
        return intent;
    }


}
