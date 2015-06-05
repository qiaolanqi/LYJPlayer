package com.liyuejiao.player;

import android.content.Context;
import android.util.AttributeSet;
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
                show(0);// 暂停市MediaController永久显示
            }
            // 未播放
            else {
                mPlayer.start();
                show();
            }
            break;
        case R.id.mediacontroller_mini_close:

            break;
        case R.id.mediacontroller_mini_fullscreen:

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
        // TODO Auto-generated method stub

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

    public void updateVideoTitle(String name) {
        mTvTitle.setText(name);
    }
}
