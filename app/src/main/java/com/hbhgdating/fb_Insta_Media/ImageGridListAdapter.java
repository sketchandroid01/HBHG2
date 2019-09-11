package com.hbhgdating.fb_Insta_Media;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hbhgdating.R;
import com.hbhgdating.lazyload.ImageLoader;

import java.util.ArrayList;


public class ImageGridListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<String> imageThumbList;
	private LayoutInflater inflater;
	private ImageLoader imageLoader;
	SparseBooleanArray mSparseBooleanArray;
	TextView tv_done;

	CheckBox[] ckbDigits;



	public ImageGridListAdapter(Context context_, ArrayList<String> imageThumbList, TextView tv_done_) {
		inflater = (LayoutInflater) context_
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		this.context = context_;
		this.imageThumbList = imageThumbList;
		this.imageLoader = new ImageLoader(context);
		this.tv_done = tv_done_;
		tv_done.setVisibility(View.GONE);
		this.mSparseBooleanArray = new SparseBooleanArray();

		ckbDigits = new CheckBox[imageThumbList.size()];
	}


	public ArrayList<String> getCheckedItems() {
		ArrayList<String> mTempArry = new ArrayList<String>();

		for(int i=0;i<imageThumbList.size();i++) {
			if(mSparseBooleanArray.get(i)) {
				mTempArry.add(imageThumbList.get(i));
			}
		}

		return mTempArry;
	}





	@Override
	public int getCount() {
		return imageThumbList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, final View convertView, ViewGroup parent) {
		View view = inflater.inflate(R.layout.image_adapter_item, null);
		final Holder holder = new Holder();
		holder.ivPhoto = (ImageView) view.findViewById(R.id.ivImage);
		imageLoader.DisplayImage(imageThumbList.get(position), holder.ivPhoto);


		holder.mCheckBox = (CheckBox) view.findViewById(R.id.mCheckBox);

		holder.mCheckBox.setTag(position);
		holder.mCheckBox.setChecked(mSparseBooleanArray.get(position));



		holder.ivPhoto.setId(position);
		holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				ImageView iv = (ImageView) v;
				int id = iv.getId();

				//CheckBox cb = ckbDigits[id];

				//Log.d("TAG", "image position = "+id);

				holder.mCheckBox.setChecked(mSparseBooleanArray.get(id));
				if (mSparseBooleanArray.get(id)){

					mCheckedChangeListener.onCheckedChanged(holder.mCheckBox, false);
				}else {

					mCheckedChangeListener.onCheckedChanged(holder.mCheckBox, true);
				}

				notifyDataSetChanged();

				//ckbDigits[position].setOnCheckedChangeListener(mCheckedChangeListener);

			}
		});


		return view;
	}

	private class Holder {
		private ImageView ivPhoto;
		private CheckBox mCheckBox;
	}


	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub

			if (isChecked){


				if (geLength() > 6){
					buttonView.setChecked(false);
					Toast.makeText(context , "Select only 7 pictures" ,Toast.LENGTH_LONG).show();

				}else {
					mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);
					for (int i = 0; i < imageThumbList.size(); i++){
						if (mSparseBooleanArray.get(i)){

							tv_done.setVisibility(View.VISIBLE);

							break;
						}
						else {

							tv_done.setVisibility(View.INVISIBLE);
						}

					}

				}

				//Log.d("TAG" , "geLength > "+geLength());

			}else {

				mSparseBooleanArray.put((Integer) buttonView.getTag(), isChecked);

				for (int i = 0; i < imageThumbList.size(); i++){

					if (mSparseBooleanArray.get(i)){

						tv_done.setVisibility(View.VISIBLE);

						break;
					}
					else {

						tv_done.setVisibility(View.INVISIBLE);
					}

				}

				//Log.d("TAG" , "geLength > "+geLength());

			}

		}
	};


	private int geLength() {
		ArrayList<String> mTempArry = new ArrayList<String>();

		for(int i=0;i<imageThumbList.size();i++) {
			if(mSparseBooleanArray.get(i)) {
				mTempArry.add(imageThumbList.get(i));
			}
		}

		return mTempArry.size();
	}


}
