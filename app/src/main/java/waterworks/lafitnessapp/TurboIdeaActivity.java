package waterworks.lafitnessapp;

import java.net.SocketException;
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
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.TextView;

public class TurboIdeaActivity extends Activity implements OnClickListener{

	int SiteID;
	boolean server_response = false;
	boolean getsitelist = false,connection = false;
	boolean sended = false;
	String sendinfo="";
	TextView mtv_name,mtv_day,mtv_date,mtv_time;
	String time;
	public static String TAG="TurboIdeaActivity";
	String am_pm;
	java.util.Date noteTS;
	SharedPreferences mPreferences;
	Boolean isInternetPresent = false;
	ArrayList<String>Siteid,Sitename;
	ListPopupWindow sitenameList;
	Button btn_tidea_site;
	EditText et_message_body,et_idea_tital;
	Thread t;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_turbo_idea);
		
		mPreferences = PreferenceManager
				.getDefaultSharedPreferences(TurboIdeaActivity.this);
		isInternetPresent = Utility
				.isNetworkConnected(TurboIdeaActivity.this);
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}
		Calendar c = Calendar.getInstance(); 
		int hour = c.get(Calendar.HOUR);
		int min = c.get(Calendar.MINUTE);
		int Day_Name = c.get(Calendar.DAY_OF_WEEK);
		int Date =  c.get(Calendar.DATE);
		int Month = c.get(Calendar.MONTH);
		String day_name = null;
		if(Day_Name == 1){
			day_name = "SUN";
		}
		else if(Day_Name == 2){
			day_name = "MON";
		}
		else if(Day_Name == 3){
			day_name = "TUES";
		}
		else if(Day_Name == 4){
			day_name = "WED";
		}
		else if(Day_Name == 5){
			day_name = "THUR";
		}
		else if(Day_Name == 6){
			day_name = "FRI";
		}
		else if(Day_Name == 7){
			day_name = "SAT";
		}
		Log.i("Time", "Time = " + hour + ":" + min + " "  + " " + day_name + " " + Date + "/" + Month);
		//////////////
		initialize();
		///////////////////////
		
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
	                            
	                    		if(AM_PM == 0){
	                    			am_pm = "AM";
	                    		}
	                    		else{
	                    			am_pm = "PM";
	                    		} 
	                        }

							private void updateTextView() {
								// TODO Auto-generated method stub
								noteTS = Calendar.getInstance().getTime();

							    time = "hh:mm"; // 12:00
							    mtv_time.setText(DateFormat.format(time, noteTS)+" "+am_pm);
							}
	                    });
	                }
	            } catch (InterruptedException e) {
	            }
	        }
	    };

	    t.start();
	
		mtv_name.setText( WW_StaticClass.UserName);
		mtv_day.setText(day_name);
		mtv_date.setText(Month+1 + "/" + Date);
		new GetSite().execute();

	}
	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
		builder1.setMessage("Please turn on internet connection and try again.")
		.setTitle("No Internet Connection.")
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // TODO Auto-generated method stub
		    	t.interrupt();
		        TurboIdeaActivity.this.finish();
		    }
		})       
		.setPositiveButton("Οk",new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // TODO Auto-generated method stub
		        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
		    }
		});
		    return builder1.create();
	}
	
	private void initialize() {
		mtv_date = (TextView)findViewById(R.id.tv_app_date);
		mtv_day = (TextView)findViewById(R.id.tv_app_day);
		mtv_name = (TextView)findViewById(R.id.tv_app_inst_name);
		mtv_time = (TextView)findViewById(R.id.tv_app_time);
		btn_tidea_site = (Button)findViewById(R.id.btn_tidea_site);
		sitenameList = new ListPopupWindow(getApplicationContext());
		et_idea_tital = (EditText)findViewById(R.id.et_tide_title);
		et_message_body = (EditText)findViewById(R.id.et_tidea_msg);
	}

ProgressDialog pDialog;
	private class GetSite extends AsyncTask<Void, Void, Void>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(TurboIdeaActivity.this);
			pDialog.setTitle("Loading wait...");
			pDialog.setMessage(Html.fromHtml("Fetching sites..."));
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
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
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					AppConfig.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(AppConfig.GetSiteList_Action,
						envelope); // Calling Web service
				SoapObject response = (SoapObject)envelope.getResponse();
				 Log.i(TAG,"" + response.toString());
				 SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				 Log.i(TAG, "mSoapObject1="+mSoapObject1);
				 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				 Log.i(TAG, "mSoapObject2="+mSoapObject2);
				 String code = mSoapObject2.getPropertyAsString(0).toString();
				 Log.i("Code", code);
				if(code.equalsIgnoreCase("000")){
					getsitelist = true;
					Object mSoapObject3 =  mSoapObject1.getProperty(1);
					Log.i(TAG, "mSoapObject3="+mSoapObject3);
					String resp = mSoapObject3.toString();
					JSONObject jo = new JSONObject(resp);
					JSONArray jArray = jo.getJSONArray("Sites");
					Log.i(TAG,"jArray : " + jArray.toString());
					Siteid = new ArrayList<String>();
					Sitename = new ArrayList<String>();
					JSONObject jsonObject ;
					for(int i=0;i<jArray.length();i++){
						jsonObject = jArray.getJSONObject(i);
						Sitename.add(jsonObject.getString("SiteName"));
						Siteid.add(jsonObject.getString("SiteID"));
					}
					Log.i(TAG, "id = "+Siteid);
					Log.i(TAG, "name = "+Sitename);
					sitenameList.setAdapter(new ArrayAdapter<String>(
				            getApplicationContext(),
				            R.layout.edittextpopup,Sitename ));
					sitenameList.setAnchorView(btn_tidea_site);
					sitenameList.setHeight(300);
					sitenameList.setModal(true);
					sitenameList.setOnItemClickListener(
			            new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> parent, View view,
									int pos, long id) {
								// TODO Auto-generated method stub
								btn_tidea_site.setText(Sitename.get(pos));
								SiteID = Integer.parseInt(Siteid.get(pos));
								sitenameList.dismiss();
							}
						});

				}
				else{
					getsitelist =false;
				}
			}
			catch(JSONException e){
				e.printStackTrace();
				server_response =true;
			}
			catch(Exception e){
				server_response =true;
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog.dismiss();
			if(server_response){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(
						TurboIdeaActivity.this,"WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
				server_response = false;
			}
			else{
				if(!getsitelist){
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							TurboIdeaActivity.this,"WaterWorks",
							"No site found.", "OK");
				}
				else{
					getsitelist = false;
				}
			}
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
		isInternetPresent = Utility
				.isNetworkConnected(TurboIdeaActivity.this); 
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		t.interrupt();
		finish();
		Intent it = new Intent(TurboIdeaActivity.this, DashBoardActivity.class);
		startActivity(it);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (isInternetPresent) {
			switch (v.getId()) {
			case R.id.btn_back:
				t.interrupt();
				finish();
				Intent it = new Intent(TurboIdeaActivity.this, DashBoardActivity.class);
				startActivity(it);
				break;
			case R.id.btn_app_logoff:
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						TurboIdeaActivity.this);
				// set title
				alertDialogBuilder.setTitle("WaterWorks");
				alertDialogBuilder.setIcon(getResources().getDrawable(R.drawable.ic_launcher));

				// set dialog message
				alertDialogBuilder
						.setMessage("Are you sure you want to logout ?")
						.setCancelable(false)
						.setPositiveButton("Logout",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										mPreferences.edit().clear();
										mPreferences.edit().commit();
										WW_StaticClass.InstructorID="";
										t.interrupt();
										finish();
										
										Intent loginIntent= new Intent(TurboIdeaActivity.this, LoginActivity.class);
										loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
										loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
										
										startActivity(loginIntent);
										
									}
								})
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
				break;

			case R.id.btn_tidea_site:
					sitenameList.show();
				break;
			case R.id.btn_tidea_send:
				if(btn_tidea_site.getText().toString().isEmpty()||btn_tidea_site.getText().toString().equalsIgnoreCase("")){
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							TurboIdeaActivity.this, "WaterWorks", "Please select site.", "Ok");
				}
				else if(et_idea_tital.getText().toString().isEmpty()||et_idea_tital.getText().toString().equalsIgnoreCase("")){
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							TurboIdeaActivity.this, "WaterWorks", "Please enter title.", "Ok");
				}
				else if(et_message_body.getText().toString().isEmpty()||et_message_body.getText().toString().equalsIgnoreCase("")){
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							TurboIdeaActivity.this, "WaterWorks", "Please enter idea.", "Ok");
				}
				else{
					new SendIdea().execute();
				}
				break;
			default:
				break;
			}
			}else {
				// Internet connection is not present
				onDetectNetworkState().show();
			}
		}
	ProgressDialog pDialog3;
	private class SendIdea extends AsyncTask<Void, Void, Void>{
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
			pDialog3 = new ProgressDialog(TurboIdeaActivity.this);
			pDialog3.setTitle(Html.fromHtml("Loading wait..."));
			pDialog.setMessage("Sending...");
			pDialog3.setIndeterminate(true);
			pDialog3.setCancelable(false);
			pDialog3.show();
		}
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(AppConfig.NAMESPACE,
					AppConfig.Insert_TurboFlash_Method);
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("siteid", SiteID);
			request.addProperty("subject", et_idea_tital.getText().toString());
			request.addProperty("message",et_message_body.getText().toString());
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request",  "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					AppConfig.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(AppConfig.Insert_TurboFlash_Action,
						envelope); // Calling Web service
				SoapObject response = (SoapObject)envelope.getResponse();
				 Log.i(TAG,"" + response.toString());
				 SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				 Log.i(TAG, "mSoapObject1="+mSoapObject1);
				 SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
				 Log.i(TAG, "mSoapObject2="+mSoapObject2);
				 String code = mSoapObject2.getPropertyAsString(0).toString();
				 Log.i("Code", code);
				 if (code.equals("000")) {
					 sended= true;
					 Object mSoapObject3 =  mSoapObject1.getProperty(1);
						Log.i(TAG, "mSoapObject3="+mSoapObject3);
						String resp = mSoapObject3.toString();
						JSONObject jo = new JSONObject(resp);
						sendinfo = jo.getString("InstTerificTurbo");
				 }
				 else{
						sended= false;
					}


			}
			catch (SocketException e){
				e.printStackTrace();
				connection = true;
			}
			catch(JSONException e){
				e.printStackTrace();
				server_response = true;
			}
			catch(Exception e){
				e.printStackTrace();
				server_response = true;
			}
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pDialog3.dismiss();
			
			if(server_response){
				SingleOptionAlertWithoutTitle.ShowAlertDialog(
						TurboIdeaActivity.this,"WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
				server_response = false;
			}
			else if(connection){
				connection = false;
				new SendIdea().execute();
			}
			else{
				if (sended) {
					sended = false;
					AlertDialog.Builder alertdialogbuilder2 = new AlertDialog.Builder(
							TurboIdeaActivity.this);
					alertdialogbuilder2.setTitle("Waterworks")
					.setIcon(getResources().getDrawable(R.drawable.ic_launcher));
					alertdialogbuilder2
							.setMessage("Message sent successfully...")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.cancel();
											Intent newIntent = new Intent(TurboIdeaActivity.this, DashBoardActivity.class);
											startActivity(newIntent);
											t.interrupt();
											finish();
											}

									});
					AlertDialog alertDialog2 = alertdialogbuilder2.create();
					alertDialog2.show();


				} else {
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							TurboIdeaActivity.this,"WaterWorks",
							"Please send valid data.", "OK");
				}
			}
			

		}

	}

}
