package com.example.liang.zhbj74;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.example.liang.zhbj74.utils.PrefUtils;

public class SplashActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        RelativeLayout rl_root = findViewById(R.id.rl_root);
        //旋转动画
        RotateAnimation animation=new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,
        0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        //缩放动画
        ScaleAnimation animation1=new ScaleAnimation(0,1,0,1,
                Animation.RELATIVE_TO_SELF,
                0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        animation1.setDuration(1000);
        animation1.setFillAfter(true);
        //渐变动画
        AlphaAnimation alphaAnimation=new AlphaAnimation(0,1);
        alphaAnimation.setDuration(1000);
        alphaAnimation.setFillAfter(true);
        //动画集合
        AnimationSet set=new AnimationSet(true);
        set.addAnimation(animation);
        set.addAnimation(animation1);
        set.addAnimation(alphaAnimation);
        //启动动画
        rl_root.startAnimation(set);

        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            //动画结束 跳转页面
                boolean isFirstEnter = PrefUtils.getBoolean(SplashActivity.this, "is_first_enter", true);
                if (isFirstEnter){
                    //第一次进入，跳新手引导
                     intent=new Intent(getApplicationContext(),GuideActivity.class);
                }else {
                    //否则跳主页面
                     intent=new Intent(getApplicationContext(),MainActivity.class);
                }
                startActivity(intent);
                finish();//结束当前页面
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
