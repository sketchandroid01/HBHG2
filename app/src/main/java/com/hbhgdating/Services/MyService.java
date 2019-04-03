package com.hbhgdating.Services;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.hbhgdating.DatabaseLocal.DatabaseHelper;
import com.hbhgdating.screens.NotificationActivity;
import com.hbhgdating.utils.All_Constants_Urls;
import com.hbhgdating.utils.Common;
import com.hbhgdating.utils.Global_Class;
import com.hbhgdating.utils.SharedPref;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Developer on 9/14/17.
 */

public class MyService extends Service {



    private static final String TAG = "Service Protocol";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000 * 10;
    private static final float LOCATION_DISTANCE = 0.1f;
    double lati,longi;
    String loc_name;
    SharedPref sharedPref;
    Global_Class global_class;
    DatabaseHelper databaseHelper;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.i(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.i(TAG, "onLocationChanged: " + location);
           // Toast.makeText(getApplicationContext(), ""+location.getLatitude()+"==>"+location.getLongitude(), Toast.LENGTH_SHORT).show();

            lati = location.getLatitude();
            longi = location.getLongitude();

            global_class.LATITUDE = String.valueOf(lati);
            global_class.LONGITUDE = String.valueOf(longi);



            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            try {
                List<Address> addressList = geocoder.getFromLocation(lati, longi, 1);
                if (addressList != null && addressList.size() > 0) {
                    {
                        loc_name = addressList.get(0).getAddressLine(1);

                    }

                }


                Log.i(TAG, "City : " + addressList.get(0).getLocality());
                Log.i(TAG, "Country : " + addressList.get(0).getCountryName());

                global_class.CITY = addressList.get(0).getLocality();
                global_class.COUNTRY = addressList.get(0).getCountryName();


                if (!sharedPref.isLatLngUpload()){
                    Update_LATLNG();
                }


            } catch (IOException e) {
                e.printStackTrace();

                mLastLocation.set(location);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.i(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.i(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.i(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(MyService.this, "Service Started", Toast.LENGTH_SHORT).show();

        Log.i("Service Started", "Started");
        return START_STICKY;

    }

    @Override
    public void onCreate() {

        global_class = (Global_Class)getApplicationContext();
        sharedPref = new SharedPref(getApplicationContext());
        databaseHelper = new DatabaseHelper(getApplicationContext());

        Log.e(TAG, "onCreate");









        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
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
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listeners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.i(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        }
    }

    public void Update_LATLNG(){

        String URL = All_Constants_Urls.UPDATE_LATLNG;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());
        params.put(All_Constants_Urls.lati, global_class.LATITUDE);
        params.put(All_Constants_Urls.longi, global_class.LONGITUDE);


        final String TAG = All_Constants_Urls.TAG;

    //    Log.d(TAG ,"AsyncHttpClient URL- " + URL);
    //    Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


        int DEFAULT_TIMEOUT = 30 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        client.post(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

             //   Log.d(TAG, "onSuccess- " + response.toString());

                if (response != null) {
                    try {
                        int success = response.optInt("success");

                        if (success == 0){

                            //  Toast.makeText(getApplicationContext(), "Something error", Toast.LENGTH_LONG).show();

                        }else
                        if (success == 1){

                            sharedPref.uploadLATLNGValue(true);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.d(TAG, "onFailure- " + res);

                /*android.app.AlertDialog alert = new android.app.AlertDialog.Builder(MainActivity.this).create();
                alert.setMessage("Server Error");
                alert.show();*/
            }


        });


    }



}
