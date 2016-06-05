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
    private List<Channel> channels;
    private Context context;
    private String urlLogo;
    private ImageView imageView = null;
    private String root;

    public ChannelParseByCountry(Context context, String countryName) {
        this.context = context;
        this.countryName = countryName;
        channels = new ArrayList<Channel>();
        createLogoDirectory();
    }

    public List<categoryDetail> getData(String response, List<categoryDetail> data) {
        try {
            data.clear();
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

                    saveChannelLogo(logo);

                    Channel channel = new Channel();
                    channel.setChannelId(channelId);
                    channel.setChannelName(channelName);
                    channels.add(channel);

                    categoryDetail current = new categoryDetail();
                    current.categoryName = channelName;
                    current.icon = R.drawable.ic_play_arrow;
                    data.add(current);
                }
                Map<String, List<Channel>> channelsByCountry = new HashMap<>();
                channelsByCountry.put(countryName, channels);
                Storage.getInstance().setChannelsByCountry(channelsByCountry);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }

    private void createLogoDirectory() {
        root = Environment.getExternalStorageDirectory().toString();
        File logoDirectory = new File(root + "/channel_logo");
        if (!logoDirectory.exists()) {
            logoDirectory.mkdirs();
        }
    }

    private void saveChannelLogo(String logo) {
        urlLogo = "http://www.livestreamer.com/images/" + logo;
        try {
            Toast.makeText(context, "urlLogo:" + urlLogo, Toast.LENGTH_LONG).show();
            new downloadLogo().execute(urlLogo);

//            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            int bmpByte = myBitmap.getByteCount();
//            String st = Integer.toString(bmpByte);
//            Toast.makeText(context, "byteCount:" + st, Toast.LENGTH_LONG).show();
//
//            Log.d("byte count.............", st);
//
//            OutputStream fOut = null;
//            String logoPath = root + "/channel_logo/" + logo +".png";
//            Toast.makeText(context, "logoPath:" +logoPath, Toast.LENGTH_LONG).show();
//            File file = new File(logoPath);
//            file.createNewFile();
//            fOut = new FileOutputStream(file);
//
//            // 100 means no compression, the lower you go, the stronger the compression
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, fOut);
//            fOut.flush();
//            fOut.close();

            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class downloadLogo extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String...urlLogo) {
            Bitmap myBitmap = null;
            try {
                URL url = new URL(urlLogo[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                myBitmap = BitmapFactory.decodeStream(input);

            } catch (Exception ex){
                ex.printStackTrace();
            }
            return  myBitmap;

        }

        protected void onPostExecute(Bitmap result) {
            int bmpByte = result.getByteCount();
            String st = Integer.toString(bmpByte);
            Toast.makeText(context, "byteCount:" + st, Toast.LENGTH_LONG).show();
            Log.d("byte count.............", st);
        }
    }
}
