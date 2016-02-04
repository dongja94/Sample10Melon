package com.example.dongja94.samplemelon;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by dongja94 on 2016-02-04.
 */
public class Songs implements JSONParseHandler {
    ArrayList<Song> song;

    @Override
    public void setData(JSONObject jobject) {
        JSONArray jsong = jobject.optJSONArray("song");
        song = new ArrayList<Song>();

        for (int i = 0; i < jsong.length(); i++) {
            JSONObject js = jsong.optJSONObject(i);
            Song s = new Song();
            s.setData(js);
            song.add(s);
        }
    }
}
