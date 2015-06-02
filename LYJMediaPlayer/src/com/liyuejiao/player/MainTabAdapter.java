package com.liyuejiao.player;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;

public class MainTabAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener,
        OnCheckedChangeListener {
    private ViewPager mViewPager;
    private final ArrayList<Fragment> mFragmentList;
    private final ArrayList<RadioButton> mTabs;

    public MainTabAdapter(MainActivity mainActivity, ViewPager pager) {
        super(mainActivity.getSupportFragmentManager());
        mViewPager = pager;
        mViewPager.setAdapter(this);
        mViewPager.setOnPageChangeListener(this);
        //页面缓存数
        mViewPager.setOffscreenPageLimit(2);
        mTabs = new ArrayList<RadioButton>();
        mFragmentList = new ArrayList<Fragment>();
    }

    public void addTab(RadioButton tab, Fragment fragment) {
        tab.setOnCheckedChangeListener(this);
        mFragmentList.add(fragment);
        mTabs.add(tab);
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(View container, int position, Object objec) {
        ((ViewPager) container).removeView((View) objec);
        mFragmentList.remove(position);
        mTabs.remove(position);
    }

    public int getIndexOf(Fragment fragment) {
        return mFragmentList.indexOf(fragment);
    }

    @Override
    public int getCount() {
        return mFragmentList == null ? 0 : mFragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mTabs.get(position).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.isChecked()) {
            int index = mTabs.indexOf(buttonView);
            if (index != -1) {
                mViewPager.setCurrentItem(index);
            }
        }
    }
}
