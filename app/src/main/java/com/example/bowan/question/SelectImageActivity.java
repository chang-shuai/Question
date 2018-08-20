package com.example.bowan.question;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SelectImageActivity extends SingleFragmentActivity {

    private static final String ANSWER_OPTION_ID = "answer_option_id";

    @Override
    protected Fragment createFragment() {
        int oid = getIntent().getIntExtra(ANSWER_OPTION_ID, -1);
        return SelectImageFragment.newInstance(oid);
    }

    public static Intent newIntent(Context context, int oid) {
        Intent intent = new Intent(context, SelectImageActivity.class);
        intent.putExtra(ANSWER_OPTION_ID, oid);
        return intent;
    }
}
