package com.example.bowan.question;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bowan.question.entity.AnswerImage;
import com.example.bowan.question.entity.FolderBean;
import com.example.bowan.question.util.ImageLoader;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectImageFragment extends Fragment {

    private static final int DATA_LOADED = 0X110;
    private static String ARG_ANSWER_OPTION_ID = "arg_answer_option_id";
    private int mOid;


    private GridView mGridView;
    private RecyclerView mRecyclerView;
    private List<String> mImgs;
    private ImageAdapter mImageAdapter;

    private RelativeLayout mBottomLy;
    private TextView mDirName;
    private TextView mDirCount;

    private File mCurrentDir;
    private int mMaxCount;

    private List<FolderBean> mFolderBeans = new ArrayList<FolderBean>();
    private ProgressDialog mProgressDialog;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == DATA_LOADED) {
                mProgressDialog.dismiss();
                //绑定数据到VIew中
                data2View();
            }
        }
    };

    private void data2View() {
        if (mCurrentDir == null) {
            Toast.makeText(getActivity(), "未扫描到任何图片", Toast.LENGTH_SHORT).show();
            return;
        } else {
            mImgs = Arrays.asList(mCurrentDir.list());
            mImageAdapter = new ImageAdapter(getActivity(), mImgs, mCurrentDir.getAbsolutePath());
            mGridView.setAdapter(mImageAdapter);
//            mRecyclerView.setAdapter(mImageAdapter);
//            GridLayoutManager layoutManage = new GridLayoutManager(getContext(), 4);
//            mRecyclerView.setLayoutManager(layoutManage);


            mDirCount.setText(mMaxCount+"");
            mDirName.setText(mCurrentDir.getName());

        }
    }

    public static SelectImageFragment newInstance(int oid) {
        Bundle args = new Bundle();
        args.putInt(ARG_ANSWER_OPTION_ID, oid);
        SelectImageFragment fragment = new SelectImageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOid = getArguments().getInt(ARG_ANSWER_OPTION_ID);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_image, container, false);

        initView(view);
        initDatas();
        initEvent();
        return view;
    }

    /**
     * 利用ContentProvider扫描手机中的所有图片
     */
    private void initDatas() {
        if ( !Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getActivity(), "当前储存卡不可用!", Toast.LENGTH_SHORT).show();
        }

        mProgressDialog = ProgressDialog.show(getActivity(), null, "正在加载...");

        new Thread() {
            @Override
            public void run() {
                Uri mImgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver cr = getActivity().getContentResolver();
                Cursor cursor = cr.query(mImgUri,
                        null,
                        MediaStore.Images.Media.MIME_TYPE +
                                "=? or " + MediaStore.Images.Media.MIME_TYPE +
                                "=?", new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                Set<String> mDirPaths = new HashSet<String>();
                while (cursor.moveToNext()) {
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null) {
                        continue;
                    }

                    String dirPath = parentFile.getAbsolutePath();
                    if (!dirPath.equals("/storage/emulated/0/DCIM/Camera")) {
                        continue;
                    }


                    FolderBean folderBean = null;

                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        folderBean = new FolderBean();
                        folderBean.setDir(dirPath);
                        folderBean.setFirstImgPath(path);
                    }

                    if (parentFile.list() == null) {
                        continue;
                    } else {
                        int picSize = parentFile.list(new FilenameFilter() {
                            @Override
                            public boolean accept(File dir, String name) {
                                if (name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith("png")) {
                                    return true;
                                }
                                return false;
                            }
                        }).length;

                        folderBean.setCount(picSize);

                        mFolderBeans.add(folderBean);

                        if (picSize > mMaxCount) {

                            mMaxCount = picSize;
                            mCurrentDir = parentFile;
                        }

                    }

                }
                cursor.close();
                // 通知Handler图片扫描完成
                mHandler.sendEmptyMessage(DATA_LOADED);
            }
        }.start();

    }


    private void initEvent() {
    }

    private void initView(View view) {
        mGridView = view.findViewById(R.id.id_gridView);
//        mRecyclerView = view.findViewById(R.id.image_loader_recycler_view);
        mBottomLy = view.findViewById(R.id.id_bottom_ly);
        mDirName = view.findViewById(R.id.id_dir_name);
        mDirCount = view.findViewById(R.id.id_dir_count);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_image_loader_list, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.image_loader_confirm:
                Set<String> imgPaths = ImageAdapter.mSelectImg;
                for (String imgPath : imgPaths) {
                    AnswerImage answerImage = new AnswerImage();
                    answerImage.setOid(mOid);
                    answerImage.setImagePath(imgPath);
                    answerImage.save();
                }
                imgPaths.clear();
                getActivity().finish();
        }


        return super.onOptionsItemSelected(item);
    }
}
