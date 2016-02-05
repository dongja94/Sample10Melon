package com.example.dongja94.samplemelon;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dongja94 on 2016-02-04.
 */
public class Songs implements JSONParseHandler {
    @com.begentgroup.xmlparser.SerializedName("song")
    @com.google.gson.annotations.SerializedName("song")
    ArrayList<Song> songlist;

    @Override
    public void setData(JSONObject jobject) {
        JSONArray jsong = jobject.optJSONArray("songlist");
        songlist = new ArrayList<Song>();

        for (int i = 0; i < jsong.length(); i++) {
            JSONObject js = jsong.optJSONObject(i);
            Song s = new Song();
            s.setData(js);
            songlist.add(s);
        }
    }
}
