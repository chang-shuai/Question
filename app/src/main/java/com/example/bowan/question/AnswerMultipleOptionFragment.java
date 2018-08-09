package com.example.bowan.question;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bowan.question.entity.AnswerImage;
import com.example.bowan.question.entity.AnswerOption;
import com.example.bowan.question.entity.AnswerQuestion;
import com.example.bowan.question.entity.DBManager;
import com.example.bowan.question.entity.Option;
import com.example.bowan.question.entity.Question;
import com.example.bowan.question.util.PictureUtils;

import java.io.File;
import java.util.List;

/**
 * 多选题选项类
 */
public class AnswerMultipleOptionFragment extends Fragment {
    private static final String MULTIPLE_ARG_QUESTION = "answer_multiple_arg_question";
    private static final String MULTIPLE_ARG_ANSWER_ID = "answer_multiple_arg_answerId";
    private static final String MULTIPLE_CAMERA_PHOTO_FRAGMENT = "answer_camera_photo_fragment";
    private static final int REQUEST_CAMERA = 1;
    private static final String OLD_CURRENT_SELECT_POSITION = "old_current_select_position";
    private static final String OLD_IMAGE_FILE = "old_image_file";


    private Question mQuestion;
    private RecyclerView mMultipleOptionRecycleView;
    private MultipleOptionAdapter mAdapter;
    private AnswerQuestion mAnswerQuestion;
    private List<Option> mOptions;
    private AlertDialog.Builder mBuilder;
    private File mImageFile;
    private int mCurrentSelectPosition;

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
        mOptions = DBManager.getDBManager().getOptionsByQuestionId(mQuestion.getMid());
        if (savedInstanceState != null) {
            mCurrentSelectPosition = savedInstanceState.getInt(OLD_CURRENT_SELECT_POSITION);
            mImageFile = (File) savedInstanceState.getSerializable(OLD_IMAGE_FILE);
        }

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
        questionTitleTextView.setText(mQuestion.getCid()+mQuestion.getTitle());

        /**
         * 问题描述
         */
        TextView questionDescTextView = view.findViewById(R.id.answer_option_question_description);
        questionDescTextView.setText(mQuestion.getDescription());

        /**
         * 利用RecyclerView展示多选选项
         */
        mAdapter = new MultipleOptionAdapter(mOptions);

        mMultipleOptionRecycleView = view.findViewById(R.id.answer_multiple_recycle);
        mMultipleOptionRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMultipleOptionRecycleView.setAdapter(mAdapter);

        mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle(R.string.select_camera_or_photo)
                .setItems(new String[]{"相机", "相册"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageFile));
                                startActivityForResult(intent, REQUEST_CAMERA);
                                break;
                            case 1:
                                Toast.makeText(getActivity(), "敬请期待", Toast.LENGTH_SHORT).show();

                                break;
                            default:
                                break;
                        }

                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (Activity.RESULT_OK == resultCode) {
                    int optionMid = mOptions.get(mCurrentSelectPosition).getMid();
                    AnswerOption answerOption = DBManager.getDBManager().getAnswerOptionByMid(optionMid);
                    AnswerImage answerImage = new AnswerImage();
                    answerImage.setOmid(answerOption.getMid());
                    answerImage.setOid(answerOption.getId());
                    answerImage.setImagePath(mImageFile.getPath());
                    answerImage.save();
                }
                break;
            default:
                break;
        }
    }

    private File createImageFile() {
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "问卷";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        return new File(appDir, fileName);
    }


    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyItemChanged(mCurrentSelectPosition);
    }

    @Override
    public void onPause() {
        super.onPause();
        List<AnswerOption> answerOptions = DBManager.getDBManager().getAnswerOptionByQid(mAnswerQuestion.getId());
        if (answerOptions.isEmpty()) {
            DBManager.getDBManager().deleteAnswerQuestion(mAnswerQuestion.getId());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(OLD_CURRENT_SELECT_POSITION, mCurrentSelectPosition);
        outState.putSerializable(OLD_IMAGE_FILE, mImageFile);
    }

    /**
     * ###############################################################################
     * 内部类, 选项的ViewHolder
     */

    public class MultipleOptionHolder extends RecyclerView.ViewHolder {
        private AnswerOption mAnswerOption;
        private CheckBox mCheckBoxOption;
        private ImageButton mImageButton;
        private LinearLayout mLinearLayout;
        private RecyclerView mImageRecyclerView;
        private ImageAdapter mImageAdapter;
        private List<AnswerImage> mAnswerImages;
        private EditText mTipsEditText;


        public MultipleOptionHolder(View itemView) {
            super(itemView);
            mCheckBoxOption = itemView.findViewById(R.id.answer_multiple_option_checkbox);
            mImageButton = itemView.findViewById(R.id.answer_multiple_option_image_button);
            mLinearLayout = itemView.findViewById(R.id.answer_multiple_option_linear);
            mImageRecyclerView = itemView.findViewById(R.id.answer_multiple_option_images_recycler);
            mTipsEditText = itemView.findViewById(R.id.answer_multiple_option_tips);


            mCheckBoxOption.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                /**
                 * 点击选项框按钮处理
                 * @param buttonView
                 * @param isChecked
                 */
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mCurrentSelectPosition = getAdapterPosition();
                    Option option = mOptions.get(mCurrentSelectPosition);

                    if (mAnswerOption == null) {
                        mAnswerOption = new AnswerOption();
                        mAnswerOption.setMid(option.getMid());
                        mAnswerOption.setQid(mAnswerQuestion.getId());
                    }
                    if (isChecked) { // 如果选中

                        mAnswerOption.setSelected(true);

                        /**
                         * 显示备注输入框
                         */
                        mTipsEditText.setVisibility(View.VISIBLE);
                        mTipsEditText.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                mAnswerOption.setTips(s.toString());
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                        mAnswerOption.save();

                        /**
                         * 是否上传照片
                         */
                        if (option.getCanUpLaod() == 1) {
                            mLinearLayout.setVisibility(View.VISIBLE);
                            if (mImageAdapter != null) {
                                mImageAdapter.notifyDataSetChanged();
                            }
                        }
                    } else { // 没有选中选项
                        mAnswerOption.setSelected(false);
                        DBManager.getDBManager().deleteAnswerOption(mAnswerOption.getId());
                        mTipsEditText.setVisibility(View.GONE);
                        mLinearLayout.setVisibility(View.GONE);
                    }
                }
            });

            /**
             * 添加图片按钮
             */
            mImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCurrentSelectPosition = getAdapterPosition();
                    mImageFile = createImageFile();
                    mBuilder.show();
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
            if (mAnswerOption != null && mAnswerOption.isSelected()) {
                mTipsEditText.setText(mAnswerOption.getTips());
                mCheckBoxOption.setChecked(true);
                loadImageRecycler();
            }
        }

        /**
         * 加载图片RecyclerView
         */
        private void loadImageRecycler() {
            mAnswerImages = DBManager.getDBManager().getAnswerImageByOid(mAnswerOption.getId());
            mImageAdapter = new ImageAdapter(mAnswerImages);
            LinearLayoutManager lm = new LinearLayoutManager(getActivity());
            lm.setOrientation(LinearLayoutManager.HORIZONTAL);
            mImageRecyclerView.setAdapter(mImageAdapter);
            mImageRecyclerView.setLayoutManager(lm);
        }

        /**
         * 内部类的内部类, 图片的ViewHolder
         * ******************************************************************
         */
        private class ImageHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private ImageView mImageView;
            private ImageButton mImageClearButton;
            private int mCurrentClearButton = -1;

            public ImageHolder(View itemView) {
                super(itemView);
                mImageView = itemView.findViewById(R.id.answer_image_item);
                mImageView.setOnClickListener(this);
                mImageClearButton = itemView.findViewById(R.id.answer_clear_image_item);
                mImageClearButton.setOnClickListener(this);
            }

            public void bind(AnswerImage answerImage, int position) {
                Bitmap bitmap = PictureUtils.getScaledBitmap(answerImage.getImagePath());
                if (mCurrentClearButton == position) {
                    mImageView.setVisibility(View.GONE);
                    mImageClearButton.setVisibility(View.GONE);
                } else {
                    mImageView.setImageBitmap(bitmap);
                }

            }

            @Override
            public void onClick(View v) {
                mCurrentClearButton =  this.getAdapterPosition();
                AnswerImage image = mAnswerImages.get(mCurrentClearButton);
                int imageId = image.getId();
                switch (v.getId()) {
                    case R.id.answer_clear_image_item:
                        DBManager.getDBManager().deleteAnswerImageById(imageId);
                        mImageAdapter.notifyDataSetChanged();
                        break;
                    case R.id.answer_image_item:
                        Intent intent = ImagePreviewActivity.newIntent(getActivity(), image.getImagePath());
                        startActivity(intent);
                        break;
                    default:
                        break;
                }

            }
        }

        /**
         * 内部类的内部类, 图片适配器
         * *****************************************************************************
         */
        private class ImageAdapter extends RecyclerView.Adapter<ImageHolder> {

            private List<AnswerImage> mImages;

            public ImageAdapter(List<AnswerImage> images) {
                mImages = images;
            }

            @Override
            public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View itemImageView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_image, parent, false);
                return new ImageHolder(itemImageView);
            }

            @Override
            public void onBindViewHolder(ImageHolder holder, int position) {
                AnswerImage answerImage = mImages.get(position);

                holder.bind(answerImage, position);
            }

            @Override
            public int getItemCount() {
                return mImages.size();
            }
        }
    }

    /**
     * 内部类, 选项适配器
     */
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

