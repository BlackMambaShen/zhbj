package com.example.liang.zhbj74.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/*
本地缓存
 */
public class LocalCacheUtils {

    private static final String LOCAL_CACHE_PATH=
            Environment.getExternalStorageDirectory().getAbsolutePath()+"/zhbj74_cache";
    //写本地缓存
    public void setLocalCache(String url, Bitmap bitmap){
        File dir=new File(LOCAL_CACHE_PATH);
        if (!dir.exists()||!dir.isDirectory()){
            dir.mkdirs();//创建文件夹
        }
        File cacheFile=new File(dir,url);
        //当前图片压缩到本地 图片格式，压缩比例0-100 输出流
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,new FileOutputStream(cacheFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    //读本地缓存
    public Bitmap getLocalCache(String url){
        File cacheFile=new File(LOCAL_CACHE_PATH,url);
        if (cacheFile.exists()){
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(cacheFile));
                return bitmap;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
