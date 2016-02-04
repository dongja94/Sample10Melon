package com.example.dongja94.samplemelon;

import org.json.JSONObject;

/**
 * Created by dongja94 on 2016-02-04.
 */
public class Melon implements JSONParseHandler {
    int menuId;
    int count;
    int page;
    int totalPages;
    Songs songs;

    @Override
    public void setData(JSONObject jobject) {
        menuId = jobject.optInt("menuId");
        count = jobject.optInt("count");
        page = jobject.optInt("page");
        totalPages = jobject.optInt("totalPages");
        JSONObject jsongs = jobject.optJSONObject("songs");
        songs = new Songs();
        songs.setData(jsongs);
    }
}
