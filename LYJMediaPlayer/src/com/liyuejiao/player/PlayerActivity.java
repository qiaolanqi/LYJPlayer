package com.liyuejiao.player;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.liyuejiao.player.local.LocalVideo;
import com.liyuejiao.player.util.Constant;

public class PlayerActivity extends FragmentActivity {
    private PlayerView mPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("lyj", "PlayerActivity onCreate");
        getActionBar().hide();
        setContentView(R.layout.player);

        mPlayerView = (PlayerView) findViewById(R.id.playerView);
        mPlayerView.setOnPlayCallbackListener(mOnPlayCallbackListener);

        LocalVideo localVideo = getIntentData();
        mPlayerView.setVideoItem(localVideo);
        mPlayerView.setVideoPath(localVideo.path);
    }

    private LocalVideo getIntentData() {
        String path = getIntent().getExtras().getString(Constant.KEY_PATH);
        String title = getIntent().getExtras().getString(Constant.KEY_TITLE);
        return new LocalVideo(title, path);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lyj", "PlayerActivity onResume");
        mPlayerView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lyj", "PlayerActivity onPause");
        mPlayerView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lyj", "PlayerActivity onDestroy");
        mPlayerView.onStop();
    }

    /**************************************************************************/
    private OnPlayCallbackListener mOnPlayCallbackListener = new OnPlayCallbackListener() {

        @Override
        public void onBackPressed() {
            onBackPressed();
        }

        @Override
        public void onFloatWindowShow() {
            if (mPlayerView != null) {
                mPlayerView.onStop();
            }
            WindowManager mWindowManager = ((WindowManager) LyjApplication.context
                    .getSystemService(Context.WINDOW_SERVICE));
            WindowManager.LayoutParams mVideoParams = new WindowManager.LayoutParams();
            mVideoParams.gravity = Gravity.CENTER_HORIZONTAL;
            mVideoParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            mVideoParams.format = PixelFormat.RGBA_8888;
            mVideoParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            mVideoParams.width = getResources().getDimensionPixelSize(R.dimen.controller_mini_width);
            mVideoParams.height = getResources().getDimensionPixelSize(R.dimen.controller_mini_height);

            View v = LayoutInflater.from(LyjApplication.context).inflate(R.layout.player, null);
            PlayerView playerView = (PlayerView) v.findViewById(R.id.playerView);
            playerView.setPlayMode(PlayMode.PLAYMODE_MINI);
            LocalVideo localVideo = getIntentData();
            playerView.setVideoItem(localVideo);
            playerView.setVideoPath(localVideo.path);
            mWindowManager.addView(v, mVideoParams);

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            LyjApplication.context.startActivity(intent);
        }
    };
}
