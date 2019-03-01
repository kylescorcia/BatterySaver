package com.carbonapps.afrocharger;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mirago.android.sdk.AdvertView;
import com.mirago.android.sdk.IAdvert;

import java.lang.reflect.Method;
import java.util.HashMap;

public class CloseAllTools extends AppCompatActivity implements View.OnClickListener, IAdvert {

    CardView LocationCardView, AroplaneCardView, MoblieDataCardView;
    Toolbar toolbar;
    Button LocationTurnOff, AroplaneTurnOn, MoblieDataTurnOff;
    LocationManager ManagerForLocation;
    Boolean statusOfLocation;
    TextView txtNoMoreIssue;
    Integer SetValue;
    RelativeLayout loutIssues;


    private AdvertView advertView, advertView1;

    TextView TxtTitleLocation, txtDicLocation, TxtTitleAroplane, txtDicAroplane, TxtTitleMobileData, txtDicMobileData;

    private static boolean isAirplaneModeOn(Context context) {

        return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        SetValue = getIntent().getIntExtra("SetValue", 0);
        setContentView(R.layout.activity_close_all_tools);

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
        if (SetValue == 0) {
            getSupportActionBar().setTitle("Battery Saver");
        } else {
            getSupportActionBar().setTitle("Battery Charger");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LocationCardView = (CardView) findViewById(R.id.LocationCardView);
        AroplaneCardView = (CardView) findViewById(R.id.AroplaneCardView);
        MoblieDataCardView = (CardView) findViewById(R.id.MoblieDataCardView);
        LocationTurnOff = (Button) findViewById(R.id.LocationTurnOff);
        AroplaneTurnOn = (Button) findViewById(R.id.AroplaneTurnOn);
        MoblieDataTurnOff = (Button) findViewById(R.id.MoblieDataTurnOff);
        txtNoMoreIssue = (TextView) findViewById(R.id.txtNoMoreIssue);
        TxtTitleLocation = (TextView) findViewById(R.id.TxtTitleLocation);
        txtDicLocation = (TextView) findViewById(R.id.txtDicLocation);
        TxtTitleAroplane = (TextView) findViewById(R.id.TxtTitleAroplane);
        txtDicAroplane = (TextView) findViewById(R.id.txtDicAroplane);
        TxtTitleMobileData = (TextView) findViewById(R.id.TxtTitleMobileData);
        txtDicMobileData = (TextView) findViewById(R.id.txtDicMobileData);
        loutIssues = (RelativeLayout) findViewById(R.id.loutIssues);
        LocationCardView.setOnClickListener(this);
        AroplaneCardView.setOnClickListener(this);
        MoblieDataCardView.setOnClickListener(this);
        LocationTurnOff.setOnClickListener(this);
        AroplaneTurnOn.setOnClickListener(this);
        MoblieDataTurnOff.setOnClickListener(this);


        SetCarViewColor();

        CheckWhatOn();


        if (SetValue == 0) {
            TxtTitleLocation.setText("2x Battery Saver");
            txtDicLocation.setText("Disable location service and your battery will save 2x");
            TxtTitleAroplane.setText("3x Battery Saver");
            txtDicAroplane.setText("Enable airplane mode and your battery will save 3x");
            TxtTitleMobileData.setText("2x Battery Saver");
            txtDicMobileData.setText("Disable mobile data and your battery will save 2x");
        } else {
            TxtTitleLocation.setText("2x Fast Charger");
            txtDicLocation.setText("Disable location service and your battery will charge 2x faster");
            TxtTitleAroplane.setText("3x Fast Charger");
            txtDicAroplane.setText("Enable airplane mode and your battery will charge 3x faster");
            TxtTitleMobileData.setText("2x Fast Charger");
            txtDicMobileData.setText("Disable mobile data and your battery will charge 2x faster");
        }


    }

    private void SetCarViewColor() {

        LocationCardView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
        AroplaneCardView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
        MoblieDataCardView.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
        LocationCardView.setCardElevation(0);
        AroplaneCardView.setCardElevation(0);
        MoblieDataCardView.setCardElevation(0);

    }

    private void CheckWhatOn() {

        if (!isMobileDataEnabled()) {
            MoblieDataCardView.setVisibility(View.GONE);
        }

        ManagerForLocation = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        statusOfLocation = ManagerForLocation.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!statusOfLocation) {
            LocationCardView.setVisibility(View.GONE);
        }

        if (isAirplaneModeOn(getApplicationContext())) {
            AroplaneCardView.setVisibility(View.GONE);
        }

        if (LocationCardView.getVisibility() == View.GONE && AroplaneCardView.getVisibility() == View.GONE && MoblieDataCardView.getVisibility() == View.GONE) {
            txtNoMoreIssue.setVisibility(View.VISIBLE);
            loutIssues.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.LocationCardView:

                SetUpLocationIntent();

                break;

            case R.id.AroplaneCardView:

                SetUpAroplaneIntent();

                break;

            case R.id.MoblieDataCardView:

                SetUpMobileDataIntent();

                break;

            case R.id.LocationTurnOff:

                SetUpLocationIntent();

                break;

            case R.id.AroplaneTurnOn:

                SetUpAroplaneIntent();

                break;

            case R.id.MoblieDataTurnOff:

                SetUpMobileDataIntent();

                break;
        }
    }

    private void SetUpMobileDataIntent() {

        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    private void SetUpAroplaneIntent() {
        Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
        startActivity(intent);
    }

    private void SetUpLocationIntent() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    public Boolean isMobileDataEnabled() {
        Object connectivityService = getSystemService(Context.CONNECTIVITY_SERVICE);
        ConnectivityManager cm = (ConnectivityManager) connectivityService;

        try {
            Class<?> c = Class.forName(cm.getClass().getName());
            Method m = c.getDeclaredMethod("getMobileDataEnabled");
            m.setAccessible(true);
            return (Boolean) m.invoke(cm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        CheckWhatOn();
    }

    @Override
    public void onPause() {
        super.onPause();
        CheckWhatOn();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;


            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
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



