package com.example.bowan.question;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * 登录页面的Activity, 其布局中包含一个碎片, 具体的逻辑和视图等
 * 内容都在碎片中完成.
 */
public class LoginActivity extends SingleFragmentActivity {

    /**
     * 重写父类的此方法, 返回此类中的碎片控件需要绑定的Java类.
     * @return
     */
    @Override
    protected Fragment createFragment() {
        return new LoginFragment();
    }

    
}
