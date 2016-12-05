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

public class PM_2_5 extends AppCompatActivity {

    TextView pmview, timeview, averageview;
    WebView wv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pm_2_5);

        pmview = (TextView) findViewById(R.id.textView8);
        timeview = (TextView) findViewById(R.id.textView9);
        averageview = (TextView) findViewById(R.id.textView11);
        wv3 = (WebView) findViewById(R.id.webView3);

        RequestQueue queue = Volley.newRequestQueue(PM_2_5.this);
        StringRequest request = new StringRequest("https://api.thingspeak.com/channels/189185/fields/3.json?results=10",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

// =================================================================================
    /*GSON格式讀取*/
                        Gson gson = new Gson();
                        Thingspeak data = gson.fromJson(response, Thingspeak.class);
                        //讀取GSON需先建立其呼叫類別
                        String pm_2_5 = data.getFeeds()[(data.getFeeds().length)-1].getfield3();
                        String time = data.getFeeds()[(data.getFeeds().length)-1].getCreated_at();
                        Log.d("pm2.5  " , data.getFeeds()[((data.getFeeds().length)-1)].getfield3());
                        String time2 = time.substring(11,19);
 //最後一筆資料
                        pmview.setText("PM2.5 :  " + pm_2_5);
                        timeview.setText("測量時間:" + time2);

                        Double sum=0.0;
                        for(int i=0 ; i<data.getFeeds().length ; i++) {
                            String pm = data.getFeeds()[i].getfield3();
                           sum += (Double.valueOf(pm));
                        }
                        Double avg = sum / 10.0 ;
                        averageview.setText("平均濃度:" + avg.toString());
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
        wv3.setWebChromeClient(new WebChromeClient());
        wv3.getSettings().setJavaScriptEnabled(true);
//        wv1.getSettings().setUseWideViewPort(true);      //可設定表格大小
//        wv1.getSettings().setLoadWithOverviewMode(true); //可設定表格大小
        wv3.loadUrl("https://thingspeak.com/channels/189185/charts/3?bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=10&title=PM2.5&type=line&width=300&height=250");
    }
}
