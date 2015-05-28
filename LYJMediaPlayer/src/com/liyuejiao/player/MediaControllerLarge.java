package com.liyuejiao.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MediaControllerLarge extends MediaControllerBase implements View.OnClickListener {
    private ImageButton mPauseButton;
    private SeekBar mProgress;
    private TextView mCurrentTime;
    private TextView mTotalTime;
   

    public MediaControllerLarge(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public MediaControllerLarge(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MediaControllerLarge(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        mLayoutInflater.inflate(R.layout.media_controller_large, this);
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
        mCurrentTime = (TextView) findViewById(R.id.mediacontroller_time);
        mTotalTime = (TextView) findViewById(R.id.mediacontroller_total);
        mVoiceLightWidget = (VoiceLightWidget) findViewById(R.id.voice_light_widget);
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
        default:
            break;
        }
    }

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

    @Override
    protected void onTimerTicker() {
        int curTime = mPlayer.getCurrentPosition();
        int durTime = mPlayer.getDuration();

        if (durTime > 0 && curTime <= durTime) {
            float percentage = ((float) curTime) / durTime;
            updateVideoProgress(percentage);
            mCurrentTime.setText(PlayerUtils.stringForTime(curTime));
            mTotalTime.setText(PlayerUtils.stringForTime(durTime));
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
        }
    }

}
