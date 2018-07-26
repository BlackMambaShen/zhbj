package com.example.liang.zhbj74.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.BiFunction;

/*
网络缓存
 */
public class NetCacheUtils {
    private LocalCacheUtils mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;
    public NetCacheUtils(LocalCacheUtils localCacheUtils, MemoryCacheUtils memoryCacheUtils) {
        mLocalCacheUtils=localCacheUtils;
        mMemoryCacheUtils=memoryCacheUtils;
    }

    public void getBitmapFromNet(ImageView imageView, String url) {
       // AsyncTask 异步封装的工具，可以实现异步请求及主界面更新（对线程池和handler的封装）
        new BitmapTask().execute(imageView,url);//启动asyncTask
    }
    /*
    三个泛型的意义：
    第一个泛型：doInBackground参数类型
    第二个泛型：onProgressUpdate参数类型
    第三个泛型：onPostExecute,doInBackground参数类型
     */
    class BitmapTask extends AsyncTask<Object,Integer,Bitmap>{

        private ImageView imageView;

        //后台处理的逻辑,正在加载,运行在子线程(核心方法)，可以直接异步请求
        protected Bitmap doInBackground(Object... params) {
             imageView = (ImageView) params[0];
            String url =(String) params[1];
            //开始下载图片
            Bitmap bitmap=download(url);
            return bitmap;
        }

        //预加载,主线程
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //进度更新的方法，运行在主线程
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        //加载结束之后的方法,运行在主线程(核心方法),可以直接更新UI
        protected void onPostExecute(Bitmap result) {
            if (result!=null){
                //设置图片给imageView
                imageView.setImageBitmap(result);
                //写本地缓存
//                mLocalCacheUtils.setLocalCache(url,result);
                //写内存缓存
//                mMemoryCacheUtils.setMemoryCache(url,result);
            }
            super.onPostExecute(result);
        }
    }

    private Bitmap download(String path) {
        HttpURLConnection conn=null;
        try {
                            URL url=new URL(path);
                            conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setConnectTimeout(5000);//连接超时
                            conn.setReadTimeout(5000);//读取超时
                            int code = conn.getResponseCode();
                            if (code==200){
                                InputStream inputStream = conn.getInputStream();
                                //根据输入流生成bitmap对象
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                return bitmap;
                            }
                        }catch (Exception e){

                        }finally {
                            if (conn!=null){
                                conn.disconnect();
                            }
                }
        return null;
    }
}
