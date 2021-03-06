package com.example.dongja94.samplemelon;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.begentgroup.xmlparser.XMLParser;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    EditText keywordView;
    ArrayAdapter<Song> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        keywordView = (EditText) findViewById(R.id.edit_keyword);
        listView = (ListView) findViewById(R.id.listView);
        mAdapter = new ArrayAdapter<Song>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(mAdapter);

        Button btn = (Button) findViewById(R.id.btn_json_melon);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new MyMelonXMLTask().execute();
//                MelonRequest request = new MelonRequest(2, 10);
//                NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<Melon>() {
//                    @Override
//                    public void onSuccess(NetworkRequest<Melon> request, Melon result) {
//                        for (Song s : result.songs.songlist) {
//                            mAdapter.add(s);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(NetworkRequest<Melon> request, int errorCode, int responseCode, String message, Throwable excepton) {
//                        Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
//                    }
//                });

                String keyword = keywordView.getText().toString();
                if (!TextUtils.isEmpty(keyword)) {
                    try {
                        MelonSongSearchRequest request = new MelonSongSearchRequest(keyword);
                        request.setTag(MainActivity.this);
                        NetworkManager.getInstance().getNetworkData(request, new NetworkManager.OnResultListener<Melon>() {
                            @Override
                            public void onSuccess(NetworkRequest<Melon> request, Melon result) {
                                for (Song s : result.songs.songlist) {
                                    mAdapter.add(s);
                                }
                            }

                            @Override
                            public void onFailure(NetworkRequest<Melon> request, int errorCode, int responseCode, String message, Throwable excepton) {
                                Toast.makeText(MainActivity.this, "Failure", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetworkManager.getInstance().cancelAll(this);
    }

    String urlFormat = "http://apis.skplanetx.com/melon/charts/realtime?count=%s&page=%s&version=1";

    class MyMelonXMLTask extends AsyncTask<String, Integer, Melon> {
        @Override
        protected Melon doInBackground(String... params) {
            String urlText = String.format(urlFormat, 10, 1);
            try {
                URL url = new URL(urlText);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Accept", "application/xml");
                conn.setRequestProperty("appKey", "458a10f5-c07e-34b5-b2bd-4a891e024c2a");
                int code = conn.getResponseCode();
                if (code >= HttpURLConnection.HTTP_OK && code < HttpURLConnection.HTTP_MULT_CHOICE) {

                    InputStream is = conn.getInputStream();
                    XMLParser parser = new XMLParser();
                    Melon melon = parser.fromXml(is, "melon", Melon.class);
                    return melon;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Melon melon) {
            super.onPostExecute(melon);
            if (melon != null) {
                for (Song s : melon.songs.songlist) {
                    mAdapter.add(s);
                }
            } else {
                Toast.makeText(MainActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class MyMelonJsonTask extends AsyncTask<String, Integer, Melon> {
        @Override
        protected Melon doInBackground(String... params) {
            String urlText = String.format(urlFormat, 10, 1);
            try {
                URL url = new URL(urlText);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("appKey", "458a10f5-c07e-34b5-b2bd-4a891e024c2a");
                int code = conn.getResponseCode();
                if (code >= HttpURLConnection.HTTP_OK && code < HttpURLConnection.HTTP_MULT_CHOICE) {
//                    StringBuilder sb = new StringBuilder();
//                    InputStream is = conn.getInputStream();
//                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
//                    String line;
//                    while((line=br.readLine()) != null) {
//                        sb.append(line).append("\n\r");
//                    }
//                    String text = sb.toString();
//
//                    try {
//                        JSONObject jobject = new JSONObject(text);
//                        MelonData data = new MelonData();
//                        data.setData(jobject);
//                        return data.melon;
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

                    Gson gson = new Gson();
                    InputStream is = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    MelonData data = gson.fromJson(isr, MelonData.class);
                    return data.melon;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Melon melon) {
            super.onPostExecute(melon);
            if (melon != null) {
                for (Song s : melon.songs.songlist) {
                    mAdapter.add(s);
                }
            } else {
                Toast.makeText(MainActivity.this, "Error!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
