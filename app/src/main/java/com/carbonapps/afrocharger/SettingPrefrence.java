package com.carbonapps.afrocharger;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mirago.android.sdk.AdvertView;
import com.mirago.android.sdk.IAdvert;

import java.util.HashMap;

public class SettingPrefrence extends AppCompatActivity implements IAdvert {

    Toolbar toolbar;

    private AdvertView advertView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

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

        setTheme(R.style.Theme_DarkText);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Setting");
        toolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction().replace(R.id.content_frame, new SettingActivity()).commit();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
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
