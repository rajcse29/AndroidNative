package com.google.android.exoplayer.demo.service;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.exoplayer.demo.StreamingPlayer;
import com.google.android.exoplayer.demo.Model.ChannelLink;
import com.google.android.exoplayer.demo.PlayerActivity;
import com.google.android.exoplayer.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by raj on 5/25/16.
 */
public class ChannelLinkParse {
    private Context context;
    private int channelId;

    public String sboxLink360;
    public String sboxLink720;
    public String sboxLink180;
    public String sboxLink480;

    public ChannelLinkParse(Context context, int channelId){
        this.channelId = channelId;
        this.context = context;
        init();
    }

    private void init() {
        String url = "http://www.livestreamer.com/api/channelLink/" + channelId;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonRootObject = new JSONObject(response);
                    Boolean success = (boolean)jsonRootObject.get("success");
                    if(success) {
                        JSONArray jsonArray = jsonRootObject.optJSONArray("datarows");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            int channelId = Integer.parseInt(jsonObject.optString("channelId").toString());
                            sboxLink180 = jsonObject.optString("sboxLink180").toString();
                            sboxLink360 = jsonObject.optString("sboxLink360").toString();
                            sboxLink480 = jsonObject.optString("sboxLink480").toString();
                            sboxLink720 = jsonObject.optString("sboxLink720").toString();
                            ChannelLink channelLink = new ChannelLink();
                            channelLink.setChannelId(channelId);
                            channelLink.setSboxLink180(sboxLink180);
                            channelLink.setSboxLink360(sboxLink360);
                            channelLink.setSboxLink480(sboxLink480);
                            channelLink.setSboxLink720(sboxLink720);



                            //StreamingPlayer.path = sboxLink180;
                            //StreamingPlayer.path = "http://192.168.16.6:8082";
                            //Toast.makeText(context, "sboxLink180 = " + sboxLink180, Toast.LENGTH_SHORT).show();
                            StreamingPlayer.channelLink = channelLink;
                            Intent intent = new Intent(context, StreamingPlayer.class);

//            intent.setData(Uri.parse(sboxLink360));
            //intent.setData(Uri.parse("https://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8"));
//            intent.putExtra(PlayerActivity.CONTENT_ID_EXTRA, "Apple AAC media playlist");
//            intent.putExtra(PlayerActivity.CONTENT_TYPE_EXTRA, Util.TYPE_HLS);
//            intent.putExtra(PlayerActivity.PROVIDER_EXTRA, "");
            context.startActivity(intent);

                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.i("ERROR..", error.toString());
            }
        });
        Volley.newRequestQueue(context).add(stringRequest);
    }
}
