package com.hbhgdating.Chat;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.hbhgdating.DatabaseLocal.ChatroomData;
import com.hbhgdating.DatabaseLocal.DatabaseHelper;
import com.hbhgdating.DatabaseLocal.FeaturedChatroomData;
import com.hbhgdating.DatabaseLocal.MyMatchesData;
import com.hbhgdating.R;
import com.hbhgdating.screens.FavoriteActivity;
import com.hbhgdating.screens.MainActivity;
import com.hbhgdating.screens.NotificationActivity;
import com.hbhgdating.screens.ProfileActivity;
import com.hbhgdating.utils.All_Constants_Urls;
import com.hbhgdating.utils.Common;
import com.hbhgdating.utils.ConnectivityReceiver;
import com.hbhgdating.utils.Global_Class;
import com.hbhgdating.utils.SharedPref;
import com.hbhgdating.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Developer on 5/16/17.
 */

public class Chat_screen_new extends AppCompatActivity {

    HorizontalScrollView hsv_matches_msg,hsv_fcr_msg;
    LinearLayout matches_view,fcr_ll,tcr_ll,mcr_ll;
    LayoutInflater mInflater ;
    EditText inputSearch;
    ArrayList<HashMap<String,String>> mymatches=new ArrayList<>();
    ArrayList<HashMap<String,String>> mymatches2=new ArrayList<>();
    ArrayList<HashMap<String,String>> featured_chatrooms=new ArrayList<>();
    ArrayList<HashMap<String,String>> my_chatrooms=new ArrayList<>();
    ArrayList<HashMap<String,String>> trending_chatrooms=new ArrayList<>();
    RelativeLayout mcr_img_view;
    SharedPref sharedPref;
    Dialog progressDialog;
    boolean doubleBackToExitPressedOnce = false;
    Global_Class global_class;
    TextView fcr_view, tcr_view, mcr_view;
    String search_key;
    private DatabaseHelper databaseHelper;
    Utility utility;

    public Chat_screen_new() { super();}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.chat_tab);

        Chat_screen_new.this.registerReceiver(mMessageReceiver, new IntentFilter(Common.Key_Chatrooms));

        initViews();





        getUserBlockOrNot();

    }


    public void initViews(){


        global_class = (Global_Class)getApplicationContext();
        databaseHelper = new DatabaseHelper(this);
        sharedPref = new SharedPref(this);
        utility = new Utility(this);

        progressDialog = new Dialog(this, android.R.style.Theme_Translucent);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setContentView(R.layout.progressbar_pleasewait);
        progressDialog.setCancelable(false);



        hsv_matches_msg= (HorizontalScrollView)findViewById(R.id.hsv_matches_msg);
        hsv_fcr_msg= (HorizontalScrollView)findViewById(R.id.hsv_fcr_msg);
        matches_view = (LinearLayout)findViewById(R.id.matches_view);
        fcr_ll = (LinearLayout)findViewById(R.id.fcr_ll);
        tcr_ll = (LinearLayout)findViewById(R.id.tcr_ll);
        mcr_ll = (LinearLayout)findViewById(R.id.mcr_ll);
        fcr_view = (TextView)findViewById(R.id.fcr_view);
        tcr_view = (TextView)findViewById(R.id.tcr_view);
        mcr_view = (TextView)findViewById(R.id.mcr_view);

        fcr_view.setVisibility(View.GONE);
        tcr_view.setVisibility(View.GONE);
        mcr_view.setVisibility(View.GONE);
        fcr_ll.setVisibility(View.GONE);
        tcr_ll.setVisibility(View.GONE);
        mcr_ll.setVisibility(View.GONE);

        mcr_img_view = (RelativeLayout) findViewById(R.id.mcr_img_view);
        mcr_img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(Chat_screen_new.this, CreateChatRoomActivity.class);
                intent.putExtra("matches",mymatches2);
                startActivity(intent);
            }
        });

        inputSearch = (EditText) findViewById(R.id.inputSearch);
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().length() <= 0){
                    populateData(sharedPref.get_My_Matches());
                    getMyMatchesFromDB();
                    getMyChatroomFromDB();
                    getFeaturedChatroomFromDB();

                }

            }
        });

        inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String strText = v.getText().toString().trim();
                    //	String strText1 = v.getText().toString().trim();
                    if (strText.isEmpty()) {
                        Common.customToast(Chat_screen_new.this, getResources().getString(
                                R.string.entertext), Common.displayType.CENTER);
                    } else {

                        search_key = strText.toLowerCase();

                        get_Data_for_Keyword(strText);

                    }
                    return true;
                }
                return false;
            }
        });



        setBottom();



    }

    public void threarStart(){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                boolean isConnected = ConnectivityReceiver.isConnected();

                if (isConnected){

                    getMatchesData();

                }else {

                    // Toast.makeText(getApplicationContext(), "Please Connect to Internet", Toast.LENGTH_LONG).show();

                    showToast("Please connect to internet");

                    threarStart();
                }

            }
        }, 1500);

    }

    private Toast toast;
    public void showToast(String toast_text){

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


    @Override
    protected void onResume() {

        if (global_class.isIf_action_on_chatroom_create()){

            getMatchesData();

            global_class.setIf_action_on_chatroom_create(false);

        }else {

            if (sharedPref.get_My_Matches() == null){

                threarStart();

            }else {

                populateData(sharedPref.get_My_Matches());

                getMyMatchesFromDB();

                getMyChatroomFromDB();

                getFeaturedChatroomFromDB();
            }

        }

        updateFCM();

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


    View matches_List(final int position){

        mInflater = LayoutInflater.from(this);
        View matches_list = mInflater.inflate(R.layout.match_one_item, null);
        RelativeLayout relMy2 = (RelativeLayout)matches_list.findViewById(R.id.relMy2);
        TextView name_tv= (TextView)matches_list.findViewById(R.id.name_tv);
        RoundedImageView imageView2= (RoundedImageView)matches_list.findViewById(R.id.imageView2);
        RelativeLayout rl_circle = (RelativeLayout) matches_list.findViewById(R.id.rl_circle);
        rl_circle.setVisibility(View.GONE);


        Picasso.with(getApplicationContext()).load(mymatches.get(position).get("profile_image")).error(R.mipmap.grid_bg_3).into( imageView2, new Callback() {
            @Override
            public void onSuccess() {
                //  Log.d("TAG", "onSuccess");
            }

            @Override
            public void onError() {
                //  Toast.makeText(mactivity, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        String full_name = mymatches.get(position).get("name");
        if(full_name.split("\\w+").length>1){
            String[] array = full_name.split(" ");
            name_tv.setText(array[0]);
        }else {
            name_tv.setText(full_name);
        }

        if (mymatches.get(position).get("is_new_msg").matches("1")){
            rl_circle.setVisibility(View.VISIBLE);
        }else {
            rl_circle.setVisibility(View.GONE);
        }

        //rl_circle.setVisibility(View.VISIBLE);

        relMy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chat_screen_new.this, Get_Matches_msg.class);
                intent.putExtra("name_user", mymatches.get(position).get("name"));
                intent.putExtra("id", mymatches.get(position).get("id"));
                startActivity(intent);
            }
            });


        return matches_list;
    }

    View fcr_List(final int position){
        mInflater = LayoutInflater.from(this);

        View fcr_list = mInflater.inflate(R.layout.fcr_single_item, null);
        LinearLayout fcr_img_view = (LinearLayout)fcr_list.findViewById(R.id.fcr_img_view);
        TextView name_tv= (TextView)fcr_list.findViewById(R.id.name_tv);
        ImageView iv_fcr = (ImageView)fcr_list.findViewById(R.id.iv_fcr);
        ImageView iv_new = (ImageView) fcr_list.findViewById(R.id.iv_new);

        String image_url = "";
        if (featured_chatrooms.get(position).get("content_type").matches("2")){
            image_url = featured_chatrooms.get(position).get("content_img");
        }else if (featured_chatrooms.get(position).get("content_type").matches("4")){
            image_url = featured_chatrooms.get(position).get("content_video_img");
        }

        Picasso.with(getApplicationContext()).load(image_url).error(R.mipmap.grid_bg_3).into( iv_fcr, new Callback() {
            @Override
            public void onSuccess() {
                //  Log.d("TAG", "onSuccess");
            }

            @Override
            public void onError() {
                //  Toast.makeText(mactivity, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        if (featured_chatrooms.get(position).get("is_new").matches("1")){
            iv_new.setVisibility(View.VISIBLE);
        }else {
            iv_new.setVisibility(View.GONE);
        }

        name_tv.setText(featured_chatrooms.get(position).get("title"));
        fcr_img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                global_class.setSelected_chatroom_id(featured_chatrooms.get(position).get("id"));
                global_class.setSelected_chatroom_name(featured_chatrooms.get(position).get("title"));

                Intent intent = new Intent(Chat_screen_new.this, Get_Chatroom_msg.class);
                intent.putExtra("chatroom_title",featured_chatrooms.get(position).get("title"));
                intent.putExtra("tags",featured_chatrooms.get(position).get("tags"));
                intent.putExtra("id", featured_chatrooms.get(position).get("id"));
                intent.putExtra("user_id", featured_chatrooms.get(position).get("user_id"));
                intent.putExtra("content_type", featured_chatrooms.get(position).get("content_type"));
                intent.putExtra("content_img", featured_chatrooms.get(position).get("content_img"));
                intent.putExtra("content_video_img", featured_chatrooms.get(position).get("content_video_img"));
                intent.putExtra("content_video", featured_chatrooms.get(position).get("content_video"));
                intent.putExtra("admin_name", featured_chatrooms.get(position).get("admin_name"));

                startActivity(intent);
            }
        });

        return fcr_list;
    }

    View tcr_List(final int position){
        mInflater = LayoutInflater.from(this);

        View tcr_list = mInflater.inflate(R.layout.tcr_single_item, null);
        LinearLayout tcr_img_view = (LinearLayout)tcr_list.findViewById(R.id.tcr_img_view);
        TextView name_tv= (TextView)tcr_list.findViewById(R.id.name_tv);
        ImageView iv_tcr = (ImageView)tcr_list.findViewById(R.id.iv_tcr);
        RelativeLayout rl_circle = (RelativeLayout)tcr_list.findViewById(R.id.rl_circle);
        rl_circle.setVisibility(View.GONE);


        String image_url = "";
        if (trending_chatrooms.get(position).get("content_type").matches("2")){
            image_url = trending_chatrooms.get(position).get("content_img");
        }else if (trending_chatrooms.get(position).get("content_type").matches("4")){
            image_url = trending_chatrooms.get(position).get("content_video_img");
        }

        Picasso.with(getApplicationContext()).load(image_url).error(R.mipmap.grid_bg_3).into( iv_tcr, new Callback() {
            @Override
            public void onSuccess() {
                //  Log.d("TAG", "onSuccess");
            }

            @Override
            public void onError() {
                //  Toast.makeText(mactivity, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });


        name_tv.setText(trending_chatrooms.get(position).get("title"));
        tcr_img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                global_class.setSelected_chatroom_id(trending_chatrooms.get(position).get("id"));
                global_class.setSelected_chatroom_name(trending_chatrooms.get(position).get("title"));


                Intent intent = new Intent(Chat_screen_new.this, Get_Chatroom_msg.class);
                intent.putExtra("chatroom_title",trending_chatrooms.get(position).get("title"));
                intent.putExtra("tags",trending_chatrooms.get(position).get("tags"));
                intent.putExtra("id", trending_chatrooms.get(position).get("id"));
                intent.putExtra("user_id", trending_chatrooms.get(position).get("user_id"));
                intent.putExtra("content_type", trending_chatrooms.get(position).get("content_type"));
                intent.putExtra("content_img", trending_chatrooms.get(position).get("content_img"));
                intent.putExtra("content_video_img", trending_chatrooms.get(position).get("content_video_img"));
                intent.putExtra("content_video", trending_chatrooms.get(position).get("content_video"));
                intent.putExtra("admin_name", trending_chatrooms.get(position).get("admin_name"));

                startActivity(intent);
            }
        });

        return tcr_list;
    }

    View mcr_List(final int position){
        mInflater = LayoutInflater.from(this);
        View mcr_list = mInflater.inflate(R.layout.mcr_single_item, null);
        LinearLayout mcr_img_view = (LinearLayout)mcr_list.findViewById(R.id.mcr_img_view);
        ImageView iv_mcr = (ImageView)mcr_list.findViewById(R.id.iv_mcr);
        TextView name_tv= (TextView)mcr_list.findViewById(R.id.name_tv);
        RelativeLayout rl_circle = (RelativeLayout)mcr_list.findViewById(R.id.rl_circle);
        rl_circle.setVisibility(View.GONE);

        String image_url = "";
        if (my_chatrooms.get(position).get("content_type").matches("2")){
            image_url = my_chatrooms.get(position).get("content_img");
        }else if (my_chatrooms.get(position).get("content_type").matches("4")){
            image_url = my_chatrooms.get(position).get("content_video_img");
        }

        Picasso.with(getApplicationContext()).load(image_url).error(R.mipmap.grid_bg_3).into( iv_mcr, new Callback() {
            @Override
            public void onSuccess() {
                //  Log.d("TAG", "onSuccess");
            }
            @Override
            public void onError() {
                //  Toast.makeText(mactivity, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });

        if (my_chatrooms.get(position).get("is_new_msg").matches("1")){
            rl_circle.setVisibility(View.VISIBLE);
        }else {
            rl_circle.setVisibility(View.GONE);
        }


        name_tv.setText(my_chatrooms.get(position).get("title"));
        mcr_img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                global_class.setSelected_chatroom_id(my_chatrooms.get(position).get("id"));
                global_class.setSelected_chatroom_name(my_chatrooms.get(position).get("title"));

                Intent intent = new Intent(Chat_screen_new.this, Get_Chatroom_msg.class);
                intent.putExtra("chatroom_title",my_chatrooms.get(position).get("title"));
                intent.putExtra("tags",my_chatrooms.get(position).get("tags"));
                intent.putExtra("id", my_chatrooms.get(position).get("id"));
                intent.putExtra("user_id", my_chatrooms.get(position).get("user_id"));
                intent.putExtra("content_type", my_chatrooms.get(position).get("content_type"));
                intent.putExtra("content_img", my_chatrooms.get(position).get("content_img"));
                intent.putExtra("content_video_img", my_chatrooms.get(position).get("content_video_img"));
                intent.putExtra("content_video", my_chatrooms.get(position).get("content_video"));
                intent.putExtra("admin_name", my_chatrooms.get(position).get("admin_name"));

                startActivity(intent);
            }
        });

        return mcr_list;
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
        RelativeLayout relFav = (RelativeLayout) this.findViewById(R.id.relFav);
        RelativeLayout relNotification = (RelativeLayout) this
                .findViewById(R.id.relNotification);


        imgView1.setImageResource(R.mipmap.profile_48);
        imgView2.setImageResource(R.mipmap.chat_48_c);
        imgView3.setImageResource(R.mipmap.sunglass_white);
        imgView4.setImageResource(R.mipmap.fav_48);
        imgView5.setImageResource(R.mipmap.notify_48);


        relProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Chat_screen_new.this,
                        ProfileActivity.class);
                startActivity(intent);
                Chat_screen_new.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        relMain.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Chat_screen_new.this,
                        MainActivity.class);
                startActivity(intent);
                Chat_screen_new.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        relFav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Chat_screen_new.this,
                        FavoriteActivity.class);
                startActivity(intent);
                Chat_screen_new.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        relNotification.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Chat_screen_new.this,
                        NotificationActivity.class);
                startActivity(intent);
                Chat_screen_new.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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


    public void getMatchesData(){

        progressDialog.show();

        String URL = All_Constants_Urls.MYMATCHES;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());


        final String TAG = All_Constants_Urls.TAG;

       // Log.d(TAG ,"AsyncHttpClient URL- " + URL);
       // Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


        client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , Common.DEFAULT_TIMEOUT);
        client.post(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

               // Log.d(TAG, "onSuccess- " + response.toString());

                if (response != null) {
                    try {

                        int success = response.optInt("success");

                        if (success == 0){

                            Toast.makeText(getApplicationContext(), "Something error", Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();


                        }else
                        if (success == 1){

                            populateData(response.toString());

                            sharedPref.save_My_Matches(response.toString());

                            databaseHelper.setJSONDataInDB(response.toString());

                            getMyMatchesFromDB();

                            getMyChatroomFromDB();

                            getFeaturedChatroomFromDB();


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

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(Chat_screen_new.this).create();
                alert.setMessage("Server Error");
                alert.show();
            }

        });

    }


    public void get_Data_for_Keyword(String keyword){

        progressDialog.show();

        String URL = All_Constants_Urls.mymatchesbychatroomkeyword;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());
        params.put("text", keyword);


        final String TAG = All_Constants_Urls.TAG;

      //  Log.d(TAG ,"AsyncHttpClient URL- " + URL);
     //   Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


        client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , Common.DEFAULT_TIMEOUT);
        client.post(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

              //  Log.d(TAG, "onSuccess- " + response.toString());

                if (response != null) {
                    try {

                        int success = response.optInt("success");

                        if (success == 0){

                            Toast.makeText(getApplicationContext(), "Something error", Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();

                        }else
                        if (success == 1){

                            populateDataForKeyword(response.toString());


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

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(Chat_screen_new.this).create();
                alert.setMessage("Server Error");
                alert.show();
            }

        });

    }



    ////// Get data from database ....

    public void getMyMatchesFromDB(){

        progressDialog.show();

        mymatches.clear();
        mymatches2.clear();
        matches_view.removeAllViews();
        ArrayList<MyMatchesData> arrayList = databaseHelper.getDataFromTABLE_MY_MATCHES();
        int length = arrayList.size();

       // Log.d(All_Constants_Urls.TAG, "L = "+length);

        int counter = 0;

        for (int i = 0; i < length; i++){

           // Log.d(All_Constants_Urls.TAG, "Time = "+arrayList.get(i).getMY_MATCHES_INCOMING_MSG_TIME());

            try {
                JSONObject object = new JSONObject(arrayList.get(i).getMY_MATCHES_DATA());

                if (object.has(All_Constants_Urls.is_blocked)) {

                    int is_blocked = object.getInt(All_Constants_Urls.is_blocked);

                   // Log.d(All_Constants_Urls.TAG, "is_blocked = "+is_blocked);

                    if (is_blocked == 0){

                        JSONObject basic_info = object.getJSONObject(All_Constants_Urls.basic_info);
                        if (basic_info != null){

                            HashMap<String, String> hashmap = new HashMap();
                            hashmap.put("id", basic_info.optString("id"));
                            hashmap.put("profile_image", basic_info.optString("profile_image"));
                            hashmap.put("name", basic_info.optString("name"));
                            hashmap.put("is_new_msg", arrayList.get(i).getCHAT_IS_NEW());

                            mymatches.add(hashmap);
                            matches_view.addView(matches_List(counter));

                            mymatches2.add(hashmap);

                            counter++;
                        }

                    }


                }else {

                    JSONObject basic_info = object.getJSONObject(All_Constants_Urls.basic_info);
                    if (basic_info != null){

                        HashMap<String, String> hashmap = new HashMap();
                        hashmap.put("id", basic_info.optString("id"));
                        hashmap.put("profile_image", basic_info.optString("profile_image"));
                        hashmap.put("name", basic_info.optString("first_name") + " " + basic_info.optString("last_name"));
                        hashmap.put("is_new_msg", arrayList.get(i).getCHAT_IS_NEW());

                        mymatches.add(hashmap);
                        matches_view.addView(matches_List(counter));

                        counter++;
                    }


                }

               // Log.d(All_Constants_Urls.TAG, "id = "+object.optString("id"));

            }catch (Exception e){
                e.printStackTrace();
            }



        }

        progressDialog.dismiss();

    }

    public void getMyChatroomFromDB(){

        my_chatrooms.clear();
        mcr_ll.removeAllViews();

        ArrayList<ChatroomData> arrayList = databaseHelper.getDataFromTABLE_MY_CHATROOM();
        int length = arrayList.size();

       // Log.d(All_Constants_Urls.TAG, "My ch L = "+length);
        int counter = 0;
        for (int i = 0; i < length; i++){

          //  Log.d(All_Constants_Urls.TAG, "Time chat = "+arrayList.get(i).getCHATROOM_INCOMING_MSG_TIME());

            try {
                JSONObject object = new JSONObject(arrayList.get(i).getCHATROOM_DATA());

                if (object.optString("status").matches("1")){

                    HashMap<String, String> hashmap = new HashMap();
                    hashmap.put("id", object.optString("id"));
                    hashmap.put("user_id", object.optString("user_id"));
                    hashmap.put("title", object.optString("title"));
                    hashmap.put("tags", object.optString("tags"));
                    hashmap.put("content", object.optString("content"));
                    hashmap.put("content_type", object.optString("content_type"));
                    hashmap.put("content_img", object.optString("content_img"));
                    hashmap.put("content_video", object.optString("content_video"));
                    hashmap.put("content_video_img", object.optString("content_video_img"));
                    hashmap.put("invited_users", object.optString("invited_users"));
                    hashmap.put("create_date", object.optString("create_date"));
                    hashmap.put("status", object.optString("status"));
                    hashmap.put("count_chats", object.optString("count_chats"));
                    hashmap.put("admin_name", object.optString("admin_name"));

                    hashmap.put("is_new_msg", arrayList.get(i).getCHATROOM_CHAT_IS_NEW());


                    my_chatrooms.add(hashmap);
                    mcr_ll.addView(mcr_List(counter));

                    counter++;
                }

            }catch (Exception e){
                e.printStackTrace();
            }


        }


        if (my_chatrooms.size() != 0){
            mcr_ll.setVisibility(View.VISIBLE);
            mcr_view.setVisibility(View.VISIBLE);
        }else {
            mcr_ll.setVisibility(View.GONE);
            mcr_view.setVisibility(View.GONE);
        }



    }

    public void getFeaturedChatroomFromDB(){

        featured_chatrooms.clear();
        fcr_ll.removeAllViews();

        ArrayList<FeaturedChatroomData> arrayList = databaseHelper.getDataFromTABLE_FEATURED_CHATROOM();
        int length = arrayList.size();

      //  Log.d(All_Constants_Urls.TAG, "Featured L = "+length);
        int counter = 0;
        for (int i = 0; i < length; i++){

            try {
                JSONObject object = new JSONObject(arrayList.get(i).getF_CHATROOM_DATA());


                if (object.optString("status").matches("1")){

                    HashMap<String, String> hashmap = new HashMap();
                    hashmap.put("id", object.optString("id"));
                    hashmap.put("user_id", object.optString("user_id"));
                    hashmap.put("title", object.optString("title"));
                    hashmap.put("tags", object.optString("tags"));
                    hashmap.put("content", object.optString("content"));
                    hashmap.put("content_type", object.optString("content_type"));
                    hashmap.put("content_img", object.optString("content_img"));
                    hashmap.put("content_video", object.optString("content_video"));
                    hashmap.put("content_video_img", object.optString("content_video_img"));
                    hashmap.put("invited_users", object.optString("invited_users"));
                    hashmap.put("create_date", object.optString("create_date"));
                    hashmap.put("status", object.optString("status"));
                    hashmap.put("count_chats", object.optString("count_chats"));
                    hashmap.put("admin_name", object.optString("admin_name"));

                    hashmap.put("is_new", arrayList.get(i).getF_CHATROOM_IS_NEW());

                    Log.d(All_Constants_Urls.TAG, "is_new_msg = "+arrayList.get(i).getF_CHATROOM_IS_NEW());

                    featured_chatrooms.add(hashmap);
                    fcr_ll.addView(fcr_List(counter));


                    counter++;
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }


        if (featured_chatrooms.size() != 0){
            fcr_ll.setVisibility(View.VISIBLE);
            fcr_view.setVisibility(View.VISIBLE);
        }else {
            fcr_ll.setVisibility(View.GONE);
            fcr_view.setVisibility(View.GONE);
        }



    }






    public void populateData(String json){

        tcr_view.setVisibility(View.GONE);

        tcr_ll.setVisibility(View.GONE);


        trending_chatrooms.clear();


        tcr_ll.removeAllViews();
        fcr_ll.removeAllViews();


        final String TAG = All_Constants_Urls.TAG;

        try{

            JSONObject response = new JSONObject(json);


            JSONArray trending_chatrooms_array = response.getJSONArray(All_Constants_Urls.trending_chatrooms);
            for (int i = 0; i < trending_chatrooms_array.length(); i++){
                JSONObject object = trending_chatrooms_array.getJSONObject(i);

                HashMap<String, String> hashmap = new HashMap();
                hashmap.put("id", object.optString("id"));
                hashmap.put("user_id", object.optString("user_id"));
                hashmap.put("title", object.optString("title"));
                hashmap.put("tags", object.optString("tags"));
                hashmap.put("content", object.optString("content"));
                hashmap.put("content_type", object.optString("content_type"));
                hashmap.put("content_img", object.optString("content_img"));
                hashmap.put("content_video", object.optString("content_video"));
                hashmap.put("content_video_img", object.optString("content_video_img"));
                hashmap.put("invited_users", object.optString("invited_users"));
                hashmap.put("create_date", object.optString("create_date"));
                hashmap.put("status", object.optString("status"));
                hashmap.put("count_chats", object.optString("count_chats"));
                hashmap.put("admin_name", object.optString("admin_name"));

                trending_chatrooms.add(hashmap);
                tcr_ll.addView(tcr_List(i));
            }

            /*JSONArray featured_chatrooms_array = response.getJSONArray(All_Constants_Urls.featured_chatrooms);
            for (int i = 0; i < featured_chatrooms_array.length(); i++){
                JSONObject object = featured_chatrooms_array.getJSONObject(i);

                HashMap<String, String> hashmap = new HashMap();
                hashmap.put("id", object.optString("id"));
                hashmap.put("user_id", object.optString("user_id"));
                hashmap.put("title", object.optString("title"));
                hashmap.put("tags", object.optString("tags"));
                hashmap.put("content", object.optString("content"));
                hashmap.put("content_type", object.optString("content_type"));
                hashmap.put("content_img", object.optString("content_img"));
                hashmap.put("content_video", object.optString("content_video"));
                hashmap.put("content_video_img", object.optString("content_video_img"));
                hashmap.put("invited_users", object.optString("invited_users"));
                hashmap.put("create_date", object.optString("create_date"));
                hashmap.put("status", object.optString("status"));
                hashmap.put("count_chats", object.optString("count_chats"));
                hashmap.put("admin_name", object.optString("admin_name"));

                featured_chatrooms.add(hashmap);
                fcr_ll.addView(fcr_List(i));
            }*/


            /*if (featured_chatrooms.size() != 0){
                fcr_ll.setVisibility(View.VISIBLE);
                fcr_view.setVisibility(View.VISIBLE);
            }*/

            if (trending_chatrooms.size() != 0){
                tcr_ll.setVisibility(View.VISIBLE);
                tcr_view.setVisibility(View.VISIBLE);
            }

            /*if (my_chatrooms.size() != 0){
                mcr_ll.setVisibility(View.VISIBLE);
                mcr_view.setVisibility(View.VISIBLE);
            }*/



        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void populateDataForKeyword(String json){

        fcr_view.setVisibility(View.GONE);
        tcr_view.setVisibility(View.GONE);
        mcr_view.setVisibility(View.GONE);
        fcr_ll.setVisibility(View.GONE);
        tcr_ll.setVisibility(View.GONE);
        mcr_ll.setVisibility(View.GONE);

        mymatches.clear();
        my_chatrooms.clear();
        trending_chatrooms.clear();
        featured_chatrooms.clear();

        matches_view.removeAllViews();
        mcr_ll.removeAllViews();
        tcr_ll.removeAllViews();
        fcr_ll.removeAllViews();


        final String TAG = All_Constants_Urls.TAG;

        try{

            JSONObject response = new JSONObject(json);

            int counter = 0;

            JSONArray data_array = response.getJSONArray(All_Constants_Urls.data);
            //Log.d(TAG, "length - " +data_array.length());
            for (int i = 0; i < data_array.length(); i++){
                JSONObject object = data_array.getJSONObject(i);

                int is_blocked = object.optInt(All_Constants_Urls.is_blocked);
                if (is_blocked == 0){

                    JSONObject basic_info=object.getJSONObject("basic_info");

                    HashMap<String, String> hashmap = new HashMap();
                    hashmap.put("id", basic_info.optString("id"));
                    hashmap.put("profile_image", basic_info.optString("profile_image"));
                    hashmap.put("name", basic_info.optString("name"));

                    hashmap.put("is_new_msg", "0");

                    String name = basic_info.optString("name").toLowerCase();

                    if (name.contains(search_key)){

                        //Log.d(All_Constants_Urls.TAG, "Name = "+name);
                       // Log.d(All_Constants_Urls.TAG, "Name = "+name);

                        mymatches.add(hashmap);
                        matches_view.addView(matches_List(counter));
                        counter++;

                    }

                }

            }

            JSONArray my_chatrooms_array = response.getJSONArray(All_Constants_Urls.my_chatrooms);
            for (int i = 0; i < my_chatrooms_array.length(); i++){
                JSONObject object = my_chatrooms_array.getJSONObject(i);

                HashMap<String, String> hashmap = new HashMap();
                hashmap.put("id", object.optString("id"));
                hashmap.put("user_id", object.optString("user_id"));
                hashmap.put("title", object.optString("title"));
                hashmap.put("tags", object.optString("tags"));
                hashmap.put("content", object.optString("content"));
                hashmap.put("content_type", object.optString("content_type"));
                hashmap.put("content_img", object.optString("content_img"));
                hashmap.put("content_video", object.optString("content_video"));
                hashmap.put("content_video_img", object.optString("content_video_img"));
                hashmap.put("invited_users", object.optString("invited_users"));
                hashmap.put("create_date", object.optString("create_date"));
                hashmap.put("status", object.optString("status"));
                hashmap.put("count_chats", object.optString("count_chats"));
                hashmap.put("admin_name", object.optString("admin_name"));

                hashmap.put("is_new_msg", "0");

                my_chatrooms.add(hashmap);
                mcr_ll.addView(mcr_List(i));
            }

            JSONArray trending_chatrooms_array = response.getJSONArray(All_Constants_Urls.trending_chatrooms);
            for (int i = 0; i < trending_chatrooms_array.length(); i++){
                JSONObject object = trending_chatrooms_array.getJSONObject(i);

                HashMap<String, String> hashmap = new HashMap();
                hashmap.put("id", object.optString("id"));
                hashmap.put("user_id", object.optString("user_id"));
                hashmap.put("title", object.optString("title"));
                hashmap.put("tags", object.optString("tags"));
                hashmap.put("content", object.optString("content"));
                hashmap.put("content_type", object.optString("content_type"));
                hashmap.put("content_img", object.optString("content_img"));
                hashmap.put("content_video", object.optString("content_video"));
                hashmap.put("content_video_img", object.optString("content_video_img"));
                hashmap.put("invited_users", object.optString("invited_users"));
                hashmap.put("create_date", object.optString("create_date"));
                hashmap.put("status", object.optString("status"));
                hashmap.put("count_chats", object.optString("count_chats"));
                hashmap.put("admin_name", object.optString("admin_name"));

                trending_chatrooms.add(hashmap);
                tcr_ll.addView(tcr_List(i));
            }

            JSONArray featured_chatrooms_array = response.getJSONArray(All_Constants_Urls.featured_chatrooms);
            for (int i = 0; i < featured_chatrooms_array.length(); i++){
                JSONObject object = featured_chatrooms_array.getJSONObject(i);

                HashMap<String, String> hashmap = new HashMap();
                hashmap.put("id", object.optString("id"));
                hashmap.put("user_id", object.optString("user_id"));
                hashmap.put("title", object.optString("title"));
                hashmap.put("tags", object.optString("tags"));
                hashmap.put("content", object.optString("content"));
                hashmap.put("content_type", object.optString("content_type"));
                hashmap.put("content_img", object.optString("content_img"));
                hashmap.put("content_video", object.optString("content_video"));
                hashmap.put("content_video_img", object.optString("content_video_img"));
                hashmap.put("invited_users", object.optString("invited_users"));
                hashmap.put("create_date", object.optString("create_date"));
                hashmap.put("status", object.optString("status"));
                hashmap.put("count_chats", object.optString("count_chats"));
                hashmap.put("admin_name", object.optString("admin_name"));

                hashmap.put("is_new", "0");

                featured_chatrooms.add(hashmap);
                fcr_ll.addView(fcr_List(i));
            }


            if (featured_chatrooms.size() != 0){
                fcr_ll.setVisibility(View.VISIBLE);
                fcr_view.setVisibility(View.VISIBLE);
            }

            if (trending_chatrooms.size() != 0){
                tcr_ll.setVisibility(View.VISIBLE);
                tcr_view.setVisibility(View.VISIBLE);
            }

            if (my_chatrooms.size() != 0){
                mcr_ll.setVisibility(View.VISIBLE);
                mcr_view.setVisibility(View.VISIBLE);
            }



        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void getNotificationData(){

        String URL = All_Constants_Urls.GET_NOTIFICATION;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());


        final String TAG = All_Constants_Urls.TAG;

       // Log.d(TAG ,"AsyncHttpClient URL- " + URL);
       // Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


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


    public void updateFCM(){

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        String URL = All_Constants_Urls.UPDATE_FCM_TOKEN;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());
        params.put(All_Constants_Urls.device_token, refreshedToken);

        final String TAG = All_Constants_Urls.TAG;

       // Log.d(TAG ,"AsyncHttpClient URL- " + URL);
       // Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


        client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , Common.DEFAULT_TIMEOUT);
        client.post(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "onSuccess- " + response.toString());

                if (response != null) {
                    try {

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

      //  Log.d(TAG ,"AsyncHttpClient URL- " + URL);
      //  Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


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
            String noti_type = intent.getStringExtra("noti_type");
            //do other stuff here
            Log.d(All_Constants_Urls.TAG, "noti_type = "+noti_type);

           /* if (noti_type.matches("1")){

                getFeaturedChatroomFromDB();

            }else*/

            if (noti_type.matches("4")){

                getMyMatchesFromDB();

            }else if (noti_type.matches("5")){

                getMyChatroomFromDB();

            }else {

                getMatchesData();

            }

           // getNotificationData();
        }

    };


    public void freeMemory(){
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }


}
