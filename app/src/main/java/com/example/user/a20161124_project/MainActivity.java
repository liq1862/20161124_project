package com.example.user.a20161124_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    String[] feeds;
    String[] thingspeak;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    Button btn1;
    Button btn2;
    Button btn3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        WebView wv;
        WebView wv2;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = (TextView) findViewById(R.id.textView);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);
        btn1 =(Button) findViewById(R.id.button1);
        btn2 =(Button) findViewById(R.id.button2);
        btn3 =(Button) findViewById(R.id.button3);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Temp.class);
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this,Humi.class);
                startActivity(intent1);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this,PM_2_5.class);
                startActivity(intent2);
            }
        });

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest request = new StringRequest("https://api.thingspeak.com/channels/176124/feeds.json?api_key=9AK9G8B8BN9GKIK8&results=1&timezone=Asia/Taipei",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

// =================================================================================
    /*GSON格式讀取*/
                        Gson gson = new Gson();
                        Thingspeak data = gson.fromJson(response, Thingspeak.class);
                        //讀取GSON需先建立其呼叫類別

                        String temp = data.getFeeds()[(data.getFeeds().length)-1].getfield1();
                        tv1.setText("現在溫度: " + temp + "度");
//最後一筆資料
//                        Log.d("Time " , data.getFeeds()[((data.getFeeds().length)-1)].getCreated_at());
//                        Log.d("Temp  " , data.getFeeds()[(data.getFeeds().length)-1].getfield1());

                        String humi = data.getFeeds()[(data.getFeeds().length)-1].getfield2();
                        tv2.setText("現在濕度: " + humi);
//                        Log.d("Temp  " , data.getFeeds()[((data.getFeeds().length)-1)].getfield2());

                        String pm_2_5 = data.getFeeds()[(data.getFeeds().length)-1].getfield3();
                        tv3.setText("PM2.5: " + pm_2_5);
//                        Log.d("Temp  " , data.getFeeds()[((data.getFeeds().length)-1)].getfield3());
// ==================================================================================

// ==================================================================================
    /*JSON格式讀取*/
//                        Log.d("NET", response);
//                        try {
//                            String feeds = new JSONObject(response).getString("feeds");
//                            Log.d("NET", feeds);
//                            String temp = new JSONArray (feeds).getJSONObject(0).getString("field1");
//                            Log.d("NET", temp);
//                            String created_at = new JSONArray (feeds).getJSONObject(0).getString("created_at");
//                            String time = created_at.substring(11,19);
//                            Log.d("NET", time);
//                            tv1.setText("現在溫度: " + temp + "度");
//                            tv2.setText("量測時間: " + time);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//  ================================================================================
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
        queue.start();

// =================================================================================
    }
}

