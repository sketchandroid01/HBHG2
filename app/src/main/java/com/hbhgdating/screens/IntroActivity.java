package com.hbhgdating.screens;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.location.LocationServices;
import com.hbhgdating.databaseLocal.DatabaseHelper;
import com.hbhgdating.databaseLocal.ImageData;
import com.hbhgdating.R;
import com.hbhgdating.services.MyService;
import com.hbhgdating.insta.Constant_C;
import com.hbhgdating.insta.InstagramApp;
import com.hbhgdating.utils.Common;
import com.hbhgdating.utils.ConnectivityReceiver;
import com.hbhgdating.utils.Global_Class;
import com.hbhgdating.utils.SharedPref;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;


/**
 * Created by Developer on 3/31/17.
 */
public class IntroActivity extends FragmentActivity implements ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    ViewPager mPager;
    PageIndicator mIndicator;
    ViewPagerAdapter adapter;
    public CallbackManager callbackManager;
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    private GoogleApiClient client;
    TextView info;
    Boolean slidedone= false;
    Handler handler_autoslide;
    Runnable update;
    String token ="" ,user_id ="", album_id = "";
    String profile_pic_dir ;
    String url;

    boolean doubleBackToExitPressedOnce = false;
    ProfileTracker profileTracker;
    Dialog progressDialog;
    private ViewPagerAdapter.ViewHolder viewHolder;
    boolean checking = true;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private DatabaseHelper dbhelper;
    RelativeLayout rel1, rel2, ll_progressbar;
    LoginButton loginButton;
    TextView login_insta,txt_hey,txt_did,txt_to_make,txt_sevensecond, tv_privacy_policy;
    RelativeLayout rel_fb,rel_insta,relativeLayout;
    SharedPref sharedPref;


///////////////////////////////////////////////insta///////////////////////////////////

    private static int WHAT_ERROR = 1;
    Global_Class global_class;
    String TAG = "TAG";
    InstagramApp mApp;
    Boolean gps_enabled = false;
    private String provider;
    boolean FB_Login_checking = false;
    Boolean netwrk_enabled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = new SharedPref(this);

        if (sharedPref.isLOGIN_FB() || sharedPref.isLOGIN_INSTA()){

            if (sharedPref.isSAVE_VIDEO()){

                Intent intent  = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            } else {

                InitViews();

            }

        }else {

            InitViews();

        }

        CheckPermission();

    }


    public  void CheckPermission(){
        ///////////////////////////////

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ( ContextCompat.checkSelfPermission(IntroActivity.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED &&

                    ContextCompat.checkSelfPermission(IntroActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {



            }
            else{
                if(checkForPermission2(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, 124)){

                }

            }
        }


    }

    public void InitViews(){

        setContentView(R.layout.intro);

        dbhelper = new DatabaseHelper(this);


        disconnectFromFacebook();

        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

        gps_enabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        netwrk_enabled = locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps_enabled)
            provider = LocationManager.GPS_PROVIDER;
        else
            provider = LocationManager.NETWORK_PROVIDER;


        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


        global_class = (Global_Class)getApplicationContext();

        global_class.login_via="";
        global_class.Location_gps="";
        global_class.FB_profile_gender = "";


        mPager = (ViewPager) findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(IntroActivity.this);
        mPager.setAdapter(adapter);
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

        info = new TextView(this);
       // client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , this)
                .addApi(LocationServices.API)
                .build();


        // print out the key hash
        try {

            String pkgname = getPackageName();

            PackageInfo info = getPackageManager().getPackageInfo(pkgname , PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.d("TAG" , "KeyHash:"+something);

            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //////insta//////

    public boolean checkForPermission2(final String[] permissions, final int permRequestCode) {

        final List<String> permissionsNeeded = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            final String perm = permissions[i];
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(IntroActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {

                    Log.d("permisssion","not granted");
                    if (shouldShowRequestPermissionRationale(permissions[i])) {

                        Log.d("if","if");
                        permissionsNeeded.add(perm);

                    } else {
                        // add the request.
                        Log.d("else","else");
                        permissionsNeeded.add(perm);
                    }

                }else {
                    Log.d("permisssion", "granted");
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

            Log.d("permisssion", "granted");
            startService(new Intent(this, MyService.class));
                        return true;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

          /*  Intent myIntent = new Intent(IntroActivity.this,MainActivity.class);
            startActivity (myIntent);
            finish(); //if you want to do not use this*/

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
            } else {

                Log.e("mLastLocation", "no_location_detected");

            }
            return;
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("mLastLocation", "Connection suspended");
        mGoogleApiClient.connect();
    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    @Override
    protected void onPause() {
        checking = true;
        super.onPause();
    }

    @Override
    public void onResume(){
        checking = true;
        //relativeLayout.setVisibility(View.VISIBLE);
        super.onResume();
    }


    class ViewPagerAdapter extends PagerAdapter  {
        // Declare Variables
        Context context;
        LayoutInflater inflater;

        AccessTokenTracker accessTokenTracker ;
        AccessToken accessToken ;
        String token ="" ,user_id ="";
        boolean bAcceptTerms = false;



        public ViewPagerAdapter(Context context) {
            this.context = context;

            progressDialog = new Dialog(context, android.R.style.Theme_Translucent);
            progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressDialog.getWindow().setContentView(R.layout.progressbar_creating_video);
            progressDialog.setCanceledOnTouchOutside(false);


        }

        public class ViewHolder {

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // Remove viewpager_item.xml from ViewPager
            ((ViewPager) container).removeView((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Declare Variables
           // final CallbackManager callbackManager = CallbackManager.Factory.create();
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View itemView = inflater.inflate(R.layout.viewpager_item, container, false);

            viewHolder = new ViewHolder();

            rel1 = itemView.findViewById(R.id.rel1);
            rel2 = itemView.findViewById(R.id.rel2);
            login_insta = itemView. findViewById(R.id.login_insta) ;
            rel_fb = itemView. findViewById(R.id.rel_fb) ;
            rel_insta = itemView. findViewById(R.id.rel_insta) ;
            relativeLayout = itemView. findViewById(R.id.relativeLayout) ;
            tv_privacy_policy = itemView. findViewById(R.id.tv_privacy_policy) ;


            txt_hey = itemView. findViewById(R.id.txt_hey) ;
            txt_did = itemView. findViewById(R.id.txt_did) ;
            txt_to_make = itemView. findViewById(R.id.txt_to_make) ;
            txt_sevensecond = itemView. findViewById(R.id.txt_sevensecond) ;

            loginButton = itemView.findViewById(R.id.login_button);

            ll_progressbar = itemView.findViewById(R.id.ll_progressbar);
            ll_progressbar.setVisibility(View.GONE);


            rel_fb.setVisibility(View.VISIBLE);


            showHideLayout(position);


            ((ViewPager) container).addView(itemView);

            mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    if (position==0){
                        autoslide();

                    }

                }

                @Override
                public void onPageScrollStateChanged(int state) {


                }
            });


            tv_privacy_policy.setPaintFlags(tv_privacy_policy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            tv_privacy_policy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String url = Common.privacy_url;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });

            /////////////   Insta login ...........

            mApp = new InstagramApp(IntroActivity.this, Constant_C.CLIENT_ID,
                    Constant_C.CLIENT_SECRET, Constant_C.CALLBACK_URL, relativeLayout);
            mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {
                @Override
                public void onSuccess() {
                    Log.d("TAG", "Instagram onSuccess");

                }
                @Override
                public void onFail(String error) {
                    Toast.makeText(IntroActivity.this, error, Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "Instagram = "+error);
                    relativeLayout.setVisibility(View.VISIBLE);
                }
            });


            rel_insta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  relativeLayout.setVisibility(View.INVISIBLE);

                    //global_class.setLogin_via("insta");

                    if (ConnectivityReceiver.isConnected()){

                        connectOrDisconnectUser();

                    }else {

                        ShowToast("Please connect to internet");

                    }


                }
            });


            ///// FB login ............

            loginButton.setReadPermissions(Arrays.asList("email", "user_photos", "user_location", "user_birthday", "user_likes"));

            rel_fb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (ConnectivityReceiver.isConnected()){

                        FB_Login_checking = true;

                        if (FB_Login_checking) {


                            deleteAll_TABLE_FB();


                            FacebookSdk.sdkInitialize(getApplicationContext());
                            AppEventsLogger.activateApp(IntroActivity.this);
                            callbackManager = CallbackManager.Factory.create();


                            //global_class.setLogin_via("fb");

                            relativeLayout.setVisibility(View.INVISIBLE);


                            loginButton.performClick();
                            loginButton.setPressed(true);
                            loginButton.invalidate();
                            loginButton.registerCallback(callbackManager, mCallBack);
                            loginButton.setPressed(false);
                            loginButton.invalidate();

                        }

                    }else {

                        ShowToast("Please connect to internet");

                    }


                }

            });

            accessTokenTracker = new AccessTokenTracker() {

                @Override
                protected void onCurrentAccessTokenChanged(
                        AccessToken oldAccessToken,
                        AccessToken currentAccessToken) {
                    // Set the access token using
                    // currentAccessToken when it's loaded or set.
                    if (AccessToken.getCurrentAccessToken() != null) {

                        token = AccessToken.getCurrentAccessToken().getToken().toString();
                        Log.d("TAG", "token 1: " + token +
                                "Application id :" + AccessToken.getCurrentAccessToken().getApplicationId() +
                                "User id: " + AccessToken.getCurrentAccessToken().getUserId());
                        user_id = AccessToken.getCurrentAccessToken().getUserId();
                        Log.d("TAG", "user_id: "+user_id);

                        if (FB_Login_checking){

                            if (checking){

                                get_all_pic();

                                checking = false;

                            }

                        }

                        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                              //  Log.d("TAG", "response: >>> "+response);
                              //  Log.d("TAG", "Json: >>> "+object);

                                try {

                                    if (FB_Login_checking){

                                        String str_id = object.optString("id");
                                        String name = object.optString("name");
                                        String email = object.optString("email");
                                        String gender = object.optString("gender");
                                        String birthday = object.optString("birthday");


                                        String location_name = "";
                                        global_class.get_location="gps";

                                        /*Log.d(TAG,"locality_gps >>>."+gpsTracker.getLocality(IntroActivity.this));
                                        Log.d(TAG,"country_gps >>>."+gpsTracker.getCountryName(IntroActivity.this));*/


                                        global_class.FB_profile_name = name;
                                        global_class.FB_profile_email = email;
                                        global_class.FB_profile_gender = gender;

                                        global_class.FB_profile_location = location_name;


                                        String full_name = global_class.FB_profile_name;
                                        String lastName = "";
                                        String firstName= "";
                                        if(name.split(" ").length>1){

                                            lastName = full_name.substring(full_name.lastIndexOf(" ")+1);
                                            firstName = full_name.substring(0, full_name.lastIndexOf(' '));
                                            global_class.FB_profile_first_name = firstName;

                                          //  Log.d(TAG,"lastName>>>>   "+lastName);
                                          //  Log.d(TAG,"firstName>>>>   "+firstName);
                                        }
                                        else{
                                            firstName = full_name;
                                          //  Log.d(TAG,"firstName else>>>>   "+firstName);

                                        }


                                        // calculate age ....

                                        SimpleDateFormat from_df = new SimpleDateFormat("M/dd/yyyy");
                                        Date birthdate = from_df.parse(birthday);
                                        int age = calculateAge(birthdate);
                                        //Log.d("TAG", "age >> " + age);
                                        global_class.FB_profile_age = age;


                                        // Date format change ...

                                        String input_date_Pattern = "M/dd/yyyy";
                                        String output_date_Pattern = "MMM d, yyyy";
                                        SimpleDateFormat input_date_Format = new SimpleDateFormat(input_date_Pattern);
                                        SimpleDateFormat output_date_Format = new SimpleDateFormat(output_date_Pattern);
                                        Date date = null;
                                        String str_date = null;

                                        try {

                                            date = input_date_Format.parse(birthday);
                                            str_date = output_date_Format.format(date);

                                            global_class.FB_profile_birthday= str_date;


                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                        String via = "fb";

                                        // save info ......
                                        sharedPref.set_FB_LoginInfo(via, true, str_id, name, email,
                                                String.valueOf(age),
                                                gender, str_date, global_class.getLocation_gps());

                                        sharedPref.set_LOGIN_FB(true);

                                        JSONObject obj_albums = object.getJSONObject("albums");

                                        JSONArray arr_data = obj_albums.getJSONArray("data");
                                        //Log.d(TAG, "arr_data = "+arr_data);

                                        global_class.FB_profile_dir = "";
                                        for (int i = 0; i < arr_data.length(); i++){
                                            JSONObject obj = arr_data.getJSONObject(i);

                                            if (obj.optString("name")
                                                    .equalsIgnoreCase("Profile pictures")){

                                                global_class.FB_profile_dir = obj.optString("id");

                                                Log.d(TAG, "FB_profile_dir = "+global_class.FB_profile_dir);

                                            }

                                        }



                                    }


                                } catch (Exception e) {
                                    e.printStackTrace();

                                    relativeLayout.setVisibility(View.VISIBLE);

                                }

                            }

                        });

                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "albums,id,name,email,birthday,gender,location");
                        request.setParameters(parameters);
                        request.executeAsync();


                    }else {
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                }
            };




            return itemView;
        }





        public int calculateAge(Date birthdate) {

            Calendar birth = Calendar.getInstance();
            birth.setTime(birthdate);

            Calendar today = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("M/dd/yyyy");
            df.setTimeZone(today.getTimeZone());

            int yearDifference = today.get(Calendar.YEAR) - birth.get(Calendar.YEAR);


            //  Log.d("TAG", "today >> " + today.get(Calendar.MONTH));
            //  Log.d("TAG", "birth >> " + birth.get(Calendar.MONTH));


            if (today.get(Calendar.MONTH) < birth.get(Calendar.MONTH)) {
                --yearDifference;


            } else {
                if (today.get(Calendar.MONTH) == birth.get(Calendar.MONTH)
                        && today.get(Calendar.DAY_OF_MONTH) < birth
                        .get(Calendar.DAY_OF_MONTH)) {

                    --yearDifference;

                }

            }

            return yearDifference;
        }



        private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code_uri_filter
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                             //   Log.e("response:main+++ ", response + "");
                             //   Log.e("response:object +++ ", object + "");

                                try {

                                    String str_id = object.optString("id");
                                    String name = object.optString("name");
                                    String email = object.optString("email");
                                    String gender = object.optString("gender");


                                   /* Log.d("TAG", "str_id >> " + str_id);
                                    Log.d("TAG", "name >> " + name);
                                    Log.d("TAG", "email >> " + email);
                                    Log.d("TAG", "gender >> " + gender);*/

                                } catch (Exception e) {
                                    e.printStackTrace();

                                }

                            }

                        });

                /*Bundle parameters = new Bundle();
                parameters.putString("fields", "albums,id,name,email,birthday,gender,location");
                request.setParameters(parameters);
                request.executeAsync();*/
            }
            @Override
            public void onCancel() {

                Log.d("TAG", "FB cancel");

                relativeLayout.setVisibility(View.VISIBLE);

            }

            @Override
            public void onError(FacebookException e) {
                Log.d("TAG", "error = "+e.toString());

                relativeLayout.setVisibility(View.VISIBLE);
            }
        };


        private void showHideLayout(int pos) {
            Log.d("TAG"," "+pos);
            if (pos == 0) {
                autoslide();
                rel1.setVisibility(View.VISIBLE);
                rel2.setVisibility(View.GONE);

            } else if (pos == 1) {
                rel1.setVisibility(View.GONE);
                rel2.setVisibility(View.VISIBLE);

            }
        }




    }

//////////////////////////////////////////////////////////fb pics////////////////////

    public void get_all_pic(){

        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                      //  Log.d("TAG", "response get_all_pic: "+response);
                      //  Log.d("TAG", "Json get_all_pic: "+object);

                    }
                });

      //  Log.d("TAG", "get_all_pic: "+profile_pic_dir);
        Bundle parameters = new Bundle();

        //parameters.putString("fields", "id,email,name,birthday,gender,location");

        parameters.putString("fields","photos");
        request.setParameters(parameters);
        request.executeAsync();
       // Log.d("TAG", "parameters: "+parameters.toString());

        token = AccessToken.getCurrentAccessToken().getToken().toString();
        user_id = AccessToken.getCurrentAccessToken().getUserId();

    //    Log.d("TAG", "user_id getprofile: "+user_id);
    //    Log.d("TAG", "token getprofile: "+token);

        url = "https://graph.facebook.com/v3.3/"+user_id
                +"/?fields=albums.fields(photos.fields(source))&limit=50" +
                "&access_token="+token ;

       // Log.d("TAG", "get_all_pic: "+url);


        ////// Get Image from FB url

        new HttpAsyncTask_1().execute(url);

    }


    private class HttpAsyncTask_1 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            progressDialog.show();
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... urls) {
            return GET(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {

            try {

                ImageData imageData = new ImageData();

                JSONObject obj_full = new JSONObject(result);

              //  Log.d("TAG", "obj_full: "+obj_full.toString());



                JSONObject obj = obj_full.getJSONObject("albums");
                JSONArray data = obj.getJSONArray("data");


                for (int a = 0; a < data.length(); a++){
                    JSONObject obj_data = data.getJSONObject(a);

                    if (global_class.FB_profile_dir.equals(obj_data.optString("id"))){


                        JSONObject profile_pic_dir_obj = data.getJSONObject(a);
                        JSONObject obj_photos = profile_pic_dir_obj.getJSONObject("photos");

                      //  Log.d("TAG", "obj_photos: " + obj_photos);

                        JSONArray data_photo = obj_photos.getJSONArray("data");

                        for (int j = 0; j < data_photo.length(); j++) {

                            JSONObject object_2 = data_photo.getJSONObject(j);

                            String image_url = object_2.optString("source");
                         //   Log.d("TAG", "picurl: " + image_url);
                            if (j == 0){
                                global_class.Profile_Image = image_url;
                            }


                            profile_pic_dir = profile_pic_dir_obj.optString("id");
                         //   Log.d("TAG", "json profile_dir: " + profile_pic_dir);

                            global_class.Image_Url_FB.add(image_url);

                            imageData.setUrl(image_url);
                            insert_Fb_Data(imageData);

                        }



                        Intent myIntent = new Intent(IntroActivity.this, Check_Video.class);
                        myIntent.putExtra("image_url", global_class.Image_Url_FB.get(0));
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(myIntent);
                        finish();

                        progressDialog.dismiss();


                        JSONObject obj_paging = obj_photos.optJSONObject("paging");
                        String next = obj_paging.optString("next");

                        /*Log.d("TAG", "next = " + next);
                        if(global_class.FB_profile_age >= 18) {
                            if (next == null || next.equals(null) || next.equals("")){

                                Intent myIntent = new Intent(IntroActivity.this, Check_Video.class);
                                myIntent.putExtra("image_url", global_class.Image_Url_FB.get(0));
                                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(myIntent);
                                finish();


                                progressDialog.dismiss();

                            }else {

                                // fetch next Selected_Gallery_Images ........
                               // new HttpAsyncTask_2().execute(next);
                            }
                        }else {
                          //  mProgress.dismiss();
                            Toast.makeText(IntroActivity.this,"You are underage to use this app.",Toast.LENGTH_LONG).show();
                        }*/

                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    private class HttpAsyncTask_2 extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {

            progressDialog.dismiss();

            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            //   Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();

            Log.d("TAG", "result: "+result.toString());


            try {

                ImageData imageData = new ImageData();


                JSONObject obj_full = new JSONObject(result);

                Log.d("TAG", "obj_full: "+obj_full.toString());

                JSONArray data = obj_full.getJSONArray("data");


                for (int a = 0; a < data.length(); a++){
                    JSONObject obj_data = data.getJSONObject(a);

                    String image_url = obj_data.optString("source");
                    Log.d("TAG", "picurl: " + image_url);


                    profile_pic_dir = obj_data.optString("id");
                    Log.d("TAG", "json profile_dir: " + profile_pic_dir);

                    global_class.Image_Url_FB.add(image_url);



                    imageData.setUrl(image_url);
                    insert_Fb_Data(imageData);

                }


                Intent myIntent = new Intent(IntroActivity.this, Check_Video.class);
                myIntent.putExtra("image_url", global_class.Image_Url_FB.get(0));
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(myIntent);
                finish();





                //JSONObject obj_paging = obj_full.optJSONObject("paging");
                //String next = obj_paging.optString("next");

                        /*if (next == null || next.equals(null)){

                            Intent myIntent = new Intent(IntroActivity.this,Check_Video.class);
                            startActivity (myIntent);
                            finish();

                        }else {

                            // fetch next Selected_Gallery_Images ........


                        }*/

             //   mProgress.dismiss();

              //  ll_progressbar.setVisibility(View.GONE);


                progressDialog.dismiss();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;

    }

    /////////////////////////////////////////////////////

    public void deleteAll_TABLE_FB() {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_FB , null, null);
        db.close();

        Log.d("TAG", "deleteAll_TABLE_FB");
    }


    ///// Database entry .................

    public void insert_Fb_Data(ImageData data) {

        SQLiteDatabase dbase = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.IMG_URL_FB, data.getUrl());

        dbase.insert(DatabaseHelper.TABLE_FB, null, values);
        dbase.close();

        Log.d("TAG", "TABLE_FB inserted value");


    }


    ////////////////////////////////////////////////////////////////////////

    // convert inputstream to String
    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

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


///////////////////////////////////////////////////////fb pics ends//////////////////////////////////////////////////////

    public  void autoslide(){

        handler_autoslide = new Handler();

        update = new Runnable() {
            public void run() {
                if (mPager.getCurrentItem() ==0) {
                    mPager.setCurrentItem(1, true);
                }
            }
        };


        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler_autoslide.post(update);
            }
        }, 4000);

    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////...........  Instagram  ........../////////////////////////////////////////////////////////////

    private void connectOrDisconnectUser() {
        if (mApp.hasAccessToken()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    IntroActivity.this);
            builder.setMessage("Disconnect from Instagram?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    mApp.resetAccessToken();

                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.cancel();
                                }
                            });
            final AlertDialog alert = builder.create();
            alert.show();
        } else {
            mApp.authorize();

        }
    }


    ////////////////////////////////////////////////////////////////
    /////////// ..............Permissions .........


    public boolean checkForPermission(final String[] permissions, final int permRequestCode) {

        final List<String> permissionsNeeded = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            final String perm = permissions[i];
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(IntroActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {

                    Log.d("permisssion", "not granted");
                    if (shouldShowRequestPermissionRationale(permissions[i])) {

                        Log.d("if", "if");
                        permissionsNeeded.add(perm);

                    } else {
                        // add the request.
                        Log.d("else", "else");
                        permissionsNeeded.add(perm);
                    }

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


            return true;
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginManager.getInstance().logOut();
    }


    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Permission");
        builder.setMessage("To Use Location services turn on your GPS");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
                System.exit(0);
            }
        });

        AlertDialog alert = builder.create();
        alert.show();


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
        }, 3000);
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


}