package com.liyuejiao.player;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class PlayerView extends RelativeLayout {

    private Activity mActivity;
    private VideoView mVideoView;
    private MediaControllerSmall mMediaControllerSmall;

    public PlayerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayerView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        // Activity才有setRequestedOrientation()方法，需要类型强转
        mActivity = (Activity) context;

        View view = LayoutInflater.from(context).inflate(R.layout.player_view, this);
        mVideoView = (VideoView) view.findViewById(R.id.videoView);
        mMediaControllerSmall = (MediaControllerSmall) view.findViewById(R.id.mediaControllerSmall);
        // 设置播放器的控制界面
        mMediaControllerSmall.setMediaPlayer(mMediaPlayerController);
        mMediaControllerSmall.hide();
        // 设置播放路径
        mVideoView.setVideoURI(Uri.parse("android.resource://"
                + context.getPackageName() + "/" + R.raw.videoviewdemo));
        // 开始播放
        mVideoView.start();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mMediaControllerSmall.dispatchTouchEvent(ev);
    }

    private MediaPlayerControl mMediaPlayerController = new MediaPlayerControl() {

        @Override
        public void start() {
            mVideoView.start();
            mMediaControllerSmall.updateVideoButtonState(true);
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
            // 请求窗口播放
            if (requestPlayMode == PlayMode.PLAYMODE_WINDOW) {

            }
            // 请求全屏播放
            else {
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
    };
}
