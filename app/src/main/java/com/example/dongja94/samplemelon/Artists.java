package com.example.dongja94.samplemelon;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dongja94 on 2016-02-04.
 */
public class Artists implements JSONParseHandler {
    ArrayList<Artist> artist;

    @Override
    public void setData(JSONObject jobject) {
        JSONArray jarray = jobject.optJSONArray("artist");
        artist = new ArrayList<Artist>();
        for (int i = 0; i < jarray.length(); i++) {
            JSONObject jitem = jarray.optJSONObject(i);
            Artist art = new Artist();
            art.setData(jitem);
            artist.add(art);
        }

    }
}
