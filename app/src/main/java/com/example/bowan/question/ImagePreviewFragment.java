package com.example.bowan.question;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class ImagePreviewFragment extends Fragment {
    private static final String ARG_IMAGE_PATH = "arg_image_path";

    private String mImagePath;

    public static ImagePreviewFragment newInstance(String imagePath) {
        Bundle args = new Bundle();
        args.putString(ARG_IMAGE_PATH, imagePath);
        ImagePreviewFragment fragment = new ImagePreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImagePath = getArguments().getString(ARG_IMAGE_PATH);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_preview, container, false);

        ImageView imageView = view.findViewById(R.id.image_preview_image_view);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2; //图片的长宽都是原来的1/2
        Bitmap bm = BitmapFactory.decodeFile(mImagePath, options);
        imageView.setImageBitmap(bm);
        return view;
    }
}
