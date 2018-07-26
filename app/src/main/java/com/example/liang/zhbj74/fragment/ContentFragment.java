package com.example.liang.zhbj74.fragment;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.example.liang.zhbj74.MainActivity;
import com.example.liang.zhbj74.R;
import com.example.liang.zhbj74.base.BasePager;
import com.example.liang.zhbj74.base.impl.GovAffairsPager;
import com.example.liang.zhbj74.base.impl.HomePager;
import com.example.liang.zhbj74.base.impl.NewsCenterPager;
import com.example.liang.zhbj74.base.impl.SettingPager;
import com.example.liang.zhbj74.base.impl.SmartServicePager;
import com.example.liang.zhbj74.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

public class ContentFragment extends BaseFragment {
    private NoScrollViewPager vp_content;
    private ArrayList<BasePager>mPagers;//5个标签页集合
    private RadioGroup rg_group;

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
         vp_content = (NoScrollViewPager)view.findViewById(R.id.vp_content);
        rg_group = (RadioGroup)view.findViewById(R.id.rg_group);
        return view;
    }

    @Override
    public void initData() {
    mPagers=new ArrayList<BasePager>();
    //添加五个标签页
        mPagers.add(new HomePager(mActivity));
        mPagers.add(new NewsCenterPager(mActivity));
        mPagers.add(new SmartServicePager(mActivity));
        mPagers.add(new GovAffairsPager(mActivity));
        mPagers.add(new SettingPager(mActivity));
        vp_content.setAdapter(new ContentAdapter());
        //底栏标签切换监听
        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rb_home:
                        vp_content.setCurrentItem(0);
                        //首页被选中
                        break;
                    case R.id.rb_news:
//                        vp_content.setCurrentItem(1);
                        //是否具有平滑滑动效果
                        vp_content.setCurrentItem(1,false);
                        break;
                    case R.id.rb_smart:
                        vp_content.setCurrentItem(2);
                        break;
                    case R.id.rb_gov:
                        vp_content.setCurrentItem(3);
                        break;
                    case R.id.rb_setting:
                        vp_content.setCurrentItem(4);
                        break;
                }
            }
        });

        vp_content.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                BasePager pager = mPagers.get(position);
                pager.initData();
                if (position==0||position==mPagers.size()-1){
                    //首页和设置页禁用侧边栏
                    setSlidingMenuEnable(false);
                }else {
                    //其他页面开启侧边栏
                    setSlidingMenuEnable(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //手动加载第一页数据
        mPagers.get(0).initData();
        setSlidingMenuEnable(false);
    }

    //开启或禁用侧边栏
    private void setSlidingMenuEnable(boolean enable) {
        //获取侧边栏对象
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        if (enable){
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }

    class ContentAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mPagers.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        //初始化布局
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            BasePager pager = mPagers.get(position);
            View view = pager.mRootView;//获取当前页面对象的布局
//            pager.initData();//初始化数据,会默认加载下一个页面，为了优化性能
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View)object);
        }
    }

    //获取新闻中心的页面
    public NewsCenterPager getNewsCenterPager(){
        NewsCenterPager pager =(NewsCenterPager) mPagers.get(1);
        return pager;
    }
}
