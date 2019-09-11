package com.hbhgdating.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.hbhgdating.utils.All_Constants_Urls;


/**
 * Created by ANDROID on 8/27/2016.
 */
public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

    String TAG = All_Constants_Urls.TAG;

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);


    }

}
