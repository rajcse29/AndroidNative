package com.google.android.exoplayer.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.ListPreference;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.exoplayer.demo.Model.ChannelLink;
import com.google.android.exoplayer.demo.service.Storage;

/**
 * Created by raj on 5/31/16.
 */
public class StreamingPlayer extends Activity implements
        MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener,
        MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback{

    private static final String TAG = StreamingPlayer.class.getSimpleName();
    private int mVideoWidth;
    private int mVideoHeight;
    private MediaPlayer mMediaPlayer;
    private SurfaceView mPreview;
    private SurfaceHolder holder;
    public static String path;
    public static ChannelLink channelLink;
    private int replayCounter = 0;

    private boolean mIsVideoSizeKnown = false;
    private boolean mIsVideoReadyToBePlayed = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().getDecorView().setSystemUiVisibility(View.GONE);
        setContentView(R.layout.mediaplayer_2);
        mPreview = (SurfaceView) findViewById(R.id.surface);
        holder = mPreview.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

//        Spinner bitRateTypeSpinner = (Spinner) findViewById(R.id.bitRateType);
//        String[] items = new String[]{"180", "360", "480", "720"};
//        ArrayAdapter<String> bitRateTypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items);
//        bitRateTypeSpinner.setAdapter(bitRateTypeAdapter);
//        bitRateTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String selectedItem = parent.getItemAtPosition(position).toString();
//                if (selectedItem.equals("180")) {
//                    onPause();
//                    path = channelLink.getSboxLink180();
//                    playVideo();
//
//                } else if (selectedItem.equals("360")) {
//                    onPause();
//                    path = channelLink.getSboxLink360();
//                    playVideo();
//                    // SetStringRequest("http://www.livestreamer.com/api/allCategory");
//                } else if (selectedItem.equals("480")) {
//                    onPause();
//                    path = channelLink.getSboxLink480();
//                    playVideo();
//                } else if (selectedItem.equals("720")) {
//                    onPause();
//                    path = channelLink.getSboxLink720();
//                    playVideo();
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
    }

    private void playVideo() {
        doCleanUp();
        try {
            //path = "http://wbde01.livestreamer.com:1935/wbde01/bdlivestreamerRTV1457781239769_180/playlist.m3u8";
            if (path == "") {
                // Tell the user to provide a media file URL.
                Toast.makeText(this, "Please edit MediaPlayerDemo_Video Activity," + " and set the path variable to your media file URL.", Toast.LENGTH_LONG).show();
            }

            Log.e("PATH", "Path = " + path);
            // Creating a new media player and setting the listeners
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(path);
            mMediaPlayer.setDisplay(holder);
            mMediaPlayer.setOnBufferingUpdateListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.prepare();
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnVideoSizeChangedListener(this);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        } catch (Exception e) {
            //replay();
            Log.e(TAG, "error: can not play video: " + e.getMessage(), e);
        }
    }

//    private void replay() {
//        try {
//            replayCounter++;
//            if(replayCounter <= 3){
//                Thread.sleep(1000);
//                playVideo();
//            } else {
//                replayCounter = 0;
//                onDestroy();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public void onBufferingUpdate(MediaPlayer arg0, int percent) {
        Log.d(TAG, "onBufferingUpdate percent:" + percent);

    }

    public void onCompletion(MediaPlayer arg0) {
        Log.d(TAG, "onCompletion called");
    }

    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
        Log.v(TAG, "onVideoSizeChanged called");
        if (width == 0 || height == 0) {
            Log.e(TAG, "invalid video width(" + width + ") or height(" + height + ")");
            return;
        }
        mIsVideoSizeKnown = true;
        mVideoWidth = width;
        mVideoHeight = height;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    public void onPrepared(MediaPlayer mediaplayer) {
        Log.d(TAG, "onPrepared called");
        mIsVideoReadyToBePlayed = true;
        if (mIsVideoReadyToBePlayed && mIsVideoSizeKnown) {
            startVideoPlayback();
        }
    }

    public void surfaceChanged(SurfaceHolder surfaceholder, int i, int j, int k) {
        Log.d(TAG, "surfaceChanged called");

    }

    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
        Log.d(TAG, "surfaceDestroyed called");
    }


    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated called");
        selectLink();
        playVideo();
    }


    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaPlayer();
        doCleanUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
        doCleanUp();
    }

    private void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void doCleanUp() {
        mVideoWidth = 0;
        mVideoHeight = 0;
        mIsVideoReadyToBePlayed = false;
        mIsVideoSizeKnown = false;
    }

    private void startVideoPlayback() {
        Log.v(TAG, "startVideoPlayback");
        holder.setFixedSize(mVideoWidth, mVideoHeight);
        mMediaPlayer.start();
    }

    private void selectLink() {
        String bitRate = Storage.getInstance().getCurrentBitRate();
        if(bitRate.equalsIgnoreCase("180p")){
            path = channelLink.getSboxLink180();
        } else if(bitRate.equalsIgnoreCase("360p")){
            path = channelLink.getSboxLink360();
        } else if(bitRate.equalsIgnoreCase("480p")){
            path = channelLink.getSboxLink480();
        } else if(bitRate.equalsIgnoreCase("720p")){
            path = channelLink.getSboxLink720();
        } else {
            path = channelLink.getSboxLink720();
        }
    }


}