package com.carbonapps.afrocharger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.mirago.android.sdk.AdvertView;
import com.mirago.android.sdk.IAdvert;

import java.util.HashMap;


public class BatteryDetailActivity extends AppCompatActivity implements IAdvert {

    ListView detailList;
    ListAdepter listAdepter;
    Toolbar toolbar;
    String[] detail_name = {"Temperature", "Voltage", "Level", "Technology", "Health"};
    String[] detail_value = new String[6];
    Integer[] detailImage = {R.drawable.temperature, R.drawable.voltage, R.drawable.battery_level, R.drawable.technology, R.drawable.battery_health};


    private AdvertView advertView, advertView1;

    public BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            detail_value[0] = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10 + Character.toString((char) 176) + " C";
            detail_value[1] = (float) intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) / (float) 1000 + Character.toString((char) 176) + " V";
            detail_value[2] = Integer.toString(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0));
            detail_value[3] = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
            if (detail_value[3].equalsIgnoreCase("")) {
                detail_value[3] = "-";
            }

            int battery_helth = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);

            switch (battery_helth) {
                case 1:
                    detail_value[4] = "UNKNOWN";
                    break;
                case 2:
                    detail_value[4] = "GOOD";
                    break;
                case 3:
                    detail_value[4] = "OVERHEAT";
                    break;
                case 4:
                    detail_value[4] = "DEAD";
                    break;
                case 5:
                    detail_value[4] = "OVER_VOLTAGE";
                    break;
                case 6:
                    detail_value[4] = "UNSPECIFIED_FAILURE";
                    break;
                case 7:
                    detail_value[4] = "COLD";
                    break;

            }

            listAdepter = new ListAdepter(BatteryDetailActivity.this, detail_name, detail_value, detailImage);
            detailList.setAdapter(listAdepter);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery_detail);

        //////////////////////////////////////
        final float scale = this.getResources().getDisplayMetrics().density;
        int height = (int) (50 * scale + 0.5f);
        int width = (int) (320 * scale + 0.5f);
        HashMap<String, String> advertParams = new HashMap<String, String>();
        advertParams.put("sys", "CarbonApps");
        advertParams.put("partnernumber", "875435");
        advertParams.put("mediatype", "banner");
        advertParams.put("imagewidth", String.valueOf(width));
        advertParams.put("imageheight", String.valueOf(height));

        //Create AdvertView object
        advertView = new AdvertView(this);

        //Set targeting data
        advertView.setTargetingData(advertParams);

        //Add advert view to layout
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.adView);


        RelativeLayout.LayoutParams rel_btn = new RelativeLayout.LayoutParams(
                width, height);
        rel_btn.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rel_btn.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layout.setLayoutParams(rel_btn);
        layout.addView(advertView);
        //////////////////////////

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Battery information");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        detailList = (ListView) findViewById(R.id.detailList);
        registerReceiver(this.batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void jsAdsFeedbackDebug(String s) {

    }

    @Override
    public void jsAdsFeedbackClick() {

    }

    @Override
    public void jsAdsFeedbackImpression(int i) {

    }

    @Override
    public void jsAdsFeedbackError(String s) {

    }

    @Override
    public void jsAdsFeedbackAdverts(String s) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        advertView.startEngine(true, 30, true);
    }

    @Override
    protected void onStop() {
        advertView.stopEngine();
        super.onStop();
    }
}

