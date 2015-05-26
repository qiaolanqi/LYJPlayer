package com.liyuejiao.player;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.SeekBar;

public class MediaControllerSmall extends MediaControllerBase implements View.OnClickListener {

    private ImageButton mPauseButton;
    private ProgressBar mProgress;
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

        mProgress = (ProgressBar) findViewById(R.id.mediacontroller_progress);
        if (mProgress instanceof SeekBar) {
            SeekBar seeker = (SeekBar) mProgress;
            seeker.setOnSeekBarChangeListener(mSeekListener);
        }
        if (mProgress != null) {
            mProgress.setMax(1000);
        }
        mFullScreenButton = (ImageButton) findViewById(R.id.mediacontroller_fullscreen);
        if (mFullScreenButton != null) {
            mFullScreenButton.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onShow() {
        setVisibility(View.VISIBLE);
    }

    @Override
    protected void onHide() {
        setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("lyj", "MediaControllerSmall dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("lyj", "MediaControllerSmall onTouchEvent");
        return super.onTouchEvent(event);
    }

}
