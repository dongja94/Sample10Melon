package com.example.dongja94.samplemelon;

import org.json.JSONObject;

/**
 * Created by dongja94 on 2016-02-04.
 */
public class Song implements JSONParseHandler {
    int songId;
    String songName;
    Artists artists;
    String albumName;
    int currentRank;

    @Override
    public void setData(JSONObject jobject) {
        songId = jobject.optInt("songId");
        songName = jobject.optString("songName");
        albumName = jobject.optString("albumName");
        currentRank = jobject.optInt("currentRank");
        JSONObject jartist = jobject.optJSONObject("artists");
        artists = new Artists();
        artists.setData(jartist);
    }

    @Override
    public String toString() {
        return "[" + currentRank + "]" + songName + "\n(" + albumName + ")";
    }
}
