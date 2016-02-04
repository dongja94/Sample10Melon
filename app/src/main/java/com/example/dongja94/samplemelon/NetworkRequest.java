package com.example.dongja94.samplemelon;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by dongja94 on 2016-02-04.
 */
public abstract class NetworkRequest<T> implements Runnable {

    public static final String METHOD_GET = "GET";
    public static final String METHOD_POST = "POST";
    public static final String METHOD_PUT = "PUT";
    public static final String METHOD_DELETE = "DELETE";

    T result;
    public abstract URL getURL() throws MalformedURLException;
    public String getRequestMethod() {
        return METHOD_GET;
    }

    public int getTimeout() {
        return 30000;
    }

    public void setOutput(OutputStream out) {
    }

    public void setRequestHeader(HttpURLConnection conn) {

    }

    public void setConfiguration(HttpURLConnection conn) {

    }

    abstract protected T parse(InputStream is);

    @Override
    public void run() {
        try {
            URL url = getURL();
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            String method = getRequestMethod();
            if (method == METHOD_POST || method == METHOD_PUT) {
                conn.setDoOutput(true);
            }
            conn.setRequestMethod(method);
            setRequestHeader(conn);
            setConfiguration(conn);
            if (conn.getDoOutput()) {
                OutputStream out = conn.getOutputStream();
                setOutput(out);
            }
            conn.setConnectTimeout(getTimeout());
            conn.setReadTimeout(getTimeout());

            int code = conn.getResponseCode();
            if (code >= HttpURLConnection.HTTP_OK && code < HttpURLConnection.HTTP_MULT_CHOICE) {
                InputStream is = conn.getInputStream();
                result = parse(is);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
