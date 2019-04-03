package com.hbhgdating.adapter;

import android.app.Activity;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hbhgdating.R;
import com.hbhgdating.utils.All_Constants_Urls;
import com.hbhgdating.utils.Global_Class;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {

	private Activity mactivity;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder;
	public static MediaPlayer mediaPlayer;
	ArrayList<String> My_matches_list;

	Global_Class global_class;

	private static class ViewHolder {
		ImageView grid_item_image, iv_new;
	}

	public GridViewAdapter(Activity con, ArrayList<String> my_list) {
		mactivity = con;
		this.My_matches_list = my_list;
		inflater = LayoutInflater.from(mactivity);
		global_class = (Global_Class)con.getApplicationContext();
		global_class.list_is_new.clear();

		setBooleanData();
	}

	public void setBooleanData(){
		Log.d(All_Constants_Urls.TAG, "GridViewAdapter size = "+My_matches_list.size());
		for (int i = 0; i < My_matches_list.size(); i++){
			try {

				JSONObject object = new JSONObject(My_matches_list.get(i));
				String is_new = object.getString("is_new");
				if (is_new.matches("1")){
					global_class.list_is_new.add(true);
				}else {
					global_class.list_is_new.add(false);
				}


			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}


	@Override
	public int getCount() {
		return My_matches_list.size();
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
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.google_grid_item_layout, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.grid_item_image = (ImageView) convertView.findViewById(R.id.grid_item_image);
			viewHolder.iv_new = (ImageView) convertView.findViewById(R.id.iv_new);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();

		}



		//viewHolder.grid_item_image .setImageResource(mThumbIds[0]);

		viewHolder.grid_item_image.setScaleType(ImageView.ScaleType.CENTER_CROP);

		viewHolder.iv_new.setVisibility(View.GONE);

		try{

			JSONObject object = new JSONObject(My_matches_list.get(position));

			JSONObject basic_info = object.getJSONObject(All_Constants_Urls.basic_info);

			////////////

			String profile_image = basic_info.getString(All_Constants_Urls.profile_image);

			Picasso.with(mactivity).load(profile_image).error(R.mipmap.grid_bg_3).into( viewHolder.grid_item_image, new Callback() {
				@Override
				public void onSuccess() {
					//  Log.d("TAG", "onSuccess");
				}

				@Override
				public void onError() {
					//  Toast.makeText(mactivity, "An error occurred", Toast.LENGTH_SHORT).show();
				}
			});





			if (global_class.list_is_new.get(position)){
				viewHolder.iv_new.setVisibility(View.VISIBLE);
			}



		}catch (Exception e){
			e.printStackTrace();
		}



		return convertView;
	}


	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	public void updateBooleanData(int pos){
		global_class.list_is_new.set(pos, false);
		notifyDataSetChanged();
	}



}
