package com.google.android.exoplayer.demo.service;

import com.google.android.exoplayer.demo.Model.Country;
import com.google.android.exoplayer.demo.R;
import com.google.android.exoplayer.demo.materialdesignone.categoryDetail;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by raj on 5/24/16.
 */
public class CountryParse {
    private Boolean success;
    private JSONObject jsonRootObject;
    //private List<categoryDetail> data;

    public List<categoryDetail> getData(String response, List<categoryDetail> data){
        try {
            jsonRootObject = new JSONObject(response);
            success = (boolean)jsonRootObject.get("success");
            if(success) {
                JSONArray jsonArray = jsonRootObject.optJSONArray("datarows");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int countryId = Integer.parseInt(jsonObject.optString("countryId").toString());
                    String countryName = jsonObject.optString("countryName").toString();
                    Country country = new Country();
                    country.setCounrtyId(countryId);
                    country.setCountyName(countryName);
                   // allCountry.add(country);

                    categoryDetail current = new categoryDetail();
                    current.categoryName = countryName;
                    current.icon = R.drawable.ic_play_arrow;
                    data.add(current);
                }

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }
}
