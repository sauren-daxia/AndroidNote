package com.example.example1_expandablelistview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyExpandListAdapter extends BaseExpandableListAdapter {
	private List<String> list = new ArrayList<String>();
	private Map<String, List<String>> map = new HashMap<String, List<String>>();

	private LayoutInflater inflater;
	private List<String> items1 = new ArrayList<String>();
	private List<String> items2 = new ArrayList<String>();

	public MyExpandListAdapter(List<String> list,
			Map<String, List<String>> map, Context context) {
		super();
//		this.list = list;
//		this.map = map;
		this.inflater = LayoutInflater.from(context);
		initDates();
	}

	public void initDates() {
		list.add("∫√”—");
		list.add("≈Û”—");

		for (int i = 0; i < 20; i++) {
			items1.add("∫√”—" + i);
		}
		map.put(list.get(0), items1);
		for (int i = 0; i < 20; i++) {
			items2.add("≈Û”—" + i);
		}
		map.put(list.get(1), items2);
	}

	@Override
	public List getChild(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return map.get(arg0);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}

	@Override
	public int getChildrenCount(int arg0) {
		// TODO Auto-generated method stub

		return map.get(list.get(arg0)).size();
	}

	@Override
	public String getGroup(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public long getGroupId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public View getGroupView(int position, boolean arg1, View convertView,
			ViewGroup arg3) {
		GroupViewHolder holder = null;
		if (convertView == null) {
			holder = new GroupViewHolder();
			convertView = inflater.inflate(R.layout.activity_expandable_groups,
					null);
			holder.groupHeadImage = (ImageView) convertView
					.findViewById(R.id.groupHeadImage);
			holder.groupNameText = (TextView) convertView
					.findViewById(R.id.groupNameText);
			convertView.setTag(holder);
		} else {
			holder = (GroupViewHolder) convertView.getTag();
		}
		holder.groupHeadImage.setImageResource(R.drawable.ic_launcher);
		holder.groupNameText.setText(list.get(position));
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean arg2, View convertView, ViewGroup arg4) {
		ChildViewHolder holder = null;
		if (convertView == null) {
			holder = new ChildViewHolder();
			convertView = inflater.inflate(R.layout.activity_expandable_items,
					null);
			holder.userNameText = (TextView) convertView
					.findViewById(R.id.userNameText);
			convertView.setTag(holder);
		} else {
			holder = (ChildViewHolder) convertView.getTag();
		}

		holder.userNameText.setText(map.get(list.get(groupPosition)).get(
				childPosition));
		return convertView;
	}

	class ChildViewHolder {
		TextView userNameText;
	}

	class GroupViewHolder {
		ImageView groupHeadImage;
		TextView groupNameText;
	}

}
