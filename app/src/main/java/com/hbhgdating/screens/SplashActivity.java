package com.hbhgdating.screens;

import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

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
import com.hbhgdating.R;
import com.hbhgdating.utils.Global_Class;
import com.hbhgdating.utils.SharedPref;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.splunk.mint.Mint;

import java.util.List;
import java.util.Locale;

import me.leolin.shortcutbadger.ShortcutBadger;


/**
 * Created by Developer on 3/31/17.
 */

public class SplashActivity extends AppCompatActivity{

    SharedPref sharedPref;
    Global_Class global_class;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Locale locale;


    protected GoogleApiClient mGoogleApiClient;
    protected LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    protected Location mCurrentLocation;
    protected Boolean mRequestingLocationUpdates;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    boolean buildstatus;


    DisplayImageOptions defaultOptions;
    ImageLoaderConfiguration config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash);


        sharedPref = new SharedPref(this);
        global_class = (Global_Class)getApplicationContext();


        TelephonyManager teleMgr = (TelephonyManager) getSystemService(this.TELEPHONY_SERVICE);
        String localeCountry = teleMgr.getNetworkCountryIso();

        Log.d("TAG", "User is from " +  localeCountry);
        if (localeCountry != null) {
            Locale loc = new Locale("",localeCountry);
            loc.getDisplayCountry();
           // loc.getDisplayName();
            String country = loc.getDisplayCountry();

           // global_class.setLocation_gps(country);
            sharedPref.Set_LOCATION_GPS_Info(country);
            sharedPref.Set_LOCATION_GPS_to_Global();



            Log.d("TAG", "User is from " +  loc.getDisplayCountry());


        }



        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {


                Intent i = new Intent(SplashActivity.this, IntroActivity.class);
                i.putExtra("value", "no");

                startActivity(i);
                finish();
            }
        };

        handler.postDelayed(r, 2000);


        ShortcutBadger.removeCount(getApplicationContext());


        //Log.d(All_Constants_Urls.TAG, "Model name - "+getDeviceName());

        Mint.disableNetworkMonitoring();
        Mint.initAndStartSession(this.getApplication(), "b7a32065");
    }



    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        //mGoogleApiClient.disconnect();
        super.onStop();
    }


    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        List<Address> addresses;
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");


                ////////only city and country///////////////


                strReturnedAddress.append(returnedAddress.getLocality()).append(", ");
                strReturnedAddress.append(returnedAddress.getCountryName());

                strAdd = strReturnedAddress.toString();
                Log.d("TAG", "full address" + strReturnedAddress.toString());

            } else {
                Log.w("TAG", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("TAG", "Cannot get Address!");
        }
        return strAdd;
    }




    public void enableloc()
    {
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
                            status.startResolutionForResult(SplashActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                }
            }
        });
    }





    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

}


