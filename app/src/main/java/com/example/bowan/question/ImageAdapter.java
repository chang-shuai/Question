package com.example.bowan.question;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.bowan.question.util.ImageLoader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImageAdapter extends BaseAdapter {
        public static Set<String> mSelectImg = new HashSet<>();
        private String mDirPath;
        private List<String> mImgPaths;
        private LayoutInflater mInflater;

        public ImageAdapter(Context context, List<String> mDatas, String dirPath) {
            this.mDirPath = dirPath;
            this.mImgPaths = mDatas;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mImgPaths.size();
        }

        @Override
        public Object getItem(int position) {
            return mImgPaths.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder viewHolder;
            View view;
            if (convertView == null) {
                view = mInflater.inflate(R.layout.item_gridview, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.mImg = view.findViewById(R.id.id_item_image);
                viewHolder.mSelect = view.findViewById(R.id.id_item_select);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            final String filePath = mDirPath+"/"+mImgPaths.get(position);

            // 重置状态
            if (mSelectImg.contains(filePath)) {
                viewHolder.mImg.setColorFilter(Color.parseColor("#77000000"));
                viewHolder.mSelect.setImageResource(R.drawable.ic_pic_selected);
            } else {
                viewHolder.mImg.setImageResource(R.drawable.ic_pic_no);
                viewHolder.mSelect.setImageResource(R.drawable.ic_pic_unselected);
                viewHolder.mImg.setColorFilter(null);
            }



            ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(mDirPath+"/"+mImgPaths.get(position), viewHolder.mImg);

            viewHolder.mImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mSelectImg.contains(filePath)) {
                        viewHolder.mImg.setColorFilter(null);
                        viewHolder.mSelect.setImageResource(R.drawable.ic_pic_unselected);
                        mSelectImg.remove(filePath);
                    } else {
                        viewHolder.mImg.setColorFilter(Color.parseColor("#77000000"));
                        viewHolder.mSelect.setImageResource(R.drawable.ic_pic_selected);
                        mSelectImg.add(filePath);
                    }
                }
            });

            return view;
        }

        private class ViewHolder {
            ImageView mImg;
            ImageButton mSelect;
        }

    }