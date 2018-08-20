package com.example.bowan.question;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bowan.question.entity.AnswerOption;
import com.example.bowan.question.entity.AnswerQuestion;
import com.example.bowan.question.entity.DBManager;
import com.example.bowan.question.entity.Dealer;
import com.example.bowan.question.entity.Question;
import com.example.bowan.question.util.MyViewPager;

import java.lang.reflect.GenericArrayType;
import java.util.List;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;

public class QuestionPagerActivity extends AppCompatActivity implements AnswerMultipleOptionFragment.Callbacks, AnswerQuestListFragment.Callbacks{

    private MyViewPager mViewPager;
    private Dealer mDealer;
    private List<Question> mQuestions;
    private static final String EXTRA_DEALER_PAGER = "dealer";
    private PagerAdapter mAdapter;
    private int mCurrentPosition;
    private boolean mFlag = false;

    public static Intent newIntent(Context packageContext, Dealer dealer) {
        Intent intent = new Intent(packageContext, QuestionPagerActivity.class);
        intent.putExtra(EXTRA_DEALER_PAGER, dealer);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_pager);

        if (savedInstanceState != null) {
            mDealer = (Dealer) savedInstanceState.getSerializable(EXTRA_DEALER_PAGER);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_answer_question_list);
        if (fragment == null) {
            fragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_answer_question_list, fragment)
                    .commit();
        }



        mQuestions = DBManager.getDBManager(getApplicationContext()).getQuestionsByQuestionnaire(mDealer.getSid());
        mViewPager = findViewById(R.id.pager_answer_option);
       // mViewPager.setOffscreenPageLimit(3);
        mAdapter = new PagerAdapter(fragmentManager);
        mViewPager.setAdapter(mAdapter);

        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == SCROLL_STATE_DRAGGING) {
                    //Toast.makeText(QuestionPagerActivity.this, "验证"+mCurrentPosition, Toast.LENGTH_SHORT).show();
                    checkScrollable();
                    
                }
            }
        });
    }

    private void checkScrollable() {
        Question question = mQuestions.get(mCurrentPosition);
        if (question.getMustAnswer().equals("1")) {
            AnswerQuestion answerQuestion = DBManager.getDBManager(getApplicationContext()).getAnswerQuestionByAnswerIdMid(mDealer.getAnswerId(), question.getMid());
            List<AnswerOption> answerOptions = DBManager.getDBManager(getApplicationContext()).getAnswerOptionByQid(answerQuestion.getId());
            if (answerOptions.isEmpty()) {
                mFlag = false;
            } else {
                for (AnswerOption answerOption : answerOptions) {
                    if (answerOption.isSelected()) {
                        Toast.makeText(this, "验证通过", Toast.LENGTH_SHORT).show();
                        mFlag = true;
                        break;
                    }
                }
            }
            if (mFlag) {
                mViewPager.setScrollble(true);
            } else {
                mViewPager.setScrollble(false);
                Toast.makeText(QuestionPagerActivity.this, "必答题", Toast.LENGTH_SHORT).show();
            }
        }
//        if (answerQuestion == null) {
//            mViewPager.setScrollble(false);
//            return;
//        }
    }





    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_DEALER_PAGER, mDealer);
    }

    protected Fragment createFragment() {
        mDealer = (Dealer) getIntent().getSerializableExtra(EXTRA_DEALER_PAGER);
        return AnswerQuestListFragment.newInstance(mDealer);
    }



    @Override
    public void increaseWeight() {
        FrameLayout frameLayout = findViewById(R.id.fragment_answer_question_list);
        frameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT, 3));

    }

    @Override
    public void reduceWeight() {
        FrameLayout frameLayout = findViewById(R.id.fragment_answer_question_list);
        frameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT, 1));

    }

    @Override
    public void onQuestionListUpdated(int currentPosition) {

    }

    @Override
    public void onUpdateOption() {
        //mAdapter.notifyDataSetChanged();
        checkScrollable();
    }

    @Override
    public void onQuestionSelected(Question question, int currentPosition) {

    }


    private class PagerAdapter extends FragmentPagerAdapter{


        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }



        @Override
        public Fragment getItem(int position) {
            Question question = mQuestions.get(position);
            return AnswerMultipleOptionFragment.newInstance(1, mDealer.getAnswerId());
        }

        @Override
        public int getCount() {
            return mQuestions.size();
        }


    }
}
