package com.liyuejiao.player;

import com.liyuejiao.player.util.PlayerUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MediaControllerMini extends MediaControllerBase implements View.OnClickListener {

    private ImageView mIvPause;
    private RelativeLayout mLyTop;
    private RelativeLayout mLyBottom;
    private ImageView mIvClose;
    private TextView mTvTitle;
    private TextView mTvTime;
    private ImageView mIvFullScreen;

    public MediaControllerMini(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public MediaControllerMini(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MediaControllerMini(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        mLayoutInflater.inflate(R.layout.media_controller_mini, this);
        mIvPause = (ImageView) findViewById(R.id.mediacontroller_pause);
        if (mIvPause != null) {
            mIvPause.setOnClickListener(this);
        }

        mLyTop = (RelativeLayout) findViewById(R.id.mediacontroller_top_ll);
        mLyBottom = (RelativeLayout) findViewById(R.id.mediacontroller_bottom_ll);

        mIvClose = (ImageView) findViewById(R.id.mediacontroller_mini_close);
        if (mIvClose != null) {
            mIvClose.setOnClickListener(this);
        }
        mTvTitle = (TextView) findViewById(R.id.mediacontroller_mini_title);
        mTvTime = (TextView) findViewById(R.id.mediacontroller_mini_time);
        mIvFullScreen = (ImageView) findViewById(R.id.mediacontroller_mini_fullscreen);
        if (mIvFullScreen != null) {
            mIvFullScreen.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.mediacontroller_pause:
            // 播放中
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                show(0);// 暂停时MediaController永久显示
            }
            // 未播放
            else {
                mPlayer.start();
                show();
            }
            break;
        case R.id.mediacontroller_mini_close:
            mPlayer.onFloatWindowClose();
            break;
        case R.id.mediacontroller_mini_fullscreen:
            mPlayer.onFloatWindowFullScreen();
            break;
        default:
            break;
        }
    }

    /**************** MediaController显示、隐藏 *******************/
    @Override
    protected void onShow(int what) {
        setVisibility(View.VISIBLE);
    }

    @Override
    protected void onHide() {
        setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onTimerTicker() {
        long curTime = mPlayer.getCurrentPosition();
        long durTime = mPlayer.getDuration();

        if (durTime > 0 && curTime <= durTime) {
            // float percentage = ((float) curTime) / durTime;
            mTvTime.setText(PlayerUtils.stringForTime(curTime) + "/" + PlayerUtils.stringForTime(durTime));
        }
    }

    /******************* 分发Touch事件 **************************/
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d("lyj", "MediaControllerMini dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("lyj", "MediaControllerMini onTouchEvent");
        return super.onTouchEvent(event);
    }

    public void updateVideoTitle(String name) {
        mTvTitle.setText(name);
    }

    /******************* 更新播放器状态 **************************/
    public void updateVideoButtonState(boolean isStart) {
        // 播放中
        if (isStart) {
            mIvPause.setBackgroundResource(R.drawable.ic_pause_mini_normal);
            if (mPlayer.canPause()) {
                mIvPause.setEnabled(true);
            } else {
                mIvPause.setEnabled(false);
            }
        }
        // 未播放
        else {
            mIvPause.setBackgroundResource(R.drawable.ic_play_mini_normal);
        }
    }
}
