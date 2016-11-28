package com.example.user.a20161124_project;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {
    String[] feeds;
    String[] thingspeak;
    TextView tv1;
    TextView tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WebView wv;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest request = new StringRequest("https://api.thingspeak.com/channels/176124/fields/1.json?results=1&api_key=9AK9G8B8BN9GKIK8&timezone=Asia/Taipei",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d("NET", response);
                        try {
                            String feeds = new JSONObject(response).getString("feeds");
                            Log.d("NET", feeds);
                            String temp = new JSONArray (feeds).getJSONObject(0).getString("field1");
                            Log.d("NET", temp);
                            String created_at = new JSONArray (feeds).getJSONObject(0).getString("created_at");
//                            String[] time = created_at.split("T");
//                            Log.d("NET", time[1]);
                            String time = created_at.substring(11,19);
                            Log.d("NET", time);
                            tv1.setText("現在溫度: " + temp + "度");
                            tv2.setText("量測時間: " + time);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
        queue.start();
        //暫時關掉權限(僅可測試用),
        StrictMode.VmPolicy policy = new StrictMode.VmPolicy.Builder()
                .detectFileUriExposure()
                .build();
        StrictMode.setVmPolicy(policy);

        wv = (WebView) findViewById(R.id.webView);
        wv.setWebChromeClient(new WebChromeClient());
        wv.getSettings().setJavaScriptEnabled(true);
        wv.loadUrl("https://thingspeak.com/channels/176124/charts/1?api_key=9AK9G8B8BN9GKIK8&bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=60&title=Temperature&type=line&width=1& height=1");
    }
}

