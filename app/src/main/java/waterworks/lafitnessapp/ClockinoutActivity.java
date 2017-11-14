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

import waterworks.lafitnessapp.utility.AppConfig;
import waterworks.lafitnessapp.utility.SingleOptionAlertWithoutTitle;
import waterworks.lafitnessapp.utility.Utility;
import waterworks.lafitnessapp.utility.WW_StaticClass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ClockinoutActivity extends Activity implements OnClickListener {
	boolean connectionout = false, isInternetPresent = false,
			server_status = false, login_status = false,
			CheckTimeClicks_status = false, Attleft = false;
	TextView mtv_name, mtv_day, mtv_date;
	Button Btn_logout;
	TextView tv_clockinday, tv_clockinbrk, tv_clockoutday, tv_clockoutbrk;
	String username, password, token, userid, userlevel, julinkid, processMsg,
			WU_Att;
	private static String Tag = "Clock in/out";
	public static boolean fromclock = false;
	int whatclick = 0;
	ArrayList<String> ticktype, juid, jobtitle, Msg;
	LinearLayout ll_clockinday, ll_clockinbrk, ll_clockoutday, ll_clockoutbrk;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clockinout);
		
		isInternetPresent = Utility.isNetworkConnected(ClockinoutActivity.this);
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		} else {
			Initialization();
		}
	}

	private void Initialization() {
		// TODO Auto-generated method stub
		mtv_name = (TextView) findViewById(R.id.tv_app_inst_name);
		mtv_day = (TextView) findViewById(R.id.tv_app_day);
		mtv_date = (TextView) findViewById(R.id.tv_app_date);
		Btn_logout = (Button) findViewById(R.id.btn_app_logoff);
		Btn_logout.setVisibility(View.GONE);
		tv_clockinbrk = (TextView) findViewById(R.id.clockinbrk);
		tv_clockinday = (TextView) findViewById(R.id.clockinday);
		tv_clockoutbrk = (TextView) findViewById(R.id.clockoutbrk);
		tv_clockoutday = (TextView) findViewById(R.id.clockoutday);
		ll_clockinbrk = (LinearLayout) findViewById(R.id.ll_clockinbrk);
		ll_clockinday = (LinearLayout) findViewById(R.id.ll_clockinday);
		ll_clockoutbrk = (LinearLayout) findViewById(R.id.ll_clockoutbrk);
		ll_clockoutday = (LinearLayout) findViewById(R.id.ll_clockoutday);
		tv_clockinbrk.setOnClickListener(this);
		tv_clockinday.setOnClickListener(this);
		tv_clockoutbrk.setOnClickListener(this);
		tv_clockoutday.setOnClickListener(this);
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
		isInternetPresent = Utility.isNetworkConnected(ClockinoutActivity.this);
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		}
	}

	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(
				ClockinoutActivity.this);
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
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (isInternetPresent) {
			switch (v.getId()) {
			case R.id.clockinday:
				whatclick = 1;
				LoginDialog();
				break;
			case R.id.clockinbrk:
				whatclick = 3;
				LoginDialog();
				break;
			case R.id.clockoutday:
				whatclick = 4;
				LoginDialog();
				break;
			case R.id.clockoutbrk:
				whatclick = 2;
				LoginDialog();
				break;
			case R.id.btn_back:
				finish();
				break;
			default:
				break;
			}
		} else {
			onDetectNetworkState().show();
		}
	}

	private void LoginDialog() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(ClockinoutActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_login);
		final EditText et_username = (EditText) dialog
				.findViewById(R.id.et_login_username);
		final EditText et_password = (EditText) dialog
				.findViewById(R.id.et_login_password);
		Button btn_login = (Button) dialog.findViewById(R.id.btn_login);
		btn_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method
				// stub
				if (isInternetPresent) {
					if (et_username.getText().length() > 2
							&& et_password.getText().length() > 2) {
						username = et_username.getText().toString();
						password = et_password.getText().toString();
						dialog.dismiss();
						new LoginAsyn().execute();
					} else {
						SingleOptionAlertWithoutTitle.ShowAlertDialog(
								ClockinoutActivity.this, "WaterWorks",
								"Username/Password Is Invalid", "OK");
					}
				} else {
					onDetectNetworkState().show();
				}
			}
		});
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	// ////////////////////////////////////////////////Login///////////////////////////////////
	public class LoginAsyn extends AsyncTask<Void, Void, Void> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pDialog = new ProgressDialog(ClockinoutActivity.this);
			pDialog.setTitle("Authenticating...");
			pDialog.setMessage(Html.fromHtml("Loading wait..."));
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(AppConfig.NAMESPACE,
					AppConfig.Login_Method);
			// Adding Username and Password for Login Invok
			request.addProperty("username", username);
			request.addProperty("password", password);
			request.addProperty("deviceid", "");
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request", "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					AppConfig.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(AppConfig.Login_Action, envelope); // Calling
																				// Web
																				// service
				SoapObject response = (SoapObject) envelope.getResponse();
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1
						.getProperty(0);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				Log.i("Code", code);
				if (code.equals("000")) {
					login_status = true;
					Object mSoapObject3 = mSoapObject1.getProperty(1);
					Log.i(Tag, "mSoapObject3=" + mSoapObject3);
					String resp = mSoapObject3.toString();
					JSONObject jo = new JSONObject(resp);
					token = jo.getString("UserToken");
					userlevel = jo.getString("UserLevel");
					userid = jo.getString("UserId");
					
					
					if(userid.equals(WW_StaticClass.InstructorID)){
						WW_StaticClass.UserToken=token;
					}
					WW_StaticClass.CLOCKED_TODAY_INSERT=true;
					WW_StaticClass.UserToken_Extra=token;
					
				} else {
					login_status = false;
				}
			} catch (SocketException e) {
				connectionout = true;
				e.printStackTrace();
			} catch (SocketTimeoutException e) {
				connectionout = true;
				e.printStackTrace();
			} catch (JSONException e) {
				server_status = true;
				e.printStackTrace();
			} catch (Exception e) {
				server_status = true;
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (server_status) {
				onDetectNetworkState().show();
				server_status = false;
			} else if (connectionout) {
				connectionout = false;
				new LoginAsyn().execute();
			} else {
				if (login_status) {
					login_status = false;
					new CheckTimeClicks().execute();
					
				} else {
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							ClockinoutActivity.this, "LAFitnessApp",
							"Please Enter Valid Username and Password", "OK");
				}
			}
		}
	}

	private class CheckTimeClicks extends AsyncTask<Void, Void, Void> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pDialog = new ProgressDialog(ClockinoutActivity.this);
			pDialog.setTitle("Please wait...");
			pDialog.setMessage(Html.fromHtml("Loading data..."));
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
			ticktype = new ArrayList<String>();
			juid = new ArrayList<String>();
			jobtitle = new ArrayList<String>();
			Msg = new ArrayList<String>();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(AppConfig.NAMESPACE,
					AppConfig.CheckTimeClicks_Method);
			// Adding Username and Password for Login Invok
			request.addProperty("token", token);
			request.addProperty("userid", userid);
			request.addProperty("getticktype", whatclick);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request", "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					AppConfig.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(AppConfig.CheckTimeClicks_Action,
						envelope); // Calling
				// Web
				// service
				SoapObject response = (SoapObject) envelope.getResponse();
				Log.i(Tag, "Response = " + response);
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1
						.getProperty(0);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				Log.i("Code", code);
				if (code.equals("000")) {
					CheckTimeClicks_status = true;
					Object mSoapObject3 = mSoapObject1.getProperty(1);
					String resp = mSoapObject3.toString();
					JSONObject jo = new JSONObject(resp);
					if (jo.getString("WU_Att").toString().equalsIgnoreCase("1")) {
						Attleft = true;
					} else {
						Attleft = false;
						JSONArray jsonArray = jo.getJSONArray("ClockTicks");
						Log.i(Tag, "" + jsonArray);
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject mJsonObject = jsonArray.getJSONObject(i);
							ticktype.add(mJsonObject.getString("ticktype"));
							juid.add(mJsonObject.getString("juid"));
							jobtitle.add(mJsonObject.getString("jobtitle"));
							Msg.add(mJsonObject.getString("Msg"));
						}
					}
				} else {
					CheckTimeClicks_status = false;
				}
			} catch (SocketException e) {
				connectionout = true;
				e.printStackTrace();
			} catch (SocketTimeoutException e) {
				connectionout = true;
				e.printStackTrace();

			} catch (JSONException e) {
				server_status = true;
				e.printStackTrace();

			} catch (Exception e) {
				server_status = true;
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (server_status) {
				onDetectNetworkState().show();
				server_status = false;
			} else if (connectionout) {
				connectionout = false;
				new CheckTimeClicks().execute();
			} else {
				if (CheckTimeClicks_status) {
					CheckTimeClicks_status = false;
					try {
						LinearLayout v = null;
						if (whatclick == 1) {
							v = ll_clockinday;
							ll_clockinbrk.removeAllViews();
							ll_clockinday.removeAllViews();
							ll_clockoutbrk.removeAllViews();
							ll_clockoutday.removeAllViews();
						} else if (whatclick == 2) {
							v = ll_clockoutbrk;
							ll_clockinbrk.removeAllViews();
							ll_clockinday.removeAllViews();
							ll_clockoutbrk.removeAllViews();
							ll_clockoutday.removeAllViews();
						} else if (whatclick == 3) {
							v = ll_clockinbrk;
							ll_clockinbrk.removeAllViews();
							ll_clockinday.removeAllViews();
							ll_clockoutbrk.removeAllViews();
							ll_clockoutday.removeAllViews();
						} else if (whatclick == 4) {
							v = ll_clockoutday;
							ll_clockinbrk.removeAllViews();
							ll_clockinday.removeAllViews();
							ll_clockoutbrk.removeAllViews();
							ll_clockoutday.removeAllViews();
						}
						if (whatclick == 4) {
							if (Attleft) {
								Attleft = false;
//								Intent i = new Intent(ClockinoutActivity.this, ScheduleActivity.class);
								Intent i = new Intent(ClockinoutActivity.this, TodaysScheduleActivity.class);
								Toast.makeText(getApplicationContext(), "Please take your attendance before clocking out.", Toast.LENGTH_LONG).show();
								fromclock = true;
								WW_StaticClass.InstructorID = userid;
								i.putExtra("FROM", "CLOCK");
								startActivity(i);
							} else {
								if (Msg.get(0).toString().equalsIgnoreCase("")) {
									for (int i = 0; i < jobtitle.size(); i++) {
										View childView = getLayoutInflater()
												.inflate(
														R.layout.home_grid_item,
														v, false);
										final TextView tv_name = (TextView) childView
												.findViewById(R.id.text);
										// tv_name.setBackgroundColor(Color.GRAY);
										tv_name.setTag(juid.get(i));
										tv_name.setTextColor(getResources()
												.getColor(R.color.link));
										tv_name.setGravity(Gravity.LEFT
												| Gravity.CENTER_VERTICAL);
										tv_name.setText(Html.fromHtml("<u>"
												+ jobtitle.get(i) + "</u>"));
										tv_name.setEllipsize(TextUtils.TruncateAt.END);
										tv_name.setMaxLines(1);
										tv_name.setMinLines(1);
										tv_name.setHorizontallyScrolling(true);
										tv_name.setPadding(5, 10, 5, 10);
										tv_name.setOnClickListener(new OnClickListener() {

											@Override
											public void onClick(View v) {
												// TODO Auto-generated method
												// stub
												julinkid = tv_name.getTag()
														.toString();
												new ProcessTimeClicks()
														.execute();
											}
										});
										v.addView(childView);
									}
								} else {
									SingleOptionAlertWithoutTitle
											.ShowAlertDialog(
													ClockinoutActivity.this,
													"LAFitnessApp", Msg.get(0)
															.toString(), "Ok");
								}

							}
						} else {
							if (Msg.get(0).toString().equalsIgnoreCase("")) {
								for (int i = 0; i < jobtitle.size(); i++) {
									View childView = getLayoutInflater()
											.inflate(R.layout.home_grid_item,
													v, false);
									final TextView tv_name = (TextView) childView
											.findViewById(R.id.text);
									// tv_name.setBackgroundColor(Color.GRAY);
									tv_name.setTag(juid.get(i));
									tv_name.setTextColor(getResources()
											.getColor(R.color.link));
									tv_name.setGravity(Gravity.LEFT
											| Gravity.CENTER_VERTICAL);
									tv_name.setText(Html.fromHtml("<u>"
											+ jobtitle.get(i) + "</u>"));
									tv_name.setEllipsize(TextUtils.TruncateAt.END);
									tv_name.setMaxLines(1);
									tv_name.setMinLines(1);
									tv_name.setHorizontallyScrolling(true);
									tv_name.setPadding(5, 10, 5, 10);
									tv_name.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											// TODO Auto-generated method stub
											julinkid = tv_name.getTag()
													.toString();
											new ProcessTimeClicks().execute();
										}
									});
									v.addView(childView);
								}
							} else {
								SingleOptionAlertWithoutTitle.ShowAlertDialog(
										ClockinoutActivity.this,
										"LAFitnessApp", Msg.get(0).toString(),
										"Ok");
							}

						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				} else {
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							ClockinoutActivity.this, "LAFitnessApp",
							"Please login again to clock in / out.", "OK");
				}
			}
		}
	}

	private class ProcessTimeClicks extends AsyncTask<Void, Void, Void> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pDialog = new ProgressDialog(ClockinoutActivity.this);
			pDialog.setTitle("Please wait...");
			pDialog.setMessage(Html.fromHtml("Loading data..."));
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(AppConfig.NAMESPACE,
					AppConfig.ProcessTimeClicks_Method);
			// Adding Username and Password for Login Invok
			request.addProperty("token", token);
			request.addProperty("userid", userid);
			request.addProperty("TypeId", whatclick);
			request.addProperty("JuId", julinkid);

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request", "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					AppConfig.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(AppConfig.ProcessTimeClicks_Action,
						envelope); // Calling
				// Web
				// service
				SoapObject response = (SoapObject) envelope.getResponse();
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1
						.getProperty(0);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				Log.i("Code", code);
				if (code.equals("000")) {
					CheckTimeClicks_status = true;
					Object mSoapObject3 = mSoapObject1.getProperty(1);
					String resp = mSoapObject3.toString();
					JSONObject jo = new JSONObject(resp);
					JSONArray jsonArray = jo.getJSONArray("ClockTicks");
					Log.i(Tag, "" + jsonArray);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject mJsonObject = jsonArray.getJSONObject(i);
						processMsg = mJsonObject.getString("Msg");
						WU_Att = mJsonObject.getString("WU_Att");
					}

				} else {
					CheckTimeClicks_status = false;
				}
			} catch (SocketException e) {
				connectionout = true;
				e.printStackTrace();
			} catch (SocketTimeoutException e) {
				connectionout = true;
				e.printStackTrace();

			} catch (JSONException e) {
				server_status = true;
				e.printStackTrace();

			} catch (Exception e) {
				server_status = true;
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if (server_status) {
				server_status = false;
				onDetectNetworkState().show();
			} else if (connectionout) {
				connectionout = false;
				new ProcessTimeClicks().execute();
			} else {
				if (CheckTimeClicks_status) {
					CheckTimeClicks_status = false;
					if (whatclick == 4) {
						if (WU_Att.toString().equalsIgnoreCase("1")) {
//							Intent i = new Intent(ClockinoutActivity.this, ScheduleActivity.class);
							Intent i = new Intent(ClockinoutActivity.this, TodaysScheduleActivity.class);
							Toast.makeText(getApplicationContext(), "Please take your attendance before clocking out.", Toast.LENGTH_LONG).show();
							WW_StaticClass.InstructorID = userid;
							fromclock = true;
							i.putExtra("FROM", "CLOCK");
							startActivity(i);
						} else {
							AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
									ClockinoutActivity.this);
							alertDialogBuilder.setCancelable(false);
							// set title
							alertDialogBuilder.setTitle("LAFitnessApp");
							alertDialogBuilder.setIcon(getResources()
									.getDrawable(R.drawable.logo));

							// set dialog message
							alertDialogBuilder
									.setMessage(processMsg)
									.setCancelable(false)
									.setPositiveButton(
											"Ok",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													recreate();
												}
											});

							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder
									.create();

							// show it
							alertDialog.show();
						}
					}else{
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								ClockinoutActivity.this);
						alertDialogBuilder.setCancelable(false);
						// set title
						alertDialogBuilder.setTitle("LAFitnessApp");
						alertDialogBuilder.setIcon(getResources()
								.getDrawable(R.drawable.logo));

						// set dialog message
						alertDialogBuilder
								.setMessage(processMsg)
								.setCancelable(false)
								.setPositiveButton(
										"Ok",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												recreate();
											}
										});

						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder
								.create();

						// show it
						alertDialog.show();
					}
					
				} else {
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							ClockinoutActivity.this, "LAFitnessApp",
							"please login again to clock in / out.", "Ok");
				}
			}

		}
	}
}
