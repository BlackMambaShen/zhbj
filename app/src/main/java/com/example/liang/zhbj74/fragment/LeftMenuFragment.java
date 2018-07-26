package com.example.liang.zhbj74.fragment;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.liang.zhbj74.MainActivity;
import com.example.liang.zhbj74.R;
import com.example.liang.zhbj74.base.BasePager;
import com.example.liang.zhbj74.base.impl.NewsCenterPager;
import com.example.liang.zhbj74.domain.NewsMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

public class LeftMenuFragment extends BaseFragment {
    private ListView lv_list;

    private ArrayList<NewsMenu.NewsMenuData> mNewsMenuData;

    private int mCurrentPos;//当前被选中的item的位置
    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
         lv_list = (ListView) view.findViewById(R.id.lv_list);
        return view;
    }

    @Override
    public void initData() {

    }

    //给侧边栏设置数据
    public void setMenuData(ArrayList<NewsMenu.NewsMenuData> data){
        mCurrentPos=0;//当前选中的位置归0
        //更新页面
        mNewsMenuData=data;

        LeftMenuAdapter adapter = new LeftMenuAdapter();
        lv_list.setAdapter(adapter);
        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos=position;//改变当前选中的位置
                adapter.notifyDataSetChanged();//更新当前被选中的位置
                //收起侧边栏
                toggle();
                //侧边栏点击之后，要修改新闻中心的framelayout的内容
                setCurrentDetailPager(position);

            }
        });
    }

    /*
    设置当前的菜单详情页
     */
    private void setCurrentDetailPager(int position) {
        //获取新闻中心的对象
        MainActivity mainUI = (MainActivity) this.mActivity;
        //获取contentFragment
        ContentFragment fragment = mainUI.getContentFragment();
        //获取newscenterPager
        NewsCenterPager newsCenterPager = fragment.getNewsCenterPager();
        //修改新闻中心的framelayout
            newsCenterPager.setCurrentDetailPager(position);
    }

    /*
    打开或者关闭侧边栏
     */
    private void toggle() {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();//如果状态是开，调用就关；反之亦然
    }

    class LeftMenuAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mNewsMenuData.size();
        }

        @Override
        public NewsMenu.NewsMenuData getItem(int position) {
            return mNewsMenuData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.list_item_menu, null);
            TextView tv_menu = (TextView)view.findViewById(R.id.tv_menu);
            NewsMenu.NewsMenuData item = getItem(position);
            tv_menu.setText(item.title);
            if (position==mCurrentPos){
                //被选中
                tv_menu.setEnabled(true);
            }else {
                //未选中
                tv_menu.setEnabled(false);
            }
            return view;
        }
    }
}
