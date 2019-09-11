package com.hbhgdating.screens;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.hbhgdating.BuildConfig;
import com.hbhgdating.chat.Chat_screen_new;
import com.hbhgdating.databaseLocal.DatabaseHelper;
import com.hbhgdating.fb_Insta_Media.AllMediaFiles_2;
import com.hbhgdating.R;
import com.hbhgdating.trimmer.TrimmerActivity;
import com.hbhgdating.adapter.Profile_Vp_Adapter_;
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
import com.hbhgdating.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;
import life.knowledge4.videotrimmer.utils.FileUtils;

public class ProfileActivity extends AppCompatActivity implements ConnectionCallbacks,
        OnConnectionFailedListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener,
        Profile_Vp_Adapter_.CallbackInterface {

    public ImageView image1, image1Play,  image2Play,  image3Play,
            imgSexualOriante, imgfull;
    TextView  txtTitle, imgEditUser;
    LinearLayout ll_footer;
    SliderLayout slider1, mDemoSliderfull, slider2, slider3, mDemoSliderfull2, mDemoSliderfull3;
    ImageView eats_img, loves_img, listens_img, iv1,iv2,iv3;
    RelativeLayout  relVideo1, relVideo2, relVideo3, rl_2nd, rl_3rd;
    ViewPager mPager;


    Boolean fullplay = false;
    Boolean imgplay = false;
    RelativeLayout linearfull;
    Boolean switchnow=false;
    private static final int VIDEO_CAPTURE = 101;
    public static final int RESULT_LOAD_IMAGE = 105;
    private static final int PICK_VIDEO_REQUEST = 106;
    private static final int ALL_MEDIA_ACTIVITY_RESULT_CODE = 107;
    public static final int RESULT_FB_INSTA_Image = 108;

    private static final int GOOGLE_PHOTO_SELECTION = 2002;
    private static final int CAMERA_REQUEST = 2003;
    public static final int TRIMMER_ACTIVITY_RESULT_CODE = 2006;
    ScalableVideoView videoView1,videoView2,videoView3;
    MediaController mediaController;


    Dialog progressDialog;
    AlertDialog alertDialog,alertDialog2;
    Dialog_Name customize_Dia_Name;
    String position_image_key;
    int page_position;
    String title_swoing_text = "";
    String Text_key;
    boolean doubleBackToExitPressedOnce = false;

    boolean bclickvideo1, bclickvideo2, bclickvideo3;

    Context context;
    Profile_Vp_Adapter_ profile_vp_adapter;
    Global_Class global_class;
    LayoutInflater inflater;

    ////////////////////////////////
    ArrayList<HashMap<String, String>> List_Text = new ArrayList<>();
    ArrayList<HashMap<String, Integer>> List_Image = new ArrayList<>();
    ArrayList<HashMap<String, String>> List_Image_Uri = new ArrayList<>();
    ArrayList<HashMap<String, String>> List_IDS = new ArrayList<>();


    private Handler mHandler;
    Runnable mRunnable;
    public static final int DELAY = 3000;
    int NUM_PAGES = 3;
    private static int currentPage = 0;
    SharedPref sharedPref;
    private DatabaseHelper dbhelper;
    ArrayList<Image> images;

    Uri VideoUri1, VideoUri2, VideoUri3;
    View vw_left,vw_right;

    String which_view="";
    int leftid=R.drawable.img2;
    int midid=R.drawable.img1;
    int rightid=R.drawable.img3;
    int tempid;

    ArrayList<File> List_File_1;
    ArrayList<File> List_File_2;
    ArrayList<File> List_File_3;
    int seleced_viedo_layout ;

    Boolean is_video_1 = false;
    Boolean is_video_2 = false;
    Boolean is_video_3 = false;
    /**
     * Provides the entry point to Google Play services.
     */
    protected GoogleApiClient mGoogleApiClient;

    /**
     * Represents a geographical location.
     */

    Utility utility;
    HttpProxyCacheServer proxy;
    String Video_Url_1, Video_Url_2, Video_Url_3;

    String Captured_Video_Path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.profile_new);


        global_class = (Global_Class) getApplicationContext();
        context = this;
        utility = new Utility(context);
        //sf = new SharedPref(this);
        global_class.isEdit = false;

        //SetValue_toList();


        setView();


        buildGoogleApiClient();


    }


    /**
     * Builds a GoogleApiClient. Uses the addApi() method to request the
     * LocationServices API.
     */
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


    private void setView() {
        setBottom();


        progressDialog = new Dialog(this, android.R.style.Theme_Translucent);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setContentView(R.layout.progressbar_pleasewait);
        progressDialog.setCanceledOnTouchOutside(false);


        sharedPref = new SharedPref(this);
        sharedPref.Set_Orentation_to_Global();
        sharedPref.Set_LOCATION_GPS_to_Global();


        dbhelper = new DatabaseHelper(this);


        relVideo1 = (RelativeLayout) this.findViewById(R.id.relvideo1);
        relVideo2 = (RelativeLayout) this.findViewById(R.id.relVideo2);
        relVideo3 = (RelativeLayout) this.findViewById(R.id.relVideo3);
        vw_left = (View) this.findViewById(R.id.vw_left);
        vw_right = (View) this.findViewById(R.id.vw_right);
        // image1 = (ImageView) this.findViewById(R.id.image1);
        image1Play = (ImageView) this.findViewById(R.id.image1Play);

        image2Play = (ImageView) this.findViewById(R.id.image2Play);

        image3Play = (ImageView) this.findViewById(R.id.image3Play);
        imgSexualOriante = (ImageView) this.findViewById(R.id.imgSexualOriante);

        txtTitle = (TextView) this.findViewById(R.id.txtTitle);

        imgEditUser = (TextView) this.findViewById(R.id.imgEditUser);

        linearfull = (RelativeLayout) this.findViewById(R.id.linearfull);
        linearfull.setVisibility(View.INVISIBLE);

        ll_footer = (LinearLayout) this.findViewById(R.id.ll_footer);

        eats_img = (ImageView) this.findViewById(R.id.eats_img);
        loves_img = (ImageView) this.findViewById(R.id.loves_img);
        listens_img = (ImageView) this.findViewById(R.id.listens_img);

        videoView1 = (ScalableVideoView) findViewById(R.id.videoView1);
        mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView1);

        videoView2 = (ScalableVideoView) findViewById(R.id.videoView2);
        mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView2);

        videoView3 = (ScalableVideoView) findViewById(R.id.videoView3);
        mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView3);


        imgfull = (ImageView) this.findViewById(R.id.imgfull);


        slider1 = (SliderLayout) findViewById(R.id.slider);

        mDemoSliderfull = (SliderLayout) findViewById(R.id.sliderfull);

        slider2 = (SliderLayout) findViewById(R.id.slider2);

        mDemoSliderfull2 = (SliderLayout) findViewById(R.id.sliderfull2);
        slider3 = (SliderLayout) findViewById(R.id.slider3);

        mDemoSliderfull3 = (SliderLayout) findViewById(R.id.sliderfull3);

        eats_img.setImageResource(R.mipmap.button_eat);


        mPager = (ViewPager) findViewById(R.id.pager);

        iv1 = (ImageView)findViewById(R.id.iv1) ;
        iv2 = (ImageView)findViewById(R.id.iv2) ;
        iv3 = (ImageView)findViewById(R.id.iv3) ;


        List_File_1 = new ArrayList<>();
        List_File_2 = new ArrayList<>();
        List_File_3 = new ArrayList<>();


        Log.e("TAG", "save data = "+sharedPref.get_Login_Info());

        try {

            JSONObject jsonObject = new JSONObject(sharedPref.get_Login_Info());

        }catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "Exception = "+e.toString());
        }


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


        ViewPager_OnPageChange();

        Action_FullVideo();

        setDeault_Video_Action();

        //Log.d(All_Constants_Urls.TAG, "Info - "+sharedPref.get_Profile_Videos());

        //getProfileData();


        if (sharedPref.get_Profile_Videos() == null){

            getProfileData();

        }else {

            progressDialog.show();

            set_Video_Data();

            setImages(sharedPref.get_Profile_Videos());

            if (profile_vp_adapter == null){

                profile_vp_adapter = new Profile_Vp_Adapter_(ProfileActivity.this, imgEditUser, List_Text, List_Image, List_Image_Uri,iv1,iv2,iv3,image1Play,image2Play,image3Play);
                mPager.setAdapter(profile_vp_adapter);

            }else {

                profile_vp_adapter.notifyDataSetChanged();
            }

            runViewPager();


            linearfull.setVisibility(View.VISIBLE);

            progressDialog.dismiss();
        }




        /*vw_left.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {


                Log.d("drag", "onLongClick: ");

                which_view="left";

                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        relVideo2);
                relVideo2.startDrag(data, shadowBuilder, relVideo2, 0);
                return false;
            }
        });*/


        /*vw_right.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                which_view="right";
                Log.d("drag", "onLongClick: ");

                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        relVideo3);
                relVideo3.getRootView().startDrag(data, shadowBuilder, relVideo3, 0);
                return false;
            }
        });*/

       // relVideo1.setOnDragListener(new MyDragListener());
    }

    public void setDeault_Video_Action(){

        rl_2nd = (RelativeLayout) findViewById(R.id.rl_2nd);
        rl_3rd = (RelativeLayout) findViewById(R.id.rl_3rd);

        rl_2nd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (global_class.isEdit){

                    Default_Video_Dialog(2);

                }else {

                    Toast.makeText(getApplicationContext(), "Please enable Edit mode", Toast.LENGTH_SHORT).show();
                }


                return false;
            }
        });

        rl_3rd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                if (global_class.isEdit){

                    Default_Video_Dialog(3);

                }else {

                    Toast.makeText(getApplicationContext(), "Please enable Edit mode", Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });



    }

    public void Default_Video_Dialog(final int pos){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("HBHG");
        // set dialog message
        alertDialogBuilder
                .setMessage("Sure to make this video as profile video?")
                .setCancelable(false)
                .setPositiveButton("YES",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        if (pos == 2){

                            if (is_video_2){

                                setDefaultVideo(All_Constants_Urls.video_2);
                            }else {

                                setDefaultVideo(All_Constants_Urls.image_video_2);
                            }

                        }else if (pos == 3){

                            if (is_video_3){

                                setDefaultVideo(All_Constants_Urls.video_3);
                            }else {

                                setDefaultVideo(All_Constants_Urls.image_video_3);
                            }

                        }

                    }
                })
                .setNegativeButton("NO",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void Action_FullVideo(){

        image1Play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.d("tag", "onClick: ");
                seleced_viedo_layout = 1;
                if (global_class.isEdit) {
                    //  iv1.setVisibility(View.VISIBLE);

                    customDialogVideo();


                } else {
                    if(is_video_1){
                        is_video_1=true;

                        if (VideoUri1 != null){

                            Intent intent = new Intent(ProfileActivity.this,View_Video.class);
                            intent.putExtra("video_view",VideoUri1.toString());
                            startActivity(intent);

                        }else if (Video_Url_1 != null){

                            Intent intent = new Intent(ProfileActivity.this,View_Video.class);
                            intent.putExtra("video_view",Video_Url_1);
                            startActivity(intent);

                        }

                    }else {
                        is_video_1=false;
                        videoEffectf1();
                        mDemoSliderfull.setVisibility(View.VISIBLE);
                        // new CheckTypesTask().execute();
                        fullplay = true;
                        linearfull.setVisibility(View.INVISIBLE);
                        ll_footer.setVisibility(View.INVISIBLE);
                    }




                }
            }
        });
        image2Play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag", "onClick: ");

                seleced_viedo_layout = 2;

                if (global_class.isEdit) {

                    customDialogVideo();

                } else {

                    if(is_video_2){
                        is_video_2=true;

                        if (VideoUri2 != null){

                            Intent intent = new Intent(ProfileActivity.this,View_Video.class);
                            intent.putExtra("video_view",VideoUri2.toString());
                            startActivity(intent);
                        }else if (Video_Url_2 != null){

                            Intent intent = new Intent(ProfileActivity.this,View_Video.class);
                            intent.putExtra("video_view",Video_Url_2);
                            startActivity(intent);

                        }

                    }else {
                        is_video_2=false;
                        videoEffectf2();
                        mDemoSliderfull2.setVisibility(View.VISIBLE);
                        fullplay = true;

                        linearfull.setVisibility(View.INVISIBLE);
                        ll_footer.setVisibility(View.INVISIBLE);
                    }

                }
            }
        });
        image3Play.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag", "onClick: ");

                seleced_viedo_layout = 3;
                if (global_class.isEdit) {
                    //  iv3.setVisibility(View.VISIBLE);
                    customDialogVideo();

                } else {
                    if(is_video_3){
                        is_video_3=true;

                        if (VideoUri3 != null){

                            Intent intent = new Intent(ProfileActivity.this,View_Video.class);
                            intent.putExtra("video_view",VideoUri3.toString());
                            startActivity(intent);

                        }else if (Video_Url_3 != null){

                            Intent intent = new Intent(ProfileActivity.this,View_Video.class);
                            intent.putExtra("video_view",Video_Url_3);
                            startActivity(intent);

                        }

                    }else {
                        is_video_3=false;
                        videoEffectf3();
                        mDemoSliderfull3.setVisibility(View.VISIBLE);
                        fullplay = true;

                        linearfull.setVisibility(View.INVISIBLE);
                        ll_footer.setVisibility(View.INVISIBLE);
                    }

                }
            }
        });



    }


    public void ViewPager_OnPageChange(){

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


    private class MyDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.d("jwd", "onDrag:started");
                    v.setVisibility(View.VISIBLE);
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.d("jwd", "onDrag: entered");
                    switchnow=true;
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.d("jwd", "onDrag: exit");

                    switchnow=false;

                    break;
                case DragEvent.ACTION_DROP:
                    Log.d("jwd", "onDrag: drop ");
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();
                    view.setVisibility(View.VISIBLE);
                    if (switchnow){

                        if (which_view.matches("left"))
                        {

                        relVideo2.setBackgroundResource(midid);
                        relVideo1.setBackgroundResource(leftid);

                            tempid=leftid;
                            leftid=midid;
                            midid=tempid;
                    }else {

                            relVideo3.setBackgroundResource(midid);
                            relVideo1.setBackgroundResource(rightid);

                            tempid=rightid;
                            rightid=midid;
                            midid=tempid;

                         }
                    }
                    break;
                case DragEvent.ACTION_DRAG_ENDED:

                    Log.d("jwd", "onDrag: end ");

                    v.setScaleX(1);
                    v.setScaleY(1);

                default:
                    break;
            }
            return true;
        }
    }


    private File getFile(Bitmap photo) {
        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
        Uri tempUri = getImageUri(getApplicationContext(), photo);

        // CALL THIS METHOD TO GET THE ACTUAL PATH
        File finalFile = new File(getRealPathFromURI(tempUri));
        return finalFile;
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = Images.Media.insertImage(inContext.getContentResolver(),
                inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result = "";
        try {
            Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
            if (cursor == null) { // Source is Dropbox or other similar local file path
                result = contentURI.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx); // Exception raised HERE
                cursor.close(); }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    private void setBottom() {
        ImageView imgView1 = (ImageView) this.findViewById(R.id.imgView1);
        ImageView imgView2 = (ImageView) this.findViewById(R.id.imgView2);
        ImageView imgView3 = (ImageView) this.findViewById(R.id.imgView3);
        ImageView imgView4 = (ImageView) this.findViewById(R.id.imgView4);
        ImageView imgView5 = (ImageView) this.findViewById(R.id.imgView5);

        RelativeLayout relChat = (RelativeLayout) this
                .findViewById(R.id.relChat);
        RelativeLayout relMain = (RelativeLayout) this
                .findViewById(R.id.relMain);
        RelativeLayout relFav = (RelativeLayout) this.findViewById(R.id.relFav);
        RelativeLayout relNotification = (RelativeLayout) this
                .findViewById(R.id.relNotification);


        imgView1.setImageResource(R.mipmap.profle_purple);
        imgView2.setImageResource(R.mipmap.g_chat_48);
        imgView3.setImageResource(R.mipmap.g_sunglass_white);
        imgView4.setImageResource(R.mipmap.g_fav_48);
        imgView5.setImageResource(R.mipmap.g_notify_48);


        imgView2.setColorFilter(getResources().getColor(R.color.tint_color));
        imgView3.setColorFilter(getResources().getColor(R.color.tint_color));
        imgView4.setColorFilter(getResources().getColor(R.color.tint_color));
        imgView5.setColorFilter(getResources().getColor(R.color.tint_color));


        relChat.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ProfileActivity.this,
                        Chat_screen_new.class);
                startActivity(intent);
                ProfileActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        relMain.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ProfileActivity.this,
                        MainActivity.class);
                startActivity(intent);
                ProfileActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        relFav.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ProfileActivity.this,
                        FavoriteActivity.class);
                startActivity(intent);
                ProfileActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        relNotification.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ProfileActivity.this,
                        NotificationActivity.class);
                startActivity(intent);
                ProfileActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
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


    public void videoEffect1(){
        slider1.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        //slider1.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider1.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        slider1.setCustomAnimation(new DescriptionAnimation());
        slider1.setDuration(2000);
        slider1.moveNextPosition(true);
        slider1.addOnPageChangeListener(this);
        slider1.startAutoCycle();

        slider1.setVisibility(View.VISIBLE);
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

    public void videoEffectf1(){
        mDemoSliderfull.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        //mDemoSliderfull.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSliderfull.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        mDemoSliderfull.setCustomAnimation(new DescriptionAnimation());
        mDemoSliderfull.setDuration(2000);
        mDemoSliderfull.moveNextPosition(true);
        mDemoSliderfull.addOnPageChangeListener(this);
        mDemoSliderfull.startAutoCycle();

       // mDemoSliderfull.setVisibility(View.VISIBLE);
    }

    public void videoEffectf2(){
        mDemoSliderfull2.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        //slider1.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSliderfull2.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        mDemoSliderfull2.setCustomAnimation(new DescriptionAnimation());
        mDemoSliderfull2.setDuration(2000);
        mDemoSliderfull2.moveNextPosition(true);
        mDemoSliderfull2.addOnPageChangeListener(this);
        mDemoSliderfull2.startAutoCycle();

       // mDemoSliderfull2.setVisibility(View.VISIBLE);
    }

    public void videoEffectf3(){
        mDemoSliderfull3.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        //slider1.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSliderfull3.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);
        mDemoSliderfull3.setCustomAnimation(new DescriptionAnimation());
        mDemoSliderfull3.setDuration(2000);
        mDemoSliderfull3.moveNextPosition(true);
        mDemoSliderfull3.addOnPageChangeListener(this);
        mDemoSliderfull3.startAutoCycle();

      //  mDemoSliderfull3.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        //slider1.stopAutoCycle();
        mDemoSliderfull.stopAutoCycle();
        mDemoSliderfull2.stopAutoCycle();
        mDemoSliderfull3.stopAutoCycle();
        super.onStop();
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
        slider1.setPresetTransformer(trans1[randomNum]);
        slider2.setPresetTransformer(trans2[randomNum]);
        slider3.setPresetTransformer(trans3[randomNum]);
        mDemoSliderfull.setPresetTransformer(trans1[randomNum]);
        mDemoSliderfull2.setPresetTransformer(trans2[randomNum]);
        mDemoSliderfull3.setPresetTransformer(trans3[randomNum]);
       // Log.d("Slider Demo", "Page Changed: " + position + "random" + randomNum1);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        //Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    private void openOptionMenuVideo(int type) {
        bclickvideo1 = false;
        bclickvideo2 = false;
        bclickvideo3 = false;
        switch (type) {
            case 1:
                bclickvideo1 = true;
                break;
            case 2:
                bclickvideo2 = true;
                break;
            case 3:
                bclickvideo3 = true;
                break;
            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {

        if (fullplay){

            if (fullplay) {
                mDemoSliderfull.setVisibility(View.INVISIBLE);
                mDemoSliderfull2.setVisibility(View.INVISIBLE);
                mDemoSliderfull3.setVisibility(View.INVISIBLE);

                linearfull.setVisibility(View.VISIBLE);
                //  rel1.setVisibility(View.VISIBLE);
                ll_footer.setVisibility(View.VISIBLE);
                fullplay = false;
            } else {
                imgfull.setVisibility(View.INVISIBLE);
                linearfull.setVisibility(View.VISIBLE);
                imgplay = false;
            }

        }else {

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finish();
            }else {

                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
            }

        }



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //case REQUEST_PHOTO_SELECTION:
        title_swoing_text = " ";
        if (resultCode == RESULT_OK && requestCode == RESULT_LOAD_IMAGE) {

            Uri uri = data.getData();

            String selectedImage = uri.toString();

            Log.d(All_Constants_Urls.TAG, "selectedImage = " + selectedImage);

            HashMap<String, String> map;
            map = List_Image_Uri.get(page_position);

            Log.d(All_Constants_Urls.TAG, "map before = " + map);

            if (position_image_key.equals("p1" + page_position)) {


                List_Image_Uri.get(page_position).put(position_image_key, selectedImage);


            } else if (position_image_key.equals("p2" + page_position)) {


                List_Image_Uri.get(page_position).put(position_image_key, selectedImage);

            } else if (position_image_key.equals("p3" + page_position)) {


                List_Image_Uri.get(page_position).put(position_image_key, selectedImage);

            }

          //  Log.d(All_Constants_Urls.TAG, "page_position = " + page_position);

          //  Log.d(All_Constants_Urls.TAG, "List_Image_Uri = " + List_Image_Uri);


            switch (page_position) {
                case 0:
                    Dialog_Edittext(page_position, title_swoing_text,selectedImage);
                    break;

                case 1:
                    Dialog_Edittext(page_position, title_swoing_text,selectedImage);
                    break;

                case 2:
                    Dialog_Edittext(page_position, title_swoing_text,selectedImage);
                    break;

            }


        }
        else if (resultCode == RESULT_OK &&
                requestCode == CAMERA_REQUEST) {

            Uri uri = data.getData();

            String selectedImage = uri.toString();

           // Log.d(All_Constants_Urls.TAG, "photoUri: "+selectedImage);

            HashMap<String, String> map;
            map = List_Image_Uri.get(page_position);

           // Log.d(All_Constants_Urls.TAG, "map before = " + map);

            if (position_image_key.equals("p1" + page_position)) {

                List_Image_Uri.get(page_position).put(position_image_key, selectedImage);

            } else if (position_image_key.equals("p2" + page_position)) {

                List_Image_Uri.get(page_position).put(position_image_key, selectedImage);

            } else if (position_image_key.equals("p3" + page_position)) {

                List_Image_Uri.get(page_position).put(position_image_key, selectedImage);

            }

           // Log.d(All_Constants_Urls.TAG, "map after = " + map);


            switch (page_position) {
                case 0:

                    Dialog_Edittext(page_position, title_swoing_text,selectedImage);

                    break;

                case 1:

                    Dialog_Edittext(page_position, title_swoing_text,selectedImage);

                    break;

                case 2:

                    Dialog_Edittext(page_position, title_swoing_text,selectedImage);

                    break;

            }



        } else if(resultCode == RESULT_OK &&
                requestCode == GOOGLE_PHOTO_SELECTION){

            // getFileUri();

          //  Log.d(All_Constants_Urls.TAG, "data from GOOGLE_PHOTO_SELECTION : "+data.getExtras().getString("sourceurl"));

            String selectedImage = data.getExtras().getString("sourceurl");
            title_swoing_text = data.getExtras().getString("text_string");

            HashMap<String, String> map;
            map = List_Image_Uri.get(page_position);

           // Log.d(All_Constants_Urls.TAG, "map before = " + map);

            if (position_image_key.equals("p1" + page_position)) {

                List_Image_Uri.get(page_position).put(position_image_key, selectedImage);

            } else if (position_image_key.equals("p2" + page_position)) {

                List_Image_Uri.get(page_position).put(position_image_key, selectedImage);

            } else if (position_image_key.equals("p3" + page_position)) {

                List_Image_Uri.get(page_position).put(position_image_key, selectedImage);

            }

           // Log.d(All_Constants_Urls.TAG, "map after = " + map);

            switch (page_position) {
                case 0:

                    Dialog_Edittext(page_position, title_swoing_text, selectedImage);

                    break;

                case 1:

                    Dialog_Edittext(page_position, title_swoing_text,selectedImage);

                    break;

                case 2:

                    Dialog_Edittext(page_position, title_swoing_text,selectedImage);

                    break;

            }


        } else if (resultCode == RESULT_OK &&
                requestCode == RESULT_FB_INSTA_Image){

              //  Log.d(All_Constants_Urls.TAG, "data from RESULT_FB_INSTA_Image : "+data.getExtras().getString("sourceurl"));

                String selectedImage = data.getExtras().getString("sourceurl");
                title_swoing_text = data.getExtras().getString("text_string");

                HashMap<String, String> map;
                map = List_Image_Uri.get(page_position);

              //  Log.d(All_Constants_Urls.TAG, "map before = " + map);

                if (position_image_key.equals("p1" + page_position)) {

                    List_Image_Uri.get(page_position).put(position_image_key, selectedImage);

                } else if (position_image_key.equals("p2" + page_position)) {

                    List_Image_Uri.get(page_position).put(position_image_key, selectedImage);

                } else if (position_image_key.equals("p3" + page_position)) {

                    List_Image_Uri.get(page_position).put(position_image_key, selectedImage);

                }

              //  Log.d(All_Constants_Urls.TAG, "map after = " + map);

                switch (page_position) {
                    case 0:

                        Dialog_Edittext(page_position, title_swoing_text, selectedImage);

                        break;

                    case 1:

                        Dialog_Edittext(page_position, title_swoing_text,selectedImage);

                        break;

                    case 2:

                        Dialog_Edittext(page_position, title_swoing_text,selectedImage);

                        break;

                }


            }

        else if (requestCode == ConstantsCustomGallery.REQUEST_CODE &&
                resultCode == Activity.RESULT_OK && data != null) {

            //The array list has the image paths of the selected Selected_Gallery_Images

            if(seleced_viedo_layout == 1) {
                slider1.removeAllSliders();

                images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);

                is_video_1 = false;

                new CheckTypesTask1().execute();


            }else if(seleced_viedo_layout == 2){

                slider2.removeAllSliders();

                images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);

                is_video_2 = false;

                new CheckTypesTask2().execute();


            }else if(seleced_viedo_layout == 3){

                slider3.removeAllSliders();

                images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);

                is_video_3 = false;

                new CheckTypesTask3().execute();


            }

        }
        else if (requestCode == VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                // Toast.makeText(this, "Video has been saved to:\n" + data.getData(), Toast.LENGTH_LONG).show();

                if (data == null){

                   // Log.d(All_Constants_Urls.TAG, "Video path = "+Captured_Video_Path);

                    Uri video_uri = Uri.fromFile(new File(Captured_Video_Path));


                    if(seleced_viedo_layout == 1){


                        is_video_1 = true;

                        VideoUri1 = video_uri;

                        Play_Video(VideoUri1.toString());


                    }else if (seleced_viedo_layout == 2){

                        is_video_2 = true;

                        VideoUri2 = video_uri;

                        Play_Video2(VideoUri2.toString());


                    }else if (seleced_viedo_layout == 3){

                        is_video_3 = true;

                        VideoUri3 = video_uri;

                        Play_Video3(VideoUri3.toString());

                    }

                    addVideoConfirmationDialog();

                }else {

                   // Log.d(All_Constants_Urls.TAG, "Video path = "+data.getData());


                    if(seleced_viedo_layout == 1){

                        is_video_1 = true;

                        VideoUri1 = data.getData();

                        Play_Video(VideoUri1.toString());

                    }else if (seleced_viedo_layout == 2){

                        is_video_2 = true;

                        VideoUri2 = data.getData();


                        Play_Video2(VideoUri2.toString());

                    }else if (seleced_viedo_layout == 3){

                        is_video_3 = true;

                        VideoUri3 = data.getData();


                        Play_Video3(VideoUri3.toString());

                    }

                    addVideoConfirmationDialog();

                }

            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Failed to record video",
                        Toast.LENGTH_LONG).show();
            }

        }
        else if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK){

            Uri selectedImageUri = data.getData();

            // OI FILE Manager
            String filemanagerstring = selectedImageUri.getPath();

          //  Log.d("TAG", "path = "+filemanagerstring);
            Intent intent = new Intent(ProfileActivity.this, TrimmerActivity.class);
            intent.putExtra("path", FileUtils.getPath(this, selectedImageUri));
            startActivityForResult(intent, TRIMMER_ACTIVITY_RESULT_CODE);


        } else if (requestCode == TRIMMER_ACTIVITY_RESULT_CODE && resultCode == RESULT_OK){

            String path_uri = data.getExtras().getString("uri");
            Uri myUri = Uri.parse(path_uri);


            if(seleced_viedo_layout == 1){

                VideoUri1 = myUri;

                is_video_1 = true;

                Play_Video(VideoUri1.toString());

            }else if (seleced_viedo_layout == 2){

                VideoUri2 = myUri;

                is_video_2 = true;

                Play_Video2(VideoUri2.toString());

            }else if (seleced_viedo_layout == 3){

                VideoUri3 = myUri;

                is_video_3 = true;

                Play_Video3(VideoUri3.toString());

            }

            addVideoConfirmationDialog();

        }else
        if (requestCode == ALL_MEDIA_ACTIVITY_RESULT_CODE && resultCode == RESULT_OK){

            String check = data.getExtras().getString("key");

            if (check.equals("image")){

                /*Intent intent = new Intent(ProfileActivity.this, Image_Filter_for_URL.class);
                intent.putExtra("image_url", global_class.Selected_Image_Url.get(0));
                intent.putStringArrayListExtra("image_list", global_class.Selected_Image_Url);
                startActivityForResult(intent, FILTER_URL_ACTIVITY_RESULT_CODE);*/


                //set_All_Meadia_Images();

                new AsyncTask_Download().execute();


            }
            else if (check.equals("video")){

                String video = data.getExtras().getString("file");

                Intent intent = new Intent(ProfileActivity.this, TrimmerActivity.class);
                intent.putExtra("path", video);
                startActivityForResult(intent, TRIMMER_ACTIVITY_RESULT_CODE);

            }

        }

    }


    @Override
    protected void onResume() {

        slider1.startAutoCycle();
        slider2.startAutoCycle();
        slider3.startAutoCycle();

        //
        Log.e("TAG", "Call onResume");

        if (VideoUri1 != null){
            Play_Video(VideoUri1.toString());
        }else if (Video_Url_1 != null){
            Play_Video(Video_Url_1);
        }

        //
        if (VideoUri2 != null){
            Play_Video2(VideoUri2.toString());
        }else if (Video_Url_2 != null){
            Play_Video2(Video_Url_2);
        }

        //
        if (VideoUri3 != null){
            Play_Video3(VideoUri3.toString());
        }else if (Video_Url_3 != null){
            Play_Video3(Video_Url_3);
        }


        super.onResume();
    }

    @Override
    protected void onPause() {

        videoView1.stopPlayback();
        videoView2.stopPlayback();
        videoView3.stopPlayback();

        slider1.stopAutoCycle();
        slider2.stopAutoCycle();
        slider3.stopAutoCycle();


        super.onPause();
    }

    public void Play_Video(final String videoUri){

        slider1.setVisibility(View.GONE);
        mDemoSliderfull.setVisibility(View.GONE);
        videoView1.setVisibility(View.VISIBLE);

        if (videoUri.startsWith("http")){
            videoView1.setVideoURI(Uri.parse(videoUri));

        }else {

            videoView1.setVideoURI(Uri.parse(videoUri));
        }

        videoView1.setMediaController(null);
        videoView1.requestFocus();
        videoView1.start();

        videoView1.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {

                mp.setVolume(0, 0);
            }
        });

        final String urll = videoUri;
        videoView1.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                if (videoUri.startsWith("http")){
                    videoView1.setVideoURI(Uri.parse(urll));
                }else {
                    videoView1.setVideoURI(Uri.parse(videoUri));
                }

                videoView1.requestFocus();
                videoView1.start();
            }
        });

        videoView1.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d(All_Constants_Urls.TAG, "setOnErrorListener ");
                return true;
            }
        });

        List_File_1.clear();

    }

    public void Play_Video2(final String videoUri){

        slider2.setVisibility(View.GONE);
        mDemoSliderfull2.setVisibility(View.GONE);
        videoView2.setVisibility(View.VISIBLE);
        mediaController.setVisibility(View.GONE);

        if (videoUri.startsWith("http")){
            videoView2.setVideoURI(Uri.parse(videoUri));

        }else {

            videoView2.setVideoURI(Uri.parse(videoUri));
        }

        videoView2.setMediaController(mediaController);
        videoView2.requestFocus();
        videoView2.start();

        videoView2.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setVolume(0, 0);
            }
        });

        final String urll = videoUri;
        videoView2.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (videoUri.startsWith("http")){
                    videoView2.setVideoURI(Uri.parse(urll));
                }else {
                    videoView2.setVideoURI(Uri.parse(videoUri));
                }
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

        List_File_2.clear();



    }

    public void Play_Video3(final String videoUri){

        slider3.setVisibility(View.GONE);
        mDemoSliderfull3.setVisibility(View.GONE);
        videoView3.setVisibility(View.VISIBLE);

        mediaController.setVisibility(View.GONE);

        if (videoUri.startsWith("http")){

            videoView3.setVideoURI(Uri.parse(videoUri));

        }else {

            videoView3.setVideoURI(Uri.parse(videoUri));
        }

        videoView3.setMediaController(mediaController);
        videoView3.requestFocus();
        videoView3.start();

        videoView3.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setVolume(0, 0);
            }
        });

        final String urll = videoUri;
        videoView3.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (videoUri.startsWith("http")){
                    videoView3.setVideoURI(Uri.parse(urll));
                }else {
                    videoView3.setVideoURI(Uri.parse(videoUri));
                }
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

        List_File_3.clear();


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

    @Override
    public void onHandleSelection(int position, String image_key, String et_text, String text_key) {

        position_image_key = image_key;
        page_position = position;

        Text_key = text_key;

      //  Log.d(All_Constants_Urls.TAG, "position = "+position);
     //   Log.d(All_Constants_Urls.TAG, "image position = "+image_key);
     //   Log.d(All_Constants_Urls.TAG, "Text_key = "+Text_key);

    }

    public void runViewPager(){

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


    public void Dialog_Edittext(final int page_pos, String str_hint, String selectedImage){

        Log.d(All_Constants_Urls.TAG, "Dialog_Edittext = "+page_pos+"\n "+str_hint);

        customize_Dia_Name = new Dialog_Name(ProfileActivity.this, page_pos, str_hint, selectedImage);
        customize_Dia_Name.edit1.setText(title_swoing_text);
        customize_Dia_Name.setOnDismissListener(new DialogInterface.OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                // TODO Auto-generated method stub
                if (customize_Dia_Name.btnclick == 1) {
                    final String et_value = customize_Dia_Name.edit1.getText().toString().trim();


                    if (Text_key.equals("t1" + page_pos)) {

                        List_Text.get(page_position).put(Text_key, et_value);

                    } else if (Text_key.equals("t2" + page_pos)) {

                        List_Text.get(page_position).put(Text_key, et_value);

                    } else if (Text_key.equals("t3" + page_pos)) {

                        List_Text.get(page_position).put(Text_key, et_value);

                    }


                    profile_vp_adapter.notifyDataSetChanged();


                    if (page_position == 0){

                        addFood();

                    }else if (page_position == 1){

                        addInterest();

                    }else if (page_position == 2){

                        addMusic();

                    }

                }
            }
        });
        customize_Dia_Name.show();


    }


    public void customDialogVideo() {

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


        insta_rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                if (sharedPref.isLOGIN_INSTA()) {

                    global_class.Selected_Image_Url.clear();

                    Intent intent = new Intent(ProfileActivity.this, AllMediaFiles_2.class);
                    intent.putExtra("key", "insta");
                    startActivityForResult(intent, ALL_MEDIA_ACTIVITY_RESULT_CODE);
                    alertDialog.dismiss();

                } else {

                   // Toast.makeText(getApplicationContext(), "You are not login with Instagram", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(ProfileActivity.this, Facebook_Insta_Login.class);
                    intent.putExtra("key", "insta");
                    startActivity(intent);
                    alertDialog.dismiss();
                }

            }
        });

        rl_fb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sharedPref.isLOGIN_FB()) {

                    global_class.Selected_Image_Url.clear();

                    Intent intent = new Intent(ProfileActivity.this, AllMediaFiles_2.class);
                    intent.putExtra("key", "fb");
                    startActivityForResult(intent, ALL_MEDIA_ACTIVITY_RESULT_CODE);
                    alertDialog.dismiss();

                } else {

                  //  Toast.makeText(getApplicationContext(), "You are not login with Facebook", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(ProfileActivity.this, Facebook_Insta_Login.class);
                    intent.putExtra("key", "fb");
                    startActivity(intent);
                    alertDialog.dismiss();

                }

            }
        });

        attach_gallery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(ProfileActivity.this, AlbumSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 7); // set limit for image selection
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                alertDialog.dismiss();


            }
        });

        rl_video.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                //is_video = true;
                alertDialog.dismiss();

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ProfileActivity.this);
                // ...Irrelevant code_uri_filter for customizing the buttons and title
                View dialogView1 = inflater.inflate(R.layout.add_video_dialog, null);
                dialogBuilder.setView(dialogView1);

                LinearLayout gallery_video = (LinearLayout) dialogView1.findViewById(R.id.gallery_video);
                LinearLayout camera_video = (LinearLayout) dialogView1.findViewById(R.id.camera_video);


                alertDialog2 = dialogBuilder.create();
                alertDialog2.show();


                gallery_video.setOnClickListener(new OnClickListener() {
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

                camera_video.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {

                          //  slider1.removeAllSliders();

                            File mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/My_video_"+System.currentTimeMillis()+".mp4");


                            Log.d(All_Constants_Urls.TAG, "mediaFile = " + mediaFile);




                            if (mediaFile != null) {

                                Captured_Video_Path = mediaFile.toString();

                                Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
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


    //////////////////////////////////////


    private class CheckTypesTask1 extends AsyncTask<Void, Void, Void> {
        ProgressDialog asyncDialog = new ProgressDialog(ProfileActivity.this);
        String typeStatus;
        Bitmap src;

        @Override
        protected void onPreExecute() {
            //set message of the dialog
            asyncDialog.setIndeterminate(false);
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            asyncDialog.setMessage("Fetching images...");
            asyncDialog.setCanceledOnTouchOutside(false);
            asyncDialog.setProgressNumberFormat(null);
            asyncDialog.setMax(100);
            asyncDialog.show();

            List_File_1.clear();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            for (int i = 0; i < images.size(); i++){

                Uri selectedImage = Uri.fromFile(new File(images.get(i).path.toString()));

                src = utility.decodeUri_640(selectedImage);

                saveBitmap(src, "P_IMG_1_", i);

                int differance = images.size() - i;
                if (differance > 0)
                    asyncDialog.setProgress(100/differance);

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            //hide the dialog
            slider1.removeAllSliders();
            mDemoSliderfull.removeAllSliders();

            for (int i = 0; i < List_File_1.size(); i++) {

                File uri = List_File_1.get(i);

                Log.d("TAG", "Image path 1 = "+List_File_1.get(i));

                TextSliderView textSliderView1 = new TextSliderView(ProfileActivity.this);
                // initialize a SliderLayout
                textSliderView1
                        .image(uri)
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(ProfileActivity.this);

                slider1.addSlider(textSliderView1);
                mDemoSliderfull.addSlider(textSliderView1);

            }

            slider1.setVisibility(View.VISIBLE);
            videoView1.stopPlayback();
            videoView1.setVisibility(View.GONE);

            VideoUri1 = null;


            iv1.setVisibility(View.GONE);

            videoEffect1();
            videoEffectf1();


            asyncDialog.dismiss();

            addVideoConfirmationDialog();


            super.onPostExecute(result);
        }

    }


    private class CheckTypesTask2 extends AsyncTask<Void, Void, Void> {
        ProgressDialog asyncDialog = new ProgressDialog(ProfileActivity.this);
        String typeStatus;
        Bitmap src;

        @Override
        protected void onPreExecute() {
            //set message of the dialog
            asyncDialog.setIndeterminate(false);
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            asyncDialog.setMessage("Fetching images...");
            asyncDialog.setCanceledOnTouchOutside(false);
            asyncDialog.setMax(100);
            asyncDialog.show();

            List_File_2.clear();


            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            //don't touch dialog here it'll break the application
            //do some lengthy stuff like calling login webservice

            for (int i = 0; i < images.size(); i++){

                Uri selectedImage = Uri.fromFile(new File(images.get(i).path.toString()));

                src = utility.decodeUri_640(selectedImage);

                saveBitmap(src, "P_IMG_2_", i);


                int differance = images.size() - i;
                if (differance > 0)
                    asyncDialog.setProgress(100/differance);

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //hide the dialog

            slider2.removeAllSliders();
            mDemoSliderfull3.removeAllSliders();

            for (int i = 0; i < List_File_2.size(); i++) {

                Log.d("TAG", "Image path 2 = "+List_File_2.get(i));

                File uri = List_File_2.get(i);

                TextSliderView textSliderView1 = new TextSliderView(ProfileActivity.this);
                // initialize a SliderLayout
                textSliderView1
                        .image(uri)
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(ProfileActivity.this);

                slider2.addSlider(textSliderView1);
                mDemoSliderfull2.addSlider(textSliderView1);

            }

            slider2.setVisibility(View.VISIBLE);
            videoView2.stopPlayback();
            videoView2.setVisibility(View.GONE);

            VideoUri2 = null;

            iv2.setVisibility(View.GONE);

            videoEffect2();
            videoEffectf2();

            asyncDialog.dismiss();

            addVideoConfirmationDialog();

            super.onPostExecute(result);
        }

    }


    private class CheckTypesTask3 extends AsyncTask<Void, Void, Void> {
        ProgressDialog asyncDialog = new ProgressDialog(ProfileActivity.this);
        String typeStatus;
        Bitmap src;



        @Override
        protected void onPreExecute() {
            //set message of the dialog
            asyncDialog.setIndeterminate(false);
            asyncDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            asyncDialog.setMessage("Fetching images...");
            asyncDialog.setCanceledOnTouchOutside(false);
            asyncDialog.setMax(100);
            asyncDialog.show();

            List_File_3.clear();


            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            for (int i = 0; i < images.size(); i++){
               // Log.d("TAG", "image path = "+images.get(i).path.toString());

                Uri selectedImage = Uri.fromFile(new File(images.get(i).path.toString()));

                //src = BitmapFactory.decodeFile(Selected_Gallery_Images.get(i).path);

                src = utility.decodeUri_640(selectedImage);

                //Log.d("TAG", "image filter = "+selectedImage);


                saveBitmap(src, "P_IMG_3_", i);


                int differance = images.size() - i;
                if (differance > 0)
                    asyncDialog.setProgress(100/differance);

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            //hide the dialog

            slider3.removeAllSliders();
            mDemoSliderfull3.removeAllSliders();

            for (int i = 0; i < List_File_3.size(); i++) {

                Log.d("TAG", "Image path 3 = "+List_File_3.get(i));

                File uri = List_File_3.get(i);

                TextSliderView textSliderView1 = new TextSliderView(ProfileActivity.this);
                // initialize a SliderLayout
                textSliderView1
                        .image(uri)
                        .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                        .setOnSliderClickListener(ProfileActivity.this);

                slider3.addSlider(textSliderView1);
                mDemoSliderfull3.addSlider(textSliderView1);

            }

            slider3.setVisibility(View.VISIBLE);
            videoView3.stopPlayback();
            videoView3.setVisibility(View.GONE);

          /*  videoView_full.setVisibility(View.GONE);*/

            VideoUri3 = null;

            iv3.setVisibility(View.GONE);

            videoEffect3();
            videoEffectf3();

            asyncDialog.dismiss();


            addVideoConfirmationDialog();

            super.onPostExecute(result);
        }

    }


    private class AsyncTask_Download extends AsyncTask<Void, Void, Void> {

        Utility utility;



        @Override
        protected void onPreExecute() {

            utility = new Utility(context);


            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            if (seleced_viedo_layout == 1){

                List_File_1.clear();

                for (int i = 0; i < global_class.Selected_Image_Url.size(); i++){

                    String file = utility.SaveImageFromUrl(global_class.Selected_Image_Url.get(i), "P_IMG_1_" , i);

                    Log.d(All_Constants_Urls.TAG, "P_IMG = "+file);

                    List_File_1.add(new File(getRealPathFromURI(Uri.parse(file))));

                }

            }else if (seleced_viedo_layout == 2){

                List_File_2.clear();

                for (int i = 0; i < global_class.Selected_Image_Url.size(); i++){

                    String file = utility.SaveImageFromUrl(global_class.Selected_Image_Url.get(i), "P_IMG_2_" , i);

                    Log.d(All_Constants_Urls.TAG, "P_IMG = "+file);

                    List_File_2.add(new File(getRealPathFromURI(Uri.parse(file))));

                }

            }else if (seleced_viedo_layout == 3){

                List_File_3.clear();

                for (int i = 0; i < global_class.Selected_Image_Url.size(); i++){

                    String file = utility.SaveImageFromUrl(global_class.Selected_Image_Url.get(i), "P_IMG_3_" , i);

                    Log.d(All_Constants_Urls.TAG, "P_IMG = "+file);

                    List_File_3.add(new File(getRealPathFromURI(Uri.parse(file))));

                }

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            set_All_Meadia_Images();

            super.onPostExecute(result);
        }

    }


    private void saveBitmap(Bitmap bmp, String fileName, int code){


        if(seleced_viedo_layout == 1){
            try {

                // imgMain.setImageBitmap(bmp);
                File f = new File(utility.get_HBHG_Directory()  + fileName + code +".png");
                FileOutputStream fos = new FileOutputStream(f);
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);

                List_File_1.add(f);

                //Log.d("TAG", "List_File> "+List_File_1);

            }
            catch(Exception ex){
                ex.printStackTrace();
            }

        }else if(seleced_viedo_layout == 2){

            try {

                // imgMain.setImageBitmap(bmp);
                File f = new File(utility.get_HBHG_Directory()  + fileName + code +".png");
                FileOutputStream fos = new FileOutputStream(f);
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);

                List_File_2.add(f);
               // Log.d("TAG", "List_File_2> "+List_File_2);

            }
            catch(Exception ex){
                ex.printStackTrace();
            }

        }else if(seleced_viedo_layout == 3){

            try {

                // imgMain.setImageBitmap(bmp);
                File f = new File(utility.get_HBHG_Directory()  + fileName + code +".png");
                FileOutputStream fos = new FileOutputStream(f);
                bmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);

                List_File_3.add(f);
               // Log.d("TAG", "List_File_3> "+List_File_3);

            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }



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


    public void set_All_Meadia_Images(){


        if (seleced_viedo_layout == 1){

            iv1.setVisibility(View.GONE);

            slider1.removeAllSliders();
            mDemoSliderfull.removeAllSliders();

            for (int i = 0; i < List_File_1.size(); i++){

                File uri = List_File_1.get(i);

                TextSliderView textSliderView1 = new TextSliderView(ProfileActivity.this);
                // initialize a SliderLayout
                textSliderView1
                        .image(uri)
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(ProfileActivity.this);

                slider1.addSlider(textSliderView1);
                mDemoSliderfull.addSlider(textSliderView1);

            }

            videoEffect1();

            is_video_1 = false;
        }
        if (seleced_viedo_layout == 2){

            iv2.setVisibility(View.GONE);

            slider2.removeAllSliders();
            mDemoSliderfull2.removeAllSliders();


            for (int i = 0; i < List_File_2.size(); i++){

                File uri = List_File_2.get(i);

                TextSliderView textSliderView1 = new TextSliderView(ProfileActivity.this);
                // initialize a SliderLayout
                textSliderView1
                        .image(uri)
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(ProfileActivity.this);

                slider2.addSlider(textSliderView1);
                mDemoSliderfull2.addSlider(textSliderView1);

            }

            videoEffect2();

            is_video_2 = false;
        }
        if (seleced_viedo_layout == 3){

            iv3.setVisibility(View.GONE);

            slider3.removeAllSliders();
            mDemoSliderfull3.removeAllSliders();

            for (int i = 0; i < List_File_3.size(); i++){

                File uri = List_File_3.get(i);

                TextSliderView textSliderView1 = new TextSliderView(ProfileActivity.this);
                // initialize a SliderLayout
                textSliderView1
                        .image(uri)
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(ProfileActivity.this);

                slider3.addSlider(textSliderView1);
                mDemoSliderfull3.addSlider(textSliderView1);

            }
            videoEffect3();
            //videoEffectf3();

            is_video_3 = false;

        }


        addVideoConfirmationDialog();


    }


    private void save_Filter_Bitmap(Bitmap bmp, String fileName, int code){

            try {
                // imgMain.setImageBitmap(bmp);
                File f = new File(utility.get_HBHG_Directory() + fileName + code +".jpg");
                FileOutputStream fos = new FileOutputStream(f);
                bmp.compress(Bitmap.CompressFormat.JPEG, 80, fos);

                Log.d("TAG", "save image filter File = "+f);

                if (seleced_viedo_layout == 1){

                    List_File_1.add(f);

                }else if (seleced_viedo_layout == 2){

                    List_File_2.add(f);

                }else if (seleced_viedo_layout == 3){

                    List_File_3.add(f);

                }


            }
            catch(Exception ex){
                ex.printStackTrace();
            }



    }


    public void addVideoConfirmationDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Would you like to save this video?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.dismiss();

                        addProfileVideo();


                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


    public void addProfileVideo(){

       // progressDialog.show();

        String URL = All_Constants_Urls.ADD_PROFILE_VIDEO;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.setSSLSocketFactory(
                new SSLSocketFactory(Common.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());

        try{

            if (seleced_viedo_layout == 1){

                for (int i = 0; i < List_File_1.size(); i++){

                    File img_file = List_File_1.get(i);
                    params.put(All_Constants_Urls.image_video_1+"["+i+"]", img_file);

                }

                File file1 = new File(getRealPathFromURI(VideoUri1));
                params.put(All_Constants_Urls.video_1, file1);

            }else if (seleced_viedo_layout == 2){

                for (int i = 0; i < List_File_2.size(); i++){

                    File img_file = List_File_2.get(i);
                    params.put(All_Constants_Urls.image_video_2+"["+i+"]", img_file);

                }

                File file2 = new File(getRealPathFromURI(VideoUri2));
                params.put(All_Constants_Urls.video_2, file2);

            }else if (seleced_viedo_layout == 3){

                for (int i = 0; i < List_File_3.size(); i++){

                    File img_file = List_File_3.get(i);
                    params.put(All_Constants_Urls.image_video_3+"["+i+"]", img_file);

                }

                File file3 = new File(getRealPathFromURI(VideoUri3));
                params.put(All_Constants_Urls.video_3, file3);

            }


        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

        final String TAG = All_Constants_Urls.TAG;

        Log.d(TAG ,"AsyncHttpClient URL- " + URL);
        Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


       // client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , Common.DEFAULT_TIMEOUT);
        client.post(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "onSuccess- " + response.toString());

                if (response != null) {
                    try {

                        int success = response.optInt("success");
                        String message = response.optString("message");

                        if (success == 0){

                            Toast.makeText(context, "Something error when adding video\nPlease try again", Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();

                        }else
                        if (success == 1){

                           // Toast.makeText(context, "Successful", Toast.LENGTH_LONG).show();

                            JSONObject user_data = response.getJSONObject("user_data");

                            sharedPref.Save_Profile_Videos(user_data.toString());

                            //set_Video_Data();

                            /////

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

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(context).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });


    }

    public void set_Video_Data(){

        try {

            JSONObject main_object = new JSONObject(sharedPref.get_Profile_Videos());

            JSONObject object = main_object.getJSONObject(All_Constants_Urls.basic_info);

            String id = object.optString("id");
            sharedPref.save_Use_Id(id);


            String name = object.getString(All_Constants_Urls.name);
            String age = object.getString(All_Constants_Urls.age);
            String strGender = object.getString(All_Constants_Urls.gender);
            String sexual_orientation = object.getString(All_Constants_Urls.sexual_orientation);
            String city = object.getString(All_Constants_Urls.city);
            String country = object.getString(All_Constants_Urls.country);


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

            Log.d(All_Constants_Urls.TAG, "temp_text = "+temp_text);

            txtTitle.setText(temp_text);

            global_class.setOrientation(sexual_orientation);

            if(global_class.getOrientation().equals("Straight")){
                imgSexualOriante.setImageResource(R.mipmap.straight_male_w);

            }else if (global_class.getOrientation().equals("Gay")){
                imgSexualOriante.setImageResource(R.mipmap.gay_w);

            }else if (global_class.getOrientation().equals("Lesbian")){
                imgSexualOriante.setImageResource(R.mipmap.lesbian_w);

            }else if (global_class.getOrientation().equals("Bisexual")){
                imgSexualOriante.setImageResource(R.mipmap.bisexual_female_w);

            }


            /////////////////////


            Log.d(All_Constants_Urls.TAG, "set_Video_Data: "+object);

            if (object != null){


                JSONArray image_video = object.optJSONArray("image_video");
                if (image_video.length() != 0){

                    iv1.setVisibility(View.GONE);

                    slider1.removeAllSliders();
                    mDemoSliderfull.removeAllSliders();


                    for (int i = 0; i < image_video.length(); i++){

                        String url = image_video.getString(i);

                        TextSliderView textSliderView1 = new TextSliderView(ProfileActivity.this);
                        // initialize a SliderLayout
                        textSliderView1
                                .image(url)
                                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                                .setOnSliderClickListener(ProfileActivity.this);

                        slider1.addSlider(textSliderView1);
                        mDemoSliderfull.addSlider(textSliderView1);

                    }

                    videoEffect1();
                    // videoEffectf1();

                    is_video_1 = false;
                }

                String video = object.optString("video");
                if (video.length() != 0){
                    Video_Url_1 = video;
                    is_video_1 = true;
                    iv1.setVisibility(View.GONE);

                    Play_Video(video);

                }

                /////

                JSONArray second_image_video = object.optJSONArray("second_image_video");
                if (second_image_video.length() != 0){

                    iv2.setVisibility(View.GONE);

                    slider2.removeAllSliders();
                    mDemoSliderfull2.removeAllSliders();


                    for (int i = 0; i < second_image_video.length(); i++){

                        String url = second_image_video.getString(i);

                        TextSliderView textSliderView1 = new TextSliderView(ProfileActivity.this);
                        // initialize a SliderLayout
                        textSliderView1
                                .image(url)
                                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                                .setOnSliderClickListener(ProfileActivity.this);

                        slider2.addSlider(textSliderView1);
                        mDemoSliderfull2.addSlider(textSliderView1);

                    }

                    videoEffect2();
                    // videoEffectf2();

                    is_video_2 = false;
                }

                String second_video = object.optString("second_video");
                if (second_video.length() != 0){
                    Video_Url_2 = second_video;
                    is_video_2 = true;
                    iv2.setVisibility(View.GONE);

                    Play_Video2(second_video);

                }

                /////

                JSONArray third_image_video = object.optJSONArray("third_image_video");
                if (third_image_video.length() != 0){

                    iv3.setVisibility(View.GONE);

                    slider3.removeAllSliders();
                    mDemoSliderfull3.removeAllSliders();

                    for (int i = 0; i < third_image_video.length(); i++){

                        String url = third_image_video.getString(i);

                        TextSliderView textSliderView1 = new TextSliderView(ProfileActivity.this);
                        // initialize a SliderLayout
                        textSliderView1
                                .image(url)
                                .setScaleType(BaseSliderView.ScaleType.CenterCrop)
                                .setOnSliderClickListener(ProfileActivity.this);

                        slider3.addSlider(textSliderView1);
                        mDemoSliderfull3.addSlider(textSliderView1);

                    }
                    videoEffect3();
                    //videoEffectf3();

                    is_video_3 = false;
                }

                String third_video = object.optString("third_video");
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


    /////////////////////

    public void addFood(){


       // progressDialog.show();


        String URL = All_Constants_Urls.ADD_FOOD;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());

        HashMap<String, String> hasmap_image = new HashMap<>();
        hasmap_image = List_Image_Uri.get(page_position);

        Log.d(All_Constants_Urls.TAG, "hasmap_image = "+hasmap_image);


        HashMap<String, String> hasmap_title = new HashMap<>();
        hasmap_title = List_Text.get(page_position);

        Log.d(All_Constants_Urls.TAG, "hasmap_title = "+hasmap_title);


        HashMap<String, String> hasmap_ids = new HashMap<>();
        hasmap_ids = List_IDS.get(page_position);

        Log.d(All_Constants_Urls.TAG, "hasmap_ids = "+hasmap_ids);


        try{

            if (position_image_key.equals("p10")){

                params.put(All_Constants_Urls.photo_id, hasmap_ids.get("id10"));
                params.put(All_Constants_Urls.photo_title_1, hasmap_title.get("t10"));

                if (hasmap_image.get("p10").startsWith("http")){
                    params.put(All_Constants_Urls.photo_1, hasmap_image.get("p10"));
                }else {
                    Uri uri = Uri.parse(hasmap_image.get("p10"));
                    File file1 = new File(getRealPathFromURI(uri));

                    params.put(All_Constants_Urls.photo_1, file1);
                }

            }else if (position_image_key.equals("p20")){

                params.put(All_Constants_Urls.photo_id, hasmap_ids.get("id20"));
                params.put(All_Constants_Urls.photo_title_1, hasmap_title.get("t20"));

                if (hasmap_image.get("p20").startsWith("http")){
                    params.put(All_Constants_Urls.photo_1, hasmap_image.get("p20"));
                }else {
                    Uri uri = Uri.parse(hasmap_image.get("p20"));
                    File file1 = new File(getRealPathFromURI(uri));

                    params.put(All_Constants_Urls.photo_1, file1);
                }

            }else if (position_image_key.equals("p30")){

                params.put(All_Constants_Urls.photo_id, hasmap_ids.get("id30"));
                params.put(All_Constants_Urls.photo_title_1, hasmap_title.get("t30"));

                if (hasmap_image.get("p30").startsWith("http")){

                    params.put(All_Constants_Urls.photo_1, hasmap_image.get("p30"));

                }else {
                    Uri uri = Uri.parse(hasmap_image.get("p30"));
                    File file1 = new File(getRealPathFromURI(uri));

                    params.put(All_Constants_Urls.photo_1, file1);
                }

            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }


        final String TAG = All_Constants_Urls.TAG;

        Log.d(TAG ,"AsyncHttpClient URL- " + URL);
        Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());



        client.setSSLSocketFactory(
                new SSLSocketFactory(Common.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));

        client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , Common.DEFAULT_TIMEOUT_30);
        client.post(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "onSuccess- " + response.toString());

                if (response != null) {
                    try {


                        int success = response.optInt("success");

                        if (success == 0){

                            Toast.makeText(context, "Something error", Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();


                        }else
                        if (success == 1){


                            Toast.makeText(context, "Successful", Toast.LENGTH_LONG).show();

                            sharedPref.Save_Profile_Videos(response.toString());

                           // setImages(response.toString());

                          //  profile_vp_adapter.notifyDataSetChanged();

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

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(context).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });


    }


    public void addInterest(){

       // progressDialog.show();


        String URL = All_Constants_Urls.ADD_INTEREST;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());

        HashMap<String, String> hasmap_image = new HashMap<>();
        hasmap_image = List_Image_Uri.get(page_position);

        Log.d(All_Constants_Urls.TAG, "hasmap_image = "+hasmap_image);


        HashMap<String, String> hasmap_title = new HashMap<>();
        hasmap_title = List_Text.get(page_position);

        Log.d(All_Constants_Urls.TAG, "hasmap_title = "+hasmap_title);

        HashMap<String, String> hasmap_ids = new HashMap<>();
        hasmap_ids = List_IDS.get(page_position);

        Log.d(All_Constants_Urls.TAG, "hasmap_ids = "+hasmap_ids);

        try{

            if (position_image_key.equals("p11")){

                params.put(All_Constants_Urls.photo_id, hasmap_ids.get("id11"));
                params.put(All_Constants_Urls.photo_title_1, hasmap_title.get("t11"));

                if (hasmap_image.get("p11").startsWith("http")){

                    params.put(All_Constants_Urls.photo_1, hasmap_image.get("p11"));

                }else {
                    Uri uri = Uri.parse(hasmap_image.get("p11"));
                    File file1 = new File(getRealPathFromURI(uri));

                    params.put(All_Constants_Urls.photo_1, file1);
                }



            }else if (position_image_key.equals("p21")){

                params.put(All_Constants_Urls.photo_id, hasmap_ids.get("id21"));
                params.put(All_Constants_Urls.photo_title_1, hasmap_title.get("t21"));


                if (hasmap_image.get("p21").startsWith("http")){

                    params.put(All_Constants_Urls.photo_1, hasmap_image.get("p21"));

                }else {
                    Uri uri = Uri.parse(hasmap_image.get("p21"));
                    File file1 = new File(getRealPathFromURI(uri));

                    params.put(All_Constants_Urls.photo_1, file1);
                }


            }else if (position_image_key.equals("p31")){

                params.put(All_Constants_Urls.photo_id, hasmap_ids.get("id31"));
                params.put(All_Constants_Urls.photo_title_1, hasmap_title.get("t31"));

                if (hasmap_image.get("p31").startsWith("http")){

                    params.put(All_Constants_Urls.photo_1, hasmap_image.get("p31"));

                }else {
                    Uri uri = Uri.parse(hasmap_image.get("p31"));
                    File file1 = new File(getRealPathFromURI(uri));

                    params.put(All_Constants_Urls.photo_1, file1);
                }


            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }


        final String TAG = All_Constants_Urls.TAG;

        Log.d(TAG ,"AsyncHttpClient URL- " + URL);
        Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());



        client.setSSLSocketFactory(
                new SSLSocketFactory(Common.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));

        client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , Common.DEFAULT_TIMEOUT_30);
        client.post(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "onSuccess- " + response.toString());

                if (response != null) {
                    try {


                        int success = response.optInt("success");

                        if (success == 0){

                            Toast.makeText(context, "Something error", Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();


                        }else
                        if (success == 1){


                            Toast.makeText(context, "Successful", Toast.LENGTH_LONG).show();

                            sharedPref.Save_Profile_Videos(response.toString());

                           // setImages(response.toString());

                          //  profile_vp_adapter.notifyDataSetChanged();

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

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(context).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });


    }


    public void addMusic(){


      //  progressDialog.show();

        String URL = All_Constants_Urls.ADD_MUSIC;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());

        HashMap<String, String> hasmap_image = new HashMap<>();
        hasmap_image = List_Image_Uri.get(page_position);

        Log.d(All_Constants_Urls.TAG, "hasmap_image = "+hasmap_image);


        HashMap<String, String> hasmap_title = new HashMap<>();
        hasmap_title = List_Text.get(page_position);

        Log.d(All_Constants_Urls.TAG, "hasmap_title = "+hasmap_title);

        HashMap<String, String> hasmap_ids = new HashMap<>();
        hasmap_ids = List_IDS.get(page_position);

        Log.d(All_Constants_Urls.TAG, "hasmap_ids = "+hasmap_ids);

        try{

            if (position_image_key.equals("p12")){

                params.put(All_Constants_Urls.photo_id, hasmap_ids.get("id12"));
                params.put(All_Constants_Urls.photo_title_1, hasmap_title.get("t12"));

                if (hasmap_image.get("p12").startsWith("http")){

                    params.put(All_Constants_Urls.photo_1, hasmap_image.get("p12"));

                }else {
                    Uri uri = Uri.parse(hasmap_image.get("p12"));
                    File file1 = new File(getRealPathFromURI(uri));

                    params.put(All_Constants_Urls.photo_1, file1);
                }



            }else if (position_image_key.equals("p22")){

                params.put(All_Constants_Urls.photo_id, hasmap_ids.get("id22"));
                params.put(All_Constants_Urls.photo_title_1, hasmap_title.get("t22"));


                if (hasmap_image.get("p22").startsWith("http")){

                    params.put(All_Constants_Urls.photo_1, hasmap_image.get("p22"));

                }else {
                    Uri uri = Uri.parse(hasmap_image.get("p22"));
                    File file1 = new File(getRealPathFromURI(uri));

                    params.put(All_Constants_Urls.photo_1, file1);
                }


            }else if (position_image_key.equals("p32")){

                params.put(All_Constants_Urls.photo_id, hasmap_ids.get("id32"));
                params.put(All_Constants_Urls.photo_title_1, hasmap_title.get("t32"));

                if (hasmap_image.get("p32").startsWith("http")){

                    params.put(All_Constants_Urls.photo_1, hasmap_image.get("p32"));

                }else {
                    Uri uri = Uri.parse(hasmap_image.get("p32"));
                    File file1 = new File(getRealPathFromURI(uri));

                    params.put(All_Constants_Urls.photo_1, file1);
                }


            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }


        final String TAG = All_Constants_Urls.TAG;

        Log.d(TAG ,"AsyncHttpClient URL- " + URL);
        Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


        client.setSSLSocketFactory(
                new SSLSocketFactory(Common.getSslContext(),
                        SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));

        client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , Common.DEFAULT_TIMEOUT_30);
        client.post(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "onSuccess- " + response.toString());

                if (response != null) {
                    try {


                        int success = response.optInt("success");

                        if (success == 0){

                            Toast.makeText(context, "Something error", Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();


                        }else
                        if (success == 1){

                            Toast.makeText(context, "Successful", Toast.LENGTH_LONG).show();

                            sharedPref.Save_Profile_Videos(response.toString());

                          //  setImages(response.toString());

                          //  profile_vp_adapter.notifyDataSetChanged();

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

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(context).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });


    }


    public void getProfileData(){


        progressDialog.show();

        List_Text.clear();
        List_Image_Uri.clear();
        List_IDS.clear();



        String URL = All_Constants_Urls.GET_PROFILE_DATA;
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

                Log.d(TAG, "onSuccess- " + response.toString());

                if (response != null) {
                    try {


                        int success = response.optInt("success");

                        if (success == 0){

                            Toast.makeText(context, "Something error", Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();


                        }else
                        if (success == 1){

                            sharedPref.Save_Profile_Videos(response.toString());

                           // mPager.setAdapter(null);


                           // Toast.makeText(context, "Successful", Toast.LENGTH_LONG).show();

                            JSONObject object = response.getJSONObject(All_Constants_Urls.basic_info);


                            String id = object.optString("id");
                            sharedPref.save_Use_Id(id);


                            String name = object.getString(All_Constants_Urls.name);
                            String age = object.getString(All_Constants_Urls.age);
                            String strGender = object.getString(All_Constants_Urls.gender);
                            String sexual_orientation = object.getString(All_Constants_Urls.sexual_orientation);
                            String city = object.getString(All_Constants_Urls.city);
                            String country = object.getString(All_Constants_Urls.country);


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

                            Log.d(All_Constants_Urls.TAG, "temp_text = "+temp_text);

                            txtTitle.setText(temp_text);

                            global_class.setOrientation(sexual_orientation);

                            if(global_class.getOrientation().equals("Straight")){
                                imgSexualOriante.setImageResource(R.mipmap.straight_male_w);

                            }else if (global_class.getOrientation().equals("Gay")){
                                imgSexualOriante.setImageResource(R.mipmap.gay_w);

                            }else if (global_class.getOrientation().equals("Lesbian")){
                                imgSexualOriante.setImageResource(R.mipmap.lesbian_w);

                            }else if (global_class.getOrientation().equals("Bisexual")){
                                imgSexualOriante.setImageResource(R.mipmap.bisexual_female_w);

                            }



                            JSONArray image_video = object.optJSONArray(All_Constants_Urls.image_video);
                            if (image_video.length() != 0){

                                iv1.setVisibility(View.GONE);

                                slider1.removeAllSliders();
                                mDemoSliderfull.removeAllSliders();


                                for (int i = 0; i < image_video.length(); i++){

                                    String url = image_video.getString(i);

                                    TextSliderView textSliderView1 = new TextSliderView(ProfileActivity.this);
                                    // initialize a SliderLayout
                                    textSliderView1
                                            .image(url)
                                            .setScaleType(BaseSliderView.ScaleType.Fit)
                                            .setOnSliderClickListener(ProfileActivity.this);

                                    slider1.addSlider(textSliderView1);
                                    mDemoSliderfull.addSlider(textSliderView1);

                                }

                                videoEffect1();
                                // videoEffectf1();

                                is_video_1 = false;
                            }

                            String video = object.optString(All_Constants_Urls.video);
                            if (video.length() != 0){
                                Video_Url_1 = video;
                                is_video_1 = true;
                                iv1.setVisibility(View.GONE);
                                Play_Video(video);
                            }

                            /////

                            JSONArray second_image_video = object.optJSONArray(All_Constants_Urls.image_video_2);
                            if (second_image_video.length() != 0){

                                slider2.removeAllSliders();
                                mDemoSliderfull2.removeAllSliders();

                                iv2.setVisibility(View.GONE);

                                for (int i = 0; i < second_image_video.length(); i++){

                                    String url = second_image_video.getString(i);

                                    TextSliderView textSliderView1 = new TextSliderView(ProfileActivity.this);
                                    // initialize a SliderLayout
                                    textSliderView1
                                            .image(url)
                                            .setScaleType(BaseSliderView.ScaleType.Fit)
                                            .setOnSliderClickListener(ProfileActivity.this);

                                    slider2.addSlider(textSliderView1);
                                    mDemoSliderfull2.addSlider(textSliderView1);

                                }

                                videoEffect2();
                                // videoEffectf2();

                                is_video_2 = false;
                            }

                            String second_video = object.optString(All_Constants_Urls.video_2);
                            if (second_video.length() != 0){
                                Video_Url_2 = second_video;
                                is_video_2 = true;
                                iv2.setVisibility(View.GONE);
                                Play_Video2(second_video);
                            }

                            /////

                            JSONArray third_image_video = object.optJSONArray(All_Constants_Urls.image_video_3);
                            if (third_image_video.length() != 0){

                                iv3.setVisibility(View.GONE);

                                slider3.removeAllSliders();
                                mDemoSliderfull3.removeAllSliders();

                                for (int i = 0; i < third_image_video.length(); i++){

                                    String url = third_image_video.getString(i);

                                    TextSliderView textSliderView1 = new TextSliderView(ProfileActivity.this);
                                    // initialize a SliderLayout
                                    textSliderView1
                                            .image(url)
                                            .setScaleType(BaseSliderView.ScaleType.Fit)
                                            .setOnSliderClickListener(ProfileActivity.this);

                                    slider3.addSlider(textSliderView1);
                                    mDemoSliderfull3.addSlider(textSliderView1);

                                }
                                videoEffect3();
                                //videoEffectf3();

                                is_video_3 = false;
                            }

                            String third_video = object.optString(All_Constants_Urls.video_3);
                            if (third_video.length() != 0){
                                Video_Url_3 = third_video;
                                is_video_3 = true;
                                iv3.setVisibility(View.GONE);
                                Play_Video3(third_video);

                            }



                            //////

                            setImages(response.toString());


                            if (profile_vp_adapter == null){

                                profile_vp_adapter = new Profile_Vp_Adapter_(ProfileActivity.this, imgEditUser, List_Text, List_Image, List_Image_Uri,iv1,iv2,iv3,image1Play,image2Play,image3Play);
                                mPager.setAdapter(profile_vp_adapter);

                            }else {

                                profile_vp_adapter.notifyDataSetChanged();
                            }



                            runViewPager();


                            linearfull.setVisibility(View.VISIBLE);

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

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(context).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });


    }


    public void setImages(String json){

        List_Text.clear();
        List_Image.clear();
        List_Image_Uri.clear();
        List_IDS.clear();


        try{

            JSONObject response = new JSONObject(json);


            /////////////  Interest data set ..........
            String uri = null;
            String ids = "";

            JSONArray food_data = response.getJSONArray(All_Constants_Urls.food_data);
            if (food_data.length() != 0){

                HashMap<String, String>  map1 = new HashMap();
                HashMap<String, String> map2 = new HashMap();
                HashMap<String, String> map_id_1 = new HashMap();

                if (food_data.length() == 1){
                    JSONObject obj_food = food_data.getJSONObject(0);

                    map_id_1.put("id10", obj_food.getString(All_Constants_Urls.id));
                    map1.put("t10", obj_food.getString(All_Constants_Urls.title));
                    map2.put("p10", obj_food.getString(All_Constants_Urls.photo));

                    map_id_1.put("id20", ids);
                    map1.put("t20", "add pic");
                    map2.put("p20", uri);

                    map_id_1.put("id30", ids);
                    map1.put("t30", "add pic");
                    map2.put("p30", uri);

                }else
                if (food_data.length() == 2){

                    for (int i = 0; i < food_data.length(); i++){
                        JSONObject obj_food = food_data.getJSONObject(i);

                        if (i == 0){
                            map_id_1.put("id10", obj_food.getString(All_Constants_Urls.id));
                            map1.put("t10", obj_food.getString(All_Constants_Urls.title));
                            map2.put("p10", obj_food.getString(All_Constants_Urls.photo));
                        }
                        if (i == 1){
                            map_id_1.put("id20", obj_food.getString(All_Constants_Urls.id));
                            map1.put("t20", obj_food.getString(All_Constants_Urls.title));
                            map2.put("p20", obj_food.getString(All_Constants_Urls.photo));
                        }

                    }

                    map_id_1.put("id30", ids);
                    map1.put("t30", "add pic");
                    map2.put("p30", uri);

                }else
                if (food_data.length() >= 3){

                    for (int i = 0; i < food_data.length(); i++){
                        JSONObject obj_food = food_data.getJSONObject(i);

                        if (i == 0){
                            map_id_1.put("id10", obj_food.getString(All_Constants_Urls.id));
                            map1.put("t10", obj_food.getString(All_Constants_Urls.title));
                            map2.put("p10", obj_food.getString(All_Constants_Urls.photo));
                        }
                        if (i == 1){
                            map_id_1.put("id20", obj_food.getString(All_Constants_Urls.id));
                            map1.put("t20", obj_food.getString(All_Constants_Urls.title));
                            map2.put("p20", obj_food.getString(All_Constants_Urls.photo));
                        }
                        if (i == 2){
                            map_id_1.put("id30", obj_food.getString(All_Constants_Urls.id));
                            map1.put("t30", obj_food.getString(All_Constants_Urls.title));
                            map2.put("p30", obj_food.getString(All_Constants_Urls.photo));
                        }

                    }


                }

                List_Text.add(map1);
                List_Image_Uri.add(map2);
                List_IDS.add(map_id_1);

            }else {

                HashMap<String, String>  map_id_1 = new HashMap();
                map_id_1.put("id10", ids);
                map_id_1.put("id20", ids);
                map_id_1.put("id30", ids);
                List_IDS.add(map_id_1);

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
                HashMap<String, String>  map_id_2 = new HashMap();


                if (interest_data.length() == 1){
                    JSONObject obj_intr = interest_data.getJSONObject(0);

                    map_id_2.put("id11", obj_intr.getString(All_Constants_Urls.id));
                    map3.put("t11", obj_intr.getString(All_Constants_Urls.title));
                    map4.put("p11", obj_intr.getString(All_Constants_Urls.photo));

                    map_id_2.put("id21", ids);
                    map3.put("t21", "add pic");
                    map4.put("p21", uri);

                    map_id_2.put("id31", ids);
                    map3.put("t31", "add pic");
                    map4.put("p31", uri);

                }else
                if (interest_data.length() == 2){

                    for (int i = 0; i < interest_data.length(); i++){
                        JSONObject obj_intr = interest_data.getJSONObject(i);

                        if (i == 0){
                            map_id_2.put("id11", obj_intr.getString(All_Constants_Urls.id));
                            map3.put("t11", obj_intr.getString(All_Constants_Urls.title));
                            map4.put("p11", obj_intr.getString(All_Constants_Urls.photo));
                        }
                        if (i == 1){
                            map_id_2.put("id21", obj_intr.getString(All_Constants_Urls.id));
                            map3.put("t21", obj_intr.getString(All_Constants_Urls.title));
                            map4.put("p21", obj_intr.getString(All_Constants_Urls.photo));
                        }

                        map_id_2.put("id31", ids);
                        map3.put("t31", "add pic");
                        map4.put("p31", uri);


                    }


                }else
                if (interest_data.length() >= 3){

                    for (int i = 0; i < interest_data.length(); i++){
                        JSONObject obj_intr = interest_data.getJSONObject(i);

                        if (i == 0){
                            map_id_2.put("id11", obj_intr.getString(All_Constants_Urls.id));
                            map3.put("t11", obj_intr.getString(All_Constants_Urls.title));
                            map4.put("p11", obj_intr.getString(All_Constants_Urls.photo));
                        }
                        if (i == 1){
                            map_id_2.put("id21", obj_intr.getString(All_Constants_Urls.id));
                            map3.put("t21", obj_intr.getString(All_Constants_Urls.title));
                            map4.put("p21", obj_intr.getString(All_Constants_Urls.photo));
                        }
                        if (i == 2){
                            map_id_2.put("id31", obj_intr.getString(All_Constants_Urls.id));
                            map3.put("t31", obj_intr.getString(All_Constants_Urls.title));
                            map4.put("p31", obj_intr.getString(All_Constants_Urls.photo));
                        }

                    }

                }

                List_Text.add(map3);
                List_Image_Uri.add(map4);
                List_IDS.add(map_id_2);

            }else {

                HashMap<String, String>  map_id_2 = new HashMap();
                map_id_2.put("id11", ids);
                map_id_2.put("id21", ids);
                map_id_2.put("id31", ids);
                List_IDS.add(map_id_2);


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
                HashMap<String, String>  map_id_3 = new HashMap();

                if (music_data.length() == 1){
                    JSONObject obj_music = music_data.getJSONObject(0);

                    map_id_3.put("id12", obj_music.getString(All_Constants_Urls.id));
                    map5.put("t12", obj_music.getString(All_Constants_Urls.title));
                    map6.put("p12", obj_music.getString(All_Constants_Urls.photo));

                    map_id_3.put("id22", ids);
                    map5.put("t22", "add pic");
                    map6.put("p22", uri);

                    map_id_3.put("id32", ids);
                    map5.put("t32", "add pic");
                    map6.put("p32", uri);


                }else
                if (music_data.length() == 2){

                    for (int i = 0; i < music_data.length(); i++){
                        JSONObject obj_music = music_data.getJSONObject(i);

                        if (i == 0){
                            map_id_3.put("id12", obj_music.getString(All_Constants_Urls.id));
                            map5.put("t12", obj_music.getString(All_Constants_Urls.title));
                            map6.put("p12", obj_music.getString(All_Constants_Urls.photo));
                        }
                        if (i == 1){
                            map_id_3.put("id22", obj_music.getString(All_Constants_Urls.id));
                            map5.put("t22", obj_music.getString(All_Constants_Urls.title));
                            map6.put("p22", obj_music.getString(All_Constants_Urls.photo));
                        }

                        map_id_3.put("id32", ids);
                        map5.put("t32", "add pic");
                        map6.put("p32", uri);

                    }

                }else
                if (music_data.length() >= 3){


                    for (int i = 0; i < music_data.length(); i++){
                        JSONObject obj_music = music_data.getJSONObject(i);

                        if (i == 0){
                            map_id_3.put("id12", obj_music.getString(All_Constants_Urls.id));
                            map5.put("t12", obj_music.getString(All_Constants_Urls.title));
                            map6.put("p12", obj_music.getString(All_Constants_Urls.photo));
                        }
                        if (i == 1){
                            map_id_3.put("id22", obj_music.getString(All_Constants_Urls.id));
                            map5.put("t22", obj_music.getString(All_Constants_Urls.title));
                            map6.put("p22", obj_music.getString(All_Constants_Urls.photo));
                        }
                        if (i == 2){
                            map_id_3.put("id32", obj_music.getString(All_Constants_Urls.id));
                            map5.put("t32", obj_music.getString(All_Constants_Urls.title));
                            map6.put("p32", obj_music.getString(All_Constants_Urls.photo));
                        }

                    }

                }

                List_Text.add(map5);
                List_Image_Uri.add(map6);
                List_IDS.add(map_id_3);

            }else {
                HashMap<String, String>  map_id_3 = new HashMap();
                map_id_3.put("id12", ids);
                map_id_3.put("id22", ids);
                map_id_3.put("id32", ids);
                List_IDS.add(map_id_3);

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



        }catch (Exception e){
            e.printStackTrace();
        }






    }


    private void setDefaultVideo(String key_string){

       // progressDialog.show();

        String URL = All_Constants_Urls.SET_DEFAULT_VIDEO;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());
        params.put(All_Constants_Urls.key, key_string);


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

                            Toast.makeText(context, "Something error", Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();


                        }else
                        if (success == 1){


                            Toast.makeText(context, "Successful", Toast.LENGTH_LONG).show();


                            progressDialog.dismiss();

                            getProfileData();

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

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(context).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });








    }


    @Override
    protected void onDestroy() {
        freeMemory();
        super.onDestroy();
    }

    public void freeMemory(){
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

}