package com.example.liang.zhbj74.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liang.zhbj74.MainActivity;
import com.example.liang.zhbj74.base.BaseMenuDetailPager;
import com.example.liang.zhbj74.base.BasePager;
import com.example.liang.zhbj74.base.menu.InteractMenuDetailPager;
import com.example.liang.zhbj74.base.menu.NewsMenuDetailPager;
import com.example.liang.zhbj74.base.menu.PhotoMenuDetailPager;
import com.example.liang.zhbj74.base.menu.TopicMenuDetailPager;
import com.example.liang.zhbj74.domain.NewsMenu;
import com.example.liang.zhbj74.fragment.LeftMenuFragment;
import com.example.liang.zhbj74.global.GlobalConstants;
import com.example.liang.zhbj74.utils.CacheUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.xutils.http.HttpMethod;

import java.io.IOException;
import java.util.ArrayList;

//首页
public class NewsCenterPager extends BasePager {
    //菜单详情页集合
    private ArrayList<BaseMenuDetailPager>menuDetailPagers;
    private NewsMenu data;

    public NewsCenterPager(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        //要给帧布局填充布局对象
//        TextView view=new TextView(mActivity);
//        view.setText("新闻中心");
//        view.setTextColor(Color.RED);
//        view.setTextSize(22);
//        view.setGravity(Gravity.CENTER);
//        fl_content.addView(view);
        tvTitle.setText("新闻");
        btnMenu.setVisibility(View.VISIBLE);
        //先判断有没有缓存,如果有的话，就加载缓存
        String cache = CacheUtils.getCache(GlobalConstants.CATEGORY_URL, mActivity);
        if (!TextUtils.isEmpty(cache)){
            //直接拿缓存解析数据
            processData(cache);
        }
            //请求服务器，获取数据
            //开源框架 XUtils
            getDataFromServer();

    }

    private void getDataFromServer() {
        new Thread(){
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url(GlobalConstants.CATEGORY_URL).build();
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();
                    System.out.println("服务器返回结果："+result);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            processData(result);
                        }
                    });
                    //写缓存
                    CacheUtils.setCache(GlobalConstants.CATEGORY_URL, result, mActivity);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    //解析数据
    private void processData(String json) {
        //Gson
        Gson gson=new Gson();
         data = gson.fromJson(json, NewsMenu.class);
        //获取侧边栏对象
        MainActivity mainUI = (MainActivity) this.mActivity;
        LeftMenuFragment fragment = mainUI.getLeftMenuFragment();
        //给侧边栏设置数据
        fragment.setMenuData(data.data);
        //初始化4个菜单详情页
        menuDetailPagers=new ArrayList<BaseMenuDetailPager>();
        menuDetailPagers.add(new NewsMenuDetailPager(mActivity,data.data.get(0).children));
        menuDetailPagers.add(new TopicMenuDetailPager(mActivity));
        menuDetailPagers.add(new PhotoMenuDetailPager(mActivity,btnPhoto));
        menuDetailPagers.add(new InteractMenuDetailPager(mActivity));
        //将新闻菜单详情页设置为默认页面
        setCurrentDetailPager(0);

    }

    //设置菜单详情页
    public void setCurrentDetailPager(int position){
        //重新给framelayout添加内容
        BaseMenuDetailPager pager = menuDetailPagers.get(position);//获取应该显示的页面
        View view = pager.mRootView;//当前页的布局
        //清楚之前旧的布局
        fl_content.removeAllViews();
        fl_content.addView(view);//给帧布局添加布局
        //初始化页面数据
        pager.initData();
        //更新标题
        tvTitle.setText(data.data.get(position).title);
        //如果是组图页面，需要显示切换按钮
        if (pager instanceof PhotoMenuDetailPager){
            btnPhoto.setVisibility(View.VISIBLE);
        }else {
            //隐藏切换按钮
            btnPhoto.setVisibility(View.GONE);
        }
    }
}
