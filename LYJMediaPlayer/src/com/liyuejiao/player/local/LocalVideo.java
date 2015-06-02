package com.liyuejiao.player.local;

public class LocalVideo {
    public String name;

    public String path;

    public float size;

    public LocalVideo() {
    }

    public LocalVideo(String name, String path) {
        super();
        this.name = name;
        this.path = path;
    }

    @Override
    public String toString() {
        return "[name=" + name + " path=" + path + " size=" + size + "]";
    }
}
