package com.liyuejiao.player.local;

public class LocalVideo {
    public String name;

    public String path;

    public float size;
    
    @Override
    public String toString() {
        return "[name=" + name + " path=" + path + " size=" + size + "]";
    }
}