package com.example.liang.zhbj74.utils;

import android.content.Context;

/*
网络缓存工具类
 */
public class CacheUtils {

    /*
    以url为key，以json为value，保存在本地
     */
    public static void setCache(String url, String json, Context context){
        PrefUtils.setString(context,url,json);
    }

    /*
    获取缓存
     */
    public static String getCache(String url,Context context){
        return PrefUtils.getString(context,url,null);
    }

}
