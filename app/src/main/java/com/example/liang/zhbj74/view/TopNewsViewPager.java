package com.example.liang.zhbj74.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/*
头条新闻自定义viewpager
 */
public class TopNewsViewPager extends ViewPager {
    private int startx;
    private int starty;

    public TopNewsViewPager(@NonNull Context context) {
        super(context);
    }

    public TopNewsViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    /*
    上下滑动不应该拦截

     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //拦截
        getParent().requestDisallowInterceptTouchEvent(true);

        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                 startx = (int) ev.getX();
                starty = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
               int endX = (int) ev.getX();
               int endY = (int) ev.getY();
               int dx=endX-startx;
               int  dy=endY-starty;
               if (Math.abs(dy)<Math.abs(dx)){
                   int currentItem = getCurrentItem();
                   //左右滑动
                   if (dx>0){
                       //向右滑
                       if (currentItem==0){
                           //第一个页面,需要拦截
                           getParent().requestDisallowInterceptTouchEvent(false);
                       }
                   }else {
                       //向左滑
                       int count = getAdapter().getCount();//item总数
                       if (currentItem==count-1){
                           //最后一个页面,需要拦截
                           getParent().requestDisallowInterceptTouchEvent(false);
                       }
                   }
               }else {
                   //上下滑动,需要拦截
                   getParent().requestDisallowInterceptTouchEvent(false);
               }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
