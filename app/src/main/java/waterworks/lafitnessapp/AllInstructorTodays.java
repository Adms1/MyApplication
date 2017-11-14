package waterworks.lafitnessapp;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import waterworks.lafitnessapp.model.AllInstructorItems;

public class AllInstructorTodays  extends BaseAdapter {

	private Context context;
	private ArrayList<AllInstructorItems> navDrawerItems;

	public AllInstructorTodays(Context context,
			ArrayList<AllInstructorItems> navDrawerItems) {
		this.context = context;
		this.navDrawerItems = navDrawerItems;
	}

	@Override
	public int getCount() {
		return navDrawerItems.size();
	}

	@Override
	public Object getItem(int position) {
		return navDrawerItems.get(position);
	}

	@Override
	public long getItemId(int position) {
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
	public class ViewHolder
    {
		TextView txtTitle;
		CheckBox checkbox;
    }

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			LayoutInflater mInflater = (LayoutInflater) context
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = mInflater.inflate(R.layout.drawer_list_item, null);
			holder.txtTitle = (TextView) convertView.findViewById(R.id.title);
			holder.checkbox = (CheckBox)convertView.findViewById(R.id.checkbox);
			holder.txtTitle.setText(navDrawerItems.get(position).getTitle());
			holder.checkbox.setVisibility(View.GONE);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		return convertView;
	}

}
