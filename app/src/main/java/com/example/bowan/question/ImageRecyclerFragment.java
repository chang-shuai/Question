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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.bowan.question.util.PictureUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageRecyclerFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private ImageButton mImageButton;
    private AlertDialog.Builder mBuilder;
    private File mImageFile;
    private List<Bitmap> mBitmaps = new ArrayList<>();
    private ImageAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_recycler, container, false);
        init(view);
        initBuilder();
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageFile = createImageFile();
                mBuilder.show();
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);

        return view;
    }

    private void init(View view) {
        mImageButton = view.findViewById(R.id.add_image_button);
        mRecyclerView = view.findViewById(R.id.image_recycler_view);
        mBuilder = new AlertDialog.Builder(getActivity());
    }
    private void initBuilder() {
        mBuilder.setTitle(R.string.select_camera_or_photo)
                .setItems(new String[]{"相机", "相册"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mImageFile));
                                startActivityForResult(intent, 1);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                Bitmap bitmap = PictureUtils.getScaledBitmap(mImageFile.getPath(),getActivity());
                mBitmaps.add(bitmap);
                mAdapter = new ImageAdapter(mBitmaps);
                mRecyclerView.setAdapter(mAdapter);
            }
        }
    }

    private class ImageHolder extends RecyclerView.ViewHolder {

        private ImageView mImageView;

        public ImageHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.image_item);

        }
    }

    private class ImageAdapter extends RecyclerView.Adapter<ImageHolder> {

        private List<Bitmap> mBitmaps;

        public ImageAdapter(List<Bitmap> bitmaps) {
            mBitmaps = bitmaps;
        }

        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_image, parent, false);

            return new ImageHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ImageHolder holder, int position) {
            Bitmap bitmap = mBitmaps.get(position);
            holder.mImageView.setImageBitmap(bitmap);
        }

        @Override
        public int getItemCount() {
            return mBitmaps.size();
        }
    }
}
