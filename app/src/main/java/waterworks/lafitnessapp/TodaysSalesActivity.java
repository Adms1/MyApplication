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
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TodaysSalesActivity extends Activity implements OnClickListener {
	boolean connectionout = false, isInternetPresent = false,
			server_status = false, getsitelist = false, getdata = false,
			login_status = false;
	TextView mtv_name, mtv_day, mtv_date;
	Button Btn_logout;
	private static String Tag = "Todays Sales";
	ArrayList<String> SiteID, SiteName;
	ListPopupWindow PW_sitename;
	ArrayList<String> wu_invoicedate, WU_ProductName, ClientName, wu_qty,
			wu_DetailsID, wu_delievered;
	Button btn_site_selection, btn_start_date, btn_end_date, btn_search;
	int mYEAR;
	int mMONTH;
	int mDAY;
	int siteid;
	String startday, startmonth, startyear, endday, endmonth, endyear;
	ListView lv_sales;
	String detailsid, username, password, token, userid, Msg;
	LinearLayout ll_body;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todays_sales);
		isInternetPresent = Utility
				.isNetworkConnected(TodaysSalesActivity.this);
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		} else {
			Initialization();
			new GetSiteList().execute();
			Calendar c = Calendar.getInstance();
			// get current date
			mYEAR = c.get(Calendar.YEAR);
			mMONTH = c.get(Calendar.MONTH);
			mDAY = c.get(Calendar.DAY_OF_MONTH);
			btn_site_selection.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					try {
						PW_sitename.show();
					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			});
			final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

				private Calendar c;

				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					mYEAR = year;
					mMONTH = monthOfYear + 1;
					mDAY = dayOfMonth;
					c = Calendar.getInstance();
					// if(mDAY >= c.get(Calendar.DAY_OF_MONTH) && (mMONTH + 1)
					// >= c.get(Calendar.MONTH)
					// && mYEAR >= c.get(Calendar.YEAR)){
					String d, m, y;
					d = Integer.toString(mDAY);
					m = Integer.toString(mMONTH);
					y = Integer.toString(mYEAR);
					if (mDAY < 10) {
						d = "0" + d;
					}
					if (mMONTH < 10) {
						m = "0" + m;
					}

					startday = d;
					startmonth = m;
					startyear = y;
					btn_start_date.setText(m + "/" + d + "/" + y);
					Log.v("daySelected2", "" + d);
					Log.v("monthSelected2", "" + m);
					Log.v("yearSelected2", "" + y);
					// }
					// else{
					// Toast.makeText(getActivity(),
					// "You can not select past date for order", 1).show();
					// }
					// updateDisplay(mMONTH,mDAY,mYEAR);
				}
			};
			final DatePickerDialog.OnDateSetListener mDateSetListener1 = new DatePickerDialog.OnDateSetListener() {

				private Calendar c;

				public void onDateSet(DatePicker view, int year,
						int monthOfYear, int dayOfMonth) {
					mYEAR = year;
					mMONTH = monthOfYear + 1;
					mDAY = dayOfMonth;
					c = Calendar.getInstance();
					// if(mDAY <= c.get(Calendar.DAY_OF_MONTH) && (mMONTH ) <=
					// c.get(Calendar.MONTH)
					// && mYEAR <= c.get(Calendar.YEAR)){
					String d, m, y;
					d = Integer.toString(mDAY);
					m = Integer.toString(mMONTH);
					y = Integer.toString(mYEAR);
					if (mDAY < 10) {
						d = "0" + d;
					}
					if (mMONTH < 10) {
						m = "0" + m;
					}

					endday = d;
					endmonth = m;
					endyear = y;
					btn_end_date.setText(m + "/" + d + "/" + y);
					Log.v("daySelected2", "" + d);
					Log.v("monthSelected2", "" + m);
					Log.v("yearSelected2", "" + y);
					// }
					// else{
					// Toast.makeText(getActivity(),
					// "You can not select past date for order", 1).show();
					// }
					// updateDisplay(mMONTH,mDAY,mYEAR);
				}
			};
			btn_start_date.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					btn_end_date.setText("");
					if(btn_start_date.getText().toString().isEmpty()){
						
					}else{
						mYEAR = Integer.parseInt(startyear);
						mMONTH = Integer.parseInt(startmonth)-1;
						mDAY = Integer.parseInt(startday);
					}
					DatePickerDialog mDialog = new DatePickerDialog(
							TodaysSalesActivity.this, mDateSetListener, mYEAR,
							mMONTH, mDAY);
					mDialog.getDatePicker().setCalendarViewShown(false);
					mDialog.getDatePicker().setMaxDate(
							System.currentTimeMillis());
					// mDialog.getDatePicker().setMinDate(System.currentTimeMillis());
					mDialog.show();
					
				}
			});
			btn_end_date.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (btn_start_date.getText().toString().isEmpty()) {
						Toast.makeText(TodaysSalesActivity.this,
								"Please select start date first", 1).show();
					} else {
						try {
							Calendar minDate = Calendar.getInstance();
							minDate.set(Integer.parseInt(startyear),
									(Integer.parseInt(startmonth) - 1),
									Integer.parseInt(startday), 00, 00, 01);
							DatePickerDialog mDialog1 = new DatePickerDialog(
									TodaysSalesActivity.this,
									mDateSetListener1, Integer
											.parseInt(startday), Integer
											.parseInt(startmonth), Integer
											.parseInt(startyear));
							mDialog1.getDatePicker()
									.setCalendarViewShown(false);
							mDialog1.getDatePicker().setMinDate(
									minDate.getTimeInMillis());
							if (System.currentTimeMillis() != minDate
									.getTimeInMillis()) {
								mDialog1.getDatePicker().setMaxDate(
										System.currentTimeMillis());
							}
							mDialog1.show();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
			btn_search.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (isInternetPresent) {
						if (btn_start_date.getText().toString().isEmpty()) {
							Toast.makeText(TodaysSalesActivity.this,
									"Please select start date", 1).show();
						} else if (btn_end_date.getText().toString().isEmpty()) {
							Toast.makeText(TodaysSalesActivity.this,
									"Please select end date", 1).show();
						} else {
							new TodaysSales().execute();
						}
					} else {
						onDetectNetworkState().show();
					}
				}
			});

		}
	}

	private void Initialization() {
		// TODO Auto-generated method stub
		mtv_name = (TextView) findViewById(R.id.tv_app_inst_name);
		mtv_day = (TextView) findViewById(R.id.tv_app_day);
		mtv_date = (TextView) findViewById(R.id.tv_app_date);
		Btn_logout = (Button) findViewById(R.id.btn_app_logoff);
		Btn_logout.setVisibility(View.GONE);
		btn_end_date = (Button) findViewById(R.id.btn_end_date);
		btn_site_selection = (Button) findViewById(R.id.btn_select_site);
		btn_start_date = (Button) findViewById(R.id.btn_start_date);
		btn_search = (Button) findViewById(R.id.btn_search);
		PW_sitename = new ListPopupWindow(TodaysSalesActivity.this);
		lv_sales = (ListView) findViewById(R.id.lv_todays_sales);
		wu_invoicedate = new ArrayList<String>();
		WU_ProductName = new ArrayList<String>();
		ClientName = new ArrayList<String>();
		wu_qty = new ArrayList<String>();
		wu_DetailsID = new ArrayList<String>();
		wu_delievered = new ArrayList<String>();
		ll_body = (LinearLayout)findViewById(R.id.ll_body);
		ll_body.setVisibility(View.GONE);
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
		isInternetPresent = Utility
				.isNetworkConnected(TodaysSalesActivity.this);
		if (!isInternetPresent) {
			onDetectNetworkState().show();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}

	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(
				TodaysSalesActivity.this);
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
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;

		default:
			break;
		}
	}

	private class GetSiteList extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(AppConfig.NAMESPACE,
					AppConfig.GetSiteList_Method);
			request.addProperty("token", WW_StaticClass.UserToken);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request", "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					AppConfig.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(AppConfig.GetSiteList_Action,
						envelope); // Calling
									// Web
									// service
				SoapObject response = (SoapObject) envelope.getResponse();
				// SoapPrimitive response = (SoapPrimitive)
				// envelope.getResponse();
				Log.i(Tag, "" + response.toString());
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				Log.i(Tag, "mSoapObject1=" + mSoapObject1);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1
						.getProperty(0);
				Log.i(Tag, "mSoapObject2=" + mSoapObject2);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				Log.i("Code", code);
				if (code.equalsIgnoreCase("000")) {
					getsitelist = true;
					Object mSoapObject3 = mSoapObject1.getProperty(1);
					Log.i(Tag, "mSoapObject3=" + mSoapObject3);
					String resp = mSoapObject3.toString();
					JSONObject jo = new JSONObject(resp);
					JSONArray jArray = jo.getJSONArray("Sites");
					Log.i(Tag, "jArray : " + jArray.toString());
					SiteID = new ArrayList<String>();
					SiteName = new ArrayList<String>();
					JSONObject jsonObject;
					for (int i = 0; i < jArray.length(); i++) {
						jsonObject = jArray.getJSONObject(i);
						SiteName.add(jsonObject.getString("SiteName"));
						SiteID.add(jsonObject.getString("SiteID"));
					}
				} else {
					getsitelist = false;
				}
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				connectionout = true;
			} catch (SocketException e) {
				e.printStackTrace();
				connectionout = true;

			} catch (JSONException e) {
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
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (server_status) {
				SingleOptionAlertWithoutTitle.ShowAlertDialog(
						TodaysSalesActivity.this, "Error", "SERVER DOWN", "OK");
				server_status = false;
			} else if (connectionout) {
				connectionout = false;
				new GetSiteList().execute();
			} else {
				if (!getsitelist) {
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							TodaysSalesActivity.this, "Error",
							"No site found.", "OK");
				} else {
					getsitelist = false;
					PW_sitename.setAdapter(new ArrayAdapter<String>(
							getApplicationContext(), R.layout.edittextpopup,
							SiteName));
					PW_sitename.setAnchorView(btn_site_selection);
					PW_sitename.setHeight(LayoutParams.WRAP_CONTENT);
					PW_sitename.setModal(true);
					PW_sitename
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int pos, long id) {
									// TODO Auto-generated method stub
									View v = findViewById(R.id.btn_cm_to);

									btn_site_selection.setText(SiteName
											.get(pos));
									siteid = Integer.parseInt(SiteID.get(pos));
									PW_sitename.dismiss();
								}
							});
				}
			}
		}
	}

	private class TodaysSales extends AsyncTask<Void, Void, Void> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			pDialog = new ProgressDialog(TodaysSalesActivity.this);
			pDialog.setTitle("Please wait...");
			pDialog.setMessage(Html.fromHtml("Loading sales report..."));
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
			wu_delievered.clear();
			wu_DetailsID.clear();
			wu_invoicedate.clear();
			wu_qty.clear();
			WU_ProductName.clear();
			ClientName.clear();
			lv_sales.setAdapter(null);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(AppConfig.NAMESPACE,
					AppConfig.GetSales_Method);
			// Adding Username and Password for Login Invok
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("startdate", btn_start_date.getText()
					.toString());
			request.addProperty("enddate", btn_end_date.getText().toString());
			request.addProperty("siteid", siteid);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request", "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					AppConfig.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(AppConfig.GetSales_Action, envelope); // Calling
				SoapObject response = (SoapObject) envelope.getResponse();
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1
						.getProperty(0);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				Log.i("Code", code);
				if (code.equals("000")) {
					
					Object mSoapObject3 = mSoapObject1.getProperty(1);
					String resp = mSoapObject3.toString();
					JSONObject jo = new JSONObject(resp);
					JSONArray jsonArray = jo.getJSONArray("Sales");
					Log.i(Tag, "" + jsonArray);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject mJsonObject = jsonArray.getJSONObject(i);
						wu_invoicedate.add(mJsonObject
								.getString("wu_invoicedate"));
						WU_ProductName.add(mJsonObject
								.getString("WU_ProductName"));
						ClientName.add(mJsonObject.getString("ClientName"));
						wu_qty.add(mJsonObject.getString("wu_qty"));
						wu_DetailsID.add(mJsonObject.getString("wu_DetailsID"));
						wu_delievered.add(mJsonObject
								.getString("wu_delievered"));
					}
					getdata = true;
				} else {
					getdata = false;
				}

			} catch (Exception e) {
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
				new TodaysSales().execute();
			} else {
				if (getdata) {
					getdata = false;
					if (wu_delievered.isEmpty()) {
						ll_body.setVisibility(View.GONE);
						SingleOptionAlertWithoutTitle.ShowAlertDialog(
								TodaysSalesActivity.this, "LAFitnessApp",
								"No sales reports found", "Ok");
					} else {
						ll_body.setVisibility(View.VISIBLE);
						lv_sales.setAdapter(new TodaysAdapter(
								TodaysSalesActivity.this));
					}
				} else {
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							TodaysSalesActivity.this, "LAFitnessApp",
							"No sales reports found", "Ok");
				}
			}
		}
	}

	public class TodaysAdapter extends BaseAdapter {

		Context context;

		public TodaysAdapter(Context context) {
			super();
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return wu_DetailsID.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return WU_ProductName.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public int getViewTypeCount() {

			return getCount();
		}

		@Override
		public int getItemViewType(int position) {

			return position;
		}

		public class ViewHolder {
			TextView tv_date, tv_productname, tv_clientname, tv_qty;
			Button btn_deliver;
		}

		private int[] colors = new int[] { Color.parseColor("#EEEEEE"),
				Color.parseColor("#FFFFFF") };

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.todayssales_item, null);
				int colorpos = position % colors.length;
				convertView.setBackgroundColor(colors[colorpos]);
				holder.tv_date = (TextView) convertView
						.findViewById(R.id.tv_invoice_date);
				holder.tv_productname = (TextView) convertView
						.findViewById(R.id.tv_productname);
				holder.tv_clientname = (TextView) convertView
						.findViewById(R.id.tv_cilentname);
				holder.tv_qty = (TextView) convertView
						.findViewById(R.id.tv_qty);
				holder.btn_deliver = (Button) convertView
						.findViewById(R.id.btn_deliver);
				holder.btn_deliver.setEnabled(false);
				holder.tv_date.setText(wu_invoicedate.get(position));
				holder.tv_productname.setText(WU_ProductName.get(position));
				holder.tv_clientname.setText(ClientName.get(position));
				holder.tv_qty.setText(wu_qty.get(position));

				if (wu_delievered.get(position).toString()
						.equalsIgnoreCase("1")) {
					holder.btn_deliver.setBackgroundColor(Color.TRANSPARENT);
					holder.btn_deliver.setTextColor(Color.BLACK);
					holder.btn_deliver.setText("Delivered");
					holder.btn_deliver.setEnabled(false);
				} else {
					holder.btn_deliver.setEnabled(true);
					detailsid = wu_DetailsID.get(position);
					holder.btn_deliver
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									LoginDialog();
								}
							});
				}
			}
			return convertView;
		}

		public class LoginAsyn extends AsyncTask<Void, Void, Void> {
			ProgressDialog pDialog;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				pDialog = new ProgressDialog(TodaysSalesActivity.this);
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
						SoapEnvelope.VER11); // Make an Envelop for sending as
												// whole
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
					SoapObject mSoapObject1 = (SoapObject) response
							.getProperty(0);
					SoapObject mSoapObject2 = (SoapObject) mSoapObject1
							.getProperty(0);
					String code = mSoapObject2.getPropertyAsString(0)
							.toString();
					Log.i("Code", code);
					if (code.equals("000")) {
						login_status = true;
						Object mSoapObject3 = mSoapObject1.getProperty(1);
						Log.i(Tag, "mSoapObject3=" + mSoapObject3);
						String resp = mSoapObject3.toString();
						JSONObject jo = new JSONObject(resp);
						token = jo.getString("UserToken");
						userid = jo.getString("UserId");
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
						new SendData().execute();

					} else {
						SingleOptionAlertWithoutTitle.ShowAlertDialog(context,
								"LAFitnessApp",
								"Please Enter Valid Username and Password",
								"OK");
					}
				}
			}
		}

		private void LoginDialog() {
			// TODO Auto-generated method stub
			final Dialog dialog = new Dialog(context);
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
									context, "WaterWorks",
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

		private class SendData extends AsyncTask<Void, Void, Void> {
			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				SoapObject request = new SoapObject(AppConfig.NAMESPACE,
						AppConfig.InsertProductDelievered_Method);
				// Adding Username and Password for Login Invok
				request.addProperty("token", token);
				request.addProperty("userid", userid);
				request.addProperty("detailsid", detailsid);
				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11); // Make an Envelop for sending as
												// whole
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				Log.i("Request", "Request = " + request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						AppConfig.SOAP_ADDRESS);
				try {
					androidHttpTransport.call(
							AppConfig.InsertProductDelievered_Action, envelope); // Calling
					// Web
					// service
					SoapObject response = (SoapObject) envelope.getResponse();
					SoapObject mSoapObject1 = (SoapObject) response
							.getProperty(0);
					SoapObject mSoapObject2 = (SoapObject) mSoapObject1
							.getProperty(0);
					String code = mSoapObject2.getPropertyAsString(0)
							.toString();
					Log.i("Code", code);
					if (code.equals("000")) {
						getdata = true;
						Object mSoapObject3 = mSoapObject1.getProperty(1);
						String resp = mSoapObject3.toString();
						JSONObject jo = new JSONObject(resp);
						JSONArray jsonArray = jo.getJSONArray("SalesLog");
						Log.i(Tag, "" + jsonArray);
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject mJsonObject = jsonArray.getJSONObject(i);
							Msg = mJsonObject.getString("Msg");
						}

					} else {
						getdata = false;
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

			@SuppressWarnings("deprecation")
			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (server_status) {
					server_status = false;
					onDetectNetworkState().show();
				} else if (connectionout) {
					connectionout = false;
					new SendData().execute();
				} else {
					if (getdata) {
						getdata = false;
						AlertDialog alertDialog = new AlertDialog.Builder(context).create();
						// hide title bar
						// alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						alertDialog.setTitle("LAFitnessApp");
						alertDialog.setIcon(context.getResources().getDrawable(R.drawable.logo));
						alertDialog.setCanceledOnTouchOutside(false); 
						alertDialog.setCancelable(false);
						// set the message
						alertDialog.setMessage(Msg);
						// set button1 functionality
						alertDialog.setButton("Ok",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {
										// close dialog
										dialog.cancel();
										new TodaysSales().execute();

									}
								});
						// show the alert dialog
						alertDialog.show();
					} else {
						SingleOptionAlertWithoutTitle.ShowAlertDialog(context,
								"LAFitnessApp",
								"Please login again to today's sales report.", "Ok");
					}
				}
			}
		}

	}

}
