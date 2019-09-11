package com.hbhgdating.chat;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hbhgdating.R;
import com.hbhgdating.utils.SharedPref;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StickersGridViewAdapter extends BaseAdapter {

	private Activity mactivity;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder;
	ArrayList<String> mainList = new ArrayList<String>();
	int Type;
	SharedPref sp;



	private static class ViewHolder {
		ImageView grid_item_image;
	}

	public StickersGridViewAdapter(Activity con, ArrayList<String> mlist,
								   int type) {
		mactivity = con;
		this.mainList = mlist;
		this.Type = type;
		inflater = LayoutInflater.from(mactivity);
		sp = new SharedPref(con);
	}

	@Override
	public int getCount() {
		return mainList.size();
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
			convertView = inflater.inflate(R.layout.google_grid_item_layout, parent,
					false);
			viewHolder = new ViewHolder();

			viewHolder.grid_item_image = (ImageView) convertView
					.findViewById(R.id.grid_item_image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();

		}

		int id = mactivity.getResources().getIdentifier(mainList.get(position),
				"mipmap", mactivity.getPackageName());
		Picasso.with(mactivity).load(id).fit()
			    	.centerInside()
					.into(viewHolder.grid_item_image);
		viewHolder.grid_item_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Type == 1) {
					detail(position);

				} else if (Type == 2) {
					detail(position);
					if (sp.getBoolPref("stickerpack1")) {
						detail(position);
					} else {
						//inAppBilling(1);
					}
				}else if (Type == 3) {
					detail(position);
					if (sp.getBoolPref("stickerpack2")) {
						detail(position);
					} else {
						//inAppBilling(2);
					}
				}else if (Type == 4) {
					detail(position);
					if (sp.getBoolPref("stickerpack3")) {
						detail(position);
					} else {
						//inAppBilling(3);
					}
				}else if (Type == 5) {
					detail(position);
					if (sp.getBoolPref("stickerpack4")) {
						detail(position);
					} else {
						//inAppBilling(4);
					}
				}
			}
		});
		return convertView;
	}

	@SuppressWarnings("static-access")
	private void detail(int pos) {
		Intent intent = new Intent();
		intent.putExtra("stickername", mainList.get(pos));
		mactivity.setResult(mactivity.RESULT_OK, intent);
		mactivity.finish();
	}

	/*private void inAppBilling(int inapptype) {
		if (!((StickersView) mactivity).readyToPurchase) {
			Common.customToast(mactivity,
					"Billing not initialized.Please connect internet!!",
					Common.displayType.CENTER);
			return;
		}
		if (inapptype == 1) {
			((StickersView) mactivity).bp_main.purchase(mactivity
					.getString(R.string.inapp_stickerpack1));
		} else if (inapptype == 2) {
			((StickersView) mactivity).bp_main.purchase(mactivity
					.getString(R.string.inapp_stickerpack2));
		}else if (inapptype == 3) {
			((StickersView) mactivity).bp_main.purchase(mactivity
					.getString(R.string.inapp_stickerpack3));
		}else if (inapptype == 4) {
			((StickersView) mactivity).bp_main.purchase(mactivity
					.getString(R.string.inapp_stickerpack4));
		}
	}*/
}
