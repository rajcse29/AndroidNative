package com.google.android.exoplayer.demo.service;

import com.google.android.exoplayer.demo.Model.Channel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by raj on 5/25/16.
 */
public class Storage {
    private static Storage storage;
    private static String currentCountry;
    private static Map<String, List<Channel>> channelsByCountry = new HashMap<>();
    private String currentBitRate;

    public static Storage getInstance(){
        if (storage == null){
            storage = new Storage();
        }
        return storage;
    }

    public static Storage getStorage() {
        return storage;
    }

    public static void setStorage(Storage storage) {
        Storage.storage = storage;
    }

    public static String getCurrentCountry() {
        return currentCountry;
    }

    public static void setCurrentCountry(String currentCountry) {
        Storage.currentCountry = currentCountry;
    }

    public static Map<String, List<Channel>> getChannelsByCountry() {
        return channelsByCountry;
    }

    public static void setChannelsByCountry(Map<String, List<Channel>> channelsByCountry) {
        Storage.channelsByCountry = channelsByCountry;
    }

    public String getCurrentBitRate() {
        return currentBitRate;
    }

    public void setCurrentBitRate(String currentBitRate) {
        this.currentBitRate = currentBitRate;
    }
}
