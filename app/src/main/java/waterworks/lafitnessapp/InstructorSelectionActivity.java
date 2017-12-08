package waterworks.lafitnessapp;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class InstructorSelectionActivity extends Activity implements OnClickListener{
	public static final String TAG = "Instructor Selection";
	TextView mtv_name,mtv_day,mtv_date;
	Boolean isInternetPresent = false;
	boolean server_response = false;
	boolean getinstructor =false,other_problem=false;
	GridView grid_all_inst;
	AllInstAdapter adapter;
	Button btn_app_logoff;
	public ArrayList<String> UserId, UserName;
	String FROM;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_instructor_selection);
		isInternetPresent = Utility
				.isNetworkConnected(InstructorSelectionActivity.this);
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}else{
			FROM = getIntent().getStringExtra("FROM");
			Initialize();
			Calendar c = Calendar.getInstance(); 
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
			new GetAllInstructor().execute();
		}
	}
	private void Initialize() {
		// TODO Auto-generated method stub
		mtv_date = (TextView)findViewById(R.id.tv_app_date);
		mtv_day = (TextView)findViewById(R.id.tv_app_day);
		mtv_name = (TextView)findViewById(R.id.tv_app_inst_name);
		grid_all_inst = (GridView)findViewById(R.id.grid_all_inst);
		btn_app_logoff = (Button) findViewById(R.id.btn_app_logoff);
		btn_app_logoff.setText("");
		btn_app_logoff.setVisibility(View.GONE);
	}
	public AlertDialog onDetectNetworkState(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setIcon(getResources().getDrawable(R.drawable.logo));
		builder1.setMessage("Please turn on internet connection and try again.")
		.setTitle("No Internet Connection.")
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        // TODO Auto-generated method stub
		        finish();
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
				.isNetworkConnected(InstructorSelectionActivity.this);
		if(!isInternetPresent){
			onDetectNetworkState().show();
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}
	String currentDateandTime;
	ProgressDialog pDialog;
	private class GetAllInstructor extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
			currentDateandTime = format.format(date);
			pDialog = new ProgressDialog(InstructorSelectionActivity.this);
			pDialog.setTitle("Loading...");
			pDialog.setMessage(Html.fromHtml("Fetching Instructors..."));
			pDialog.setIndeterminate(true);
			pDialog.setCancelable(false);
			pDialog.show();
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
			pDialog.dismiss();
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
				} else {
					getinstructor = false;
					adapter = new AllInstAdapter(getApplicationContext(), UserName, UserId);
					grid_all_inst.setAdapter(adapter);
					grid_all_inst.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							Intent i = new Intent(getApplicationContext(), ScheduleActivity.class);
							i.putExtra("name", UserName.get(position));
							i.putExtra("id", UserId.get(position));
							i.putExtra("FROM", FROM);
							startActivity(i);
						}
					});
				}
			}
		}
	}
	public class AllInstAdapter extends BaseAdapter{
		private Context mContext;
		 ArrayList<String> Uname,Uid;
		public AllInstAdapter(Context mContext, ArrayList<String> uname,
				ArrayList<String> uid) {
			super();
			this.mContext = mContext;
			Uname = uname;
			Uid = uid;
		}
		@Override
	    public int getCount() {
	      // TODO Auto-generated method stub
	      return Uid.size();
	    }
	    @Override
	    public Object getItem(int position) {
	      // TODO Auto-generated method stub
	      return null;
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
		
		public class ViewHolder{
			TextView tv_name;
		}
	    @Override
	    public View getView(int position, View convertView, ViewGroup parent) {
	      // TODO Auto-generated method stub
	    	final ViewHolder holder;
			try{
				if (convertView == null) {
					holder = new ViewHolder();
					convertView = LayoutInflater.from(parent.getContext()).inflate(
							R.layout.home_grid_item, null);
					convertView.setBackgroundColor(Color.rgb(117, 117, 117));
					holder.tv_name = (TextView)convertView.findViewById(R.id.text);
					holder.tv_name.setTextColor(Color.WHITE);
					holder.tv_name.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
					holder.tv_name.setText(Uname.get(position));
					holder.tv_name.setEllipsize(TextUtils.TruncateAt.END);
					holder.tv_name.setMaxLines(1);
					holder.tv_name.setMinLines(1);
					holder.tv_name.setHorizontallyScrolling(true);
					holder.tv_name.setPadding(5, 10, 5, 10);
				}
				else {
					holder = (ViewHolder) convertView.getTag();
				}
	            
			}
			catch(OutOfMemoryError e){
				e.printStackTrace();
			}
			catch(IndexOutOfBoundsException e){
				e.printStackTrace();
			}
			catch(NullPointerException e){
				e.printStackTrace();
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
			return convertView;
	    }
	    
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

}
