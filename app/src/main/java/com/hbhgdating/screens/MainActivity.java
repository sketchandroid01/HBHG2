package com.hbhgdating.screens;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.hbhgdating.chat.Chat_screen_new;
import com.hbhgdating.databaseLocal.DatabaseHelper;
import com.hbhgdating.R;
import com.hbhgdating.services.MyService;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.splunk.mint.Mint;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    ImageView imgSetting,imghbhgleft,imghbhgright,imgProfile,imgBlockUser,imgReport;
    TextView tv_user_info1, tv_user_info2, notfound_txt;
    RelativeLayout rel_msg_box;
    Boolean isPanelShown=false;
    RelativeLayout relSearchSection, relLogoutSection;
    TextView txtCancel, txtSearch, tv_search, tv_logout;
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
    ArrayList<String> List_Matches;
    private String Font_User_Id;
    int Matches_list_Position = 0;
    int Searching_Radious = 0;
    boolean no_more_data = false;

    DisplayImageOptions defaultOptions;
    ImageLoaderConfiguration config;

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
        checkLocationPermission();
        //enableLocation();
        startService(new Intent(this, MyService.class));


        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // slideUpDown_searchTab();

                slideUpDown_LogoutTab();
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Common.hideSoftKeyboard(MainActivity.this);
                inputSearch.setText("");
                discrete1.setProgress(200);
                Searching_Radious = 0;
                slideUpDown_searchTab();
            }
        });


        tv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation bottomDown = AnimationUtils.loadAnimation(MainActivity.this,
                        R.anim.bottom_down);

                relLogoutSection.startAnimation(bottomDown);
                relLogoutSection.setVisibility(View.INVISIBLE);

                slideUpDown_searchTab();

            }
        });


        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogLogout();

            }
        });

        //////////
        boolean isConnected = ConnectivityReceiver.isConnected();

        if (isConnected){

            getMatchesData();

        }else {

            ShowToast("Please connect to internet");

            Thread_Start();
        }


        getUserBlockOrNot();

        Mint.disableNetworkMonitoring();
        Mint.initAndStartSession(this.getApplication(), "b7a32065");
    }


    public void Thread_Start(){

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

               boolean isConnected = ConnectivityReceiver.isConnected();

                if (isConnected){

                    getMatchesData();

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
        imgSetting = findViewById(R.id.imgSetting);
        imgProfile = findViewById(R.id.imgProfile);
        relSearchSection = findViewById(R.id.relSearchSection);
        relLogoutSection = findViewById(R.id.relLogoutSection);
        relSearchSection.setVisibility(View.INVISIBLE);
        relLogoutSection.setVisibility(View.INVISIBLE);
        txtCancel = findViewById(R.id.txtCancel);
        inputSearch = findViewById(R.id.inputSearch);
        tv_search = findViewById(R.id.tv_search);
        tv_logout = findViewById(R.id.tv_logout);
        gifviewYes = findViewById(R.id.gifviewYes);
        gifviewNo = findViewById(R.id.gifviewNo);
        tv_user_info1 = findViewById(R.id.tv_user_info1);
        tv_user_info2 = findViewById(R.id.tv_user_info2);
        notfound_txt = findViewById(R.id.notfound_txt);
        notfound_txt.setVisibility(View.GONE);
        rel_msg_box = findViewById(R.id.rel_msg_box);
        rel_msg_box.setVisibility(View.GONE);
        tv_user_info1.setVisibility(View.GONE);
        tv_user_info2.setVisibility(View.GONE);


        imghbhgleft = findViewById(R.id.imghbhgleft);
        imghbhgright = findViewById(R.id.imghbhgright);
        imghbhgleft.setVisibility(View.INVISIBLE);
        imghbhgright.setVisibility(View.INVISIBLE);

        imgBlockUser = findViewById(R.id.imgBlockUser);
        imgReport = findViewById(R.id.imgReport);


        mDemoSlider_1 = findViewById(R.id.slider1);
        mDemoSlider_2 = findViewById(R.id.slider2);
        mDemoSlider_1.setVisibility(View.GONE);
        mDemoSlider_2.setVisibility(View.GONE);

        videoView1 = findViewById(R.id.videoView1);
        mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView1);

        List_Matches = new ArrayList<>();




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
                Intent intent1 = new Intent(MainActivity.this,
                        UserProfileActivity.class);
                intent1.putExtra("userid", Font_User_Id);
                intent1.putExtra("data", List_Matches.get(Matches_list_Position));
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in_up, R.anim.stay);

            }
        });


        discrete1 = (DiscreteSeekBar) findViewById(R.id.discrete1);
        discrete1.setProgress(200);
        discrete1.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {

                Log.d(All_Constants_Urls.TAG, "Seekbar value = "+value);
                Searching_Radious = value;

            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {

            }
        });


        txtSearch = (TextView) findViewById(R.id.txtSearch);
        txtSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Searching_Radious != 0){

                    if (checkGPS()){
                        getMatchesByRadius();
                    }

                }else
                if (inputSearch.getText().toString().trim().length() != 0){

                    getMatchesByKeyword(inputSearch.getText().toString().trim());

                }else {
                    Toast.makeText(MainActivity.this, "Enter name or city properly", Toast.LENGTH_SHORT).show();
                }

                slideUpDown_searchTab();
            }
        });





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


                                To_Show_Next_Mathes1();


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

                                To_Show_Next_Mathes1();


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


    public void To_Show_Next_Mathes1(){

       // progressDialog.show();

        if (List_Matches.size() != 0){
            if (Matches_list_Position + 1 < List_Matches.size()){

                no_more_data = false;

                Log.d(All_Constants_Urls.TAG, "Matches_list_Position = "+Matches_list_Position);

                showProfileVideos(List_Matches.get(++Matches_list_Position));

            }else {

               // Toast.makeText(this, "No more matches available", Toast.LENGTH_LONG).show();

                tv_user_info2.setVisibility(View.GONE);
                mDemoSlider_2.setVisibility(View.GONE);
                mDemoSlider_2.removeAllSliders();

                tv_user_info1.setVisibility(View.GONE);
                mDemoSlider_1.setVisibility(View.GONE);
                mDemoSlider_1.removeAllSliders();

                videoView1.setVisibility(View.GONE);

                imghbhgright.setVisibility(View.GONE);
                imghbhgleft.setVisibility(View.GONE);

                imgProfile.setVisibility(View.INVISIBLE);
                imgBlockUser.setVisibility(View.INVISIBLE);
                imgReport.setVisibility(View.INVISIBLE);

                notfound_txt.setVisibility(View.VISIBLE);
                rel_msg_box.setVisibility(View.VISIBLE);


                no_more_data = true;
            }
        }


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!no_more_data){

                    if (mDemoSlider_1.getVisibility() == View.GONE){

                        mDemoSlider_1.setVisibility(View.VISIBLE);
                        tv_user_info1.setVisibility(View.VISIBLE);
                        tv_user_info2.setVisibility(View.GONE);
                        mDemoSlider_2.setVisibility(View.GONE);
                        mDemoSlider_2.removeAllSliders();

                    }else if (mDemoSlider_2.getVisibility() == View.GONE){

                        mDemoSlider_2.setVisibility(View.VISIBLE);
                        tv_user_info1.setVisibility(View.GONE);
                        tv_user_info2.setVisibility(View.VISIBLE);
                        mDemoSlider_1.setVisibility(View.GONE);
                        mDemoSlider_1.removeAllSliders();

                    }

                } else {



                }

             //   progressDialog.dismiss();

            }
        }, Common.loader_time);


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
                Intent intent = new Intent(MainActivity.this,
                        ProfileActivity.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        relChat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, Chat_screen_new.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        relFav.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, FavoriteActivity.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        relNotification.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
                MainActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
    }

    public void slideUpDown_LogoutTab() {
        if (relLogoutSection.getVisibility() == View.INVISIBLE) {
            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_up);

            relLogoutSection.startAnimation(bottomUp);
            relLogoutSection.setVisibility(View.VISIBLE);

        } else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(this,
                    R.anim.bottom_down);

            relLogoutSection.startAnimation(bottomDown);
            relLogoutSection.setVisibility(View.INVISIBLE);
        }
    }

    public void slideUpDown_searchTab() {
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

        if (global_class.isIf_action_on_block_screen()){
            getMatchesData();

            global_class.setIf_action_on_block_screen(false);
        }

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

    public void checkLocationPermission(){

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED &&

                    ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                //cameraPreview.addView(preview);
            }
            else{
                if(checkForPermission(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION }, 124)){

                }
                else{

                }

            }
        }
        else {

            ///
        }

    }

    public boolean checkForPermission(final String[] permissions, final int permRequestCode) {

        final List<String> permissionsNeeded = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            final String perm = permissions[i];
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {

                    Log.d("permisssion","not granted");


                    if (shouldShowRequestPermissionRationale(permissions[i])) {

                        Log.d("if","if");
                        permissionsNeeded.add(perm);

                    } else {
                        // add the request.
                        Log.d("else","else");
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

    public boolean checkGPS(){

        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();

            return false;
        }

        return true;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
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


    public void getMatchesData(){


        progressDialog.show();

        List_Matches.clear();

        String URL = All_Constants_Urls.GET_MATCHES;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());


        final String TAG = All_Constants_Urls.TAG;

        Log.d(TAG ,"AsyncHttpClient URL- " + URL);
        Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


        client.setSSLSocketFactory(
                new SSLSocketFactory(Common.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));


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

                            notfound_txt.setVisibility(View.VISIBLE);
                            rel_msg_box.setVisibility(View.VISIBLE);

                            imgProfile.setVisibility(View.INVISIBLE);
                            imgBlockUser.setVisibility(View.INVISIBLE);
                            imgReport.setVisibility(View.INVISIBLE);

                            progressDialog.dismiss();


                        }else
                        if (success == 1){

                            //global_class.All_Matches_Data = response.toString();

                            JSONArray data_array = response.getJSONArray(All_Constants_Urls.data);

                            Log.d(TAG, "length - " +data_array.length());
                            for (int i = 0; i < data_array.length(); i++){
                                JSONObject object = data_array.getJSONObject(i);

                                int is_blocked = object.optInt(All_Constants_Urls.is_blocked);
                                if (is_blocked == 0){

                                    List_Matches.add(object.toString());

                                }

                            }

                            if (List_Matches.size() != 0){

                                notfound_txt.setVisibility(View.GONE);
                                rel_msg_box.setVisibility(View.GONE);


                                showProfileVideos(List_Matches.get(Matches_list_Position));

                                mDemoSlider_1.setVisibility(View.VISIBLE);
                                tv_user_info1.setVisibility(View.VISIBLE);

                                imgProfile.setVisibility(View.VISIBLE);
                                imgBlockUser.setVisibility(View.VISIBLE);
                                imgReport.setVisibility(View.VISIBLE);


                            }else {
                                imgProfile.setVisibility(View.INVISIBLE);
                                imgBlockUser.setVisibility(View.INVISIBLE);
                                imgReport.setVisibility(View.INVISIBLE);

                                notfound_txt.setVisibility(View.VISIBLE);
                                rel_msg_box.setVisibility(View.VISIBLE);
                            }


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

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(MainActivity.this).create();
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
            if (strGender.equalsIgnoreCase("m")
                    || strGender.equalsIgnoreCase("male") ){
                gender = "Male";
            }else if (strGender.equalsIgnoreCase("f")
                    || strGender.equalsIgnoreCase("female")){
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


            if (strGender.equalsIgnoreCase("m")
                    || strGender.equalsIgnoreCase("male") ){
                imghbhgleft.setImageResource(R.mipmap.no_boy_symbol);
                imghbhgright.setImageResource(R.mipmap.hey_boy_symbol);
                imghbhgright.setVisibility(View.VISIBLE);
                imghbhgleft.setVisibility(View.VISIBLE);
            }else if (strGender.equalsIgnoreCase("f")
                    || strGender.equalsIgnoreCase("female") ){
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

                if (mDemoSlider_1.getVisibility() == View.GONE){

                    tv_user_info1.setText(temp_text);

                    setData_Slider1(image_video);

                }else if (mDemoSlider_2.getVisibility() == View.GONE){

                    tv_user_info2.setText(temp_text);

                    setData_Slider2(image_video);

                }

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


    public void setData_Slider2(JSONArray image_video){

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

                    mDemoSlider_2.addSlider(textSliderView);
                }
                playSlider2();

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
        mDemoSlider_1.addOnPageChangeListener(MainActivity.this);
        mDemoSlider_1.startAutoCycle();

    }

    public void playSlider2(){

        mDemoSlider_2.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        mDemoSlider_2.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        mDemoSlider_2.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider_2.setDuration(2000);
        mDemoSlider_2.moveNextPosition(true);
        mDemoSlider_2.addOnPageChangeListener(MainActivity.this);
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
        //Setting MediaController and URI, then starting the videoView

        //proxy = Global_Class.getProxy(this);
        //final String proxyUrl = proxy.getProxyUrl(videoUri);


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

        Log.d(TAG ,"AsyncHttpClient URL- " + URL);
        Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


        client.setSSLSocketFactory(
                new SSLSocketFactory(Common.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));

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

                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();


                        }else
                        if (success == 1){

                            if (is_blocked == 0){
                                is_blocked = 1;
                            }else {
                                is_blocked = 0;
                            }

                            To_Show_Next_Mathes1();

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

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(MainActivity.this).create();
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

        Log.d(TAG ,"AsyncHttpClient URL- " + URL);
        Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


        client.setSSLSocketFactory(
                new SSLSocketFactory(Common.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));

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

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(MainActivity.this).create();
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

        Log.d(TAG ,"AsyncHttpClient URL- " + URL);
        Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


        client.setSSLSocketFactory(
                new SSLSocketFactory(Common.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));

        client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , Common.DEFAULT_TIMEOUT);
        client.post(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "onSuccess- " + response.toString());

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

                               // Toast.makeText(getApplicationContext(), "Like", Toast.LENGTH_LONG).show();
                            }else {

                              //  Toast.makeText(getApplicationContext(), "Unlike", Toast.LENGTH_LONG).show();
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

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(MainActivity.this).create();
                alert.setMessage("Server Error");
                alert.show();
            }

        });

    }


    public void getMatchesByRadius(){

        progressDialog.show();


        String URL = All_Constants_Urls.MATCHES_BY_RADIUS;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());
        params.put(All_Constants_Urls.radius, Searching_Radious);
        params.put(All_Constants_Urls.latitude, global_class.LATITUDE);
        params.put(All_Constants_Urls.longitude, global_class.LONGITUDE);


        final String TAG = All_Constants_Urls.TAG;

        Log.d(TAG ,"AsyncHttpClient URL- " + URL);
        Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


        client.setSSLSocketFactory(
                new SSLSocketFactory(Common.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));

        client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , Common.DEFAULT_TIMEOUT);
        client.post(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "onSuccess- " + response.toString());

                if (response != null) {
                    try {

                        int success = response.optInt("success");

                        if (success == 0){

                         //   Toast.makeText(getApplicationContext(), "Something error", Toast.LENGTH_LONG).show();

                            notfound_txt.setVisibility(View.VISIBLE);
                            rel_msg_box.setVisibility(View.VISIBLE);

                            progressDialog.dismiss();


                        }else
                        if (success == 1){

                            List_Matches.clear();

                            //global_class.All_Matches_Data = response.toString();

                            mDemoSlider_1.setVisibility(View.GONE);
                            mDemoSlider_2.setVisibility(View.GONE);
                            mDemoSlider_1.removeAllSliders();
                            mDemoSlider_2.removeAllSliders();
                            Matches_list_Position = 0;

                            JSONArray data_array = response.getJSONArray(All_Constants_Urls.data);

                            Log.d(TAG, "length - " +data_array.length());
                            for (int i = 0; i < data_array.length(); i++){
                                JSONObject object = data_array.getJSONObject(i);

                                int is_blocked = object.optInt(All_Constants_Urls.is_blocked);
                                if (is_blocked == 0){

                                    List_Matches.add(object.toString());

                                }

                            }

                            if (List_Matches.size() != 0){

                                showProfileVideos(List_Matches.get(Matches_list_Position));

                                mDemoSlider_1.setVisibility(View.VISIBLE);
                                tv_user_info1.setVisibility(View.VISIBLE);

                                imgProfile.setVisibility(View.VISIBLE);
                                imgBlockUser.setVisibility(View.VISIBLE);
                                imgReport.setVisibility(View.VISIBLE);

                                rel_msg_box.setVisibility(View.GONE);
                                notfound_txt.setVisibility(View.GONE);

                            }else {
                                imgProfile.setVisibility(View.INVISIBLE);
                                imgBlockUser.setVisibility(View.INVISIBLE);
                                imgReport.setVisibility(View.INVISIBLE);

                                notfound_txt.setVisibility(View.VISIBLE);
                                rel_msg_box.setVisibility(View.VISIBLE);
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

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(MainActivity.this).create();
                alert.setMessage("Server Error");
                alert.show();
            }

        });

    }


    public void getMatchesByKeyword(String key){

        progressDialog.show();

        String URL = All_Constants_Urls.MATCHES_BY_KEYWORD;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());
        params.put(All_Constants_Urls.keyword, key);


        final String TAG = All_Constants_Urls.TAG;

        Log.d(TAG ,"AsyncHttpClient URL- " + URL);
        Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());



        client.setSSLSocketFactory(
                new SSLSocketFactory(Common.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));

        client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , Common.DEFAULT_TIMEOUT);
        client.post(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "onSuccess- " + response.toString());

                if (response != null) {
                    try {

                        int success = response.optInt("success");

                        if (success == 0){

                          //  Toast.makeText(getApplicationContext(), "Something error", Toast.LENGTH_LONG).show();


                            notfound_txt.setVisibility(View.VISIBLE);
                            rel_msg_box.setVisibility(View.VISIBLE);

                            progressDialog.dismiss();


                        }else
                        if (success == 1){

                            List_Matches.clear();

                           // global_class.All_Matches_Data = response.toString();
                            mDemoSlider_1.setVisibility(View.GONE);
                            mDemoSlider_2.setVisibility(View.GONE);
                            mDemoSlider_1.removeAllSliders();
                            mDemoSlider_2.removeAllSliders();
                            Matches_list_Position = 0;

                            JSONArray data_array = response.getJSONArray(All_Constants_Urls.data);

                            Log.d(TAG, "length - " +data_array.length());
                            for (int i = 0; i < data_array.length(); i++){
                                JSONObject object = data_array.getJSONObject(i);

                                int is_blocked = object.optInt(All_Constants_Urls.is_blocked);
                                if (is_blocked == 0){

                                    List_Matches.add(object.toString());

                                }

                            }

                            if (List_Matches.size() != 0){


                                showProfileVideos(List_Matches.get(Matches_list_Position));

                                mDemoSlider_1.setVisibility(View.VISIBLE);
                                tv_user_info1.setVisibility(View.VISIBLE);

                                imgProfile.setVisibility(View.VISIBLE);
                                imgBlockUser.setVisibility(View.VISIBLE);
                                imgReport.setVisibility(View.VISIBLE);

                                rel_msg_box.setVisibility(View.GONE);
                                notfound_txt.setVisibility(View.GONE);

                            }else {
                                imgProfile.setVisibility(View.INVISIBLE);
                                imgBlockUser.setVisibility(View.INVISIBLE);
                                imgReport.setVisibility(View.INVISIBLE);

                                notfound_txt.setVisibility(View.VISIBLE);
                                rel_msg_box.setVisibility(View.VISIBLE);
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

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(MainActivity.this).create();
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

                        if (success == 0){

                          //  Toast.makeText(getApplicationContext(), "Something error", Toast.LENGTH_LONG).show();



                        }else
                        if (success == 1){



                        }


                        updateLATLNG();


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

    public void updateLATLNG(){


        String URL = All_Constants_Urls.UPDATE_LATLNG;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());
        params.put(All_Constants_Urls.lati, global_class.LATITUDE);
        params.put(All_Constants_Urls.longi, global_class.LONGITUDE);


        final String TAG = All_Constants_Urls.TAG;

        Log.d(TAG ,"AsyncHttpClient URL- " + URL);
        Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


        client.setSSLSocketFactory(
                new SSLSocketFactory(Common.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));


        client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , Common.DEFAULT_TIMEOUT);
        client.post(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "onSuccess- " + response.toString());

                if (response != null) {
                    try {


                        int success = response.optInt("success");

                        if (success == 0){

                            //  Toast.makeText(getApplicationContext(), "Something error", Toast.LENGTH_LONG).show();



                        }else
                        if (success == 1){



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

    public void getUserBlockOrNot(){

        String URL = All_Constants_Urls.userblock;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());


        final String TAG = All_Constants_Urls.TAG;

        Log.d(TAG ,"AsyncHttpClient URL- " + URL);
        Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


        client.setSSLSocketFactory(
                new SSLSocketFactory(Common.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));


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


    protected GoogleApiClient mGoogleApiClient;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    boolean buildstatus;

    public void enableLocation() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {

                            Log.d("TAG", "Location permissiom granted");
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
                            status.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        //freeMemory();
        super.onDestroy();
    }


    public void dialogLogout(){

        android.app.AlertDialog.Builder builder =
                new android.app.AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Hey Boy Hey Girl");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        sharedPref.clearData();

                        DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                        databaseHelper.deleteAllDataFromDB();

                        Intent intent = new Intent(MainActivity.this,
                                SplashActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });

        builder.setNegativeButton("NO",
                new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                    }
                });

        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }



    public void freeMemory(){
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

}

