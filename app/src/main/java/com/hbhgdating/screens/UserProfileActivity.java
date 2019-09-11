package com.hbhgdating.screens;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.danikula.videocache.HttpProxyCacheServer;
import com.hbhgdating.R;
import com.hbhgdating.adapter.Userprofile_Vp_Adapter;
import com.hbhgdating.from_notification.UserVideoShow_Noti;
import com.hbhgdating.slider.Animations.DescriptionAnimation;
import com.hbhgdating.slider.Indicators.PagerIndicator;
import com.hbhgdating.slider.SliderLayout;
import com.hbhgdating.slider.SliderTypes.BaseSliderView;
import com.hbhgdating.slider.SliderTypes.TextSliderView;
import com.hbhgdating.slider.Tricks.ViewPagerEx;
import com.hbhgdating.utils.All_Constants_Urls;
import com.hbhgdating.utils.Common;
import com.hbhgdating.utils.Global_Class;
import com.hbhgdating.utils.ScalableVideoView;
import com.hbhgdating.utils.SharedPref;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;

public class UserProfileActivity extends Activity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    ImageView imgFood1, imgFood2, imgFood3, imgMusic1, imgMusic2, imgMusic3,
            imgInterest1, imgInterest2, imgInterest3, imgSexualOriante,
            imgReport, imgBlockUser;
    TextView  txtTitle;
    SliderLayout slider, slider2, slider3, sliderfull, sliderfull2, sliderfull3;
    ImageView iv1, iv2, iv3, image1Play, image2Play, image3Play;
    ScalableVideoView videoView1, videoView2, videoView3;
    ImageView eats_img, loves_img, listens_img;
    RelativeLayout  relVideo1, relVideo2, relVideo3;
    RelativeLayout relativeLayoutfull;
    ViewPager mPager;

    Dialog progressDialog;

    MediaController mediaController;

    String strSexual = "", strGender = "";
    String userID;
    String User_Data;
    boolean isUserMatch = false;
    int is_blocked;
    AlertDialog alertDialog;
   // private InterstitialAd interstitial;
    int ispush = 0;

    Boolean fullplay = false;


    Userprofile_Vp_Adapter userprofile_vp_adapter;
    private Handler mHandler;
    Runnable mRunnable;
    public static final int DELAY = 3000;
    int NUM_PAGES = 3;
    private static int currentPage = 0;

    Global_Class global_class;
    SharedPref sharedPref;

    boolean is_video_1,is_video_2, is_video_3;
    String Video_Url_1, Video_Url_2, Video_Url_3;
    String Font_User_Id;

    //HttpProxyCacheServer proxy;

    ArrayList<HashMap<String, String>> List_Text = new ArrayList<>();
    ArrayList<HashMap<String, Integer>> List_Image = new ArrayList<>();
    ArrayList<HashMap<String, String>> List_Image_Uri = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.userprofile_new);
        global_class = (Global_Class) getApplicationContext();
        sharedPref = new SharedPref(this);

        //proxy = Global_Class.getProxy(this);

        progressDialog = new Dialog(this, android.R.style.Theme_Translucent);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setContentView(R.layout.progressbar_pleasewait);
        progressDialog.setCanceledOnTouchOutside(false);



        setView();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            userID = bundle.getString("userid");
            User_Data = bundle.getString("data");


        }

        if (User_Data != null){

            setProfileData(User_Data);
            set_Video_Data(User_Data);
            Set_Images(User_Data);

        }else {

            getUserData();
        }







        setSexualOrientation();
        //loadInterstitialAD();
    }


    @Override
    protected void onStop() {

        slider.stopAutoCycle();
        sliderfull.stopAutoCycle();

        slider2.stopAutoCycle();
        sliderfull2.stopAutoCycle();

        slider3.stopAutoCycle();
        sliderfull3.stopAutoCycle();

        videoView1.stopPlayback();
        videoView2.stopPlayback();
        videoView3.stopPlayback();

        super.onStop();
    }

    @Override
    protected void onResume() {

        slider.startAutoCycle();

        slider2.startAutoCycle();

        slider3.startAutoCycle();

        videoView1.start();
        videoView2.start();
        videoView3.start();

        super.onResume();
    }

    @Override
    protected void onPause() {

        slider.stopAutoCycle();
        sliderfull.stopAutoCycle();

        slider2.stopAutoCycle();
        sliderfull2.stopAutoCycle();

        slider3.stopAutoCycle();
        sliderfull3.stopAutoCycle();

        videoView1.stopPlayback();
        videoView2.stopPlayback();
        videoView3.stopPlayback();


        super.onPause();
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog

        if (fullplay) {
            sliderfull.setVisibility(View.GONE);
            sliderfull2.setVisibility(View.GONE);
            sliderfull3.setVisibility(View.GONE);
            sliderfull.stopAutoCycle();
            sliderfull2.stopAutoCycle();
            sliderfull3.stopAutoCycle();

            if (is_video_1){
                videoView1.start();
            }else {
                slider.startAutoCycle();
            }

            if (is_video_2){
                videoView2.start();
            }else {
                slider2.startAutoCycle();
            }

            if (is_video_3){
                videoView3.start();
            }else {
                slider3.startAutoCycle();
            }


            relativeLayoutfull.setVisibility(View.VISIBLE);
            fullplay = false;

        } else {

            finish();
            super.onBackPressed();
            overridePendingTransition(R.anim.stay, R.anim.top_to_bottom);

        }


    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {

        String trans1[] = {"", "ZoomOut", "ZoomOutSlide", "Tablet", "DepthPage", "ZoomOut", "Fade", "RotateDown"};
        String trans2[] = {"", "ZoomOutSlide", "DepthPage", "Fade", "ZoomOut", "RotateDown", "Tablet", "ZoomOut"};
        String trans3[] = {"", "RotateDown", "Fade", "ZoomOut", "DepthPage", "Tablet", "ZoomOutSlide", "ZoomOut"};
        Random rand = new Random();
        int randomNum = rand.nextInt((7 - 1) + 1) + 1;

        slider.setPresetTransformer(trans1[randomNum]);
        slider2.setPresetTransformer(trans2[randomNum]);
        slider3.setPresetTransformer(trans3[randomNum]);
        sliderfull.setPresetTransformer(trans1[randomNum]);
        sliderfull2.setPresetTransformer(trans2[randomNum]);
        sliderfull3.setPresetTransformer(trans3[randomNum]);

        Log.d("Slider Demo", "Page Changed: " + position + "random" + randomNum);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }


    private void loadInterstitialAD() {
        /*interstitial = new InterstitialAd(UserProfileActivity.this);
        interstitial.setAdUnitId(getResources().getString(
                R.string.interstitial_ad_unit_id));
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);
        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {
                displayInterstitial();
            }

            public void onAdClosed() {

            }
        });*/

    }

    public void displayInterstitial() {
        /*if (interstitial.isLoaded()) {
            interstitial.show();
        }*/
    }

    private void setView() {

        mediaController = new MediaController(this);


        relVideo1 = (RelativeLayout) this.findViewById(R.id.relvideo1);
        relVideo2 = (RelativeLayout) this.findViewById(R.id.relVideo2);
        relVideo3 = (RelativeLayout) this.findViewById(R.id.relVideo3);

        imgSexualOriante = (ImageView) this.findViewById(R.id.imgSexualOriante);
        image1Play = (ImageView) this.findViewById(R.id.image1Play);
        image2Play = (ImageView) this.findViewById(R.id.image2Play);
        image3Play = (ImageView) this.findViewById(R.id.image3Play);

        iv1 = (ImageView) this.findViewById(R.id.iv1);
        iv2 = (ImageView) this.findViewById(R.id.iv2);
        iv3 = (ImageView) this.findViewById(R.id.iv3);

        txtTitle = (TextView) this.findViewById(R.id.txtTitle);

        imgReport = (ImageView) this.findViewById(R.id.imgReport);
        imgBlockUser = (ImageView) this.findViewById(R.id.imgBlockUser);

        eats_img = (ImageView) this.findViewById(R.id.eats_img);
        loves_img = (ImageView) this.findViewById(R.id.loves_img);
        listens_img = (ImageView) this.findViewById(R.id.listens_img);



        slider = (SliderLayout) findViewById(R.id.slider);
        slider2 = (SliderLayout) findViewById(R.id.slider2);
        slider3 = (SliderLayout) findViewById(R.id.slider3);
        sliderfull = (SliderLayout) findViewById(R.id.sliderfull);
        sliderfull2 = (SliderLayout) findViewById(R.id.sliderfull2);
        sliderfull3 = (SliderLayout) findViewById(R.id.sliderfull3);

        videoView1 = (ScalableVideoView) findViewById(R.id.videoView1);
        videoView2 = (ScalableVideoView) findViewById(R.id.videoView2);
        videoView3 = (ScalableVideoView) findViewById(R.id.videoView3);



        relativeLayoutfull = (RelativeLayout) this.findViewById(R.id.relativeLayoutfull);
        mPager = (ViewPager) findViewById(R.id.pager_user_profile);


        eats_img.setImageResource(R.mipmap.button_eat);
        loves_img.setImageResource(R.mipmap.button_love2);
        listens_img.setImageResource(R.mipmap.button_listen2);

        ViewPager_Onchange();

        Run_ViewPager();


        eats_img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                eats_img.setImageResource(R.mipmap.button_eat);
                loves_img.setImageResource(R.mipmap.button_love2);
                listens_img.setImageResource(R.mipmap.button_listen2);

                mPager.setCurrentItem(0);
            }
        });

        loves_img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                eats_img.setImageResource(R.mipmap.button_eat2);
                loves_img.setImageResource(R.mipmap.button_love);
                listens_img.setImageResource(R.mipmap.button_listen2);

                mPager.setCurrentItem(1);
            }
        });

        listens_img.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                listens_img.setImageResource(R.mipmap.button_listen);
                loves_img.setImageResource(R.mipmap.button_love2);
                eats_img.setImageResource(R.mipmap.button_eat2);

                mPager.setCurrentItem(2);
            }
        });



        imgReport.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                openReportDialogBox();
            }
        });
        imgBlockUser.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                BlockUserDialog();
            }
        });

        image1Play.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (is_video_1){

                    Intent intent = new Intent(UserProfileActivity.this,View_Video.class);
                    intent.putExtra("video_view",Video_Url_1);
                    startActivity(intent);

                }else{

                    videoEffectFull1();
                    sliderfull.setVisibility(View.VISIBLE);
                    relativeLayoutfull.setVisibility(View.INVISIBLE);
                    fullplay = true;

                    slider.stopAutoCycle();
                    slider2.stopAutoCycle();
                    slider3.stopAutoCycle();

                    videoView1.stopPlayback();
                    videoView2.stopPlayback();
                    videoView3.stopPlayback();
                }

            }
        });
        image2Play.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (is_video_2){

                    Intent intent = new Intent(UserProfileActivity.this,View_Video.class);
                    intent.putExtra("video_view",Video_Url_2);
                    startActivity(intent);

                }else {

                    videoEffectFull2();

                    sliderfull2.setVisibility(View.VISIBLE);
                    relativeLayoutfull.setVisibility(View.INVISIBLE);
                    fullplay = true;

                    slider.stopAutoCycle();
                    slider2.stopAutoCycle();
                    slider3.stopAutoCycle();

                    videoView1.stopPlayback();
                    videoView2.stopPlayback();
                    videoView3.stopPlayback();

                }
            }
        });
        image3Play.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                if (is_video_3){

                    Intent intent = new Intent(UserProfileActivity.this,View_Video.class);
                    intent.putExtra("video_view",Video_Url_3);
                    startActivity(intent);

                }else {
                    videoEffectFull3();

                    sliderfull3.setVisibility(View.VISIBLE);
                    relativeLayoutfull.setVisibility(View.INVISIBLE);
                    fullplay = true;

                    slider.stopAutoCycle();
                    slider2.stopAutoCycle();
                    slider3.stopAutoCycle();


                    videoView1.stopPlayback();
                    videoView2.stopPlayback();
                    videoView3.stopPlayback();

                }
            }
        });




    }


    public void ViewPager_Onchange(){

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {

                currentPage = position;

                if (position == 0) {

                    eats_img.setImageResource(R.mipmap.button_eat);
                    loves_img.setImageResource(R.mipmap.button_love2);
                    listens_img.setImageResource(R.mipmap.button_listen2);


                } else if (position == 1) {

                    loves_img.setImageResource(R.mipmap.button_love);
                    listens_img.setImageResource(R.mipmap.button_listen2);
                    eats_img.setImageResource(R.mipmap.button_eat2);



                } else if (position == 2) {

                    listens_img.setImageResource(R.mipmap.button_listen);
                    loves_img.setImageResource(R.mipmap.button_love2);
                    eats_img.setImageResource(R.mipmap.button_eat2);


                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public void Run_ViewPager(){

        mHandler = new Handler();

        mRunnable = new Runnable() {
            public void run() {


                if (global_class.isEdit){

                    // not change pages

                }else {

                    if (currentPage == NUM_PAGES) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);

                }

            }
        };


        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                mHandler.post(mRunnable);

            }
        }, 500, DELAY);


    }

    public void SetValue_toList() {


        HashMap<String, String>  map1 = new HashMap();
        map1.put("t10", "add pic");
        map1.put("t20", "add pic");
        map1.put("t30", "add pic");


        HashMap<String, String>  map2 = new HashMap();
        map2.put("t11", "add pic");
        map2.put("t21", "add pic");
        map2.put("t31", "add pic");


        HashMap<String, String>  map3 = new HashMap();
        map3.put("t12", "add pic");
        map3.put("t22", "add pic");
        map3.put("t32", "add pic");

        List_Text.add(map1);
        List_Text.add(map2);
        List_Text.add(map3);


        HashMap<String, Integer> map4 = new HashMap();
        map4.put("i1", 0);
        map4.put("i2", 0);
        map4.put("i3", 0);


        HashMap<String, Integer> map5 = new HashMap();
        map5.put("i1", 0);
        map5.put("i2", 0);
        map5.put("i3", 0);


        HashMap<String, Integer> map6 = new HashMap();
        map6.put("i1", 0);
        map6.put("i2", 0);
        map6.put("i3", 0);


        List_Image.add(map4);
        List_Image.add(map5);
        List_Image.add(map6);


        String uri = null;

        HashMap<String, String> map7 = new HashMap();
        map7.put("p10", uri);
        map7.put("p20", uri);
        map7.put("p30", uri);

        HashMap<String, String> map8 = new HashMap();
        map8.put("p11", uri);
        map8.put("p21", uri);
        map8.put("p31", uri);

        HashMap<String, String> map9 = new HashMap();
        map9.put("p12", uri);
        map9.put("p22", uri);
        map9.put("p32", uri);

        List_Image_Uri.add(map7);
        List_Image_Uri.add(map8);
        List_Image_Uri.add(map9);


    }

    private void BlockUserDialog() {
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

                            Block_Unblock_User();

                            dialog.dismiss();
                        } catch (Exception e) {
                            // TODO: handle exception
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

    private void mDialogNewUser() {
        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.app_name)
                .content(R.string.newuser)
                .positiveText(R.string.ok)
                .show();
    }



    @SuppressLint("InflateParams")
    private void openReportDialogBox() {

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Report User :");
        // ...Irrelevant code for customizing the buttons and title
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

                    Report_User(et_description.getText().toString());
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


    private void setProfileData(String json){

        try{
            JSONObject object = new JSONObject(json);

            is_blocked = object.optInt(All_Constants_Urls.is_blocked);

            JSONObject basic_info = object.getJSONObject(All_Constants_Urls.basic_info);

            Font_User_Id = basic_info.getString(All_Constants_Urls.id);

            String name = basic_info.getString(All_Constants_Urls.name);
            String age = basic_info.getString(All_Constants_Urls.age);
            strGender = basic_info.getString(All_Constants_Urls.gender);
            strSexual = basic_info.getString(All_Constants_Urls.sexual_orientation);
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

            txtTitle.setText(temp_text);


            setSexualOrientation();

        }catch (Exception e){
            e.printStackTrace();
        }



    }



    private void setSexualOrientation() {
        if (strGender.equalsIgnoreCase("male")) {
            if (strSexual.equalsIgnoreCase("straight")) {
                imgSexualOriante.setImageResource(R.mipmap.straight_male_w);
            } else if (strSexual.equalsIgnoreCase("gay")) {
                imgSexualOriante.setImageResource(R.mipmap.gay_w);
            } else {
                imgSexualOriante.setImageResource(R.mipmap.bisexual_male_w);
            }
        } else {
            if (strSexual.equalsIgnoreCase("straight")) {
                imgSexualOriante.setImageResource(R.mipmap.straight_female_w);
            } else if (strSexual.equalsIgnoreCase("lesbian")) {
                imgSexualOriante.setImageResource(R.mipmap.lesbian_w);
            } else {
                imgSexualOriante.setImageResource(R.mipmap.bisexual_female_w);
            }
        }
    }


    private void openVideoviewr(String strPhotoURL) {
        if (!strPhotoURL.isEmpty()) {
            Intent mVideoWatch = new Intent(Intent.ACTION_VIEW);
            mVideoWatch.setDataAndType(Uri.parse(strPhotoURL), "video/*");
            try {
                startActivity(mVideoWatch);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public void set_Video_Data(String json){

        try {

            JSONObject object = new JSONObject(json);

            JSONObject basic_info = object.getJSONObject(All_Constants_Urls.basic_info);

            Log.d(All_Constants_Urls.TAG, "set_Video_Data: "+basic_info);

            if (basic_info != null){


                JSONArray image_video = basic_info.optJSONArray("image_video");
                if (image_video.length() != 0){

                    iv1.setVisibility(View.GONE);

                    slider.removeAllSliders();
                    sliderfull.removeAllSliders();


                    for (int i = 0; i < image_video.length(); i++){

                        String url = image_video.getString(i);

                        TextSliderView textSliderView1 = new TextSliderView(UserProfileActivity.this);
                        // initialize a SliderLayout
                        textSliderView1
                                .image(url)
                                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                                .setOnSliderClickListener(UserProfileActivity.this);

                        slider.addSlider(textSliderView1);
                        sliderfull.addSlider(textSliderView1);

                    }

                    videoEffect1();
                    // videoEffectf1();

                    is_video_1 = false;
                }

                String video = basic_info.optString("video");
                if (video.length() != 0){
                    Video_Url_1 = video;
                    is_video_1 = true;
                    iv1.setVisibility(View.GONE);
                    Play_Video(video);
                }

                /////

                JSONArray second_image_video = basic_info.optJSONArray("second_image_video");
                if (second_image_video.length() != 0){

                    slider2.removeAllSliders();
                    sliderfull2.removeAllSliders();

                    iv2.setVisibility(View.GONE);

                    for (int i = 0; i < second_image_video.length(); i++){

                        String url = second_image_video.getString(i);

                        TextSliderView textSliderView1 = new TextSliderView(UserProfileActivity.this);
                        // initialize a SliderLayout
                        textSliderView1
                                .image(url)
                                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                                .setOnSliderClickListener(UserProfileActivity.this);

                        slider2.addSlider(textSliderView1);
                        sliderfull2.addSlider(textSliderView1);

                    }

                    videoEffect2();
                    // videoEffectf2();

                    is_video_2 = false;
                }

                String second_video = basic_info.optString("second_video");
                if (second_video.length() != 0){
                    Video_Url_2 = second_video;
                    is_video_2 = true;
                    iv2.setVisibility(View.GONE);
                    Play_Video2(second_video);
                }

                /////

                JSONArray third_image_video = basic_info.optJSONArray("third_image_video");
                if (third_image_video.length() != 0){

                    iv3.setVisibility(View.GONE);

                    slider3.removeAllSliders();
                    sliderfull3.removeAllSliders();

                    for (int i = 0; i < third_image_video.length(); i++){

                        String url = third_image_video.getString(i);

                        TextSliderView textSliderView1 = new TextSliderView(UserProfileActivity.this);
                        // initialize a SliderLayout
                        textSliderView1
                                .image(url)
                                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                                .setOnSliderClickListener(UserProfileActivity.this);

                        slider3.addSlider(textSliderView1);
                        sliderfull3.addSlider(textSliderView1);

                    }
                    videoEffect3();
                    //videoEffectf3();

                    is_video_3 = false;
                }

                String third_video = basic_info.optString("third_video");
                if (third_video.length() != 0){
                    Video_Url_3 = third_video;
                    is_video_3 = true;
                    iv3.setVisibility(View.GONE);
                    Play_Video3(third_video);

                }


            }

        }catch (Exception e){
            e.printStackTrace();
        }




    }


    public void Set_Images(String json){

        List_Text.clear();
        List_Image_Uri.clear();


        try{

            JSONObject response = new JSONObject(json);


            /////////////  Interest data set ..........
            String uri = null;
            String ids = "";

            JSONArray food_data = response.getJSONArray(All_Constants_Urls.food_data);
            if (food_data.length() != 0){

                HashMap<String, String>  map1 = new HashMap();
                HashMap<String, String> map2 = new HashMap();


                if (food_data.length() == 1){
                    JSONObject obj_food = food_data.getJSONObject(0);


                    map1.put("t10", obj_food.getString(All_Constants_Urls.title));
                    map2.put("p10", obj_food.getString(All_Constants_Urls.photo));


                    map1.put("t20", "add pic");
                    map2.put("p20", uri);


                    map1.put("t30", "add pic");
                    map2.put("p30", uri);

                }else
                if (food_data.length() == 2){

                    for (int i = 0; i < food_data.length(); i++){
                        JSONObject obj_food = food_data.getJSONObject(i);

                        if (i == 0){

                            map1.put("t10", obj_food.getString(All_Constants_Urls.title));
                            map2.put("p10", obj_food.getString(All_Constants_Urls.photo));
                        }
                        if (i == 1){

                            map1.put("t20", obj_food.getString(All_Constants_Urls.title));
                            map2.put("p20", obj_food.getString(All_Constants_Urls.photo));
                        }

                    }


                    map1.put("t30", "add pic");
                    map2.put("p30", uri);

                }else
                if (food_data.length() == 3){

                    for (int i = 0; i < food_data.length(); i++){
                        JSONObject obj_food = food_data.getJSONObject(i);

                        if (i == 0){

                            map1.put("t10", obj_food.getString(All_Constants_Urls.title));
                            map2.put("p10", obj_food.getString(All_Constants_Urls.photo));
                        }
                        if (i == 1){

                            map1.put("t20", obj_food.getString(All_Constants_Urls.title));
                            map2.put("p20", obj_food.getString(All_Constants_Urls.photo));
                        }
                        if (i == 2){

                            map1.put("t30", obj_food.getString(All_Constants_Urls.title));
                            map2.put("p30", obj_food.getString(All_Constants_Urls.photo));
                        }

                    }


                }

                List_Text.add(map1);
                List_Image_Uri.add(map2);


            }else {


                HashMap<String, String>  map1 = new HashMap();
                map1.put("t10", "add pic");
                map1.put("t20", "add pic");
                map1.put("t30", "add pic");
                List_Text.add(map1);

                HashMap<String, String> map2 = new HashMap();
                map2.put("p10", uri);
                map2.put("p20", uri);
                map2.put("p30", uri);
                List_Image_Uri.add(map2);

            }

            //////////////
            /////////////


            JSONArray interest_data = response.getJSONArray(All_Constants_Urls.interest_data);
            if (interest_data.length() != 0){

                HashMap<String, String>  map3 = new HashMap();
                HashMap<String, String> map4 = new HashMap();



                if (interest_data.length() == 1){
                    JSONObject obj_intr = interest_data.getJSONObject(0);


                    map3.put("t11", obj_intr.getString(All_Constants_Urls.title));
                    map4.put("p11", obj_intr.getString(All_Constants_Urls.photo));


                    map3.put("t21", "add pic");
                    map4.put("p21", uri);


                    map3.put("t31", "add pic");
                    map4.put("p31", uri);

                }else
                if (interest_data.length() == 2){

                    for (int i = 0; i < interest_data.length(); i++){
                        JSONObject obj_intr = interest_data.getJSONObject(i);

                        if (i == 0){

                            map3.put("t11", obj_intr.getString(All_Constants_Urls.title));
                            map4.put("p11", obj_intr.getString(All_Constants_Urls.photo));
                        }
                        if (i == 1){

                            map3.put("t21", obj_intr.getString(All_Constants_Urls.title));
                            map4.put("p21", obj_intr.getString(All_Constants_Urls.photo));
                        }


                        map3.put("t31", "add pic");
                        map4.put("p31", uri);


                    }


                }else
                if (interest_data.length() == 3){

                    for (int i = 0; i < interest_data.length(); i++){
                        JSONObject obj_intr = interest_data.getJSONObject(i);

                        if (i == 0){

                            map3.put("t11", obj_intr.getString(All_Constants_Urls.title));
                            map4.put("p11", obj_intr.getString(All_Constants_Urls.photo));
                        }
                        if (i == 1){

                            map3.put("t21", obj_intr.getString(All_Constants_Urls.title));
                            map4.put("p21", obj_intr.getString(All_Constants_Urls.photo));
                        }
                        if (i == 2){

                            map3.put("t31", obj_intr.getString(All_Constants_Urls.title));
                            map4.put("p31", obj_intr.getString(All_Constants_Urls.photo));
                        }

                    }

                }

                List_Text.add(map3);
                List_Image_Uri.add(map4);


            }else {

                HashMap<String, String>  map3 = new HashMap();
                map3.put("t11", "add pic");
                map3.put("t21", "add pic");
                map3.put("t31", "add pic");

                List_Text.add(map3);

                HashMap<String, String> map4 = new HashMap();
                map4.put("p11", uri);
                map4.put("p21", uri);
                map4.put("p31", uri);

                List_Image_Uri.add(map4);

            }

            /////////////
            /////////////


            JSONArray music_data = response.getJSONArray(All_Constants_Urls.music_data);
            if (music_data.length() != 0){

                HashMap<String, String>  map5 = new HashMap();
                HashMap<String, String> map6 = new HashMap();


                if (music_data.length() == 1){
                    JSONObject obj_music = music_data.getJSONObject(0);


                    map5.put("t12", obj_music.getString(All_Constants_Urls.title));
                    map6.put("p12", obj_music.getString(All_Constants_Urls.photo));


                    map5.put("t22", "add pic");
                    map6.put("p22", uri);


                    map5.put("t32", "add pic");
                    map6.put("p32", uri);


                }else
                if (music_data.length() == 2){

                    for (int i = 0; i < music_data.length(); i++){
                        JSONObject obj_music = music_data.getJSONObject(i);

                        if (i == 0){

                            map5.put("t12", obj_music.getString(All_Constants_Urls.title));
                            map6.put("p12", obj_music.getString(All_Constants_Urls.photo));
                        }
                        if (i == 1){

                            map5.put("t22", obj_music.getString(All_Constants_Urls.title));
                            map6.put("p22", obj_music.getString(All_Constants_Urls.photo));
                        }


                        map5.put("t32", "add pic");
                        map6.put("p32", uri);

                    }

                }else
                if (music_data.length() == 3){


                    for (int i = 0; i < music_data.length(); i++){
                        JSONObject obj_music = music_data.getJSONObject(i);

                        if (i == 0){

                            map5.put("t12", obj_music.getString(All_Constants_Urls.title));
                            map6.put("p12", obj_music.getString(All_Constants_Urls.photo));
                        }
                        if (i == 1){

                            map5.put("t22", obj_music.getString(All_Constants_Urls.title));
                            map6.put("p22", obj_music.getString(All_Constants_Urls.photo));
                        }
                        if (i == 2){

                            map5.put("t32", obj_music.getString(All_Constants_Urls.title));
                            map6.put("p32", obj_music.getString(All_Constants_Urls.photo));
                        }

                    }

                }

                List_Text.add(map5);
                List_Image_Uri.add(map6);


            }else {

                HashMap<String, String>  map5 = new HashMap();
                map5.put("t12", "add pic");
                map5.put("t22", "add pic");
                map5.put("t32", "add pic");

                List_Text.add(map5);

                HashMap<String, String> map6 = new HashMap();
                map6.put("p12", uri);
                map6.put("p22", uri);
                map6.put("p32", uri);

                List_Image_Uri.add(map6);

            }


            HashMap<String, Integer> map4 = new HashMap();
            map4.put("i1", 0);
            map4.put("i2", 0);
            map4.put("i3", 0);

            HashMap<String, Integer> map5 = new HashMap();
            map5.put("i1", 0);
            map5.put("i2", 0);
            map5.put("i3", 0);


            HashMap<String, Integer> map6 = new HashMap();
            map6.put("i1", 0);
            map6.put("i2", 0);
            map6.put("i3", 0);


            List_Image.add(map4);
            List_Image.add(map5);
            List_Image.add(map6);



            userprofile_vp_adapter = new Userprofile_Vp_Adapter(UserProfileActivity.this,  List_Text,
                    List_Image, List_Image_Uri, image1Play, image2Play, image3Play);

            mPager.setAdapter(userprofile_vp_adapter);



        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void Play_Video(final String videoUri){

        slider.setVisibility(View.GONE);
        sliderfull.setVisibility(View.GONE);
        videoView1.setVisibility(View.VISIBLE);
        mediaController.setVisibility(View.GONE);

        videoView1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {

                mp.setVolume(0, 0);
            }
        });
        //Setting MediaController and URI, then starting the videoView

        //proxy = Global_Class.getProxy(this);
        //final String proxyUrl = proxy.getProxyUrl(videoUri);

        videoView1.setVideoURI(Uri.parse(videoUri));

        videoView1.setMediaController(mediaController);
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

    public void Play_Video2(final String videoUri){

        slider2.setVisibility(View.GONE);
        sliderfull2.setVisibility(View.GONE);
        videoView2.setVisibility(View.VISIBLE);
        mediaController.setVisibility(View.GONE);

        videoView2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {

                mp.setVolume(0, 0);
            }
        });
        //Setting MediaController and URI, then starting the videoView

        //proxy = Global_Class.getProxy(this);
        //final String proxyUrl = proxy.getProxyUrl(videoUri);


        videoView2.setVideoURI(Uri.parse(videoUri));

        videoView2.setMediaController(mediaController);
        videoView2.requestFocus();
        videoView2.start();

        videoView2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                videoView2.setVideoURI(Uri.parse(videoUri));
                videoView2.requestFocus();
                videoView2.start();
            }
        });

        videoView2.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d("video", "setOnErrorListener ");
                return true;
            }
        });


    }

    public void Play_Video3(final String videoUri){

        slider3.setVisibility(View.GONE);
        sliderfull3.setVisibility(View.GONE);
        videoView3.setVisibility(View.VISIBLE);
        mediaController.setVisibility(View.GONE);

        videoView3.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {

                mp.setVolume(0, 0);
            }
        });
        //Setting MediaController and URI, then starting the videoView

       // proxy = Global_Class.getProxy(this);
       // final String proxyUrl = proxy.getProxyUrl(videoUri);


        videoView3.setVideoURI(Uri.parse(videoUri));

        videoView3.setMediaController(mediaController);
        videoView3.requestFocus();
        videoView3.start();

        videoView3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                videoView3.setVideoURI(Uri.parse(videoUri));

                videoView3.requestFocus();
                videoView3.start();
            }
        });

        videoView3.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d("video", "setOnErrorListener ");
                return true;
            }
        });

    }

    public void videoEffect1(){
        slider.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        //slider1.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        slider.setCustomAnimation(new DescriptionAnimation());
        slider.setDuration(2000);
        slider.moveNextPosition(true);
        slider.addOnPageChangeListener(this);
        slider.startAutoCycle();

        slider.setVisibility(View.VISIBLE);
        videoView1.setVisibility(View.GONE);
        videoView1.stopPlayback();
    }

    public void videoEffect2(){
        slider2.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        //slider1.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider2.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        slider2.setCustomAnimation(new DescriptionAnimation());
        slider2.setDuration(2000);
        slider2.moveNextPosition(true);
        slider2.addOnPageChangeListener(this);
        slider2.startAutoCycle();

        slider2.setVisibility(View.VISIBLE);
        videoView2.setVisibility(View.GONE);
        videoView2.stopPlayback();
    }

    public void videoEffect3(){
        slider3.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        //slider1.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider3.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        slider3.setCustomAnimation(new DescriptionAnimation());
        slider3.setDuration(2000);
        slider3.moveNextPosition(true);
        slider3.addOnPageChangeListener(this);
        slider3.startAutoCycle();

        slider3.setVisibility(View.VISIBLE);
        videoView3.setVisibility(View.GONE);
        videoView3.stopPlayback();
    }

    public void videoEffectFull1(){
        sliderfull.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        sliderfull.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        sliderfull.setCustomAnimation(new DescriptionAnimation());
        sliderfull.setDuration(2000);
        sliderfull.moveNextPosition(true);
        sliderfull.addOnPageChangeListener(this);
        sliderfull.startAutoCycle();

    }

    public void videoEffectFull2(){
        sliderfull2.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        sliderfull2.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        sliderfull2.setCustomAnimation(new DescriptionAnimation());
        sliderfull2.setDuration(2000);
        sliderfull2.moveNextPosition(true);
        sliderfull2.addOnPageChangeListener(this);
        sliderfull2.startAutoCycle();

    }

    public void videoEffectFull3(){
        sliderfull3.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        sliderfull3.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        sliderfull3.setCustomAnimation(new DescriptionAnimation());
        sliderfull3.setDuration(2000);
        sliderfull3.moveNextPosition(true);
        sliderfull3.addOnPageChangeListener(this);
        sliderfull3.startAutoCycle();

    }



    public void Block_Unblock_User(){


        progressDialog.show();


        String URL = All_Constants_Urls.BLOCK_USER;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());
        params.put(All_Constants_Urls.block_user_id, Font_User_Id);


        final String TAG = All_Constants_Urls.TAG;

        Log.d(TAG ,"AsyncHttpClient URL- " + URL);
        Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


        client.setSSLSocketFactory(
                new SSLSocketFactory(Common.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));

        int DEFAULT_TIMEOUT = 30 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        client.post(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "onSuccess- " + response.toString());

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


                            global_class.setIf_action_on_block_screen(true);

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

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(UserProfileActivity.this).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });

    }


    public void Report_User(String description){


        progressDialog.show();


        String URL = All_Constants_Urls.REPORT_USER;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());
        params.put(All_Constants_Urls.report_user_id, Font_User_Id);
        params.put(All_Constants_Urls.description, description);


        final String TAG = All_Constants_Urls.TAG;

        Log.d(TAG ,"AsyncHttpClient URL- " + URL);
        Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


        client.setSSLSocketFactory(
                new SSLSocketFactory(Common.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));


        int DEFAULT_TIMEOUT = 30 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        client.post(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "onSuccess- " + response.toString());

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

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(UserProfileActivity.this).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });




    }

    public void getUserData(){


        progressDialog.show();


        String URL = All_Constants_Urls.GET_PROFILE_DATA;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, userID);


        final String TAG = All_Constants_Urls.TAG;

        Log.d(TAG ,"AsyncHttpClient URL- " + URL);
        Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


        //  client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , Common.DEFAULT_TIMEOUT);
        client.post(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "onSuccess- " + response.toString());

                if (response != null) {
                    try {


                        int success = response.optInt("success");

                        if (success == 0){

                            // Toast.makeText(getApplicationContext(), "Something error", Toast.LENGTH_LONG).show();


                            progressDialog.dismiss();


                        }else
                        if (success == 1){

                            User_Data = response.toString();

                            setProfileData(User_Data);
                            set_Video_Data(User_Data);
                            Set_Images(User_Data);

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

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(UserProfileActivity.this).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });


    }






}
