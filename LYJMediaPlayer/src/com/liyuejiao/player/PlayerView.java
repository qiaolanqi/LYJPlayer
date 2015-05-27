package com.liyuejiao.player;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class PlayerView extends RelativeLayout {

    private Activity mActivity;
    private VideoView mVideoView;
    private MediaControllerSmall mMediaControllerSmall;
    private MediaControllerLarge mMediaControllerLarge;
    // 窗口、全屏模式
    private PlayMode mPlayMode;

    public PlayerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, -1);
    }

    public PlayerView(Context context) {
        super(context);
        init(context, null, -1);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        mActivity = (Activity) context;
        // 获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PlayerView);
        int playMode = ta.getInt(R.styleable.PlayerView_playMode, 0);
        ta.recycle();
        if (playMode == 0) {
            mPlayMode = PlayMode.PLAYMODE_WINDOW;
        } else {
            mPlayMode = PlayMode.PLAYMODE_FULLSCREEN;
        }

        ViewGroup rootView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.player_view, null);
        mVideoView = (VideoView) rootView.findViewById(R.id.videoView);
        mMediaControllerSmall = (MediaControllerSmall) rootView.findViewById(R.id.mediaControllerSmall);
        mMediaControllerLarge = (MediaControllerLarge) rootView.findViewById(R.id.mediaControllerLarge);
        // 设置播放器的控制界面
        mMediaControllerSmall.setMediaPlayer(mMediaPlayerController);
        mMediaControllerLarge.setMediaPlayer(mMediaPlayerController);

        rootView.removeAllViews();
        removeAllViews();
        addView(mVideoView);
        if (mPlayMode == PlayMode.PLAYMODE_WINDOW) {
            addView(mMediaControllerSmall);
            mMediaControllerSmall.hide();
        } else {
            addView(mMediaControllerLarge);
            mMediaControllerLarge.hide();
        }

        // 设置播放路径
        mVideoView.setVideoURI(Uri.parse("android.resource://"
                + context.getPackageName() + "/" + R.raw.videoviewdemo));
        // 开始播放
        mVideoView.start();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mPlayMode == PlayMode.PLAYMODE_WINDOW) {
            return mMediaControllerSmall.dispatchTouchEvent(ev);
        } else {
            return mMediaControllerLarge.dispatchTouchEvent(ev);
        }
    }

    private MediaPlayerControl mMediaPlayerController = new MediaPlayerControl() {

        @Override
        public void start() {
            mVideoView.start();
            mMediaControllerSmall.updateVideoButtonState(true);
            mMediaControllerLarge.updateVideoButtonState(true);
        }

        @Override
        public void seekTo(int pos) {
            mVideoView.seekTo(pos);
        }

        @Override
        public void pause() {
            if (canPause()) {
                mVideoView.pause();
                mMediaControllerSmall.updateVideoButtonState(false);
                mMediaControllerLarge.updateVideoButtonState(false);
            }
        }

        @Override
        public boolean isPlaying() {
            return mVideoView.isPlaying();
        }

        @Override
        public int getDuration() {
            return mVideoView.getDuration();
        }

        @Override
        public int getCurrentPosition() {
            return mVideoView.getCurrentPosition();
        }

        @Override
        public int getBufferPercentage() {
            return mVideoView.getBufferPercentage();
        }

        @Override
        public boolean canSeekForward() {
            return true;
        }

        @Override
        public boolean canSeekBackward() {
            return true;
        }

        @Override
        public boolean canPause() {
            return true;
        }

        @Override
        public void onRequestPlayMode(PlayMode requestPlayMode) {
            if (mPlayMode == requestPlayMode) {
                return;
            }
            mPlayMode = requestPlayMode;
            // 请求窗口播放
            if (requestPlayMode == PlayMode.PLAYMODE_WINDOW) {
                removeView(mMediaControllerLarge);
                addView(mMediaControllerSmall);
                mMediaControllerSmall.hide();
            }
            // 请求全屏播放
            else {
                removeView(mMediaControllerSmall);
                addView(mMediaControllerLarge);
                mMediaControllerLarge.hide();
                //强制旋转屏幕->横屏
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
    };
}
