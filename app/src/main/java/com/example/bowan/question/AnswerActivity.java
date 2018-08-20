package com.example.bowan.question;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.bowan.question.entity.AnswerQuestion;
import com.example.bowan.question.entity.Dealer;
import com.example.bowan.question.entity.Question;

/**
 * 答题页面的Activity, 此类重写了父类的`getLayoutResId`方法, 因为此类需要2个碎片,
 * 所以返回包含2个碎片的布局资源ID.
 *
 */
public class AnswerActivity extends AppCompatActivity implements AnswerQuestListFragment.Callbacks, AnswerMultipleOptionFragment.Callbacks{
    public static final String EXTRA_DEALER = "dealer";


    private Dealer mDealer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

//        ActionBar bar = getSupportActionBar();
//        if (bar != null) {
//            bar.hide();
//        }

        if (savedInstanceState != null) {
            mDealer = (Dealer) savedInstanceState.getSerializable(EXTRA_DEALER);
        }


        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_answer_question_list);
        if (fragment == null) {
            fragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_answer_question_list, fragment)
                    .commit();
        }


    }

    /**
     * 此方法返回一个封装好的Intent对象, 此Intent对象的跳转目标为此类.
     * @param packageContext 跳转的起点上下文.
     * @param dealer 经销商id
     * @return
     */
    public static Intent newIntent(Context packageContext, Dealer dealer) {
        Intent intent = new Intent(packageContext, AnswerActivity.class);
        intent.putExtra(EXTRA_DEALER, dealer);
        return intent;
    }

    /**
     * 此类的第一个碎片控件的id设定为`fragment_container`, 和父类的默认碎片控件id
     * 相同, 所以父类还是可以正常使得第一个碎片控件和此方法的`AnswerQuestListFragment`类
     * 相关联.
     * @return
     */
    protected Fragment createFragment() {
        mDealer = (Dealer) getIntent().getSerializableExtra(EXTRA_DEALER);
        return AnswerQuestListFragment.newInstance(mDealer);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_DEALER, mDealer);
    }

    /**
     * 此方法为`AnswerQuestListFragment.Callback`接口的实现方法, 定义了点击一条问题后,
     * 需要展现哪一个答题界面的逻辑.
     *
     * 此方法意在解耦, 此类中的2个碎片的控制权都在`AnswerActivity`中, 一个碎片最好不要只
     * 接控制另一个碎片的展现.
     * @param question
     */
    @Override
    public void onQuestionSelected(Question question, int currentPosition) {
        switch (question.getType()) {
            case "q_single":
                replaceFragment(AnswerSingleOptionFragment.newInstance(question));
                break;
            case "q_multiple":
                replaceFragment(AnswerMultipleOptionFragment.newInstance(currentPosition, mDealer.getAnswerId()));
                break;
            default:
                break;
        }
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
    public void onUpdateOption() {
        AnswerMultipleOptionFragment optionFragment = (AnswerMultipleOptionFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_answer_option);
        optionFragment.updateOption();
    }

    /**
     * 根据不同题目, 切换不同碎片
     * @param fragment
     */
    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_answer_option,fragment)
                .commit();
    }


    @Override
    public void onQuestionListUpdated(int currentPosition) {
        AnswerQuestListFragment questListFragment = (AnswerQuestListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_answer_question_list);
        questListFragment.updateTitleColor(currentPosition);
    }



}
