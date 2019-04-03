package com.hbhgdating.screens;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.hbhgdating.DatabaseLocal.DatabaseHelper;
import com.hbhgdating.DatabaseLocal.ImageData;
import com.hbhgdating.R;
import com.hbhgdating.insta.Constant_C;
import com.hbhgdating.insta.InstagramApp_2;
import com.hbhgdating.utils.Global_Class;
import com.hbhgdating.utils.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by Developer on 6/28/17.
 */

public class Facebook_Insta_Login extends AppCompatActivity {


    public CallbackManager callbackManager;
    RelativeLayout rel_fb, rel_insta;
    LoginButton login_button;

    AccessTokenTracker accessTokenTracker;
    String FB_token, FB_user_id;
    boolean checking = true;
    String profile_pic_dir;

    SharedPref sharedPref;
    private DatabaseHelper dbhelper;

    Global_Class global_class;

    InstagramApp_2 mApp_2;;

    String TAG = "TAG";

    Dialog progressDialog;

  //  RelativeLayout ll_progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.facebook_insta_login);

        sharedPref = new SharedPref(this);
        dbhelper = new DatabaseHelper(this);
        global_class = (Global_Class) getApplicationContext();


        rel_fb = (RelativeLayout) findViewById(R.id.rel_fb);
        rel_insta = (RelativeLayout) findViewById(R.id.rel_insta);
        login_button = (LoginButton) findViewById(R.id.login_button2);

        progressDialog = new Dialog(this, android.R.style.Theme_Translucent);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setContentView(R.layout.progressbar_add_filter_video);
        progressDialog.setCancelable(false);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            String key = bundle.getString("key");

            if (key.equals("fb")){

                rel_fb.setVisibility(View.VISIBLE);
                rel_insta.setVisibility(View.GONE);

            }else {

                rel_fb.setVisibility(View.GONE);
                rel_insta.setVisibility(View.VISIBLE);

            }


        }



        ///////////// FB login ....
        login_button.setReadPermissions(Arrays.asList("email", "user_photos", "user_location", "user_birthday", "user_likes"));

        rel_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                login_button.performClick();
                login_button.setPressed(true);
                login_button.invalidate();
                login_button.registerCallback(callbackManager, mCallBack);
                login_button.setPressed(false);
                login_button.invalidate();



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



                    FB_token = AccessToken.getCurrentAccessToken().getToken().toString();
                    Log.d("TAG", "token 1: " + FB_token +
                            "Application id :" + AccessToken.getCurrentAccessToken().getApplicationId() +
                            "User id: " + AccessToken.getCurrentAccessToken().getUserId());
                    FB_user_id = AccessToken.getCurrentAccessToken().getUserId();
                   // Log.d("TAG", "user_id: "+FB_user_id);



                    GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                        @Override
                        public void onCompleted(JSONObject object, GraphResponse response) {
                          //  Log.d("TAG", "response: >>> "+response);
                          //  Log.d("TAG", "Json: >>> "+object);

                            try {


                                String str_id = object.optString("id");
                                String name = object.optString("name");
                                String email = object.optString("email");
                                String gender = object.optString("gender");
                                String birthday = object.optString("birthday");


                               /* Log.d("TAG", "str_id >> " + str_id);
                                Log.d("TAG", "name >> " + name);
                                Log.d("TAG", "email >> " + email);
                                Log.d("TAG", "gender >> " + gender);*/


                                JSONObject obj_albums = object.getJSONObject("albums");

                                JSONArray arr_data = obj_albums.getJSONArray("data");
                               // Log.d(TAG, "arr_data = "+arr_data.length());




                                sharedPref.set_LOGIN_FB(true);



                                global_class.FB_profile_dir = "";
                                for (int i = 0; i <arr_data.length(); i++){
                                    JSONObject obj = arr_data.getJSONObject(i);

                                    if (obj.optString("name").equals("Profile Pictures")){

                                        global_class.FB_profile_dir = obj.optString("id");

                                       /// Log.d(TAG, "FB_profile_dir = "+global_class.FB_profile_dir);

                                    }

                                }


                            } catch (Exception e) {
                                e.printStackTrace();

                            }



                            if (checking){

                                deleteAll_TABLE_FB();

                                get_all_pic();
                                checking = false;

                            }

                        }

                    });

                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "albums,id,name,email,birthday,gender,location");
                    request.setParameters(parameters);
                    request.executeAsync();


                }
            }
        };

        ///////////////////////////////

        //////////////// Insta login ......
        mApp_2 = new InstagramApp_2(Facebook_Insta_Login.this, Constant_C.CLIENT_ID,
                Constant_C.CLIENT_SECRET, Constant_C.CALLBACK_URL);
        mApp_2.setListener(new InstagramApp_2.OAuthAuthenticationListener() {
            @Override
            public void onSuccess() {

            }
            @Override
            public void onFail(String error) {
                Toast.makeText(Facebook_Insta_Login.this, error, Toast.LENGTH_SHORT).show();
            }
        });




        rel_insta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                connectOrDisconnectUser();

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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


                            } catch (Exception e) {
                                e.printStackTrace();

                            }

                        }

                    });

        }
        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

          /*  Intent myIntent = new Intent(IntroActivity.this,MainActivity.class);
            startActivity (myIntent);
            finish(); //if you want to do not use this*/
    }


    public void get_all_pic(){


        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                       // Log.d("TAG", "response get_all_pic: "+response);
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

        FB_token = AccessToken.getCurrentAccessToken().getToken().toString();
        FB_user_id = AccessToken.getCurrentAccessToken().getUserId();

       // Log.d("TAG", "user_id getprofile: "+FB_user_id);
      //  Log.d("TAG", "token getprofile: "+FB_token);

        String url = "https://graph.facebook.com/"+FB_user_id+
                "/?fields=albums.fields(photos.fields(source))&limit=7&access_token="+FB_token;

     //   Log.d("TAG", "get_all_pic: "+url);



        ////// Get Image from FB url

        new HttpAsyncTask_1().execute(url);



    }


    private class HttpAsyncTask_1 extends AsyncTask<String, Void, String> {

      //  ProgressDialog mProgress;

        @Override
        protected void onPreExecute() {

           // ll_progressbar.setVisibility(View.VISIBLE);

            progressDialog.show();

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

          //  Log.d("TAG", "result: "+result.toString());


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
                        //    Log.d("TAG", "picurl: " + image_url);


                            profile_pic_dir = profile_pic_dir_obj.optString("id");
                        //    Log.d("TAG", "json profile_dir: " + profile_pic_dir);

                            global_class.Image_Url_FB.add(image_url);

                            imageData.setUrl(image_url);
                            insert_Fb_Data(imageData);

                        }

                        JSONObject obj_paging = obj_photos.optJSONObject("paging");
                        String next = obj_paging.optString("next");

                     //   Log.d("TAG", "next = " + next);
                            if (next == null || next.equals(null) || next.equals("")){

                                LoginManager.getInstance().logOut();

                                progressDialog.dismiss();

                              //  ll_progressbar.setVisibility(View.GONE);

                                finish();

                            }else {

                                // fetch next Selected_Gallery_Images ........
                                new HttpAsyncTask_2().execute(next);
                            }

                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    private class HttpAsyncTask_2 extends AsyncTask<String, Void, String> {

      //  ProgressDialog mProgress;

        @Override
        protected void onPreExecute() {

            //ll_progressbar.setVisibility(View.VISIBLE);

            progressDialog.show();

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

          //  Log.d("TAG", "result: "+result.toString());


            try {

                ImageData imageData = new ImageData();


                JSONObject obj_full = new JSONObject(result);

             //   Log.d("TAG", "obj_full: "+obj_full.toString());

                JSONArray data = obj_full.getJSONArray("data");


                for (int a = 0; a < data.length(); a++){
                    JSONObject obj_data = data.getJSONObject(a);

                    String image_url = obj_data.optString("source");
                //    Log.d("TAG", "picurl: " + image_url);


                    profile_pic_dir = obj_data.optString("id");
                 //   Log.d("TAG", "json profile_dir: " + profile_pic_dir);

                    global_class.Image_Url_FB.add(image_url);



                    imageData.setUrl(image_url);
                    insert_Fb_Data(imageData);

                }




                LoginManager.getInstance().logOut();

                progressDialog.dismiss();

              //  ll_progressbar.setVisibility(View.GONE);

                finish();




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

    // convert inputstream to String
    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


    /////////////////////
    ////////////////////


    public void deleteAll_TABLE_FB() {
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_FB , null, null);
        db.close();

      //  Log.d("TAG", "deleteAll_TABLE_FB");
    }


    public void insert_Fb_Data(ImageData data) {

        SQLiteDatabase dbase = dbhelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.IMG_URL_FB, data.getUrl());

        dbase.insert(DatabaseHelper.TABLE_FB, null, values);
        dbase.close();

      //  Log.d("TAG", "TABLE_FB inserted value");


    }

    @Override
    protected void onResume() {
        dbhelper.close();

        super.onResume();
    }


    //////////////////////
    ////////////////////////



    private void connectOrDisconnectUser() {
        if (mApp_2.hasAccessToken()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    Facebook_Insta_Login.this);
            builder.setMessage("Disconnect from Instagram?")
                    .setCancelable(false)
                    .setPositiveButton("Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    mApp_2.resetAccessToken();

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
            mApp_2.authorize();
        }
    }




}
