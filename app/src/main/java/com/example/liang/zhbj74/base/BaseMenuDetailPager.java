package com.example.liang.zhbj74.base;


import android.app.Activity;
import android.view.View;

public abstract class BaseMenuDetailPager {

    public View mRootView;
    public Activity mActivity;
    public BaseMenuDetailPager(Activity activity){
        mActivity=activity;
        mRootView=initView();
    }

    //初始化布局
    public abstract View initView();

    //初始化数据
    public void initData(){

    }
}
