package com.liyuejiao.player;

public enum PlayMode {
    PLAYMODE_WINDOW(0), PLAYMODE_FULLSCREEN(1), PLAYMODE_MINI(2);

    private int playMode;

    private PlayMode(int playMode) {
        this.playMode = playMode;
    }

}
