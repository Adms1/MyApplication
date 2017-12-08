package waterworks.lafitnessapp;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class ReportActivity extends Activity implements OnClickListener {
    TextView mtv_name, mtv_day, mtv_date, mtv_time;
    String time;
    public static String TAG = "ReportActivity";
    String am_pm;
    java.util.Date noteTS;
    Boolean isInternetPresent = false;
    GridView grid_report_option;
    String[] Reports;
    ReportsAdapter adapter;
    Thread t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        isInternetPresent = Utility.isNetworkConnected(ReportActivity.this);
        if (!isInternetPresent) {
            onDetectNetworkState().show();
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
        Log.i("Time", "Time = " + hour + ":" + min + " " + " " + day_name + " "
                + Date + "/" + Month);
        // ////////////
        Initialize();

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
                                mtv_time.setText(DateFormat
                                        .format(time, noteTS) + " " + am_pm);
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
    }

    private void Initialize() {
        // TODO Auto-generated method stub
        mtv_date = (TextView) findViewById(R.id.tv_app_date);
        mtv_day = (TextView) findViewById(R.id.tv_app_day);
        mtv_name = (TextView) findViewById(R.id.tv_app_inst_name);
        mtv_time = (TextView) findViewById(R.id.tv_app_time);
        Reports = getResources().getStringArray(R.array.report);
        adapter = new ReportsAdapter(getApplicationContext(), Reports);
        grid_report_option = (GridView) findViewById(R.id.gv_rep);
        grid_report_option.setAdapter(adapter);
        grid_report_option.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                switch (position) {

                    case 0:
                        Intent i0 = new Intent(getApplicationContext(),
                                DailyReportActivity.class);
                        startActivity(i0);
                        break;

                    case 1:
                        Intent i1 = new Intent(getApplicationContext(), DetailReportActivity.class);
                        i1.putExtra("FROM", "");
//                        i1.putExtra("url", "http://192.168.1.32:8087/Q12Survey_LaFitnessApp.aspx?Userid="
//                                + WW_StaticClass.InstructorID);
                        i1.putExtra("url", "http://office.waterworksswimonline.com/NewCode/Q12Survey_LaFitnessApp.aspx?Userid="
                                + WW_StaticClass.InstructorID);
                        startActivity(i1);
                        break;

                    case 2:
                        Intent i2 = new Intent(getApplicationContext(),
                                DetailReportActivity.class);
                        i2.putExtra("FROM", "");
                        i2.putExtra("url",
                                "http://reports.waterworksswim.com/reports/office/peer.php?type=O&uid=jon" + WW_StaticClass.UserName);
                        startActivity(i2);
                        break;

                    case 3:
                        Intent i3 = new Intent(getApplicationContext(),
                                DetailReportActivity.class);
                        i3.putExtra("FROM", "");
                        i3.putExtra("url",
                                "http://office.waterworksswimonline.com/NewCode/employeesurvey_new.aspx?surveyid=2&Userid=" + WW_StaticClass.InstructorID);
                        startActivity(i3);
                        break;

                    case 4:
                        Intent i4 = new Intent(getApplicationContext(), DetailReportActivity.class);
                        i4.putExtra("FROM", "");
                        i4.putExtra("url", AppConfig.Report_Url
                                + "ViewIndividualPerformance.aspx?Userid=" + WW_StaticClass.InstructorID);
                        startActivity(i4);
                        break;

                    case 5:
                        Intent i5 = new Intent(getApplicationContext(),
                                DetailReportActivity.class);
                        i5.putExtra("FROM", "");
//                        i5.putExtra("url",
//                                "http://reports.waterworksswim.com/reports/office/Qmonth.php")
                        i5.putExtra("url", "http://reports.waterworksswim.com/reports/office/Qweek.php");
                        startActivity(i5);
                        break;
                    case 6:
                        Intent i6 = new Intent(getApplicationContext(),
                                DetailReportActivity.class);
                        String new_change = "&Lafitness=1";
                        i6.putExtra("FROM", "");
                        i6.putExtra("url", AppConfig.Report_Url
                                + "ViewMyTimeCard.aspx?Userid="
                                + WW_StaticClass.InstructorID
                                + "&Status=" + WW_StaticClass.UserLevel + new_change);
                        startActivity(i6);
                        break;

                    case 7:
                        Intent i7 = new Intent(getApplicationContext(),
                                DetailReportActivity.class);
                        i7.putExtra("FROM", "");
                        i7.putExtra("url", AppConfig.Report_Url
                                + "AppRankingReport.aspx?Userid="
                                + WW_StaticClass.InstructorID + "&Status=" + WW_StaticClass.UserLevel);
                        System.out.println("Appranking : " + AppConfig.Report_Url
                                + "AppRankingReport.aspx?Userid="
                                + WW_StaticClass.InstructorID + "&Status=" + WW_StaticClass.UserLevel);
                        startActivity(i7);
                        break;
                    case 8:
                        Intent i8 = new Intent(getApplicationContext(),
                                DetailReportActivity.class);
                        i8.putExtra("FROM", "");
                        i8.putExtra("url",
                                "http://reports.waterworksswim.com/issue/pool.php");
                        startActivity(i8);
                        break;

                    case 9:
                        Intent i9 = new Intent(getApplicationContext(),
                                SubmitWorkReport.class);
                        i9.putExtra("FROM", "");
                        startActivity(i9);
                        break;
                    case 10:
                        Intent i10 = new Intent(getApplicationContext(),
                                DetailReportActivity.class);
                        i10.putExtra("FROM", "");
                        i10.putExtra("url",
                                "http://reports.waterworksswim.com/issue/it.php");
                        startActivity(i10);
                        break;

                    case 11:
                        Intent i11 = new Intent(getApplicationContext(),
                                DetailReportActivity.class);
                        i11.putExtra("FROM", "");
                        i11.putExtra("url",
                                "http://reports.waterworksswim.com/issue/maint.php");
                        startActivity(i11);
                        break;

                    case 12:
                        Intent i12 = new Intent(getApplicationContext(),
                                DetailReportActivity.class);
                        i12.putExtra("FROM", "");
                        i12.putExtra("url",
                                "http://reports.waterworksswim.com/requests/suit.php");
                        startActivity(i12);
                        break;
                    case 13:
                        Intent i13 = new Intent(getApplicationContext(),
                                DetailReportActivity.class);
                        i13.putExtra("FROM", "");
                        i13.putExtra("url",
                                "http://reports.waterworksswim.com/reports/aquatics/concern_cust.php");
                        startActivity(i13);
                        break;
                    case 14:
                        Intent i14 = new Intent(getApplicationContext(), DetailReportActivity.class);

                        i14.putExtra("FROM", "");
                        i14.putExtra("url",
                                "http://office.waterworksswimonline.com/newcode/TimeClockAdjustment_LaFitnessApp.aspx?Userid="
                                        + WW_StaticClass.InstructorID + "&Status=" + WW_StaticClass.UserLevel);
                        startActivity(i14);
                        break;

                    case 15:
                        Intent i15 = new Intent(getApplicationContext(),
                                DetailReportActivity.class);
                        i15.putExtra("FROM", "");
                        i15.putExtra("url",
                                "http://office.waterworksswimonline.com/NewCode/RequestTimeOffApp.aspx?userid="
                                        + WW_StaticClass.InstructorID);
                        startActivity(i15);
                        break;
                    case 16:
                        Intent i16 = new Intent(getApplicationContext(), DetailReportActivity.class);
                        i16.putExtra("FROM", "");
                        i16.putExtra("url", "http://reports.waterworksswim.com/reports/office/feedback_srvy.php");
                        startActivity(i16);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    public AlertDialog onDetectNetworkState() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
        builder1.setMessage("Please turn on internet connection and try again.")
                .setTitle("No Internet Connection.")
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                t.interrupt();
                                ReportActivity.this.finish();
                            }
                        })
                .setPositiveButton("Οk", new DialogInterface.OnClickListener() {

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
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (isInternetPresent) {
            switch (v.getId()) {
                case R.id.btn_back:
                    t.interrupt();
                    finish();
                    break;
                case R.id.btn_app_logoff:
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            ReportActivity.this);
                    // set title
                    alertDialogBuilder.setTitle("WaterWorks");
                    alertDialogBuilder.setIcon(getResources().getDrawable(
                            R.drawable.ic_launcher));

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Are you sure you want to logout ?")
                            .setCancelable(false)
                            .setPositiveButton("Logout",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            WW_StaticClass.InstructorID = "";
                                            t.interrupt();
                                            finish();

                                            Intent loginIntent = new Intent(
                                                    ReportActivity.this,
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
                    break;
                default:
                    break;
            }
        } else {
            onDetectNetworkState().show();
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
        isInternetPresent = Utility.isNetworkConnected(getApplicationContext());
    }

    public class ReportsAdapter extends BaseAdapter {
        Context context;
        String Reports[];

        public ReportsAdapter(Context context, String[] reports) {
            super();
            this.context = context;
            Reports = reports;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return Reports.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return Reports[position];
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View grid;
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                grid = new View(context);
                grid = inflater.inflate(R.layout.reports_item, null);
                TextView tv_item = (TextView) grid
                        .findViewById(R.id.tv_reports_item);
                tv_item.setText(Reports[position]);
            } else {
                grid = (View) convertView;
            }
            return grid;
        }

    }
}
