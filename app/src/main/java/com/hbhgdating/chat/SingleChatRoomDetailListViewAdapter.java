package com.hbhgdating.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.hbhgdating.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;

public class SingleChatRoomDetailListViewAdapter extends BaseAdapter {

	private Activity mactivity;
	private LayoutInflater inflater = null;
	private ViewHolder viewHolder;
	ArrayList<ChatMessage> mainList = new ArrayList<ChatMessage>();
	public static MediaPlayer mediaPlayer;
	int userID = 0;

	public static class ViewHolder {
		ImageView imageView15, imageView25,img_giphy,img_giphy2;
		TextView textView1, textView2, textView1Date, textView2Date,
				textView1ImgDate, textView2ImgDate,textView1GifDate,textView2GifDate;
		RelativeLayout relMy1, relMy2, relimgView1,relimgView2,relgifView1,relgifView2;
		LinearLayout linMsg1, linMsg2;
		RoundedImageView imageView11, imageView22;
		DonutProgress donutProgress_iv22;
	}

	public SingleChatRoomDetailListViewAdapter(Activity con, ArrayList<ChatMessage> mlist) {
		mactivity = con;
		this.mainList = mlist;
		this.userID = userID;
		inflater = LayoutInflater.from(mactivity);


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
			convertView = inflater.inflate(R.layout.chatdetail_listitem_single,
					parent, false);
			viewHolder = new ViewHolder();

			viewHolder.imageView11 = (RoundedImageView) convertView
					.findViewById(R.id.imageView11);
			viewHolder.imageView15 = (ImageView) convertView
					.findViewById(R.id.imageView15);
			viewHolder.imageView22 = (RoundedImageView) convertView
					.findViewById(R.id.imageView22);
			viewHolder.imageView25 = (ImageView) convertView
					.findViewById(R.id.imageView25);
			viewHolder.img_giphy = (ImageView) convertView
					.findViewById(R.id.img_giphy);
			viewHolder.img_giphy2 = (ImageView) convertView
					.findViewById(R.id.img_giphy2);
			viewHolder.textView1 = (TextView) convertView
					.findViewById(R.id.textView1);
			viewHolder.textView1Date = (TextView) convertView
					.findViewById(R.id.textView1Date);
			viewHolder.textView1ImgDate = (TextView) convertView
					.findViewById(R.id.textView1ImgDate);
			viewHolder.textView1GifDate = (TextView) convertView
					.findViewById(R.id.textView1GifDate);
			viewHolder.textView2GifDate = (TextView) convertView
					.findViewById(R.id.textView2GifDate);
			viewHolder.textView2 = (TextView) convertView
					.findViewById(R.id.textView2);
			viewHolder.textView2ImgDate = (TextView) convertView
					.findViewById(R.id.textView2ImgDate);
			viewHolder.textView2Date = (TextView) convertView
					.findViewById(R.id.textView2Date);
			viewHolder.relMy1 = (RelativeLayout) convertView
					.findViewById(R.id.relMy1);
			viewHolder.relimgView1 = (RelativeLayout) convertView
					.findViewById(R.id.relimgView1);
			viewHolder.relimgView2 = (RelativeLayout) convertView
					.findViewById(R.id.relimgView2);
			viewHolder.relMy2 = (RelativeLayout) convertView
					.findViewById(R.id.relMy2);
			viewHolder.relgifView1 = (RelativeLayout) convertView
					.findViewById(R.id.relgifView1);
			viewHolder.relgifView2 = (RelativeLayout) convertView
					.findViewById(R.id.relgifView2);
			viewHolder.linMsg1 = (LinearLayout) convertView
					.findViewById(R.id.linMsg1);
			viewHolder.linMsg2 = (LinearLayout) convertView
					.findViewById(R.id.linMsg2);


			viewHolder.donutProgress_iv22=(DonutProgress)convertView.findViewById(R.id.donut_progress_iv22);


			convertView.setTag(viewHolder);


		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}


		if (mainList.get(position).type.matches("text")) {

			if (mainList.get(position).isMine) {

				viewHolder.relMy1.setVisibility(View.VISIBLE);
				viewHolder.relMy2.setVisibility(View.GONE);

				viewHolder.textView1.setText(mainList.get(position).body);
				viewHolder.textView1Date.setText(mainList.get(position).Date);
				viewHolder.linMsg1.setVisibility(View.VISIBLE);
				viewHolder.relimgView1.setVisibility(View.GONE);
				viewHolder.relgifView1.setVisibility(View.GONE);

			}else{

				viewHolder.relMy2.setVisibility(View.VISIBLE);
				viewHolder.relMy1.setVisibility(View.GONE);

				viewHolder.textView2.setText(mainList.get(position).body);
				viewHolder.textView2Date.setText(mainList.get(position).Date);
				viewHolder.linMsg2.setVisibility(View.VISIBLE);
				viewHolder.relimgView2.setVisibility(View.GONE);
				viewHolder.relgifView2.setVisibility(View.GONE);

			}

		}else if (mainList.get(position).type.matches("img")){

			if (mainList.get(position).isMine) {

				viewHolder.relMy1.setVisibility(View.VISIBLE);
				viewHolder.relMy2.setVisibility(View.GONE);

				//checking if image is just sent or loaded via api
				if (mainList.get(position).note.matches("loaded")){

					//viewHolder.donutProgress_iv22.setVisibility(View.GONE);

					Picasso.with(mactivity).load(mainList.get(position).img).error(R.mipmap.grid_bg_3).into( viewHolder.imageView11, new Callback() {
						@Override
						public void onSuccess() {
							//  Log.d("TAG", "onSuccess");
						}

						@Override
						public void onError() {
							//  Toast.makeText(mactivity, "An error occurred", Toast.LENGTH_SHORT).show();
						}
					});
					viewHolder.imageView11.setScaleType(ImageView.ScaleType.CENTER_CROP);

				}else {

					try {

						//viewHolder.donutProgress_iv22.setVisibility(View.VISIBLE);

						//Log.d("imageadapter", "getView: " + mainList.get(position).img);

						InputStream inputStream = mactivity.getContentResolver().openInputStream(Uri.parse(mainList.get(position).img));
						Bitmap bmp = BitmapFactory.decodeStream(inputStream);
						if (inputStream != null) inputStream.close();

						viewHolder.imageView11.setImageURI(Uri.parse(mainList.get(position).img));
						viewHolder.imageView11.setScaleType(ImageView.ScaleType.CENTER_CROP);

					} catch (Exception e) {

					}
				}

					viewHolder.relimgView1.setVisibility(View.VISIBLE);
					viewHolder.linMsg1.setVisibility(View.GONE);
					viewHolder.relgifView1.setVisibility(View.GONE);
					viewHolder.textView1ImgDate.setText(mainList.get(position).Date);


			}else {

				viewHolder.relMy2.setVisibility(View.VISIBLE);
				viewHolder.relMy1.setVisibility(View.GONE);

				Picasso.with(mactivity).load(mainList.get(position).img).error(R.mipmap.grid_bg_3).into( viewHolder.imageView22, new Callback() {
					@Override
					public void onSuccess() {
						//  Log.d("TAG", "onSuccess");
					}

					@Override
					public void onError() {
						//  Toast.makeText(mactivity, "An error occurred", Toast.LENGTH_SHORT).show();
					}
				});

				viewHolder.imageView22.setScaleType(ImageView.ScaleType.CENTER_CROP);

				viewHolder.relimgView2.setVisibility(View.VISIBLE);
				viewHolder.linMsg2.setVisibility(View.GONE);
				viewHolder.relgifView2.setVisibility(View.GONE);
				viewHolder.textView2ImgDate.setText(mainList.get(position).Date);


			}


		}else if (mainList.get(position).type.matches("sticker")){

			Context c = getApplicationContext();
			int id = c.getResources().getIdentifier("mipmap/" + mainList.get(position).body, null, c.getPackageName());
			//Log.d("sticker", "getView: " + id);

			if (mainList.get(position).isMine) {

				viewHolder.relMy1.setVisibility(View.VISIBLE);
				viewHolder.relMy2.setVisibility(View.GONE);


				viewHolder.imageView11.setImageResource(id);
				viewHolder.imageView11.setScaleType(ImageView.ScaleType.FIT_XY);

				viewHolder.relimgView1.setVisibility(View.VISIBLE);
				viewHolder.linMsg1.setVisibility(View.GONE);
				viewHolder.relgifView1.setVisibility(View.GONE);
				viewHolder.textView1ImgDate.setText(mainList.get(position).Date);

			}else {

				viewHolder.relMy2.setVisibility(View.VISIBLE);
				viewHolder.relMy1.setVisibility(View.GONE);


				viewHolder.imageView22.setImageResource(id);
				viewHolder.imageView22.setScaleType(ImageView.ScaleType.FIT_XY);
				viewHolder.relimgView2.setVisibility(View.VISIBLE);
				viewHolder.linMsg2.setVisibility(View.GONE);
				viewHolder.relgifView2.setVisibility(View.GONE);
				viewHolder.textView2ImgDate.setText(mainList.get(position).Date);


			}

		} else if (mainList.get(position).type.matches("gif")){


			if (mainList.get(position).isMine) {

				viewHolder.relMy1.setVisibility(View.VISIBLE);
				viewHolder.relMy2.setVisibility(View.GONE);


				Glide.with(mactivity).load(mainList.get(position).body).into(viewHolder.img_giphy);
				viewHolder.relimgView1.setVisibility(View.GONE);
				viewHolder.linMsg1.setVisibility(View.GONE);
				viewHolder.relgifView1.setVisibility(View.VISIBLE);
				viewHolder.textView1GifDate.setText(mainList.get(position).Date);

			}else {

				viewHolder.relMy2.setVisibility(View.VISIBLE);
				viewHolder.relMy1.setVisibility(View.GONE);


				Glide.with(mactivity).load(mainList.get(position).body).into(viewHolder.img_giphy2);
				viewHolder.relimgView2.setVisibility(View.GONE);
				viewHolder.linMsg2.setVisibility(View.GONE);
				viewHolder.relgifView2.setVisibility(View.VISIBLE);
				viewHolder.textView2GifDate.setText(mainList.get(position).Date);

			}


		}

		viewHolder.imageView11.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (mainList.get(position).type.matches("img")) {

					Intent intent = new Intent(mactivity, ChatImageFullScreen.class);
					intent.putExtra("img", mainList.get(position).img);
					intent.putExtra("key", mainList.get(position).note);
					mactivity.startActivity(intent);
				}

			}
		});

		viewHolder.imageView22.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (mainList.get(position).type.matches("img")) {

					Intent intent = new Intent(mactivity, ChatImageFullScreen.class);
					intent.putExtra("img", mainList.get(position).img);
					intent.putExtra("key", mainList.get(position).note);
					mactivity.startActivity(intent);
				}

			}
		});
		return convertView;
	}




}
