package com.liyuejiao.player;

import com.liyuejiao.player.local.LocalVideo;
import com.liyuejiao.player.util.Constant;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class PlayerActivity extends FragmentActivity {
    private PlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().hide();
        setContentView(R.layout.player);

        String path = getIntent().getExtras().getString(Constant.KEY_PATH);
        String title = getIntent().getExtras().getString(Constant.KEY_TITLE);
        mPlayerView = (PlayerView) findViewById(R.id.playerView);
        mPlayerView.setOnPlayCallbackListener(mOnPlayCallbackListener);

        LocalVideo localVideo = new LocalVideo(title, path);
        mPlayerView.setVideoItem(localVideo);
        mPlayerView.setVideoPath(path);
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

    /**************************************************************************/
    private OnPlayCallbackListener mOnPlayCallbackListener = new OnPlayCallbackListener() {

        @Override
        public void onBackPressed() {
            onBackPressed();
        }
    };
}
