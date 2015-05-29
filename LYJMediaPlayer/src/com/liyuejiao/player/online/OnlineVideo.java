package com.liyuejiao.player.online;

public class OnlineVideo {
    public String name;

    public String uri;

    public OnlineVideo(String name, String uri) {
        super();
        this.name = name;
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "[name=" + name + " uri=" + uri + "]";
    }

}
