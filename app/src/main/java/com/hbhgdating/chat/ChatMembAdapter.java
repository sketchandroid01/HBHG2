package com.hbhgdating.chat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hbhgdating.R;
import com.hbhgdating.from_notification.UserVideoShow_Noti;
import com.hbhgdating.screens.UserProfileActivity;
import com.hbhgdating.utils.All_Constants_Urls;
import com.hbhgdating.utils.Common;
import com.hbhgdating.utils.Global_Class;
import com.hbhgdating.utils.SharedPref;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;

/**
 * Created by Developer on 11/3/17.
 */

public class ChatMembAdapter extends BaseAdapter{

    private Activity mactivity;
    private LayoutInflater inflater = null;
    private ChatMembAdapter.ViewHolder viewHolder;
    private ArrayList<String> list_members;
    private ArrayList<String> list_is_matches;
    private ArrayList<String> list_userId;
    private Dialog progressDialog;

    private DisplayImageOptions defaultOptions;
    private ImageLoaderConfiguration config;
    public ImageLoader loader;
    int is_blocked = 1;
    private SharedPref sharedPref;
    private Global_Class global_class;
    private boolean is_me_admin = false;
    //private ArrayList<String> list_userids;
    private String admin_id;


    private static class ViewHolder {
        ImageView iv_popup;
        RoundedImageView iv_user;
        TextView tv_name, tv_is_admin;
        RelativeLayout rel_info;

        public ViewHolder(View view) {
            this.iv_user = view.findViewById(R.id.iv_user);
            this.iv_popup = view.findViewById(R.id.iv_popup);
            this.tv_name = view.findViewById(R.id.tv_name);
            this.tv_is_admin = view.findViewById(R.id.tv_is_admin);
            this.rel_info = view.findViewById(R.id.rel_info);
        }
    }

    public ChatMembAdapter(Activity con, ArrayList<String> list_members_,
                           ArrayList<String> list_userId, ArrayList<String> list_is_matches,
                           String admin_id_) {

        mactivity = con;
        this.list_members = list_members_;
        this.list_userId = list_userId;
        this.list_is_matches = list_is_matches;
        this.admin_id = admin_id_;
        this.inflater = LayoutInflater.from(mactivity);
        sharedPref = new SharedPref(mactivity);
        global_class = (Global_Class)mactivity.getApplicationContext();

        progressDialog = new Dialog(mactivity, android.R.style.Theme_Translucent);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setContentView(R.layout.progressbar_pleasewait);
        progressDialog.setCancelable(false);

        //list_userids = new ArrayList<>();

        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.mipmap.grid_bg_3)
                .showImageOnFail(R.mipmap.grid_bg_3)
                .showImageOnLoading(R.mipmap.grid_bg_3).build();
        config = new ImageLoaderConfiguration.Builder(con)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);
        loader = ImageLoader.getInstance();


        if (admin_id.equals(sharedPref.get_Use_Id())){
            is_me_admin = true;
        }

    }

    @Override
    public int getCount() {
        return list_members.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list_members.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        View convertView = inflater.inflate(R.layout.chatroom_memeber_list_item, null, true);
            viewHolder = new ViewHolder(convertView);
            viewHolder.iv_popup.setVisibility(View.INVISIBLE);

            convertView.setTag(viewHolder);

            try{

                JSONObject object = new JSONObject(list_members.get(position));

                JSONObject basic_info = object.getJSONObject(All_Constants_Urls.basic_info);

                //list_userids.add(basic_info.optString(All_Constants_Urls.id));

                final String userId = basic_info.optString(All_Constants_Urls.id);

                if (basic_info.optString(All_Constants_Urls.id).equals(sharedPref.get_Use_Id()) &&
                        basic_info.optString(All_Constants_Urls.type).equals(All_Constants_Urls.admin)){

                    viewHolder.tv_name.setText("Me");

                    viewHolder.tv_is_admin.setText("Chatroom Admin");


                }else if (basic_info.optString(All_Constants_Urls.id).equals(sharedPref.get_Use_Id())){

                    viewHolder.tv_name.setText("Me");

                    viewHolder.tv_is_admin.setText("Chatroom Member");

                }else {

                    viewHolder.tv_name.setText(basic_info.getString(All_Constants_Urls.name));

                    if (basic_info.optString(All_Constants_Urls.type).equals(All_Constants_Urls.admin)){

                        viewHolder.tv_is_admin.setText("Chatroom Admin");
                    }else {

                        viewHolder.tv_is_admin.setText("Chatroom Member");
                    }

                }

                loader.displayImage(basic_info.getString(All_Constants_Urls.profile_image), viewHolder.iv_user);



                if (is_me_admin){

                    if (!basic_info.optString(All_Constants_Urls.id).equals(sharedPref.get_Use_Id()))
                        viewHolder.iv_popup.setVisibility(View.VISIBLE);

                }else {

                    viewHolder.iv_popup.setVisibility(View.INVISIBLE);
                }


                viewHolder.iv_popup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Log.d(All_Constants_Urls.TAG, "position - "+position);

                        blockDialogBox(userId, position);

                    }
                });



            }catch (Exception e){
                e.printStackTrace();
            }





            viewHolder.rel_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    clickOnMember(position);
                }
            });


        return convertView;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private void clickOnMember(int position){

        Log.d(All_Constants_Urls.TAG, "id = "+list_userId.get(position));

        if (sharedPref.get_Use_Id().matches(list_userId.get(position))){

            /// it's me

        }else {

            if (list_is_matches.get(position).matches("0")){
                // new user

                Intent intent = new Intent(mactivity, UserVideoShow_Noti.class);
                intent.putExtra("id", list_userId.get(position));
                intent.putExtra("from", "chatroom");
                mactivity.startActivity(intent);


            }else if (list_is_matches.get(position).matches("1")){
                // my matches
                Intent intent1 = new Intent(mactivity, UserProfileActivity.class);
                intent1.putExtra("userid", list_userId.get(position));
                intent1.putExtra("data", list_members.get(position));
                mactivity.startActivity(intent1);
                mactivity.overridePendingTransition(R.anim.slide_in_up, R.anim.stay);


            }

        }



    }


    private void blockDialogBox(final String blo_userid, final int position) {
        String strMessage = "";
        /*if (is_blocked == 0) {
            strMessage = "Are you sure you want to block this user?";
        } else {
            strMessage = "Are you sure you want to unblock this user?";
        }*/

        strMessage = "Are you sure, you want to block this user from chatroom '"+global_class.getSelected_chatroom_name()+"' ?";

        new MaterialDialog.Builder(mactivity)
                .title(R.string.app_name)
                .content(strMessage)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancelcaps)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        try {

                            blockUser(blo_userid, position);

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


    public void blockUser(String blo_userid, final int position){

        progressDialog.show();

        String URL = All_Constants_Urls.blockuserfromchatroom;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());
        params.put(All_Constants_Urls.block_user_id, blo_userid);
        params.put(All_Constants_Urls.chatroom_id, global_class.getSelected_chatroom_id());


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

              //  Log.d(TAG, "onSuccess- " + response.toString());

                if (response != null) {
                    try {


                        int success = response.optInt("success");
                        String message = response.optString("message");

                        if (success == 0){

                            Toast.makeText(mactivity, message, Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();


                        }else
                        if (success == 1){

                            Log.d(All_Constants_Urls.TAG, "position - "+position);

                            list_members.remove(position);
                            list_is_matches.remove(position);
                            list_userId.remove(position);

                            notifyDataSetChanged();

                            Toast.makeText(mactivity, message, Toast.LENGTH_LONG).show();

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

                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(mactivity).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });

    }



}

