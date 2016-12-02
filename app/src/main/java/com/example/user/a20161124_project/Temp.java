package com.example.user.a20161124_project;

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

public class Temp extends AppCompatActivity {

    TextView tempview,timeview,averageview;
    WebView wv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        tempview = (TextView) findViewById(R.id.textView4);
        timeview = (TextView) findViewById(R.id.textView5);
        averageview = (TextView) findViewById(R.id.textView10);
        wv1 = (WebView) findViewById(R.id.webView1);

        RequestQueue queue = Volley.newRequestQueue(Temp.this);
        StringRequest request = new StringRequest("https://api.thingspeak.com/channels/176124/fields/1.json?api_key=9AK9G8B8BN9GKIK8&results=10&timezone=Asia/Taipei",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

// =================================================================================
    /*GSON格式讀取*/
                        Gson gson = new Gson();
                        Thingspeak data = gson.fromJson(response, Thingspeak.class);
                        //讀取GSON需先建立其呼叫類別
                        String temp = data.getFeeds()[(data.getFeeds().length)-1].getfield1();
                        String time = data.getFeeds()[(data.getFeeds().length)-1].getCreated_at();
                        Log.d("Temp  " , data.getFeeds()[(data.getFeeds().length)-1].getfield1());
                        String time2 = time.substring(11,19);
                        //最後一筆資料
                        tempview.setText("現在溫度: " + temp + "度");
                        timeview.setText("測量時間:" + time2);
                        //計算平均
                        Double sum=0.0;
                        for(int i=0 ; i<data.getFeeds().length ; i++) {
                            String temputer = data.getFeeds()[i].getfield1();
                            sum += (Double.valueOf(temputer));
                        }
                        Double avg = sum / 10.0 ;
                        averageview.setText("平均溫度:" + avg.toString());
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
        wv1.setWebChromeClient(new WebChromeClient());
        wv1.getSettings().setJavaScriptEnabled(true);
//        wv1.getSettings().setUseWideViewPort(true);      //可設定表格大小
//        wv1.getSettings().setLoadWithOverviewMode(true); //可設定表格大小
        wv1.loadUrl("https://thingspeak.com/channels/176124/charts/1?api_key=9AK9G8B8BN9GKIK8&bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=10&title=Temperature&type=line&width=300&height=250");
//  ===================================================================================
    }
}
