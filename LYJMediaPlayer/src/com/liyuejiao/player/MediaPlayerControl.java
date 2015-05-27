package com.liyuejiao.player;

/**
 * 播放器控制接口
 */
public interface MediaPlayerControl {
    void start();

    void pause();

    int getDuration();

    int getCurrentPosition();

    void seekTo(int pos);

    boolean isPlaying();

    int getBufferPercentage();

    boolean canPause();

    boolean canSeekBackward();

    boolean canSeekForward();
    
    void onRequestPlayMode(PlayMode requestPlayMode);
}
