package com.example.bowan.question;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bowan.question.entity.AnswerOption;
import com.example.bowan.question.entity.AnswerQuestion;
import com.example.bowan.question.entity.DBManager;
import com.example.bowan.question.entity.Option;
import com.example.bowan.question.entity.Question;
import java.util.List;

/**
 * 多选题选项类
 */
public class AnswerMultipleOptionFragment extends Fragment {
    private static final String MULTIPLE_ARG_QUESTION = "answer_multiple_arg_question";
    private static final String MULTIPLE_ARG_ANSWER_ID = "answer_multiple_arg_answerId";
    private static final String MULTIPLE_CAMERA_PHOTO_FRAGMENT = "answer_camera_photo_fragment";

    private Question mQuestion;
    private RecyclerView mMultipleOptionRecycleView;
    private MultipleOptionAdapter mAdapter;
    private AnswerQuestion mAnswerQuestion;


    public static AnswerMultipleOptionFragment newInstance(Question question, int answerId) {
        Bundle args = new Bundle();
        args.putSerializable(MULTIPLE_ARG_QUESTION, question);
        args.putInt(MULTIPLE_ARG_ANSWER_ID, answerId);
        AnswerMultipleOptionFragment fragment = new AnswerMultipleOptionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQuestion = (Question) getArguments().getSerializable(MULTIPLE_ARG_QUESTION);

        /**
         * 根据经销商的answerId和问题的mid在AnswerQuestion表中查询, 若查到, 则记录此问题对象.
         * 若没有查到, 则新建一个此问题对象, 记录经销商点开的每一道题, 并表中添加一条记录.
         */
        int answerId = getArguments().getInt(MULTIPLE_ARG_ANSWER_ID);
        int mid = mQuestion.getMid();
        mAnswerQuestion = DBManager.getDBManager().getAnswerQuestionByAnswerIdMid(answerId, mid);
        if (mAnswerQuestion == null) {
            mAnswerQuestion = new AnswerQuestion();
            mAnswerQuestion.setMid(mid);
            mAnswerQuestion.setAnswerId(answerId);
            mAnswerQuestion.save();
        }


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer_multiple, container, false);
        initView(view);

        return view;
    }

    /**
     * 初始化视图
     * @param view
     */
    private void initView(View view) {
        /**
         * 题目题干
         */
        TextView questionTitleTextView = view.findViewById(R.id.answer_option_question_title);
        questionTitleTextView.setText(mQuestion.getTitle());

        /**
         * 问题描述
         */
        TextView questionDescTextView = view.findViewById(R.id.answer_option_question_description);
        questionDescTextView.setText("问题描述: " + mQuestion.getDescription());

        /**
         * 利用RecyclerView展示多选选项
         */
        List<Option> options = DBManager.getDBManager().getOptionsByQuestionId(mQuestion.getMid());
        mAdapter = new MultipleOptionAdapter(options);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        mMultipleOptionRecycleView = view.findViewById(R.id.answer_multiple_recycle);
        mMultipleOptionRecycleView.setAdapter(mAdapter);
        mMultipleOptionRecycleView.setLayoutManager(layoutManager);

    }

    /**
     * ###############################################################################
     * 以下是选项列表适配器内部类
     */

    public class MultipleOptionHolder extends RecyclerView.ViewHolder {
        private AnswerOption mAnswerOption;
        private CheckBox mCheckBoxOption;
        private ImageButton mImageButton;
        private LinearLayout mLinearLayout;

        public MultipleOptionHolder(View itemView) {
            super(itemView);
            mCheckBoxOption = itemView.findViewById(R.id.answer_multiple_option_checkbox);
            mImageButton = itemView.findViewById(R.id.answer_multiple_option_image_button);
            mLinearLayout = itemView.findViewById(R.id.answer_multiple_option_linear);


            /**
             * 多选按钮逻辑
             */
            mCheckBoxOption.setOnCheckedChangeListener(new CheckBoxDealer());

            mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ImageRecyclerActivity.class);
                    startActivity(intent);

                }
            });

        }

        public void bind(Option option, int position) {

            mCheckBoxOption.setText(option.getTitle());
            /**
             * 根据问题的id和选项的id在AnswerOption表中查找, 如果找到说明选中过此选项, 把记录的答案填充.
             * 若没有选中过此选项, 则新建一个对象, 在选择此选项后保存
             */
            mAnswerOption = DBManager.getDBManager().getAnswerOptionByQidMid(mAnswerQuestion.getId(), option.getMid());
            if (mAnswerOption == null) {
                mAnswerOption = new AnswerOption();
                mAnswerOption.setMid(option.getMid());
                mAnswerOption.setQid(mAnswerQuestion.getId());
            } else {
                if (mAnswerOption.isSelected()) {
                    mCheckBoxOption.setChecked(true);

                }
            }

        }

        /**
         * *****************************************************************************
         * 选项按钮逻辑内部类
         */
        private class CheckBoxDealer implements CompoundButton.OnCheckedChangeListener{
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mAnswerOption.setSelected(true);
                    mLinearLayout.setVisibility(View.VISIBLE);
                    mAnswerOption.save();
                } else {
                    mAnswerOption.setSelected(false);
                    mAnswerOption.save();
                    mLinearLayout.setVisibility(View.GONE);
                }
            }
        }

    }

    public class MultipleOptionAdapter extends RecyclerView.Adapter<MultipleOptionHolder> {

        private List<Option> mOptions;

        public MultipleOptionAdapter(List<Option> options) {
            this.mOptions = options;
        }

        @Override
        public MultipleOptionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_multiple_option, parent, false);
            return new MultipleOptionHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MultipleOptionHolder holder, int position) {
            Option option = mOptions.get(position);
            holder.bind(option, position);
        }

        @Override
        public int getItemCount() {
            return mOptions.size();
        }

    }
}

