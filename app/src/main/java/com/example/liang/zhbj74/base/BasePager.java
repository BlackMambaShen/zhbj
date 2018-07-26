package com.example.liang.zhbj74.base;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.liang.zhbj74.MainActivity;
import com.example.liang.zhbj74.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

//5个标签页的基类
public class BasePager {
    public Activity mActivity;
    public TextView tvTitle;
    public ImageButton btnMenu;
    private TextView tv_title;
    public View mRootView;
    private ImageButton btn_menu;
    public FrameLayout fl_content;//空的帧布局对象

    public ImageButton btnPhoto;//组图切换按钮

    public BasePager(Activity activity){

         mActivity=activity;
         mRootView = initView();
    }
    public View initView(){
        View view = View.inflate(mActivity, R.layout.base_pager, null);
        tvTitle =(TextView) view.findViewById(R.id.tv_title);
        btnMenu =(ImageButton) view.findViewById(R.id.btn_menu);
        btnPhoto =(ImageButton) view.findViewById(R.id.btn_photo);
        fl_content =(FrameLayout)view.findViewById(R.id.fl_content);
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
        return view;
    }

    /*
    打开或者关闭侧边栏
    */
    private void toggle() {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();//如果状态是开，调用就关；反之亦然
    }

    public void initData(){

    }
}
