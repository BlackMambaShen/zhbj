package com.example.liang.zhbj74;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout ll_control;
    private ImageButton btn_share;
    private ImageButton btn_textsize;
    private ImageButton btn_menu;
    private ImageButton btn_back;
    private WebView wb_news_detail;
    private ProgressBar pb_loading;
    private String mUrl;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
         ll_control = (LinearLayout)findViewById(R.id.ll_control);
        btn_back = (ImageButton)findViewById(R.id.btn_back);
        btn_share = (ImageButton)findViewById(R.id.btn_share);
        btn_textsize = (ImageButton)findViewById(R.id.btn_textsize);
        btn_menu = (ImageButton)findViewById(R.id.btn_menu);
        wb_news_detail = (WebView)findViewById(R.id.wb_news_detail);
         pb_loading = (ProgressBar)findViewById(R.id.pb_loading);
        ll_control.setVisibility(View.VISIBLE);
        btn_back.setVisibility(View.VISIBLE);
        btn_menu.setVisibility(View.GONE);

        btn_back.setOnClickListener(this);
        btn_textsize.setOnClickListener(this);
        btn_share.setOnClickListener(this);

         mUrl = getIntent().getStringExtra("url");
        wb_news_detail.loadUrl("http://www.itheima.com");
        WebSettings settings = wb_news_detail.getSettings();
        settings.setBuiltInZoomControls(true);//显示缩放按钮 wap网页不支持
        settings.setUseWideViewPort(true);//支持双击缩放 wap网页不支持
        settings.setJavaScriptEnabled(true);//支持js
        wb_news_detail.setWebViewClient(new WebViewClient(){
            //开始加载网页
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pb_loading.setVisibility(View.VISIBLE);
            }

            //网页加载结束
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb_loading.setVisibility(View.INVISIBLE);
            }

            //所有链接跳转会走此方法

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);//在跳转链接时强制在当前webview中加载
                return true;
            }
        });

//        wb_news_detail.goBack();//跳到上个页面
         wb_news_detail.setWebChromeClient(new WebChromeClient(){
             @Override
             public void onProgressChanged(WebView view, int newProgress) {
                 super.onProgressChanged(view, newProgress);
                 //进度发生变化

             }

             @Override
             public void onReceivedTitle(WebView view, String title) {
                 super.onReceivedTitle(view, title);
                 //网页标题
             }
         });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_textsize:
                showChooseDialog();
                break;
            case R.id.btn_share:
                showShare();
                break;
        }
    }

    private int mTempWhich;//记录临时选择的字体大小（点击确定之前）

    private int mCurrentWhich=2;//记录当前选中的字体大小（点击确定之后）
    /*
    展示选择字体大小的弹窗
     */
    private void showChooseDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("字体设置");
        String[]items=new String[]{"超大号字体","大号字体","正常字体","小号字体","超小号字体"};
        builder.setSingleChoiceItems(items, mCurrentWhich, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mTempWhich=which;
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //根据选择的字体，来修改网页字体的大小
                WebSettings settings = wb_news_detail.getSettings();
                switch (mTempWhich){
                    case 0://超大字体
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
                        break;
                    case 1://大字体
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2://正常字体
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3://小字体
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4://超小字体
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;
                }
                mCurrentWhich=mTempWhich;

            }
        });

        builder.setNegativeButton("取消",null);
        builder.show();
    }
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }
}
