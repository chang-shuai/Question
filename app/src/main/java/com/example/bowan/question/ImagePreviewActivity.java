package com.example.bowan.question;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ImagePreviewActivity extends SingleFragmentActivity {

    private static final String EXTRA_IMAGE_PATH = "extra_image_path";

    @Override
    protected Fragment createFragment() {
        String imagePath = getIntent().getStringExtra(EXTRA_IMAGE_PATH);
        return ImagePreviewFragment.newInstance(imagePath);
    }

    public static Intent newIntent(Context context, String imagePath) {
        Intent intent = new Intent(context, ImagePreviewActivity.class);
        intent.putExtra(EXTRA_IMAGE_PATH, imagePath);
        return intent;
    }

}
