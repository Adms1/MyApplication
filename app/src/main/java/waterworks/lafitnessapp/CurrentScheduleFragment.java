package waterworks.lafitnessapp;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import waterworks.lafitnessapp.Swipe.BaseSwipeListViewListener;
import waterworks.lafitnessapp.Swipe.SwipeListView;
import waterworks.lafitnessapp.adapter.ViewCurrentScheduleAdapter;

import waterworks.lafitnessapp.model.CurrentScheduleAdapterItem;
import waterworks.lafitnessapp.utility.AppConfig;
import waterworks.lafitnessapp.utility.Utility;
import waterworks.lafitnessapp.utility.WW_StaticClass;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class CurrentScheduleFragment extends Fragment {
	protected static final String TAG = "Current Schedule Fragment";
	View rootView;
	boolean server_response = false, connectionout = false, getlevel = false;
	SwipeListView swipelistview;
	Boolean isInternetPresent = false;
	float width;
	Button btn_send_att;
	// CurrentScheduleAdapter adapter;
	ViewCurrentScheduleAdapter adapter;
	List<CurrentScheduleAdapterItem> itemData;
	TextView tv_view_current_lesson_error;
	ProgressDialog pd;
	int wu_avail = 2;
	public ArrayList<String> LevelName = new ArrayList<String>();
	public ArrayList<String> LevelID = new ArrayList<String>();
	public ArrayList<String> ExistSwimComp = new ArrayList<String>();
	public ArrayList<String> IsShowSmCampStatus = new ArrayList<String>();
	public ArrayList<String> att = new ArrayList<String>();
	public ArrayList<String> wu_attendancetaken = new ArrayList<String>();
	public ArrayList<String> ISAAlert = new ArrayList<String>();
	public ArrayList<String> SScheduleID = new ArrayList<String>();
	public ArrayList<String> IScheduleID = new ArrayList<String>();
	public ArrayList<String> InstructorID = new ArrayList<String>();
	public ArrayList<String> lessontypeid = new ArrayList<String>();
	public ArrayList<String> SAge = new ArrayList<String>();
	public ArrayList<String> SLevel = new ArrayList<String>();
	public ArrayList<String> ScheLevel = new ArrayList<String>();
	public ArrayList<String> StudentID = new ArrayList<String>();
	public ArrayList<String> OrderDetailID = new ArrayList<String>();
	public ArrayList<String> StTimeHour = new ArrayList<String>();
	public ArrayList<String> StTimeMin = new ArrayList<String>();
	public ArrayList<String> SLastName = new ArrayList<String>();
	public ArrayList<String> SFirstName = new ArrayList<String>();
	public ArrayList<String> wu_count = new ArrayList<String>();
	public ArrayList<String> wu_Prev = new ArrayList<String>();
	public ArrayList<String> wu_Next = new ArrayList<String>();
	public ArrayList<String> ParentFirstName = new ArrayList<String>();
	public ArrayList<String> ParentLastName = new ArrayList<String>();
	public ArrayList<String> BirthDay = new ArrayList<String>();
	public ArrayList<String> LessonName = new ArrayList<String>();
	public ArrayList<String> LvlAdvAvail = new ArrayList<String>();
	public ArrayList<String> Comments = new ArrayList<String>();
	public ArrayList<String> wu_comments = new ArrayList<String>();
	public ArrayList<String> SwimComp = new ArrayList<String>();
	public ArrayList<String> SkillsList = new ArrayList<String>();
	public ArrayList<String> SkillsCount = new ArrayList<String>();
	public ArrayList<String> StudentGender = new ArrayList<String>();
	public ArrayList<String> NewUser = new ArrayList<String>();
	public ArrayList<ArrayList<String>> PreReqID = new ArrayList<ArrayList<String>>();
	public ArrayList<ArrayList<String>> Abbr = new ArrayList<ArrayList<String>>();
	public ArrayList<ArrayList<String>> PreReqChecked = new ArrayList<ArrayList<String>>();
	String currentDateandTime;
	int prevcount = 0, nextcount = 0;
	int viewpos;
	String temp_instid, temp_date;
	public static boolean yesitiszero = false;

	public CurrentScheduleFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_currentschedule, container, false);
		isInternetPresent = Utility.isNetworkConnected(getActivity());
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		width = size.x;
		if (isInternetPresent) {
			Initialize();
			new GetLevel().execute();
			swipelistview
					.setSwipeListViewListener(new BaseSwipeListViewListener() {

						@Override
						public void onOpened(int position, boolean toRight) {
							Log.e(TAG,"" +"onopen");

							if (toRight) {
								prevcount++;
								nextcount--;
								if

								(prevcount == nextcount) {
									yesitiszero = true;
								} else {
									yesitiszero = false;
								}

								viewpos = position;
								Log.i(TAG, "" + viewpos);

								temp_instid = itemData.get(position)
										.getInstructorID();
								temp_date = itemData.get(position).getWu_Prev();
								swipelistview.closeOpenedItems();
								new PrevNextData().execute();
							} else {
								nextcount++;
								prevcount--;
								if (nextcount == prevcount) {
									yesitiszero = true;
								} else {
									yesitiszero = false;
								}

								viewpos = position;
								Log.i(TAG, "" + viewpos);
								/*
								 * int month, year, day, min, hour; String date
								 * = adapter.getItem(viewpos)
								 * .getMainScheduleDate(); String tempdate[] =
								 * date.toString() .split("\\/"); month =
								 * Integer.parseInt(tempdate[0]); day =
								 * Integer.parseInt(tempdate[1]); year =
								 * Integer.parseInt(tempdate[2]); hour =
								 * Integer.parseInt(adapter
								 * .getItem(viewpos).getStTimeHour()); min =
								 * Integer.parseInt(adapter.getItem(viewpos)
								 * .getStTimeMin()); Calendar now =
								 * Calendar.getInstance();
								 * now.set(Calendar.MONTH, month);
								 * now.set(Calendar.DAY_OF_MONTH, day);
								 * now.set(Calendar.YEAR, year);
								 * now.set(Calendar.HOUR_OF_DAY, hour);
								 * now.set(Calendar.MINUTE, min);
								 * 
								 * now.add(Calendar.MINUTE, (20)); tempmonth =
								 * "" + (now.get(Calendar.MONTH)); tempday = ""
								 * + now.get(Calendar.DAY_OF_MONTH); tempyear =
								 * "" + now.get(Calendar.YEAR); temphour = "" +
								 * now.get(Calendar.HOUR_OF_DAY); tempmin = "" +
								 * now.get(Calendar.MINUTE); String am_pm; if
								 * (Integer.parseInt(temphour) > 11) { am_pm =
								 * "PM"; } else { am_pm = "AM"; } if
								 * (tempmonth.toString().length() == 1) {
								 * tempmonth = "0" + tempmonth; } if
								 * (tempyear.toString().length() == 1) {
								 * tempyear = "0" + tempyear; } if
								 * (tempday.toString().length() == 1) { tempday
								 * = "0" + tempday; } if
								 * (temphour.toString().length() == 1) {
								 * temphour = "0" + temphour; } if
								 * (tempmin.toString().length() == 1) { tempmin
								 * = "0" + tempmin; } temp_instid =
								 * adapter.getItem(viewpos)
								 * .getWu_InstructorID(); temp_istname =
								 * adapter.getItem(viewpos)
								 * .getWu_InstructorName(); temp_date =
								 * tempmonth + "/" + tempday + "/" + tempyear;
								 * temp_time_hour = temphour; temp_time_min =
								 * tempmin; mydatetime = tempmonth + "/" +
								 * tempday + "/" + tempyear + " " + temphour +
								 * ":" + tempmin + " " + am_pm;
								 */
								swipelistview.closeOpenedItems();
								// new PrevNextData().execute();
							}

						}

						@Override
						public void onClosed(int position, boolean fromRight) {
						}

						@Override
						public void onListChanged() {
						}

						@Override
						public void onMove(int position, float x) {
						}

						@Override
						public void onStartOpen(int position, int action,
								boolean right) {
						}

						@Override
						public void onStartClose(int position, boolean right) {
						}

						@Override
						public void onClickFrontView(int position) {
						}

						@Override
						public void onClickBackView(int position) {
						}

						@Override
						public void onDismiss(int[] reverseSortedPositions) {

						}
					});
		} else {
			onDetectNetworkState().show();
		}
		return rootView;
	}

	public int convertDpToPixel(float dp) {
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return (int) px;
	}

	private void Initialize() {
		// TODO Auto-generated method stub
		btn_send_att = (Button) rootView.findViewById(R.id.btn_send_att);
		btn_send_att.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "Click", Toast.LENGTH_SHORT).show();
			}
		});
		itemData = new ArrayList<CurrentScheduleAdapterItem>();
//		adapter = new AdapterCurrentSchedule(itemData, getActivity());
		tv_view_current_lesson_error = (TextView) rootView
				.findViewById(R.id.tv_view_current_lesson_error);
		swipelistview = (SwipeListView) rootView
				.findViewById(R.id.example_swipe_lv_list);
		swipelistview.setSwipeMode(SwipeListView.SWIPE_MODE_BOTH);
		swipelistview.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL);
		swipelistview.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
		swipelistview.setOffsetLeft(convertDpToPixel(width / 2)); // left side
		swipelistview.setOffsetRight(convertDpToPixel(width / 2)); // right side
		swipelistview.setAnimationTime(500); // Animation time
		swipelistview.setSwipeOpenOnLongPress(false);
	}

	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
		builder1.setIcon(getResources().getDrawable(R.drawable.logo));
		builder1.setMessage("Please turn on internet connection and try again.")
				.setTitle("No Internet Connection.")
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								getActivity().finish();
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

	// ////////////Get level list/////////////
	private class GetLevel extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setTitle("Please wait...");
			pd.setMessage("Loading Levels...");
			pd.setCancelable(true);
			pd.setCanceledOnTouchOutside(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(AppConfig.NAMESPACE,
					AppConfig.GetLevelList_Method);
			request.addProperty("token", WW_StaticClass.UserToken);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request", "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					AppConfig.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(AppConfig.GetLevelList_Action,
						envelope); // Calling Web service
				SoapObject response = (SoapObject) envelope.getResponse();
				Log.i("here Get Level", "Result : " + response.toString());
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1
						.getProperty(0);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				Log.i("Code", code);
				// response.toString();
				if (code.equals("000")) {
					getlevel = true;
					Object mSoapObject3 = mSoapObject1.getProperty(1);
					Log.i(TAG, "mSoapObject3=" + mSoapObject3);
					JSONObject jo = new JSONObject(mSoapObject3.toString());
					JSONArray jArray = jo.getJSONArray("Levels");
					JSONObject jsonObject;
					for (int i = 0; i < jArray.length(); i++) {
						jsonObject = jArray.getJSONObject(i);
						LevelName.add(jsonObject.getString("LevelName"));
						LevelID.add(jsonObject.getString("LevelId"));
					}
					Log.e(TAG, "Level name = " + LevelName);
					Log.e(TAG, "Level id = " + LevelID);
				} else {
					getlevel = false;
				}

			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				connectionout = true;
			} catch (SocketException e) {
				e.printStackTrace();
				connectionout = true;
			} catch (JSONException e) {
				e.printStackTrace();
				server_response = false;
			} catch (Exception e) {
				e.printStackTrace();
				server_response = false;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pd.dismiss();
			if (server_response) {
				server_response = false;
				onDetectNetworkState().show();
			} else if (connectionout) {
				connectionout = false;
				new GetLevel().execute();
			} else {
				if (getlevel) {
					getlevel = false;
					new GetCurrentSchedule().execute();
				}
			}
		}
	}

	private class GetCurrentSchedule extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
			currentDateandTime = format.format(date);
			pd = new ProgressDialog(getActivity());
			pd.setTitle("Please wait...");
			pd.setMessage("Loading schedule...");
			pd.setCancelable(true);
			pd.setCanceledOnTouchOutside(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(AppConfig.NAMESPACE,
					AppConfig.GetCurrentForAllInstrList_Method);
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("strsiteid", WW_StaticClass.siteid.toString()
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
						AppConfig.GetCurrentForAllInstrList_Action, envelope); // Calling
																				// Web
																				// service
				SoapObject response = (SoapObject) envelope.bodyIn;
				Log.i("here Current Schedule", "Result : " + response.toString());
				SoapPrimitive sp1 = (SoapPrimitive) response.getProperty(0);
				String resp = sp1.toString();
				JSONObject jo = new JSONObject(resp);
				wu_avail = jo.getInt("wu_avail");
				if (wu_avail == 1 || wu_avail == 2) {
					JSONArray jArray = jo.getJSONArray("Attendance");
					JSONArray jArray2, jArray3;
					JSONObject jsonObject, jsonObject2, jsonObject3;
					for (int k = 0; k < jArray.length(); k++) {
						jsonObject = jArray.getJSONObject(k);
						jArray2 = jsonObject.getJSONArray("Items");
						for (int i = 0; i < jArray2.length(); i++) {
							jsonObject2 = jArray2.getJSONObject(i);
							ExistSwimComp.add(jsonObject2
									.getString("ExistSwimComp"));
							IsShowSmCampStatus.add(jsonObject2
									.getString("IsShowSmCampStatus"));
							att.add(jsonObject2.getString("att"));
							wu_attendancetaken.add(jsonObject2
									.getString("wu_attendancetaken"));
							ISAAlert.add(jsonObject2.getString("ISAAlert"));
							SScheduleID.add(jsonObject2
									.getString("SScheduleID"));
							IScheduleID.add(jsonObject2
									.getString("IScheduleID"));
							InstructorID.add(jsonObject2
									.getString("InstructorID"));
							lessontypeid.add(jsonObject2
									.getString("lessontypeid"));
							SAge.add(jsonObject2.getString("SAge"));
							SLevel.add(jsonObject2.getString("SLevel"));
							ScheLevel.add(jsonObject2.getString("ScheLevel"));
							StudentID.add(jsonObject2.getString("StudentID"));
							OrderDetailID.add(jsonObject2
									.getString("OrderDetailID"));
							StTimeHour.add(jsonObject2.getString("StTimeHour"));
							StTimeMin.add(jsonObject2.getString("StTimeMin"));
							SLastName.add(jsonObject2.getString("SLastName"));
							SFirstName.add(jsonObject2.getString("SFirstName"));
							wu_count.add(jsonObject2.getString("wu_count"));
							wu_Prev.add(jsonObject2.getString("wu_Prev"));
							wu_Next.add(jsonObject2.getString("wu_Next"));
							ParentFirstName.add(jsonObject2
									.getString("ParentFirstName"));
							ParentLastName.add(jsonObject2
									.getString("ParentLastName"));
							BirthDay.add(jsonObject2.getString("BirthDay"));
							LessonName.add(jsonObject2.getString("LessonName"));
							LvlAdvAvail.add(jsonObject2
									.getString("LvlAdvAvail"));
							Comments.add(jsonObject2.getString("Comments"));
							wu_comments.add(jsonObject2
									.getString("wu_comments"));
							SwimComp.add(jsonObject2.getString("SwimComp"));
							SkillsCount.add(jsonObject2
									.getString("SkillsCount"));
							StudentGender.add(jsonObject2
									.getString("StudentGender"));
							NewUser.add(jsonObject2.getString("NewUser"));
							if (SkillsCount.get(i).toString()
									.equalsIgnoreCase("0")) {

							} else {
								jArray3 = jsonObject2
										.getJSONArray("SkillsList");
								ArrayList<String> tempabbr = new ArrayList<String>();
								ArrayList<String> tempPreReqId = new ArrayList<String>();
								ArrayList<String> tempPreReqChecked = new ArrayList<String>();
								for (int b = 0; b < jArray3.length(); b++) {
									jsonObject3 = jArray3.getJSONObject(b);
									String abbr = jsonObject3.getString("Abbr");
									String prereqid = jsonObject3
											.getString("PreReqID");
									String prereqchecked = jsonObject3
											.getString("PreReqChecked");

									tempabbr.add(abbr);
									tempPreReqId.add(prereqid);
									tempPreReqChecked.add(prereqchecked);
								}
								Abbr.add(tempabbr);
								PreReqID.add(tempPreReqId);
								PreReqChecked.add(tempPreReqChecked);
							}
						}
					}
				}
			} catch (SocketTimeoutException e) {
				e.printStackTrace();
				connectionout = true;
			} catch (SocketException e) {
				e.printStackTrace();
				connectionout = true;
			} catch (JSONException e) {
				e.printStackTrace();
				server_response = false;
			} catch (Exception e) {
				e.printStackTrace();
				server_response = false;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pd.dismiss();
			if (server_response) {
				server_response = false;
				onDetectNetworkState().show();
			} else if (connectionout) {
				connectionout = false;
				new GetCurrentSchedule().execute();
			} else {
				if (wu_avail == 0) {
					tv_view_current_lesson_error.setVisibility(View.VISIBLE);
					tv_view_current_lesson_error
							.setText("No availability at this time.");
				} else {
					if (!LessonName.isEmpty()
							&& !LessonName.get(0).equalsIgnoreCase("")) {
						tv_view_current_lesson_error.setVisibility(View.GONE);
						try {
							// swipelistview.setAdapter(adapter);
							for (int i = 0; i < StudentID.size(); i++) {
								itemData.add(new CurrentScheduleAdapterItem(
										wu_avail,
										ExistSwimComp.get(i),
										IsShowSmCampStatus.get(i),
										att.get(i),
										wu_attendancetaken.get(i),
										ISAAlert.get(i),
										SScheduleID.get(i),
										IScheduleID.get(i),
										InstructorID.get(i),
										lessontypeid.get(i),
										SAge.get(i),
										SLevel.get(i),
										ScheLevel.get(i),
										StudentID.get(i),
										OrderDetailID.get(i),
										StTimeHour.get(i),
										StTimeMin.get(i),
										SLastName.get(i),
										SFirstName.get(i),
										wu_count.get(i),
										wu_Prev.get(i),
										wu_Next.get(i),
										ParentFirstName.get(i),
										ParentLastName.get(i),
										BirthDay.get(i),
										LessonName.get(i),
										LvlAdvAvail.get(i),
										Comments.get(i),
										wu_comments.get(i),
										SwimComp.get(i),
										SkillsCount.get(i),
										StudentGender.get(i),
										NewUser.get(i),
										PreReqID.get(i),
										Abbr.get(i),
										PreReqChecked.get(i),
										LevelName,
										LevelID,
										((CurrentScheduleActivity) getActivity()).UserName,
										((CurrentScheduleActivity) getActivity()).UserId));
							}
							swipelistview.setAdapter(adapter);
							adapter.notifyDataSetChanged();
						} catch (Exception e) {
							e.printStackTrace();
						}

					} else {
						tv_view_current_lesson_error
								.setVisibility(View.VISIBLE);
						tv_view_current_lesson_error
								.setText("No lesson at this time.");
					}
				}
			}
		}
	}

	private class PrevNextData extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pd = new ProgressDialog(getActivity());
			pd.setTitle("Please wait...");
			pd.setMessage("Loading Schedule...");
			pd.setCancelable(true);
			pd.setCanceledOnTouchOutside(false);
			pd.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(AppConfig.NAMESPACE,
					AppConfig.GetAttendanceList_Method);
			request.addProperty("token", WW_StaticClass.UserToken);
			request.addProperty("Rinstructorid", temp_instid);
			request.addProperty("strRScheDate", temp_date);
			// request.addProperty("strRScheDate","01/18/2015 09:00 AM" );

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request", "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					AppConfig.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(AppConfig.GetAttendanceList_Action,
						envelope); // Calling
									// Web
									// service
				SoapObject response = (SoapObject) envelope.bodyIn;
				Log.i(TAG, "Result : " + response.toString());
				SoapPrimitive sp1 = (SoapPrimitive) response.getProperty(0);
				String resp = sp1.toString();
				JSONObject jo = new JSONObject(resp);
				wu_avail = jo.getInt("wu_avail");
				// ISAFlag = jo.getBoolean("ISAFlag");
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
							itemData.get(viewpos).setExistSwimComp(
									jsonObject2.getString("ExistSwimComp"));
							itemData.get(viewpos)
									.setIsShowSmCampStatus(
											jsonObject2
													.getString("IsShowSmCampStatus"));
							itemData.get(viewpos).setAtt(
									jsonObject2.getString("att"));
							itemData.get(viewpos)
									.setWu_attendancetaken(
											jsonObject2
													.getString("wu_attendancetaken"));
							itemData.get(viewpos).setISAAlert(
									jsonObject2.getString("ISAAlert"));
							itemData.get(viewpos).setSScheduleID(
									jsonObject2.getString("SScheduleID"));
							itemData.get(viewpos).setIScheduleID(
									jsonObject2.getString("IScheduleID"));
							itemData.get(viewpos).setInstructorID(
									jsonObject2.getString("InstructorID"));
							itemData.get(viewpos).setLessontypeid(
									jsonObject2.getString("lessontypeid"));
							itemData.get(viewpos).setSAge(
									jsonObject2.getString("SAge"));
							itemData.get(viewpos).setSLevel(
									jsonObject2.getString("SLevel"));
							itemData.get(viewpos).setScheLevel(
									jsonObject2.getString("ScheLevel"));
							itemData.get(viewpos).setStudentID(
									jsonObject2.getString("StudentID"));
							itemData.get(viewpos).setOrderDetailID(
									jsonObject2.getString("OrderDetailID"));
							itemData.get(viewpos).setStTimeHour(
									jsonObject2.getString("StTimeHour"));
							itemData.get(viewpos).setStTimeMin(
									jsonObject2.getString("StTimeMin"));
							itemData.get(viewpos).setSLastName(
									jsonObject2.getString("SLastName"));
							itemData.get(viewpos).setSFirstName(
									jsonObject2.getString("SFirstName"));
							itemData.get(viewpos).setWu_count(
									jsonObject2.getString("wu_count"));
							itemData.get(viewpos).setWu_Prev(
									jsonObject2.getString("wu_Prev"));
							itemData.get(viewpos).setWu_Next(
									jsonObject2.getString("wu_Next"));
							itemData.get(viewpos).setParentFirstName(
									jsonObject2.getString("ParentFirstName"));
							itemData.get(viewpos).setParentLastName(
									jsonObject2.getString("ParentLastName"));
							itemData.get(viewpos).setBirthDay(
									jsonObject2.getString("BirthDay"));
							itemData.get(viewpos).setLessonName(
									jsonObject2.getString("LessonName"));
							itemData.get(viewpos).setLvlAdvAvail(
									jsonObject2.getString("LvlAdvAvail"));
							itemData.get(viewpos).setComments(
									jsonObject2.getString("Comments"));
							itemData.get(viewpos).setWu_comments(
									jsonObject2.getString("wu_comments"));
							itemData.get(viewpos).setSwimComp(
									jsonObject2.getString("SwimComp"));
							itemData.get(viewpos).setSkillsCount(
									jsonObject2.getString("SkillsCount"));
							itemData.get(viewpos).setStudentGender(
									jsonObject2.getString("StudentGender"));
							itemData.get(viewpos).setNewUser(
									jsonObject2.getString("NewUser"));
							if (SkillsCount.get(i).toString()
									.equalsIgnoreCase("0")) {
								ArrayList<String> tempabbr = new ArrayList<String>();
								ArrayList<String> tempPreReqId = new ArrayList<String>();
								ArrayList<String> tempPreReqChecked = new ArrayList<String>();
								tempabbr.add("");
								tempPreReqId.add("");
								tempPreReqChecked.add("");
								Abbr.add(tempabbr);
								PreReqID.add(tempPreReqId);
								PreReqChecked.add(tempPreReqChecked);
							} else {
								jArray3 = jsonObject2
										.getJSONArray("SkillsList");
								ArrayList<String> tempabbr = new ArrayList<String>();
								ArrayList<String> tempPreReqId = new ArrayList<String>();
								ArrayList<String> tempPreReqChecked = new ArrayList<String>();
								for (int b = 0; b < jArray3.length(); b++) {
									jsonObject3 = jArray3.getJSONObject(b);
									String abbr = jsonObject3.getString("Abbr");
									String prereqid = jsonObject3
											.getString("PreReqID");
									String prereqchecked = jsonObject3
											.getString("PreReqChecked");

									tempabbr.add(abbr);
									tempPreReqId.add(prereqid);
									tempPreReqChecked.add(prereqchecked);
								}
								itemData.get(viewpos).setAbbr(tempabbr);
								itemData.get(viewpos).setPreReqID(tempPreReqId);
								itemData.get(viewpos).setPreReqChecked(
										tempPreReqChecked);
							}
						}
					}
				}
			} catch (JSONException e) {
				server_response = true;
				e.printStackTrace();
			} catch (SocketTimeoutException e) {
				// TODO: handle exception
				connectionout = true;
				e.printStackTrace();
			} catch (SocketException e) {
				// TODO: handle exception
				e.printStackTrace();
				connectionout = true;
			} catch (Exception e) {
				server_response = true;
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			pd.dismiss();
			if (server_response) {
				server_response = false;
				onDetectNetworkState().show();
			} else if (connectionout) {
				connectionout = false;
				new PrevNextData().execute();
			} else {
				try {
					adapter.notifyDataSetChanged();
					swipelistview.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	}
