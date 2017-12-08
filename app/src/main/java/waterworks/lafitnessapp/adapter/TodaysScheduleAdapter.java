package waterworks.lafitnessapp.adapter;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import waterworks.lafitnessapp.ClockinoutActivity;
import waterworks.lafitnessapp.ModifyComments;

import waterworks.lafitnessapp.R;
import waterworks.lafitnessapp.ScheduleActivity;
import waterworks.lafitnessapp.TodaysScheduleActivity;
import waterworks.lafitnessapp.TodaysScheduleFragment;

import waterworks.lafitnessapp.model.TodaysScheduleAdapterItem;
import waterworks.lafitnessapp.AppConfig;
import waterworks.lafitnessapp.utility.MyTagHandler;
import waterworks.lafitnessapp.utility.SingleOptionAlertWithoutTitle;
import waterworks.lafitnessapp.Utility;
import waterworks.lafitnessapp.WW_StaticClass;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class TodaysScheduleAdapter extends BaseAdapter implements
AnimationListener {
	List<TodaysScheduleAdapterItem> data;
	Context context;
	int layoutResID;
	Animation animBlink;
	ArrayList<String> selected_student = new ArrayList<String>();
	ArrayList<Boolean> ischange;
	ArrayList<String> newslevel, newschdlevel, newprereqid, isched, comments,
	wu_sscheduleid, wu_studentid, wu_orderdetailid, wu_lessontypeid,
	wu_sttimehr, wu_sttimemin, wu_scheduledate, ddlW, ddlB, ddlR,
	wu_siteid, wu_slevel, ddlSchedLevel, wu_schedlevel, chkschedselect,
	straarylist, Msg_Status, Msg_Str, prereqid, lev, oldlev, newddlw,
	newddlb, newddlr;
	String siteid, instructor_id;
	ArrayList<String> FinalPreReqId = new ArrayList<String>();
	ArrayList<Character> levelchanged;
	ArrayList<Integer> final_newatt = new ArrayList<Integer>();
	ArrayList<Integer> final_oldatt = new ArrayList<Integer>();

	String comment = "", reason = "", SwimCompId = "", StudentId = "",
			UserToken, UserLevel;
	boolean attendance_response = false, server_response = false,
			cancel_response = false, connectionout = false,
			login_status = false;
	View temp_view;

	//Shadow Request Vars
	ArrayList<String> PoolName,PoolId;
	boolean pool_status,server_status=false,shadow_click=false,deck_click = false;
	public static final int request_shadow=0;
	public static final int request_deck=1;
	Dialog dialog=null;
	public String token="";
	

	public TodaysScheduleAdapter(List<TodaysScheduleAdapterItem> data,
			Context context) {
		super();
		this.data = data;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
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
		TextView tv_sName, tv_pName, tv_sAge, tv_sPaid_cls, tv_sComments,
		tv_lec_time, tv_lesson_name,shadow_request_text;
		Button btn_sIsa, btn_sCls_lvl;
		ImageButton btn_sNote, btn_sCamera;
		ImageView iv_sLate;
		Button btn_sLevel, btn_sSch_leve;
		TableLayout tl_skills;
		// Button btn_s_r,btn_s_b,btn_s_w;
		ToggleButton sw_precent;
		ListPopupWindow listpopupwindow, listpopupwindow1;
		// lpw_s_w,lpw_s_b,lpw_s_r;
		int oldlevel, newlevel;
		ImageButton level_add, level_sub, sch_level_add, sch_level_sub;
		CheckBox chb_todays_student_selection;
		RelativeLayout rl_exp_col;
		ImageButton show_detail, hide_detail;
		
	}

	private int[] colors = new int[] { Color.parseColor("#EEEEEE"),
			Color.parseColor("#FFFFFF") };
	TableLayout ll_animator_layout;
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		try {
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(parent.getContext()).inflate(
						R.layout.todaysschedule_list_item, null);
				int colorpos = position % colors.length;
				convertView.setBackgroundColor(colors[colorpos]);

				holder.shadow_request_text = (TextView) convertView
						.findViewById(R.id.shadow_request_text);

				holder.tv_lesson_name = (TextView) convertView
						.findViewById(R.id.tv_ts_level_name_row);
				holder.tv_lec_time = (TextView) convertView
						.findViewById(R.id.tv_ts_time_row);
				holder.btn_sIsa = (Button) convertView
						.findViewById(R.id.btn_isa_alert);
				holder.tv_sName = (TextView) convertView
						.findViewById(R.id.tv_ts_studentname_row);
				holder.tv_pName = (TextView) convertView
						.findViewById(R.id.tv_ts_parentname_row);
				holder.btn_sCamera = (ImageButton) convertView
						.findViewById(R.id.btn_camera);
				holder.tv_sAge = (TextView) convertView
						.findViewById(R.id.tv_ts_age_row);
				holder.btn_sLevel = (Button) convertView
						.findViewById(R.id.btn_ts_slevel_row);
				holder.btn_sSch_leve = (Button) convertView
						.findViewById(R.id.btn_ts_sched_level_row);
				holder.sw_precent = (ToggleButton) convertView
						.findViewById(R.id.btn_ts_yes_no);
				holder.btn_sCls_lvl = (Button) convertView
						.findViewById(R.id.btn_ts_cls_lvl_row);
				holder.tv_sPaid_cls = (TextView) convertView
						.findViewById(R.id.tv_ts_paid_cls_row);
				holder.show_detail = (ImageButton) convertView
						.findViewById(R.id.show_detail);
				holder.hide_detail = (ImageButton) convertView
						.findViewById(R.id.hide_detail);
				holder.tl_skills = (TableLayout) convertView
						.findViewById(R.id.table_cl_data);
				holder.tl_skills.setVisibility(View.GONE);
				holder.rl_exp_col = (RelativeLayout)convertView
						.findViewById(R.id.rl_exp_col);
				// holder.btn_s_w =
				// (Button)convertView.findViewById(R.id.chb_ts_wbr_w);
				// holder.btn_s_b =
				// (Button)convertView.findViewById(R.id.chb_ts_wbr_b);
				// holder.btn_s_r =
				// (Button)convertView.findViewById(R.id.chb_ts_wbr_r);
				holder.tv_sComments = (TextView) convertView
						.findViewById(R.id.tv_ts_comment_row);
				holder.btn_sNote = (ImageButton) convertView
						.findViewById(R.id.btn_ts_note_row);
				holder.iv_sLate = (ImageView) convertView
						.findViewById(R.id.img_ts_late);
				holder.listpopupwindow = new ListPopupWindow(
						context.getApplicationContext());
				holder.listpopupwindow1 = new ListPopupWindow(
						context.getApplicationContext());
				holder.sch_level_add = (ImageButton) convertView
						.findViewById(R.id.ib_ts_plus_sch_level);
				holder.sch_level_sub = (ImageButton) convertView
						.findViewById(R.id.ib_ts_sub_sch_level);
				holder.level_add = (ImageButton) convertView
						.findViewById(R.id.ib_ts_plus_level);
				holder.level_sub = (ImageButton) convertView
						.findViewById(R.id.ib_ts_sub_level);
				holder.chb_todays_student_selection = (CheckBox) convertView
						.findViewById(R.id.chb_todays_student_selection);
				ischange = new ArrayList<Boolean>();
				newslevel = new ArrayList<String>();
				newschdlevel = new ArrayList<String>();
				newprereqid = new ArrayList<String>();
				for (int i = 0; i < data.size(); i++) {
					ischange.add(i, false);
					newslevel.add(i, data.get(i).getSLevel());
					newschdlevel.add(i, data.get(i).getScheLevel());

					selected_student.add("0");
				}
				
				if(WW_StaticClass.CLOCKED_TODAY_INSERT==true){
					token=WW_StaticClass.UserToken_Extra;
				}else{
					token=WW_StaticClass.UserToken;
				}
				
				holder.chb_todays_student_selection
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// TODO Auto-generated method stub
						if (isChecked) {
							selected_student.remove(position);
							selected_student.add(position, "1");
						} else {
							selected_student.remove(position);
							selected_student.add(position, "0");
						}
					}
				});
				//				holder.tl_skills.getViewTreeObserver().addOnPreDrawListener(
				//						new ViewTreeObserver.OnPreDrawListener() {
				//
				//							@Override
				//							public boolean onPreDraw() {
				//								holder.tl_skills.getViewTreeObserver()
				//										.removeOnPreDrawListener(this);
				//								holder.tl_skills.setVisibility(View.GONE);
				//
				//								final int widthSpec = View.MeasureSpec
				//										.makeMeasureSpec(0,
				//												View.MeasureSpec.UNSPECIFIED);
				//								final int heightSpec = View.MeasureSpec
				//										.makeMeasureSpec(0,
				//												View.MeasureSpec.UNSPECIFIED);
				//								holder.tl_skills
				//										.measure(widthSpec, heightSpec);
				//
				//								mAnimator = slideAnimator(0,
				//										holder.tl_skills.getMeasuredHeight());
				//								return true;
				//							}
				//						});
				holder.show_detail
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						//						ll_animator_layout = holder.tl_skills;

						holder.show_detail.setVisibility(View.GONE);
						holder.hide_detail.setVisibility(View.VISIBLE);
						if (holder.tl_skills.getVisibility() == View.GONE) {
							//							expand();
							holder.tl_skills.setVisibility(View.VISIBLE);
						}
					}
				});

				holder.shadow_request_text.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub\

						if(context instanceof ScheduleActivity){
							//							((ScheduleActivity)context).
						}else if(context instanceof TodaysScheduleActivity){
							((TodaysScheduleActivity)context).call_attendance(position);
							((TodaysScheduleActivity)context).direct_request();
						}	
						else{
						}
					}
				});
				holder.hide_detail.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//				ll_animator_layout = holder.tl_skills;
						holder.hide_detail.setVisibility(View.GONE);
						holder.show_detail.setVisibility(View.VISIBLE);
						if (holder.tl_skills.getVisibility() == View.VISIBLE) {
							//					collapse();
							holder.tl_skills.setVisibility(View.GONE);
						}
					}
				});

				instructor_id = data.get(position).getInstructorId();
				animBlink = AnimationUtils.loadAnimation(
						context.getApplicationContext(), R.anim.blink);
				animBlink.setAnimationListener(this);
				int temp_att = data.get(position).getAtt();
				if (data.get(position).getWu_attendancetaken() == 0) {
					holder.btn_sLevel.setEnabled(true);
					holder.btn_sSch_leve.setEnabled(true);
					holder.btn_sNote.setEnabled(true);
					holder.sw_precent.setEnabled(true);
					holder.sw_precent.setChecked(true);
					holder.level_add.setEnabled(true);
					holder.level_sub.setEnabled(true);
					holder.sch_level_add.setEnabled(true);
					holder.sch_level_sub.setEnabled(true);
					holder.rl_exp_col.setVisibility(View.VISIBLE);
					holder.chb_todays_student_selection.setVisibility(View.VISIBLE);
				} else {
					if(TodaysScheduleFragment.showall){
						
					}else{
						convertView = new Space(context);
					}
//					holder.chb_todays_student_selection.setVisibility(View.GONE);
					holder.chb_todays_student_selection.setBackgroundResource(R.drawable.screen3_check_done);
					holder.chb_todays_student_selection.setEnabled(false);
					//					convertView.setLayoutParams(new AbsListView.LayoutParams(-1,1));
					//					convertView.setVisibility(View.GONE);
					holder.btn_sLevel.setEnabled(false);
					holder.btn_sSch_leve.setEnabled(false);
					holder.btn_sNote.setEnabled(false);
					holder.sw_precent.setEnabled(false);
					holder.level_add.setEnabled(false);
					holder.level_sub.setEnabled(false);
					holder.sch_level_add.setEnabled(false);
					holder.sch_level_sub.setEnabled(false);
					if (temp_att == 2 || temp_att == 3 || temp_att == 4
							|| temp_att == 5 || temp_att == 6 || temp_att == 7
							|| temp_att == 8 || temp_att == 10 || temp_att == 12
							|| temp_att == 13 || temp_att == 14 || temp_att == 15
							|| temp_att == 16 || temp_att == 17) {
						holder.rl_exp_col.setVisibility(View.GONE);
						holder.sw_precent.setChecked(false);
						//					holder.chb_todays_student_selection.setVisibility(View.GONE);
					}
					/*
					 * else{ holder.sw_precent.setChecked(true);
					 * holder.sw_precent.setEnabled(true);
					 * holder.btn_sLevel.setEnabled(true);
					 * holder.btn_sSch_leve.setEnabled(true);
					 * holder.btn_sNote.setEnabled(true); newatt.remove(position);
					 * newatt.add(position,0); }
					 */

					else if (temp_att == 0) {
						holder.rl_exp_col.setVisibility(View.VISIBLE);
						holder.sw_precent.setChecked(true);
						//					holder.chb_todays_student_selection.setVisibility(View.VISIBLE);
					} else {
						holder.rl_exp_col.setVisibility(View.GONE);
						holder.sw_precent.setChecked(false);
						//					holder.chb_todays_student_selection.setVisibility(View.GONE);
					}
				}
				// ////////// sw precent click///////////////
				holder.sw_precent.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (holder.sw_precent.isChecked()) {
							TodaysScheduleFragment.newatt.remove(position);
							TodaysScheduleFragment.newatt.add(position, 0);
						} else {
							TodaysScheduleFragment.newatt.remove(position);
							TodaysScheduleFragment.newatt.add(position, 1);
						}

						Log.i("todays new att",
								TodaysScheduleFragment.newatt.toString());
					}
				});
				/*
				 * holder.sw_precent .setOnCheckedChangeListener(new
				 * OnCheckedChangeListener() {
				 * 
				 * @Override public void onCheckedChanged( CompoundButton
				 * buttonView, boolean isChecked) { // TODO Auto-generated method
				 * stub
				 * 
				 * if (holder.sw_precent.isChecked() == true) {
				 * newatt.remove(position); newatt.add(position, 0); } if(isChecked
				 * == false){ newatt.remove(position); newatt.add(position, 1); }
				 * Log.i("new att", newatt.toString()); } });
				 */// /////////////////////////////////
				// //isa and L and cls/lvl ////////

				if (data.get(position).getISAAlert().equalsIgnoreCase("true")) {
					holder.btn_sIsa.setVisibility(View.VISIBLE);
					holder.btn_sIsa.startAnimation(animBlink);
				} else {
					holder.btn_sIsa.setVisibility(View.INVISIBLE);
				}
				// / For cls lvl /////////
				String cls_lvl = data.get(position).getCls_Lvl();
				holder.btn_sCls_lvl.setText(cls_lvl);
				// / L ///
				String lvlavail = data.get(position).getLvl_Adv_Avail();
				if (Integer.parseInt(lvlavail) > 1) {
					holder.iv_sLate.setVisibility(View.VISIBLE);
				} else {
					holder.iv_sLate.setVisibility(View.INVISIBLE);
				}

				// //////////////////////
				// CLS (Lesson name)////
				holder.tv_lesson_name.setText(data.get(position).getLessonName());
				// ////
				// Lec time//
				int hr = Integer.parseInt(data.get(position).getStTimeHour());
				int min = Integer.parseInt(data.get(position).getStTimeMin());
				String am_pm;
				if (hr >= 12 && min >= 00) {

					am_pm = "PM";
				} else {
					am_pm = "AM";
				}
				holder.tv_lec_time.setText(Html.fromHtml("<small>"
						+ data.get(position).getFormateStTimeHour() + ":"
						+ data.get(position).getFormatStTimeMin() + am_pm
						+ "</small>"));

				// /////////////////////////
				// / Name and Age /////
				String gender = data.get(position).getStudentGender().trim();
				Log.i("Attendance adapter", "Gender = " + gender);

				String sname = data.get(position).getSFirstName() + " "
						+ data.get(position).getSLastName();
				String fname = "(" + data.get(position).getParentFirstName() + " "
						+ data.get(position).getParentLastName() + ")";
				if (gender.toString().equalsIgnoreCase("Female")) {
					holder.tv_sName.setTextColor(Color.rgb(136, 0, 183));
					holder.tv_sName.setText(sname);

				} else {
					holder.tv_sName.setTextColor(Color.rgb(0, 0, 102));
					holder.tv_sName.setText(sname);

				}
				holder.tv_pName.setText(fname);

				holder.tv_sAge.setText(data.get(position).getSAge());
				// //////////////
				// ///////Level and Schedule Level/////////////

				holder.oldlevel = Integer.parseInt(data.get(position).getSLevel());
				holder.newlevel = Integer.parseInt(data.get(position).getSLevel());
				final ArrayList<String> LevelName = new ArrayList<String>();
				final ArrayList<String> LevelID = new ArrayList<String>();
				LevelName.addAll(data.get(position).getLevelName());
				LevelID.addAll(data.get(position).getLevelID());
				String levelname = LevelName.get(LevelID.indexOf(data.get(position)
						.getSLevel()));
				String schlevelname = LevelName.get(LevelID.indexOf(data.get(
						position).getSLevel()));
				if (levelname.length() == 1) {
					levelname = "0" + levelname;
				}
				if (schlevelname.length() == 1) {
					schlevelname = "0" + schlevelname;
				}
				holder.btn_sLevel.setText(levelname);
				holder.btn_sSch_leve.setText(schlevelname);

				holder.level_add.setOnClickListener(new OnClickListener() {

					@SuppressWarnings("deprecation")
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int size = LevelName.size();
						String temp_level = holder.btn_sLevel.getText().toString();
						final String templevel, tempschdlevel;
						templevel = holder.btn_sLevel.getText().toString();
						tempschdlevel = holder.btn_sSch_leve.getText().toString();
						if (temp_level.charAt(0) == '0') {
							temp_level = "" + temp_level.charAt(1);
						}
						final int index = LevelName.indexOf(temp_level);
						if (size == index + 1) {
							Toast.makeText(context, "Level Maximum", Toast.LENGTH_LONG).show();
						} else {
							String lname = LevelName.get(index + 1);
							if (lname.length() == 1) {
								lname = "0" + lname;
							}
							holder.btn_sLevel.setText(lname);
							holder.btn_sSch_leve.setText(lname);
							AlertDialog alertDialog = new AlertDialog.Builder(
									context).create();
							alertDialog.setTitle("LAFitnessApp");
							alertDialog.setIcon(R.drawable.logo);
							alertDialog.setCanceledOnTouchOutside(false);
							alertDialog.setCancelable(false);
							// set the message
							alertDialog
							.setMessage("You have selected to change this student’s level. From "
									+ temp_level
									+ " To "
									+ lname
									+ " Is this correct?");
							// set button1 functionality
							alertDialog.setButton("Yes",
									new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// close dialog
									ischange.remove(position);
									ischange.add(position, true);
									holder.newlevel = Integer
											.parseInt(LevelID
													.get(index + 1));

									if (ischange.get(position).equals(true)) {
										newslevel.remove(position);
										newschdlevel.remove(position);
										newslevel.add(position,
												LevelID.get(index + 1));
										newschdlevel.add(position,
												LevelID.get(index + 1));
//										if (holder.newlevel == 4) {
//											if ((holder.oldlevel == 11)
//													|| (holder.oldlevel == 12)) {
//												showAlert(
//														context,
//														"LAFitnessApp",
//														"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.1",
//														"Ok");
//											}
//										} else if (holder.newlevel == 5) {
//											if ((holder.oldlevel == 11)
//													|| (holder.oldlevel == 12)
//													|| (holder.oldlevel == 13)) {
//												showAlert(
//														context,
//														"LAFitnessApp",
//														"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.2",
//														"Ok");
//											}
//										}
//
//										else if (holder.newlevel == 6) {
//											if (holder.oldlevel == 4) {
//												showAlert(
//														context,
//														"LAFitnessApp",
//														"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.3",
//														"Ok");
//											}
//										} else if (holder.newlevel == 7) {
//											if ((holder.oldlevel == 4)
//													|| (holder.oldlevel == 5)) {
//												showAlert(
//														context,
//														"LAFitnessApp",
//														"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.4",
//														"Ok");
//											}
//										} else if (holder.newlevel == 8) {
//											if (((holder.oldlevel >= 4) && (holder.oldlevel <= 6))
//													&& (holder.newlevel != holder.oldlevel)
//													&& (holder.oldlevel != 8)) {
//												showAlert(
//														context,
//														"LAFitnessApp",
//														"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.5",
//														"Ok");
//											}
//										}
//
//										else if (holder.newlevel == 9) {
//											if (((holder.oldlevel >= 4) && (holder.oldlevel <= 7))
//													&& (holder.newlevel != holder.oldlevel)) {
//												showAlert(
//														context,
//														"LAFitnessApp",
//														"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.6",
//														"Ok");
//											}
//										}
//
//										else if (holder.newlevel == 10) {
//											if (((holder.oldlevel >= 4) && (holder.oldlevel <= 8))
//													&& (holder.newlevel != holder.oldlevel)) {
//												showAlert(
//														context,
//														"LAFitnessApp",
//														"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.7",
//														"Ok");
//											}
//										}
//
//										else if (holder.newlevel == 13) {
//											if ((holder.oldlevel == 11)) {
//												showAlert(
//														context,
//														"LAFitnessApp",
//														"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.8",
//														"Ok");
//											}
//										} else if (holder.newlevel == 14) {
//											if ((holder.oldlevel >= 4)
//													&& (holder.oldlevel <= 9)) {
//												showAlert(
//														context,
//														"LAFitnessApp",
//														"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.9",
//														"Ok");
//											}
//											if ((holder.oldlevel >= 11)
//													&& (holder.oldlevel <= 13)) {
//												showAlert(
//														context,
//														"LAFitnessApp",
//														"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.10",
//														"Ok");
//											}
//										}
									}
									dialog.cancel();

								}
							});
							alertDialog.setButton2("No",
									new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
									holder.btn_sLevel.setText(templevel);
									holder.btn_sSch_leve
									.setText(tempschdlevel);
								}
							});
							// show the alert dialog
							alertDialog.show();
						}
					}
				});

				holder.level_sub.setOnClickListener(new OnClickListener() {

					@SuppressWarnings("deprecation")
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int size = 0;
						String temp_level = holder.btn_sLevel.getText().toString();
						final String templevel, tempschdlevel;
						templevel = holder.btn_sLevel.getText().toString();
						tempschdlevel = holder.btn_sSch_leve.getText().toString();
						if (temp_level.charAt(0) == '0') {
							temp_level = "" + temp_level.charAt(1);
						}
						final int index = LevelName.indexOf(temp_level);
						if (size == index) {
							Toast.makeText(context, "Level Minimum.", Toast.LENGTH_LONG).show();
						} else {
							String lname = LevelName.get(index - 1);
							if (lname.length() == 1) {
								lname = "0" + lname;
							}
							holder.btn_sLevel.setText(lname);
							holder.btn_sSch_leve.setText(lname);
							AlertDialog alertDialog = new AlertDialog.Builder(
									context).create();
							alertDialog.setTitle("LAFitnessApp");
							alertDialog.setIcon(R.drawable.logo);
							alertDialog.setCanceledOnTouchOutside(false);
							alertDialog.setCancelable(false);
							// set the message
							alertDialog
							.setMessage("You have selected to change this student’s level. From "
									+ temp_level
									+ " To "
									+ lname
									+ " Is this correct?");
							// set button1 functionality
							alertDialog.setButton("Yes",
									new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// close dialog
									ischange.remove(position);
									ischange.add(position, true);
									holder.newlevel = Integer
											.parseInt(LevelID
													.get(index - 1));
									if (ischange.get(position).equals(true)) {
										newslevel.remove(position);
										newschdlevel.remove(position);
										newslevel.add(position,
												LevelID.get(index + 1));
										newschdlevel.add(position,
												LevelID.get(index + 1));
//										if (holder.newlevel == 4) {
//											if ((holder.oldlevel == 11)
//													|| (holder.oldlevel == 12)) {
//												showAlert(
//														context,
//														"LAFitnessApp",
//														"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.1",
//														"Ok");
//											}
//										} else if (holder.newlevel == 5) {
//											if ((holder.oldlevel == 11)
//													|| (holder.oldlevel == 12)
//													|| (holder.oldlevel == 13)) {
//												showAlert(
//														context,
//														"LAFitnessApp",
//														"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.2",
//														"Ok");
//											}
//										}
//
//										else if (holder.newlevel == 6) {
//											if (holder.oldlevel == 4) {
//												showAlert(
//														context,
//														"LAFitnessApp",
//														"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.3",
//														"Ok");
//											}
//										} else if (holder.newlevel == 7) {
//											if ((holder.oldlevel == 4)
//													|| (holder.oldlevel == 5)) {
//												showAlert(
//														context,
//														"LAFitnessApp",
//														"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.4",
//														"Ok");
//											}
//										} else if (holder.newlevel == 8) {
//											if (((holder.oldlevel >= 4) && (holder.oldlevel <= 6))
//													&& (holder.newlevel != holder.oldlevel)
//													&& (holder.oldlevel != 8)) {
//												showAlert(
//														context,
//														"LAFitnessApp",
//														"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.5",
//														"Ok");
//											}
//										}
//
//										else if (holder.newlevel == 9) {
//											if (((holder.oldlevel >= 4) && (holder.oldlevel <= 7))
//													&& (holder.newlevel != holder.oldlevel)) {
//												showAlert(
//														context,
//														"LAFitnessApp",
//														"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.6",
//														"Ok");
//											}
//										}
//
//										else if (holder.newlevel == 10) {
//											if (((holder.oldlevel >= 4) && (holder.oldlevel <= 8))
//													&& (holder.newlevel != holder.oldlevel)) {
//												showAlert(
//														context,
//														"LAFitnessApp",
//														"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.7",
//														"Ok");
//											}
//										}
//
//										else if (holder.newlevel == 13) {
//											if ((holder.oldlevel == 11)) {
//												showAlert(
//														context,
//														"LAFitnessApp",
//														"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.8",
//														"Ok");
//											}
//										} else if (holder.newlevel == 14) {
//											if ((holder.oldlevel >= 4)
//													&& (holder.oldlevel <= 9)) {
//												showAlert(
//														context,
//														"LAFitnessApp",
//														"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.9",
//														"Ok");
//											}
//											if ((holder.oldlevel >= 11)
//													&& (holder.oldlevel <= 13)) {
//												showAlert(
//														context,
//														"LAFitnessApp",
//														"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.10",
//														"Ok");
//											}
//										}
									}

									dialog.cancel();

								}
							});
							alertDialog.setButton2("No",
									new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
									holder.btn_sLevel.setText(templevel);
									holder.btn_sSch_leve
									.setText(tempschdlevel);
								}
							});
							// show the alert dialog
							alertDialog.show();
						}
					}
				});

				holder.sch_level_add.setOnClickListener(new OnClickListener() {

					@SuppressWarnings("deprecation")
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int size = LevelName.size();
						String temp_level = holder.btn_sSch_leve.getText()
								.toString();
						final String templevel = holder.btn_sSch_leve.getText()
								.toString();
						if (temp_level.charAt(0) == '0') {
							temp_level = "" + temp_level.charAt(1);
						}
						final int index = LevelName.indexOf(temp_level);
						if (size == index + 1) {
							Toast.makeText(context, "Schedule Level Maximum", Toast.LENGTH_LONG)
							.show();
						} else {
							String lname = LevelName.get(index + 1);
							if (lname.length() == 1) {
								lname = "0" + lname;
							}
							holder.btn_sSch_leve.setText(lname);
							AlertDialog alertDialog = new AlertDialog.Builder(
									context).create();
							alertDialog.setTitle("LAFitnessApp");
							alertDialog.setIcon(R.drawable.logo);
							alertDialog.setCanceledOnTouchOutside(false);
							alertDialog.setCancelable(false);
							// set the message
							alertDialog
							.setMessage("You have selected to change this student’s level. From "
									+ temp_level
									+ " To "
									+ lname
									+ " Is this correct?");
							// set button1 functionality
							alertDialog.setButton("Yes",
									new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// close dialog
									newschdlevel.remove(position);
									newschdlevel.add(position,
											LevelID.get(index + 1));
									dialog.cancel();

								}
							});
							alertDialog.setButton2("No",
									new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
									holder.btn_sSch_leve.setText(templevel);
								}
							});
							// show the alert dialog
							alertDialog.show();
						}
					}
				});

				holder.sch_level_sub.setOnClickListener(new OnClickListener() {

					@SuppressWarnings("deprecation")
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int size = 0;
						String temp_level = holder.btn_sSch_leve.getText()
								.toString();
						final String templevel = holder.btn_sSch_leve.getText()
								.toString();
						if (temp_level.charAt(0) == '0') {
							temp_level = "" + temp_level.charAt(1);
						}
						final int index = LevelName.indexOf(temp_level);
						if (size == index) {
							Toast.makeText(context, "Schedule Level Minimum.", Toast.LENGTH_LONG)
							.show();
						} else {
							String lname = LevelName.get(index - 1);
							if (lname.length() == 1) {
								lname = "0" + lname;
							}
							holder.btn_sSch_leve.setText(lname);
							AlertDialog alertDialog = new AlertDialog.Builder(
									context).create();
							alertDialog.setTitle("LAFitnessApp");
							alertDialog.setIcon(R.drawable.logo);
							alertDialog.setCanceledOnTouchOutside(false);
							alertDialog.setCancelable(false);
							// set the message
							alertDialog
							.setMessage("You have selected to change this student’s level. From "
									+ temp_level
									+ " To "
									+ lname
									+ " Is this correct?");
							// set button1 functionality
							alertDialog.setButton("Yes",
									new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// close dialog
									newschdlevel.remove(position);
									newschdlevel.add(position,
											LevelID.get(index - 1));
									dialog.cancel();

								}
							});
							alertDialog.setButton2("No",
									new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									holder.btn_sSch_leve.setText(templevel);
									dialog.cancel();
								}
							});
							// show the alert dialog
							alertDialog.show();
						}

					}
				});

				holder.btn_sSch_leve.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						holder.listpopupwindow1.show();
					}
				});

				holder.listpopupwindow = new ListPopupWindow(
						context.getApplicationContext());
				holder.btn_sLevel.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						holder.listpopupwindow.show();
					}
				});
				holder.listpopupwindow
				.setAdapter(new ArrayAdapter<String>(context
						.getApplicationContext(), R.layout.edittextpopup,
						LevelName));
				holder.listpopupwindow.setAnchorView(holder.btn_sLevel);
				holder.listpopupwindow.setHeight(LayoutParams.WRAP_CONTENT);
				holder.listpopupwindow.setModal(true);
				holder.listpopupwindow
				.setOnItemClickListener(new OnItemClickListener() {
					@SuppressWarnings("deprecation")
					@Override
					public void onItemClick(AdapterView<?> parent,
							View view, final int pos, long id) {
						// TODO Auto-generated method stub
						String levelname, schlevelname;
						final String level, schdlevel;
						level = holder.btn_sLevel.getText().toString();
						schdlevel = holder.btn_sSch_leve.getText()
								.toString();
						schlevelname = LevelName.get(pos);
						levelname = LevelName.get(pos);
						if (levelname.length() == 1) {
							levelname = "0" + levelname;
						}
						if (schlevelname.length() == 1) {
							schlevelname = "0" + schlevelname;
						}
						holder.btn_sLevel.setText(levelname);
						holder.btn_sSch_leve.setText(schlevelname);
						holder.listpopupwindow.dismiss();
						AlertDialog alertDialog = new AlertDialog.Builder(
								context).create();
						alertDialog.setTitle("LAFitnessApp");
						alertDialog.setIcon(R.drawable.logo);
						alertDialog.setCanceledOnTouchOutside(false);
						alertDialog.setCancelable(false);
						// set the message
						alertDialog
						.setMessage("You have selected to change this student’s level. From "
								+ level
								+ " To "
								+ levelname
								+ " Is this correct?");
						alertDialog.setButton("Yes",
								new DialogInterface.OnClickListener() {

							@Override
							public void onClick(
									DialogInterface dialog,
									int which) {
								// close dialog
								ischange.remove(position);
								ischange.add(position, true);
								holder.newlevel = Integer
										.parseInt(LevelID.get(pos));
								if (ischange.get(position).equals(
										true)) {
									newslevel.remove(position);
									newschdlevel.remove(position);
									newslevel.add(position,
											LevelID.get(pos));
									newschdlevel.add(position,
											LevelID.get(pos));
									if (holder.newlevel == 4) {
										if ((holder.oldlevel == 11)
												|| (holder.oldlevel == 12)) {
											showAlert(
													context,
													"LAFitnessApp",
													"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.1",
													"Ok");
										}
									} else if (holder.newlevel == 5) {
										if ((holder.oldlevel == 11)
												|| (holder.oldlevel == 12)
												|| (holder.oldlevel == 13)) {
											showAlert(
													context,
													"LAFitnessApp",
													"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.2",
													"Ok");
										}
									}

									else if (holder.newlevel == 6) {
										if (holder.oldlevel == 4) {
											showAlert(
													context,
													"LAFitnessApp",
													"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.3",
													"Ok");
										}
									} else if (holder.newlevel == 7) {
										if ((holder.oldlevel == 4)
												|| (holder.oldlevel == 5)) {
											showAlert(
													context,
													"LAFitnessApp",
													"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.4",
													"Ok");
										}
									} else if (holder.newlevel == 8) {
										if (((holder.oldlevel >= 4) && (holder.oldlevel <= 6))
												&& (holder.newlevel != holder.oldlevel)
												&& (holder.oldlevel != 8)) {
											showAlert(
													context,
													"LAFitnessApp",
													"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.5",
													"Ok");
										}
									}

									else if (holder.newlevel == 9) {
										if (((holder.oldlevel >= 4) && (holder.oldlevel <= 7))
												&& (holder.newlevel != holder.oldlevel)) {
											showAlert(
													context,
													"LAFitnessApp",
													"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.6",
													"Ok");
										}
									}

									else if (holder.newlevel == 10) {
										if (((holder.oldlevel >= 4) && (holder.oldlevel <= 8))
												&& (holder.newlevel != holder.oldlevel)) {
											showAlert(
													context,
													"LAFitnessApp",
													"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.7",
													"Ok");
										}
									}

									else if (holder.newlevel == 13) {
										if ((holder.oldlevel == 11)) {
											showAlert(
													context,
													"LAFitnessApp",
													"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.8",
													"Ok");
										}
									} else if (holder.newlevel == 14) {
										if ((holder.oldlevel >= 4)
												&& (holder.oldlevel <= 9)) {
											showAlert(
													context,
													"LAFitnessApp",
													"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.9",
													"Ok");
										}
										if ((holder.oldlevel >= 11)
												&& (holder.oldlevel <= 13)) {
											showAlert(
													context,
													"LAFitnessApp",
													"Please check your level selection.\nYou have advanced the child by more than 1 level\nerror 1.10",
													"Ok");
										}
									}
								}

							}
						});
						alertDialog.setButton2("No",
								new DialogInterface.OnClickListener() {

							@Override
							public void onClick(
									DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
								holder.btn_sLevel.setText(level);
								holder.btn_sSch_leve
								.setText(schdlevel);
							}
						});
						// show the alert dialog
						alertDialog.show();
					}
				});

				holder.listpopupwindow1
				.setAdapter(new ArrayAdapter<String>(context
						.getApplicationContext(), R.layout.edittextpopup,
						LevelName));
				holder.listpopupwindow1.setAnchorView(holder.btn_sSch_leve);
				holder.listpopupwindow1.setHeight(LayoutParams.WRAP_CONTENT);
				holder.listpopupwindow1.setModal(true);
				holder.listpopupwindow1
				.setOnItemClickListener(new OnItemClickListener() {

					@SuppressWarnings("deprecation")
					@Override
					public void onItemClick(AdapterView<?> parent,
							View view, final int pos, long id) {
						// TODO Auto-generated method stub
						String schlevelname = "";
						schlevelname = LevelName.get(pos);
						final String schdlevel = holder.btn_sSch_leve
								.getText().toString();
						if (schlevelname.toString().length() == 1) {
							schlevelname = "0" + schlevelname;
						}
						holder.btn_sSch_leve.setText(schlevelname);
						holder.listpopupwindow1.dismiss();
						AlertDialog alertDialog = new AlertDialog.Builder(
								context).create();
						alertDialog.setTitle("LAFitnessApp");
						alertDialog.setIcon(R.drawable.logo);
						alertDialog.setCanceledOnTouchOutside(false);
						alertDialog.setCancelable(false);
						// set the message
						alertDialog
						.setMessage("You have selected to change this student’s level.From "
								+ schdlevel
								+ " To "
								+ schlevelname
								+ " Is this correct?");
						alertDialog.setButton("Yes",
								new DialogInterface.OnClickListener() {

							@Override
							public void onClick(
									DialogInterface dialog,
									int which) {
								// close dialog
								newschdlevel.remove(position);
								newschdlevel.add(position,
										LevelID.get(position));
							}
						});
						alertDialog.setButton2("No",
								new DialogInterface.OnClickListener() {

							@Override
							public void onClick(
									DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.cancel();
								holder.btn_sSch_leve
								.setText(schdlevel);
							}
						});
						// show the alert dialog
						alertDialog.show();
					}
				});

				// ////////////////////////////////////////////

				// /Paid class/////
				String paidcls = data.get(position).getPaidClasses();
				String temp1[] = paidcls.toString().split("\\.");
				int paid_cls = Integer.parseInt(temp1[0]);
				if (paid_cls < 2) {

					holder.tv_sPaid_cls.setText(Html.fromHtml("<b>" + paid_cls
							+ "</b>"));
					holder.tv_sPaid_cls.setBackgroundColor(Color.RED);
					holder.tv_sPaid_cls.startAnimation(animBlink);
				} else {
					holder.tv_sPaid_cls.setText("" + paid_cls);
				}

				// ////////////////////////////

				// new student//
				Boolean newstudent = data.get(position).getNewUser();
				Log.i("here", "New student = " + newstudent);
				if (newstudent == true) {
					String next = "<font color='#EE0000'>New Student</font>";
					holder.tv_sComments.setText(Html.fromHtml(
							next + data.get(position).getComments().toString(),
							null, new MyTagHandler()));
				} else {
					// Comment//
					// Using MyTagHandler class for generating list//
					holder.tv_sComments.setText(Html.fromHtml(data.get(position)
							.getComments().toString(), null, new MyTagHandler()));
				}

				// /New comment///
				holder.btn_sNote.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub

						TodaysScheduleActivity.t.interrupt();
						Intent it = new Intent(v.getContext(), ModifyComments.class);
						it.putExtra("studentid", data.get(position).getStudentID());
						it.putExtra("userid", data.get(position).getInstructorId());
						it.putExtra("FROM", "TODAY");
						v.getContext().startActivity(it);

					}
				});

				// / Skill list///
				if(data.get(position).getSkillsCount().toString().equalsIgnoreCase("0")){
					holder.rl_exp_col.setVisibility(View.GONE);
				}else{
					holder.rl_exp_col.setVisibility(View.VISIBLE);
					final ArrayList<String> finalABBR = (ArrayList<String>) data.get(
							position).getAbbr();
					final ArrayList<String> finalPreReqId = (ArrayList<String>) data
							.get(position).getPreReqID();
					final ArrayList<String> finalPreReqChecked = (ArrayList<String>) data
							.get(position).getPreReqChecked();
					int offset_in_column = 0, table_size = Integer.parseInt(data.get(
							position).getSkillsCount());

					TableRow tr = null;
					int offset_in_table = 0;
					for (offset_in_table = 0; offset_in_table < table_size; offset_in_table++) {

						if (offset_in_column == 0) {
							tr = new TableRow(context.getApplicationContext());
							TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(
									TableLayout.LayoutParams.FILL_PARENT,
									TableLayout.LayoutParams.WRAP_CONTENT);
							tr.setLayoutParams(tableRowParams);
						}

						final CheckBox check = new CheckBox(
								context.getApplicationContext());
						check.setText(finalABBR.get(offset_in_table));
						check.setId(Integer.parseInt(finalPreReqId.get(offset_in_table)));
						check.setButtonDrawable(context.getResources().getDrawable(
								R.drawable.checkbox_selection));
						check.setTextColor(context.getResources().getColor(
								R.color.texts1));
						check.setPadding(5, 5, 5, 5);
						check.setSingleLine(true);
						check.setChecked(Boolean.valueOf(finalPreReqChecked
								.get(offset_in_table)));
						// check.setLayoutParams(new
						// TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,
						// LayoutParams.WRAP_CONTENT, 1));

						tr.addView(check);

						offset_in_column++;
						if (offset_in_column == 4) {
							holder.tl_skills.addView(tr);
							offset_in_column = 0;
						}

						check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(CompoundButton buttonView,
									boolean isChecked) {
								// TODO Auto-generated method stub
								if (isChecked) {
									newprereqid.add(position+"_"+data.get(position).getSScheduleID()
											+ "*" + check.getId() + "*");
								} else {
									newprereqid.remove(position+"_"+data.get(position)
											.getSScheduleID()
											+ "*"
											+ check.getId()
											+ "*");
								}
							}
						});
					}
					if (offset_in_column != 0) {
						holder.tl_skills.addView(tr);
					}
				}

				// /Insert att////
				/*
				 * TodaysScheduleFragment.btn_send_att.setOnClickListener(new
				 * OnClickListener() {
				 * 
				 * @Override public void onClick(View v) { // TODO Auto-generated
				 * method stub String abc = ""; for (int i = 0; i <
				 * selected_student.size(); i++) {
				 * if(selected_student.get(i).toString().equalsIgnoreCase("1")){ abc
				 * = abc+data.get(i).getStudentID()+","; } } Log.e("Todays Adapter",
				 * "Selected students = " +abc ); } });
				 */
				TodaysScheduleFragment.btn_send_att
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Log.i("new att",
								TodaysScheduleFragment.newatt.toString());
						//
						// final Dialog dialog = new Dialog(v.getContext());
						// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						// dialog.setContentView(R.layout.dialog_login);
						// final EditText et_username = (EditText) dialog
						// .findViewById(R.id.et_login_username);
						// final EditText et_password = (EditText) dialog
						// .findViewById(R.id.et_login_password);
						// Button btn_login = (Button) dialog
						// .findViewById(R.id.btn_login);
						// btn_login.setOnClickListener(new
						// OnClickListener() {
						//
						// @Override
						// public void onClick(View v) {
						// if (Utility.isNetworkConnected(context) == true)
						// {
						// if (et_username.getText().length() > 2
						// && et_password.getText()
						// .length() > 2) {
						// username = et_username.getText()
						// .toString();
						// password = et_password.getText()
						// .toString();
						// dialog.dismiss();
						// // new LoginAsyn().execute();
						//
						// } else {
						// SingleOptionAlertWithoutTitle.ShowAlertDialog(
						// v.getContext(),
						// "LAFitnessApp",
						// "Username/Password Is Invalid",
						// "OK");
						// }
						// } else {
						// onDetectNetworkState().show();
						// }
						// }
						// });
						// dialog.setCanceledOnTouchOutside(false);
						// dialog.show();
						if (Utility.isNetworkConnected(context) == true) {
							straarylist = new ArrayList<String>();
							levelchanged = new ArrayList<Character>();
							prereqid = new ArrayList<String>();
							isched = new ArrayList<String>();
							comments = new ArrayList<String>();
							wu_sscheduleid = new ArrayList<String>();
							wu_studentid = new ArrayList<String>();
							wu_orderdetailid = new ArrayList<String>();
							wu_lessontypeid = new ArrayList<String>();
							wu_sttimehr = new ArrayList<String>();
							wu_sttimemin = new ArrayList<String>();
							wu_scheduledate = new ArrayList<String>();
							wu_slevel = new ArrayList<String>();
							ddlSchedLevel = new ArrayList<String>();
							wu_schedlevel = new ArrayList<String>();
							oldlev = new ArrayList<String>();
							lev = new ArrayList<String>();
							chkschedselect = new ArrayList<String>();
							siteid = data.get(0).getSiteID();
							int newpos;
							for (int i = 0; i < selected_student.size(); i++) {
								newpos = i;
								if (selected_student.get(newpos).toString()
										.equalsIgnoreCase("1")) {
									if (ischange.get(newpos).equals(true)) {
										levelchanged.add('Y');
									} else {
										levelchanged.add('N');
									}
									oldlev.add(data.get(newpos).getSLevel());
									lev.add(newslevel.get(newpos));
									ddlSchedLevel.add(data.get(newpos)
											.getScheLevel());
									wu_schedlevel.add(newschdlevel
											.get(newpos));
									chkschedselect.add("true");
									isched.add(data.get(newpos)
											.getIScheduleID());
									comments.add(data.get(newpos)
											.getWu_Comments());
									wu_sscheduleid.add(data.get(newpos)
											.getSScheduleID());
									wu_studentid.add(data.get(newpos)
											.getStudentID());
									wu_orderdetailid.add(data.get(newpos)
											.getOrderDetailID());
									wu_lessontypeid.add(data.get(newpos)
											.getLessontypeid());
									wu_sttimehr.add(data.get(newpos)
											.getStTimeHour());
									wu_sttimemin.add(data.get(newpos)
											.getStTimeMin());
									wu_scheduledate.add(data.get(newpos)
											.getMainScheduleDate());
									wu_slevel.add(data.get(newpos)
											.getSLevel());
									final_newatt.add(TodaysScheduleFragment.newatt.get(newpos));
									final_oldatt.add(TodaysScheduleFragment.oldatt.get(newpos));
									for (int j = 0; j < newprereqid.size(); j++) {
										if(newprereqid.get(j).toString().startsWith(""+newpos)){
											prereqid.add(newprereqid.get(j).toString().replaceAll(newpos+"_", ""));
										}
									}
								}
							}
							Log.i("Adapter", "Final pre req = " + prereqid);
							if(wu_studentid.size()>0){
								new Insert_Attandance().execute();
							}else{
								Toast.makeText(context, "Please select at Least one student.", Toast.LENGTH_SHORT).show();
							}
						} else {
							onDetectNetworkState().show();
						}

					}
				});

			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return convertView;
	}

	String username, password;

	private class Insert_Attandance extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			for (int i = 0; i < prereqid.size(); i++) {
				if (FinalPreReqId.contains(prereqid.get(i))) {

				} else {
					FinalPreReqId.add(prereqid.get(i));
				}
			}
			Log.i("Final Prereq Id", "Final prereq id = " + FinalPreReqId);
			straarylist.add("levelschanged=" + levelchanged
					+ ";chkschedselect=" + chkschedselect + ";lev=" + lev
					+ ";oldatt=" + final_oldatt + ";att="
					+ final_newatt + ";isched=" + isched
					+ ";comments=" + comments + ";wu_sscheduleid="
					+ wu_sscheduleid + ";wu_studentid=" + wu_studentid
					+ ";wu_orderdetailid=" + wu_orderdetailid
					+ ";wu_lessontypeid=" + wu_lessontypeid + ";wu_sttimehr="
					+ wu_sttimehr + ";wu_sttimemin=" + wu_sttimemin
					+ ";wu_scheduledate=" + wu_scheduledate + ";oldlev="
					+ oldlev + ";ddlSchedLevel=" + ddlSchedLevel
					+ ";wu_schedlevel=" + wu_schedlevel);

		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			String str = straarylist.toString();
			str = str.replaceFirst("\\[", "");
			str = str.substring(0, str.lastIndexOf("]"));
			SoapObject request = new SoapObject(AppConfig.NAMESPACE,
					AppConfig.Insert_Attandance_ForToday_Method);
			request.addProperty("token", token); // 1
			request.addProperty("wu_InstructorID", instructor_id); // 2
			request.addProperty("wu_siteid", siteid.toString()); // 3
			request.addProperty("_prereq", FinalPreReqId.toString()); //4
			request.addProperty("straarylist", str);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request", "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					AppConfig.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(
						AppConfig.Insert_Attandance_ForToday_Action, envelope); // Calling
				// Web
				// service
				SoapObject response = (SoapObject) envelope.bodyIn;
				Log.i("Attendance apdater", "Result : " + response.toString());
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				// Log.i("Attendance Adapter", "mSoapObject1="+mSoapObject1);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1
						.getProperty(0);
				// Log.i("Attendance Adapter", "mSoapObject2="+mSoapObject2);
				SoapObject mSoapObject3 = (SoapObject) mSoapObject2
						.getProperty(0);
				String code = mSoapObject3.getPropertyAsString(0).toString();
				Log.i("Code", code);
				if (code.equals("000")) {
					attendance_response = true;
					Object mSoapObject4 = mSoapObject2.getProperty(1);

					String resp = mSoapObject4.toString();
					JSONObject jobj = new JSONObject(resp);
					Msg_Status = new ArrayList<String>();
					Msg_Str = new ArrayList<String>();
					JSONArray mArray = jobj.getJSONArray("InsrtAttnDtl");
					for (int i = 0; i < mArray.length(); i++) {
						JSONObject mJsonObject = mArray.getJSONObject(i);
						Msg_Status.add(mJsonObject.getString("Msg_Status"));
						Msg_Str.add(mJsonObject.getString("Msg_Str"));
					}
				} else {
					attendance_response = false;
				}
			} catch (JSONException e) {
				server_response = true;
				e.printStackTrace();
			} catch (NullPointerException e) {
				server_response = true;
				e.printStackTrace();
			} catch (Exception e) {
				server_response = true;
				e.printStackTrace();
			}
			return null;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (server_response) {
				server_response = false;
				onDetectNetworkState().show();
			} else {
				if (attendance_response) {
					attendance_response = false;
					String msg = "";
					for (int i = 0; i < Msg_Status.size(); i++) {
						if (Msg_Status.get(i).equalsIgnoreCase("Failure")
								&& Msg_Str.get(i).equalsIgnoreCase("")) {
							msg = msg + "\n" + Msg_Str.get(i);
						} else {
							msg = msg + "\n" + Msg_Str.get(i);
						}
					}
					if (ClockinoutActivity.fromclock) {
						WW_StaticClass.InstructorID = WW_StaticClass.DeckSuperID;
						ClockinoutActivity.fromclock = false;
						Toast.makeText(context,
								"Attendance recorded successfully", Toast.LENGTH_LONG).show();
						((TodaysScheduleActivity) context).finish();
						TodaysScheduleFragment.TimeList.clear();
						TodaysScheduleFragment.showall = false;
					} else {
						//						SingleOptionAlertWithoutTitle.ShowAlertDialog(context,
						//								"LAFitnessApp", msg, "Ok");
						AlertDialog alertDialog = new AlertDialog.Builder(context).create();
						alertDialog.setTitle("LAFitnessApp");
						alertDialog
						.setIcon(context.getResources().getDrawable(R.drawable.logo));
						alertDialog.setCanceledOnTouchOutside(false);
						alertDialog.setCancelable(false);
						alertDialog.setMessage(msg);
						alertDialog.setButton("Ok",
								new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
								((TodaysScheduleActivity)context).finish();
								TodaysScheduleFragment.showall = false;
								TodaysScheduleFragment.TimeList.clear();

							}
						});
						// show the alert dialog
						alertDialog.show();
					}
				} else {
					SingleOptionAlertWithoutTitle
					.ShowAlertDialog(
							context,
							"LAFitnessApp",
							"Some internal error. Please try again after sometime",
							"Ok");
				}
			}
		}
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		if (animation == animBlink) {
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("deprecation")
	public void showAlert(Context context, String Heading, String message,
			String buttonText) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();
		// hide title bar
		// alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		alertDialog.setTitle(Heading);
		alertDialog
		.setIcon(context.getResources().getDrawable(R.drawable.logo));
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setCancelable(false);
		// set the message
		alertDialog.setMessage(message);
		// set button1 functionality
		alertDialog.setButton(buttonText,
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// close dialog

				dialog.cancel();

			}
		});
		// show the alert dialog
		alertDialog.show();
	}

	private class SubmitReason extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			SoapObject request = new SoapObject(AppConfig.NAMESPACE,
					AppConfig.Insert_SwimCompCancellation_Method);
			request.addProperty("token", token); // 1
			request.addProperty("SwimCompId", SwimCompId); // 2
			request.addProperty("StudentId", StudentId); // 3
			request.addProperty("Reason", reason); // 4
			request.addProperty("Comments", comment); // 5
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11); // Make an Envelop for sending as whole
			envelope.dotNet = true;
			envelope.setOutputSoapObject(request);
			Log.i("Request", "Request = " + request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(
					AppConfig.SOAP_ADDRESS);
			try {
				androidHttpTransport.call(
						AppConfig.Insert_SwimCompCancellation_Action, envelope); // Calling
				// Web
				// service
				SoapObject response = (SoapObject) envelope.bodyIn;
				Log.i("Attendance apdater", "Result : " + response.toString());
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				Log.i("Attendance Adapter", "mSoapObject1=" + mSoapObject1);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1
						.getProperty(0);
				Log.i("Attendance Adapter", "mSoapObject2=" + mSoapObject2);
				SoapObject mSoapObject3 = (SoapObject) mSoapObject2
						.getProperty(0);
				String code = mSoapObject3.getPropertyAsString(0).toString();
				Log.i("Code", code);
				if (code.equals("000")) {
					cancel_response = true;
				} else {
					cancel_response = false;
				}
			} catch (SocketTimeoutException e) {
				e.printStackTrace();

			} catch (SocketException e) {
				// TODO: handle exception
				e.printStackTrace();

			} catch (NullPointerException e) {
				server_response = true;
				e.printStackTrace();
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
			if (server_response) {
				server_response = false;
				onDetectNetworkState().show();
			} else {
				if (cancel_response) {
					cancel_response = false;
					Toast.makeText(context, "Add successfully..!!", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(context, "Not Add successfully..!!", Toast.LENGTH_LONG)
					.show();
				}
			}
		}
	}

	public AlertDialog onDetectNetworkState() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
		builder1.setIcon(context.getResources().getDrawable(R.drawable.logo));
		builder1.setMessage("Please turn on internet connection and try again.")
		.setTitle("No Internet Connection.")
		.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				// TODO Auto-generated method stub
				((Activity) context).finish();
				TodaysScheduleFragment.TimeList.clear();
				TodaysScheduleFragment.showall = false;
			}
		})
		.setPositiveButton("Οk", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				context.startActivity(new Intent(
						Settings.ACTION_WIRELESS_SETTINGS));
			}
		});
		return builder1.create();
	}

	public class LoginAsyn extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

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
				// Log.i("here","Result : " + response.toString());
				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
				// Log.i(Tag, "mSoapObject1="+mSoapObject1);
				SoapObject mSoapObject2 = (SoapObject) mSoapObject1
						.getProperty(0);
				// Log.i(Tag, "mSoapObject2="+mSoapObject2);
				String code = mSoapObject2.getPropertyAsString(0).toString();
				Log.i("Code", code);
				// response.toString();
				if (code.equals("000")) {
					login_status = true;
					Object mSoapObject3 = mSoapObject1.getProperty(1);
					Log.i("Adapter Login", "mSoapObject3=" + mSoapObject3);
					String resp = mSoapObject3.toString();
					JSONObject jo = new JSONObject(resp);
					UserToken = jo.getString("UserToken");
					UserLevel = jo.getString("UserLevel");
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
				server_response = true;
				e.printStackTrace();
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
			if (server_response) {
				onDetectNetworkState().show();
				server_response = false;
			} else {
				if (login_status) {
					login_status = false;
					// new Insert_Attandance().execute();
				} else {
					SingleOptionAlertWithoutTitle.ShowAlertDialog(
							temp_view.getContext(), "LAFitnessApp",
							"Please Enter Valid Username and Password", "OK");
				}
			}
		}
	}

	protected void collapse() {
		// TODO Auto-generated method stub
		int finalHeight = ll_animator_layout.getHeight();

		ValueAnimator mAnimator = slideAnimator(finalHeight, 0);

		mAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationEnd(Animator animator) {
				// Height=0, but it set visibility to GONE
				ll_animator_layout.setVisibility(View.GONE);
			}

			@Override
			public void onAnimationStart(Animator animator) {
			}

			@Override
			public void onAnimationCancel(Animator animator) {
			}

			@Override
			public void onAnimationRepeat(Animator animator) {
			}
		});
		mAnimator.start();
	}

	protected void expand() {
		// TODO Auto-generated method stub
		ll_animator_layout.setVisibility(View.VISIBLE);

		mAnimator.start();
	}
	ValueAnimator mAnimator;
	protected ValueAnimator slideAnimator(int start, int end) {

		ValueAnimator animator = ValueAnimator.ofInt(start, end);

		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				// Update Height
				int value = (Integer) valueAnimator.getAnimatedValue();

				LayoutParams layoutParams = ll_animator_layout
						.getLayoutParams();
				layoutParams.height = value;
				ll_animator_layout.setLayoutParams(layoutParams);
			}
		});
		return animator;
	}

	//Shadow Request implementation
	//	public class IamInPool extends AsyncTask<Void, Void, Void>   {
	//		@Override
	//		protected void onPreExecute() {
	//			// TODO Auto-generated method stub
	//			super.onPreExecute();
	//			//			WW_StaticClass.Siteid = ViewCurrentScheduleFragment.SiteID.get(0);
	//		}
	//
	//		@Override
	//		protected Void doInBackground(Void... params) {
	//
	//			SoapObject request = new SoapObject(AppConfig.NAMESPACE,AppConfig.METHOD_NAME_GETPOOLLIST);
	//			request.addProperty("token",  WW_StaticClass.UserToken);
	//			request.addProperty("siteid", ViewCurrentScheduleFragment.SiteID.get(0));
	//			// Log.i(Tag, "Login name"+mEd_User.getText().toString());
	//			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
	//					SoapEnvelope.VER11); // Make an Envelop for sending as whole
	//			envelope.dotNet = true;
	//			envelope.setOutputSoapObject(request);
	//			Log.i("Request",  "Request = " + request);
	//			HttpTransportSE androidHttpTransport = new HttpTransportSE(
	//					AppConfig.SOAP_ADDRESS);
	//			PoolId = new ArrayList<String>();
	//			PoolName = new ArrayList<String>();
	//			try {
	//				androidHttpTransport.call(AppConfig.SOAP_ACTION_POOLLIST, envelope); // Calling
	//				// Web
	//				// service
	//
	//				SoapObject response =  (SoapObject) envelope.getResponse();
	//				Log.i("here","Result : " + response.toString());
	//				SoapObject mSoapObject1 = (SoapObject) response.getProperty(0);
	//				Log.i("Current Lesson", "mSoapObject1="+mSoapObject1);
	//				SoapObject mSoapObject2 = (SoapObject) mSoapObject1.getProperty(0);
	//				Log.i("Current Lesson", "mSoapObject2="+mSoapObject2);
	//				String code = mSoapObject2.getPropertyAsString(0).toString();
	//				Log.i("Code", code);
	//				//			response.toString();
	//				if (code.equals("000")) {
	//					pool_status=true;
	//					Object mSoapObject3 =  mSoapObject1.getProperty(1);
	//					Log.i("Current Lesson", "mSoapObject3="+mSoapObject3);
	//					String resp = mSoapObject3.toString();
	//
	//
	//					Log.i("here","Result : " + resp.toString());
	//					JSONObject jobj = new JSONObject(resp);
	//					JSONArray mArray = jobj.getJSONArray("Pools");
	//					for (int i = 0; i < mArray.length(); i++) {
	//						JSONObject mJsonObjectFee = mArray.getJSONObject(i);
	//						PoolId.add(mJsonObjectFee.getString("PoolId"));
	//						PoolName.add(mJsonObjectFee.getString("PoolName"));
	//					}	
	//				}
	//				else{
	//					pool_status=false;
	//				}
	//			} catch (Exception e) {
	//				server_status=true;
	//				e.printStackTrace();
	//
	//			}
	//			return null;
	//		}
	//
	//
	//		@Override
	//		protected void onPostExecute(Void result) {
	//			dialog = new Dialog(context);
	//			if(server_status){
	//				SingleOptionAlertWithoutTitle.ShowAlertDialog(
	//						context,"WaterWorks", "Server not responding.\nPlease check internet connection or try after sometime.", "OK");
	//				server_status = false;
	//			}
	//			else{
	//				if(pool_status)
	//				{
	//					if(shadow_click){
	//						showDialog(request_shadow);
	//						shadow_click = false;
	//					}
	//					else if(deck_click){
	//						showDialog(request_deck);
	//						deck_click = false;
	//					}
	//					else{
	//						Log.i("Nothing Click", "Nothing Click");
	//					}
	//					pool_status = false;
	//				}
	//				else{
	//					SingleOptionAlertWithoutTitle.ShowAlertDialog(context,"WaterWorks", "No pool found", "Ok");
	//				}
	//			}
	//		}
	//	}
	//	
	//	protected Dialog onCreateDialog(int id) {
	//		switch (id) {
	//		case request_shadow:
	//			dialog = new Dialog(context);
	//			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	//			dialog.setContentView(R.layout.requestshadow);
	//			Button send_request = (Button)dialog.findViewById(R.id.btn_rs_send_request);
	//			send_request.setOnClickListener(new OnClickListener() {
	//
	//				@Override
	//				public void onClick(View v) {
	//					// TODO Auto-generated method stub
	//					//					if(Shadow_poolid.isEmpty()){
	//					//						SingleOptionAlertWithoutTitle.ShowAlertDialog(ScheduleActivity.this, "WaterWorks", "Please select pool befor requesting", "Ok");
	//					//					}
	//					dialog.dismiss();
	//					//				for(int i=0;i<WW_StaticClass.SStudnetID.size();i++){
	//					//					SStudnetid = WW_StaticClass.SStudnetID.get(i);
	//					new InsertShadowRequest().execute();
	//					//				}
	//				}
	//			});
	//
	//			LinearLayout mLinearLayout = (LinearLayout)dialog.findViewById(R.id.ll_pool_list);
	//			final RadioButton[] rb = new RadioButton[PoolName.size()];
	//			RadioGroup rg = new RadioGroup(context);
	//			rg.setOrientation(RadioGroup.HORIZONTAL);
	//			for (int i = 0; i < PoolName.size(); i++) {
	//				rb[i] = new RadioButton(context);
	//				rg.addView(rb[i]);
	//				rb[i].setText(PoolName.get(i));
	//				rb[i].setId(i);
	//				rb[i].setButtonDrawable(android.R.drawable.btn_radio);
	////				rb[i].setTextColor(getResources().getColor(R.color.texts1));
	//				rb[i].setTextSize(18);
	//
	//			}
	//			mLinearLayout.addView(rg);
	//			rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	//
	//				@Override
	//				public void onCheckedChanged(RadioGroup group, int checkedId) {
	//					// TODO Auto-generated method stub
	//					Log.i("Radio Check", ""+checkedId);
	//					Log.i("Pool Name", "Pool name = " + PoolName.get(checkedId));
	//					int a = PoolName.indexOf(PoolName.get(checkedId));
	//					Log.i("Here", ""+a);
	//					String poolidvalue = PoolId.get(a);
	//					Shadow_poolid = poolidvalue;
	//					Log.i("poolid", ""+poolidvalue);
	//
	//				}
	//
	//				@Override
	//				public void onCheckedChanged(CompoundButton buttonView,
	//						boolean isChecked) {
	//					// TODO Auto-generated method stub
	//					
	//				}
	//			});
	//			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
	//			lp.copyFrom(dialog.getWindow().getAttributes());
	//			lp.width = LayoutParams.MATCH_PARENT;
	//			lp.height = LayoutParams.WRAP_CONTENT;
	//			dialog.getWindow().setAttributes(lp);
	//			dialog.setCanceledOnTouchOutside(true);
	//
	//			break;
	//
	//		case request_deck:
	//			dialog = new Dialog(ScheduleActivity.this);
	//			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	//			dialog.setContentView(R.layout.requestdeck);
	//			final CheckBox chk_cee = (CheckBox)dialog.findViewById(R.id.chk_cee);
	//			final CheckBox chk_cee_manager = (CheckBox)dialog.findViewById(R.id.chk_cee_manager);
	//			final CheckBox chk_aquatics_manager = (CheckBox)dialog.findViewById(R.id.chk_aquatics_manager);
	//			final RadioButton rc_now=(RadioButton)dialog.findViewById(R.id.rc_now);
	//			final RadioButton rc_min=(RadioButton)dialog.findViewById(R.id.rc_min);
	//			Button rc_send_request = (Button)dialog.findViewById(R.id.btn_rc_send_request);
	//			LinearLayout mLinearLayout1 = (LinearLayout)dialog.findViewById(R.id.ll_pool_list);
	//			final RadioButton[] rb1 = new RadioButton[PoolName.size()];
	//			final RadioGroup rg1 = new RadioGroup(getApplicationContext());
	//			rg1.setOrientation(RadioGroup.HORIZONTAL);
	//			for (int i = 0; i < PoolName.size(); i++) {
	//				rb1[i] = new RadioButton(getApplicationContext());
	//				rg1.addView(rb1[i]);
	//				rb1[i].setText(PoolName.get(i));
	//				rb1[i].setId(i);
	//				rb1[i].setButtonDrawable(android.R.drawable.btn_radio);
	//				rb1[i].setTextColor(getResources().getColor(R.color.texts1));
	//				rb1[i].setTextSize(18);
	//
	//			}
	//			mLinearLayout1.addView(rg1);
	//			rg1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	//
	//				@Override
	//				public void onCheckedChanged(RadioGroup group, int checkedId) {
	//					// TODO Auto-generated method stub
	//					try{
	//						int a = PoolName.indexOf(PoolName.get(checkedId));
	//						Log.i("Here", ""+a);
	//						String poolidvalue = PoolId.get(a);
	//						Log.i("poolid", ""+poolidvalue);
	//						//												desk_poolid = poolidvalue;
	//
	//						for(int j=0;j<PoolName.size();j++){
	//							rb1[j].setError(null);
	//						}
	//					}
	//					catch(Exception e){
	//						e.printStackTrace();
	//					}
	//				}
	//			});
	//
	//
	//
	//			rc_now.setOnClickListener(new OnClickListener() {
	//
	//				@Override
	//				public void onClick(View v) {
	//					// TODO Auto-generated method stub
	//					rc_now.setChecked(true);
	//					rc_min.setChecked(false);
	//					whattimeforassist = "1";
	//					rc_min.setError(null);
	//					rc_now.setError(null);
	//				}
	//			});
	//			rc_min.setOnClickListener(new OnClickListener() {
	//
	//				@Override
	//				public void onClick(View v) {
	//					// TODO Auto-generated method stub
	//					rc_now.setChecked(false);
	//					rc_min.setChecked(true);
	//					whattimeforassist = "2";
	//					rc_min.setError(null);
	//					rc_now.setError(null);
	//				}
	//			});
	//			rc_send_request.setOnClickListener(new OnClickListener() {
	//
	//				@Override
	//				public void onClick(View v) {
	//					// TODO Auto-generated method stub
	//					if(desk_poolid.equalsIgnoreCase("-1")&&whattimeforassist.equalsIgnoreCase("-1")){
	//						for(int i=0;i<PoolName.size();i++){
	//							rb1[i].setError("Please select anyone option");
	//						}
	//						rc_min.setError("Please select anyone option");
	//						rc_now.setError("Please select anyone option");
	//					}
	//					else if(desk_poolid.equalsIgnoreCase("-1")){
	//						for(int i=0;i<PoolName.size();i++){
	//							rb1[i].setError("Please select anyone option");
	//						}
	//					}
	//					else if(whattimeforassist.equalsIgnoreCase("-1")){
	//						rc_min.setError("Please select anyone option");
	//						rc_now.setError("Please select anyone option");
	//					}
	//					else{
	//						if(chk_cee.isChecked()){
	//							emp_type_for_cee = "1";
	//							emp_userid_for_cee ="-1";
	//
	//						}
	//						if(chk_cee_manager.isChecked()){
	//							emp_type_for_cee_manager = "2";
	//							emp_userid_for_cee_manager ="-1";
	//
	//						}
	//						if(chk_aquatics_manager.isChecked()){
	//							emp_type_for_aquatics = "3";
	//							emp_userid_for_aquatics ="-1";
	//
	//						}
	//						//						new InsertRequestDesk().execute();
	//						chk_aquatics_manager.setChecked(false);
	//						chk_cee.setChecked(false);
	//						chk_cee_manager.setChecked(false);
	//						rc_min.setChecked(false);
	//						rc_now.setChecked(false);
	//						rg1.clearCheck();
	//
	//						desk_poolid = "-1";
	//						whattimeforassist = "-1";
	//						dialog.dismiss();
	//					}
	//				}
	//			});
	//
	//			dialog.setOnCancelListener(new OnCancelListener() {
	//
	//				@Override
	//				public void onCancel(DialogInterface dialog) {
	//					// TODO Auto-generated method stub
	//					chk_aquatics_manager.setChecked(false);
	//					chk_cee.setChecked(false);
	//					chk_cee_manager.setChecked(false);
	//					rc_min.setChecked(false);
	//					rc_now.setChecked(false);
	//					rg1.clearCheck();
	//					desk_poolid = "-1";
	//					whattimeforassist = "-1";
	//				}
	//			});
	//
	//			WindowManager.LayoutParams lp1 = new WindowManager.LayoutParams();
	//			lp1.copyFrom(dialog.getWindow().getAttributes());
	//			lp1.width = LayoutParams.MATCH_PARENT;
	//			lp1.height = LayoutParams.WRAP_CONTENT;
	//			dialog.getWindow().setAttributes(lp1);
	//			dialog.setCanceledOnTouchOutside(true);
	//			break;
	//		}
	//		return dialog;
	//	}

}
