package com.liyuejiao.player;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.liyuejiao.player.local.LocalVideo;

public class FloatWindow extends RelativeLayout {
    private WindowManager mWindowManager;
    private View mView;
    private PlayerView mPlayerView;
    private LocalVideo mLocalVideo;
    private WindowManager.LayoutParams mWindowParams;
    private int lastX;
    private int lastY;
    private boolean mShowing = false;
    private Activity mActivity;

    public FloatWindow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public FloatWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public FloatWindow(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        mWindowParams.format = PixelFormat.RGBA_8888;
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
        mWindowParams.width = getResources().getDimensionPixelSize(R.dimen.controller_mini_width);
        mWindowParams.height = getResources().getDimensionPixelSize(R.dimen.controller_mini_height);

        launchHome();
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
        ComponentName cn = new ComponentName(mActivity, MainActivity.class);
        intent.setComponent(cn);
        LyjApplication.context.startActivity(intent);
    }

    public void create() {
        mView = LayoutInflater.from(LyjApplication.context).inflate(R.layout.player_mini, null);
        mPlayerView = (PlayerView) mView.findViewById(R.id.playerView);
        mPlayerView.setVideoItem(mLocalVideo);
        mPlayerView.setVideoPath(mLocalVideo.path);
        mWindowManager.addView(mView, mWindowParams);
        mShowing = true;
    }

    public void destroy() {
        mPlayerView.onStop();
        mWindowManager.removeView(mView);
        mShowing = false;
    }

    public boolean isShowing() {
        return mShowing;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            this.lastX = (int) event.getRawX();
            this.lastY = (int) event.getRawY();
            break;
        case MotionEvent.ACTION_MOVE:
            int deltaX = (int) event.getRawX() - this.lastX;
            int deltaY = (int) event.getRawY() - this.lastY;
            mWindowParams.x += deltaX;
            mWindowParams.y += deltaY;
            if (mView != null) {
                mWindowManager.updateViewLayout(mView, mWindowParams);
            }
            break;
        case MotionEvent.ACTION_UP:
            break;
        }
        return mPlayerView.dispatchTouchEvent(event);
    }

    public void setVideo(LocalVideo video) {
        mLocalVideo = video;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

}
