package com.hbhgdating.Chat;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hbhgdating.R;
import com.hbhgdating.utils.All_Constants_Urls;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class ListAdapterFriends extends BaseAdapter {

	private Activity mactivity;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder;
	ArrayList<HashMap<String, String>> finallist;

	ArrayList<String> selectedStrings;
	ArrayList<Boolean> checked_array;

	private static class ViewHolder {
		TextView list_item_textname;
		CheckBox cb;
		RoundedImageView iv_user;
	}

	public ListAdapterFriends(Activity con, ArrayList<HashMap<String, String>> arl) {
		mactivity = con;
		this.finallist = arl;
		inflater = LayoutInflater.from(mactivity);

		selectedStrings = new ArrayList<String>();
		checked_array = new ArrayList<Boolean>();

		setDataOnChecked_array();
	}

	public void setDataOnChecked_array(){

		for (int i = 0; i < finallist.size(); i++){
			checked_array.add(false);
		}


	}

	public ArrayList<String> getSelectedString(){
		return selectedStrings;
	}

	@Override
	public int getCount() {
		return finallist.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = inflater.inflate(R.layout.list_invite_layout, parent, false);
		viewHolder = new ViewHolder();
		viewHolder.list_item_textname = (TextView) view.findViewById(R.id.list_item_textname);
		viewHolder.cb = (CheckBox) view.findViewById(R.id.cb);
		viewHolder.iv_user = (RoundedImageView) view.findViewById(R.id.iv_user);


		viewHolder.list_item_textname.setText(finallist.get(position).get("name"));

		Picasso.with(mactivity).load(finallist.get(position).get("profile_image")).error(R.mipmap.grid_bg_3).into( viewHolder.iv_user, new Callback() {
			@Override
			public void onSuccess() {
				//  Log.d("TAG", "onSuccess");
			}
			@Override
			public void onError() {
				//  Toast.makeText(mactivity, "An error occurred", Toast.LENGTH_SHORT).show();
			}
		});


		viewHolder.cb.setChecked(checked_array.get(position));
		viewHolder.cb.setTag(position);

		viewHolder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if(isChecked){
					setChangedData((Integer) buttonView.getTag(), isChecked);

					selectedStrings.add(finallist.get(position).get("id"));
				}else{
					setChangedData((Integer) buttonView.getTag(), isChecked);

					selectedStrings.remove(finallist.get(position).get("id"));

				}
			}
		});


		view.setVisibility(View.VISIBLE);


		return view;
	}


	public void setChangedData(int pos, boolean boo){
		checked_array.set(pos, boo);

		Log.d(All_Constants_Urls.TAG, "checked_array = "+checked_array);

		notifyDataSetChanged();
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
}
