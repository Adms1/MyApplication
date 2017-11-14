package waterworks.lafitnessapp;

import java.util.Calendar;

import waterworks.lafitnessapp.utility.Utility;
import waterworks.lafitnessapp.utility.WW_StaticClass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint("SetJavaScriptEnabled")
public class DetailReportActivity extends Activity {
    TextView mtv_name, mtv_day, mtv_date, mtv_time;
    String time;
    public static String TAG = "AwardTurboActivity";
    String am_pm;
    java.util.Date noteTS;
    Boolean isInternetPresent = false;
    ImageButton ib_back;
    WebView browser;
    String url;
    TextView title, progress_text;
    Thread t;
    String FROM = "";
    Button mBtn_logout;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_PROGRESS);
        FROM = getIntent().getStringExtra("FROM");
        if (FROM.toString().equalsIgnoreCase("ISA")) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(R.layout.activity_detail_report);
        getWindow().setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON);

        isInternetPresent = Utility
                .isNetworkConnected(DetailReportActivity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
        } else {
            url = getIntent().getStringExtra("url");
            Log.i(TAG, "Url = " + url);
            System.out.println("URL : " + url);
            Initialize();
            title.setText("REPORTS!!");
            browser.setWebViewClient(new MyBrowser());
            open();
            preventFullScreen();
        }
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR);
        int min = c.get(Calendar.MINUTE);
        int Day_Name = c.get(Calendar.DAY_OF_WEEK);
        int Date = c.get(Calendar.DATE);
        int Month = c.get(Calendar.MONTH);
        String day_name = null;
        if (Day_Name == 1) {
            day_name = "SUN";
        } else if (Day_Name == 2) {
            day_name = "MON";
        } else if (Day_Name == 3) {
            day_name = "TUES";
        } else if (Day_Name == 4) {
            day_name = "WED";
        } else if (Day_Name == 5) {
            day_name = "THUR";
        } else if (Day_Name == 6) {
            day_name = "FRI";
        } else if (Day_Name == 7) {
            day_name = "SAT";
        }

        t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(10);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateTextView();
                                Calendar cc = Calendar.getInstance();
                                int AM_PM = cc.get(Calendar.AM_PM);

                                if (AM_PM == 0) {
                                    am_pm = "AM";
                                } else {
                                    am_pm = "PM";
                                }
                            }

                            private void updateTextView() {
                                // TODO Auto-generated method stub
                                noteTS = Calendar.getInstance().getTime();

                                time = "hh:mm"; // 12:00
                                mtv_time.setText(DateFormat.format(time, noteTS) + " " + am_pm);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();

        mtv_name.setText(WW_StaticClass.UserName);
        mtv_day.setText(day_name);
        mtv_date.setText(Month + 1 + "/" + Date);

        ib_back.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                t.interrupt();
                finish();
            }
        });
    }

    private void Initialize() {
        // TODO Auto-generated method stub
        View view = findViewById(R.id.toplayout_date_time);
        mBtn_logout = (Button) view.findViewById(R.id.btn_app_logoff);
        title = (TextView) findViewById(R.id.tv_det_rep_title);
        mtv_date = (TextView) findViewById(R.id.tv_app_date);
        mtv_day = (TextView) findViewById(R.id.tv_app_day);
        mtv_name = (TextView) findViewById(R.id.tv_app_inst_name);
        mtv_time = (TextView) findViewById(R.id.tv_app_time);
        browser = (WebView) findViewById(R.id.webview_report);
        ib_back = (ImageButton) findViewById(R.id.btn_back);
        loading = (ProgressBar) findViewById(R.id.loading);
        progress_text = (TextView) findViewById(R.id.progress_text);
    }

    public void open() {
        browser.clearCache(true);
        browser.setInitialScale(1);
        browser.getSettings().setAppCacheEnabled(false);
        browser.getSettings().setCacheMode(WebSettings.LOAD_NORMAL);
        browser.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        browser.getSettings().setAppCacheEnabled(true);
        browser.getSettings().setLoadsImagesAutomatically(true);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.getSettings().setBuiltInZoomControls(true);
        //		     browser.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        browser.getSettings().setLoadWithOverviewMode(true);
        browser.getSettings().setUseWideViewPort(true);
        browser.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        browser.setScrollbarFadingEnabled(false);
        browser.loadUrl(url);

    }

    private class MyBrowser extends WebViewClient {
        ProgressDialog pd;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);

            browser.setWebChromeClient(new WebChromeClient() {
                public void onProgressChanged(WebView view, int newProgress) {
                    //Make the bar disappear after URL is loaded, and changes string to Loading...
                    setTitle("Loading...");
                    loading.setVisibility(View.VISIBLE);
                    progress_text.setVisibility(View.VISIBLE);
                    loading.setProgress(newProgress + 2); //Make the bar disappear after URL is loaded
                    progress_text.setText(String.valueOf(newProgress) + "%");
                    // Return the app name after finish loading
                    if (newProgress == 120 || newProgress == 100) {
                        loading.setVisibility(View.GONE);
                        progress_text.setVisibility(View.GONE);
                    }
                    super.onProgressChanged(view, newProgress);
                }
            });

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);
            if (pd != null) {
                pd.dismiss();
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (pd != null) {
                pd.dismiss();
            }
        }
    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setIcon(getResources().getDrawable(R.drawable.logo));
        builder1.setMessage("Please turn on internet connection and try again.")
                .setTitle("No Internet Connection.")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        t.interrupt();
                        finish();
                    }
                })
                .setPositiveButton("Οk", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });
        return builder1.create();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        isInternetPresent = Utility
                .isNetworkConnected(DetailReportActivity.this);

        mBtn_logout.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        DetailReportActivity.this);

                // set title
                alertDialogBuilder.setTitle("LAFitnessApp");
                alertDialogBuilder.setIcon(getResources().getDrawable(
                        R.drawable.logo));

                // set dialog message
                alertDialogBuilder
                        .setMessage("Are you sure you want to logout ?")
                        .setCancelable(false)
                        .setPositiveButton("Logout",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        WW_StaticClass.InstructorID = "";
                                        finish();

                                        Intent loginIntent = new Intent(
                                                DetailReportActivity.this,
                                                LoginActivity.class);
                                        loginIntent
                                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        loginIntent
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                        startActivity(loginIntent);

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }
        });

    }

    private void preventFullScreen() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if ((keyCode == KeyEvent.KEYCODE_BACK && browser.canGoBack())) {
            browser.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
