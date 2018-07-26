package com.example.liang.zhbj74.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

/*
自定义三级缓存图片加载工具
 */
public class MyBitmapUtils {
    private LocalCacheUtils localCacheUtils;
    private NetCacheUtils netCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;
    public MyBitmapUtils(){
        mMemoryCacheUtils=new MemoryCacheUtils();
        localCacheUtils=new LocalCacheUtils();
        netCacheUtils=new NetCacheUtils(localCacheUtils,mMemoryCacheUtils);
    }
    public void display(ImageView imageView, String url) {
        //优先从内存中加载图片
        Bitmap bitmap = mMemoryCacheUtils.getMemoryCache(url);
        if (bitmap!=null){
            imageView.setImageBitmap(bitmap);
            return;
        }

        //从本地加载图片了
         bitmap = localCacheUtils.getLocalCache(url);
        if (bitmap!=null){
            imageView.setImageBitmap(bitmap);
            //写内存缓存
            mMemoryCacheUtils.setMemoryCache(url,bitmap);
            return;
        }
        //从网络中加载图片
        netCacheUtils.getBitmapFromNet(imageView,url);
    }
}
