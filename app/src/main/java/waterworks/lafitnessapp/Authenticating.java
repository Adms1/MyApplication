package waterworks.lafitnessapp;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.widget.Toast;

public class Authenticating extends Activity {
	static String Tag = "Server activity";
	String result = "";
	Boolean isInternetPresent = false, server_status = false,
			version_status = false;
	SharedPreferences mPreferences;
	String version, web_version, web_path;
	public static final String MyPREFERENCES = "LAFitness_Version";
	private ProgressDialog pDialog;
	public static final int progress_bar_type = 0;
	public static final String CUSTOM_INTENT = "com.waterworks";
	// ///////////////
	private NotificationManager mNotifyManager;
	private Builder mBuilder;
	int id = 1;
	private boolean mRun;
	CharSequence title;

	final String strPref_Download_ID = "PREF_DOWNLOAD_ID";
	DownloadManager downloadManager;
	Context mContext=this;
	SharedPreferences sharedpreferences;
	boolean installed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.authenticating);
		isInternetPresent = Utility
				.isNetworkConnected(Authenticating.this);
		mPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		if (mPreferences.contains("Version")) {
			version = mPreferences.getString("Version", "1");
			Log.i(Tag, "Version = " + version);
		}

		boolean root_access = mPreferences.getBoolean("root", true);
		installed = mPreferences.getBoolean("installed", false);
		if(root_access==true){
			delete();
			//			Auto_Launch();
			if(!isAppInstalled("com.updater")){
				copyAssets();
			}
//			else{
//				launch_it(mContext);
//			}
		}else{
			boolean root__access = root_checker();
			if(root__access==true){
				//			Auto_Launch();
				if(!isAppInstalled("com.updater")){
					copyAssets();
				}
//				else{
//					launch_it(mContext);
//				}
			}
		}
		if(installed==false){
			copyAssets();	
		}
		
		new AsyscWhichServer().execute();

		//		if (isInternetPresent) {
		//			new AsyscWhichServer().execute();
		//		} else {
		//			onDetectNetworkState().show();
		//		}
	}
	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
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

	ProgressDialog pd;

	private class AsyscWhichServer extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(Authenticating.this);
			pd.setTitle("Please wait.....");
			pd.setMessage("Initializing Server...");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			SoapObject request = new SoapObject(AppConfig.SOAP_ADDRESS,
					"?op=CheckAccessibility");
			Log.e("Request","" + request);
			request.addProperty("str", 1);
			Log.i(Tag, "Request = " + request);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);

			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					AppConfig.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(
						"http://tempuri.org/CheckAccessibility", envelope); // Calling
				//				androidHttpTransport.call(
				//						AppConfig.SOAP_ADDRESS, envelope);

				// Web
				// service
				SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
				result = response.toString();
				Log.i("here", "Result : " + response.toString());
			} catch (SocketException e) {
				server_status = true;
				e.printStackTrace();
			} catch (SocketTimeoutException e) {
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
			pd.dismiss();
			/*
			 * Intent intent = new Intent(Intent.ACTION_VIEW);
			 * intent.setDataAndType(Uri.fromFile(new
			 * File(Environment.getExternalStorageDirectory() + "/Download/" +
			 * "Aquatics-1.apk")),"application/vnd.android.package-archive");
			 * startActivity(intent);
			 */
			if (server_status) {
				server_status = false;
				AppConfig.SOAP_ADDRESS = "http://office.waterworksswimonline.com/WWWebService/Service.asmx?WSDL";
				AppConfig.Report_Url = "http://office.waterworksswimonline.com/newcode/";

				//To run locally Please turn this code ON
				//				Intent i = new Intent(getApplicationContext(),
				//						LoginActivity.class);
				//				startActivity(i);
				//				finish();
				//To run Live Please turn this code ON
				new CheckVerion().execute();
			} else {
				//To run locally Please turn this code ON
				//				Intent i = new Intent(getApplicationContext(),
				//						LoginActivity.class);
				//				startActivity(i);
				//				finish();
				//To run Live Please turn this code ON
				new CheckVerion().execute();
			}
		}
	}

	private class CheckVerion extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(Authenticating.this);
			pd.setTitle("Please wait...");
			pd.setMessage("Initializing Server...");
			pd.setCancelable(false);
			pd.show();
			Log.i(Tag, "Soap address is= " + AppConfig.SOAP_ADDRESS);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(AppConfig.NAMESPACE,
					"GetAppLatestVersion");
			request.addProperty("App", "LAFitness");

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i(Tag, "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					AppConfig.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(
						"http://tempuri.org/GetAppLatestVersion", envelope); // Calling
				// Web
				// service
				SoapObject response = (SoapObject) envelope.getResponse();
				Log.i("here", "Result : " + response.toString());
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1
						.getProperty(0);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				Log.i("Code", code);
				if (code.equals("000")) {
					version_status = true;
					Object mSoapObject3 = mSoapObject1.getProperty(1);
					Log.i(Tag, "mSoapObject3=" + mSoapObject3);
					String resp = mSoapObject3.toString();
					JSONObject jo = new JSONObject(resp);
					JSONArray jsonArray = jo.getJSONArray("APK");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jo1 = jsonArray.getJSONObject(i);
						web_version = jo1.getString("Version");
						web_path = jo1.getString("Path");
					}
					//
				} else {
					version_status = false;
				}
			} /*catch (SocketException e) {
				server_status = true;
				e.printStackTrace();
			} catch (SocketTimeoutException e) {
				server_status = true;
				e.printStackTrace();
			}*/ catch (Exception e) {
				server_status = true;
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pd.dismiss();
			/*if (server_status) {
				onDetectNetworkState().show();
				server_status = false;
			} else {*/
			if (version_status) {
				version_status = false;
				Editor editor = mPreferences.edit();
				editor.putString("Version", web_version);
				editor.commit();
				if (web_version.toString().equalsIgnoreCase(version)) {
					Log.i(Tag, "Same version");
					Intent i = new Intent(getApplicationContext(),
							LoginActivity.class);
					startActivity(i);
					finish();
				} else {
					Log.i(Tag, "New version");
					/*new Thread(new Runnable() {
							public void run() {
								int mCount = 0;
								mRun = true;
								while (mRun) {
									++mCount;
									SystemClock.sleep(1000);
									title = "Downloading: " + mCount% 100
											+ "%";
								}
							}
						}).start();
						mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

						mBuilder = new NotificationCompat.Builder(
								Authenticating.this);
						mBuilder.setContentTitle("LAFitnessApp")
								.setContentText(title)
								.setSmallIcon(R.drawable.ic_download);

						new DownloadFileFromURL().execute(web_version);*/
					Uri downloadUri = Uri.parse(web_path);
					DownloadManager.Request request = new DownloadManager.Request(
							downloadUri);
					request.setTitle("Downloading... LAFitnessApp.apk");
					request.setMimeType("application/vnd.android.package-archive");
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
						request.allowScanningByMediaScanner();
						request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
					}

					request.setDestinationInExternalFilesDir(
							getApplicationContext(), null, "LAFitnessApp.apk");

					onProgress().show();
					long id = downloadManager.enqueue(request);

					// Save the request id
					Editor PrefEdit = mPreferences.edit();
					PrefEdit.putLong(strPref_Download_ID, id);
					PrefEdit.commit();
				}
			}
			//}
		}
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter intentFilter = new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		registerReceiver(downloadReceiver, intentFilter);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		unregisterReceiver(downloadReceiver);
	}

	private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			DownloadManager.Query query = new DownloadManager.Query();
			query.setFilterById(mPreferences.getLong(strPref_Download_ID, 0));
			Cursor cursor = downloadManager.query(query);
			if (cursor.moveToFirst()) {
				int columnIndex = cursor
						.getColumnIndex(DownloadManager.COLUMN_STATUS);
				int status = cursor.getInt(columnIndex);
				if (status == DownloadManager.STATUS_SUCCESSFUL) {
					onProgress().dismiss();
					boolean root_access = root_checker();
					if(root_access==true){
						//						Auto_Launch();

						String file = Environment.getExternalStorageDirectory()
								+ "/Android/data/lafitnessapp/files/" + "LAFitnessApp.apk";
						InstallAPK(file,null,mContext);

						//						installNewApk();
					}else{
						File apkFile = new File(
								Environment.getExternalStorageDirectory()
								+ "/Android/data/lafitnessapp/files/" + "LAFitnessApp.apk");
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setDataAndType(Uri.fromFile(apkFile),
								"application/vnd.android.package-archive");
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
				}
			}
		}
	};

	public static void Install_wdout_Root(Context context,String filename){
		File apkFile;
		if(filename.contains("Updater")){
			apkFile = new File(
					Environment.getExternalStorageDirectory()
					+ "/Android/data/lafitnessapp/files/" + "Updater.apk");
		}
		else{
			apkFile = new File(
					Environment.getExternalStorageDirectory()
					+ "/Android/data/lafitnessapp/files/" + "LAFitnessApp.apk");
		}
		
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(apkFile),
				"application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	@SuppressWarnings("unused")
	public static void InstallAPK(String filename,File direct_file,Context context){
		File file = null;
		int installResult = -1337;
		if(filename!=null && filename.trim().length()>0){
			file = new File(filename); 
		}else if(direct_file.toString().length()>0 && direct_file!=null){
			file = direct_file;
		}

		if(file.exists()){
			try {   
				String command;

				if(filename.toString().contains("LAFitness")){
					App_Installed(context);
				}
				
				Process proc = Runtime.getRuntime().exec("su -c pm install -r " + filename);
				proc.waitFor();

				if (proc != null) {
					try {
						installResult = proc.waitFor();
					} catch(InterruptedException e) {
						// Handle InterruptedException the way you like.
					}
					if (installResult == 0) {
						// Success!
						if(filename.toString().contains("Updater")){
							launch_it(context);
						}else if(filename.toString().contains("LAFitness")){
							App_Installed(context);
						}
						System.out.println("Installed");
					} else {
						// Failure. :-/
						Install_wdout_Root(context,filename);
						System.out.println(" Not Installed");
					}
				} else {
					// Failure 2. :-(
					Install_wdout_Root(context,filename);
					System.out.println(" Not Installed");
				}

			} catch (Exception e) {
				Install_wdout_Root(context,filename);
				e.printStackTrace();
			}
		}else{
			System.out.println("Not Present");
		}
	}


	public static void launch_it(Context context){
		//		Intent i = new Intent();
		//        i.setClassName("com.updater", "com.updater.MainActivity");
		//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		//        context.startActivity(i);

		Intent i = new Intent();
		i.setClassName("com.updater", "com.updater.Service_Class");
		context.startService(i);
		Toast.makeText(context, "Service", Toast.LENGTH_SHORT).show();
	}
	public boolean root_checker(){
		Process p;   
		boolean root = false;
		try {   
			// Preform su to get root privledges  
			p = Runtime.getRuntime().exec("su");   

			// Attempt to write a file to a root-only   
			DataOutputStream os = new DataOutputStream(p.getOutputStream());   
			os.writeBytes("echo \"Do I have root?\" >/system/sd/temporary.txt\n");  

			// Close the terminal  
			os.writeBytes("exit\n");   
			os.flush();   
			try {   
				p.waitFor();   
				if (p.exitValue() != 255) {   
					// TODO Code to run on success 
					root=true;
					Editor edit = mPreferences.edit();
					edit.putBoolean("root", root);
					edit.commit();
					//					Toast.makeText(getApplicationContext(), "root", Toast.LENGTH_SHORT).show();
					//		              toastMessage("root");  
				}   
				else {   
					// TODO Code to run on unsuccessful  
					//					Toast.makeText(getApplicationContext(), "not root", Toast.LENGTH_SHORT).show();
					//		               toastMessage("not root");      
				}   
			} catch (InterruptedException e) {   
				// TODO Code to run in interrupted exception  
				//				Toast.makeText(getApplicationContext(), "not root", Toast.LENGTH_SHORT).show();
				//		       toastMessage("not root");   
			}   
		} catch (IOException e) {   
			// TODO Code to run in input/output exception  
			//			Toast.makeText(getApplicationContext(), "not root", Toast.LENGTH_SHORT).show();
			//		    toastMessage("not root");   
		}
		return root;  

	}
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case progress_bar_type: // we set this to 0
			pDialog = new ProgressDialog(this);
			pDialog.setMessage("Downloading file. Check status in Notification Bar...");
			pDialog.setIndeterminate(false);
			pDialog.setIcon(R.drawable.ic_download);
			/*
			 * pDialog.setMax(100); pDialog.setProgressNumberFormat(null);
			 * pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			 * pDialog.setProgressDrawable(getResources().getDrawable(
			 * R.drawable.custom_progress_bar_horizontal));
			 */
			pDialog.setCancelable(false);
			pDialog.show();
			return pDialog;
		default:
			return null;
		}
	}

	class DownloadFileFromURL extends AsyncTask<String, Integer, String> {

		/**
		 * Before starting background thread Show Progress Bar Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//			showDialog(progress_bar_type);
			onProgress().show();
			mBuilder.setProgress(100, 0, false);
			mNotifyManager.notify(id, mBuilder.build());

		}

		/**
		 * Downloading file in background thread
		 * */
		@Override
		protected String doInBackground(String... f_url) {
			int count;
			try {
				URL url = new URL(web_path);
				URLConnection conection = url.openConnection();
				conection.connect();

				// this will be useful so that you can show a tipical 0-100%
				// progress bar
				int lenghtOfFile = conection.getContentLength();

				// download the file
				InputStream input = new BufferedInputStream(url.openStream(),
						8192);

				// Output stream
				OutputStream output = new FileOutputStream(
						"sdcard/Download/LAFitnessApp.apk");

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					// After this onProgressUpdate will be called
					publishProgress((int) ((total * 100) / lenghtOfFile));
					// writing data to file
					output.write(data, 0, count);
					//					title = "Downloading: " + count % 100
					//							+ "%";
				}
				/*				mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

				mBuilder = new NotificationCompat.Builder(
						Authenticating.this);
				mBuilder.setContentTitle("LAFitnessApp")
						.setContentText(title)
						.setSmallIcon(R.drawable.ic_download);*/

				// flushing output
				output.flush();

				// closing streams
				output.close();
				input.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * Updating progress bar
		 * */
		protected void onProgressUpdate(Integer... progress) {
			// setting progress percentage
			// pDialog.setProgress(Integer.parseInt(progress[0]));
			mBuilder.setProgress(100, progress[0], false);
			mBuilder.setContentText(title);
			mNotifyManager.notify(id, mBuilder.build());
			super.onProgressUpdate(progress);
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after the file was downloaded
			//			dismissDialog(progress_bar_type);
			onProgress().dismiss();
			mBuilder.setContentText("Download complete");
			// Removes the progress bar
			mBuilder.setProgress(0, 0, false);
			mNotifyManager.notify(id, mBuilder.build());
			Toast.makeText(getApplicationContext(), "File downloaded", 1)
			.show();

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(new File(Environment
					.getExternalStorageDirectory()
					+ "/Download/"
					+ "LAFitnessApp.apk")),
					"application/vnd.android.package-archive");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			finish();

		}
	}


	public AlertDialog onProgress(){
		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setIcon(getResources().getDrawable(R.drawable.ic_download));
		builder1.setMessage("Downloading file. Check status in Notification Bar...")
		.setTitle("Downloading")
		.setCancelable(false);
		return builder1.create();
	}

	public static void App_Installed(Context context)
	{
		Intent intent = new Intent();
		intent.setAction(CUSTOM_INTENT);
		context.sendBroadcast(intent);
	}

	private void copyAssets() {
		
		installed=true;
		mPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		Editor edit = mPreferences.edit();
		edit.putBoolean("installed", installed);
		edit.apply();
		edit.commit();
		
		AssetManager assetManager = getAssets();
		String[] files = null;
		try {
			files = assetManager.list("");
		} catch (IOException e) {
			Log.e("tag", "Failed to get asset file list.", e);
		}
		for(String filename : files) {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = assetManager.open(filename);
				File outFile = new File(getExternalFilesDir(null), filename);
				out = new FileOutputStream(outFile);
				//	          outFile.getPath();
				if(copyFile(in, out)==true){
					InstallAPK("/sdcard/Android/data/lafitnessapp/files/Updater.apk", outFile,mContext);
				}
			} catch(IOException e) {
				Log.e("tag", "Failed to copy asset file: " + filename, e);
			}     
			finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						// NOOP
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						// NOOP
					}
				}
			}  
		}
	}
	private boolean copyFile(InputStream in, OutputStream out) throws IOException {
		boolean success=false;
		byte[] buffer = new byte[1024];
		int read;
		while((read = in.read(buffer)) != -1){
			out.write(buffer, 0, read);
			success=true;
		}
		return success;

	}

	private boolean isAppInstalled(String packageName) {
		PackageManager pm = getPackageManager();
		boolean installed = false;
		try {
			pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
			installed = true;
		} catch (PackageManager.NameNotFoundException e) {
			installed = false;
		}
		return installed;
	}

	private static boolean isRooted() {
		return findBinary("su");
	}

	public static boolean findBinary(String binaryName) {
		boolean found = false;
		if (!found) {
			String[] places = {"/sbin/", "/system/bin/", "/system/xbin/", "/data/local/xbin/",
					"/data/local/bin/", "/system/sd/xbin/", "/system/bin/failsafe/", "/data/local/"};
			for (String where : places) {
				if ( new File( where + binaryName ).exists() ) {
					found = true;
					break;
				}
			}
		}
		return found;
	}
	
	public void delete(){
		File exist = new File("/sdcard/Android/data/lafitnessapp/files/Updater.apk");
		if(exist.exists()){
		boolean dir = new File("/sdcard/Android/data/lafitnessapp/files/Updater.apk").delete();
		}else{
			//Do Nothing
		}
	}

}
