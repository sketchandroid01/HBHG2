package com.hbhgdating.screens;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hbhgdating.Chat.Chat_screen_new;
import com.hbhgdating.DatabaseLocal.DatabaseHelper;
import com.hbhgdating.R;
import com.hbhgdating.adapter.Notification_Adapter;
import com.hbhgdating.utils.All_Constants_Urls;
import com.hbhgdating.utils.Common;
import com.hbhgdating.utils.ConnectivityReceiver;
import com.hbhgdating.utils.SharedPref;
import com.hbhgdating.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class NotificationActivity extends Activity {
	ListView listView;
	ImageView img_delete_all;
	TextView empty;
	SharedPref sharedPref;
	Dialog progressDialog;
	//private InterstitialAd interstitial;
	boolean doubleBackToExitPressedOnce = false;
	ArrayList<String> List_Noification;
	String Notifications_Ids;
	private Notification_Adapter notification_adapter;
	private DatabaseHelper databaseHelper;
	Utility utility;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.notification);


		NotificationActivity.this.registerReceiver(mMessageReceiver, new IntentFilter(Common.Key_Notification));

		utility = new Utility(this);
		databaseHelper = new DatabaseHelper(this);
		sharedPref = new SharedPref(this);
		progressDialog = new Dialog(this, android.R.style.Theme_Translucent);
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progressDialog.getWindow().setContentView(R.layout.progressbar_pleasewait);
		progressDialog.setCancelable(false);
		List_Noification = new ArrayList<>();



		setView();


		getUserBlockOrNot();

		//loadInterstitialAD();
	}

	@Override
	protected void onStart() {
		//getData();
		super.onStart();
	}
	private void loadInterstitialAD() {
		/*interstitial = new InterstitialAd(NotificationActivity.this);
		interstitial.setAdUnitId(getResources().getString(
				R.string.interstitial_ad_unit_id));
		AdRequest adRequest = new AdRequest.Builder().build();
		interstitial.loadAd(adRequest);
		interstitial.setAdListener(new AdListener() {
			public void onAdLoaded() {
				displayInterstitial();
			}

			public void onAdClosed() {
				
			}
		});*/

	}

	public void displayInterstitial() {
		/*if (interstitial.isLoaded()) {
			interstitial.show();
		}*/
	}



	private void setView() {


		setBottom();
		listView = (ListView) findViewById(R.id.listView);
		//listView.setStackFromBottom(true);
		//listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		empty = (TextView) findViewById(R.id.empty);
		img_delete_all = (ImageView) findViewById(R.id.img_delete_all);

		img_delete_all.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mDialog();
			}
		});


		/*if (sharedPref.get_Notification() == null){
			Get_Notification_Data();
		}else {
			setNotiData(sharedPref.get_Notification());
		}*/

		Get_Notification_Data();
	}


	public void Threar_Start(){

		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {

				boolean isConnected = ConnectivityReceiver.isConnected();

				if (isConnected){

					Get_Notification_Data();

				}else {

					// Toast.makeText(getApplicationContext(), "Please Connect to Internet", Toast.LENGTH_LONG).show();

					ShowToast("Please connect to internet");

					Threar_Start();
				}

			}
		}, 1500);

	}

	private Toast toast;
	public void ShowToast(String toast_text){

		if(toast == null) // first time around
		{
			CharSequence text = toast_text;
			int duration = Toast.LENGTH_LONG;
			toast = Toast.makeText(getApplicationContext(), text, duration);
		}
		try
		{
			if(toast.getView().isShown() == false) // if false not showing anymore, then show it
				toast.show();
		}
		catch (Exception e) {e.printStackTrace();}
	}


	private void mDialog() {
		String strMessage = "Are you sure you want to delete all notification?";
        new MaterialDialog.Builder(this)
            .title(R.string.app_name)
            .content(strMessage)
            .positiveText(R.string.ok)
            .negativeText(R.string.cancelcaps)
            .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    try {

						if (Notifications_Ids.endsWith(",")){

							Notifications_Ids = Notifications_Ids.substring(0, Notifications_Ids.length() - 1);

							Log.d(All_Constants_Urls.TAG, "Notifications_Ids = "+Notifications_Ids);

							Delete_Notification_Data();

						}else {

							Delete_Notification_Data();
						}

                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
            })
            .onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    // TODO
                }
            }).show();
	}

	public void Get_Notification_Data(){

		progressDialog.show();

		String URL = All_Constants_Urls.GET_NOTIFICATION;
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();

		params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
		params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());


		final String TAG = All_Constants_Urls.TAG;

		Log.d(TAG ,"AsyncHttpClient URL- " + URL);
		Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


		client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , Common.DEFAULT_TIMEOUT);
		client.post(URL, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				Log.d(TAG, "onSuccess- " + response.toString());

				if (response != null) {
					try {

						int success = response.optInt("success");
						String message = response.optString("message");

						if (success == 0){

							empty.setVisibility(View.VISIBLE);
							listView.setVisibility(View.GONE);

							Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

							progressDialog.dismiss();

						}else
						if (success == 1){

							//sharedPref.save_Notification(response.toString());

							setNotiData(response.toString());

							progressDialog.dismiss();

						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
				Log.d(TAG, "onFailure- " + res);

				progressDialog.dismiss();

				android.app.AlertDialog alert = new android.app.AlertDialog.Builder(NotificationActivity.this).create();
				alert.setMessage("Server Error");
				alert.show();
			}


		});


	}

	public void setNotiData(String data){

		List_Noification.clear();
		Notifications_Ids = "";

		try{

			JSONObject response = new JSONObject(data);

			empty.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);

			Notifications_Ids = "";

			JSONArray data_array = response.getJSONArray(All_Constants_Urls.data);

			for (int i = 0; i < data_array.length(); i++){
				JSONObject object = data_array.getJSONObject(i);

				String id = object.optString(All_Constants_Urls.id);

				Notifications_Ids = Notifications_Ids + id + ",";

				List_Noification.add(object.toString());

			}


			if (data_array.length() == 0){
				empty.setVisibility(View.VISIBLE);
				listView.setVisibility(View.GONE);
				img_delete_all.setVisibility(View.INVISIBLE);
			}else {

				notification_adapter = new Notification_Adapter(NotificationActivity.this, List_Noification);
				listView.setAdapter(notification_adapter);
			}


		}catch (Exception e){
			e.printStackTrace();
		}


	}

	public void Delete_Notification_Data(){

		progressDialog.show();

		String URL = All_Constants_Urls.DELETE_NOTIFICATION;
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();

		params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
		params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());
		params.put(All_Constants_Urls.noti_id, Notifications_Ids);


		final String TAG = All_Constants_Urls.TAG;

		Log.d(TAG ,"AsyncHttpClient URL- " + URL);
		Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


		client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , Common.DEFAULT_TIMEOUT);
		client.post(URL, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				Log.d(TAG, "onSuccess- " + response.toString());

				if (response != null) {
					try {

						int success = response.optInt("success");
						String message = response.optString("message");

						if (success == 0){

							Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

							progressDialog.dismiss();

						}else
						if (success == 1){

							Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

							sharedPref.save_Notification(null);

							List_Noification.clear();
							notification_adapter.notifyDataSetChanged();

							empty.setVisibility(View.VISIBLE);
							listView.setVisibility(View.GONE);
							img_delete_all.setVisibility(View.INVISIBLE);

							progressDialog.dismiss();

						}


					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
				Log.d(TAG, "onFailure- " + res);

				progressDialog.dismiss();

				android.app.AlertDialog alert = new android.app.AlertDialog.Builder(NotificationActivity.this).create();
				alert.setMessage("Server Error");
				alert.show();
			}


		});


	}


	private void setBottom() {
		ImageView imgView1 = (ImageView) this.findViewById(R.id.imgView1);
		ImageView imgView2 = (ImageView) this.findViewById(R.id.imgView2);
		ImageView imgView3 = (ImageView) this.findViewById(R.id.imgView3);
		ImageView imgView4 = (ImageView) this.findViewById(R.id.imgView4);
		ImageView imgView5 = (ImageView) this.findViewById(R.id.imgView5);

		RelativeLayout relProfile = (RelativeLayout) this
				.findViewById(R.id.relProfile);
		RelativeLayout relChat = (RelativeLayout) this
				.findViewById(R.id.relChat);
		RelativeLayout relMain = (RelativeLayout) this
				.findViewById(R.id.relMain);
		RelativeLayout relFav = (RelativeLayout) this.findViewById(R.id.relFav);


		imgView1.setImageResource(R.mipmap.g_profile_48);
		imgView2.setImageResource(R.mipmap.g_chat_48);
		imgView3.setImageResource(R.mipmap.g_sunglass_white);
		imgView4.setImageResource(R.mipmap.g_fav_48);
		imgView5.setImageResource(R.mipmap.notify_48_c);

		imgView2.setColorFilter(getResources().getColor(R.color.tint_color));
		imgView3.setColorFilter(getResources().getColor(R.color.tint_color));
		imgView4.setColorFilter(getResources().getColor(R.color.tint_color));
		imgView1.setColorFilter(getResources().getColor(R.color.tint_color));

		relProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(NotificationActivity.this,
						ProfileActivity.class);
				startActivity(intent);
				NotificationActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			}
		});

		relChat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(NotificationActivity.this,
						Chat_screen_new.class);
				startActivity(intent);
				NotificationActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			}
		});

		relMain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(NotificationActivity.this,
						MainActivity.class);
				startActivity(intent);
				NotificationActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			}
		});

		relFav.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(NotificationActivity.this,
						FavoriteActivity.class);
				startActivity(intent);
				NotificationActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			}
		});
	}


	@Override
	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			finish();
		}

		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				doubleBackToExitPressedOnce=false;
			}
		}, 2000);
	}


	@Override
	protected void onResume() {

		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		Log.d(All_Constants_Urls.TAG, "onDestroy");
		this.unregisterReceiver(mMessageReceiver);
		freeMemory();
		super.onDestroy();
	}

	public void get_MyMatches_Data(){


		String URL = All_Constants_Urls.MY_MATCHES;
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();

		params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
		params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());


		final String TAG = All_Constants_Urls.TAG;

		Log.d(TAG ,"AsyncHttpClient URL- " + URL);
		Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


		client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , Common.DEFAULT_TIMEOUT);
		client.post(URL, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				Log.d(TAG, "onSuccess- " + response.toString());

				if (response != null) {
					try {

						int success = response.optInt("success");
						String message = response.optString("message");

						if (success == 0){

							progressDialog.dismiss();

						}else
						if (success == 1){

							sharedPref.save_My_Matches(response.toString());

							progressDialog.dismiss();



							//////////////////////
							//  entry in DB

							databaseHelper.setJSONDataInDB(response.toString());


						}


					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
				Log.d(TAG, "onFailure- " + res);

			}


		});


	}


	public void Get_Notification_Data2(){


		String URL = All_Constants_Urls.GET_NOTIFICATION;
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();

		params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
		params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());


		final String TAG = All_Constants_Urls.TAG;

		Log.d(TAG ,"AsyncHttpClient URL- " + URL);
		Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


		client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , Common.DEFAULT_TIMEOUT);
		client.post(URL, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				Log.d(TAG, "onSuccess- " + response.toString());

				if (response != null) {
					try {

						int success = response.optInt("success");
						String message = response.optString("message");

						if (success == 0){

							empty.setVisibility(View.VISIBLE);
							listView.setVisibility(View.GONE);

							Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

							//progressDialog.dismiss();

						}else
						if (success == 1){

							sharedPref.save_Notification(response.toString());

							setNotiData(response.toString());

							//progressDialog.dismiss();

						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
				Log.d(TAG, "onFailure- " + res);

				//progressDialog.dismiss();

			}


		});


	}


	public void getUserBlockOrNot(){

		String URL = All_Constants_Urls.userblock;
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();

		params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
		params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());


		final String TAG = All_Constants_Urls.TAG;

		Log.d(TAG ,"AsyncHttpClient URL- " + URL);
		Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


		client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , Common.DEFAULT_TIMEOUT);
		client.post(URL, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				Log.d(TAG, "userblock- " + response.toString());

				if (response != null) {
					try {

						int success = response.optInt("success");
						String message = response.optString("message");

						if (success == 0){


						}else if (success == 1){

							showDialog(message);

						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			@Override
			public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
				Log.d(TAG, "onFailure- " + res);

			}


		});


	}


	public void showDialog(String msg){

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setMessage(msg);
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				Intent intent = getBaseContext().getPackageManager()
						.getLaunchIntentForPackage(getBaseContext().getPackageName());
				intent .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();

			}
		});


		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();


	}



	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Extract data included in the Intent
			String message = intent.getStringExtra("message");
			//do other stuff here
			Log.d(All_Constants_Urls.TAG, "Message = "+message);

			Get_Notification_Data2();

			get_MyMatches_Data();

		}

	};


	public void freeMemory(){
		System.runFinalization();
		Runtime.getRuntime().gc();
		System.gc();
	}
}
