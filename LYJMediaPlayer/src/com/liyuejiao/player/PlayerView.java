package com.liyuejiao.player;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.liyuejiao.player.local.LocalVideo;
import com.liyuejiao.player.widget.LyjOrientationDetector;

public class PlayerView extends RelativeLayout {

    private VideoView mVideoView;
    private MediaControllerSmall mMediaControllerSmall;
    private MediaControllerLarge mMediaControllerLarge;
    // 窗口、全屏模式
    private PlayMode mPlayMode;
    private LyjOrientationDetector mLyjOrientationDetector;

    private Activity mActivity;
    private Window mWindow;
    private android.view.ViewGroup.LayoutParams mLayoutParamWindowMode;
    private LayoutParams mLayoutParamFullScreenMode;

    private OnPlayCallbackListener mOnPlayCallbackListener;
    private LocalVideo mLocalVideo;

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

    public void onResume() {
        mLyjOrientationDetector.enable();
    }

    public void onPause() {
        mLyjOrientationDetector.disable();
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

        mWindow = mActivity.getWindow();
        mMediaControllerLarge.setWindow(mWindow);

        rootView.removeAllViews();
        removeAllViews();
        addView(mVideoView);
        // 设置黑色背景，有的视频尺寸大小不能平铺满窗口
        setBackgroundColor(Color.BLACK);

        if (mPlayMode == PlayMode.PLAYMODE_WINDOW) {
            addView(mMediaControllerSmall);
            mMediaControllerSmall.hide();
        } else if (mPlayMode == PlayMode.PLAYMODE_FULLSCREEN) {
            addView(mMediaControllerLarge);
            mMediaControllerLarge.hide();
        }
        // 设置VideoView回调函数
        mVideoView.setOnPreparedListener(mOnPreparedListener);

        mLyjOrientationDetector = new LyjOrientationDetector(context,
                LyjOrientationDetector.SCREEN_ORIENTATION_PORTRAIT_NORMAL);
        mLyjOrientationDetector.setOnRequestPlayMode(mOnReuqestPlayModeListener);

        // 半屏LayoutParam 界面未出现getLayoutParams = null
        // mLayoutParamWindowMode = getLayoutParams();
        mLayoutParamWindowMode = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParamWindowMode.height = (int) context.getResources().getDimension(R.dimen.player_height);

        // 全屏LayoutParam
        mLayoutParamFullScreenMode = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mPlayMode == PlayMode.PLAYMODE_WINDOW) {
            return mMediaControllerSmall.dispatchTouchEvent(ev);
        } else if (mPlayMode == PlayMode.PLAYMODE_FULLSCREEN) {
            return mMediaControllerLarge.dispatchTouchEvent(ev);
        } else {
            return true;
        }
    }

    /************************************ VideoView的回调函数 ****************************************************/
    // OnPreparedListener:onPrepared方法  MediaPlayer-->回调给VideoView-->回调给PlayerView
    private OnPreparedListener mOnPreparedListener = new OnPreparedListener() {

        @Override
        public void onPrepared(MediaPlayer mp) {
            mMediaPlayerController.start();
            mMediaControllerLarge.updateVideoTitle(mLocalVideo.name);
        }
    };

    /************************************ MediaController的回调函数 ****************************************************/
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
        public long getDuration() {
            return mVideoView.getDuration();
        }

        @Override
        public long getCurrentPosition() {
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
            // 全屏按钮：窗口->全屏(和大屏onBackPressed反操作)
            requestPlayMode(requestPlayMode);
            mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        @Override
        public void onBackPressed(PlayMode playMode) {
            if (playMode == PlayMode.PLAYMODE_FULLSCREEN) {
                // 返回:全屏->窗口(和小屏onRequestPlayMode反操作)
                requestPlayMode(PlayMode.PLAYMODE_WINDOW);
                mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                // 返回:窗口->退出播放
                if (mOnPlayCallbackListener != null) {
                    mOnPlayCallbackListener.onBackPressed();
                }
            }
        }

    };

    /************************************ 旋转方向的回调函数 ****************************************************/
    private LyjOrientationDetector.OnReuqestPlayModeListener mOnReuqestPlayModeListener = new LyjOrientationDetector.OnReuqestPlayModeListener() {

        @Override
        public void onRequestPlayMode(PlayMode requestPlayMode) {
            // 方向感应器旋转屏幕
            requestPlayMode(requestPlayMode);
        }
    };

    /**
     * 根据当前播放器模式PlayMode切换不同的MediaController，并旋转屏幕
     * 
     * @param requestPlayMode
     */
    private void requestPlayMode(PlayMode requestPlayMode) {
        // 请求和当前播放模式相同
        if (mPlayMode == requestPlayMode) {
            return;
        }
        // 请求窗口播放
        if (requestPlayMode == PlayMode.PLAYMODE_WINDOW) {
            removeView(mMediaControllerLarge);
            addView(mMediaControllerSmall);
            mMediaControllerSmall.hide();
            setLayoutParams(mLayoutParamWindowMode);
            // mWindow.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
            // | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        // 请求全屏播放
        else if (requestPlayMode == PlayMode.PLAYMODE_FULLSCREEN) {
            removeView(mMediaControllerSmall);
            addView(mMediaControllerLarge);
            mMediaControllerLarge.hide();
            // 重新设置VideoView大小
            setLayoutParams(mLayoutParamFullScreenMode);
            // mWindow.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
            // | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        mPlayMode = requestPlayMode;
    }

    /************************************ PUBLIC ****************************************************/
    public void setVideoPath(String path) {
        mVideoView.setVideoPath(path);
    }

    public void setOnPlayCallbackListener(OnPlayCallbackListener l) {
        mOnPlayCallbackListener = l;
    }

    public void setVideoItem(LocalVideo localVideo) {
        mLocalVideo = localVideo;
    }

}
