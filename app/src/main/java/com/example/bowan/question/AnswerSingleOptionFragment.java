package com.example.bowan.question;

import android.graphics.Path;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bowan.question.entity.DBManager;
import com.example.bowan.question.entity.Option;
import com.example.bowan.question.entity.Question;

import java.util.List;

/**
 * 单选题目选项展示页面
 */
public class AnswerSingleOptionFragment extends Fragment {
    private static final String SINGLE_ARG_QUESTION = "answerSingleArgQuestion";
    private static final int OPTION_ID_EXTRA = 11110000;

    private Question mQuestion;
    private TextView mQuestionTitleTextView;
    private RadioGroup mOptionsRadioGroup;
    private List<Option> mOptions;

    /**
     * 封装获得此类实例的方法
     * @param question
     * @return
     */
    public static AnswerSingleOptionFragment newInstance(Question question) {
        Bundle args = new Bundle();
        args.putSerializable(SINGLE_ARG_QUESTION, question);

        AnswerSingleOptionFragment fragment = new AnswerSingleOptionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuestion = (Question) getArguments().getSerializable(SINGLE_ARG_QUESTION);
        mOptions = DBManager.getDBManager().getOptionsByQuestionId(mQuestion.getMid());

    }


    /**
     * 重新回到栈顶的时候, 重写获取一次所有选项
     */
    @Override
    public void onResume() {
        super.onResume();
        mOptions = DBManager.getDBManager().getOptionsByQuestionId(mQuestion.getMid());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer_single, container, false);
        mQuestionTitleTextView = view.findViewById(R.id.answer_option_question_title);
        mQuestionTitleTextView.setText(mQuestion.getTitle());

        mOptionsRadioGroup = view.findViewById(R.id.answer_single_group);
        for (Option option : mOptions) {
            RadioButton radioButton = newRadioButton(option);
            mOptionsRadioGroup.addView(radioButton);
        }
        mOptionsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (Option option : mOptions) {
                    option.setIsSelected(0);
                    option.save();
                }
                RadioButton radioButton = group.findViewById(checkedId);
                Option option = DBManager.getDBManager().getOptionById(radioButton.getId()-OPTION_ID_EXTRA);
                option.setIsSelected(1);
                option.save();

            }
        });

        return view;

    }

    /**
     * 动态生成一个RadioButton
     * @param option
     * @return
     */
    private RadioButton newRadioButton(Option option) {
        RadioButton radioButton = new RadioButton(getActivity());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(20,20,20,20);
        params.gravity = Gravity.CENTER;

        radioButton.setText(option.getTitle());
        radioButton.setTextSize(20);
        radioButton.setPadding(20,20,20,20);
        radioButton.setGravity(Gravity.CENTER);
        radioButton.setLayoutParams(params);
        radioButton.setId(option.getMid()+OPTION_ID_EXTRA);
        if (option.getIsSelected() == 1) {
            radioButton.setChecked(true);
        }
        return radioButton;
    }
}
