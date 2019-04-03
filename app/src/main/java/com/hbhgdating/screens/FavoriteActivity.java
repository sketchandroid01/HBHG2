package com.hbhgdating.screens;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hbhgdating.Chat.Chat_screen_new;
import com.hbhgdating.Chat.Get_Matches_msg;
import com.hbhgdating.DatabaseLocal.DatabaseHelper;
import com.hbhgdating.R;
import com.hbhgdating.adapter.Favourite_Search_Adapter;
import com.hbhgdating.adapter.GridViewAdapter;
import com.hbhgdating.adapter.ListViewAdapter;
import com.hbhgdating.slider.Animations.DescriptionAnimation;
import com.hbhgdating.slider.Indicators.PagerIndicator;
import com.hbhgdating.slider.SliderLayout;
import com.hbhgdating.slider.SliderTypes.BaseSliderView;
import com.hbhgdating.slider.SliderTypes.TextSliderView;
import com.hbhgdating.slider.Tricks.ViewPagerEx;
import com.hbhgdating.utils.All_Constants_Urls;
import com.hbhgdating.utils.Common;
import com.hbhgdating.utils.ConnectivityReceiver;
import com.hbhgdating.utils.Global_Class;
import com.hbhgdating.utils.ScalableVideoView;
import com.hbhgdating.utils.SharedPref;
import com.hbhgdating.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import cz.msebera.android.httpclient.Header;


public class FavoriteActivity extends AppCompatActivity implements  OnMapReadyCallback,
		OnConnectionFailedListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {


	GridView gridView;
	GridViewAdapter gridAdapter;
	LinearLayout ll_body, ll_body_no_record;
	RelativeLayout rel_users;
	TextView txtTitle, txtLikeCount, txtChatrooms, txtbolked, tvSearchEmpty,
			tv_cancel;
	ImageView imgDemo, imgGrid, imgList, imgMap, imgSearch, imgUser;
	ScalableVideoView videoView1;
	SliderLayout mDemoSlider;
	EditText inputSearch;
	ListView listView, lvSearch;
	FrameLayout mapLayout;
	RelativeLayout relSearch, rl_blockuser;
	TextView tv_user_info, tv_no_mymatches;

	Dialog progressDialog;
	SharedPref sharedPref;
	Global_Class global_class;
	boolean doubleBackToExitPressedOnce = false;

	Favourite_Search_Adapter favourite_search_adapter;
	ListViewAdapter listAdapter;
	MediaController mediaController;

	protected Location mLastLocation;
	String ToUserID;
	RelativeLayout relChatTAB;
	String userName = "";
	int is_blocked = 0;
	//private InterstitialAd interstitial;
	String strImgageLink = "", strVideoLink = "", strVideoName = "";
	boolean isloadAds = false;
	double Latitude, Longitude;

	private GoogleMap mMap;
	SupportMapFragment mapFragment;
	ArrayList<String> List_My_Matches;
	ArrayList<String> List_Filter_My_Matches;
	String Font_User_Id;
	int MyMatches_list_Position = 0;
	String BlockedUser_Data = "";
	private boolean is_new_changes_onClick = false;

	HttpProxyCacheServer proxy;

	private DatabaseHelper databaseHelper;
	Utility utility;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.favorite);

		databaseHelper = new DatabaseHelper(this);
		sharedPref = new SharedPref(this);
		global_class = (Global_Class) getApplicationContext();
		utility = new Utility(this);

		progressDialog = new Dialog(this, android.R.style.Theme_Translucent);
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progressDialog.getWindow().setContentView(R.layout.progressbar_pleasewait);
		progressDialog.setCancelable(false);

		isloadAds = false;
		setView();



		if (sharedPref.get_My_Matches() == null){
			get_MyMatches_Data();
		}else {
			collect_data(sharedPref.get_My_Matches());
		}



		//showslider();


		//deleteFiles(Common.strVideoFolder);
		//getData("");
		//buildGoogleApiClient();
		//loadInterstitialAD();


		mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);


	}


	public void Threar_Start(){

		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {

				boolean isConnected = ConnectivityReceiver.isConnected();

				if (isConnected){

					get_MyMatches_Data();

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


	public static void deleteFiles(String path) {

		File f = new File(path);
//
//		if (file.exists()) {
//			String deleteCmd = "rm -r " + path;
//			Runtime runtime = Runtime.getRuntime();
//			try {
//				runtime.exec(deleteCmd);
//			} catch (IOException e) {
//			}
//		}
		if (f.isDirectory()) {
			for (File c : f.listFiles()) {
				c.delete();
			}
		}
	}


/*	private void loadInterstitialAD() {
		interstitial = new InterstitialAd(FavoriteActivity.this);
		interstitial.setAdUnitId(getResources().getString(
				R.string.interstitial_ad_unit_id));
		AdRequest adRequest = new AdRequest.Builder().build();
		interstitial.loadAd(adRequest);
		interstitial.setAdListener(new AdListener() {
			public void onAdLoaded() {
				displayInterstitial();
			}

			public void onAdClosed() {
				isloadAds = false;
				//playVideo();
			}
		});

	}*/


	public void displayInterstitial() {
		/*if (interstitial.isLoaded()) {
			if (videoView1.isPlaying())
				videoView1.stopPlayback();
			isloadAds = true;
			interstitial.show();
		}*/
	}


	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onStop() {

		this.unregisterReceiver(mMessageReceiver);

		super.onStop();

		mDemoSlider.stopAutoCycle();
	}

	@Override
	protected void onResume() {

		FavoriteActivity.this.registerReceiver(mMessageReceiver, new IntentFilter(Common.Key_Mymatches));

		mDemoSlider.startAutoCycle();

		get_BlockedUser_Data();

		if (global_class.isIf_action_on_block_screen()){
			get_MyMatches_Data();
			global_class.setIf_action_on_block_screen(false);
		}

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		super.onResume();
	}


	private void setView() {
		setBottom();

		List_My_Matches = new ArrayList<>();
		List_Filter_My_Matches = new ArrayList<>();

		ll_body = (LinearLayout) findViewById(R.id.ll_body);
		rel_users = (RelativeLayout) findViewById(R.id.rel_users);
		ll_body_no_record = (LinearLayout) findViewById(R.id.ll_body_no_record);
		gridView = (GridView) findViewById(R.id.gridView);
		listView = (ListView) findViewById(R.id.listView);
		relSearch = (RelativeLayout) findViewById(R.id.relSearch);
		relChatTAB = (RelativeLayout) findViewById(R.id.relChatTAB);
		txtLikeCount = (TextView) this.findViewById(R.id.txtLikeCount);
		txtChatrooms = (TextView) this.findViewById(R.id.txtChatrooms);
		txtbolked = (TextView) this.findViewById(R.id.txtbolked);
		txtTitle = (TextView) this.findViewById(R.id.txtTitle);
		tv_no_mymatches = (TextView) this.findViewById(R.id.tv_no_mymatches);
		tv_no_mymatches.setVisibility(View.GONE);
		//imgDemo = (ImageView) findViewById(R.id.imgDemo);

		imgGrid = (ImageView) findViewById(R.id.imgGrid);
		imgList = (ImageView) findViewById(R.id.imgList);
		imgMap = (ImageView) findViewById(R.id.imgMap);
		imgSearch = (ImageView) findViewById(R.id.imgSearch);
		imgUser = (ImageView) findViewById(R.id.imgUser);
		tvSearchEmpty = (TextView) findViewById(R.id.tvSearchEmpty);
		lvSearch = (ListView) findViewById(R.id.lvSearch);
		inputSearch = (EditText) findViewById(R.id.inputSearch);
		tv_cancel = (TextView) findViewById(R.id.tv_cancel);
		videoView1 = (ScalableVideoView) findViewById(R.id.videoView1);
		videoView1.setVisibility(View.GONE);
		ll_body_no_record.setVisibility(View.GONE);
		ll_body.setVisibility(View.VISIBLE);
		imgUser.setVisibility(View.VISIBLE);
		mapLayout = (FrameLayout) this.findViewById(R.id.mapLayout);
		rl_blockuser = (RelativeLayout) findViewById(R.id.rl_blockuser);

		mDemoSlider = (SliderLayout) findViewById(R.id.slider);
		mDemoSlider.setVisibility(View.INVISIBLE);
		tv_user_info = (TextView) findViewById(R.id.tv_user_info);
		mediaController = new MediaController(this);
		//mediaController.setAnchorView(videoView1);

		relChatTAB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (List_My_Matches.size() > 0){

					Intent intent = new Intent(FavoriteActivity.this,
							Get_Matches_msg.class);
					intent.putExtra("id", ToUserID);
					intent.putExtra("name_user", userName);
					startActivity(intent);
				}


			}
		});
		imgUser.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (List_My_Matches.size() > 0){

					Intent intent1 = new Intent(FavoriteActivity.this, UserProfileActivity.class);
					intent1.putExtra("userid", Font_User_Id);
					intent1.putExtra("data", List_My_Matches.get(MyMatches_list_Position));
					startActivity(intent1);
					overridePendingTransition(R.anim.slide_in_up, R.anim.stay);

				}

			}
		});
		imgGrid.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (List_My_Matches.size() > 0){
					displayType(1);
				}

			}
		});

		imgList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (List_My_Matches.size() > 0){
					displayType(2);
				}
			}
		});

		imgMap.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (List_My_Matches.size() > 0){
					displayType(3);
				}
			}
		});

		imgSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (List_My_Matches.size() > 0){
					displayType(4);
				}
			}
		});

		tv_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Common.hideSoftKeyboard(FavoriteActivity.this);
				inputSearch.setText("");
				lvSearch.setVisibility(View.GONE);
				lvSearch.setAdapter(null);
				tvSearchEmpty.setVisibility(View.GONE);
			}
		});

		inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
												  KeyEvent event) {
						// TODO Auto-generated method stub
						if (actionId == EditorInfo.IME_ACTION_SEARCH) {
							String strText = v.getText().toString().trim();
							if (strText.isEmpty()) {
								Common.customToast(
										FavoriteActivity.this,
										getResources().getString(
												R.string.entertext),
										Common.displayType.CENTER);
							} else {
								Common.hideSoftKeyboard(FavoriteActivity.this);

								filter_City(inputSearch.getText().toString());
							}
							return true;
						}
						return false;
					}
				});


		rl_blockuser.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (txtbolked.getText().toString().matches("0")){

					Toast.makeText(getApplicationContext(), "There are no any blocked user", Toast.LENGTH_SHORT).show();
					
				}else {

					Intent intent = new Intent(FavoriteActivity.this, Bloked_User_Screen.class);
					intent.putExtra("data", BlockedUser_Data);
					startActivity(intent);
				}
				
			}
		});



	}


	//////////////////////////

	private void starVideoAsync() {
		File file = new File(Common.strVideoFolder + File.separator + strVideoName);
		if (file.exists()) {
			displayVideo(strVideoName);
		} else {
			progressBack PB = new progressBack();
			PB.execute(strVideoLink, strVideoName, ToUserID);
		}
	}

	public class progressBack extends AsyncTask<String, String, String> {
		ProgressDialog PD;
		int tUserID = 0;

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(String... arg0) {
			tUserID = Integer.valueOf(arg0[2]);
			try {
				File RootFile = new File(Common.strVideoFolder);
//				if (!RootFile.exists()) {
//					RootFile.mkdir();
//				}
				if (!RootFile.exists()) {
					RootFile.mkdirs();
				}
				URL u = new URL(arg0[0]);
				HttpURLConnection c = (HttpURLConnection) u.openConnection();
				c.setRequestMethod("GET");
				c.setDoOutput(true);
				c.connect();
				FileOutputStream f = new FileOutputStream(new File(RootFile,
						arg0[1]));
				InputStream in = c.getInputStream();
				byte[] buffer = new byte[1024];
				int len1 = 0;

				while ((len1 = in.read(buffer)) > 0) {
					f.write(buffer, 0, len1);
				}
				f.close();
			} catch (Exception e) {

				Log.e("Error....", e.toString());
			}
			return arg0[1];
		}

		protected void onPostExecute(String result) {
			/*if (UserID == tUserID) {
				if (!isloadAds)
					displayVideo(result);
			}*/
		}
	}

	private void displayVideo(String strVName) {
		videoView1.setVisibility(View.VISIBLE);
		imgDemo.setVisibility(View.GONE);
		String strPath = Common.strVideoFolder + File.separator + strVName;
		if (videoView1.isPlaying())
			videoView1.stopPlayback();
		videoView1.setVideoPath(strPath);
		videoView1.start();
		videoView1.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.setLooping(true);
			}
		});

		videoView1.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				Log.e("video", "setOnErrorListener ");
				return true;
			}
		});
	}

	//////////////////////////


	private void displayType(int type) {
		imgGrid.setImageResource(R.mipmap.dots_icon_gray_color);
		imgList.setImageResource(R.mipmap.gray_dots_and_line);
		imgMap.setImageResource(R.mipmap.gray_location_symbol);
		imgSearch.setImageResource(R.mipmap.search_gray);
		gridView.setVisibility(View.GONE);
		listView.setVisibility(View.GONE);
		relSearch.setVisibility(View.GONE);

		mapLayout.setVisibility(View.GONE);
		// map.setVisibility(View.GONE);
		switch (type) {
			case 1:
				imgGrid.setImageResource(R.mipmap.dots_icon_orange_color);
				gridView.setVisibility(View.VISIBLE);

				break;
			case 2:
				imgList.setImageResource(R.mipmap.orange_dots_and_line);
				listView.setVisibility(View.VISIBLE);

				break;
			case 3:
				imgMap.setImageResource(R.mipmap.orange_location_symbol);
				mapLayout.setVisibility(View.VISIBLE);
				break;
			case 4:
				relSearch.setVisibility(View.VISIBLE);
				imgSearch.setImageResource(R.mipmap.search_orange);
				inputSearch.setText("");
				lvSearch.setVisibility(View.GONE);
				tvSearchEmpty.setVisibility(View.GONE);
				break;
		}
	}


	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;

		mMap.getUiSettings().setZoomControlsEnabled(true);

		// Add a marker in Sydney and move the camera
		/*LatLng TutorialsPoint = new LatLng(21, 57);
		mMap.addMarker(new
				MarkerOptions().position(TutorialsPoint).title(""));
		mMap.moveCamera(CameraUpdateFactory.newLatLng(TutorialsPoint));*/
	}


	@SuppressWarnings("deprecation")
	private void mapDisplay() throws GooglePlayServicesNotAvailableException {
		if (mMap == null) {
			//map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));


			// map.getUiSettings().setMyLocationButtonEnabled(false);
			if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				// TODO: Consider calling
				//    ActivityCompat#requestPermissions
				// here to request the missing permissions, and then overriding
				//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
				//                                          int[] grantResults)
				// to handle the case where the user grants the permission. See the documentation
				// for ActivityCompat#requestPermissions for more details.
				return;
			}
			mMap.setMyLocationEnabled(true);

			MapsInitializer.initialize(FavoriteActivity.this);
			if (mLastLocation != null) {
				mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
						new LatLng(mLastLocation.getLatitude(), mLastLocation
								.getLongitude()), 0));
			}
			mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

				@Override
				public boolean onMarkerClick(Marker marker) {
					// TODO Auto-generated method stub
					marker.showInfoWindow();
					return true;
				}
			});
		}
		//addMarkersToMap();

	}

	public LatLng generateRandomLoc(Location l, int radius) {

		// Log.d(TAG,"current location random ");
		// Log.d(TAG,""+getCurrentCoordinates().toString());
		Random random = new Random();

		double x0 = (double) l.getLongitude();
		double y0 = (double) l.getLatitude();

		// Convert radius from meters to degrees
		double radiusInDegrees = radius / 111300;

		double u = random.nextDouble();
		double v = random.nextDouble();
		double w = radiusInDegrees * Math.sqrt(u);
		double t = 2 * Math.PI * v;
		double x = w * Math.cos(t);
		double y = w * Math.sin(t);

		// Adjust the x-coordinate for the shrinking of the east-west distances
		double new_x = x / Math.cos(y0);

		double foundLongitude = new_x + x0;
		double foundLatitude = y + y0;
		System.out.println("Longitude: " + foundLongitude + "  Latitude: "
				+ foundLatitude);

		// Location rl = new Location("");
		// rl.setLatitude(foundLatitude);
		// rl.setLongitude(foundLongitude);
		// return rl;
		LatLng ll = new LatLng(foundLatitude, foundLongitude);
		return ll;
	}

	private void addMarkersToMap(String name, String city) {
		mMap.clear();


		LatLng markerLoc = new LatLng(Latitude, Longitude);

		final CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(markerLoc)          // Sets the center of the map to Mountain View
				.zoom(10)                   // Sets the zoom
				.bearing(90)                // Sets the orientation of the camera to east
				.tilt(30)                   // Sets the tilt of the camera to 30 degrees
				.build();                   //
		mMap.addMarker(new MarkerOptions().position(new LatLng(Latitude, Longitude)).title(name + ", " + city));
		mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


	/*	for (int i = 0; i < arrData.size(); i++) {
			// Random random = new Random();
			// double latitude = (random.nextDouble()) * 50;
			// double longitude = (random.nextDouble()) * 100;
			//
			// System.out.println("Longitude: " + longitude + "  Latitude: "
			// + latitude);
			// LatLng ll = new LatLng(latitude, longitude);
			LatLng ll = new LatLng(arrData.get(i).latitude,
					arrData.get(i).longitude);
			// Marker storeMarker = map.addMarker(new
			// MarkerOptions().position(ll)
			// .title("Shawn Martin " + (i + 1))
			// .snippet("Address " + (i + 1)));

			// Create user marker with custom icon and other options
			MarkerOptions markerOption = new MarkerOptions().position(ll);
			// markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation_icon));
			// arrData.get(i).latitude = latitude;
			// arrData.get(i).longitude = longitude;

			Marker currentMarker = map.addMarker(markerOption);
			mMarkersHashMap.put(currentMarker, arrData.get(i));

			map.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
			}*/

	}


	private void setBottom() {
		ImageView imgView1 = (ImageView) this.findViewById(R.id.imgView1);
		ImageView imgView2 = (ImageView) this.findViewById(R.id.imgView2);
		ImageView imgView3 = (ImageView) this.findViewById(R.id.imgView3);
		ImageView imgView4 = (ImageView) this.findViewById(R.id.imgView4);
		ImageView imgView5 = (ImageView) this.findViewById(R.id.imgView5);

		RelativeLayout relProfile = (RelativeLayout) this
				.findViewById(R.id.relProfile);
		RelativeLayout relMain = (RelativeLayout) this
				.findViewById(R.id.relMain);
		RelativeLayout relNotification = (RelativeLayout) this
				.findViewById(R.id.relNotification);
		RelativeLayout relChat = (RelativeLayout) this
				.findViewById(R.id.relChat);


		imgView1.setImageResource(R.mipmap.g_profile_48);
		imgView2.setImageResource(R.mipmap.g_chat_48);
		imgView3.setImageResource(R.mipmap.g_sunglass_white);
		imgView4.setImageResource(R.mipmap.fav_48_c);
		imgView5.setImageResource(R.mipmap.g_notify_48);

		imgView2.setColorFilter(getResources().getColor(R.color.tint_color));
		imgView3.setColorFilter(getResources().getColor(R.color.tint_color));
		imgView1.setColorFilter(getResources().getColor(R.color.tint_color));
		imgView5.setColorFilter(getResources().getColor(R.color.tint_color));

		relProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FavoriteActivity.this,
						ProfileActivity.class);
				startActivity(intent);
				FavoriteActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			}
		});

		relMain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FavoriteActivity.this,
						MainActivity.class);
				startActivity(intent);
				FavoriteActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			}
		});

		relNotification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FavoriteActivity.this,
						NotificationActivity.class);
				startActivity(intent);
				FavoriteActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			}
		});

		relChat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(FavoriteActivity.this,
						Chat_screen_new.class);
				startActivity(intent);
				FavoriteActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
				finish();
			}
		});
	}

	/**
	 * Runs when a GoogleApiClient object successfully connects.
	 */

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// Refer to the javadoc for ConnectionResult to see what error codes
		// might be returned in
		// onConnectionFailed.
		Log.e("mLastLocation",
				"Connection failed: ConnectionResult.getErrorCode() = "
						+ result.getErrorCode());
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

	@Override
	public void onPageSelected(int position) {
		String trans[]={"","ZoomOut","ZoomOutSlide","Tablet","DepthPage","ZoomOut","Fade","RotateDown"};
		Random rand = new Random();
		int randomNum = rand.nextInt((7 - 1) + 1) + 1;
		mDemoSlider.setPresetTransformer(trans[randomNum]);
		Log.d("Slider Demo", "Page Changed: " + position+"random"+randomNum);
	}

	@Override
	public void onPageScrollStateChanged(int state) {}

	@Override
	public void onSliderClick(BaseSliderView slider) {
		//Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
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


	public void get_MyMatches_Data(){

		if (!global_class.isIf_action_on_block_screen())
			progressDialog.show();

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

							//Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

							rel_users.setVisibility(View.GONE);
							tv_no_mymatches.setVisibility(View.VISIBLE);

							progressDialog.dismiss();

						}else if (success == 1){


							if (!is_new_changes_onClick){

								List_My_Matches.clear();

								collect_data(response.toString());

							}

							sharedPref.save_My_Matches(response.toString());

							progressDialog.dismiss();

							is_new_changes_onClick = false;

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

				progressDialog.dismiss();

				android.app.AlertDialog alert = new android.app.AlertDialog.Builder(FavoriteActivity.this).create();
				alert.setMessage("Server Error");
				alert.show();
			}


		});


	}


	public void collect_data(String data){

		try {

			JSONObject response = new JSONObject(data);

			JSONArray data_array = response.getJSONArray(All_Constants_Urls.data);

			String likes_count = response.optString(All_Constants_Urls.likes_count);
			txtLikeCount.setText(likes_count);

			if (data_array.length() == 0){

				rel_users.setVisibility(View.GONE);
				tv_no_mymatches.setVisibility(View.VISIBLE);

			}else {

				rel_users.setVisibility(View.VISIBLE);
				tv_no_mymatches.setVisibility(View.GONE);
			}


			Log.d(All_Constants_Urls.TAG, "length - " +data_array.length());
			for (int i = 0; i < data_array.length(); i++){
				JSONObject object = data_array.getJSONObject(i);

				int is_blocked = object.optInt(All_Constants_Urls.is_blocked);
				if (is_blocked == 0){

					List_My_Matches.add(object.toString());

				}

			}


			setData_inAdapters();

			displayType(1);

			show_Profile_Videos(List_My_Matches.get(MyMatches_list_Position));


		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public void setData_inAdapters(){

		if (gridAdapter == null){
			gridAdapter = new GridViewAdapter(FavoriteActivity.this, List_My_Matches);
			gridView.setAdapter(gridAdapter);

		}else {
			gridAdapter.notifyDataSetChanged();
		}

		updateIsNewMatches();

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if (MyMatches_list_Position != position){

					MyMatches_list_Position = position;

					show_Profile_Videos(List_My_Matches.get(MyMatches_list_Position));

				}

				try {

					JSONObject object = new JSONObject(List_My_Matches.get(position));
					String is_new = object.getString("is_new");
					if (is_new.matches("1")){
						is_new_changes_onClick = true;
						updateIsNewMatches();
					}


				}catch (Exception e){
					e.printStackTrace();
				}

				gridAdapter.updateBooleanData(position);
				listAdapter.updateBooleanData(position);

			}
		});


		if (listAdapter == null){
			listAdapter = new ListViewAdapter(FavoriteActivity.this, List_My_Matches);
			listView.setAdapter(listAdapter);

		}else {
			listAdapter.notifyDataSetChanged();
		}

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				if (MyMatches_list_Position != position){

					MyMatches_list_Position = position;

					show_Profile_Videos(List_My_Matches.get(MyMatches_list_Position));

				}

				try {

					JSONObject object = new JSONObject(List_My_Matches.get(position));
					String is_new = object.getString("is_new");
					if (is_new.matches("1")){
						is_new_changes_onClick = true;
						updateIsNewMatches();
					}


				}catch (Exception e){
					e.printStackTrace();
				}

				listAdapter.updateBooleanData(position);
				gridAdapter.updateBooleanData(position);

			}
		});

		gridAdapter.updateBooleanData(0);
		listAdapter.updateBooleanData(0);
	}


	public void show_Profile_Videos(String json){

		mDemoSlider.setVisibility(View.INVISIBLE);
		videoView1.setVisibility(View.INVISIBLE);


		//Log.d(All_Constants_Urls.TAG, "Clicked data = "+json);

		mDemoSlider.removeAllSliders();

		try{

			JSONObject object = new JSONObject(json);

			is_blocked = object.optInt(All_Constants_Urls.is_blocked);

			JSONObject basic_info = object.getJSONObject(All_Constants_Urls.basic_info);

			////////////

			ToUserID = basic_info.getString(All_Constants_Urls.id);
			String name = basic_info.getString(All_Constants_Urls.name);
			userName = name;
			String age = basic_info.getString(All_Constants_Urls.age);
			String strGender = basic_info.getString(All_Constants_Urls.gender);
			String strSexual = basic_info.getString(All_Constants_Urls.sexual_orientation);
			String city = basic_info.getString(All_Constants_Urls.city);
			String country = basic_info.getString(All_Constants_Urls.country);

			Latitude = basic_info.getDouble(All_Constants_Urls.latitude);
			Longitude = basic_info.getDouble(All_Constants_Urls.longitude);

			String gender = "";
			if (strGender.equalsIgnoreCase("m")){
				gender = "Male";
			}else if (strGender.equalsIgnoreCase("f")){
				gender = "Female";
			}


			String[] array = name.split(" ");
			String temp_text = "";

			if (!array[0].equals("")){
				temp_text = temp_text + array[0] + ", ";
			}
			if (!age.equals("")){
				temp_text = temp_text + age + ", ";
			}
			if (!gender.equals("")){
				temp_text = temp_text + gender + ", ";
			}
			if (!city.equals("")){
				temp_text = temp_text + city + ", ";
			}
			if (!country.equals("")){
				temp_text = temp_text + country;
			}
			//String temp_text = array[0] + ", "+ age + ", " + strGender + ", "+city;

			if (temp_text.endsWith(", ")){
				temp_text.substring(0, temp_text.length() - 2);
			}

			tv_user_info.setText(temp_text);



			///////////

			Font_User_Id = basic_info.getString(All_Constants_Urls.id);
			Log.d(All_Constants_Urls.TAG, "Font_User_Id = "+Font_User_Id);

			String video = basic_info.getString(All_Constants_Urls.video);
			if (!video.isEmpty()){

				play_Video(video);

			}else {

				JSONArray image_video = basic_info.optJSONArray(All_Constants_Urls.image_video);
				if (image_video.length() != 0){
					for (int j = 0; j < image_video.length(); j++){
						String image_url = image_video.getString(j);

						TextSliderView textSliderView = new TextSliderView(this);
						// initialize a SliderLayout
						textSliderView
								.image(image_url)
								.setScaleType(BaseSliderView.ScaleType.CenterCrop)
								.setOnSliderClickListener(this);

						mDemoSlider.addSlider(textSliderView);

					}

					mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
					mDemoSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
					mDemoSlider.setCustomAnimation(new DescriptionAnimation());
					mDemoSlider.setDuration(2000);
					mDemoSlider.moveNextPosition(true);
					//mDemoSlider.addOnPageChangeListener(this);
					mDemoSlider.startAutoCycle();


					mDemoSlider.setVisibility(View.VISIBLE);
					videoView1.setVisibility(View.GONE);

				}

			}


			addMarkersToMap(array[0], city);

		}catch (Exception e){
			e.printStackTrace();
		}

	}


	public void play_Video(final String videoUri){

		mDemoSlider.setVisibility(View.GONE);
		videoView1.setVisibility(View.VISIBLE);

		videoView1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {

				//mp.setVolume(0, 0);
			}
		});

		//proxy = Global_Class.getProxy(this);
		//final String proxyUrl = proxy.getProxyUrl(videoUri);

		videoView1.setMediaController(mediaController);
		videoView1.setVideoURI(Uri.parse(videoUri));
		videoView1.requestFocus();
		videoView1.start();

        videoView1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

				videoView1.setVideoURI(Uri.parse(videoUri));
                videoView1.requestFocus();
                videoView1.start();
            }
        });
		videoView1.setOnErrorListener(new MediaPlayer.OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				Log.d("video", "setOnErrorListener ");
				return true;
			}
		});


	}


	public void get_BlockedUser_Data(){

		//progressDialog.show();

		String URL = All_Constants_Urls.GET_BLOCKED_USER;
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

							//Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

							txtbolked.setText("0");

							//progressDialog.dismiss();

						}else
						if (success == 1){

							BlockedUser_Data = "";

							JSONArray data_array = response.getJSONArray(All_Constants_Urls.data);

							Log.d(TAG, "length - " +data_array.length());

							txtbolked.setText(String.valueOf(data_array.length()));

							BlockedUser_Data = response.toString();


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

				android.app.AlertDialog alert = new android.app.AlertDialog.Builder(FavoriteActivity.this).create();
				alert.setMessage("Server Error");
				alert.show();
			}


		});


	}


	public void filter_City(String search_key){

		List_Filter_My_Matches.clear();

		for (int i = 0; i < List_My_Matches.size(); i++){

			try{

				JSONObject object = new JSONObject(List_My_Matches.get(i));

				JSONObject basic_info = object.getJSONObject(All_Constants_Urls.basic_info);

				////////////

				String city = basic_info.getString(All_Constants_Urls.city);
				String country = basic_info.getString(All_Constants_Urls.country);


				if (search_key.equalsIgnoreCase(city) || search_key.equalsIgnoreCase(country)){

					List_Filter_My_Matches.add(List_My_Matches.get(i));

				}


			}catch (Exception e){
				e.printStackTrace();
			}


		}

		if (List_Filter_My_Matches.size() > 0){
			lvSearch.setVisibility(View.VISIBLE);
			tvSearchEmpty.setVisibility(View.GONE);
		}else {
			lvSearch.setVisibility(View.GONE);
			tvSearchEmpty.setVisibility(View.VISIBLE);
		}


		favourite_search_adapter = new Favourite_Search_Adapter(FavoriteActivity.this, List_Filter_My_Matches);
		lvSearch.setAdapter(favourite_search_adapter);
		lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				show_Profile_Videos(List_Filter_My_Matches.get(position));

			}
		});


	}


	public void getNotificationData(){

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


						}else
						if (success == 1){

							sharedPref.save_Notification(response.toString());


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

	public void updateIsNewMatches(){

		String URL = All_Constants_Urls.readmatches;
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();

		params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
		params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());
		params.put("match_friend_id", ToUserID);


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

						int status = response.optInt("status");
						String message = response.optString("message");

						if (status == 0){


						}else
						if (status == 1){

							get_MyMatches_Data();

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


						}else
						if (success == 1){

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

		android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
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


		android.app.AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();


	}


	private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// Extract data included in the Intent
			String message = intent.getStringExtra("message");
			//do other stuff here
			Log.d(All_Constants_Urls.TAG, "Message = "+message);

			get_MyMatches_Data();

			getNotificationData();

		}

	};


}
