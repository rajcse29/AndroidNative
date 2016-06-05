package com.google.android.exoplayer.demo.Model;

/**
 * Created by raj on 5/18/16.
 */
public class Channel {
    public int channelId;
    public String channelName;
    public int channelIcon;

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public int getChannelIcon() {
        return channelIcon;
    }

    public void setChannelIcon(int channelIcon) {
        this.channelIcon = channelIcon;
    }
}
