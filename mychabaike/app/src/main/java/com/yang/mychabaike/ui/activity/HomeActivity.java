package com.yang.mychabaike.ui.activity;

import android.os.Bundle;
import android.os.Looper;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.yang.mychabaike.beans.TabInfo;
import com.yang.mychabaike.ui.activity.fragment.ContentFragment;

import yang.mychabaike.R;

/**
 * Created by my on 2016/6/20.
 */
public class HomeActivity extends FragmentActivity{
    private TabLayout mTabs;
    private ViewPager viewPager;
    private static final String TAG=HomeActivity.class.getSimpleName();
    private TabInfo[] tabs=new TabInfo[]{
            new TabInfo("社会热点",6),
            new TabInfo("企业要闻",1),
            new TabInfo("医疗新闻",2),
            new TabInfo("生活贴士",3),
            new TabInfo("药品新闻",4),
            new TabInfo("疾病快讯",7),
            new TabInfo("食品新闻",5)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        boolean b = Thread.currentThread() == Looper.getMainLooper().getThread();
        System.out.println("onCreate+++++++++"+b);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void initView() {
       mTabs= (TabLayout) findViewById(R.id.home_class);
        viewPager= (ViewPager) findViewById(R.id.home_vp);
        viewPager.setAdapter(new ContentAdapter(getSupportFragmentManager()));
        mTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabs.setupWithViewPager(viewPager);
    }
    public class ContentAdapter extends FragmentStatePagerAdapter{

        public ContentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Log.d(TAG, "getPageTitle() returned: position=" + position);
            ContentFragment cf = new ContentFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id",tabs[position].class_id);
            cf.setArguments(bundle);
            System.out.println("getItem=======================");
            return cf;
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position].name;
        }
    }

}
