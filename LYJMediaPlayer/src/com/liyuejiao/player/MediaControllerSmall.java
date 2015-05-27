package com.liyuejiao.player;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MediaControllerSmall extends MediaControllerBase implements View.OnClickListener {

    private ImageButton mPauseButton;
    private SeekBar mProgress;
    private ImageButton mFullScreenButton;

    // 如何控制调用那个构造方法？
    public MediaControllerSmall(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public MediaControllerSmall(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MediaControllerSmall(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        mLayoutInflater.inflate(R.layout.media_controller_small, this);
        mPauseButton = (ImageButton) findViewById(R.id.mediacontroller_pause);
        if (mPauseButton != null) {
            mPauseButton.setOnClickListener(this);
        }
        // SeekBar 是 ProgressBar的一种
        mProgress = (SeekBar) findViewById(R.id.mediacontroller_progress);
        if (mProgress != null) {
            mProgress.setProgress(0);
            mProgress.setMax(1000);
            mProgress.setOnSeekBarChangeListener(mSeekBarChangeListener);
        }
        mFullScreenButton = (ImageButton) findViewById(R.id.mediacontroller_fullscreen);
        if (mFullScreenButton != null) {
            mFullScreenButton.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.mediacontroller_pause:
            // 播放中
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                show(0);// 暂停市MediaController永久显示
            }
            // 未播放
            else {
                mPlayer.start();
                show();
            }
            break;
        case R.id.mediacontroller_fullscreen:
            //全屏
            mPlayer.onRequestPlayMode(PlayMode.PLAYMODE_FULLSCREEN);
            break;
        default:
            break;
        }

    }

    /**
     * 拖动SeekBar结束时，根据当前SeekBar进度，将播放器seekTo到响应的位置pos.<br>
     * curProgress：SeekBar当前进度<br>
     * maxProgress：SeekBar最大进度<br>
     * mPlayer.getDuration:视频总时长<br>
     * 
     * currentPos = curProgress / maxProgress * getDuration
     */
    private OnSeekBarChangeListener mSeekBarChangeListener = new OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            int curProgress = seekBar.getProgress();
            int maxProgress = seekBar.getMax();

            if (curProgress > 0 && curProgress <= maxProgress) {
                float percentage = ((float) curProgress) / maxProgress;
                int position = (int) (mPlayer.getDuration() * percentage);
                mPlayer.seekTo(position);
                mPlayer.start();
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser && isShowing()) {// fromUser用户拖动触发
                show();
            }
        }
    };

    /**************** MediaController显示、隐藏 *******************/
    @Override
    protected void onShow() {
        setVisibility(View.VISIBLE);
    }

    @Override
    protected void onHide() {
        setVisibility(View.INVISIBLE);
    }

    /**
     * 显示MediaController时，根据当前播放时间，SeekBar的位置进行相应更新<br>
     * curProgress：SeekBar当前进度<br>
     * maxProgress：SeekBar最大进度<br>
     * mPlayer.getCurrentPosition:视频当前播放时间 mPlayer.getDuration:视频总时长<br>
     * 
     * curProgress = mPlayer.getCurrentPosition / mPlayer.getDuration * maxProgress
     */
    @Override
    protected void onTimerTicker() {
        int curTime = mPlayer.getCurrentPosition();
        int durTime = mPlayer.getDuration();

        if (durTime > 0 && curTime <= durTime) {
            float percentage = ((float) curTime) / durTime;
            updateVideoProgress(percentage);
        }
    }

    /******************* 分发Touch事件 **************************/
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /******************* 更新播放器状态 **************************/
    public void updateVideoButtonState(boolean isStart) {
        // 播放中
        if (isStart) {
            mPauseButton.setBackgroundResource(R.drawable.ic_media_play);
            if (mPlayer.canPause()) {
                mPauseButton.setEnabled(true);
            } else {
                mPauseButton.setEnabled(false);
            }
        }
        // 未播放
        else {
            mPauseButton.setBackgroundResource(R.drawable.ic_media_pause);
        }
    }

    private void updateVideoProgress(float percentage) {
        if (percentage >= 0 && percentage <= 1) {
            int progress = (int) (percentage * mProgress.getMax());
            mProgress.setProgress(progress);
            Log.d("lyj", "progress=" + percentage);
        }
    }
}
