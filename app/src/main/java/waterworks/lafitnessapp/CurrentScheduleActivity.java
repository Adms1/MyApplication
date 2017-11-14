package waterworks.lafitnessapp;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import waterworks.lafitnessapp.adapter.AllInstructorListAdapter;

import waterworks.lafitnessapp.model.AllInstructorItems;
import waterworks.lafitnessapp.utility.AppConfig;
import waterworks.lafitnessapp.utility.Utility;
import waterworks.lafitnessapp.utility.WW_StaticClass;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CurrentScheduleActivity extends Activity {
	private static final String TAG = "Current Schedule";
	private DrawerLayout mDrawerLayout;
	private LinearLayout ll_filter;
	boolean isInternetPrecent = false, getinstructor = false,
			other_problem = false, server_response = false,
			connectionout = false, getlevel = false, getschedule = false;
	ActionBar actionBar;
	ListView lv_filter_instructor;
	ActionBarDrawerToggle mDrawerToggle;
	public ArrayList<String> UserId, UserName;
	private ArrayList<AllInstructorItems> navDrawerItems_main;
	private AllInstructorListAdapter adapter_main;
	ImageView drawerImageView, isa_main;
	TextView actionBarTitleview;
	LinearLayout actionBarLayout;
	Fragment fragment;
	String currentDateandTime;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_current_schedule);
		isInternetPrecent = Utility
				.isNetworkConnected(CurrentScheduleActivity.this);
		if (isInternetPrecent) {
			Initialization();
			new GetAllInstructor().execute();
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
				R.layout.currentschedule_actionbar, null);
		actionBarTitleview = (TextView) actionBarLayout
				.findViewById(R.id.actionbar_titleview);
		actionBarTitleview.setText("Current Schedule");
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(
				ActionBar.LayoutParams.MATCH_PARENT,
				ActionBar.LayoutParams.MATCH_PARENT, Gravity.LEFT);

		drawerImageView = (ImageView) actionBarLayout
				.findViewById(R.id.drawer_imageview);
		isa_main = (ImageView) actionBarLayout
				.findViewById(R.id.drawer_imageview1);
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
		isa_main.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		actionBar.setCustomView(actionBarLayout, params);
		ll_filter = (LinearLayout) findViewById(R.id.ll_filter);
		lv_filter_instructor = (ListView) findViewById(R.id.lv_filter_instructor);
		lv_filter_instructor.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(),
						UserId.get(position).toString(), 1).show();
				displayView_Main(position);
				if (mDrawerLayout.isDrawerOpen(ll_filter)) {
					mDrawerLayout.closeDrawers();
				}

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
	}

	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(
				CurrentScheduleActivity.this);
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

	// ////////////////////////new//////////////////////////

	private class GetAllInstructor extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			currentDateandTime = format.format(date);
			navDrawerItems_main.clear();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(AppConfig.NAMESPACE,
					AppConfig.Get_InstrctListForMgrBySite_Method);
			request.addProperty("token", WW_StaticClass.UserToken);
//			request.addProperty("siteid", "0");
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
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				other_problem = true;
			} catch (JSONException e) {
				e.printStackTrace();
				server_response = true;
			} catch (SocketException e) {
				e.printStackTrace();
				other_problem = true;
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
//					adapter_main = new AllInstructorListAdapter(
//							getApplicationContext(), navDrawerItems_main);
//					lv_filter_instructor.setAdapter(adapter_main);
					displayView_Main(0);
				}
			}
		}
	}

	private void displayView_Main(int position) {
		// update the main content by replacing fragments
		for (int i = 0; i < navDrawerItems_main.size(); i++) {
			fragment = new CurrentScheduleFragment();
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
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	}
