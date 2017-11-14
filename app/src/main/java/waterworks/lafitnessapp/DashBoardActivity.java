package waterworks.lafitnessapp;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import waterworks.lafitnessapp.adapter.CustomMenuDataPageAdapter;
import waterworks.lafitnessapp.adapter.HomeGridMenuOptionAdapter;
import waterworks.lafitnessapp.utility.AppConfig;
import waterworks.lafitnessapp.utility.Utility;
import waterworks.lafitnessapp.utility.WW_StaticClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class DashBoardActivity extends Activity implements OnClickListener {
	boolean connectionout = false;
	TextView mtv_name, mtv_day, mtv_date;
	Button mBtn_logout;
	boolean data_load_status, server_status = false, login_status = false;
	public static String TAG = "MenuActivity";
	public static ArrayList<String> Announcements_title, Announcements_content;
	public static ArrayList<String> title1, title2, content1, content2;
	TextView tv_GetAnnouncements;
	ViewPager myPager;
	Boolean isInternetPresent = false;
	GridView grid_home_option;
	String[] MenuName;
	int[] imageId_MANAGER = { R.drawable.clock, R.drawable.menu_schedule,
			R.drawable.menu_currentlesson, R.drawable.todayssales,
			R.drawable.menu_mail, R.drawable.menu_reports, R.drawable.menu_cup,
			R.drawable.menu_peer, R.drawable.menu_bulb, R.drawable.menu_ring };
	HomeGridMenuOptionAdapter adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dash_board);
		
		isInternetPresent = Utility.isNetworkConnected(DashBoardActivity.this);
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		} else {
			check_dataLost();
			Initialization();
			grid_home_option.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					if (position == 0) {
						WW_StaticClass.CLOCKED_TODAY_INSERT=false;
						Intent i = new Intent(DashBoardActivity.this,
								ClockinoutActivity.class);
						startActivity(i);
					} else if (position == 1) {
						/*Intent i = new Intent(DashBoardActivity.this,
								InstructorSelectionActivity.class);*/
						WW_StaticClass.CLOCKED_TODAY_INSERT=false;
						Intent i = new Intent(DashBoardActivity.this,
								TodaysScheduleActivity.class);
//						i.putExtra("FROM", "TODAY");
						startActivity(i);
					} else if (position == 2) {
						/*Intent i = new Intent(DashBoardActivity.this,
								InstructorSelectionActivity.class);*/
						WW_StaticClass.CLOCKED_TODAY_INSERT=false;
						Intent i = new Intent(DashBoardActivity.this,
								ScheduleActivity.class);
						i.putExtra("FROM", "CURRENT");
						startActivity(i);
						
						
//						Intent currentIntent=new Intent(DashBoardActivity.this,ViewCurrentLessonActivity.class);
//						currentIntent.putExtra("DELETE", "NO");
//						currentIntent.putExtra("HOUR", "");
//						currentIntent.putExtra("MIN", "");
//						currentIntent.putExtra("DATE", "");
//						startActivity(currentIntent);
						
					} else if (position == 3) {
						WW_StaticClass.CLOCKED_TODAY_INSERT=false;
						Intent messageIntent = new Intent(
								DashBoardActivity.this,
								TodaysSalesActivity.class);
						startActivity(messageIntent);
					} else if (position == 4) {
						WW_StaticClass.CLOCKED_TODAY_INSERT=false;
						Intent messageIntent = new Intent(
								DashBoardActivity.this,
								MessageCenterActivity.class);
						startActivity(messageIntent);
					}else if(position == 5){
						WW_StaticClass.CLOCKED_TODAY_INSERT=false;
						Intent i = new Intent(getApplicationContext(),ReportActivity.class);
						startActivity(i);
					}
					else if (position == 6) {
						WW_StaticClass.CLOCKED_TODAY_INSERT=false;
						Intent intent = new Intent(DashBoardActivity.this,
								AwardTurboActivity.class);
						startActivity(intent);
						finish();
					} else if (position == 7) {
						WW_StaticClass.CLOCKED_TODAY_INSERT=false;
						Intent i = new Intent(getApplicationContext(),
								DetailReportActivity.class);
						i.putExtra("FROM", "");
						/*i.putExtra("url",
								"http://forms.waterworksswim.com/survey_office/peer.php?type=A&uid="
										+ WW_StaticClass.UserName);*///Old URL
						i.putExtra("url",
								"http://reports.waterworksswim.com/reports/office/peer.php?type=O&uid=jon"
										+ WW_StaticClass.UserName);
						startActivity(i);
						
					}else if (position==8) {
						WW_StaticClass.CLOCKED_TODAY_INSERT=false;
						Intent ideaIntent = new Intent(
								DashBoardActivity.this,
								TurboIdeaActivity.class);
						startActivity(ideaIntent);
						finish();
					}else if (position ==9) {
						WW_StaticClass.CLOCKED_TODAY_INSERT=false;
						Intent i = new Intent(DashBoardActivity.this,
								RequestDeckMenuActivity.class);
						startActivity(i);
					}
					else {
						WW_StaticClass.CLOCKED_TODAY_INSERT=false;
						Toast.makeText(getApplicationContext(),
								MenuName[position], 1).show();
					}
				}
			});
		}
	}
	
	public void check_dataLost(){
		if(!WW_StaticClass.UserName.equals(null) || !WW_StaticClass.UserName.contains("UserName")){
			//Ignore this case
		}else{
			SharedPreferences imp_details = getPreferences(Context.MODE_PRIVATE);
			WW_StaticClass.UserName = imp_details.getString("UserName", "");
			WW_StaticClass.UserToken = imp_details.getString("UserToken", "");
			WW_StaticClass.UserLevel = imp_details.getString("UserLevel", "");
			WW_StaticClass.InstructorID = imp_details.getString("UserId", "");
			WW_StaticClass.DeckSuperID = imp_details.getString("UserId", "");
			String sites = imp_details.getString("sitelist", "");
			
			String temp[] = sites.toString().split("\\,");
			String temp2[];
			for (int i = 0; i < temp.length; i++) {
				temp2 = temp[i].toString().split("\\:");
				WW_StaticClass.siteid.add(temp2[0].toString());
				WW_StaticClass.sitename.add(temp2[1].toString());
			}
			
			Toast.makeText(getApplicationContext(), "Restored from cache data.", Toast.LENGTH_SHORT).show();
		}
	}
	
	private void Initialization() {
		// TODO Auto-generated method stub
		mtv_name = (TextView) findViewById(R.id.tv_app_inst_name);
		mtv_day = (TextView) findViewById(R.id.tv_app_day);
		mtv_date = (TextView) findViewById(R.id.tv_app_date);
		mBtn_logout = (Button) findViewById(R.id.btn_app_logoff);
		MenuName = getResources().getStringArray(R.array.menuoption);
		adapter = new HomeGridMenuOptionAdapter(DashBoardActivity.this,
				MenuName, imageId_MANAGER);
		grid_home_option = (GridView) findViewById(R.id.grid_home_grid);
		grid_home_option.setNumColumns(5);
		grid_home_option.setAdapter(adapter);
		tv_GetAnnouncements = (TextView) findViewById(R.id.tv_GetAnnouncements);
		myPager = (ViewPager) findViewById(R.id.ll_menu_rotate_data);
		SetUp();
	}

	private void SetUp() {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
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
		mtv_name.setText(WW_StaticClass.UserName);
		mtv_day.setText(day_name);
		String m, d;
		if (String.valueOf(Month).length() == 1) {
			m = "0" + (Month + 1);
		} else {
			m = "" + (Month + 1);
		}
		if (String.valueOf(Date).length() == 1) {
			d = "0" + (Date);
		} else {
			d = "" + (Date);
		}

		mtv_date.setText(m + "/" + d);
		
		isInternetPresent = Utility.isNetworkConnected(DashBoardActivity.this);
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		} else {
			new DataLoad().execute();
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
		isInternetPresent = Utility.isNetworkConnected(DashBoardActivity.this);
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		} else {
			check_dataLost();
//			new DataLoad().execute();
		}
	}

	ProgressDialog pDialog;

	public class DataLoad extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(DashBoardActivity.this);
			pDialog.setTitle("LAFitnessApp");
			pDialog.setMessage(Html.fromHtml("Loading wait..."));
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(AppConfig.NAMESPACE,
					AppConfig.GetAnnouncements_Method);
			request.addProperty("token", WW_StaticClass.UserToken);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request", "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					AppConfig.SOAP_ADDRESS);
			Announcements_title = new ArrayList<String>();
			Announcements_content = new ArrayList<String>();
			try {
				androidHttpTransport.call(AppConfig.GetAnnouncements_Action,
						envelope);

				SoapObject response = (SoapObject) envelope.getResponse();
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1
						.getProperty(0);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				if (code.equals("000")) {
					data_load_status = true;
					Object mSoapObject3 = mSoapObject1.getProperty(1);

					String resp = mSoapObject3.toString();
					JSONObject jobj = new JSONObject(resp);
					JSONArray mArray = jobj.getJSONArray("Announcements");
					for (int i = 0; i < mArray.length(); i++) {
						JSONObject mJsonObjectFee = mArray.getJSONObject(i);
						Announcements_title.add(mJsonObjectFee
								.getString("title"));
						Announcements_content.add(mJsonObjectFee
								.getString("content"));

					}
				} else {
					data_load_status = false;
				}
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				connectionout = true;
			} catch (SocketException e) {
				// TODO: handle exception
				e.printStackTrace();
				connectionout = true;
			} catch (JSONException e) {
				// TODO: handle exception
				e.printStackTrace();
				server_status = true;
			} catch (Exception e) {
				server_status = true;
				e.printStackTrace();
			}

			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if(pDialog!=null){
				pDialog.cancel();
			}
			if (server_status) {
				server_status = false;
			} else if (connectionout) {
				connectionout = false;
				new DataLoad().execute();
			} else {
				if (data_load_status) {
					title1 = new ArrayList<String>();
					title2 = new ArrayList<String>();
					content1 = new ArrayList<String>();
					content2 = new ArrayList<String>();
					for (int i = 0; i < Announcements_title.size(); i++) {
						if (i % 2 == 1) {
							title1.add(Announcements_title.get(i));
							Log.i("title1",
									"Title 1=" + Announcements_title.get(i));
						} else {
							title2.add(Announcements_title.get(i));
							Log.i("title2",
									"Title 2=" + Announcements_title.get(i));
						}
					}
					for (int i = 0; i < Announcements_content.size(); i++) {
						if (i % 2 == 1) {
							content1.add(Announcements_content.get(i));
							Log.i("content1", "Content 1="
									+ Announcements_content.get(i));
						} else {
							content2.add(Announcements_content.get(i));
							Log.i("content2", "Content 2="
									+ Announcements_content.get(i));
						}
					}
					// Create and set adapter
					CustomMenuDataPageAdapter adapter = new CustomMenuDataPageAdapter(
							DashBoardActivity.this);
					myPager.setVisibility(View.VISIBLE);
					myPager.setAdapter(adapter);
					myPager.setCurrentItem(0);
					data_load_status = false;
				} else {
					myPager.setVisibility(View.GONE);
				}
			}

		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		// finish();

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				DashBoardActivity.this);

		// set title
		alertDialogBuilder.setTitle("LAFitnessApp");
		alertDialogBuilder.setIcon(getResources().getDrawable(R.drawable.logo));

		// set dialog message
		alertDialogBuilder
				.setMessage("Are you sure you want to logout ?")
				.setCancelable(false)
				.setPositiveButton("Logout",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								WW_StaticClass.InstructorID = "";
								finish();

								Intent loginIntent = new Intent(
										DashBoardActivity.this,
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
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(
				DashBoardActivity.this);
		builder1.setIcon(getResources().getDrawable(R.drawable.logo));
		builder1.setMessage("Please turn on internet connection and try again.")
				.setTitle("No Internet Connection.")
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								finish();
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
			case R.id.btn_app_logoff:
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						DashBoardActivity.this);

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
												DashBoardActivity.this,
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
}
