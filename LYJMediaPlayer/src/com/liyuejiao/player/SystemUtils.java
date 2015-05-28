package com.liyuejiao.player;

import android.app.Activity;
import android.util.DisplayMetrics;

public class SystemUtils {
    private static DisplayMetrics sDisplayMetrics;

    /**
     * 加载屏幕宽高信息
     * 
     * @param activity
     */
    public static void loadScreenInfo(Activity activity) {
        if (sDisplayMetrics == null) {
            sDisplayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(sDisplayMetrics);
        }
    }

    public static int getScreenWidth() {
        return sDisplayMetrics.widthPixels;
    }

    public static int getScreenHeight() {
        return sDisplayMetrics.heightPixels;
    }
}
