package com.liyuejiao.player;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VideoView mVideoView = (VideoView) findViewById(R.id.videoView1);
        // 设置播放路径
        mVideoView.setVideoURI(Uri.parse("android.resource://"
                + getPackageName() + "/" + R.raw.videoviewdemo));
        // 设置播放器的控制界面
        MediaController controller = new MediaController(this);
        mVideoView.setMediaController(controller);
        // 开始播放
        mVideoView.start();
    }

}
