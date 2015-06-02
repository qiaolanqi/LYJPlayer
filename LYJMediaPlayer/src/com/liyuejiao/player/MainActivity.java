package com.liyuejiao.player;

import io.vov.vitamio.LibsChecker;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;

import com.liyuejiao.player.local.LocalVideoFragment;
import com.liyuejiao.player.online.OnlineVideoFragment;
import com.liyuejiao.player.util.SystemUtils;

public class MainActivity extends FragmentActivity implements OnClickListener {

    private ViewPager mViewPager;
    private RadioButton mTabLocal;
    private RadioButton mTabOnline;
    private MainTabAdapter mAdapter;
    private LocalVideoFragment mLocalVideoFragment;
    private OnlineVideoFragment mOnlineVideoFragment; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!LibsChecker.checkVitamioLibs(this))
            return;
        SystemUtils.loadScreenInfo(this);
        getActionBar().hide();
        setContentView(R.layout.activity_main);

        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLocal = (RadioButton) findViewById(R.id.radio_file);
        mTabOnline = (RadioButton) findViewById(R.id.radio_online);

        mTabLocal.setOnClickListener(this);
        mTabOnline.setOnClickListener(this);
        // mViewPager.setOnPageChangeListener(mOnPageChangeListener);

        mAdapter = new MainTabAdapter(this, mViewPager);
        mLocalVideoFragment = new LocalVideoFragment();
        mOnlineVideoFragment = new OnlineVideoFragment();
        mAdapter.addTab(mTabLocal, mLocalVideoFragment);
        mAdapter.addTab(mTabOnline, mOnlineVideoFragment);
    }

    /**
     * 点击“全屏”由小屏->大屏，Activity重新加载，PlayerView重新创建<br>
     * 1.Manifest:android:configChanges="orientation|keyboardHidden|screenSize"<br>
     * 2.重写onConfigurationChanged
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

}
