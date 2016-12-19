package com.example.user.a20161124_project;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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

import java.util.Date;

import pl.pawelkleczkowski.customgauge.CustomGauge;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;
import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {
    String[] feeds;
    String[] thingspeak;
    TextView tempview,humiview,pmview;
    Button totemp,tohumi,topm,torefresh;
    private static final int msgKey1 = 111;
    CustomGauge cg_temp;
    CustomGauge cg_humi;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViews();
        setTitle("綜合資訊");

        ReadData();

        handler.post(manyinfo);
//  =========================================================================
/*Button設定*/
        totemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Temp.class);
                startActivity(intent);
            }
        });
        tohumi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this,Humi.class);
                startActivity(intent1);
            }
        });
        topm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this,PM_2_5.class);
                startActivity(intent2);
            }
        });
        torefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReadData();
            }
        });


    }
//  ===================================================================
    protected void findViews(){
        tempview = (TextView) findViewById(R.id.textView);
        humiview = (TextView) findViewById(R.id.textView2);
        pmview = (TextView) findViewById(R.id.textView3);
        totemp =(Button) findViewById(R.id.button1);
        tohumi =(Button) findViewById(R.id.button2);
        topm =(Button) findViewById(R.id.button3);
        torefresh =(Button) findViewById(R.id.button4);
        cg_temp = (CustomGauge) findViewById(R.id.gauge2);
        cg_humi = (CustomGauge) findViewById(R.id.gauge3);
    }

    Runnable manyinfo = new Runnable() {
        @Override
        public void run() {
            ReadData();
            handler.postDelayed(this, 60000);
//            Log.d("handler","1111111");
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(manyinfo);
    }

    public void ReadData(){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest request = new StringRequest("https://api.thingspeak.com/channels/189185/feeds.json?results=1",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
// =================================================================================
//    /*GSON格式讀取*/
                        Gson gson = new Gson();
                        Thingspeak data = gson.fromJson(response, Thingspeak.class);
                        //讀取GSON需先建立其呼叫類別

                        String temp = data.getFeeds()[(data.getFeeds().length)-1].getfield1();
                        tempview.setText("現在溫度: " + temp + "度");

                        float temp2 =Float.parseFloat(temp);
                        temp2=temp2*10;
                        cg_temp.setValue((int)temp2);
//                        Log.d("Temp  " , data.getFeeds()[(data.getFeeds().length)-1].getfield1());
//最後一筆資料
                        String humi = data.getFeeds()[(data.getFeeds().length)-1].getfield2();
                        humiview.setText("現在濕度: " + humi);
                        float temp3 =Float.parseFloat(humi);
                        temp3=temp3*10;
                        cg_humi.setValue((int)temp3);
                        cg_humi.setPointStartColor(R.color.md_blue_800);
//                        Log.d("Humi  " , data.getFeeds()[((data.getFeeds().length)-1)].getfield2());

                        String pm_2_5 = data.getFeeds()[(data.getFeeds().length)-1].getfield3();
                        pmview.setText("PM2.5: " + pm_2_5);

//                        Log.d("PM2.5  " , data.getFeeds()[((data.getFeeds().length)-1)].getfield3());
// ==================================================================================
//    /*JSON格式讀取*/
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
    }
}


