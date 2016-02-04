package com.example.dongja94.samplemelon;

import org.json.JSONObject;

/**
 * Created by dongja94 on 2016-02-04.
 */
public class Artist implements JSONParseHandler{
    int artistId;
    String artistName;

    @Override
    public void setData(JSONObject jobject) {
        artistId = jobject.optInt("artistId");
        artistName = jobject.optString("artistName");
    }
}
