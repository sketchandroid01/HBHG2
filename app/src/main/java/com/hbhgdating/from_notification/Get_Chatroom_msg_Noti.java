package com.hbhgdating.from_notification;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hbhgdating.chat.ChatMessage;
import com.hbhgdating.chat.ChatRoomDetailListViewAdapter;
import com.hbhgdating.chat.Chat_screen_new;
import com.hbhgdating.chat.ChatroomMembersActivity;
import com.hbhgdating.chat.ChatroomVideoImg;
import com.hbhgdating.chat.GiphyAdapter_Chatroom;
import com.hbhgdating.chat.StickersView;
import com.hbhgdating.databaseLocal.DatabaseHelper;
import com.hbhgdating.R;
import com.hbhgdating.utils.All_Constants_Urls;
import com.hbhgdating.utils.Common;
import com.hbhgdating.utils.Global_Class;
import com.hbhgdating.utils.SharedPref;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;

/**
 * Created by Developer on 5/5/17.
 */
public class Get_Chatroom_msg_Noti extends AppCompatActivity {

    TextView textView_tag,tv_grp_info,textView_chatroom, tv_created_by_name, empty;
    CountDownTimer timer;

    ListView listView;
    ChatRoomDetailListViewAdapter adapter;
    ImageView img1, imgPlay, imgSticker, imgSend, imgAttachment, imgGif;
    private static final int SELECT_STICKRS = 15;
    EditText edt_message;
    ArrayList<ChatMessage> arrData = new ArrayList<ChatMessage>();
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");
    RecyclerView recycler_view_giphy;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<HashMap<String,String>> array_list;
    ArrayList<HashMap<String,String>> user_list;

    RelativeLayout rl_bottom_options,rl_giphy_search;
    ImageView keyboard_icon;
    AutoCompleteTextView edt_search_giphy;
    Boolean searchnow = true;
    SharedPref sharedPref;
    Bitmap resized;
    public static final int PICK_IMAGE = 1;
    String name_chatroom="";
    String user_details;
    String from;
    String chatroom_admin_id, video_link, chatroom_id, content_img, content_video_img;
    int image_type;

    Dialog progressDialog;
    Global_Class global_class;
    private DatabaseHelper databaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.chatroom_msgs);

        Get_Chatroom_msg_Noti.this.registerReceiver(mMessageReceiver, new IntentFilter(Common.Key_ChatroomNoti));

        initViews();


    }


    private void initViews(){


        progressDialog = new Dialog(this, android.R.style.Theme_Translucent);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setContentView(R.layout.progressbar_pleasewait);
        progressDialog.setCanceledOnTouchOutside(false);

        sharedPref = new SharedPref(this);
        global_class = (Global_Class)getApplicationContext();
        databaseHelper = new DatabaseHelper(this);

        textView_chatroom = (TextView)findViewById(R.id.textView_chatroom);
        textView_tag = (TextView)findViewById(R.id.textView_tag);
        tv_created_by_name = (TextView)findViewById(R.id.tv_created_by_name);
        tv_grp_info = (TextView)findViewById(R.id.tv_grp_info);
        empty = (TextView)findViewById(R.id.empty);
        empty.setVisibility(View.GONE);
        img1 = (ImageView) findViewById(R.id.img1);
        imgPlay = (ImageView) findViewById(R.id.imgPlay);
        listView = (ListView)findViewById(R.id.listView);
        adapter = new ChatRoomDetailListViewAdapter(this,arrData);
        listView.setStackFromBottom(true);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(adapter);


        final Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            chatroom_id = bundle.getString("id");
            from = bundle.getString("from", "");

            global_class.setChatting_chatroom_id(chatroom_id);
            databaseHelper.update_isNewMsg_TABLE_MY_MATCHES(chatroom_id);

            try {
                JSONObject object = new JSONObject(databaseHelper.getChatroomData(chatroom_id));


                name_chatroom = object.getString("title");
                textView_chatroom.setText(name_chatroom);
                textView_tag.setText(object.getString("tags"));
                chatroom_admin_id = object.getString("user_id");
                image_type = Integer.parseInt(object.getString("content_type"));
                video_link = object.getString("content_video");
                String[] array = object.getString("admin_name").split(" ");
                tv_created_by_name.setText(array[0]);
                content_img = object.optString("content_img");
                content_video_img = object.optString("content_video_img");


            }catch (Exception e){
                e.printStackTrace();
            }



            if (image_type == 2){
                Picasso.with(getApplicationContext()).load(content_img).error(R.mipmap.grid_bg_3).into( img1, new Callback() {
                    @Override
                    public void onSuccess() {
                        //  Log.d("TAG", "onSuccess");
                    }
                    @Override
                    public void onError() {
                        //  Toast.makeText(mactivity, "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                });

                imgPlay.setVisibility(View.GONE);
            }else if (image_type == 4){
                Picasso.with(getApplicationContext()).load(content_video_img).error(R.mipmap.grid_bg_3).into( img1, new Callback() {
                    @Override
                    public void onSuccess() {
                        //  Log.d("TAG", "onSuccess");
                    }
                    @Override
                    public void onError() {
                        //  Toast.makeText(mactivity, "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
                imgPlay.setVisibility(View.VISIBLE);
            }


           // Log.d(All_Constants_Urls.TAG, ""+image_type);
           // Log.d(All_Constants_Urls.TAG, ""+video_link);
           // Log.d(All_Constants_Urls.TAG, ""+bundle.getString("content_img"));
           // Log.d(All_Constants_Urls.TAG, ""+bundle.getString("content_video_img"));


            imgPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Get_Chatroom_msg_Noti.this, ChatroomVideoImg.class);
                    intent.putExtra("content_type", image_type);
                    intent.putExtra("content_video", video_link);
                    intent.putExtra("content_img", content_img);
                    startActivity(intent);

                }
            });





        }


        edt_message = (EditText) findViewById(R.id.edt_message);
        imgGif = (ImageView)findViewById(R.id.imgGif);
        imgSticker = (ImageView)findViewById(R.id.imgSticker);
        imgSend = (ImageView)findViewById(R.id.imgSend);
        imgAttachment = (ImageView)findViewById(R.id.imgAttachment);
        keyboard_icon = (ImageView) findViewById(R.id.keyboard_icon);
        rl_bottom_options = (RelativeLayout)findViewById(R.id.rl_bottom_options);
        rl_giphy_search = (RelativeLayout) findViewById(R.id.rl_giphy_search);
        edt_search_giphy=(AutoCompleteTextView) findViewById(R.id.edt_search_giphy);
        recycler_view_giphy = (RecyclerView) findViewById(R.id.recycler_view_giphy);
        recycler_view_giphy.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recycler_view_giphy.setLayoutManager(mLayoutManager);
        array_list=new ArrayList<>();
        user_list=new ArrayList<>();



        fetchChat();

        getGiphy("hello");



        tv_grp_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserList();
            }
        });


        imgAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent pictureActionIntent = null;

                pictureActionIntent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(
                        pictureActionIntent,
                        PICK_IMAGE);

            }
        });

        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!edt_message.getText().toString().isEmpty()){
                    sendTextMessage();
                }

            }
        });

        imgGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                recycler_view_giphy.setVisibility(View.VISIBLE);
                rl_giphy_search.setVisibility(View.VISIBLE);
                rl_bottom_options.setVisibility(View.GONE);
                edt_search_giphy.requestFocus();

            }
        });

        keyboard_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recycler_view_giphy.setVisibility(View.GONE);
                rl_giphy_search.setVisibility(View.GONE);
                rl_bottom_options.setVisibility(View.VISIBLE);
                edt_message.requestFocus();
            }
        });

        imgSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Get_Chatroom_msg_Noti.this, StickersView.class);
                startActivityForResult(intent1, SELECT_STICKRS);
            }
        });

        edt_search_giphy.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {

                if (timer != null) {
                    timer.cancel();
                }

                timer = new CountDownTimer(1500, 1000) {

                    public void onTick(long millisUntilFinished) {
                    }

                    public void onFinish() {
                        //do what you wish
                        getGiphy(s.toString());

                    }

                }.start();
            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    final Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {

            case SELECT_STICKRS:
                if (resultCode == RESULT_OK) {
                    String str1 = imageReturnedIntent.getStringExtra("stickername");
                    sendSticker(str1);
                }
                break;

            case PICK_IMAGE: {
                if (resultCode == RESULT_OK) {

                    sendImage(imageReturnedIntent.getData());

                }
                break;


            }


        }
    }


    public void sendSticker(String sticker) {
        Date today = Calendar.getInstance().getTime();
        final ChatMessage chatMessage = new ChatMessage("sticker","me", "test",true);
        //chatMessage.body = message;
        chatMessage.Date = dateFormat.format(today);
        chatMessage.body = sticker;
        arrData.add(chatMessage);
        adapter.notifyDataSetChanged();

        postChat("6",sticker);
    }


    public void sendImage(Uri uri) {

        try {

            InputStream inputStream = getContentResolver().openInputStream(uri);
            Bitmap bmp = BitmapFactory.decodeStream(inputStream);
            if (inputStream != null) inputStream.close();
            resized=getResizedBitmap(bmp,640);
        }catch (Exception e){

        }



        long time = 0;
        time =  System.currentTimeMillis();
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/HBHG/Images/sent";
        File dir = new File(file_path);
        if(!dir.exists())
            dir.mkdirs();
        final File image_file = new File(dir, "hbhg" + time + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(image_file);
            resized.compress(Bitmap.CompressFormat.JPEG, 90, os);
            os.flush();
            os.close();
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(image_file)));
        } catch (Exception e) {

        }


        Date today = Calendar.getInstance().getTime();
        final ChatMessage chatMessage = new ChatMessage("img","me", "test",true);
        chatMessage.note = "sentnow";
        chatMessage.Date = dateFormat.format(today);
        chatMessage.img = Uri.fromFile(image_file).toString();
        arrData.add(chatMessage);
        adapter.notifyDataSetChanged();

        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

//        Log.d("position", "sendImage: "+arrData.size());
//        Log.d("position", "sendImage: "+lastListItemPosition);
//        Log.d("position", "sendImage: "+firstListItemPosition);
//        Log.d("position", "sendImage: "+listView.getChildCount());

        // donutProgress=(DonutProgress)listView.getChildAt(listView.getChildCount()-1).findViewById(R.id.donut_progress_iv22);

        postChat("2",image_file.getAbsolutePath());
    }


    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        if (width < maxSize && height < maxSize){

            return image;

        }else {

            float bitmapRatio = (float) width / (float) height;
            if (bitmapRatio > 1) {
                width = maxSize;
                height = (int) (width / bitmapRatio);
            } else {
                height = maxSize;
                width = (int) (height * bitmapRatio);
            }

            return Bitmap.createScaledBitmap(image, width, height, true);
        }

    }


    public void sendTextMessage() {
        Date today = Calendar.getInstance().getTime();
        String message = edt_message.getEditableText().toString();
        if (!message.equalsIgnoreCase("")) {
            final ChatMessage chatMessage = new ChatMessage("text","me", "test",true);
            chatMessage.body = message;
            chatMessage.Date = dateFormat.format(today);
            //chatMessage.img = globalclass.getPurl();
            edt_message.setText("");
            arrData.add(chatMessage);
            adapter.notifyDataSetChanged();
            postChat("1",message);
        }
    }


    public  void getGiphy(String keywoard){

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTING || conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTING) {
            String url = "http://api.giphy.com/v1/gifs/search?q="+keywoard+"&api_key=kfWMil4OFYOGqlN3zBWDm1zrSfTcWUHt";
            final AsyncHttpClient client = new AsyncHttpClient();

            array_list.clear();


            try {
                client.get(this,url,new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {


                        searchnow=true;
                        if (response != null) {
                            try {


                              //  Log.d("response", response.toString());

                                JSONArray array=response.getJSONArray("data");

                                for (int i=0;i<array.length();i++){

                                    JSONObject object=array.getJSONObject(i);


                                    JSONObject images=object.getJSONObject("images");
                                    JSONObject fixed_width=images.getJSONObject("fixed_width");
                                    JSONObject fixed_height=images.getJSONObject("fixed_height");
                                    JSONObject original=images.getJSONObject("original");

                                    HashMap<String, String> amn= new HashMap<String, String>();
                                    amn.put("fixed_width_url",fixed_width.getString("url"));
                                    amn.put("fixed_height_url",fixed_height.getString("url"));
                                    amn.put("original_url",original.getString("url"));
                                    array_list.add(amn);
                                }

                                GiphyAdapter_Chatroom giphyAdapter = new GiphyAdapter_Chatroom(Get_Chatroom_msg_Noti.this, array_list,arrData,adapter,getIntent().getStringExtra("id"));
                                recycler_view_giphy.setAdapter(giphyAdapter);




                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {

                    }

                });

            }catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            AlertDialog alert1 = new AlertDialog.Builder(Get_Chatroom_msg_Noti.this).create();
            alert1.setMessage("No Internet !");
            alert1.show();
        }
    }


    public void fetchChat(){


        String URL = All_Constants_Urls.getchatfromchatroom;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());
        params.put(All_Constants_Urls.chatroom_id, chatroom_id);


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

                        arrData.clear();


                        int success = response.optInt("success");

                        if (success == 0){

                           /* android.app.AlertDialog alert = new android.app.AlertDialog.Builder(Get_Chatroom_msg.this).create();
                            alert.setMessage("No message available for you");
                            alert.show();*/

                            empty.setVisibility(View.VISIBLE);

                          //  Toast.makeText(getApplicationContext(), "No message available for you", Toast.LENGTH_LONG).show();

                        }else
                        if (success == 1){

                            JSONArray chatdata = response.getJSONArray(All_Constants_Urls.chatdata);

                            Log.d(TAG, "length - " +chatdata.length());
                            for (int i = 0; i < chatdata.length(); i++){
                                JSONObject object = chatdata.getJSONObject(i);

                                setChatData(object.toString());


                            }


                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.d(TAG, "onFailure- " + res);


                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(Get_Chatroom_msg_Noti.this).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });


    }


    public void postChat(String msg_type, String content){

        databaseHelper.setMyChatroomMessageDateTime(chatroom_id, "0");

        empty.setVisibility(View.GONE);


        String URL = All_Constants_Urls.postonchatroom;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());
        params.put(All_Constants_Urls.chatroom_id, chatroom_id);
        params.put(All_Constants_Urls.msg_type, msg_type);
        params.put(All_Constants_Urls.content, content);

        if (msg_type.matches("2")){
            try {

                File file=new File(content);
                params.put("content_media", file);

            }catch (Exception e) {

            }
        }


        final String TAG = All_Constants_Urls.TAG;

       // Log.d(TAG ,"AsyncHttpClient URL- " + URL);
       // Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());

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

                            Toast.makeText(getApplicationContext(), "Something error\nPlease try again", Toast.LENGTH_LONG).show();

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


                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(Get_Chatroom_msg_Noti.this).create();
                alert.setMessage("Server Error");
                alert.show();
            }

        });


    }


    public void getUserList() {

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTING || conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED || conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTING) {

            progressDialog.show();

            String url = All_Constants_Urls.getusersfromchatroom;

            RequestParams params = new RequestParams();

            params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
            params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());
            params.put(All_Constants_Urls.chatroom_id, chatroom_id);
            final AsyncHttpClient client = new AsyncHttpClient();

            user_list.clear();

          //  Log.d(All_Constants_Urls.TAG, "getUserList: "+url);
          //  Log.d(All_Constants_Urls.TAG, "getUserList: "+params);


            client.setSSLSocketFactory(
                    new SSLSocketFactory(Common.getSslContext(),
                            SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));

            try {
                client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , Common.DEFAULT_TIMEOUT);
                client.post(url,params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                        if (response != null) {
                            try {
                             //   Log.d("response", response.toString());

                                int status = response.optInt("success");

                                if (status == 0){


                                }else if (status == 1){

                                    user_details = response.toString();


                                    progressDialog.dismiss();

                                    Intent intent = new Intent(Get_Chatroom_msg_Noti.this, ChatroomMembersActivity.class);
                                    intent.putExtra("admin_id", chatroom_admin_id);
                                    intent.putExtra("data", user_details);
                                    startActivity(intent);

                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {

                        progressDialog.dismiss();
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {

            AlertDialog alert1 = new AlertDialog.Builder(Get_Chatroom_msg_Noti.this).create();
            alert1.setMessage("No Internet !");
            alert1.show();
        }
    }


    public void setChatData(String chatData){

        try{
            JSONObject object = new JSONObject(chatData);

            boolean is_mine;
            String msg_Type;
            String user_id=object.getString("user_id");
            String msg_type=object.getString("msg_type");

            if (user_id.matches(sharedPref.get_Use_Id())){
                is_mine=true;
            }else {
                is_mine=false;
            }

            if (msg_type.matches("1")){
                msg_Type="text";
            }else if (msg_type.matches("2")) {
                msg_Type = "img";
            }else if (msg_type.matches("5")) {
                msg_Type = "gif";
            }
            else if (msg_type.matches("6")) {
                msg_Type = "sticker";
            }
            else {
                msg_Type="";
            }


            final ChatMessage chatMessage = new ChatMessage(msg_Type,object.getString("first_name")+" "+object.getString("last_name"), "test", is_mine);
            chatMessage.body = object.getString("content");
            chatMessage.note = "loaded";
            chatMessage.Date = object.getString("create_date");
            chatMessage.img = object.getString("content_img");
            arrData.add(chatMessage);
            adapter.notifyDataSetChanged();

        }catch (Exception e){
            e.printStackTrace();
        }


    }


    @Override
    protected void onStart() {
        Log.d(All_Constants_Urls.TAG, "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(All_Constants_Urls.TAG, "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(All_Constants_Urls.TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(All_Constants_Urls.TAG, "onStop");
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        Log.d(All_Constants_Urls.TAG, "onDestroy");
        this.unregisterReceiver(mMessageReceiver);
        global_class.setChatting_chatroom_id("");
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        if (from.matches("noti_tab")){

            finish();

        }else {

            Intent intent = new Intent(Get_Chatroom_msg_Noti.this, Chat_screen_new.class);
            startActivity(intent);
            finish();

        }

        super.onBackPressed();
    }


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            String message = intent.getStringExtra("message");
            //do other stuff here
            Log.d(All_Constants_Urls.TAG, "Message = "+message);

            setChatData(message);

        }

    };




}

