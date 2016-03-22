package com.example.apple.photoutil.base;

import android.app.Dialog;
import android.support.v4.app.FragmentActivity;

import com.example.apple.photoutil.R;

/**
 * 作    者：高学军
 * 时    间：16/3/18
 * 描    述：
 * 修改时间：
 */
public abstract class BaseActivity extends FragmentActivity {


    //进度条diaolog
    private Dialog dialog;


    //初始化头部控件的方法
    public abstract void initHeader();

    //查找控件的方法
    public abstract void initWidget();

    //设置状态的方法
    public abstract void setWidgetState();


    /**
     * 启动一个dialog
     */
    protected void showProgressDialog() {
        dialog = new Dialog(this, R.style.MyDialog);
        dialog.setContentView(R.layout.proessdialog);
        dialog.show();
    }

    /**
     * 关闭dialog
     */
    protected void closeProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

}
