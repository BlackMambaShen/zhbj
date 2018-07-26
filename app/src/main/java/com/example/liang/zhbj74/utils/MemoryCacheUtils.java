package com.example.liang.zhbj74.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import java.lang.ref.SoftReference;
import java.util.HashMap;

/*
内存缓存
 */
public class MemoryCacheUtils {

//    private HashMap<String,SoftReference<Bitmap>>mMemoryCache=new HashMap<String,SoftReference<Bitmap>>();
    private LruCache<String,Bitmap>mMemoryCache;
    public MemoryCacheUtils(){
        long maxMemory = Runtime.getRuntime().maxMemory();
        //LruCache
        mMemoryCache=new LruCache<String, Bitmap>((int)maxMemory/8){
            //返回每个对象的大小
            protected int sizeOf(String key, Bitmap value) {
                int byteCount = value.getByteCount();//返回图片总大小
                return byteCount;
            }
        };
    }
    //写缓存
    public void setMemoryCache(String url,Bitmap bitmap){
////        mMemoryCache.put(url,bitmap);
//        SoftReference<Bitmap>soft=new SoftReference<Bitmap>(bitmap);//使用软引用包装起来
//        mMemoryCache.put(url,soft);
        mMemoryCache.put(url,bitmap);

    }

    //读缓存
    public Bitmap getMemoryCache(String url){
//        SoftReference<Bitmap> softReference = mMemoryCache.get(url);
//        if (softReference!=null){
//            Bitmap bitmap = softReference.get();
//            return bitmap;
//        }
        return mMemoryCache.get(url);
    }
}
