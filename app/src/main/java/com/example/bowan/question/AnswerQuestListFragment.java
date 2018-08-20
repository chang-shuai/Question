package com.example.bowan.question;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bowan.question.entity.AnswerDealer;
import com.example.bowan.question.entity.AnswerOption;
import com.example.bowan.question.entity.AnswerQuestion;
import com.example.bowan.question.entity.DBManager;
import com.example.bowan.question.entity.Dealer;
import com.example.bowan.question.entity.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * 展现题目列表. 布局文件`activity_answer`中id为`fragment_container`Frame控件的关联类.
 */
public class AnswerQuestListFragment extends Fragment {

    private static final String ARG_DEALER = "ANSWER_QUEST_LIST_DEALER_ITEM";
    private static final String ARG_CURRENT_SELECTED_QUESTION = "current_selected_question";

    private Callbacks mCallbacks;
    private RecyclerView mQuestionRecyclerView;
    private Dealer mDealer;
    private int mCurrentPosition = -1;
    private QuestionAdapter mAdapter;
    private ImageButton mArrows;
    private boolean mFlag = true;


    /**
     * 封装获得AnswerQuestListFragment实例的方法, 直接在实例中保存经销商的answerId;
     * @param dealer
     * @return
     */
    public static AnswerQuestListFragment newInstance(Dealer dealer) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DEALER, dealer);
        AnswerQuestListFragment fragment = new AnswerQuestListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 获得新建AnswerQuestListFragment中保存的经销商answerId;
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDealer = (Dealer) getArguments().getSerializable(ARG_DEALER);
        if (savedInstanceState != null) {

            mCurrentPosition = savedInstanceState.getInt(ARG_CURRENT_SELECTED_QUESTION);
        }
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_answer_quest_list, container, false);

        mArrows = view.findViewById(R.id.answer_quest_arrows);
        mArrows.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFlag) {
                    mArrows.setImageResource(R.drawable.ic_question_hide);
                    mCallbacks.increaseWeight();
                    mAdapter.notifyDataSetChanged();
                    mCallbacks.onUpdateOption();
                    mFlag = false;
                } else {
                    mArrows.setImageResource(R.drawable.ic_question_show);
                    mCallbacks.reduceWeight();
                    mAdapter.notifyDataSetChanged();
                    mCallbacks.onUpdateOption();
                    mFlag = true;
                }
            }
        });


        mQuestionRecyclerView = view.findViewById(R.id.answer_quest_recycler_view);

        updateUI();
        return view;
    }


    /**
     * 在Activity刚启动Fragment的时候调用此方法, 并将Activity的实例传入.
     * 这里将AnswerActivity的实例传入此处, 并将其强转为Callback接口的对象,
     * 调用Callback接口中的`onQuestionSelected`抽象方法的时候, 会执行其
     * 实现类`AnswerActivity`中的`onQuestionSelected`方法.
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_CURRENT_SELECTED_QUESTION, mCurrentPosition);
    }

    /**
     * 定义回调接口, 供AnswerActivity类实现.
     */
    public interface Callbacks {
        void onQuestionSelected(Question question, int currentPosition);
        void increaseWeight();
        void reduceWeight();
        void onUpdateOption();
    }

    public void updateUI() {
        // 要展现的数据集合
        List<Question> questions = DBManager.getDBManager(getContext()).getQuestionsByQuestionnaire(mDealer.getSid());
        if (mAdapter == null) {
            mAdapter = new QuestionAdapter(questions);
            mQuestionRecyclerView.setAdapter(mAdapter);
            mQuestionRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        } else {
            mAdapter.setQuestions(questions);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void updateTitleColor(int currentPosition) {
        mCurrentPosition = currentPosition;
        updateUI();
    }

    /**
     *
     */
    public class QuestionHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTextView;
        private ImageView mImageView;
        private Question mQuestion;

        public QuestionHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.answer_question_title);
            itemView.setOnClickListener(this);

            mImageView = itemView.findViewById(R.id.answer_question_answered);
        }

        public void bind(Question question, int position) {
            this.mQuestion = question;
            if (mFlag) {
                mTextView.setText(question.getCid());
            } else {
                mTextView.setText(question.getCid()+question.getTitle());
            }
            if (mCurrentPosition == position ) {
                mTextView.setTextColor(Color.parseColor("#3370CC"));
            } else {
                mTextView.setTextColor(Color.parseColor("#999999"));
            }
            AnswerQuestion answerQuestion = DBManager.getDBManager(getContext()).getAnswerQuestionByAnswerIdMid(mDealer.getAnswerId(), mQuestion.getMid());
            if (answerQuestion != null) {
                List<AnswerOption> answerOptions = DBManager.getDBManager(getContext()).getAnswerOptionByQid(answerQuestion.getId());
                for (AnswerOption answerOption : answerOptions) {
                    if (answerOption.isSelected()) {
                        mImageView.setVisibility(View.VISIBLE);
                        break;
                    } else {
                        mImageView.setVisibility(View.INVISIBLE);
                    }
                }
            } else {
                mImageView.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public void onClick(View v) {
            mCurrentPosition = this.getAdapterPosition();
            // 方法的实现在AnswerActivity中
            mCallbacks.onQuestionSelected(mQuestion, mCurrentPosition);
            mAdapter.notifyDataSetChanged();
        }

    }

    /**
     *
     */
    private class QuestionAdapter extends RecyclerView.Adapter<QuestionHolder> {

        private List<Question> mQuestions;

        public QuestionAdapter(List<Question> questions) {
            this.mQuestions = questions;
        }

        @Override
        public QuestionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_question, parent, false);
            return new QuestionHolder(itemView);
        }

        @Override
        public void onBindViewHolder(QuestionHolder holder, int position) {
            Question question = mQuestions.get(position);
            holder.bind(question, position);
        }

        @Override
        public int getItemCount() {
            return mQuestions.size();
        }

        public void setQuestions(List<Question> questions) {
            mQuestions = questions;
        }


    }
}
