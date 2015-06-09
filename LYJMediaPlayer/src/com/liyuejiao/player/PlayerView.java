package com.liyuejiao.player;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.MediaPlayer.OnPreparedListener;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.liyuejiao.player.local.LocalVideo;
import com.liyuejiao.player.widget.LyjOrientationDetector;

public class PlayerView extends RelativeLayout {

    private VideoView mVideoView;
    private MediaControllerSmall mMediaControllerSmall;
    private MediaControllerLarge mMediaControllerLarge;
    private MediaControllerMini mMediaControllerMini;
    // 窗口、全屏模式
    private PlayMode mPlayMode;
    private LyjOrientationDetector mLyjOrientationDetector;

    private Activity mActivity;
    private Window mWindow;
    private android.view.ViewGroup.LayoutParams mLayoutParamWindowMode;
    private LayoutParams mLayoutParamFullScreenMode;

    private OnPlayCallbackListener mOnPlayCallbackListener;
    private LocalVideo mLocalVideo;
    private WindowManager.LayoutParams mWindowParams;
    private WindowManager mWindowManager;

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

    public void onStop() {
        mVideoView.stopPlayback();
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // 获取自定义属性
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PlayerView);
        int playMode = ta.getInt(R.styleable.PlayerView_playMode, 0);
        ta.recycle();

        if (playMode == 0) {
            mPlayMode = PlayMode.PLAYMODE_WINDOW;
        } else if (playMode == 1) {
            mPlayMode = PlayMode.PLAYMODE_FULLSCREEN;
        } else if (playMode == 2) {
            mPlayMode = PlayMode.PLAYMODE_MINI;
        }

        ViewGroup rootView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.player_view, null);
        mVideoView = (VideoView) rootView.findViewById(R.id.videoView);
        mMediaControllerLarge = (MediaControllerLarge) rootView.findViewById(R.id.mediaControllerLarge);
        mMediaControllerSmall = (MediaControllerSmall) rootView.findViewById(R.id.mediaControllerSmall);
        mMediaControllerMini = (MediaControllerMini) rootView.findViewById(R.id.mediaControllerMini);
        // 设置播放器的控制界面
        mMediaControllerSmall.setMediaPlayer(mMediaPlayerController);
        mMediaControllerLarge.setMediaPlayer(mMediaPlayerController);
        mMediaControllerMini.setMediaPlayer(mMediaPlayerController);

        if (context instanceof Activity) {
            mActivity = (Activity) context;
            mWindow = mActivity.getWindow();
        }
        // 需要Window控制亮度
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
        } else if (mPlayMode == PlayMode.PLAYMODE_MINI) {
            addView(mMediaControllerMini);
            mMediaControllerMini.hide();
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

    private int lastX = -1;
    private int lastY = -1;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("lyj", "PlayerView dispatchTouchEvent");
        if (mPlayMode == PlayMode.PLAYMODE_WINDOW) {
            return mMediaControllerSmall.dispatchTouchEvent(ev);
        } else if (mPlayMode == PlayMode.PLAYMODE_FULLSCREEN) {
            return mMediaControllerLarge.dispatchTouchEvent(ev);
        } else if (mPlayMode == PlayMode.PLAYMODE_MINI) {
            if (lastX == -1) {
                lastX = (int) ev.getX();
            }
            if (lastY == -1) {
                lastY = (int) ev.getY();
            }
            switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = (int) ev.getX() - this.lastX;
                int deltaY = (int) ev.getY() - this.lastY;
                if (deltaX > 5 || deltaY > 5) {
                    mWindowParams.x += deltaX;
                    mWindowParams.y += deltaY;
                    mWindowManager.updateViewLayout((View) getParent(), mWindowParams);

                    lastX = (int) ev.getX();
                    lastY = (int) ev.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            }
            return mMediaControllerMini.dispatchTouchEvent(ev);
        } else {
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("lyj", "PlayerView onTouchEvent");
        return super.onTouchEvent(event);
    }

    /************************************ VideoView的回调函数 ****************************************************/
    // OnPreparedListener:onPrepared方法 MediaPlayer-->回调给VideoView-->回调给PlayerView
    private OnPreparedListener mOnPreparedListener = new OnPreparedListener() {

        @Override
        public void onPrepared(MediaPlayer mp) {
            mMediaPlayerController.start();
            mMediaControllerLarge.updateVideoTitle(mLocalVideo.name);
            mMediaControllerMini.updateVideoTitle(mLocalVideo.name);
        }
    };

    /************************************ MediaController的回调函数 ****************************************************/
    private MediaPlayerControl mMediaPlayerController = new MediaPlayerControl() {

        @Override
        public void start() {
            mVideoView.start();
            mMediaControllerSmall.updateVideoButtonState(true);
            mMediaControllerLarge.updateVideoButtonState(true);
            mMediaControllerMini.updateVideoButtonState(true);
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
                mMediaControllerMini.updateVideoButtonState(false);
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

        @Override
        public void onFloatWindowShow() {
            if (mOnPlayCallbackListener != null) {
                mOnPlayCallbackListener.onFloatWindowShow();
            }
        }

        @Override
        public void onFloatWindowClose() {
            if (mOnPlayCallbackListener != null) {
                mOnPlayCallbackListener.onFloatWindowClose();
            }
        }

        @Override
        public void onFloatWindowFullScreen() {
            if (mOnPlayCallbackListener != null) {
                mOnPlayCallbackListener.onFloatWindowFullScreen();
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

    // 代码设置自定义属性
    public void setPlayMode(PlayMode playMode) {
        mPlayMode = playMode;
    }

    public void setWindowParams(android.view.WindowManager.LayoutParams windowParams) {
        mWindowParams = windowParams;
    }
}
