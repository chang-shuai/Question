package com.example.bowan.question.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class PictureUtils {

    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {

        /**
         * 获取缩放比例inSampleSize
         */
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;  // 不加载到内存中
        BitmapFactory.decodeFile(path, options);
        float srcWidth = options.outWidth;  // 图片的原始宽
        float srcHeight = options.outHeight;// 图片的原始高
        int inSampleSize = 1;
        // 根据原图和希望的宽高得到缩放比例.
        if (srcWidth>destWidth || srcHeight>destHeight) {
            float widthScale = srcWidth/destWidth;
            float heightScale = srcHeight/destHeight;

            inSampleSize = Math.round(widthScale > heightScale ? widthScale : heightScale);
        }

        /**
         * 使用计算得到的缩放比例, 打开已经缩小的图片, 存入bitmap
         */
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options);

        return bitmap;
    }


    public static Bitmap getScaledBitmap(String path) {
        //Point point = new Point();
        //activity.getWindowManager().getDefaultDisplay().getSize(point);
        return getScaledBitmap(path, 200, 200);
    }

}
