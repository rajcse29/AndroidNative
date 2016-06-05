package com.google.android.exoplayer.demo.materialdesignone;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.exoplayer.demo.Model.Country;
import com.google.android.exoplayer.demo.R;
import com.google.android.exoplayer.demo.adapter.listRecyclerAdapter;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationDrawerFrag extends Fragment {

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle  mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    boolean mDrawer = false;
    private View drawerLayout;
    public static List<Country> allCountry;
    public List<categoryDetail> data = new ArrayList<>();

    private listRecyclerAdapter adapter;

    public NavigationDrawerFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment

        drawerLayout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) drawerLayout.findViewById(R.id.drawerRecyclerList);
        recyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));

        Spinner selectionSpinner = (Spinner) drawerLayout.findViewById(R.id.selectionType);
        String[] items = new String[]{"Country", "Category", "Language"};
        ArrayAdapter<String> selectionAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        selectionSpinner.setAdapter(selectionAdapter);
        selectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    if (selectedItem.equals("Country")) {
                       // countries = getAllCountry("http://www.livestreamer.com/api/allCountry");
                       getAllCountry("http://www.livestreamer.com/api/allCountry");

                    } else if (selectedItem.equals("Category")) {
                        Toast.makeText(getActivity(), selectedItem, Toast.LENGTH_SHORT).show();
                        // SetStringRequest("http://www.livestreamer.com/api/allCategory");
                    } else if (selectedItem.equals("Language")) {
                        Toast.makeText(getActivity(), selectedItem, Toast.LENGTH_SHORT).show();
                        // SetStringRequest("http://www.livestreamer.com/api/allLanguage");
                    }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return drawerLayout;
    }

    public void setUp(DrawerLayout drawerLayout, Toolbar toolbar) {
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                /*if (!mDrawer){
                    mDrawer=true;
                    saveToSharedPref(getActivity(),"drawerState",mDrawer+"");
                }*/

                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };


        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static void saveToSharedPref(Context context, String prefKey, String prefValue){
        SharedPreferences sharedPref = context.getSharedPreferences("file_name", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(prefKey, prefValue);
        editor.apply();
    }

    public static String readFromPreference(Context context, String prefKey, String defaultValue) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("file_name", Context.MODE_PRIVATE);
        return sharedPreferences.getString(prefKey,defaultValue);
    }

    public List<categoryDetail> getData(){
        List<categoryDetail> data = new ArrayList<>();
        String titles[] = {"Bangladesh","India"};

        for (int i =0;i<titles.length;i++){
            categoryDetail current = new categoryDetail();
            current.categoryName = titles[i];
            current.icon = R.drawable.ic_play_arrow;
            data.add(current);
        }
        return data;
    }

    public void getAllCountry(String url) {
        allCountry = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonRootObject = new JSONObject(response.toString());
                    Boolean success = (boolean)jsonRootObject.get("success");
                    if(success) {
                        JSONArray jsonArray = jsonRootObject.optJSONArray("datarows");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int countryId = Integer.parseInt(jsonObject.optString("countryId").toString());
                            String countryName = jsonObject.optString("countryName").toString();
                            Country country = new Country();
                            country.setCounrtyId(countryId);
                            country.setCountyName(countryName);
                            allCountry.add(country);

                            categoryDetail current = new categoryDetail();
                            current.categoryName = countryName;
                            current.icon = R.drawable.ic_play_arrow;
                            data.add(current);
                        }

                        adapter = new listRecyclerAdapter(getActivity(), data);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
                Log.i("ERROR..", error.toString());
            }
        });

        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}
