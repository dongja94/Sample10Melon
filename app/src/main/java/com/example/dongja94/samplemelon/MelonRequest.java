package com.example.dongja94.samplemelon;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dongja94 on 2016-02-05.
 */
public class MelonRequest extends NetworkRequest<Melon> {
    String urlFormat = "http://apis.skplanetx.com/melon/charts/realtime?count=%s&page=%s&version=1";

    int count;
    int page;
    public MelonRequest() {
        this(1, 10);
    }

    public MelonRequest(int page, int count) {
        this.page = page;
        this.count = count;
    }
    @Override
    public URL getURL() throws MalformedURLException {
        String urlText = String.format(urlFormat, count, page);
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
