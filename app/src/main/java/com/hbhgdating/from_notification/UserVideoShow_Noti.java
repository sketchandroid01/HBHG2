package com.hbhgdating.from_notification;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.cunoraz.gifview.library.GifView;
import com.danikula.videocache.HttpProxyCacheServer;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hbhgdating.chat.Chat_screen_new;
import com.hbhgdating.R;
import com.hbhgdating.screens.FavoriteActivity;
import com.hbhgdating.screens.NotificationActivity;
import com.hbhgdating.screens.ProfileActivity;
import com.hbhgdating.screens.SplashActivity;
import com.hbhgdating.screens.UserProfileActivity;
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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.Random;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;

public class UserVideoShow_Noti extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    ImageView imgSetting,imghbhgleft,imghbhgright,imgProfile,imgBlockUser,imgReport;
    TextView tv_user_info1, tv_user_info2, notfound_txt;
    ImageView iv_bg;
    Boolean isPanelShown=false;
    RelativeLayout relSearchSection;
    TextView txtCancel, txtSearch;
    EditText inputSearch;
    DiscreteSeekBar discrete1;
    File file;
    private SliderLayout mDemoSlider_1, mDemoSlider_2;
    private ScalableVideoView videoView1;
    MediaController mediaController;
    HttpProxyCacheServer proxy;
    int is_blocked = 0;
    GifView gifviewYes,gifviewNo;
    AlertDialog alertDialog;
    Global_Class global_class;
    boolean doubleBackToExitPressedOnce = false;
    SharedPref sharedPref;
    Dialog progressDialog;
    String User_Data;
    String user_id, from;
    private String Font_User_Id;

    RelativeLayout rel_msg_box;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        global_class = (Global_Class)getApplicationContext();
        sharedPref = new SharedPref(this);

        progressDialog = new Dialog(this, android.R.style.Theme_Translucent);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setContentView(R.layout.progressbar_pleasewait);
        progressDialog.setCanceledOnTouchOutside(false);

        setView();




        //////////
        boolean isConnected = ConnectivityReceiver.isConnected();

        if (isConnected){

            getUserData();

        }else {

            ShowToast("Please connect to internet");

            Thread_Start();
        }

    }


    public void Thread_Start(){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

               boolean isConnected = ConnectivityReceiver.isConnected();

                if (isConnected){

                    getUserData();

                }else {

                    // Toast.makeText(getApplicationContext(), "Please Connect to Internet", Toast.LENGTH_LONG).show();

                    ShowToast("Please connect to internet");

                    Thread_Start();
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



    public  void setView(){
        setBottom();
        imgSetting = (ImageView) findViewById(R.id.imgSetting);
        imgSetting.setVisibility(View.INVISIBLE);
        imgProfile = (ImageView) findViewById(R.id.imgProfile);
        relSearchSection = (RelativeLayout) findViewById(R.id.relSearchSection);
        relSearchSection.setVisibility(View.INVISIBLE);
        txtCancel = (TextView) findViewById(R.id.txtCancel);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        gifviewYes = (GifView) findViewById(R.id.gifviewYes);
        gifviewNo = (GifView) findViewById(R.id.gifviewNo);
        tv_user_info1 = (TextView) findViewById(R.id.tv_user_info1);
        tv_user_info2 = (TextView) findViewById(R.id.tv_user_info2);
        notfound_txt = (TextView) findViewById(R.id.notfound_txt);
        rel_msg_box = findViewById(R.id.rel_msg_box);
        notfound_txt.setVisibility(View.GONE);
        rel_msg_box.setVisibility(View.GONE);
        tv_user_info1.setVisibility(View.GONE);
        tv_user_info2.setVisibility(View.GONE);


        imghbhgleft = (ImageView) findViewById(R.id.imghbhgleft);
        imghbhgright = (ImageView) findViewById(R.id.imghbhgright);
        imghbhgleft.setVisibility(View.INVISIBLE);
        imghbhgright.setVisibility(View.INVISIBLE);

        imgBlockUser = (ImageView) findViewById(R.id.imgBlockUser);
        imgReport = (ImageView) findViewById(R.id.imgReport);


        mDemoSlider_1 = (SliderLayout)findViewById(R.id.slider1);
        mDemoSlider_2 = (SliderLayout)findViewById(R.id.slider2);
        mDemoSlider_1.setVisibility(View.GONE);
        mDemoSlider_2.setVisibility(View.GONE);

        videoView1 = (ScalableVideoView) findViewById(R.id.videoView1);
        mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView1);


        imgReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openReportDialogBox();
            }
        });

        imgBlockUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlockDialogBox();
            }
        });


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(UserVideoShow_Noti.this, UserProfileActivity.class);
                intent1.putExtra("userid", Font_User_Id);
                intent1.putExtra("data", User_Data);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay);

            }
        });


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            user_id = bundle.getString("id");
            from = bundle.getString("from", "");
        }






        gifThread();

    }


    public void gifThread(){

        Runnable newthread = new Runnable(){

            @Override
            public void run() {

                imghbhgleft.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        gifviewYes.setVisibility(View.GONE);
                        gifviewNo.play();
                        gifviewNo.setVisibility(View.VISIBLE);


                        imghbhgleft.setEnabled(false);
                        imghbhgright.setEnabled(false);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                gifviewNo.setVisibility(View.GONE);
                                if (gifviewNo.isPlaying())
                                {
                                    gifviewNo.pause();
                                }


                                imghbhgleft.setEnabled(true);
                                imghbhgright.setEnabled(true);


                            }
                        }, Common.gifNo);


                        likeUnlikeProfile(0);

                    }


                });
                imghbhgright.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                      //  gifviewYes.setGifResource(R.drawable.hey);
                        gifviewYes.play();
                        gifviewYes.setVisibility(View.VISIBLE);


                        gifviewNo.setVisibility(View.GONE);
                        imghbhgleft.setEnabled(false);
                        imghbhgright.setEnabled(false);
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                gifviewYes.setVisibility(View.GONE);
                                if (gifviewYes.isPlaying())
                                {
                                    gifviewYes.pause();
                                }
                                imghbhgleft.setEnabled(true);
                                imghbhgright.setEnabled(true);


                            }
                        }, Common.gifYes);


                        likeUnlikeProfile(1);

                    }

                });

            }

        };
        Thread t = new Thread(newthread);
        t.start();


    }


    private void BlockDialogBox() {
        String strMessage = "";
        if (is_blocked == 0) {
            strMessage = "Are you sure you want to block this user?";
        } else {
            strMessage = "Are you sure you want to unblock this user?";
        }
        new MaterialDialog.Builder(this)
                .title(R.string.app_name)
                .content(strMessage)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancelcaps)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        try {

                            blockUnblockUser();

                            dialog.dismiss();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        // TODO
                        dialog.dismiss();
                    }
                }).show();
    }

    @SuppressLint("InflateParams")
    private void openReportDialogBox() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Report User :");
        // ...Irrelevant code_uri_filter for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.report_user_new, null);
        dialogBuilder.setView(dialogView);

        final EditText et_description = (EditText)dialogView.findViewById(R.id.et_description);


        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (et_description.getText().toString().trim().length() == 0){
                    Toast.makeText(getApplicationContext(), "Please write some description", Toast.LENGTH_LONG).show();

                }else {

                    reportUser(et_description.getText().toString());
                }

            }
        });

        dialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });


        alertDialog = dialogBuilder.create();
        alertDialog.show();


    }


    private void setBottom() {
        ImageView imgView1 = (ImageView) this.findViewById(R.id.imgView1);
        ImageView imgView2 = (ImageView) this.findViewById(R.id.imgView2);
        ImageView imgView3 = (ImageView) this.findViewById(R.id.imgView3);
        ImageView imgView4 = (ImageView) this.findViewById(R.id.imgView4);
        ImageView imgView5 = (ImageView) this.findViewById(R.id.imgView5);

        LinearLayout line = (LinearLayout)this.findViewById(R.id.line);
        line.setVisibility(View.INVISIBLE);


     /*   RelativeLayout relBottomSection = (RelativeLayout) this
                .findViewById(R.id.relBottomSection);
        relBottomSection.bringToFront();
        relBottomSection.invalidate();*/


        RelativeLayout relProfile = (RelativeLayout) this.findViewById(R.id.relProfile);
        RelativeLayout relChat = (RelativeLayout) this.findViewById(R.id.relChat);
        RelativeLayout relFav = (RelativeLayout) this.findViewById(R.id.relFav);
        RelativeLayout relNotification = (RelativeLayout) this.findViewById(R.id.relNotification);



        imgView1.setImageResource(R.mipmap.profile_48);
        imgView2.setImageResource(R.mipmap.chat_48);
        imgView3.setImageResource(R.mipmap.sunglass);
        imgView4.setImageResource(R.mipmap.fav_48);
        imgView5.setImageResource(R.mipmap.notify_48);

        relProfile.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(UserVideoShow_Noti.this,
                        ProfileActivity.class);
                startActivity(intent);
                UserVideoShow_Noti.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        relChat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(UserVideoShow_Noti.this, Chat_screen_new.class);
                startActivity(intent);
                UserVideoShow_Noti.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        relFav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(UserVideoShow_Noti.this, FavoriteActivity.class);
                startActivity(intent);
                UserVideoShow_Noti.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        relNotification.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(UserVideoShow_Noti.this, NotificationActivity.class);
                startActivity(intent);
                UserVideoShow_Noti.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
    }

    public void slideUpDown() {
        if (!isPanelShown) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_up);

            relSearchSection.startAnimation(bottomUp);
            relSearchSection.setVisibility(View.VISIBLE);
            isPanelShown = true;
        } else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_down);

            relSearchSection.startAnimation(bottomDown);
            relSearchSection.setVisibility(View.INVISIBLE);
            isPanelShown = false;
        }
    }


    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider_1.stopAutoCycle();
        mDemoSlider_2.stopAutoCycle();
        super.onStop();
    }

    @Override
    protected void onPause() {
        mDemoSlider_1.stopAutoCycle();
        mDemoSlider_2.stopAutoCycle();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mDemoSlider_1.startAutoCycle();
        mDemoSlider_2.startAutoCycle();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {

        String trans[]={"","ZoomOut","ZoomOutSlide","Tablet","DepthPage","ZoomOut","Fade","RotateDown"};
        Random rand = new Random();
        int randomNum = rand.nextInt((7 - 1) + 1) + 1;
        mDemoSlider_1.setPresetTransformer(trans[randomNum]);
        mDemoSlider_2.setPresetTransformer(trans[randomNum]);
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
        super.onBackPressed();

        if (from.matches("noti_tab")){

            finish();

        }else if (from.matches("chatroom")){

            finish();

        } else{

            Intent intent = new Intent(UserVideoShow_Noti.this, SplashActivity.class);
            startActivity(intent);
            finish();
        }


    }


    ////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////
    /* *********************************************************
     * *********************************************************
     * *********************************************************
     */

    //   API  Functions ................


    public void getUserData(){

        progressDialog.show();

        String URL = All_Constants_Urls.GET_PROFILE_DATA;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, user_id);


        final String TAG = All_Constants_Urls.TAG;

      //  Log.d(TAG ,"AsyncHttpClient URL- " + URL);
     //   Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


        client.setSSLSocketFactory(
                new SSLSocketFactory(Common.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));

        client.post(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
              //  Log.d(TAG, "onSuccess- " + response.toString());
                if (response != null) {
                    try {


                        int success = response.optInt("success");

                        if (success == 0){
                            notfound_txt.setVisibility(View.VISIBLE);

                            progressDialog.dismiss();

                        }else if (success == 1){

                            User_Data = response.toString();

                            showProfileVideos(User_Data);

                            mDemoSlider_1.setVisibility(View.VISIBLE);
                            tv_user_info1.setVisibility(View.VISIBLE);

                            imgProfile.setVisibility(View.VISIBLE);
                            imgBlockUser.setVisibility(View.VISIBLE);
                            imgReport.setVisibility(View.VISIBLE);


                            progressDialog.dismiss();


                            updateFCM();

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

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(UserVideoShow_Noti.this).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });


    }


    public void showProfileVideos(String json){

        //Log.d(All_Constants_Urls.TAG, "Clicked data = "+json);

        try{

            JSONObject object = new JSONObject(json);

            is_blocked = object.optInt(All_Constants_Urls.is_blocked);

            JSONObject basic_info = object.getJSONObject(All_Constants_Urls.basic_info);

            ////////////

            String name = basic_info.getString(All_Constants_Urls.name);
            String age = basic_info.getString(All_Constants_Urls.age);
            String strGender = basic_info.getString(All_Constants_Urls.gender);
            String strSexual = basic_info.getString(All_Constants_Urls.sexual_orientation);
            String city = basic_info.getString(All_Constants_Urls.city);
            String country = basic_info.getString(All_Constants_Urls.country);

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

            if (temp_text.endsWith(", ")){
                temp_text.substring(0, temp_text.length() - 2);
            }


            if (strGender.equalsIgnoreCase("m")){
                imghbhgleft.setImageResource(R.mipmap.no_boy_symbol);
                imghbhgright.setImageResource(R.mipmap.hey_boy_symbol);
                imghbhgright.setVisibility(View.VISIBLE);
                imghbhgleft.setVisibility(View.VISIBLE);
            }else if (strGender.equalsIgnoreCase("f")){
                imghbhgleft.setImageResource(R.mipmap.no_girl_symbol);
                imghbhgright.setImageResource(R.mipmap.hey_girl_symbol);
                imghbhgright.setVisibility(View.VISIBLE);
                imghbhgleft.setVisibility(View.VISIBLE);
            }


            ///////////

            Font_User_Id = basic_info.getString(All_Constants_Urls.id);
            Log.d(All_Constants_Urls.TAG, "Font_User_Id = "+Font_User_Id);

            String video = basic_info.getString(All_Constants_Urls.video);
            if (!video.isEmpty()){

                tv_user_info1.setText(temp_text);

                playVideo(video);

            }else {

                videoView1.setVisibility(View.GONE);


                JSONArray image_video = basic_info.optJSONArray(All_Constants_Urls.image_video);


                tv_user_info1.setText(temp_text);

                setData_Slider1(image_video);

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void setData_Slider1(JSONArray image_video){

        try {

            if (image_video.length() != 0){
                for (int j = 0; j < image_video.length(); j++) {
                    String image_url = image_video.getString(j);

                    TextSliderView textSliderView = new TextSliderView(this);
                    // initialize a SliderLayout
                    textSliderView
                            .image(image_url)
                            .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                            .setOnSliderClickListener(this);

                    mDemoSlider_1.addSlider(textSliderView);
                }
                playSlider1();

            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }


    public void playSlider1(){

        mDemoSlider_1.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        mDemoSlider_1.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        mDemoSlider_1.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider_1.setDuration(2000);
        mDemoSlider_1.moveNextPosition(true);
        mDemoSlider_1.addOnPageChangeListener(UserVideoShow_Noti.this);
        mDemoSlider_1.startAutoCycle();

    }

    public void playSlider2(){

        mDemoSlider_2.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        mDemoSlider_2.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        mDemoSlider_2.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider_2.setDuration(2000);
        mDemoSlider_2.moveNextPosition(true);
        mDemoSlider_2.addOnPageChangeListener(UserVideoShow_Noti.this);
        mDemoSlider_2.startAutoCycle();

    }


    public void playVideo(final String videoUri){

        mDemoSlider_1.setVisibility(View.GONE);
        mDemoSlider_2.setVisibility(View.GONE);
        videoView1.setVisibility(View.VISIBLE);


        videoView1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {

                //mp.setVolume(0, 0);
            }
        });

        videoView1.setVideoURI(Uri.parse(videoUri));
        videoView1.setMediaController(null);
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
                return true;
            }
        });


    }


    public void blockUnblockUser(){


        progressDialog.show();


        String URL = All_Constants_Urls.BLOCK_USER;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());
        params.put(All_Constants_Urls.block_user_id, Font_User_Id);


        final String TAG = All_Constants_Urls.TAG;

      //  Log.d(TAG ,"AsyncHttpClient URL- " + URL);
      //  Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());



        client.setSSLSocketFactory(
                new SSLSocketFactory(Common.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));

        client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , Common.DEFAULT_TIMEOUT);
        client.post(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

             //   Log.d(TAG, "onSuccess- " + response.toString());

                if (response != null) {
                    try {


                        int success = response.optInt("success");
                        String message = response.optString("message");

                        if (success == 0){

                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();


                        }else
                        if (success == 1){

                            if (is_blocked == 0){
                                is_blocked = 1;
                            }else {
                                is_blocked = 0;
                            }


                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

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

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(UserVideoShow_Noti.this).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });

    }


    public void reportUser(String description){


        progressDialog.show();


        String URL = All_Constants_Urls.REPORT_USER;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());
        params.put(All_Constants_Urls.report_user_id, Font_User_Id);
        params.put(All_Constants_Urls.description, description);


        final String TAG = All_Constants_Urls.TAG;

      //  Log.d(TAG ,"AsyncHttpClient URL- " + URL);
      //  Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());

        client.setSSLSocketFactory(
                new SSLSocketFactory(Common.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));


        client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , Common.DEFAULT_TIMEOUT);
        client.post(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

             //   Log.d(TAG, "onSuccess- " + response.toString());

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

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(UserVideoShow_Noti.this).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });

    }


    public void likeUnlikeProfile(final int is_like){
       // progressDialog.show();

        String URL = All_Constants_Urls.LIKE_UNLIKE_PROFILE;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        /// user id who like profile
        params.put(All_Constants_Urls.like_by_user_id, sharedPref.get_Use_Id());
        /// whom profile liked
        params.put(All_Constants_Urls.user_id, Font_User_Id);
        params.put(All_Constants_Urls.is_like, is_like);


        final String TAG = All_Constants_Urls.TAG;

     //   Log.d(TAG ,"AsyncHttpClient URL- " + URL);
     //   Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


        client.setSSLSocketFactory(
                new SSLSocketFactory(Common.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));

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

                            if (is_like == 1){
                                sharedPref.save_My_Matches(null);
                            }
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

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(UserVideoShow_Noti.this).create();
                alert.setMessage("Server Error");
                alert.show();
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


        client.setSSLSocketFactory(
                new SSLSocketFactory(Common.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));



        final String TAG = All_Constants_Urls.TAG;

      //  Log.d(TAG ,"AsyncHttpClient URL- " + URL);
      //  Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


        client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , Common.DEFAULT_TIMEOUT);
        client.post(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

              //  Log.d(TAG, "onSuccess- " + response.toString());

                if (response != null) {
                    try {
                        int success = response.optInt("success");

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


    @Override
    protected void onDestroy() {
        //freeMemory();
        super.onDestroy();
    }

    public void freeMemory(){
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }




}

