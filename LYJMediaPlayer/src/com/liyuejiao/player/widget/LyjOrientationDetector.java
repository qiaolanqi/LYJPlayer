package com.liyuejiao.player.widget;

import com.liyuejiao.player.PlayMode;
import com.liyuejiao.player.util.PlayerUtils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.OrientationEventListener;

public class LyjOrientationDetector extends OrientationEventListener {
    public static final int SCREEN_ORIENTATION_UNKNOWN = -2;
    public static final int SCREEN_ORIENTATION_FLAT = -1;
    public static final int SCREEN_ORIENTATION_PORTRAIT_NORMAL = 0;
    public static final int SCREEN_ORIENTATION_LANDSCAPE_INVERTED = 90;
    public static final int SCREEN_ORIENTATION_PORTRAIT_INVERTED = 180;
    public static final int SCREEN_ORIENTATION_LANDSCAPE_NORMAL = 270;
    private int mScreenOrientation;
    private Context mContext;
    private OnReuqestPlayModeListener mOnReuqestPlayModeListener;

    public LyjOrientationDetector(Context context, int orientation) {
        super(context);
        mContext = context;
        mScreenOrientation = orientation;
    }

    @Override
    public void onOrientationChanged(int orientation) {
        int currOrientation = -1;
        PlayMode requestPlayMode = null;
        if (orientation > 350 || orientation < 10) { // 0度 竖屏正方向
            currOrientation = SCREEN_ORIENTATION_PORTRAIT_NORMAL;
            requestPlayMode = PlayMode.PLAYMODE_WINDOW;
        }
        else if (orientation > 80 && orientation < 100) { // 90度 横屏反方向
            currOrientation = SCREEN_ORIENTATION_LANDSCAPE_INVERTED;
            requestPlayMode = PlayMode.PLAYMODE_FULLSCREEN;
        }
        else if (orientation > 170 && orientation < 190) { // 180度 竖屏反方向
            currOrientation = SCREEN_ORIENTATION_PORTRAIT_INVERTED;
            requestPlayMode = PlayMode.PLAYMODE_WINDOW;
        }
        else if (orientation > 260 && orientation < 280) { // 270度 横屏正方向
            currOrientation = SCREEN_ORIENTATION_LANDSCAPE_NORMAL;
            requestPlayMode = PlayMode.PLAYMODE_FULLSCREEN;
        }
        if (mScreenOrientation != currOrientation && requestPlayMode != null) {
            // 系统设置中关闭了自动旋转功能
            if (!PlayerUtils.checkSystemGravity(mContext)) {
                return;
            }
            mScreenOrientation = currOrientation;
            if (mOnReuqestPlayModeListener != null) {
                mOnReuqestPlayModeListener.onRequestPlayMode(requestPlayMode);
            }
            doScreenOrientationRotate(mScreenOrientation);
        }
    }

    /**
     * 强制旋转屏幕
     * @param screenOrientation
     */
    public void doScreenOrientationRotate(int screenOrientation) {
        Activity activity = (Activity) mContext;
        switch (screenOrientation) {
        case SCREEN_ORIENTATION_PORTRAIT_NORMAL:
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            break;
        case SCREEN_ORIENTATION_LANDSCAPE_NORMAL:
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            break;
        case SCREEN_ORIENTATION_PORTRAIT_INVERTED:
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
            break;
        case SCREEN_ORIENTATION_LANDSCAPE_INVERTED:
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            break;
        default:
            break;
        }
    }

    public void enableOrientation() {
        enable();
    }

    public void disableOrientation() {
        disable();
    }

    public void setOnRequestPlayMode(OnReuqestPlayModeListener l) {
        mOnReuqestPlayModeListener = l;
    }

    public interface OnReuqestPlayModeListener {
        void onRequestPlayMode(PlayMode requestPlayMode);
    }
}
