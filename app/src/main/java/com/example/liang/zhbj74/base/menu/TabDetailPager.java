package com.example.liang.zhbj74.base.menu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.liang.zhbj74.NewsDetailActivity;
import com.example.liang.zhbj74.R;
import com.example.liang.zhbj74.base.BaseMenuDetailPager;
import com.example.liang.zhbj74.domain.NewsTabBean;
import com.example.liang.zhbj74.domain.NewsMenu;
import com.example.liang.zhbj74.global.GlobalConstants;
import com.example.liang.zhbj74.utils.CacheUtils;
import com.example.liang.zhbj74.utils.PrefUtils;
import com.example.liang.zhbj74.view.PullToRefreshListView;
import com.example.liang.zhbj74.view.TopNewsViewPager;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Handler;

/*
页签的页面对象
 */
public class TabDetailPager extends BaseMenuDetailPager {
    private final String mUrl;
    private NewsMenu.NewsTabData mTabData;//单个页签的网络数据
    private ArrayList<NewsTabBean.TopNews> topnews;
    private TopNewsViewPager vp_top_news;
    private TextView tvTitle;
    private PullToRefreshListView lv_list;
    private ArrayList<NewsTabBean.NewsData> mNewsList;
    private NewsAdapter newsAdapter;
    private String mMoreUrl;
    private NewsTabBean.NewsData news;
    private android.os.Handler mHandler;

    public TabDetailPager(Activity activity, NewsMenu.NewsTabData newsTabData) {
        super(activity);
        mTabData=newsTabData;
         mUrl = GlobalConstants.SERVER_URL+mTabData.url;
    }

    @Override
    public View initView() {
//         view=new TextView(mActivity);
//       view.setText(mTabData.title);//此处空指针
//        view.setTextColor(Color.RED);
//        view.setTextSize(22);
//        view.setGravity(Gravity.CENTER);
        View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
         //给listview添加头布局
        View mHeaderView = View.inflate(mActivity, R.layout.list_item_header, null);
        lv_list =(PullToRefreshListView) view.findViewById(R.id.lv_list);
        lv_list.addHeaderView(mHeaderView);
        //5.前端界面设置回调
        lv_list.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //6.刷新数据
                getDataFromServer();
                //收起下拉刷新控件
                lv_list.onRefreshComplete();
            }

            @Override
            public void onLoadMore() {
                //判断是否有下一页数据
                if (mMoreUrl!=null){
                    //有下一页
                    getMoreDataFromServer();
                }else {
                    //没有下一页
                    Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT).show();
                    //没有数据时，也要收起控件
                    lv_list.onRefreshComplete();
                }
            }
        });
        vp_top_news = (TopNewsViewPager) mHeaderView.findViewById(R.id.vp_top_news);
        tvTitle = (TextView) mHeaderView.findViewById(R.id.tv_title);
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int headerViewsCount = lv_list.getHeaderViewsCount();//获取头布局数量
                position=position-headerViewsCount;//减去头布局的占位
                System.out.println("第"+position+"个被点击了");
                news = mNewsList.get(position);
                String readIds = PrefUtils.getString(mActivity, "read_ids", "");
                if (readIds.contains(news.id+"")){//只有不包含当前id，才存储到sp中
                    readIds=readIds+news.id+",";
                    PrefUtils.setString(mActivity,"read_ids",readIds);
                }
                //要将被点击的item文字颜色改为灰色
                TextView tv_title =(TextView) view.findViewById(R.id.tv_title);
                tv_title.setTextColor(Color.GRAY);

                //跳到新闻详情页面
                Intent intent=new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url",news.url);
                mActivity.startActivity(intent);
            }
        });
        return view;
    }

    /*
    加载下一页数据
     */
    private void getMoreDataFromServer() {
        new Thread(){
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder().url(mMoreUrl).build();
                try {
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            processData(result,true);
//                            CacheUtils.setCache(mUrl,result,mActivity);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //
    class NewsAdapter extends BaseAdapter{

        private final BitmapUtils bitmapUtils;

        public NewsAdapter(){
             bitmapUtils=new BitmapUtils(mActivity);
            bitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
        }
        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public NewsTabBean.NewsData getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView==null){
                convertView=View.inflate(mActivity,R.layout.list_item_news,null);
                holder=new ViewHolder();
                holder.ivIcon=(ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tvTitle=(TextView) convertView.findViewById(R.id.tv_title);
                holder.tvDate=(TextView) convertView.findViewById(R.id.tv_date);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder) convertView.getTag();
            }
            NewsTabBean.NewsData news = getItem(position);
            holder.tvTitle.setText(news.title);
            holder.tvDate.setText(news.pubdate);

            //根据本地记录来更改标记 已读未读
            String readIds = PrefUtils.getString(mActivity, "read_ids", "");
            if (readIds.contains(news.id+"")){
                holder.tvTitle.setTextColor(Color.GRAY);
            }else {
                holder.tvTitle.setTextColor(Color.BLACK);
            }
            bitmapUtils.display(holder.ivIcon,news.listimage);
            return convertView;
        }
    }

    static class ViewHolder{
        public ImageView ivIcon;
        public TextView tvTitle;
        public TextView tvDate;
    }


    //头条新闻数据适配器
    class TopNewsAdapter extends PagerAdapter{

        private final BitmapUtils bitmapUtils;

        public TopNewsAdapter(){
             bitmapUtils=new BitmapUtils(mActivity);
             bitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);//设置加载中的默认图片
        }
        @Override
        public int getCount() {
            return topnews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView view=new ImageView(mActivity);
//            view.setImageResource(R.drawable.topnews_item_default);
            view.setScaleType(ImageView.ScaleType.FIT_XY);
            //下载图片，将图片设置给IV，避免内存溢出，缓存
             String url = topnews.get(position).topimage;
            //BitmapUtils帮你实现
            bitmapUtils.display(view,"http://192.168.3.8:8080/zhbj/10007/1452327318UU91.jpg");
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }
    }

    @Override
    public void initData() {
        String cache = CacheUtils.getCache(mUrl, mActivity);
        if (!TextUtils.isEmpty(cache)){
            processData(cache,false);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        new Thread(){
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder().url(mUrl).build();
                try {
                    Response response = client.newCall(request).execute();
                    String result = response.body().string();
//                    System.out.println("news返回结果："+result);
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            processData(result,false);
                            CacheUtils.setCache(mUrl,result,mActivity);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @SuppressLint({"HandlerLeak", "ClickableViewAccessibility"})
    private void processData(String json, boolean isMore) {
        Gson gson=new Gson();
        NewsTabBean newsTabBean = gson.fromJson(json, NewsTabBean.class);
        topnews = newsTabBean.data.topnews;
        String moreUrl = newsTabBean.data.more;
        if (! TextUtils.isEmpty(moreUrl)){
            //下一页数据链接
             mMoreUrl=GlobalConstants.SERVER_URL+moreUrl;
        }else {
            mMoreUrl=null;
        }
        if (!isMore){
            if (topnews!=null){
                vp_top_news.setAdapter(new TopNewsAdapter());
                vp_top_news.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        //更新头条新闻的标题
                        NewsTabBean.TopNews topNews = topnews.get(position);
                        tvTitle.setText(topNews.title);
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
                //手动更新第一页标题
                tvTitle.setText(topnews.get(0).title);
            }
            //根据服务器返回的数据
            mNewsList = newsTabBean.data.news;
            if (mNewsList!=null){
                newsAdapter = new NewsAdapter();
                lv_list.setAdapter(newsAdapter);
            }

            if (mHandler==null){
                mHandler=new android.os.Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        int currentItem = vp_top_news.getCurrentItem();
                        currentItem++;
                        if (currentItem>topnews.size()-1){
                            currentItem=0;
                        }
                        vp_top_news.setCurrentItem(currentItem);
                        mHandler.sendEmptyMessageDelayed(0,3000);//发送延迟3秒的消息
                    }
                };
                mHandler.sendEmptyMessageDelayed(0,3000);//发送延迟3秒的消息
                vp_top_news.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()){
                            case MotionEvent.ACTION_DOWN:
                                //停止广告自动轮播
                                //删除handler的所有消息
                                mHandler.removeCallbacksAndMessages(null);
                                break;
                            case MotionEvent.ACTION_UP:
                                mHandler.sendEmptyMessageDelayed(0,3000);
                                break;
                        }
                        return false;
                    }
                });
            }
        }else {
            //加载更多数据
            ArrayList<NewsTabBean.NewsData> moreNews = newsTabBean.data.news;
            mNewsList.addAll(moreNews);//将数据追加在原来的集合中
            //刷新listview
            newsAdapter.notifyDataSetChanged();
        }
    }
}
