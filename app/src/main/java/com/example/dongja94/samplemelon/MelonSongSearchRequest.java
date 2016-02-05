package com.example.dongja94.samplemelon;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by dongja94 on 2016-02-05.
 */
public class MelonSongSearchRequest extends NetworkRequest<Melon> {
    String keyword;
    public MelonSongSearchRequest(String keyword) throws UnsupportedEncodingException {
        this.keyword = URLEncoder.encode(keyword,"utf8");
    }
    private static final String URL_FORMAT = "http://apis.skplanetx.com/melon/songs?count=10&page=1&searchKeyword=%s&version=1";
    @Override
    public URL getURL() throws MalformedURLException {
        String urlText = String.format(URL_FORMAT, keyword);
        return new URL(urlText);
    }

    @Override
    public void setRequestHeader(HttpURLConnection conn) {
        super.setRequestHeader(conn);
        conn.setRequestProperty("Accept","application/json");
        conn.setRequestProperty("appKey", "458a10f5-c07e-34b5-b2bd-4a891e024c2a");
    }

    @Override
    protected Melon parse(InputStream is) throws ParseException {
        Gson gson = new Gson();
        InputStreamReader isr = new InputStreamReader(is);
        try {
            MelonData data = gson.fromJson(isr, MelonData.class);
            return data.melon;
        }catch (JsonSyntaxException | JsonIOException e){
            throw new ParseException(e.getMessage());
        }
    }
}
