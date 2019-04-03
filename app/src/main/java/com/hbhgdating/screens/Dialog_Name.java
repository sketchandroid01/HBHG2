package com.hbhgdating.screens;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbhgdating.R;
import com.hbhgdating.utils.Common;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class Dialog_Name extends Dialog implements OnClickListener {

	TextView tTitle, tMessage;
	EditText edit1;
	LinearLayout linSave, linCacel;
	public int btnclick = 0;
	private Context mctx;
	ImageView img_dialog;
	ImageLoaderConfiguration config;
	public ImageLoader loader;



	public Dialog_Name(final Context context, int type, String strValue, String selectedImage) {
		super(context, android.R.style.Theme_Translucent);
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		setContentView(R.layout.dialoag_name);
		mctx = context;


		config = new ImageLoaderConfiguration.Builder(context)
				.memoryCache(new WeakMemoryCache())
				.diskCacheSize(100 * 1024 * 1024).build();
		ImageLoader.getInstance().init(config);
		loader = ImageLoader.getInstance();


		tTitle = (TextView) findViewById(R.id.tTitle);
		tMessage = (TextView) findViewById(R.id.tMessage);
		edit1 = (EditText) findViewById(R.id.edit1);
		edit1.setTextColor(Color.parseColor("#000000"));
		linSave = (LinearLayout) findViewById(R.id.linSave);
		linCacel = (LinearLayout) findViewById(R.id.linCacel);

		img_dialog = (ImageView)findViewById(R.id.img_dialog);


		//String selectedImage = List_Image_Uri.get(position).get("p3" + position);




		if (selectedImage.startsWith("http")) {


			loader.displayImage(selectedImage, img_dialog);


		} else  {

			Uri uri = Uri.parse(selectedImage);

			InputStream image_stream = null;
			try {
				image_stream = context.getContentResolver().openInputStream(uri);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			Bitmap bitmap = BitmapFactory.decodeStream(image_stream);

			img_dialog.setImageBitmap(bitmap);
			//img_dialog.setScaleType(ImageView.ScaleType.CENTER_CROP);


		}


		edit1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				boolean handled = false;
				if (actionId == EditorInfo.IME_ACTION_DONE) {
				/*	InputMethodManager imm = (InputMethodManager)mctx.getSystemService(context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(edit1.getWindowToken(), 0);
					*/
					if (edit1.getText().toString().trim().length() > 0) {
						btnclick = 1;
						dismiss();

					} else {
						Common.customToast((Activity)mctx, "Please enter name", Common.displayType.CENTER);
					}
					handled = true;
				}
				return handled;
			}
		});



		linSave.setOnClickListener(this);
		linCacel.setOnClickListener(this);
		//edit1.setText(strValue);
		edit1.setFocusable(true);
		getWindow().setSoftInputMode (LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

		switch (type) {
			case 0:
				tTitle.setText(context.getResources().getString(R.string.foodName));
				tMessage.setText(context.getResources().getString(R.string.foodph));
				edit1.setHint(context.getResources().getString(R.string.foodName));
				break;
			case 1:
				tTitle.setText(context.getResources().getString(
						R.string.interestName));
				tMessage.setText(context.getResources().getString(
						R.string.interestph));
				edit1.setHint(context.getResources().getString(
						R.string.interestName));

				break;
			case 2:

				tTitle.setText(context.getResources().getString(R.string.musicName));
				tMessage.setText(context.getResources().getString(R.string.musicph));
				edit1.setHint(context.getResources().getString(R.string.musicName));
				break;

			default:
				break;
		}
	}

	public void onClick(View v) {
		if (v == linSave) {
			if (edit1.getText().toString().trim().length() > 0) {
				btnclick = 1;
				dismiss();
			} else {
				Common.customToast((Activity)mctx, "Please enter name", Common.displayType.CENTER);
			}
		} else if (v == linCacel) {
			btnclick = 0;
			dismiss();
		}
	}
}
