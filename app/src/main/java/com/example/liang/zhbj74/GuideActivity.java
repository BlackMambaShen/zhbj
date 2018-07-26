package com.example.liang.zhbj74;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.liang.zhbj74.utils.DensityUtils;
import com.example.liang.zhbj74.utils.PrefUtils;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {

    private ViewPager vp_guide;
    private ArrayList<ImageView>imageViews;
    private int[] mImageIds=new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
    private LinearLayout ll_container;
    private ImageView iv_red_point;
    private int mPointDis;
    private Button btn_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
         vp_guide =(ViewPager) findViewById(R.id.vp_guide);
         ll_container = (LinearLayout)findViewById(R.id.ll_container);
         iv_red_point = (ImageView)findViewById(R.id.iv_red_point);
         btn_start = (Button)findViewById(R.id.btn_start);
        initData();
        vp_guide.setAdapter(new GuideAdapter());
        vp_guide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //当页面滑动过程中回调
                //更新小红点位置
                int lefMargin = (int)(mPointDis * positionOffset)+position*mPointDis;
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_red_point.getLayoutParams();
                params.leftMargin=lefMargin;
                iv_red_point.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
            //某个页面被选中
                if (position==imageViews.size()-1){
                    //最后一个页面显示开始体验的按钮
                    btn_start.setVisibility(View.VISIBLE);
                }else {
                    btn_start.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            //页面状态发生变化
            }
        });
        //视图树
        iv_red_point.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                iv_red_point.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //layout方法执行结束的回调
                //计算两个圆点的距离
                mPointDis = ll_container.getChildAt(1).getLeft() - ll_container.getChildAt(0).getLeft();
            }
        });

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //更新sp,已经不是第一次进入了
                PrefUtils.setBoolean(getApplicationContext(),"is_first_enter",false);
                //跳主页面
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });
    }

    //初始化数据
    private void initData(){
        imageViews=new ArrayList<ImageView>();
        for (int i = 0; i <mImageIds.length ; i++) {
            ImageView view=new ImageView(this);
            view.setBackgroundResource(mImageIds[i]);
            imageViews.add(view);
            //初始化小圆点
            ImageView point=new ImageView(this);
            point.setImageResource(R.drawable.shape_point_gray);//设置图片形状
            //初始化布局参数
            LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i>0){
                //设置左边距
                params.leftMargin= DensityUtils.dip2px(10,this);
            }
            point.setLayoutParams(params);
            ll_container.addView(point);
        }
    }

    class GuideAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        //初始化item的布局
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView view = imageViews.get(position);
            container.addView(view);
            return view;
        }

        //销毁item
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }
    }

}
