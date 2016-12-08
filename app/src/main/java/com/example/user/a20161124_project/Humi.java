package com.example.user.a20161124_project;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import com.google.gson.Gson;

import java.nio.charset.MalformedInputException;

public class Humi extends AppCompatActivity {

    TextView humiview, timeview, averageview;
    WebView wv2;
    private static final int msgKey4 = 111;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humi);
        setTitle("濕度詳細資料");
        new TimeThread4().start();

        humiview = (TextView) findViewById(R.id.textView6);
        timeview = (TextView) findViewById(R.id.textView7);
        averageview = (TextView) findViewById(R.id.textView12);
        wv2 = (WebView) findViewById(R.id.webView2);

        RequestQueue queue = Volley.newRequestQueue(Humi.this);
        StringRequest request = new StringRequest("https://api.thingspeak.com/channels/189185/fields/2.json?results=10",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

// =================================================================================
 /*GSON格式讀取*/
                        Gson gson = new Gson();
                        Thingspeak data = gson.fromJson(response, Thingspeak.class);
//讀取GSON需先建立其呼叫類別
                        String humi = data.getFeeds()[(data.getFeeds().length)-1].getfield2();
//                        Log.d("Humi  " , data.getFeeds()[((data.getFeeds().length)-1)].getfield2());
                        String time = data.getFeeds()[(data.getFeeds().length)-1].getCreated_at();
                        String time2 = time.substring(11,19);
 //最後一筆資料
                        humiview.setText("現在濕度: " + humi);
                        timeview.setText("測量時間:" + time2);

                        Double sum=0.0;
                        for(int i=0 ; i<data.getFeeds().length ; i++) {
                            String humidity = data.getFeeds()[i].getfield2();
                            sum += (Double.valueOf(humidity));
                        }
                        Double avg = sum / 10.0 ;
                        averageview.setText("平均濕度:" + avg.toString());
                        Log.d("AVG: ",avg.toString());
// ==================================================================================
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
        queue.start();

// =================================================================================
    /*顯示圖表*/
        //暫時關掉權限(僅可測試用)
//        StrictMode.VmPolicy policy = new StrictMode.VmPolicy.Builder()
//                .detectFileUriExposure()
//                .build();
//        StrictMode.setVmPolicy(policy);
//
        wv2.setWebChromeClient(new WebChromeClient());
        wv2.getSettings().setJavaScriptEnabled(true);
//        wv1.getSettings().setUseWideViewPort(true);      //可設定表格大小
//        wv1.getSettings().setLoadWithOverviewMode(true); //可設定表格大小
        wv2.loadUrl("https://thingspeak.com/channels/189185/charts/2?bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=10&title=Humidity&type=line&width=300&height=250");

    }
    public class TimeThread4 extends Thread {
        @Override
        public void run () {
            do{
                try {
                    Thread.sleep(60000);
                    Message msg = new Message();
                    msg.what = msgKey4;
                    mHandler.sendMessage(msg);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }while(true);
        }
    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == msgKey4) {
                RequestQueue queue = Volley.newRequestQueue(Humi.this);
                StringRequest request = new StringRequest("https://api.thingspeak.com/channels/189185/fields/2.json?results=10",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Gson gson = new Gson();
                                Thingspeak data = gson.fromJson(response, Thingspeak.class);
                                String humi = data.getFeeds()[(data.getFeeds().length)-1].getfield2();
//                                Log.d("Humi  " , data.getFeeds()[((data.getFeeds().length)-1)].getfield2());
                                String time = data.getFeeds()[(data.getFeeds().length)-1].getCreated_at();
                                String time2 = time.substring(11,19);
                                humiview.setText("現在濕度: " + humi);
                                timeview.setText("測量時間:" + time2);
                                Log.d("TEST","set h");

                                Double sum=0.0;
                                for(int i=0 ; i<data.getFeeds().length ; i++) {
                                    String humidity = data.getFeeds()[i].getfield2();
                                    sum += (Double.valueOf(humidity));
                                }
                                Double avg = sum / 10.0 ;
                                averageview.setText("平均濕度:" + avg.toString());
//                                Log.d("AVG: ",avg.toString());
// ==================================================================================
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue.add(request);
                queue.start();
            }
        }
    };

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//
//    }
}