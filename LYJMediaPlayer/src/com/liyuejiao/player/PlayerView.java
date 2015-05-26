package com.liyuejiao.player;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class PlayerView extends RelativeLayout {

    private MediaControllerSmall mMediaControllerSmall;
    private VideoView mVideoView;

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
        }

        @Override
        public void seekTo(int pos) {
            mVideoView.seekTo(pos);
        }

        @Override
        public void pause() {
            mVideoView.pause();
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
    };
}
