package com.liyuejiao.player;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

public abstract class MediaControllerBase extends FrameLayout {

    protected MediaPlayerControl mPlayer;
    private Context mContext;
    // VideoView中调用setAnchorView()设置进来的View，MediaController显示的时候会感觉该AnchorView的位置进行显示
    private View mAnchor;
    // MediaController最外层的根布局
    private View mRoot;
    // 通过Window的方式来显示MediaController，MediaController是一个填充屏幕的布局，但是背景是透明的
    private WindowManager mWindowManager;
    private Window mWindow;
    private View mDecor;
    // 理解为当前整个MediaController的布局
    private WindowManager.LayoutParams mDecorLayoutParams;
    private ProgressBar mProgress;
    private TextView mEndTime, mCurrentTime;
    // 最好不用这个BOOL表示是否显示MediaController，因为默认3秒隐藏
    private boolean mShowing = true;
    private boolean mDragging;
    // 默认自动消失的时间
    private static final int sDefaultTimeout = 3000;
    protected static final int TIMER_TICKER_INTERVAL = 1000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private static final int MSG_TIMER_TICKER = 3;
    private boolean mUseFastForward;
    private boolean mFromXml;
    private boolean mListenersSet;
    private View.OnClickListener mNextListener, mPrevListener;
    private ImageButton mPauseButton;
    private ImageButton mFfwdButton;
    private ImageButton mRewButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    protected LayoutInflater mLayoutInflater;
    private GestureDetector mGestureDetector;
    protected VoiceLightWidget mVoiceLightWidget;

    public MediaControllerBase(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MediaControllerBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MediaControllerBase(Context context) {
        super(context);
        init();
    }

    private void init() {
        mLayoutInflater = LayoutInflater.from(getContext());
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                toggle();
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                float mOldX = e1.getX(), mOldY = e1.getY();
                int y = (int) e2.getRawY();
                int windowWidth = SystemUtils.getScreenWidth();
                int windowHeight = SystemUtils.getScreenHeight();
                if (mVoiceLightWidget != null) {
                    if (mOldX > windowWidth * 4.0 / 5)// 右边滑动
                        mVoiceLightWidget.onVolumeSlide((mOldY - y) / windowHeight);
                    else if (mOldX < windowWidth / 5.0)// 左边滑动
                        mVoiceLightWidget.onBrightnessSlide((mOldY - y) / windowHeight, mWindow);
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
    }

    public void setMediaPlayer(MediaPlayerControl player) {
        mPlayer = player;
        updatePausePlay();
    }

    public void setWindow(Window window) {
        mWindow = window;
    }

    /**
     * Show the controller on screen. It will go away automatically after 3 seconds of inactivity.
     */
    public void show() {
        show(sDefaultTimeout);
    }

    /**
     * Disable pause or seek buttons if the stream cannot be paused or seeked. This requires the control interface to be
     * a MediaPlayerControlExt
     */
    private void disableUnsupportedButtons() {
        try {
            if (mPauseButton != null && !mPlayer.canPause()) {
                mPauseButton.setEnabled(false);
            }
            if (mRewButton != null && !mPlayer.canSeekBackward()) {
                mRewButton.setEnabled(false);
            }
            if (mFfwdButton != null && !mPlayer.canSeekForward()) {
                mFfwdButton.setEnabled(false);
            }
        } catch (IncompatibleClassChangeError ex) {
            // We were given an old version of the interface, that doesn't have
            // the canPause/canSeekXYZ methods. This is OK, it just means we
            // assume the media can be paused and seeked, and so we don't disable
            // the buttons.
        }
    }

    /**
     * Show the controller on screen. It will go away automatically after 'timeout' milliseconds of inactivity.
     * 
     * @param timeout
     *            The timeout in milliseconds. Use 0 to show the controller until hide() is called.
     */
    public void show(int timeout) {
        // 暂停时timeout=0,防止MessageQueue队列中还有msg=FADE_OUT,3s后消失
        mHandler.removeMessages(FADE_OUT);
        mHandler.sendEmptyMessage(SHOW_PROGRESS);
        if (timeout != 0) {
            Message msg = mHandler.obtainMessage(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
    }

    public boolean isShowing() {
        return mShowing;
    }

    /**
     * Remove the controller from the screen.
     */
    public void hide() {
        if (mShowing) {
            mHandler.removeMessages(SHOW_PROGRESS);
            Message msg = mHandler.obtainMessage(FADE_OUT);
            mHandler.sendMessage(msg);
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case FADE_OUT:
                mShowing = false;
                stopTimerTicker();
                onHide();
                break;
            case SHOW_PROGRESS:
                mShowing = true;
                startTimerTicker();
                onShow();
                break;
            case MSG_TIMER_TICKER:
                onTimerTicker();
                sendEmptyMessageDelayed(MSG_TIMER_TICKER, TIMER_TICKER_INTERVAL);
            }
        }
    };

    protected abstract void onShow();

    protected abstract void onHide();

    protected abstract void onTimerTicker();

    /*************** 开启定时更新ProgressBar *********************/
    protected void stopTimerTicker() {
        mHandler.removeMessages(MSG_TIMER_TICKER);
    }

    protected void startTimerTicker() {
        mHandler.removeMessages(MSG_TIMER_TICKER);
        mHandler.sendEmptyMessage(MSG_TIMER_TICKER);
    }

    public void toggle() {
        if (isShowing()) {
            hide();
        } else {
            // 如果视频暂停状态下进行show操作,统一将show更改为永久显示
            if (!mPlayer.isPlaying()) {
                show(0);
            } else {
                show();
            }
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:

            break;
        case MotionEvent.ACTION_UP:
            if (mVoiceLightWidget != null) {
                mVoiceLightWidget.onGestureFinish();
            }
            break;
        default:
            break;
        }

        if (mGestureDetector != null) {
            mGestureDetector.onTouchEvent(event);
        }
        return true;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(sDefaultTimeout);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        final boolean uniqueDown = event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN;
        if (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                || keyCode == KeyEvent.KEYCODE_SPACE) {
            if (uniqueDown) {
                doPauseResume();
                show(sDefaultTimeout);
                if (mPauseButton != null) {
                    mPauseButton.requestFocus();
                }
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
            if (uniqueDown && !mPlayer.isPlaying()) {
                mPlayer.start();
                updatePausePlay();
                show(sDefaultTimeout);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
            if (uniqueDown && mPlayer.isPlaying()) {
                mPlayer.pause();
                updatePausePlay();
                show(sDefaultTimeout);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                || keyCode == KeyEvent.KEYCODE_VOLUME_UP
                || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE
                || keyCode == KeyEvent.KEYCODE_CAMERA) {
            // don't show the controls for volume adjustment
            return super.dispatchKeyEvent(event);
        } else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
            if (uniqueDown) {
                hide();
            }
            return true;
        }

        show(sDefaultTimeout);
        return super.dispatchKeyEvent(event);
    }

    private View.OnClickListener mPauseListener = new View.OnClickListener() {
        public void onClick(View v) {
            doPauseResume();
            show(sDefaultTimeout);
        }
    };

    private void updatePausePlay() {
        if (mRoot == null || mPauseButton == null)
            return;

        if (mPlayer.isPlaying()) {
            mPauseButton.setBackgroundResource(R.drawable.ic_media_pause);
        } else {
            mPauseButton.setBackgroundResource(R.drawable.ic_media_play);
        }
    }

    private void doPauseResume() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
        updatePausePlay();
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (mPauseButton != null) {
            mPauseButton.setEnabled(enabled);
        }
        if (mFfwdButton != null) {
            mFfwdButton.setEnabled(enabled);
        }
        if (mRewButton != null) {
            mRewButton.setEnabled(enabled);
        }
        if (mNextButton != null) {
            mNextButton.setEnabled(enabled && mNextListener != null);
        }
        if (mPrevButton != null) {
            mPrevButton.setEnabled(enabled && mPrevListener != null);
        }
        if (mProgress != null) {
            mProgress.setEnabled(enabled);
        }
        disableUnsupportedButtons();
        super.setEnabled(enabled);
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        super.onInitializeAccessibilityEvent(event);
        event.setClassName(MediaControllerBase.class.getName());
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfo(info);
        info.setClassName(MediaControllerBase.class.getName());
    }

    private void installPrevNextListeners() {
        if (mNextButton != null) {
            mNextButton.setOnClickListener(mNextListener);
            mNextButton.setEnabled(mNextListener != null);
        }

        if (mPrevButton != null) {
            mPrevButton.setOnClickListener(mPrevListener);
            mPrevButton.setEnabled(mPrevListener != null);
        }
    }

    public void setPrevNextListeners(View.OnClickListener next, View.OnClickListener prev) {
        mNextListener = next;
        mPrevListener = prev;
        mListenersSet = true;

        if (mRoot != null) {
            installPrevNextListeners();

            if (mNextButton != null && !mFromXml) {
                mNextButton.setVisibility(View.VISIBLE);
            }
            if (mPrevButton != null && !mFromXml) {
                mPrevButton.setVisibility(View.VISIBLE);
            }
        }
    }
}
