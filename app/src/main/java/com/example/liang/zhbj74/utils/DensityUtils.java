package com.example.liang.zhbj74.utils;

import android.content.Context;

public class DensityUtils {
    //将dp转成px
    public static int dip2px(float dip, Context context){
        float density = context.getResources().getDisplayMetrics().density;
        int px= (int) (dip*density);
        return px;
    }

    public static float px2dip(int px,Context context){
        float density = context.getResources().getDisplayMetrics().density;
        float dp=px/density;
        return dp;
    }
}
