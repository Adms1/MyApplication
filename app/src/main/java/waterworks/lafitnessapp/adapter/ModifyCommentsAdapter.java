package waterworks.lafitnessapp.adapter;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import waterworks.lafitnessapp.ModifyComments;

import waterworks.lafitnessapp.R;
import waterworks.lafitnessapp.TodaysScheduleFragment;
import waterworks.lafitnessapp.ViewCurrentScheduleFragment;
import waterworks.lafitnessapp.AppConfig;
import waterworks.lafitnessapp.WW_StaticClass;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ModifyCommentsAdapter extends BaseAdapter {

	public ModifyCommentsAdapter(ArrayList<String> commentsgetted,
			ArrayList<String> expdate, ArrayList<String> tbid,
			String studentid,String FROM, Context context) {
		super();
		this.commentsgetted = commentsgetted;
		this.expdate = expdate;
		this.tbid = tbid;
		this.context = context;
		this.studentid = studentid;
		this.FROM= FROM;
	}
	String FROM;
	boolean comment_status = false;
	boolean server_status = false;
	ArrayList<String> commentsgetted, expdate, tbid;
	Context context;

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return commentsgetted.size();
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

	public class ViewHolder {
		TextView tv_comment, tv_date_time;
		Button btn_delete_comment;
	}

	View temp_view;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		try {
			if (convertView == null) {
				holder = new ViewHolder();

				convertView = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.commentsraw, null);
				holder.tv_comment = (TextView) convertView
						.findViewById(R.id.tv_commnetsraw_comment);
				holder.tv_date_time = (TextView) convertView
						.findViewById(R.id.tv_commentsraw_date);
				holder.btn_delete_comment = (Button) convertView
						.findViewById(R.id.btn_delete_comment);

				holder.tv_comment.setText(commentsgetted.get(position));
				holder.tv_date_time.setText(expdate.get(position));
				holder.btn_delete_comment
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								whichdelete = tbid.get(position);
								new deletecomment().execute();
								temp_view = v;
								// ((Activity)v.getContext()).finish();
								/*
								 * Intent it = new Intent(v.getContext(),
								 * ViewCurrentLessonActivity.class); //
								 * it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								 * it.putExtra("DELETE", "YES");
								 * it.putExtra("HOUR",
								 * ViewCurrentLessonActivity.hour_for_data);
								 * it.putExtra("MIN",
								 * ViewCurrentLessonActivity.min_for_data);
								 * it.putExtra("DATE",
								 * ViewCurrentLessonActivity.date_for_data);
								 * v.getContext().startActivity(it);
								 */
							}
						});
			} else {
				holder = (ViewHolder) convertView.getTag();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	String whichdelete, studentid;
	public String insertionresponse;

	private class deletecomment extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(AppConfig.NAMESPACE,
					AppConfig.DeleteStudentCommentByID_Method);
			// Adding Username and Password for Login Invok
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("studentid", studentid);
			request.addProperty("tbid", whichdelete);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request", "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					AppConfig.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(
						AppConfig.DeleteStudentCommentByID_Action, envelope); // Calling
																				// Web
																				// service
				SoapObject response = (SoapObject) envelope.getResponse();
				Log.i("here", "Result : " + response.toString());
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				Log.i("Comment adapter", "mSoapObject1=" + mSoapObject1);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1
						.getProperty(0);
				Log.i("Comment adapter", "mSoapObject2=" + mSoapObject2);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				Log.i("Code", code);
				if (code.equals("000")) {
					comment_status = true;
					Object mSoapObject3 = mSoapObject1.getProperty(1);
					Log.i("Comment adapter", "mSoapObject3=" + mSoapObject3);
					insertionresponse = mSoapObject3.toString();

				} else {
					comment_status = false;
				}
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
				Toast.makeText(
						context,
						"Server not responding.\nPlease check internet connection or try after sometime.",
						1).show();
			} else {
				if (comment_status) {
					// Toast.makeText(context, insertionresponse, 1).show();
					AlertDialog alertDialog = new AlertDialog.Builder(
							temp_view.getContext()).create();
					// hide title bar
					// alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					alertDialog.setTitle("LAFitnessApp");
					alertDialog.setIcon(temp_view.getContext().getResources().getDrawable(
							R.drawable.logo));
					alertDialog.setCanceledOnTouchOutside(false);
					alertDialog.setCancelable(false);
					// set the message
					alertDialog.setMessage(insertionresponse);
					// set button1 functionality
					alertDialog.setButton("Ok",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// close dialog

									dialog.cancel();
									((ModifyComments)temp_view.getContext()).recreate();
									if(FROM.toString().equalsIgnoreCase("CURRENT")){
										ViewCurrentScheduleFragment.commented = true;
									}else{
										TodaysScheduleFragment.commented = true;
									}
									
									notifyDataSetChanged();

								}
							});
					// show the alert dialog
					alertDialog.show();
				} else {
					Toast.makeText(context, "Comment not deleted", 1).show();
				}
			}

		}
	}

}
