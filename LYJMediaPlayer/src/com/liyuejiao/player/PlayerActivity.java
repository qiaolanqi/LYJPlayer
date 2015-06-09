package com.liyuejiao.player;

import android.content.ComponentName;
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
    protected FloatWindow mFloatWindow;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;
    private PlayerView mPlayerViewMini;
    private View mMiniView;

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
            PlayerActivity.this.finish();
        }

        @Override
        public void onFloatWindowShow() {
            if (mPlayerView != null) {
                mPlayerView.onStop();
            }
            // mFloatWindow = new FloatWindow(LyjApplication.context);
            // mFloatWindow.setVideo(getIntentData());
            // mFloatWindow.create();
            mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            mWindowParams = new WindowManager.LayoutParams();
            mWindowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            mWindowParams.format = PixelFormat.RGBA_8888;
            mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            mWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
            mWindowParams.width = getResources().getDimensionPixelSize(R.dimen.controller_mini_width);
            mWindowParams.height = getResources().getDimensionPixelSize(R.dimen.controller_mini_height);
            create();
            launchHome();
        }

        @Override
        public void onFloatWindowClose() {
            destroy();
            PlayerActivity.this.destroy();
        }

        @Override
        public void onFloatWindowFullScreen() {
            destroy();
            launchPlayer();
        }

    };

    public void create() {
        mMiniView = LayoutInflater.from(LyjApplication.context).inflate(R.layout.player_mini, null);
        mPlayerViewMini = (PlayerView) mMiniView.findViewById(R.id.playerView);
        mPlayerViewMini.setOnPlayCallbackListener(mOnPlayCallbackListener);
        LocalVideo localVideo = getIntentData();
        mPlayerViewMini.setVideoItem(localVideo);
        mPlayerViewMini.setVideoPath(localVideo.path);
        mPlayerViewMini.setWindowParams(mWindowParams);
        mWindowManager.addView(mMiniView, mWindowParams);
    }

    public void destroy() {
        mPlayerViewMini.onStop();
        mWindowManager.removeView(mMiniView);
    }

    private void launchHome() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        LyjApplication.context.startActivity(intent);
    }

    private void launchPlayer() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ComponentName cn = new ComponentName(PlayerActivity.this, PlayerActivity.class);
        intent.setComponent(cn);
        LyjApplication.context.startActivity(intent);
    }
    // @Override
    // public boolean dispatchTouchEvent(MotionEvent ev) {
    // Log.d("lyj", "PlayerActivity dispatchTouchEvent");
    // if (mFloatWindow != null && mFloatWindow.isShowing()) {
    // return mFloatWindow.dispatchTouchEvent(ev);
    // } else {
    // return mPlayerView.dispatchTouchEvent(ev);
    // }
    // }
    //
    // @Override
    // public boolean onTouchEvent(MotionEvent event) {
    // Log.d("lyj", "PlayerActivity onTouchEvent");
    // return super.onTouchEvent(event);
    // }
}
