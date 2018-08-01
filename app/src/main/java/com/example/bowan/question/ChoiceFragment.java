package com.example.bowan.question;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bowan.question.entity.DBManager;
import com.example.bowan.question.entity.Dealer;
import com.example.bowan.question.entity.Questionnaire;
import com.example.bowan.question.util.GeneratePdf;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * 此类为选择问卷的Fragment, 功能是选择问卷页面的具体逻辑和视图
 */
public class ChoiceFragment extends Fragment {

    private static final String ARG_CURRENT_SELECTED_DEALER = "CHOICE_CURRENT_SELECTED_DEALER";


    private Button mStartButton;
    private Button mGenerateButton;
    private List<Dealer> mDealers;
    private int mCurrentSelectedPosition = -1;
    private DealerAdapter mAdapter;
    private RecyclerView mRecyclerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(ARG_CURRENT_SELECTED_DEALER);
        }

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
        mDealers = DBManager.getDBManager().getDealers();

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
                    startActivity(intent);
                }

            }
        });

        /**
         * 生成PDF按钮
         */
        mGenerateButton = view.findViewById(R.id.choice_generate_result);
        mGenerateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File pdfFilePath = getActivity().getExternalCacheDir();
                @SuppressLint("ResourceType") GeneratePdf generatePdf = new GeneratePdf(getActivity().getExternalCacheDir(), getResources().getString(R.raw.simsun));
                String result = generatePdf.generate("测试内容");
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle(R.string.generate_pdf_path_title);
                dialog.setMessage(pdfFilePath.getAbsolutePath());
                dialog.setCancelable(true);
                dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
            }
        });
        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(ARG_CURRENT_SELECTED_DEALER, mCurrentSelectedPosition);
    }



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
