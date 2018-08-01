package com.example.bowan.question;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bowan.question.util.PictureUtils;

import java.io.File;

public class CameraPhotoFileFragment extends DialogFragment {

    private static final int LAUNCH_CAMERA = 0;
    private static final int LAUNCH_PHOTO_FILE = 1;
    private static final int REQUEST_PHOTO = 2;
    public static final String RESULT_BITMAP = "result_bitmap";



    private String[] mItems = new String[]{"相机", "相册"};
    private Uri mImageUri;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_camera_or_photo)
                .setItems(mItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which) {
                            case LAUNCH_CAMERA:
                                mImageUri = createImageFile();
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                                startActivityForResult(intent, REQUEST_PHOTO);

                                Bitmap bitmap = PictureUtils.getScaledBitmap(mImageUri.getPath(),getActivity());
                                Intent intent1 = new Intent();
                                intent.putExtra(RESULT_BITMAP, bitmap);
                                getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent1);

                                break;
                            case LAUNCH_PHOTO_FILE:
                                Toast.makeText(getActivity(), "这是1", Toast.LENGTH_SHORT).show();

                                break;
                            default:
                                break;
                        }


                    }
                });

        return builder.create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PHOTO:
                if (resultCode == Activity.RESULT_OK) {

                }
                break;
        }
    }

    /**
     * 获取保存图片的路径
     * @return
     */
    private Uri createImageFile() {
        String storePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "问卷";
        File appDir = new File(storePath);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        return Uri.fromFile(file);
    }

}
