package com.example.bowan.question;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.bowan.question.entity.User;


/**
 * 选择问卷页面的Activity, 其布局中包含一个碎片, 具体的逻辑和视图等
 * 内容都在碎片中完成.
 */
public class ChoiceActivity extends SingleFragmentActivity {
    private static final String IS_UPLOAD_PAGE = "is_upload_page";
    private static final String USER = "user";

    /**
     * 重写父类的此方法, 返回此类中的碎片控件需要绑定的Java类.
     * @return
     */
    @Override
    protected Fragment createFragment() {
        boolean isUploadPage = getIntent().getBooleanExtra(IS_UPLOAD_PAGE, false);
        User user = (User) getIntent().getSerializableExtra(USER);
        return ChoiceFragment.newInstance(isUploadPage, user);
    }

    public static Intent newIntent(Context packageContext, boolean isUpload, User user) {
        Intent intent = new Intent(packageContext, ChoiceActivity.class);
        intent.putExtra(IS_UPLOAD_PAGE, isUpload);
        intent.putExtra(USER, user);
        return intent;
    }

}
