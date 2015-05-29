package com.liyuejiao.player.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.liyuejiao.player.local.LocalVideo;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;

/**
 * 本地视频扫描
 * 
 * @author lyj
 * 
 */
public class VideoScanner {
    public static final double BASE_LENGTH_BYTES = 1024.0 * 1024.0;
    public static String[] MediaSupportedFiles = new String[] { ".ts", ".mov", ".wmv", ".3gp", ".mpeg",
            ".mpg", ".VOB", ".WEBM", ".F4V", ".flv", ".mp4", ".rmvb", ".rm", ".mkv", ".xv", ".avi" };
    private VideoScannTask mScanVideoTask;
    private Context mContext;
    private OnScanListener mOnScanListener;

    public VideoScanner(Context context) {
        mContext = context;
    }

    public void setOnScanListener(OnScanListener listener) {
        mOnScanListener = listener;
        if (mScanVideoTask != null) {
            mScanVideoTask.setOnScanListener(mOnScanListener);
        }
    }

    public void start() {
        cancelTask();
        mScanVideoTask = new VideoScannTask(mContext);
        if (mOnScanListener == null) {
            throw new NullPointerException("OnScanListener must not be null,set setOnScanListener first");
        }
        mScanVideoTask.setOnScanListener(mOnScanListener);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            mScanVideoTask.execute();
        } else {
            mScanVideoTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private void cancelTask() {
        if (mScanVideoTask != null) {
            mScanVideoTask.cancel(true);
            mScanVideoTask = null;
        }
    }

    private class VideoScannTask extends AsyncTask<Void, Void, List<LocalVideo>> {
        private List<LocalVideo> dirList;
        private OnScanListener onScanListener;

        public VideoScannTask(Context context) {
            dirList = new ArrayList<LocalVideo>();
        }

        public void setOnScanListener(OnScanListener listener) {
            onScanListener = listener;
        }

        @Override
        protected void onPreExecute() {
            if (onScanListener != null) {
                onScanListener.onStart();
            }
        }

        @Override
        protected void onPostExecute(List<LocalVideo> result) {
            if (onScanListener != null) {
                onScanListener.onStop(result);
            }
        }

        @Override
        protected List<LocalVideo> doInBackground(Void... params) {
            return scan(Environment.getExternalStorageDirectory());
        }

        private List<LocalVideo> scan(File file) {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length && !isCancelled(); i++) {
                    File childFile = files[i];
                    if (childFile.isFile() && isSupportedVideoFile(childFile)) {// 文件
                        float fileSize = (float) (childFile.length() / BASE_LENGTH_BYTES);
                        LocalVideo video = new LocalVideo();
                        video.name = childFile.getName();
                        video.path = childFile.getPath();
                        video.size = fileSize;
                        dirList.add(video);
                    }

                }
            }
            return dirList;
        }

        /**
         * 判断视频文件格式是否支持
         * 
         * @param childFile
         * @return
         */
        private boolean isSupportedVideoFile(File file) {
            String fileName = file.getName().trim().toLowerCase(Locale.US);
            for (String str : MediaSupportedFiles) {
                if (fileName.endsWith(str)
                        || fileName.endsWith(str.toLowerCase(Locale.US))
                        || fileName.endsWith(str.toUpperCase(Locale.US))) {
                    return true;
                }
            }
            return false;
        }

    }

    public interface OnScanListener {
        public void onStart();

        public void onStop(List<LocalVideo> result);
    }
}
