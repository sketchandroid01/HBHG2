package com.hbhgdating.screens;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.hbhgdating.BuildConfig;
import com.hbhgdating.DatabaseLocal.DatabaseHelper;
import com.hbhgdating.Fb_Insta_Media.AllMediaFiles;
import com.hbhgdating.R;
import com.hbhgdating.Services.MyService;
import com.hbhgdating.Trimmer.TrimmerActivity;
import com.hbhgdating.slider.Animations.DescriptionAnimation;
import com.hbhgdating.slider.Indicators.PagerIndicator;
import com.hbhgdating.slider.SliderLayout;
import com.hbhgdating.slider.SliderTypes.BaseSliderView;
import com.hbhgdating.slider.SliderTypes.TextSliderView;
import com.hbhgdating.slider.Tricks.ViewPagerEx;
import com.hbhgdating.utils.All_Constants_Urls;
import com.hbhgdating.utils.Global_Class;
import com.hbhgdating.utils.Initialize_Filter;
import com.hbhgdating.utils.ScalableVideoView;
import com.hbhgdating.utils.SharedPref;
import com.hbhgdating.utils.Utility;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;
import life.knowledge4.videotrimmer.utils.FileUtils;

/**
 * Created by Developer on 4/21/17.
 */

public class Check_Video extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private static final int ACTIVITY_SELECT_IMAGE = 1111;
    private static final int FILTER_URI_ACTIVITY_RESULT_CODE = 2222;
    private static final int TRIMMER_ACTIVITY_RESULT_CODE = 3333;
    private static final int ALL_MEDIA_ACTIVITY_RESULT_CODE = 4444;
    private static final int FILTER_URL_ACTIVITY_RESULT_CODE = 5555;
    private static final int FIRST_TIME_FILTER_URL_ACTIVITY_RESULT_CODE = 6666;
    private static final int VIDEO_CAPTURE = 101;
    private static final int PICK_VIDEO_REQUEST = 102;
    public static Check_Video check_video;
    Global_Class global_class;
    SliderLayout mDemoSlider, slider_2nd;
    ScalableVideoView videoView1;
    ImageView tv_save, tv_edit;
    Boolean isPanelShown = false;
    RelativeLayout layout_top;
    LinearLayout layout_bottom;
    TextView tv_user_detail, tv_user_location;
    LayoutInflater inflater;
    RelativeLayout ll_progressbar;
    Gallery gallery;
    ArrayList<Image> Selected_Gallery_Images;
    String Captured_Video_Path;
    String mCurrentPhotoPath;
    AlertDialog alertDialog, alertDialog2;
    Uri videoUri;
    MediaController mediaController;
    LocationManager locationManager;
    boolean doubleBackToExitPressedOnce = false;
    SharedPref sharedPref;
    int code_uri_filter;
    int code_url_filter;
    ArrayList<String> List_File;
    String TAG = "TAG";

    Utility utility;
    int filter_code;
    ArrayList<String> List_Insta_Uri;
    ArrayList<String> List_FB_Uri;
    ImageView Test_image;
    AccessTokenTracker accessTokenTracker;
    String FB_token, FB_user_id;
    boolean checking = true;
    String profile_pic_dir;
    Dialog progressDialog;
    CallbackManager callbackManager;
    boolean is_video_upload = false;
    private Uri fileUri;
    private DatabaseHelper dbhelper;


    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
    protected GoogleApiClient mGoogleApiClient;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    boolean buildstatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_video);

        utility = new Utility(this);
        global_class = (Global_Class) getApplicationContext();
        global_class.Final_Image_To_Upload.clear();
        sharedPref = new SharedPref(this);
        sharedPref.getLogin_ViaTo_Global();


        if (global_class.getLogin_via().equals("fb")) {

            sharedPref.Set_FB_InfoToGlobal();

        } else if (global_class.getLogin_via().equals("insta")) {

            sharedPref.Set_INSTA_InfoToGlobal();
        }

        sharedPref.Set_LOCATION_GPS_to_Global();

        dbhelper = new DatabaseHelper(this);


        mDemoSlider = (SliderLayout) findViewById(R.id.slider_intro);
        slider_2nd = (SliderLayout) findViewById(R.id.slider_2nd);
        slider_2nd.setVisibility(View.GONE);

        tv_save = (ImageView) findViewById(R.id.tv_save);
        tv_edit = (ImageView) findViewById(R.id.tv_edit);

        check_video = this;


        tv_user_detail = (TextView) findViewById(R.id.tv_user_detail);
        tv_user_location = (TextView) findViewById(R.id.tv_user_location);

        videoView1 = (ScalableVideoView) findViewById(R.id.videoView1);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView1);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        Test_image = (ImageView) findViewById(R.id.Test_image);
        Test_image.setVisibility(View.GONE);

        ll_progressbar = (RelativeLayout) findViewById(R.id.ll_progressbar);
        ll_progressbar.setVisibility(View.GONE);


        progressDialog = new Dialog(this, android.R.style.Theme_Translucent);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setContentView(R.layout.progressbar_creating_video);
        progressDialog.setCancelable(false);


        if (global_class.getLogin_via().equals("fb")) {

            Get_FB_Url_from_Database();


            tv_user_detail.setText(global_class.FB_profile_first_name + ", " + global_class.FB_profile_age + ", " + global_class.FB_profile_gender);

            tv_user_location.setText(global_class.getLocation_gps());


            String full_name = global_class.FB_profile_name;
            String lastName = "";
            String firstName = "";

            if (global_class.getFB_profile_name().split(" ").length > 1) {

                lastName = full_name.substring(full_name.lastIndexOf(" ") + 1);
                firstName = full_name.substring(0, full_name.lastIndexOf(' '));
                global_class.FB_profile_first_name = firstName;


                Log.d(TAG, "lastName>>>>   " + lastName);
                Log.d(TAG, "firstName>>>>   " + firstName);
            }


            tv_user_detail.setText(global_class.FB_profile_first_name + ", "
                    + global_class.getFB_profile_age() + ", "
                    + global_class.getFB_profile_gender());


            tv_user_location.setText(global_class.getLocation_gps());


        } else if (global_class.getLogin_via().equals("insta")) {


            Get_INSTA_Url_from_Database();


            tv_user_detail.setText(global_class.Insta_first_name);
            tv_user_location.setText(global_class.getLocation_gps());


        }


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Handle_Permissions();

        } else {

            startService(new Intent(this, MyService.class));
        }


        layout_bottom = (LinearLayout) findViewById(R.id.layout_bottom);
        layout_bottom.setVisibility(View.INVISIBLE);

        layout_top = (RelativeLayout) findViewById(R.id.layout_top);
        layout_top.setVisibility(View.INVISIBLE);


        tv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               // Log.d("TAG", "Lat = " + global_class.LATITUDE);
              //  Log.d("TAG", "Lng = " + global_class.LATITUDE);


                // ll_progressbar.setVisibility(View.VISIBLE);

                progressDialog.show();

                if (!is_video_upload) {

                    if (global_class.Final_Image_To_Upload.size() == 0) {


                       // ImHappyDialog customDialog = new ImHappyDialog(Check_Video.this);
                       // customDialog.show();

                        Toast.makeText(Check_Video.this, "No Images selected", Toast.LENGTH_SHORT).show();

                    } else {

                        ImHappyDialog customDialog = new ImHappyDialog(Check_Video.this);
                        customDialog.show();

                    }

                } else {

                    ImHappyDialog customDialog = new ImHappyDialog(Check_Video.this);
                    customDialog.show();
                }


                //  ll_progressbar.setVisibility(View.GONE);


               // Log.d("TAG", "Final_Image_To_Upload = " + global_class.Final_Image_To_Upload.size());

                progressDialog.dismiss();


            }
        });

        tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CustomDialog();
            }
        });


        buildGoogleApiClient();


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }


    @Override
    protected void onStart() {

        super.onStart();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle connectionHint) {


    }

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
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We
        // call connect() to
        // attempt to re-establish the connection.
        Log.e("mLastLocation", "Connection suspended");
        mGoogleApiClient.connect();
    }


    public void Handle_Permissions(){

        enableloc();

        if (ContextCompat.checkSelfPermission(Check_Video.this,
                android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED &&

                ContextCompat.checkSelfPermission(Check_Video.this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED &&

                ContextCompat.checkSelfPermission(Check_Video.this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED &&

                ContextCompat.checkSelfPermission(Check_Video.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {

            startService(new Intent(this, MyService.class));

        } else {
            if (checkForPermission(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, 124)) {

            } else {

                startService(new Intent(this, MyService.class));
            }

        }

    }

    public boolean checkForPermission(final String[] permissions, final int permRequestCode) {

        final List<String> permissionsNeeded = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            final String perm = permissions[i];
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(Check_Video.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {

                    Log.d(All_Constants_Urls.TAG, "not granted");

                    if (shouldShowRequestPermissionRationale(permissions[i])) {

                        Log.d(All_Constants_Urls.TAG, "if");
                        permissionsNeeded.add(perm);

                    } else {
                        // add the request.
                        Log.d(All_Constants_Urls.TAG, "else");
                        permissionsNeeded.add(perm);
                    }


                    startService(new Intent(this, MyService.class));

                }else {

                    //enableLocation();

                    startService(new Intent(this, MyService.class));
                }
            }
        }

        if (permissionsNeeded.size() > 0) {
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // go ahead and request permissions
                requestPermissions(permissionsNeeded.toArray(new String[permissionsNeeded.size()]), permRequestCode);
            }
            return false;
        } else {
            // no permission need to be asked so all good...we have them all.

           // Log.d(All_Constants_Urls.TAG, "no permission need to be asked so all good");

            return true;
        }

    }


    public void enableloc() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                        }

                        @Override
                        public void onConnectionSuspended(int i) {
                            mGoogleApiClient.connect();
                        }
                    })
                    .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult connectionResult) {

                            Log.d("Location error", "Location error " + connectionResult.getErrorCode());
                        }
                    }).build();
            mGoogleApiClient.connect();
        }
        buildstatus=false;
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        final LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(Check_Video.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                }
            }
        });
    }


    private boolean hasCamera() {
        if (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FRONT)) {
            return true;
        } else {
            return false;
        }
    }


    public void Get_FB_Url_from_Database() {

        global_class.Image_Url_FB.clear();


        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_FB;
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor1 = db.rawQuery(selectQuery, null);

        if (cursor1.moveToFirst()) {
            do {
                String url = cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.IMG_URL_FB));

                global_class.Image_Url_FB.add(url);


            } while (cursor1.moveToNext());
        }

        db.close();

        showslider_FB();


        new Handler().postDelayed(new Runnable() {
            public void run() {
                layout_bottom.setVisibility(View.VISIBLE);
                layout_top.setVisibility(View.VISIBLE);
                Animation bottomUp = AnimationUtils.loadAnimation(Check_Video.this,
                        R.anim.bottom_up);

                layout_bottom.startAnimation(bottomUp);

                Animation top_down = AnimationUtils.loadAnimation(Check_Video.this,
                        R.anim.slide_down);

                layout_top.startAnimation(top_down);
            }
        }, 7000);



        if (global_class.Image_Url_FB.size() > 7){

            for (int i = 0; i < 7; i++){
                global_class.Final_Image_To_Upload.add(global_class.Image_Url_FB.get(i));
            }

        }else {

            global_class.Final_Image_To_Upload = global_class.Image_Url_FB;
        }



    }


    public void Get_INSTA_Url_from_Database() {

        global_class.Image_Url_INSTA.clear();


        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_INSTA;
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor1 = db.rawQuery(selectQuery, null);

        if (cursor1.moveToFirst()) {
            do {
                String url = cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.IMG_URL_INSTA));

                global_class.Image_Url_INSTA.add(url);


            } while (cursor1.moveToNext());
        }

        // Log.d("TAG", "Image_Url_INSTA = "+global_class.Image_Url_INSTA);

        db.close();

        showslider_insta();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                layout_bottom.setVisibility(View.VISIBLE);
                layout_top.setVisibility(View.VISIBLE);
                Animation bottomUp = AnimationUtils.loadAnimation(Check_Video.this,
                        R.anim.bottom_up);

                layout_bottom.startAnimation(bottomUp);

                Animation top_down = AnimationUtils.loadAnimation(Check_Video.this,
                        R.anim.slide_down);

                layout_top.startAnimation(top_down);
            }
        }, 7000);



        if (global_class.Image_Url_INSTA.size() > 7){

            for (int i = 0; i < 7; i++){
                global_class.Final_Image_To_Upload.add(global_class.Image_Url_INSTA.get(i));
            }

        }else {

            global_class.Final_Image_To_Upload = global_class.Image_Url_INSTA;
        }


    }


    public void showslider_FB() {
        mDemoSlider.removeAllSliders();

        if (global_class.Image_Url_FB.size() > 7) {

            for (int i = 0; i < 7; i++) {

                TextSliderView textSliderView2 = new TextSliderView(this);
                // initialize a SliderLayout
                textSliderView2
                        .image(global_class.Image_Url_FB.get(i))
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(this);

                mDemoSlider.addSlider(textSliderView2);
            }


        } else {

            for (int i = 0; i < global_class.Image_Url_FB.size(); i++) {

                TextSliderView textSliderView2 = new TextSliderView(this);
                // initialize a SliderLayout
                textSliderView2
                        .image(global_class.Image_Url_FB.get(i))
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(this);

                mDemoSlider.addSlider(textSliderView2);
            }


        }

        videoEffect();

    }

    public void showslider_insta() {

        mDemoSlider.removeAllSliders();

        if (global_class.Image_Url_INSTA.size() > 7) {

            for (int i = 0; i < 7; i++) {

                TextSliderView textSliderView3 = new TextSliderView(this);
                // initialize a SliderLayout
                textSliderView3
                        .image(global_class.Image_Url_INSTA.get(i))
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(this);

                mDemoSlider.addSlider(textSliderView3);
            }

        } else {

            for (int i = 0; i < global_class.Image_Url_INSTA.size(); i++) {

                TextSliderView textSliderView3 = new TextSliderView(this);
                // initialize a SliderLayout
                textSliderView3
                        .image(global_class.Image_Url_INSTA.get(i))
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(this);

                mDemoSlider.addSlider(textSliderView3);
            }
        }

        videoEffect();


    }


    ////////////////////////////


    public void videoEffect() {
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        //slider1.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(2000);
        mDemoSlider.moveNextPosition(true);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.startAutoCycle();
    }

    /// disconnect from facebook
    public void disconnectFromFacebook() {

        if (AccessToken.getCurrentAccessToken() == null) {
            return; // already logged out
        }

        new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                .Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {

                LoginManager.getInstance().logOut();

                Log.d("TAG", "LoginManager.getInstance().logOut()");

            }
        }).executeAsync();
    }


    @Override
    protected void onStop() {

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        slider_2nd.stopAutoCycle();

        dbhelper.close();

        super.onStop();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

        String trans[] = {"", "ZoomOut", "ZoomOutSlide", "Tablet", "DepthPage", "ZoomOut", "Fade", "RotateDown"};
        Random rand = new Random();
        int randomNum = rand.nextInt((7 - 1) + 1) + 1;
        mDemoSlider.setPresetTransformer(trans[randomNum]);
        slider_2nd.setPresetTransformer(trans[randomNum]);
        Log.d("Slider Demo", "Page Changed: " + position + "random" + randomNum);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

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
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    public void CustomDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        // ...Irrelevant code_uri_filter for customizing the buttons and title
        inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_custom_dialog, null);
        dialogBuilder.setView(dialogView);

        RelativeLayout attach_gallery = (RelativeLayout) dialogView.findViewById(R.id.attach_gallery);
        RelativeLayout rl_video = (RelativeLayout) dialogView.findViewById(R.id.rl_video);
        RelativeLayout insta_rl = (RelativeLayout) dialogView.findViewById(R.id.insta_rl);
        RelativeLayout rl_fb = (RelativeLayout) dialogView.findViewById(R.id.rl_fb);


        alertDialog = dialogBuilder.create();
        alertDialog.show();

        insta_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sharedPref.isLOGIN_INSTA()) {

                    global_class.Selected_Image_Url.clear();

                    videoView1.setVisibility(View.GONE);
                    Intent intent = new Intent(Check_Video.this, AllMediaFiles.class);
                    intent.putExtra("key", "insta");
                    startActivityForResult(intent, ALL_MEDIA_ACTIVITY_RESULT_CODE);
                    alertDialog.dismiss();

                } else {

                    // Toast.makeText(getApplicationContext(), "You are not login with Instagram", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(Check_Video.this, Facebook_Insta_Login.class);
                    intent.putExtra("key", "insta");
                    startActivity(intent);
                    alertDialog.dismiss();

                }

            }
        });

        rl_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sharedPref.isLOGIN_FB()) {

                    global_class.Selected_Image_Url.clear();

                    videoView1.setVisibility(View.GONE);
                    Intent intent = new Intent(Check_Video.this, AllMediaFiles.class);
                    intent.putExtra("key", "fb");
                    startActivityForResult(intent, ALL_MEDIA_ACTIVITY_RESULT_CODE);
                    alertDialog.dismiss();

                } else {

                    // Toast.makeText(getApplicationContext(), "You are not login with Facebook", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(Check_Video.this, Facebook_Insta_Login.class);
                    intent.putExtra("key", "fb");
                    startActivity(intent);
                    alertDialog.dismiss();

                }

            }
        });

        attach_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                videoView1.setVisibility(View.GONE);
                Intent intent = new Intent(Check_Video.this, AlbumSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 7); // set limit for image selection
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                alertDialog.dismiss();

            }
        });

        rl_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Check_Video.this);
                // ...Irrelevant code_uri_filter for customizing the buttons and title
                View dialogView1 = inflater.inflate(R.layout.add_video_dialog, null);
                dialogBuilder.setView(dialogView1);

                LinearLayout gallery_video = (LinearLayout) dialogView1.findViewById(R.id.gallery_video);
                LinearLayout camera_video = (LinearLayout) dialogView1.findViewById(R.id.camera_video);


                alertDialog2 = dialogBuilder.create();
                alertDialog2.show();


                gallery_video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setTypeAndNormalize("video/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST);


                        alertDialog2.dismiss();

                    }
                });

                camera_video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {

                            File mediaFile = new File(utility.get_HBHG_Directory() + "/My_video.mp4");

                            /// File mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/My_video_"+System.currentTimeMillis()+".mp4");

                            Log.d("TAG", "mediaFile = " + mediaFile);

                            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                            if (mediaFile != null) {

                                Captured_Video_Path = mediaFile.toString();

                                takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 7);
                                takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);


                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    takeVideoIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    Uri contentUri = FileProvider.getUriForFile(getApplicationContext(),
                                            BuildConfig.APPLICATION_ID + ".provider", mediaFile);
                                    takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                                } else {
                                    takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile));
                                }

                                startActivityForResult(takeVideoIntent, VIDEO_CAPTURE);


                            }


                            alertDialog2.dismiss();


                        } else {

                            Toast.makeText(getApplicationContext(), "No camera on device", Toast.LENGTH_LONG).show();

                        }


                    }


                });


            }
        });


    }

    @SuppressLint("SimpleDateFormat")
    private File createVideoFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        String imageFileName = timeStamp + ".mp4";
        File storageDir = getExternalFilesDir(null);
        File imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        return imageFile;
    }


    public void Play_Video(final Uri videoUri) {
        mDemoSlider.removeAllSliders();
        mDemoSlider.setVisibility(View.GONE);
        videoView1.setVisibility(View.VISIBLE);
        mediaController.setVisibility(View.GONE);

        mediaController.setAnchorView(videoView1);


        //Setting MediaController and URI, then starting the videoView
        videoView1.setMediaController(mediaController);
        videoView1.setVideoURI(videoUri);
        videoView1.requestFocus();
        videoView1.start();

        videoView1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                videoView1.setVideoURI(videoUri);
                videoView1.requestFocus();
                videoView1.start();
            }
        });


    }


    ///// On activity result ...........

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == ConstantsCustomGallery.REQUEST_CODE &&
                resultCode == Activity.RESULT_OK && data != null) {
            //The array list has the image paths of the selected Selected_Gallery_Images

            mDemoSlider.removeAllSliders();
            Selected_Gallery_Images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);

            is_video_upload = false;

            new Asyntask_From_Gallary().execute();


        } else if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                // Toast.makeText(this, "Video has been saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();

                mDemoSlider.removeAllSliders();

                if (data == null) {

                    Uri video_uri = Uri.fromFile(new File(Captured_Video_Path));

                   // Log.d("TAG", "VIDEO_CAPTURE = " + video_uri);

                    global_class.setFinal_Video_To_upload(video_uri.toString());

                    Play_Video(video_uri);

                    global_class.Final_Image_To_Upload.clear();

                    is_video_upload = true;

                } else if (data.getData() != null) {

                   // Log.d("TAG", "VIDEO_CAPTURE = " + data.getData());

                    global_class.setFinal_Video_To_upload(data.getData().toString());

                    Play_Video(data.getData());

                    global_class.Final_Image_To_Upload.clear();

                    is_video_upload = true;

                } else {

                    Toast.makeText(this, "Something went wrong",
                            Toast.LENGTH_LONG).show();

                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video",
                        Toast.LENGTH_LONG).show();
            }

        } else if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK) {

            Uri selectedImageUri = data.getData();

            if (checkVideoLength(selectedImageUri)){

                Intent intent = new Intent(Check_Video.this, TrimmerActivity.class);
                intent.putExtra("path", FileUtils.getPath(this, selectedImageUri));
                startActivityForResult(intent, TRIMMER_ACTIVITY_RESULT_CODE);
            }

            // OI FILE Manager
           // String filemanagerstring = selectedImageUri.getPath();

            /*Log.d("TAG", "path = " + selectedImageUri);*/


        } else if (requestCode == TRIMMER_ACTIVITY_RESULT_CODE && resultCode == RESULT_OK) {

            String path_uri = data.getExtras().getString("uri");
            Uri myUri = Uri.parse(path_uri);

            global_class.setFinal_Video_To_upload(path_uri);

            Play_Video(myUri);

            is_video_upload = true;
            global_class.Final_Image_To_Upload.clear();

        } else if (requestCode == ALL_MEDIA_ACTIVITY_RESULT_CODE && resultCode == RESULT_OK) {

            String check = data.getExtras().getString("key");

            if (check.equals("image")) {

                mDemoSlider.stopAutoCycle();
                mDemoSlider.removeAllSliders();

                progressDialog.show();

                if (sharedPref.isLOGIN_FB()) {

                    global_class.Image_Url_FB.clear();

                    global_class.Image_Url_FB = global_class.Selected_Image_Url;

                    global_class.Final_Image_To_Upload = global_class.Selected_Image_Url;

                    mDemoSlider.setVisibility(View.INVISIBLE);

                    showslider_FB();

                    new Handler().postDelayed(new Runnable() {
                        public void run() {

                            progressDialog.dismiss();
                        }
                    }, 2000);

                    slider_2nd.setVisibility(View.GONE);
                    slider_2nd.removeAllSliders();
                    mDemoSlider.setVisibility(View.VISIBLE);


                } else if (sharedPref.isLOGIN_INSTA()) {

                    global_class.Image_Url_INSTA.clear();

                    global_class.Image_Url_INSTA = global_class.Selected_Image_Url;

                    global_class.Final_Image_To_Upload = global_class.Image_Url_INSTA;

                    mDemoSlider.setVisibility(View.INVISIBLE);
                    showslider_insta();

                    new Handler().postDelayed(new Runnable() {
                        public void run() {

                            progressDialog.dismiss();
                        }
                    }, 2000);

                    slider_2nd.setVisibility(View.GONE);
                    slider_2nd.removeAllSliders();
                    mDemoSlider.setVisibility(View.VISIBLE);

                }

            } else if (check.equals("video")) {

                String video = data.getExtras().getString("file");

                Intent intent = new Intent(Check_Video.this, TrimmerActivity.class);
                intent.putExtra("path", video);
                startActivityForResult(intent, TRIMMER_ACTIVITY_RESULT_CODE);

            }

        }


    }


    public boolean checkVideoLength(Uri uri){

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(Check_Video.this, uri);
        String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        long timeInMillisec = Long.parseLong(time );


        int minutes = (int) ((timeInMillisec / (1000*60)) % 60);
        int seconds = (int) (timeInMillisec / 1000) % 60 ;


        if (minutes > 30){

            Toast.makeText(getApplicationContext(), "Video is too large" , Toast.LENGTH_LONG).show();

            return false;
        }else if (seconds < 7){

            global_class.setFinal_Video_To_upload(uri.toString());

            Play_Video(uri);

            is_video_upload = true;
            global_class.Final_Image_To_Upload.clear();

        }

        return true;
    }


    //////////////////////
    //////////////////////
    ///  Filter effects .....

    private void saveBitmap(Bitmap bmp, String fileName, int code) {

        try {

            // imgMain.setImageBitmap(bmp);
            File f = new File(utility.get_HBHG_Directory() + fileName + code + ".jpg");
            FileOutputStream fos = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);

            List_File.add(f.toString());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void videoEffect_For_2nd() {
        slider_2nd.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        slider_2nd.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        slider_2nd.setCustomAnimation(new DescriptionAnimation());
        slider_2nd.setDuration(2000);
        slider_2nd.moveNextPosition(true);
        slider_2nd.addOnPageChangeListener(this);
        slider_2nd.startAutoCycle();
        slider_2nd.setVisibility(View.VISIBLE);
    }

    private void save_Filter_Bitmap(Bitmap bmp, String fileName, int code) {

        if (sharedPref.isLOGIN_FB()) {

            try {
                File f = new File(utility.get_HBHG_Directory() + fileName + code + ".jpg");
                FileOutputStream fos = new FileOutputStream(f);
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);

                List_FB_Uri.add(f.toString());

            } catch (Exception ex) {
                ex.printStackTrace();
            }


        } else if (sharedPref.isLOGIN_INSTA()) {
            try {

                File f = new File(utility.get_HBHG_Directory() + fileName + code + ".jpg");
                FileOutputStream fos = new FileOutputStream(f);
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);

                List_Insta_Uri.add(f.toString());

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }


    ////////////////////////////////  For Download image and filter effect .......

    @Override
    protected void onResume() {

        videoEffect();
        videoEffect_For_2nd();

        disconnectFromFacebook();

        startService(new Intent(this, MyService.class));

        super.onResume();
    }

    @Override
    protected void onPause() {

        mDemoSlider.stopAutoCycle();
        slider_2nd.stopAutoCycle();

        super.onPause();
    }

    private class Asyntask_From_Gallary extends AsyncTask<Void, Void, Void> {

        Bitmap src;

        @Override
        protected void onPreExecute() {

            progressDialog.show();

            mDemoSlider.setVisibility(View.GONE);
            mDemoSlider.removeAllSliders();

            List_File = new ArrayList<>();
            List_File.clear();

            slider_2nd.removeAllSliders();
            slider_2nd.stopAutoCycle();

            global_class.Final_Image_To_Upload.clear();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            for (int i = 0; i < Selected_Gallery_Images.size(); i++) {

                Uri selectedImage = Uri.fromFile(new File(Selected_Gallery_Images.get(i).path.toString()));

                src = utility.decodeUri_640(selectedImage);

                saveBitmap(src, "P_IMG", i);

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            slider_2nd.removeAllSliders();

            global_class.Final_Image_To_Upload = List_File;

            for (int i = 0; i < List_File.size(); i++) {

              //  Log.d("TAG", "Image path = " + List_File.get(i));

                TextSliderView textSliderView1 = new TextSliderView(Check_Video.this);
                // initialize a SliderLayout
                textSliderView1
                        .image(new File(List_File.get(i)))
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(Check_Video.this);

                slider_2nd.addSlider(textSliderView1);

            }

            new Handler().postDelayed(new Runnable() {
                public void run() {

                    //asyncDialog.dismiss();
                }
            }, 3000);


            videoEffect_For_2nd();

            progressDialog.dismiss();

            super.onPostExecute(result);
        }

    }


    //////////////////////////


}





