package com.example.apple.photoutil.Fragmeng;

import android.support.v4.app.Fragment;

/**
 * 作    者：高学军
 * 时    间：16/3/18
 * 描    述：
 * 修改时间：
 */
public abstract class BaseFragment extends Fragment{

    //初始化头部控件的方法
    public abstract void initHeader();

    //查找控件的方法
    public abstract void initWidget();

    //设置状态的方法
    public abstract void setWidgetState();
}
