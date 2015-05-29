package com.liyuejiao.player.util;

import java.util.Formatter;
import java.util.Locale;

import android.content.Context;
import android.provider.Settings;

public class PlayerUtils {
    private static StringBuilder mFormatBuilder;
    private static Formatter mFormatter;

    static {
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }

    public static String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    /**
     * 检查系统是否关闭了自动旋转屏幕功能
     * @param context
     * @return 
     */
    public static boolean checkSystemGravity(Context context) {
        int flag = Settings.System.getInt(context.getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0);
        if (flag == 1) {
            return true;
        }
        return false;
    }
    
}
