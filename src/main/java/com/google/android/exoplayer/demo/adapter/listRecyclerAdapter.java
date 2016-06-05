package com.google.android.exoplayer.demo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.exoplayer.demo.Model.Channel;
import com.google.android.exoplayer.demo.Model.Country;
import com.google.android.exoplayer.demo.R;
import com.google.android.exoplayer.demo.materialdesignone.MainActivity;
import com.google.android.exoplayer.demo.materialdesignone.NavigationDrawerFrag;
import com.google.android.exoplayer.demo.materialdesignone.categoryDetail;
import com.google.android.exoplayer.demo.service.ChannelParseByCountry;
import com.google.android.exoplayer.demo.service.Storage;


import java.util.ArrayList;
import java.util.List;

/**
 * Developed by rubayet on 3/29/16.
 */

public class listRecyclerAdapter extends RecyclerView.Adapter<listRecyclerAdapter.myViewHolder> {

    private LayoutInflater inflater;
    public List<categoryDetail> data = new ArrayList<>();
    private Context context;


    public listRecyclerAdapter(Context context , List<categoryDetail> data){
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View customRootView = inflater.inflate(R.layout.custom_row,parent,false);
        myViewHolder viewHolder = new myViewHolder(customRootView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        categoryDetail current = data.get(position);
        holder.title.setText(current.categoryName);
        holder.icon.setImageResource(current.icon);
    }

    @Override
    public int getItemCount() {

        if (data != null){
            return data.size();
        }
        return 0;
    }



    class myViewHolder extends RecyclerView.ViewHolder{

        TextView title;
        ImageView icon;

        public myViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.customRowtitle);
            icon = (ImageView) itemView.findViewById(R.id.customRowImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(final Country ct : NavigationDrawerFrag.allCountry){
                        if(ct.getCountyName().equalsIgnoreCase((String) title.getText())){
                            String url = "http://www.livestreamer.com/api/liveChannelByCountry/" + ct.getCounrtyId();
                            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Storage.getInstance().setCurrentCountry(ct.getCountyName());
                                        ChannelParseByCountry channelParseByCountry = new ChannelParseByCountry(context, ct.getCountyName());
                                        List<Channel> channels = channelParseByCountry.getData(response);
                                        //MainActivity.populateNewData(channels);

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
                }
            });
        }
    }
}
