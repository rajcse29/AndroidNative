package com.google.android.exoplayer.demo.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.exoplayer.demo.Model.Channel;
import com.google.android.exoplayer.demo.R;
import com.google.android.exoplayer.demo.materialdesignone.MainActivity;
import com.google.android.exoplayer.demo.materialdesignone.categoryDetail;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by raj on 5/24/16.
 */
public class ChannelParseByCountry {

    private String countryName;
    private Boolean success;
    private JSONObject jsonRootObject;
    private Context context;
    private String root;
    private int counter = 0;
    private List<String> logoList = new ArrayList<>();
    private List<String> channelNameList = new ArrayList<>();
    private List<Channel> channels;

    public ChannelParseByCountry(Context context, String countryName) {
        this.context = context;
        this.countryName = countryName;
        channels = new ArrayList<Channel>();
        createLogoDirectory();
    }

    public List<Channel> getData(String response) {
        try {
            channels = new ArrayList<>();
            jsonRootObject = new JSONObject(response);
            success = (boolean) jsonRootObject.get("success");
            if (success) {
                JSONArray jsonArray = jsonRootObject.optJSONArray("datarows");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = (JSONObject) jsonArray.get(i);
                    Integer channelId = (Integer) obj.get("channelId");
                    String channelName = (String) obj.get("channelName");
                    String link = (String) obj.get("link");
                    String logo = (String) obj.get("logo");
                    logoList.add(logo);
                    channelNameList.add(channelName);

                    new downloadLogo().execute("http://www.livestreamer.com/images/" + logo);
                }
                Map<String, List<Channel>> channelsByCountry = new HashMap<>();
                channelsByCountry.put(countryName, channels);
                Storage.getInstance().setChannelsByCountry(channelsByCountry);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return channels;
    }

    private void createLogoDirectory() {
        root = Environment.getExternalStorageDirectory().toString();
        File logoDirectory = new File(root + "/channel_logo");
        if (!logoDirectory.exists()) {
            logoDirectory.mkdirs();
        }
    }
//            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

    private class downloadLogo extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String...logoUrl) {
            Bitmap bitMap = null;
            try {
                URL url = new URL(logoUrl[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                bitMap = BitmapFactory.decodeStream(input);

            } catch (Exception ex){
                ex.printStackTrace();
            }
            return  bitMap;
        }

        protected void onPostExecute(Bitmap bitMap) {
            try {
                OutputStream fOut = null;
                String logoName = logoList.get(counter);
                String logoPath = root + "/channel_logo/" + logoName;

                //Log.d("logoName.......", logoName);
                //Toast.makeText(context, "logoPath:" + logoPath, Toast.LENGTH_SHORT).show();
                File file = new File(logoPath);
                file.createNewFile();
                fOut = new FileOutputStream(file);



                bitMap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
                fOut.flush();
                fOut.close();

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitMap, 100, 100, true);


                Channel channel = new Channel();
                channel.setChannelName(channelNameList.get(counter));
                channel.setChannelBitMap(scaledBitmap);
                channels.add(channel);
                counter++;

                MainActivity.populateNewData(channels);

            } catch (Exception ex) {
                ex.printStackTrace();

            }
        }
    }
}
