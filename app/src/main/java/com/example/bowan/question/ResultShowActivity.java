package com.example.bowan.question;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.bowan.question.SingleFragmentActivity;

public class ResultShowActivity extends SingleFragmentActivity {
    private static final String EXTRA_DEALER_ID = "dealer_id";


    @Override
    protected Fragment createFragment() {
        int answerId = getIntent().getIntExtra(EXTRA_DEALER_ID, 0);
        return ResultShowFragment.newInstance(answerId);
    }

    public static Intent newIntent(Context context, int answerId) {
        Intent intent = new Intent(context, ResultShowActivity.class);
        intent.putExtra(EXTRA_DEALER_ID, answerId);
        return intent;
    }

}
