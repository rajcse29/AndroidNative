package com.google.android.exoplayer.demo.Model;

import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ImageView;

/**
 * Created by raj on 5/18/16.
 */
public class Channel {
    public int channelId;
    public String channelName;
    public Bitmap channelBitMap;

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

    public Bitmap getChannelBitMap() {
        return channelBitMap;
    }

    public void setChannelBitMap(Bitmap channelBitMap) {
        this.channelBitMap = channelBitMap;
    }
}
