package com.carbonapps.afrocharger;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.ArcProgress;
import com.google.firebase.FirebaseApp;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mirago.android.sdk.AdvertView;
import com.mirago.android.sdk.IAdvert;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.media.AudioManager.RINGER_MODE_NORMAL;
import static android.media.AudioManager.RINGER_MODE_SILENT;
import static android.media.AudioManager.RINGER_MODE_VIBRATE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IAdvert {

    Toolbar toolbar;
    ImageView Tools_WiFi, Tools_Rotate, Tools_Brightness, Tools_Bluetooth, Tools_Timeout, Tools_Mode;
    Button PowerSavingMode;
    BluetoothAdapter AdapterForBluetooth;
    CardView CardViewBatteryArc, CardViewTools, batteryDetail, CardViewRate, mCardViewShare;
    Button btn_feedback, btn_ratenow;
    TextView TxtLevel, TxtVoltage, TxtTemperature;
    public BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            TxtTemperature.setText(intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10 + Character.toString((char) 176) + " C");
            TxtVoltage.setText((float) intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) / (float) 1000 + Character.toString((char) 176) + " V");
            TxtLevel.setText(Integer.toString(intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)));
        }
    };
    Intent i;
    AudioManager am;
    RelativeLayout AlertLout;
    TextView mAlertText;
    Integer Issue = 0;
    BroadcastReceiver batteryLevelReceiver;
    AlertDialog.Builder exit_dialog, writesetting_dialog, donotdisterb_dialog;
    private ArcProgress arcProgress;
    private Timer timer;
    private int brightness;
    private int rotate;
    private int timeout;
    private ContentResolver cResolver;
    private Window window;
    Integer Profile;
    private FirebaseAnalytics mFirebaseAnalytics;

    private AdvertView advertView, advertView1;

    private static boolean isAirplaneModeOn(Context context) {

        return Settings.System.getInt(context.getContentResolver(),
                Settings.System.AIRPLANE_MODE_ON, 0) != 0;

    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.app_color_Dark));
        }
        setContentView(R.layout.activity_main);

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
        rel_btn.setMargins(0,5,0,5);
        layout.setLayoutParams(rel_btn);
        layout.addView(advertView);
        //////////////////////////


        ////////////////////
        final float scale1 = this.getResources().getDisplayMetrics().density;
        int height1 = (int) (250 * scale + 0.5f);
        int width1 = (int) (300 * scale + 0.5f);
        HashMap<String, String> advertParams1 = new HashMap<String, String>();
        advertParams1.put("sys", "CarbonApps");
        advertParams1.put("partnernumber", "875435");
        advertParams1.put("mediatype", "banner");
        advertParams1.put("imagewidth", String.valueOf(width1));
        advertParams1.put("imageheight", String.valueOf(height1));
        //Create AdvertView object
        advertView1 = new AdvertView(this);

        //Set targeting data
        advertView1.setTargetingData(advertParams1);

        //Add advert view to layout
        RelativeLayout layout1 = (RelativeLayout) findViewById(R.id.NativeadView);
        RelativeLayout.LayoutParams rel_btn1 = new RelativeLayout.LayoutParams(
                width1, height1);
        rel_btn1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        rel_btn1.setMargins(0,8,0,8);
        rel_btn1.addRule(RelativeLayout.BELOW, R.id.CardViewTools);
        layout1.setLayoutParams(rel_btn1);
        layout1.addView(advertView1);
        /////////////////////////////

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        setupdialog();


        getBaseContext().getApplicationContext().sendBroadcast(new Intent("BatteryReciver"));

        arcProgress = (ArcProgress) findViewById(R.id.arc_progress);
        TxtLevel = (TextView) findViewById(R.id.TxtLevel);
        TxtVoltage = (TextView) findViewById(R.id.TxtVoltage);
        TxtTemperature = (TextView) findViewById(R.id.TxtTemperature);
        Tools_WiFi = (ImageView) findViewById(R.id.tool_wifi);
        Tools_Rotate = (ImageView) findViewById(R.id.tool_rotate);
        Tools_Brightness = (ImageView) findViewById(R.id.tool_brightness);
        Tools_Bluetooth = (ImageView) findViewById(R.id.tool_bluetooth);
        Tools_Timeout = (ImageView) findViewById(R.id.tool_timeout);
        Tools_Mode = (ImageView) findViewById(R.id.tool_mode);
        PowerSavingMode = (Button) findViewById(R.id.PowerSavingMode);
        CardViewBatteryArc = (CardView) findViewById(R.id.CardViewBatteryArc);
        CardViewTools = (CardView) findViewById(R.id.CardViewTools);
        batteryDetail = (CardView) findViewById(R.id.batteryDetail);
        CardViewRate = (CardView) findViewById(R.id.CardViewRate);
        mCardViewShare = (CardView) findViewById(R.id.CardViewShare);
        btn_feedback = (Button) findViewById(R.id.btn_feedbak);
        btn_ratenow = (Button) findViewById(R.id.btn_ratenow);
        AlertLout = (RelativeLayout) findViewById(R.id.AlertLout);
        mAlertText = (TextView) findViewById(R.id.AlertText);

        FirebaseApp.initializeApp(getApplicationContext());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        registerReceiver(this.batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        setColorToCardview();

        cResolver = getContentResolver();
        window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                try {
                    Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                    brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
                    rotate = Settings.System.getInt(cResolver, Settings.System.ACCELEROMETER_ROTATION);
                    timeout = Settings.System.getInt(cResolver, Settings.System.SCREEN_OFF_TIMEOUT);

                    if (timeout > 40000) {
                        setTimeout(3);
                        timeout = 40000;
                    }

                    CheckOnAndOff();

                } catch (Settings.SettingNotFoundException e) {
                    //Throw an error case it couldn't be retrieved
                    Log.e("Error", "Cannot access system brightness");
                    e.printStackTrace();
                }
            } else {
                NotificationManager notificationManager =
                        (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                        && !notificationManager.isNotificationPolicyAccessGranted()) {

                    donotdisterb_dialog.show();
                }
                writesetting_dialog.show();

            }

        } else {
            try {
                Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
                rotate = Settings.System.getInt(cResolver, Settings.System.ACCELEROMETER_ROTATION);
                timeout = Settings.System.getInt(cResolver, Settings.System.SCREEN_OFF_TIMEOUT);

                Log.e("timeout", "" + timeout);

                if (timeout > 40000) {
                    setTimeout(3);
                    timeout = 40000;
                }

                CheckOnAndOff();

            } catch (Settings.SettingNotFoundException e) {
                //Throw an error case it couldn't be retrieved
                Log.e("Error", "Cannot access system brightness");
                e.printStackTrace();
            }
        }

        //refreshAd1();
        //refreshAd2();
        getBatteryPercentage();
        SetClickListner();
        CheckIntentToolsOnOrOff();

    }

    private void setColorToCardview() {

        CardViewBatteryArc.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
        CardViewTools.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
        batteryDetail.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
        CardViewRate.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
        mCardViewShare.setCardBackgroundColor(getApplicationContext().getResources().getColor(R.color.white));
        CardViewBatteryArc.setCardElevation(0);
        CardViewTools.setCardElevation(0);
        batteryDetail.setCardElevation(0);
        CardViewRate.setCardElevation(0);
        mCardViewShare.setCardElevation(0);
    }

    private void setupdialog() {

        exit_dialog = new AlertDialog.Builder(MainActivity.this);
        exit_dialog.setTitle("Are you sure?");
        exit_dialog.setMessage("Do you want to exit from this application?");
        exit_dialog.setNegativeButton("Yes",
                new DialogInterface.OnClickListener() {
                    @SuppressLint("NewApi")
                    public void onClick(DialogInterface dialog, int whichButton) {
                        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                        am.setRingerMode(Profile);
                        setTimeout(3);
                        finish();
                    }
                });

        writesetting_dialog = new AlertDialog.Builder(MainActivity.this);
        writesetting_dialog.setTitle("Important!");
        writesetting_dialog.setCancelable(false);
        writesetting_dialog.setMessage("Need write setting permission to set screen brightness, screen timeout, screen rotation, sound profile.");
        writesetting_dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        intent.setData(Uri.parse("package:" + getPackageName()));
                        startActivity(intent);
                    }
                });

        donotdisterb_dialog = new AlertDialog.Builder(MainActivity.this);
        donotdisterb_dialog.setTitle("Important!");
        donotdisterb_dialog.setCancelable(false);
        donotdisterb_dialog.setMessage("Need do not disturb permisson for fast charging.");
        donotdisterb_dialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                        }

                        startActivity(intent);
                    }
                });


    }

    private void SetClickListner() {
        Tools_WiFi.setOnClickListener(this);
        Tools_Rotate.setOnClickListener(this);
        Tools_Bluetooth.setOnClickListener(this);
        Tools_Brightness.setOnClickListener(this);
        Tools_Mode.setOnClickListener(this);
        Tools_Timeout.setOnClickListener(this);
        batteryDetail.setOnClickListener(this);
        PowerSavingMode.setOnClickListener(this);
        mCardViewShare.setOnClickListener(this);
        btn_feedback.setOnClickListener(this);
        btn_ratenow.setOnClickListener(this);
        AlertLout.setOnClickListener(this);

    }

    private void getBatteryPercentage() {
        batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);
                int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int level = -1;
                if (currentLevel >= 0 && scale > 0) {
                    level = (currentLevel * 100) / scale;
                    Log.e("%", "" + level);
                }

                timer = new Timer();
                final int finalLevel = level;
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (arcProgress.getProgress() == finalLevel) {
                                    arcProgress.setProgress(finalLevel);
                                    arcProgress.setBottomText("");
                                    timer.cancel();
                                } else {
                                    arcProgress.setProgress(arcProgress.getProgress() + 1);
                                    arcProgress.setBottomText("");
                                }


                            }
                        });
                    }
                }, 1000, level);
            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    private void CheckOnAndOff() {

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            Tools_WiFi.setImageResource(R.drawable.tool_wifi_on);
        } else {
            Tools_WiFi.setImageResource(R.drawable.tool_wifi_off);
        }

        if (rotate == 1) {
            Tools_Rotate.setImageResource(R.drawable.tool_rotate_autorotate);
        } else {
            Tools_Rotate.setImageResource(R.drawable.tool_rotate_portiat);
        }

        AdapterForBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (AdapterForBluetooth == null) {
        } else {
            if (AdapterForBluetooth.isEnabled()) {
                Tools_Bluetooth.setImageResource(R.drawable.tool_bluetooth_on);
            } else {
                Tools_Bluetooth.setImageResource(R.drawable.tool_bluetooth_off);
            }
        }


        if (brightness > 20) {
            Tools_Brightness.setImageResource(R.drawable.tool_brightness_on);
        } else {
            Tools_Brightness.setImageResource(R.drawable.tool_brightness_off);
        }


        if (timeout == 10000) {
            Tools_Timeout.setImageResource(R.drawable.tool_timeout_ten);
        } else if (timeout == 20000) {
            Tools_Timeout.setImageResource(R.drawable.tool_timeout_twenty);
        } else if (timeout == 30000) {
            Tools_Timeout.setImageResource(R.drawable.tool_timeout_thirty);
        } else if (timeout == 40000) {
            Tools_Timeout.setImageResource(R.drawable.tool_timeout_fourty);
        } else {
            Tools_Timeout.setImageResource(R.drawable.tool_timeout_fourty);
            setTimeout(3);
            timeout = 40000;
        }

        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        switch (am.getRingerMode()) {
            case RINGER_MODE_SILENT:
                Tools_Mode.setImageResource(R.drawable.tool_profile_silent);
                Profile = RINGER_MODE_SILENT;
                break;
            case RINGER_MODE_VIBRATE:
                Tools_Mode.setImageResource(R.drawable.tool_profile_vibrate);
                Profile = RINGER_MODE_VIBRATE;
                break;
            case RINGER_MODE_NORMAL:
                Tools_Mode.setImageResource(R.drawable.tool_profile_normal);
                Profile = RINGER_MODE_NORMAL;
                break;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        CheckIntentToolsOnOrOff();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(this)) {
                try {
                    Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                    brightness = Settings.System.getInt(cResolver, Settings.System.SCREEN_BRIGHTNESS);
                    rotate = Settings.System.getInt(cResolver, Settings.System.ACCELEROMETER_ROTATION);
                    timeout = Settings.System.getInt(cResolver, Settings.System.SCREEN_OFF_TIMEOUT);

                    if (timeout > 40000) {
                        setTimeout(3);
                        timeout = 40000;
                    }

                    CheckOnAndOff();
                } catch (Settings.SettingNotFoundException e) {
                    Log.e("Error", "Cannot access system brightness");
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.tool_wifi:
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(false);
                    Tools_WiFi.setImageResource(R.drawable.tool_wifi_off);
                } else {
                    wifiManager.setWifiEnabled(true);
                    Tools_WiFi.setImageResource(R.drawable.tool_wifi_on);
                }
                break;

            case R.id.tool_rotate:

                if (rotate == 1) {
                    Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, true ? 1 : 0);
                    Tools_Rotate.setImageResource(R.drawable.tool_rotate_autorotate);
                    rotate = 0;
                } else {
                    Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, true ? 0 : 1);
                    Tools_Rotate.setImageResource(R.drawable.tool_rotate_portiat);
                    rotate = 1;
                }


                break;


            case R.id.tool_mode:

                am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

                switch (am.getRingerMode()) {
                    case RINGER_MODE_SILENT:
                        Tools_Mode.setImageResource(R.drawable.tool_profile_vibrate);
                        am.setRingerMode(RINGER_MODE_VIBRATE);
                        break;
                    case RINGER_MODE_VIBRATE:
                        Tools_Mode.setImageResource(R.drawable.tool_profile_normal);
                        am.setRingerMode(RINGER_MODE_NORMAL);
                        break;
                    case RINGER_MODE_NORMAL:
                        Tools_Mode.setImageResource(R.drawable.tool_profile_silent);
                        am.setRingerMode(RINGER_MODE_SILENT);
                        break;
                }


                break;

            case R.id.tool_bluetooth:

                AdapterForBluetooth = BluetoothAdapter.getDefaultAdapter();
                if (AdapterForBluetooth == null) {
                } else {
                    if (AdapterForBluetooth.isEnabled()) {
                        Tools_Bluetooth.setImageResource(R.drawable.tool_bluetooth_off);
                        AdapterForBluetooth.disable();
                    } else {
                        Tools_Bluetooth.setImageResource(R.drawable.tool_bluetooth_on);
                        AdapterForBluetooth.enable();
                    }
                }
                break;

            case R.id.tool_brightness:

                if (brightness > 20) {
                    Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, 20);
                    WindowManager.LayoutParams layoutpars = window.getAttributes();
                    layoutpars.screenBrightness = 20;
                    window.setAttributes(layoutpars);
                    Tools_Brightness.setImageResource(R.drawable.tool_brightness_off);
                    brightness = 20;
                } else {
                    Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, 254);
                    WindowManager.LayoutParams layoutpars = window.getAttributes();
                    layoutpars.screenBrightness = 254;
                    window.setAttributes(layoutpars);
                    brightness = 254;
                    Tools_Brightness.setImageResource(R.drawable.tool_brightness_on);
                }

                break;


            case R.id.batteryDetail:

                i = new Intent(getApplicationContext(), BatteryDetailActivity.class);
                startActivity(i);

                break;

            case R.id.PowerSavingMode:

                StartPowerSavingMode();

                break;

            case R.id.tool_timeout:

                if (timeout == 10000) {
                    Tools_Timeout.setImageResource(R.drawable.tool_timeout_twenty);
                    setTimeout(1);
                    timeout = 20000;
                } else if (timeout == 20000) {
                    Tools_Timeout.setImageResource(R.drawable.tool_timeout_thirty);
                    setTimeout(2);
                    timeout = 30000;
                } else if (timeout == 30000) {
                    Tools_Timeout.setImageResource(R.drawable.tool_timeout_fourty);
                    setTimeout(3);
                    timeout = 40000;
                } else if (timeout == 40000) {
                    Tools_Timeout.setImageResource(R.drawable.tool_timeout_ten);
                    setTimeout(0);
                    timeout = 10000;
                } else {
                    Tools_Timeout.setImageResource(R.drawable.tool_timeout_fourty);
                    setTimeout(3);
                    timeout = 40000;
                }

                break;

            case R.id.CardViewShare:

                i = new Intent();
                i.setAction(Intent.ACTION_SEND);
                i.setType("text/plain");
                final String text = "Check out "
                        + getResources().getString(R.string.app_name)
                        + ", the free app for save your battery with " + getResources().getString(R.string.app_name) + ". https://play.google.com/store/apps/details?id="
                        + getPackageName();
                i.putExtra(Intent.EXTRA_TEXT, text);
                Intent sender = Intent.createChooser(i, "Share " + getResources().getString(R.string.app_name));
                startActivity(sender);

                break;

            case R.id.btn_feedbak:

                DisplayMetrics displaymetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int height = displaymetrics.heightPixels;
                int width = displaymetrics.widthPixels;

                PackageManager manager = getApplicationContext()
                        .getPackageManager();
                PackageInfo info = null;
                try {
                    info = manager.getPackageInfo(getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                String version = info.versionName;

                i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.developer_email)});
                i.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name) + "Version =" + version);
                i.putExtra(Intent.EXTRA_TEXT,
                        "\n" + " Device :" + getDeviceName() +
                                "\n" + " SystemVersion:" + Build.VERSION.SDK_INT +
                                "\n" + " Display Height  :" + height + "px" +
                                "\n" + " Display Width  :" + width + "px" +
                                "\n\n" + " Please write your problem to us we will try our best to solve it .." +
                                "\n");

                startActivity(Intent.createChooser(i, "Send Email"));

                break;

            case R.id.btn_ratenow:

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri
                            .parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }

                break;

            case R.id.AlertLout:
                i = new Intent(getApplicationContext(), CloseAllTools.class);
                i.putExtra("SetValue", 0);
                startActivity(i);
                break;


        }

    }

    public void CheckIntentToolsOnOrOff() {

        Issue = 0;

        if (isMobileDataEnabled()) {
            Issue = Issue + 1;
        }

        LocationManager ManagerForLocation = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Boolean statusOfLocation = ManagerForLocation.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (statusOfLocation) {
            Issue = Issue + 1;
        }

        if (!isAirplaneModeOn(getApplicationContext())) {
            Issue = Issue + 1;
        }

        mAlertText.setText(String.valueOf(Issue));

        if (Issue == 0) {
            AlertLout.setVisibility(View.GONE);
        }
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

    public void StartPowerSavingMode() {
        List<ApplicationInfo> packages;
        PackageManager pm;
        pm = getPackageManager();
        packages = pm.getInstalledApplications(0);

        ActivityManager mActivityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        //  String myPackage = getApplicationContext().getPackageName();
        for (ApplicationInfo packageInfo : packages) {
            Log.e("pakages", packages + "");
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) continue;
            //if (packageInfo.packageName.equals(myPackage)) continue;
            mActivityManager.killBackgroundProcesses(packageInfo.packageName);
        }

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(false);
        Tools_WiFi.setImageResource(R.drawable.tool_wifi_off);

        AdapterForBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (AdapterForBluetooth == null) {
        } else {
            if (AdapterForBluetooth.isEnabled()) {
                AdapterForBluetooth.disable();
                Tools_Bluetooth.setImageResource(R.drawable.tool_bluetooth_off);
            }
        }


        if (brightness > 20) {
            Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, 20);
            WindowManager.LayoutParams layoutpars = window.getAttributes();
            layoutpars.screenBrightness = 20;
            window.setAttributes(layoutpars);
            Tools_Brightness.setImageResource(R.drawable.tool_brightness_off);
            brightness = 20;
        }

        Settings.System.putInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0); //0 means off, 1 means on
        Tools_Rotate.setImageResource(R.drawable.tool_rotate_portiat);

        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setRingerMode(RINGER_MODE_SILENT);
        Tools_Mode.setImageResource(R.drawable.tool_profile_silent);

        setTimeout(0);
        timeout = 10000;
        Tools_Timeout.setImageResource(R.drawable.tool_timeout_ten);

        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);


        PowerSavingMode.startAnimation(fadeInAnimation);
        fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                PowerSavingMode.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;

            case R.id.Setting:
                i = new Intent(getApplicationContext(), SettingPrefrence.class);
                startActivity(i);
                break;


            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    private void setTimeout(int screenOffTimeout) {
        int time;
        switch (screenOffTimeout) {
            case 0:
                time = 10000;
                break;
            case 1:
                time = 20000;
                break;
            case 2:
                time = 30000;
                break;
            case 3:
                time = 40000;
                break;
            default:
                time = -1;
        }
        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, time);
    }


    @Override
    public void onBackPressed() {
        exit_dialog.show();
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
        advertView1.startEngine(true, 30, true);
    }

    @Override
    protected void onStop() {
        advertView.stopEngine();
        advertView1.stopEngine();
        super.onStop();
    }

}
