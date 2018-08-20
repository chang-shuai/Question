package com.example.bowan.question;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.aspose.cells.Cells;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.cells.WorksheetCollection;
import com.example.bowan.question.entity.AnswerImage;
import com.example.bowan.question.entity.AnswerImageUpload;
import com.example.bowan.question.entity.AnswerOption;
import com.example.bowan.question.entity.AnswerQuestion;
import com.example.bowan.question.entity.DBManager;
import com.example.bowan.question.entity.Dealer;
import com.example.bowan.question.entity.Detail;
import com.example.bowan.question.entity.Summary;
import com.example.bowan.question.entity.User;
import com.example.bowan.question.util.AnswerResult;
import com.example.bowan.question.util.ExcelHelper;


import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 此类为选择问卷的Fragment, 功能是选择问卷页面的具体逻辑和视图
 */
public class ChoiceFragment extends Fragment {

    private static final String ARG_CURRENT_SELECTED_DEALER = "CHOICE_CURRENT_SELECTED_DEALER";
    private static final MediaType JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    private static final MediaType IMAGE = MediaType.parse("image/*");
    private static final String ARG_IS_UPLOAD_PAGE = "arg_is_upload_page";
    private static final String ARG_USER = "arg_user";



    private Button mStartButton;
    private Button mResultGenerate;
    private Button mResultShow;
    private Button mUploadAnswer;
    private Button mUploadImage;

    private List<Dealer> mDealers;
    private int mCurrentSelectedPosition = -1;
    private DealerAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private boolean mIsUploadPage;
    private User mUser;
    private Context mContext;
    private SQLiteDatabase db;

    public static ChoiceFragment newInstance(boolean isUpload, User user) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_UPLOAD_PAGE, isUpload);
        args.putSerializable(ARG_USER, user);
        ChoiceFragment fragment = new ChoiceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIsUploadPage = getArguments().getBoolean(ARG_IS_UPLOAD_PAGE);
        mUser = (User) getArguments().getSerializable(ARG_USER);
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(ARG_CURRENT_SELECTED_DEALER);
        }
        mContext = getActivity().getApplicationContext();
        db = DBManager.getDBManager(mContext).getDB();

    }

    /**
     * 加载选择问卷页面的布局视图.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choice, container, false);
        mDealers = DBManager.getDBManager(mContext).getDealers();

        mRecyclerView = view.findViewById(R.id.choice_dealer_recycle);
        if (mDealers.isEmpty()) {
            TextView textView = view.findViewById(R.id.choice_none_dealer_hint);
            textView.setVisibility(View.VISIBLE);
        } else {
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mAdapter = new DealerAdapter(mDealers);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(mAdapter);

        }




        /**
         * 开始答题按钮
         */
        mStartButton = view.findViewById(R.id.choice_start_restart);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentSelectedPosition < 0 ) {
                    Toast.makeText(getActivity(), "请选择一个经销商", Toast.LENGTH_SHORT).show();
                } else {
                    Dealer dealer = mDealers.get(mCurrentSelectedPosition);
                    Intent intent = AnswerActivity.newIntent(getActivity(), dealer);
                    //Intent intent = QuestionPagerActivity.newIntent(getActivity(), dealer);
                    startActivity(intent);
                }

            }
        });


        mResultShow = view.findViewById(R.id.choice_result_show);
        mResultShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentSelectedPosition < 0 ) {
                    Toast.makeText(getActivity(), "请选择一个经销商", Toast.LENGTH_SHORT).show();
                } else {
                    Dealer dealer = mDealers.get(mCurrentSelectedPosition);
                    Intent intent = ResultShowActivity.newIntent(getActivity(), dealer.getAnswerId());
                    startActivity(intent);
                }
            }
        });

        /**
         * 生成Excel按钮
         */
        mResultGenerate = view.findViewById(R.id.choice_generate_result);
        mResultGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                生成PDF
//                File pdfFilePath = getActivity().getExternalCacheDir();
//                @SuppressLint("ResourceType") GeneratePdf generatePdf = new GeneratePdf(getActivity().getExternalCacheDir(), getResources().getString(R.raw.simsun));
//                String result = generatePdf.generate("测试内容");
//                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
//                dialog.setTitle(R.string.generate_pdf_path_title);
//                dialog.setMessage(pdfFilePath.getAbsolutePath());
//                dialog.setCancelable(true);
//                dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//                dialog.show();

                if (mCurrentSelectedPosition < 0 ) {
                    Toast.makeText(getActivity(), "请选择一个经销商", Toast.LENGTH_SHORT).show();
                } else {
                    Dealer dealer = mDealers.get(mCurrentSelectedPosition);
                    DBManager.getDBManager(mContext).selectGGGG(dealer.getAnswerId());
                    //DBManager.getDBManager(mContext).test();
                    // 验证License
//                    if (!ExcelHelper.getLicense()) {
//                        return;
//                    }

                    try {
                        String excelPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "问卷" + File.separator + "模板" + File.separator + "报告模板.xlsx";
                        String outPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "问卷" + File.separator + "报告" + File.separator + dealer.getDealerName() + "报告.xlsx";
                        Workbook currentWorkbook = new Workbook(excelPath);
                        WorksheetCollection sheets = currentWorkbook.getWorksheets();

                        /**
                         * 封面Sheet
                         */
                        Worksheet coversheet = sheets.get("Cover");
                        HashMap<String, String> data = new HashMap<>();
                        data.put("code", dealer.getsDealerCode());
                        data.put("fullName", (dealer.getsDealerFullName()!=null)?dealer.getsDealerFullName():"");
                        data.put("area", dealer.getsArea());
                        data.put("city", (dealer.getsCity()!=null)?dealer.getsCity():"");
                        ExcelHelper.writeCover(coversheet, data);

                        /**
                         * 经销商得分汇总Sheet
                         */
                        Worksheet dealerScoreSummary = sheets.get("经销商得分汇总");
                        List<Summary> summaries = DBManager.getDBManager(mContext).getSummaries();
                        ExcelHelper.writeSummary(dealerScoreSummary, summaries);

                        /**
                         * DSAT指标得分详情sheet
                         */
                        Worksheet scoreDetails = sheets.get("DSAT指标得分详情");
                        List<Detail> details = DBManager.getDBManager(mContext).getDetails();
                        ExcelHelper.writeDetail(scoreDetails, details);

                        /**
                         * DSAT本期指标失分照片sheet
                         */
                        Worksheet defectImage = sheets.get("DSAT本期指标失分照片");
                        List<Map<String, String>> imageItems = DBManager.getDBManager(mContext).getImages();
                        List<HashMap<String, String>> defectImageRows = ExcelHelper.getDefectImageRows(scoreDetails, imageItems);
                        int j = 2;
                        for (int i=0; i<defectImageRows.size(); i++) {
                            HashMap<String, String> hashMap = defectImageRows.get(i);

                            String images = hashMap.get("imgs");
                            int line = ExcelHelper.writeLostInfo(defectImage, j, hashMap, images);
                            j = line + 3;

                        }



                        //sheets.add("DSAT本期指标失分照片1").copy(shi1);
                        // cells = shi1.getCells();

//                        HashMap<String, String> h = new HashMap<String, String>();
//                        h.put("chapter", "A1");
//                        h.put("item", "A1.1.1");
//                        h.put("description", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//                        h.put("value", "4");
//                        h.put("score", "0");
//                        h.put("error", "bbbbbbbbbbb安重哈哈哈aaaaaaaaaaaaaaaaaaaaaaaaddd大多数水水水水水水水水水水水水水水水水顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶顶哈bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb法兰克福");


//                        ExcelHelper.writeLostInfo(shi1, 2, h,new String[]{
//                                        "C:\\Users\\zhanghua\\Pictures\\a.jpg"
//                                });

                        currentWorkbook.save(outPath);
                        Toast.makeText(getActivity(), "报告已生成", Toast.LENGTH_SHORT).show();
                        DBManager.getDBManager(mContext).clearGGGG();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        /**
         * 上传回答答案
         */
        mUploadAnswer = view.findViewById(R.id.choice_upload_answer);
        mUploadAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentSelectedPosition < 0 ) {
                    Toast.makeText(getActivity(), "请选择一个经销商", Toast.LENGTH_SHORT).show();
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                            Looper.prepare();
                            AnswerResult result = new AnswerResult();
                            Dealer dealer = mDealers.get(mCurrentSelectedPosition);
                            String json = result.getJSON(dealer.getAnswerId(), getContext());
                            postJSON(json);
                            Looper.loop();
                        }
                    }.start();
                }
            }
        });

        mUploadImage = view.findViewById(R.id.choice_upload_image);
        mUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentSelectedPosition < 0 ) {
                    Toast.makeText(getActivity(), "请选择一个经销商", Toast.LENGTH_SHORT).show();
                } else {
                    final ProgressDialog mProgressDialog = new ProgressDialog(getActivity());
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.setTitle("正在上传");
                    mProgressDialog.show();

                    new Thread() {
                        @Override
                        public void run() {
                            Looper.prepare();

                            DBManager dbManager = DBManager.getDBManager(mContext);
                            Dealer dealer = mDealers.get(mCurrentSelectedPosition);
                            int dealerId = dealer.getAnswerId();

                            saveImageUpload(dbManager, dealerId);

                            List<AnswerImageUpload> imageUploads = filterImageUpload(dbManager, dealerId);
                            if (imageUploads.size() == 0) {
                                mProgressDialog.dismiss();

                                Toast.makeText(getActivity(), "没有要上传的图片", Toast.LENGTH_SHORT).show();
                            } else {

                                upLoadImageTask(mProgressDialog, imageUploads);
                                // 在进度条走完时删除Dialog
                                mProgressDialog.dismiss();
                                Toast.makeText(getActivity(), "图片上传完成", Toast.LENGTH_SHORT).show();
                            }
                            Looper.loop();
                        }
                    }.start();
                }

            }
        });


        if (mIsUploadPage) {
            mStartButton.setVisibility(View.GONE);
            mResultShow.setVisibility(View.GONE);
            mResultGenerate.setVisibility(View.GONE);
        } else {
            mUploadAnswer.setVisibility(View.GONE);
            mUploadImage.setVisibility(View.GONE);
        }

        return view;
    }





    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_CURRENT_SELECTED_DEALER, mCurrentSelectedPosition);
    }



    /**
     * #########################################################################################
     * 处理上传按钮方法
     * 上传答案数据
     * @param json
     */
    private void postJSON(String json) {
        OkHttpClient client = new OkHttpClient();
        FormBody formBody = new FormBody.Builder()
                .add("uploaddata", json)
                .add("token",mUser.getToken())
                .build();

        Request request = new Request.Builder()
                .url("http://qv4.bwing.com.cn/api/answer/uploaddataforandroid")
                .post(formBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                Toast.makeText(getActivity(), "上传完成", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 根据AnswerID取出此经销商中的所有图片, 然后存入AnswerImageUpload表中
     * @param dbManager
     * @param dealerId
     */
    private void saveImageUpload(DBManager dbManager, int dealerId) {
        List<AnswerQuestion> answerQuestions = dbManager.getAnswerQuestionsByAnswerId(dealerId);
        List<AnswerImageUpload> imageUploads = dbManager.getAnswerImageUploads(dealerId);
        for (AnswerQuestion answerQuestion : answerQuestions) {
            List<AnswerOption> answerOptions = dbManager.getAnswerOptionByQid(answerQuestion.getId());
            for (AnswerOption answerOption : answerOptions) {
                List<AnswerImage> answerImages = dbManager.getAnswerImageByOid(answerOption.getId());
                for (AnswerImage answerImage : answerImages) {
                    AnswerImageUpload imageUpload = new AnswerImageUpload();
                    imageUpload.setAnswerId(dealerId);
                    imageUpload.setQuid(answerQuestion.getMid());
                    imageUpload.setOptid(answerOption.getMid());
                    String imagePath = answerImage.getImagePath();
                    int position = imagePath.lastIndexOf("/");
                    String fileName = imagePath.substring(position+1);
                    imageUpload.setFileName(fileName);
                    imageUpload.setImagePath(imagePath);
                    if (!imageUploads.contains(imageUpload)) {
                        imageUpload.save();
                    }
                }
            }
        }
    }

    /**
     * 将已经上传过的图片过滤掉
     *
     */
    private List<AnswerImageUpload> filterImageUpload(DBManager dbManager, int dealerId) {
        List<AnswerImageUpload> imageUploads = dbManager.getAnswerImageUploads(dealerId);
        Iterator<AnswerImageUpload> iterator = imageUploads.iterator();
        while(iterator.hasNext()) {
            AnswerImageUpload imageUpload = iterator.next();
            if (imageUpload.isUploaded()) {
                iterator.remove();
            }
        }
        return imageUploads;
    }

    /**
     * 上传图片
     * @param imageUploads
     */
    private void upLoadImageTask(ProgressDialog mProgressDialog, List<AnswerImageUpload> imageUploads) {
        mProgressDialog.setMax(imageUploads.size());
        OkHttpClient client = new OkHttpClient();
        int i = 0;
        while (i < imageUploads.size() && mProgressDialog.getProgress() != imageUploads.size()) {
            try {
                AnswerImageUpload imageUpload = imageUploads.get(i);
                File file = new File(imageUpload.getImagePath());
                // form 表单形式上传
                RequestBody requestBody = null;
                if(file != null){
                    RequestBody imageData = RequestBody.create(IMAGE, file);
                    String filename = file.getName();
                    // 参数分别为， 请求key ，文件名称 ， RequestBody
                    requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                            .addFormDataPart("imgdata", file.getName(), imageData)
                            .addFormDataPart("answerid", imageUpload.getAnswerId()+"")
                            .addFormDataPart("quid", imageUpload.getQuid()+"")
                            .addFormDataPart("filename", imageUpload.getFileName())
                            .addFormDataPart("token",mUser.getToken())
                            .addFormDataPart("optid", imageUpload.getOptid()+"")
                            .build();

                }

                Request request = new Request.Builder()
                        .url("http://qv4.bwing.com.cn/api/answer/uploadimg")
                        .post(requestBody)
                        .build();

                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    String resultJSON = response.body().string();
                    JSONObject jsonObject = new JSONObject(resultJSON);
                    int code = jsonObject.getInt("code");
                    if (code == 1) {
                        imageUpload.setUploaded(true);
                        imageUpload.save();
                        mProgressDialog.incrementProgressBy(1);
                    } else {
                        throw new RuntimeException("上传图片失败");
                    }

                }
                i++;
            } catch (Exception e) {
            }
        }
    }


    /**
     * ###################################################################################
     * 显示经销商列表
     * 经销商列表RecyclerView内部类
     */
    public class DealerHolder extends RecyclerView.ViewHolder {

        private RadioButton mRadioButton;

        public DealerHolder(View itemView) {
            super(itemView);
            mRadioButton = itemView.findViewById(R.id.choice_item_dealer);
            mRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCurrentSelectedPosition = getAdapterPosition();
                    mAdapter.notifyDataSetChanged();

                }
            });
        }

        private void bind(Dealer dealer, int position) {
            mRadioButton.setText(dealer.getDealerName());
            if (mCurrentSelectedPosition == position) {
                mRadioButton.setChecked(true);
            } else {
                mRadioButton.setChecked(false);
            }
        }

    }

    public class DealerAdapter extends RecyclerView.Adapter<DealerHolder> {

        private List<Dealer> mDealers;

        public DealerAdapter(List<Dealer> dealers) {
            this.mDealers = dealers;
        }

        @Override
        public DealerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_dealer, parent, false);
            return new DealerHolder(itemView);
        }

        @Override
        public void onBindViewHolder(DealerHolder holder, int position) {
            Dealer dealer = mDealers.get(position);
            holder.bind(dealer, position);
        }

        @Override
        public int getItemCount() {
            return mDealers.size();
        }
    }
}
