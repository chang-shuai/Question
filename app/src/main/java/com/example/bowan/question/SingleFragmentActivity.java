package com.example.bowan.question;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * 所有的本来继承AppCompatActivity的类, 现在都继承SingleFragmentActivity抽象类.
 *
 * 此抽象类继承了AppCompatActivity类, 此类默认使用`activity_fragment`布局, 此布局中
 * 只包含一个`FrameLayout`的碎片控件. 此类的实现类应重写`createFragment`方法, 指定碎片
 * 控件的Fragment子类.
 *
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    /**
     * 继承类必须重写此抽象方法, 返回默认布局文件中的碎片控件关联的Java类.
     * @return
     */
    protected abstract Fragment createFragment();

    /**
     * 若继承类需要加载其他布局文件, 请重写此方法.
     * @return 布局文件的Id
     */
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());


        /**
         * 碎片布局文件`fragment_container`没有制定任何Java类, 需要调用实现类的
         * `createFragment`方法, 获得需要关联的Java类. 并用Fragment的事务添加绑定
         * 后提交.
         */
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = createFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

}
