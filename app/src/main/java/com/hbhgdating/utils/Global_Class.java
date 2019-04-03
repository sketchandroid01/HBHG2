package com.hbhgdating.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.danikula.videocache.HttpProxyCacheServer;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.util.ArrayList;
import java.util.HashMap;


public class Global_Class extends MultiDexApplication {


    private static Global_Class mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize the SDK before executing any other operations,
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);



        mInstance = this;
    }


    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }


    private HttpProxyCacheServer proxy;

    public static HttpProxyCacheServer getProxy(Context context) {
        Global_Class app = (Global_Class) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
       // return new HttpProxyCacheServer(this);

        /*return new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(1024 * 1024 * 1024)       // 1 Gb for cache
                .build();*/

        return new HttpProxyCacheServer.Builder(this)
                .maxCacheFilesCount(20)
                .build();
    }


    ////// Volley ......

    public static synchronized Global_Class getInstance() {
        return mInstance;
    }


    public static final String TAG = Global_Class.class
            .getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;



    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }





    String fullName;
    String email;
    String location;
    String gender;
    String dob;
    ArrayList<String> pic ;
    String facebookId;
    String id;
    public boolean login_status;
    public  String login_via;
    public String LATITUDE;
    public String LONGITUDE;
    public String CITY;
    public String COUNTRY;
    public String Profile_Image;





    public ArrayList<String> getPic() {
        return pic;
    }

    public void setPic(ArrayList<String> pic) {
        this.pic = pic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isLogin_status() {
        return login_status;
    }

    public void setLogin_status(boolean login_status) {
        this.login_status = login_status;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }



    public String FB_profile_dir = "";
    public String FB_profile_name = "";
    public String FB_profile_first_name = "";
    public int FB_profile_age = 0;
    public String FB_profile_location = "";
    public String FB_profile_gender = "";
    public String FB_profile_email = "";
    public String FB_profile_id = "";
    public String FB_profile_birthday = "";
    public String Location_gps = "";
    public String get_location;

    public String Insta_Profile_id="";
    public String Insta_name="";
    public String Insta_first_name="";
    public String Orientation = "";



    public String getOrientation() {
        return Orientation;
    }

    public void setOrientation(String orientation) {
        Orientation = orientation;
    }

    public String getLogin_via() {
        return login_via;
    }

    public void setLogin_via(String login_via) {
        this.login_via = login_via;
    }

    public String getFB_profile_dir() {
        return FB_profile_dir;
    }

    public void setFB_profile_dir(String FB_profile_dir) {
        this.FB_profile_dir = FB_profile_dir;
    }

    public String getFB_profile_name() {
        return FB_profile_name;
    }

    public void setFB_profile_name(String FB_profile_name) {
        this.FB_profile_name = FB_profile_name;
    }

    public int getFB_profile_age() {
        return FB_profile_age;
    }

    public void setFB_profile_age(int FB_profile_age) {
        this.FB_profile_age = FB_profile_age;
    }

    public String getFB_profile_first_name() {
        return FB_profile_first_name;
    }

    public void setFB_profile_first_name(String FB_profile_first_name) {
        this.FB_profile_first_name = FB_profile_first_name;
    }

    public String getFB_profile_location() {
        return FB_profile_location;
    }

    public void setFB_profile_location(String FB_profile_location) {
        this.FB_profile_location = FB_profile_location;
    }

    public String getFB_profile_gender() {
        return FB_profile_gender;
    }

    public void setFB_profile_gender(String FB_profile_gender) {
        this.FB_profile_gender = FB_profile_gender;
    }

    public String getFB_profile_email() {
        return FB_profile_email;
    }

    public void setFB_profile_email(String FB_profile_email) {
        this.FB_profile_email = FB_profile_email;
    }

    public String getFB_profile_id() {
        return FB_profile_id;
    }

    public void setFB_profile_id(String FB_profile_id) {
        this.FB_profile_id = FB_profile_id;
    }

    public String getFB_profile_birthday() {
        return FB_profile_birthday;
    }

    public void setFB_profile_birthday(String FB_profile_birthday) {
        this.FB_profile_birthday = FB_profile_birthday;
    }

    public String getLocation_gps() {
        return Location_gps;
    }

    public void setLocation_gps(String location_gps) {
        Location_gps = location_gps;
    }


    public String getInsta_Profile_id() {
        return Insta_Profile_id;
    }

    public void setInsta_Profile_id(String insta_Profile_id) {
        Insta_Profile_id = insta_Profile_id;
    }


    public String getInsta_name() {
        return Insta_name;
    }

    public void setInsta_name(String insta_name) {
        Insta_name = insta_name;
    }

    ////////////////
    public ArrayList<String> Image_Url_FB = new ArrayList<>();
    public ArrayList<String> Image_Url_INSTA = new ArrayList<>();
    public ArrayList<HashMap<String, String>> Video_Url_INSTA = new ArrayList<>();
    public boolean isEdit = false;


    public ArrayList<String> Final_Image_To_Upload = new ArrayList<>();

    public String Final_Video_To_upload = "";

    public String getFinal_Video_To_upload() {
        return Final_Video_To_upload;
    }

    public void setFinal_Video_To_upload(String final_Video_To_upload) {
        Final_Video_To_upload = final_Video_To_upload;
    }

////////////////////////////////////////

    public ArrayList<String> Selected_Image_Url = new ArrayList<>();
    public ArrayList<Bitmap> List_Bitmap_for_ProfileActivity = new ArrayList<>();

    ////////////////

    public String All_Matches_Data = "";


    /////////////////////////////////////////

    private boolean if_action_on_block_screen = false;

    public boolean isIf_action_on_block_screen() {
        return if_action_on_block_screen;
    }

    public void setIf_action_on_block_screen(boolean if_action_on_block_screen) {
        this.if_action_on_block_screen = if_action_on_block_screen;
    }

    ////////////////////////////////////

    private boolean if_action_on_chatroom_create = false;

    public boolean isIf_action_on_chatroom_create() {
        return if_action_on_chatroom_create;
    }

    public void setIf_action_on_chatroom_create(boolean if_action_on_chatroom_create) {
        this.if_action_on_chatroom_create = if_action_on_chatroom_create;
    }

    /////////////////////////////////

    private String selected_chatroom_id;
    private String selected_chatroom_name;

    public String getSelected_chatroom_id() {
        return selected_chatroom_id;
    }

    public void setSelected_chatroom_id(String selected_chatroom_id) {
        this.selected_chatroom_id = selected_chatroom_id;
    }

    public String getSelected_chatroom_name() {
        return selected_chatroom_name;
    }

    public void setSelected_chatroom_name(String selected_chatroom_name) {
        this.selected_chatroom_name = selected_chatroom_name;
    }

//////////////////////////////////

    private String chatting_user_id = "";
    private String chatting_chatroom_id = "";

    public String getChatting_user_id() {
        return chatting_user_id;
    }

    public void setChatting_user_id(String chatting_user_id) {
        this.chatting_user_id = chatting_user_id;
    }

    public String getChatting_chatroom_id() {
        return chatting_chatroom_id;
    }

    public void setChatting_chatroom_id(String chatting_chatroom_id) {
        this.chatting_chatroom_id = chatting_chatroom_id;
    }
    ////////////////////////////////////////////

    public ArrayList<Boolean> list_is_new = new ArrayList<>();


}