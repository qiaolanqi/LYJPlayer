package com.liyuejiao.player;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

public class MainActivity extends Activity {

    private PlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUtils.loadScreenInfo(this);
        getActionBar().hide();
        setContentView(R.layout.activity_main);
        mPlayerView = (PlayerView) findViewById(R.id.playerView);

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
    protected void onResume() {
        super.onResume();
        mPlayerView.onResume();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        mPlayerView.onPause();
    }
}
