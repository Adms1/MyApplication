package waterworks.lafitnessapp;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


import waterworks.lafitnessapp.model.AllInstructorItems;
import waterworks.lafitnessapp.utility.SingleOptionAlertWithoutTitle;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TodaysScheduleActivity extends Activity {
    private static final String TAG = "Todays Schedule";
    private DrawerLayout mDrawerLayout;
    static String InstrIschlAry = "";
    int wu_avail = 2;
    public static int positions = 0;

    boolean isInternetPrecent = false, getinstructor = false,
            other_problem = false, server_response = false,
            connectionout = false, getlevel = false, getschedule = false,
            getISA = false, ISAFlag = false;
    ;
    ActionBar actionBar;
    ListView lv_filter_instructor;
    ActionBarDrawerToggle mDrawerToggle;
    public ArrayList<String> UserId, UserName;
    private ArrayList<AllInstructorItems> navDrawerItems_main;
    private AllInstructorTodays adapter_main;
    public ImageView drawerImageView, isa_main;
    TextView actionBarTitleview;
    LinearLayout actionBarLayout;
    Fragment fragment;
    String currentDateandTime;
    public String inst_name, inst_id;
    int viewpos = 0;
    Timer t1;
    private RelativeLayout ll_filter;
    Button btn_search;
    public static TextView tv_scheduletime, tv_instructor_name, tv_lessonname;
    public static ArrayList<String> ClickPos = new ArrayList<String>();
    public static ArrayList<String> Instroctorname = new ArrayList<String>();
    public static ArrayList<String> Instroctorid = new ArrayList<String>();
    public static boolean searchClick = false;
    public static Button btn_show_all;
    LinearLayout llbody;
    //Clocked in
    public String token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_schedule);
        //				Toast.makeText(getApplicationContext(), "We are at todays Schedule", Toast.LENGTH_SHORT).)
        isInternetPrecent = Utility.isNetworkConnected(TodaysScheduleActivity.this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (WW_StaticClass.CLOCKED_TODAY_INSERT == true) {
            token = WW_StaticClass.UserToken_Extra;
        } else {
            token = WW_StaticClass.UserToken;
        }

        if (isInternetPrecent) {
            inst_id = WW_StaticClass.InstructorID;
            inst_name = WW_StaticClass.UserName;

            Initialization();
        } else {
            onDetectNetworkState().show();
        }


    }

    @SuppressLint("NewApi")
    private void Initialization() {
        // TODO Auto-generated method stub
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        actionBarLayout = (LinearLayout) getLayoutInflater().inflate(
                R.layout.todaysschedule_actionbar, null);
        actionBarTitleview = (TextView) actionBarLayout
                .findViewById(R.id.actionbar_titleview);
        tv_instructor_name = (TextView) actionBarLayout
                .findViewById(R.id.tv_instructorname);
        btn_show_all = (Button) actionBarLayout
                .findViewById(R.id.btn_show_all);
        actionBarTitleview.setText("Today's Schedule");
        ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT, Gravity.LEFT);
        drawerImageView = (ImageView) actionBarLayout
                .findViewById(R.id.drawer_imageview);
        drawerImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDrawerLayout.isDrawerOpen(ll_filter)) {
                    mDrawerLayout.closeDrawer(ll_filter);
                } else {
                    mDrawerLayout.openDrawer(ll_filter);
                }
            }
        });

        actionBar.setCustomView(actionBarLayout, params);
        ll_filter = (RelativeLayout) findViewById(R.id.ll_filter);
        lv_filter_instructor = (ListView) findViewById(R.id.lv_filter_instructor);
        lv_filter_instructor.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                viewpos = position;
                displayView_Main(position);
                inst_id = UserId.get(position);
                inst_name = UserName.get(position);
                if (mDrawerLayout.isDrawerOpen(ll_filter)) {
                    mDrawerLayout.closeDrawers();
                }
                Instroctorid.clear();
                Instroctorid.add(inst_id);

//						if(Instroctorid.size()>0){
//							new GetAttendance().execute();
//						}
            }
        });
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer1, R.string.app_name, R.string.app_name) {
            @SuppressWarnings("deprecation")
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset == 0
                        && getActionBar().getNavigationMode() == ActionBar.NAVIGATION_MODE_STANDARD) {
                    // drawer closed
                    getActionBar().setNavigationMode(
                            ActionBar.NAVIGATION_MODE_STANDARD);
                    invalidateOptionsMenu();
                    drawerImageView.setImageDrawable(getResources()
                            .getDrawable(R.drawable.ic_drawer1));
                } else if (slideOffset != 0
                        && getActionBar().getNavigationMode() == ActionBar.NAVIGATION_MODE_STANDARD) {
                    // started opening
                    getActionBar().setNavigationMode(
                            ActionBar.NAVIGATION_MODE_STANDARD);
                    invalidateOptionsMenu();
                    drawerImageView.setImageDrawable(getResources()
                            .getDrawable(R.drawable.ic_action_back));
                }
                super.onDrawerSlide(drawerView, slideOffset);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        navDrawerItems_main = new ArrayList<AllInstructorItems>();
        SetRefreshView();

        if (!isInternetPrecent) {
            onDetectNetworkState().show();
        } else {
            new GetAllInstructor().execute();
        }

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
        isInternetPrecent = Utility.isNetworkConnected(TodaysScheduleActivity.this);
        if (!isInternetPrecent) {
            onDetectNetworkState().show();
        } else {
//					new GetAllInstructor().execute();
        }
    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(
                TodaysScheduleActivity.this);
        builder1.setIcon(getResources().getDrawable(R.drawable.logo));
        builder1.setMessage("Please turn on internet connection and try again.")
                .setTitle("No Internet Connection.")
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                t.interrupt();
                                finish();
                                TodaysScheduleFragment.TimeList.clear();
                                TodaysScheduleFragment.showall = false;
                                WW_StaticClass.InstructorID = WW_StaticClass.DeckSuperID;
                                ClockinoutActivity.fromclock = false;
                            }
                        })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        startActivity(new Intent(
                                Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });
        return builder1.create();
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        WW_StaticClass.InstructorID = WW_StaticClass.DeckSuperID;
        ClockinoutActivity.fromclock = false;
        TodaysScheduleFragment.showall = false;
        TodaysScheduleFragment.TimeList.clear();
        t.interrupt();
        finish();
    }

    public static Thread t;
    int oldmin, newmin, oldmil, newmil, count = 1;

    private void SetRefreshView() {
        // TODO Auto-generated method stub

        t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                newmin = Calendar.getInstance().getTime()
                                        .getMinutes();
                                updateTextView();
                            }

                            @SuppressLint("SimpleDateFormat")
                            private void updateTextView() {
                                // TODO Auto-generated method stub
                                oldmil = 0;
                                oldmin = newmin;
                                if (newmin == 0 || newmin == 20 || newmin == 40) {
                                    newmil = Calendar.getInstance().getTime()
                                            .getSeconds();
                                    if (count == 1 && newmil == oldmil) {

                                        try {
                                            if (TodaysScheduleFragment.TimeList.size() > 0) {
                                                Date date = new Date();
                                                SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm");
                                                String datetime = format.format(date);
                                                for (int i = 0; i < TodaysScheduleFragment.TimeList.size(); i++) {
                                                    Log.i(TAG, "Date time = " + TodaysScheduleFragment.TimeList.get(i));
                                                    if (TodaysScheduleFragment.TimeList.get(i).toString().equalsIgnoreCase(datetime)) {
                                                        displayView_Main(viewpos);
                                                        break;
                                                    } else {
                                                        Log.i(TAG, datetime);
                                                        //														Toast.makeText(getApplicationContext(), "No more", 1).show();
                                                        //														break;

                                                    }
                                                }
                                            }

                                        } catch (Exception e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
            }
        };

        t.start();
    }


    private class GetAllInstructor extends AsyncTask<Void, Void, Void> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            currentDateandTime = format.format(date);
            navDrawerItems_main.clear();
            pDialog = new ProgressDialog(TodaysScheduleActivity.this);
            pDialog.setTitle("Please wait...");
            pDialog.setMessage("Fetching Instructors...");
            pDialog.setCancelable(true);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            SoapObject request = new SoapObject(AppConfig.NAMESPACE,
                    AppConfig.Get_InstrctListForMgrBySite_Method);
            request.addProperty("token", token);
            request.addProperty("siteid", WW_StaticClass.siteid.toString()
                    .replaceAll("\\[", "").replaceAll("\\]", ""));
            request.addProperty("strRScheDate", currentDateandTime);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    AppConfig.SOAP_ADDRESS);
            try {
                androidHttpTransport.call(
                        AppConfig.Get_InstrctListForMgrBySite_Action, envelope); // Calling
                // Web
                // service
                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i(TAG, "" + response.toString());
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1
                        .getProperty(0);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                if (code.equalsIgnoreCase("000")) {
                    getinstructor = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    Log.i(TAG, "mSoapObject3=" + mSoapObject3);
                    String resp = mSoapObject3.toString();
                    JSONObject jo = new JSONObject(resp);
                    JSONArray jArray = jo.getJSONArray("InstrListBySiteid");
                    Log.i(TAG, "jArray : " + jArray.toString());
                    UserId = new ArrayList<String>();
                    UserName = new ArrayList<String>();
                    JSONObject jsonObject;
                    for (int i = 0; i < jArray.length(); i++) {
                        jsonObject = jArray.getJSONObject(i);
                        UserName.add(jsonObject.getString("UserName"));
                        UserId.add(jsonObject.getString("Userid"));
                    }
                } else {
                    getinstructor = false;
                }
            } catch (SocketException e) {
                e.printStackTrace();
                other_problem = true;
            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                other_problem = true;
            } catch (JSONException e) {
                e.printStackTrace();
                server_response = true;
            } catch (Exception e) {
                e.printStackTrace();
                server_response = true;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (server_response) {
                server_response = false;
                onDetectNetworkState().show();
            } else if (other_problem) {
                other_problem = false;
                new GetAllInstructor().execute();
            } else {
                if (!getinstructor) {
                    Toast.makeText(getApplicationContext(),
                            "No instructor found", 1).show();
                    navDrawerItems_main.add(new AllInstructorItems(
                            "No instructor found."));
                } else {
                    getinstructor = false;
                    for (int i = 0; i < UserId.size(); i++) {
                        navDrawerItems_main.add(new AllInstructorItems(UserName
                                .get(i)));
                    }
                    adapter_main = new AllInstructorTodays(
                            getApplicationContext(), navDrawerItems_main);
                    lv_filter_instructor.setAdapter(adapter_main);
                    displayView_Main(0);
                }
            }
        }
    }

    public void displayView_Main(int position) {
        // update the main content by replacing fragments

        for (int i = 0; i < navDrawerItems_main.size(); i++) {
            fragment = new TodaysScheduleFragment();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            lv_filter_instructor.setItemChecked(position, true);
            lv_filter_instructor.setSelection(position);
            mDrawerLayout.closeDrawers();
        } else {
            // error in creating fragment
            Log.e(TAG, "Error in creating fragment");
        }
    }

    //-------------------------------------
    ArrayList<String> PoolName, PoolId;
    boolean pool_status, server_status = false, shadow_click = false, deck_click = false;
    public static final int request_shadow = 0;
    public static final int request_deck = 1;
    Dialog dialog = null;

    /**
     * Harsh - Shadow Request change
     */
    Button mBtn_request_shadow;
    ArrayList<String> SiteID = new ArrayList<String>();
    ArrayList<String> IScheduleID = new ArrayList<String>();
    ArrayList<String> StudentID = new ArrayList<String>();

    String whattimeforassist = "-1";
    String desk_poolid = "-1";
    String emp_type_for_cee = "", emp_type_for_cee_manager = "", emp_type_for_aquatics = "", emp_userid_for_cee = "", emp_userid_for_cee_manager = "", emp_userid_for_aquatics = "";
    //public static String Shadow_poolid = "poolid";
    public static String Shadow_poolid = "0", Shadow_reason = "";

    boolean status_shadow = false;
    //			String InstrIschlAry = "";


    public class IamInPool extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //			WW_StaticClass.Siteid = ViewCurrentScheduleFragment.SiteID.get(0);
        }

        @Override
        protected Void doInBackground(Void... params) {

            SoapObject request = new SoapObject(AppConfig.NAMESPACE, AppConfig.METHOD_NAME_GETPOOLLIST);
            request.addProperty("token", token);
            request.addProperty("siteid", TodaysScheduleFragment.SiteID.get(0));
            // Log.i(Tag, "Login name"+mEd_User.getText().toString());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    AppConfig.SOAP_ADDRESS);
            PoolId = new ArrayList<String>();
            PoolName = new ArrayList<String>();
            try {
                androidHttpTransport.call(AppConfig.SOAP_ACTION_POOLLIST, envelope); // Calling
                // Web
                // service

                SoapObject response = (SoapObject) envelope.getResponse();
                Log.i("here", "Result : " + response.toString());
                SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
                Log.i("Current Lesson", "mSoapObject1=" + mSoapObject1);
                SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
                Log.i("Current Lesson", "mSoapObject2=" + mSoapObject2);
                String code = mSoapObject2.getPropertyAsString(0).toString();
                Log.i("Code", code);
                //			response.toString();
                if (code.equals("000")) {
                    pool_status = true;
                    Object mSoapObject3 = mSoapObject1.getProperty(1);
                    Log.i("Current Lesson", "mSoapObject3=" + mSoapObject3);
                    String resp = mSoapObject3.toString();


                    Log.i("here", "Result : " + resp.toString());
                    JSONObject jobj = new JSONObject(resp);
                    JSONArray mArray = jobj.getJSONArray("Pools");
                    for (int i = 0; i < mArray.length(); i++) {
                        JSONObject mJsonObjectFee = mArray.getJSONObject(i);
                        PoolId.add(mJsonObjectFee.getString("PoolId"));
                        PoolName.add(mJsonObjectFee.getString("PoolName"));
                    }
                } else {
                    pool_status = false;
                }
            } catch (Exception e) {
                server_status = true;
                e.printStackTrace();

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void result) {
            dialog = new Dialog(TodaysScheduleActivity.this);
            if (server_status) {
                SingleOptionAlertWithoutTitle.ShowAlertDialog(
                        TodaysScheduleActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
                server_status = false;
            } else {
                if (pool_status) {
                    if (shadow_click) {
                        showDialog(request_shadow);
                        shadow_click = false;
                    } else if (deck_click) {
                        showDialog(request_deck);
                        deck_click = false;
                    } else {
                        Log.i("Nothing Click", "Nothing Click");
                    }
                    pool_status = false;
                } else {
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(TodaysScheduleActivity.this, "WaterWorks", "No pool found", "Ok");
                }
            }
        }
    }

    public void direct_request() {
        shadow_click = true;

        SoapObject request = new SoapObject(AppConfig.NAMESPACE, AppConfig.METHOD_NAME_GETPOOLLIST);
        request.addProperty("token", token);
        request.addProperty("siteid", TodaysScheduleFragment.SiteID.get(0));
        // Log.i(Tag, "Login name"+mEd_User.getText().toString());
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                SoapEnvelope.VER11); // Make an Envelop for sending as whole
        envelope.dotNet = true;
        envelope.setOutputSoapObject(request);
        Log.i("Request", "Request = " + request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(
                AppConfig.SOAP_ADDRESS);
        PoolId = new ArrayList<String>();
        PoolName = new ArrayList<String>();
        try {
            androidHttpTransport.call(AppConfig.SOAP_ACTION_POOLLIST, envelope); // Calling
            // Web
            // service

            SoapObject response = (SoapObject) envelope.getResponse();
            Log.i("here", "Result : " + response.toString());
            SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
            Log.i("Current Lesson", "mSoapObject1=" + mSoapObject1);
            SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
            Log.i("Current Lesson", "mSoapObject2=" + mSoapObject2);
            String code = mSoapObject2.getPropertyAsString(0).toString();
            Log.i("Code", code);
            //			response.toString();
            if (code.equals("000")) {
                pool_status = true;
                Object mSoapObject3 = mSoapObject1.getProperty(1);
                Log.i("Current Lesson", "mSoapObject3=" + mSoapObject3);
                String resp = mSoapObject3.toString();


                Log.i("here", "Result : " + resp.toString());
                JSONObject jobj = new JSONObject(resp);
                JSONArray mArray = jobj.getJSONArray("Pools");
                for (int i = 0; i < mArray.length(); i++) {
                    JSONObject mJsonObjectFee = mArray.getJSONObject(i);
                    PoolId.add(mJsonObjectFee.getString("PoolId"));
                    PoolName.add(mJsonObjectFee.getString("PoolName"));
                }

                dialog = new Dialog(TodaysScheduleActivity.this);
                if (server_status) {
                    SingleOptionAlertWithoutTitle.ShowAlertDialog(
                            TodaysScheduleActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
                    server_status = false;
                } else {
                    if (pool_status) {
                        if (shadow_click) {
                            showDialog(request_shadow);
                            shadow_click = false;
                        } else if (deck_click) {
                            showDialog(request_deck);
                            deck_click = false;
                        } else {
                            Log.i("Nothing Click", "Nothing Click");
                        }
                        pool_status = false;
                    } else {
                        SingleOptionAlertWithoutTitle.ShowAlertDialog(TodaysScheduleActivity.this, "WaterWorks", "No pool found", "Ok");
                    }
                }
            } else {
                pool_status = false;
            }
        } catch (Exception e) {
            server_status = true;
            e.printStackTrace();

        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case request_shadow:
                dialog = new Dialog(TodaysScheduleActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.requestshadow);
                Button send_request = (Button) dialog.findViewById(R.id.btn_rs_send_request);
                final EditText txt_reason = (EditText) dialog.findViewById(R.id.txt_reason);


                send_request.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        //					if(Shadow_poolid.isEmpty()){
                        //						SingleOptionAlertWithoutTitle.ShowAlertDialog(ScheduleActivity.this, "WaterWorks", "Please select pool befor requesting", "Ok");
                        //					}
                        dialog.dismiss();
                        //				for(int i=0;i<WW_StaticClass.SStudnetID.size();i++){
                        //					SStudnetid = WW_StaticClass.SStudnetID.get(i);
                        Shadow_reason = txt_reason.getText().toString();
                        new InsertShadowRequest().execute();
                        //				}
                    }
                });
//26-06-2017 megha
//					LinearLayout mLinearLayout = (LinearLayout)dialog.findViewById(R.id.ll_pool_list);
//					final RadioButton[] rb = new RadioButton[PoolName.size()];
//					RadioGroup rg = new RadioGroup(getApplicationContext());
//					rg.setOrientation(RadioGroup.HORIZONTAL);
//					for (int i = 0; i < PoolName.size(); i++) {
//						rb[i] = new RadioButton(getApplicationContext());
//						rg.addView(rb[i]);
//						rb[i].setText(PoolName.get(i));
//						rb[i].setId(i);
//						rb[i].setButtonDrawable(android.R.drawable.btn_radio);
//						rb[i].setTextColor(getResources().getColor(R.color.texts1));
//						rb[i].setTextSize(18);
//
//					}
//					mLinearLayout.addView(rg);
//					rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//
//						@Override
//						public void onCheckedChanged(RadioGroup group, int checkedId) {
//							// TODO Auto-generated method stub
//							Log.i("Radio Check", ""+checkedId);
//							Log.i("Pool Name", "Pool name = " + PoolName.get(checkedId));
//							int a = PoolName.indexOf(PoolName.get(checkedId));
//							Log.i("Here", ""+a);
//							String poolidvalue = PoolId.get(a);
//							Shadow_poolid = poolidvalue;
//							Log.i("poolid", ""+poolidvalue);
//
//						}
//					});
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = LayoutParams.MATCH_PARENT;
                lp.height = LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(lp);
                dialog.setCanceledOnTouchOutside(true);

                break;
        }
        return dialog;
    }

    ProgressDialog pDialog2;

    public class InsertShadowRequest extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //					pDialog2 = new ProgressDialog(ScheduleActivity.this);
            //					pDialog2.setMessage(Html.fromHtml("Loading wait..."));
            //					pDialog2.setIndeterminate(true);
            //					pDialog2.setCancelable(false);
            //					pDialog2.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub
            //SoapObject request = new SoapObject(AppConfig.NAMESPACE,AppConfig.METHOD_NAME_InsertShadowRequest_New1);
            SoapObject request = new SoapObject(AppConfig.NAMESPACE, AppConfig.METHOD_NAME_InsertShadowRequest_LA);
            //			request.addProperty("token",  WW_StaticClass.UserToken);
            //			request.addProperty("IScheduleID", IScheduleID.get(0).toString());
            //			request.addProperty("Rinstructorid", WW_StaticClass.InstructorID);
            //			request.addProperty("RPoolID", Shadow_poolid);


            request.addProperty("token", token);
            //request.addProperty("RPoolID", Shadow_poolid);
            request.addProperty("RPoolID", 0);
            request.addProperty("InstrIschlAry", InstrIschlAry);
            request.addProperty("Reason", Shadow_reason);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                    SoapEnvelope.VER11); // Make an Envelop for sending as whole
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            Log.i("Request", "Request = " + request);
            HttpTransportSE androidHttpTransport = new HttpTransportSE(
                    AppConfig.SOAP_ADDRESS);
            try {
                //androidHttpTransport.call(AppConfig.SOAP_Action_InsertShadowRequest_New1,envelope); // Calling Web service
                androidHttpTransport.call(AppConfig.SOAP_Action_InsertShadowRequest_LA, envelope); // Calling Web service
                SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                Log.i("here", "Result : " + response.toString());
                if (response.toString().equalsIgnoreCase("Inserted Successfully") || response.toString().equalsIgnoreCase("Registered Successfully")) {
                    status_shadow = true;
                } else {
                    status_shadow = false;
                }
            } catch (SocketTimeoutException e) {
                server_status = true;
                e.printStackTrace();
            } catch (Exception e) {
                server_status = true;
                e.printStackTrace();
            }
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //				pDialog2.dismiss();
            //			fl_vcl_loading.setVisibility(View.GONE);
            if (server_status == true) {
                SingleOptionAlertWithoutTitle.ShowAlertDialog(TodaysScheduleActivity.this, "WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "Ok");
                server_status = false;
            }
            if (status_shadow) {
                status_shadow = false;
                //					SingleOptionAlertWithoutTitle.ShowAlertDialog(ViewCurrentLessonActivity.this, "Request Shadow", "Request submitted.", "Ok");
                AlertDialog alertDialog = new AlertDialog.Builder(TodaysScheduleActivity.this).create();
                // hide title bar
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setTitle("WaterWorks");
                alertDialog.setCanceledOnTouchOutside(false);
                // set the message
                alertDialog.setMessage("Request submitted.");
                // set button1 functionality
                alertDialog.setButton("Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // close dialog

                                dialog.cancel();
                                TodaysScheduleActivity.this.dialog.dismiss();
                            }
                        });
                // show the alert dialog
                alertDialog.show();

            } else {
                AlertDialog alertDialog = new AlertDialog.Builder(TodaysScheduleActivity.this).create();
                // hide title bar
                alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                alertDialog.setTitle("WaterWorks");
                alertDialog.setCanceledOnTouchOutside(false);
                // set the message
                alertDialog.setMessage("Request not submitted.");
                // set button1 functionality
                alertDialog.setButton("Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // close dialog
                                dialog.cancel();
                                TodaysScheduleActivity.this.dialog.dismiss();
                            }
                        });
                // show the alert dialog
                alertDialog.show();
            }
        }

    }

    public void call_attendance(int pos) {
        positions = pos;
        new GetAttendance().execute();
    }

    private class GetAttendance extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy h:mm a");
            currentDateandTime = format.format(date);

            //single instructor patch : Aanal : 23Aug2016
            Instroctorid.clear();
            Instroctorid.add(inst_id);
        }

        @Override
        protected Void doInBackground(Void... params) {
            // TODO Auto-generated method stub

            for (int j = 0; j < Instroctorid.size(); j++) {

                SoapObject request = new SoapObject(AppConfig.NAMESPACE,
                        AppConfig.METHOD_NAME_GetAttendanceList);
                request.addProperty("token", token);
                request.addProperty("Rinstructorid", Instroctorid.get(j));
                request.addProperty("strRScheDate", currentDateandTime);
                //			request.addProperty("strRScheDate","01/18/2015 09:00 AM" );

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11); // Make an Envelop for sending as whole
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                Log.i("Request", "Request = " + request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(
                        AppConfig.SOAP_ADDRESS);
                try {
                    androidHttpTransport.call(AppConfig.SOAP_Action_AttendanceList,
                            envelope); // Calling Web service
                    SoapObject response = (SoapObject) envelope.bodyIn;
                    Log.i(TAG, "Result : " + response.toString());
                    SoapPrimitive sp1 = (SoapPrimitive) response.getProperty(0);
                    String resp = sp1.toString();
                    JSONObject jo = new JSONObject(resp);
                    wu_avail = jo.getInt("wu_avail");
                    //				ISAFlag = jo.getBoolean("ISAFlag");
                    if (wu_avail == 0) {

                    } else if (wu_avail == 1 || wu_avail == 2) {

                        JSONArray jArray = jo.getJSONArray("Attendance");
                        Log.i(TAG, "jArray : " + jArray.toString());

                        JSONObject jsonObject;
                        JSONObject jsonObject2, jsonObject3;
                        JSONArray jArray2;
                        JSONArray jArray3 = null;
                        for (int k = 0; k < jArray.length(); k++) {
                            jsonObject = jArray.getJSONObject(k);
                            Log.i(TAG, "jsonObject: " + jsonObject.toString());

                            jArray2 = jsonObject.getJSONArray("Items");
                            Log.i(TAG, "jArray2 : " + jArray2.toString());
                            for (int i = 0; i < jArray2.length(); i++) {
                                jsonObject2 = jArray2.getJSONObject(i);
                                //								SiteID.add(jsonObject2.getString("SiteID"));
                                IScheduleID.add(jsonObject2.getString("IScheduleID"));
                                StudentID.add(jsonObject2.getString("StudentID"));
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (SocketTimeoutException e) {
                    // TODO: handle exception
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;

        }

        @SuppressWarnings("deprecation")
        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            InstrIschlAry = "";
            if (TodaysScheduleFragment.IScheduleID.size() > 0) {
                ArrayList<String> temp_array = new ArrayList<String>();
                for (int i = 0; i < Instroctorid.size(); i++) {
                        temp_array.add(Instroctorid.get(i) + "|" + TodaysScheduleFragment.IScheduleID.get(positions));
                }
                for (String s : temp_array) {
                    InstrIschlAry += s + ",";
                }
            }
        }
    }

}
