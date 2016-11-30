package com.example.user.a20161124_project;

/**
 * Created by user on 2016/11/24.
 */

public class Thingspeak {
    private String Channel;
    private Feeds[] feeds;

    public void setFeeds(Feeds[] feeds)
    {
        this.feeds = feeds;
    }

    public Feeds[] getFeeds()
    {
        return feeds;
    }
}

