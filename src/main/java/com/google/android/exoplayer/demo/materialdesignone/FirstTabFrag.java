package com.google.android.exoplayer.demo.materialdesignone;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.exoplayer.demo.Model.Channel;
import com.google.android.exoplayer.demo.R;
import com.google.android.exoplayer.demo.adapter.RecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;


/**
 * Developed by root on 4/2/16.
 */
public class FirstTabFrag extends Fragment {

    public View firstTabLayout;
    public RecyclerView firstTabRecyclerView;
    private static FirstTabFrag firstTabFrag;
    private TextView text;
    private GridLayoutManager lLayout;
    private RecyclerViewAdapter rcAdapter;
    public List<Channel> data = new ArrayList<>();

    public static FirstTabFrag getInstance(){
            firstTabFrag = new FirstTabFrag();
        return firstTabFrag;
    }

    public List<Channel> getData() {
        return data;
    }

    public void setData(List<Channel> data) {
        this.data = data;
    }

    public RecyclerViewAdapter getRcAdapter() {
        return rcAdapter;
    }

    public void setRcAdapter(RecyclerViewAdapter rcAdapter) {
        this.rcAdapter = rcAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        firstTabLayout = inflater.inflate(R.layout.firsttabcontent,container,false);
        lLayout = new GridLayoutManager(this.getActivity(), 7);

        RecyclerView rView = (RecyclerView) firstTabLayout.findViewById(R.id.firstTabContentList);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(lLayout);


        rcAdapter = new RecyclerViewAdapter(this.getActivity(), data);
        rView.setAdapter(rcAdapter);
        return firstTabLayout;
    }
}
