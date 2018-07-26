package com.example.liang.zhbj74.base.menu;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.liang.zhbj74.R;
import com.example.liang.zhbj74.base.BaseMenuDetailPager;
import com.example.liang.zhbj74.domain.PhotosBean;
import com.example.liang.zhbj74.global.GlobalConstants;
import com.example.liang.zhbj74.utils.CacheUtils;
import com.example.liang.zhbj74.utils.MyBitmapUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

/*
菜单详情页 组图
 */
public class PhotoMenuDetailPager extends BaseMenuDetailPager {


    private ListView lv_photo;
    private GridView gv_photo;
    private ArrayList<PhotosBean.PhotoNews> mNewsList;
    private ImageButton mBtnPhoto;
    private boolean isListView=true;//标记当前是否是listview展示
    public PhotoMenuDetailPager(Activity activity, ImageButton btnPhoto) {
        super(activity);
        this.mBtnPhoto=btnPhoto;
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isListView){
                    //切成gridview
                    lv_photo.setVisibility(View.GONE);
                    gv_photo.setVisibility(View.VISIBLE);
                    mBtnPhoto.setImageResource(R.drawable.icon_pic_grid_type);
                    isListView=false;
                }else {
                    //切成listview
                    lv_photo.setVisibility(View.VISIBLE);
                    gv_photo.setVisibility(View.GONE);
                    mBtnPhoto.setImageResource(R.drawable.icon_pic_list_type);
                    isListView=true;
                }
            }
        });
    }

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.pager_photos_menu_detail, null);
        lv_photo = (ListView)view.findViewById(R.id.lv_photo);
        gv_photo = (GridView)view.findViewById(R.id.gv_photo);
        return view;
    }

    public void initData(){
        String cache = CacheUtils.getCache(GlobalConstants.PHOTOS_URL, mActivity);
        if (!TextUtils.isEmpty(cache)){
            processData(cache);
        }
        getDataFromServer();
    }

    private void getDataFromServer() {
        OkHttpClient client=new OkHttpClient();
        Request request = new Request.Builder().url(GlobalConstants.PHOTOS_URL).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String result = response.body().string();
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        processData(result);
                    }
                });
                CacheUtils.setCache(GlobalConstants.PHOTOS_URL,result,mActivity);
            }
        });
    }

    private void processData(String result) {
        Gson gson=new Gson();
        PhotosBean photosBean = gson.fromJson(result, PhotosBean.class);
         mNewsList = photosBean.data.news;
         lv_photo.setAdapter(new PhotoAdapter());
         gv_photo.setAdapter(new PhotoAdapter());
    }

    class PhotoAdapter extends BaseAdapter{
        private final MyBitmapUtils bitmapUtils;

        public PhotoAdapter(){
             bitmapUtils=new MyBitmapUtils();
//             bitmapUtils=new BitmapUtils(mActivity);
//             bitmapUtils.configDefaultLoadingImage(R.drawable.topnews_item_default);
        }
        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public PhotosBean.PhotoNews getItem(int position) {
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
                convertView=View.inflate(mActivity,R.layout.list_item_photos,null);
                holder=new ViewHolder();
                holder.ivPic=(ImageView) convertView.findViewById(R.id.iv_pic);
                holder.tvTitle=(TextView) convertView.findViewById(R.id.tv_title);
                convertView.setTag(holder);
            }else {
                holder=(ViewHolder) convertView.getTag();
            }

            PhotosBean.PhotoNews item = getItem(position);
            holder.tvTitle.setText(item.title);
            bitmapUtils.display(holder.ivPic,item.listimage);
            return convertView;
        }
    }

    static class ViewHolder{
        public ImageView ivPic;
        public TextView tvTitle;
    }
}
