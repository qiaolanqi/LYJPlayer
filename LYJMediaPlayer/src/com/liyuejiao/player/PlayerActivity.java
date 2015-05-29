package com.liyuejiao.player;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class PlayerActivity extends FragmentActivity {
    private PlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.player);
        mPlayerView = (PlayerView) findViewById(R.id.playerView);
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