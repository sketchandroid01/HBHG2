package com.hbhgdating.insta;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hbhgdating.databaseLocal.DatabaseHelper;
import com.hbhgdating.databaseLocal.ImageData;
import com.hbhgdating.R;
import com.hbhgdating.screens.Check_Video;
import com.hbhgdating.utils.Global_Class;
import com.hbhgdating.utils.SharedPref;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Developer on 4/28/17.
 */

public class InstagramApp {

	Global_Class global_class;

	private HashMap<String, String> userInfo = new HashMap<String, String>();

	private InstagramSession mSession;
	private InstagramDialog mDialog;
	private OAuthAuthenticationListener mListener;
	private String mAuthUrl;
	private String mTokenUrl;
	private String mAccessToken;
	private String mInstaUserId;
	private Context mCtx;
	private String mClientId;
	private String mClientSecret;


	public static final String TAG_IMAGES = "images";
	public static final String TAG_THUMBNAIL = "thumbnail";

	public static final String TAG_STANDARD_RESOLUTION = "standard_resolution";

	public static final String TAG_VIDEOS = "videos";

	public static final String TAG_URL = "url";

	public static int WHAT_FINALIZE = 0;
	private static int WHAT_ERROR = 1;
	private static int WHAT_FETCH_INFO = 2;

	/**
	 * Callback url, as set in 'Manage OAuth Costumers' page
	 * (https://developer.github.com/)
	 */

	public static String mCallbackUrl = "";
	private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
	private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
	private static final String API_URL = "https://api.instagram.com/v1";

	private static final String TAG = "TAG";

	public static final String TAG_DATA = "data";
	public static final String TAG_ID = "id";
	public static final String TAG_PROFILE_PICTURE = "profile_picture";
	public static final String TAG_USERNAME = "username";
	public static final String TAG_BIO = "bio";
	public static final String TAG_WEBSITE = "website";
	public static final String TAG_COUNTS = "counts";
	public static final String TAG_FOLLOWS = "follows";
	public static final String TAG_FOLLOWED_BY = "followed_by";
	public static final String TAG_MEDIA = "media";
	public static final String TAG_FULL_NAME = "full_name";
	public static final String TAG_META = "meta";
	public static final String TAG_CODE = "code";


	DatabaseHelper dbhelper;
	SharedPref sharedPref;
	String one_image_url;
	RelativeLayout relativeLayout;
	Dialog progressDialog;


	private ProgressDialog pd;
	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			if (pd != null && pd.isShowing())
				pd.dismiss();
			if (msg.what == WHAT_FINALIZE) {
				//setImageGridAdapter();
			} else {
				Toast.makeText(mCtx, "Check your network.",
						Toast.LENGTH_SHORT).show();
			}
			return false;
		}
	});



	public InstagramApp(Context context, String clientId, String clientSecret,
						String callbackUrl, RelativeLayout relativeLayout_) {

		dbhelper = new DatabaseHelper(context);
		sharedPref = new SharedPref(context);


		progressDialog = new Dialog(context, android.R.style.Theme_Translucent);
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progressDialog.getWindow().setContentView(R.layout.progressbar_creating_video);
		progressDialog.setCancelable(false);


		this.relativeLayout = relativeLayout_;

		mClientId = clientId;
		mClientSecret = clientSecret;
		mCtx = context;
		mSession = new InstagramSession(context);
		mAccessToken = mSession.getAccessToken();
		mCallbackUrl = callbackUrl;
		mTokenUrl = TOKEN_URL
				+ "?client_id=" + clientId
				+ "&client_secret=" + clientSecret
				+ "&redirect_uri=" + mCallbackUrl
				+ "&grant_type=authorization_code";

		mAuthUrl = AUTH_URL
				+ "?client_id=" + clientId
				+ "&redirect_uri=" + mCallbackUrl
				//+ "&response_type=code&display=touch&scope=likes+comments+relationships";
				+ "&response_type=code&display=touch&scope=basic";

		InstagramDialog.OAuthDialogListener listener =
				new InstagramDialog.OAuthDialogListener() {
			@Override
			public void onComplete(String code) {

				relativeLayout.setVisibility(View.GONE);

				progressDialog.show();

				deleteAll_TABLE_INSTA();

				getAccessToken(code);
			}

			@Override
			public void onError(String error) {
				mListener.onFail("Authorization failed");

				relativeLayout.setVisibility(View.VISIBLE);
			}
		};

		mDialog = new InstagramDialog(context, mAuthUrl, listener);

		global_class = (Global_Class)mCtx.getApplicationContext();


	}

	private void getAccessToken(final String code) {


		new Thread() {
			@Override
			public void run() {
				Log.i(TAG, "Getting access token");
				int what = WHAT_FETCH_INFO;
				try {
					URL url = new URL(TOKEN_URL);
					//URL url = new URL(mTokenUrl + "&code=" + code);
					//Log.i(TAG, "Opening Token URL " + url.toString());
					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setRequestMethod("POST");
					urlConnection.setDoInput(true);
					urlConnection.setDoOutput(true);
					//urlConnection.connect();
					OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
					writer.write("client_id="+mClientId+
							"&client_secret="+mClientSecret+
							"&grant_type=authorization_code" +
							"&redirect_uri="+mCallbackUrl+
							"&code=" + code);
					writer.flush();
					String response = streamToString(urlConnection.getInputStream());
					//Log.i(TAG, "response " + response);
					JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();
					mAccessToken = jsonObj.getString("access_token");
					//Log.i(TAG, "Got access token: " + mAccessToken);
					String id = jsonObj.getJSONObject("user").getString("id");
					String user = jsonObj.getJSONObject("user").getString("username");
					String name = jsonObj.getJSONObject("user").getString("full_name");
					String profile_picture = jsonObj.getJSONObject("user").getString("profile_picture");

					mInstaUserId = id;
					global_class.Insta_name = name;

					String full_name = global_class.Insta_name;
					String lastName = "";
					String firstName= "";
					if(name.split(" ").length>1){

						lastName = full_name.substring(full_name.lastIndexOf(" ")+1);
						firstName = full_name.substring(0, full_name.lastIndexOf(' '));
						global_class.Insta_first_name = firstName;

					}
					else{
						firstName = full_name;
					}

					String lo_via = "insta";

					sharedPref.set_INSTA_LoginInfo(lo_via, true, id, name, user, global_class.getLocation_gps());

					sharedPref.set_LOGIN_INSTA(true);

					getAllMediaImages_Video();

					mSession.storeAccessToken(mAccessToken, id, user, name);
				} catch (Exception ex) {
					what = WHAT_ERROR;
					ex.printStackTrace();
				}

				mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0));
			}
		}.start();
	}


/*
	public void fetchUserName() {
		mProgress.setMessage("Finalizing ...");
		new Thread() {
			@Override
			public void run() {
				Log.i(TAG, "Fetching user info");
				int what = WHAT_FINALIZE;
				try {
					URL url = new URL(API_URL + "/users/" + mSession.getId() + "/?access_token=" + mAccessToken);

					Log.d(TAG, "Opening URL " + url.toString());
					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setRequestMethod("GET");
					urlConnection.setDoInput(true);
					urlConnection.connect();
					String response = streamToString(urlConnection.getInputStream());
					// System.out.println(response);
					JSONObject jsonObj = (JSONObject) new JSONTokener(response).nextValue();
					String name = jsonObj.getJSONObject("data").getString("full_name");
					String bio = jsonObj.getJSONObject("data").getString("bio");
					String profile_picture = jsonObj.getJSONObject("data").getString("profile_picture");
					Log.i(TAG, "Got name: " + name + ", bio [" + bio + "]");
					Log.d(TAG, "profile_picture " + profile_picture.toString());

					JSONObject data_obj = jsonObj.getJSONObject(TAG_DATA);
					userInfo.put(TAG_ID, data_obj.getString(TAG_ID));

					userInfo.put(TAG_PROFILE_PICTURE,
							data_obj.getString(TAG_PROFILE_PICTURE));

					userInfo.put(TAG_USERNAME, data_obj.getString(TAG_USERNAME));

					userInfo.put(TAG_BIO, data_obj.getString(TAG_BIO));

					userInfo.put(TAG_WEBSITE, data_obj.getString(TAG_WEBSITE));

					JSONObject counts_obj = data_obj.getJSONObject(TAG_COUNTS);

					userInfo.put(TAG_FOLLOWS, counts_obj.getString(TAG_FOLLOWS));

					userInfo.put(TAG_FOLLOWED_BY,
							counts_obj.getString(TAG_FOLLOWED_BY));

					userInfo.put(TAG_MEDIA, counts_obj.getString(TAG_MEDIA));

					userInfo.put(TAG_FULL_NAME,
							data_obj.getString(TAG_FULL_NAME));

					JSONObject meta_obj = jsonObj.getJSONObject(TAG_META);

					userInfo.put(TAG_CODE, meta_obj.getString(TAG_CODE));


					Log.i(TAG, "userInfo = "+userInfo);



				//	getAllMediaImages();


				} catch (Exception ex) {
					what = WHAT_ERROR;
					ex.printStackTrace();
				}

				mHandler.sendMessage(mHandler.obtainMessage(what, 2, 0));
			}
		}.start();
	}*/


	private void getAllMediaImages_Video() {
		// mProgress.setMessage("Loading images...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				int what = WHAT_FINALIZE;
				try {
					// URL url = new URL(mTokenUrl + "&code=" + code);

					ImageData imageData = new ImageData();

					String image_url = "https://api.instagram.com/v1/users/self/media/recent/?access_token="+mAccessToken;

					//Log.d(TAG, "image_url>>>> "+image_url.toString());
					JSONParser jsonParser = new JSONParser();
					JSONObject jsonObject = jsonParser.getJSONFromUrlByGet(image_url);
					JSONArray data = jsonObject.getJSONArray(TAG_DATA);
					for (int data_i = 0; data_i < data.length(); data_i++) {
						JSONObject data_obj = data.getJSONObject(data_i);

						JSONObject images_obj = data_obj.getJSONObject(TAG_IMAGES);
						JSONObject video_obj = data_obj.optJSONObject(TAG_VIDEOS);

						if (video_obj == null) {
							JSONObject thumbnail_obj = images_obj.getJSONObject(TAG_STANDARD_RESOLUTION);
							String str_url = thumbnail_obj.getString(TAG_URL);
							Log.d(TAG, "str_url>>>> " + str_url.toString());

							if (data_i == 0){
								global_class.Profile_Image = str_url;
							}

							one_image_url = str_url;

							imageData.setUrl(str_url);

							insert_INSTA_IMAGE_Data(imageData);

						}


					}

					System.out.println("jsonObject::" + jsonObject);



					//////////////////////////////////////////////
					//video fetch ...............

					String url_video = "https://api.instagram.com/v1/users/"+mInstaUserId
							+"/media/recent/?access_token="+mAccessToken;

				//	Log.d(TAG, "url_video>>>> "+url_video.toString());

					JSONParser jsonParser_v = new JSONParser();
					JSONObject jsonObject_v = jsonParser_v.getJSONFromUrlByGet(url_video);
					JSONArray data_v = jsonObject_v.getJSONArray(TAG_DATA);
					for (int data_i = 0; data_i < data_v.length(); data_i++) {
						JSONObject data_obj = data_v.getJSONObject(data_i);

						JSONObject video_obj = data_obj.optJSONObject(TAG_VIDEOS);
						if (video_obj!=null){

							// get video thumb image...
							JSONObject thumbnail_obj = data_obj.getJSONObject(TAG_IMAGES);
							JSONObject obj_image = thumbnail_obj.getJSONObject(TAG_STANDARD_RESOLUTION);
							String thumb_url = obj_image.optString(TAG_URL);
						//	Log.d(TAG, "ima_url>>>> "+thumb_url.toString());


							JSONObject obj_v = video_obj.getJSONObject(TAG_STANDARD_RESOLUTION);
							String v_url = obj_v.getString(TAG_URL);

						//	Log.d(TAG, "v_url>>>> "+v_url.toString());



							imageData.setUrl(v_url);
							imageData.setThumb_image(thumb_url);

							insert_INSTA_VIDEO_Data(imageData);


						}


					}


					/*Intent intent = new Intent(mCtx, Image_Filter_for_URL.class);
					intent.putExtra("image_url", one_image_url);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					mCtx.startActivity(intent);
					((Activity) mCtx).finish();*/


					Intent intent = new Intent(mCtx, Check_Video.class);
					intent.putExtra("image_url", one_image_url);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					mCtx.startActivity(intent);
					((Activity) mCtx).finish();

					resetAccessToken();

					//ll_progressbar.setVisibility(View.GONE);

					progressDialog.dismiss();

				} catch (Exception exception) {
					exception.printStackTrace();
					what = WHAT_ERROR;
				}
				// pd.dismiss();
				handler.sendEmptyMessage(what);
			}
		}).start();
	}



	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == WHAT_ERROR) {


				progressDialog.dismiss();

				if(msg.arg1 == 1) {
					mListener.onFail("Failed to get access token");
				}
				else if(msg.arg1 == 2) {
					mListener.onFail("Failed to get user information");

				}
			}
			else if(msg.what == WHAT_FETCH_INFO) {
				//fetchUserName();
			}
			else {


				progressDialog.dismiss();

				mListener.onSuccess();
			}
		}
	};

	public boolean hasAccessToken() {
		return (mAccessToken == null) ? false : true;
	}

	public void setListener(OAuthAuthenticationListener listener) {
		mListener = listener;
	}

	public String getUserName() {
		return mSession.getUsername();
	}

	public String getId() {
		return mSession.getId();
	}
	public String getName() {
		return mSession.getName();
	}
	public void authorize() {
		//Intent webAuthIntent = new Intent(Intent.ACTION_VIEW);
		//webAuthIntent.setData(Uri.parse(AUTH_URL));
		//mCtx.startActivity(webAuthIntent);
		mDialog.show();
	}

	private String streamToString(InputStream is) throws IOException {
		String str = "";

		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}

				reader.close();
			} finally {
				is.close();
			}

			str = sb.toString();
		}

		return str;
	}

	public void resetAccessToken() {
		if (mAccessToken != null) {
			mSession.resetAccessToken();
			mAccessToken = null;
		}
	}

	public interface OAuthAuthenticationListener {
		public abstract void onSuccess();

		public abstract void onFail(String error);
	}



	public void insert_INSTA_IMAGE_Data(ImageData data) {

		SQLiteDatabase dbase = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(DatabaseHelper.IMG_URL_INSTA, data.getUrl());

		dbase.insert(DatabaseHelper.TABLE_INSTA, null, values);
		dbase.close();

	//	Log.d("TAG", "TABLE_INSTA inserted value");

	}

	public void insert_INSTA_VIDEO_Data(ImageData data) {

		SQLiteDatabase dbase = dbhelper.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(DatabaseHelper.THUMB_IMAGE_INSTA, data.getThumb_image());
		values.put(DatabaseHelper.VIDEO_URL_INSTA, data.getUrl());

		dbase.insert(DatabaseHelper.TABLE_INSTA_VIDEO, null, values);
		dbase.close();

	//	Log.d("TAG", "TABLE_INSTA_VIDEO inserted value");

	}


	public void deleteAll_TABLE_INSTA() {

		SQLiteDatabase db = dbhelper.getWritableDatabase();
		db.delete(DatabaseHelper.TABLE_INSTA , null, null);
		db.delete(DatabaseHelper.TABLE_INSTA_VIDEO, null, null);
		db.close();

		Log.d("TAG", "deleteAll_TABLE_INSTA");

	}




}
