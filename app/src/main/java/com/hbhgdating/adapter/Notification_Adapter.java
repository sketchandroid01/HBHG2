package com.hbhgdating.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hbhgdating.R;
import com.hbhgdating.from_notification.Get_Chatroom_msg_Noti;
import com.hbhgdating.from_notification.Get_Matches_msg_Noti;
import com.hbhgdating.from_notification.UserVideoShow_Noti;
import com.hbhgdating.screens.NotificationActivity;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;


import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Developer on 10/11/17.
 */

public class Notification_Adapter extends BaseAdapter {


    private Activity mactivity;
    private LayoutInflater inflater = null;
    private Notification_Adapter.ViewHolder viewHolder;
    ArrayList<String> Msg_list;
    Dialog progressDialog;

    int is_blocked = 1;
    SharedPref sharedPref;
    Global_Class global_class;
    String chatroom_id = "";
    String noti_type, action_did_by_user, Notifications_Id;

    private static class ViewHolder {

        TextView list_item_textname, list_item_textdate;
        RoundedImageView imageViewPro;
        RelativeLayout rl_main;

    }

    public Notification_Adapter(Activity con, ArrayList<String> msg_list) {
        mactivity = con;
        this.Msg_list = msg_list;
        inflater = LayoutInflater.from(mactivity);

        sharedPref = new SharedPref(mactivity);
        global_class = (Global_Class)mactivity.getApplicationContext();

    }

    @Override
    public int getCount() {
        return Msg_list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return arg0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.notification_list_item, parent, false);
            viewHolder = new Notification_Adapter.ViewHolder();

            viewHolder.list_item_textname = (TextView) convertView.findViewById(R.id.list_item_textname);
            viewHolder.list_item_textdate = (TextView) convertView.findViewById(R.id.list_item_textdate);
            viewHolder.imageViewPro = (RoundedImageView) convertView.findViewById(R.id.imageViewPro);
            viewHolder.rl_main = (RelativeLayout)convertView.findViewById(R.id.rl_main);


            convertView.setTag(viewHolder);

        } else {
            viewHolder = (Notification_Adapter.ViewHolder) convertView.getTag();

        }


        try{

            final JSONObject object = new JSONObject(Msg_list.get(position));

            final String noti_content = object.getString(All_Constants_Urls.noti_content);


            viewHolder.list_item_textname.setText(noti_content);


            Picasso.with(mactivity).load(object.getString("sender_image")).error(R.mipmap.grid_bg_3).into( viewHolder.imageViewPro, new Callback() {
                @Override
                public void onSuccess() {
                  //  Log.d("TAG", "onSuccess");
                }

                @Override
                public void onError() {
                  //  Toast.makeText(mactivity, "An error occurred", Toast.LENGTH_SHORT).show();
                }
            });


            SimpleDateFormat simpleDateFormat_from = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat simpleDateFormat_to = new SimpleDateFormat("dd-MMM-yyyy  hh:mm a");

            Date from_date = simpleDateFormat_from.parse(object.getString("create_date"));
            String to_date = simpleDateFormat_to.format(from_date);


         //   Log.d(All_Constants_Urls.TAG, "date = "+to_date);

            viewHolder.list_item_textdate.setText(to_date);



            viewHolder.rl_main.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    try{

                        Notifications_Id = object.getString("id");

                        chatroom_id = object.optString("chatroom_id");

                        noti_type = object.optString("noti_type");

                        action_did_by_user = object.optString("action_did_by");

                    }catch (Exception e){
                        e.printStackTrace();
                    }



                    if (noti_type.matches("1")){
                        // Admin create chatroom


                    }else if (noti_type.matches("4")){
                        // for 1 to 1 chat

                        Intent intent_4 = new Intent(mactivity, Get_Matches_msg_Noti.class);
                        intent_4.putExtra("id", action_did_by_user);
                        intent_4.putExtra("from", "noti_tab");
                        mactivity.startActivity(intent_4);


                    }else if (noti_type.matches("5")){
                        // for chatroom chat

                        Intent intent_5 = new Intent(mactivity, Get_Chatroom_msg_Noti.class);
                        intent_5.putExtra("id", chatroom_id);
                        intent_5.putExtra("from", "noti_tab");
                        mactivity.startActivity(intent_5);


                    }else if (noti_type.matches("8")){
                        /// like user

                        if (noti_content.contains(Common.likes_your_profile)){

                            Intent intent_8 = new Intent(mactivity, UserVideoShow_Noti.class);
                            intent_8.putExtra("id", action_did_by_user);
                            intent_8.putExtra("from", "noti_tab");
                            mactivity.startActivity(intent_8);

                        }else {

                            Intent intent_8 = new Intent(mactivity, UserProfileActivity.class);
                            intent_8.putExtra("userid", action_did_by_user);
                            intent_8.putExtra("from", "noti_tab");
                            mactivity.startActivity(intent_8);

                        }

                    }




                    Delete_Notification_Data(position);
                }
            });



        }catch (Exception e){
            e.printStackTrace();
        }




	/*viewHolder.list_item_textname
				.setText(mainList.get(position).noti_content);
		long timestamp = Long.valueOf(mainList.get(position).create_date);
		String strDate = getDateCurrentTimeZone(timestamp);
		viewHolder.list_item_textdate.setText(strDate);
		viewHolder.relMain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int notification_type = mainList.get(position).noti_type;
				if (notification_type == 1) { // 1 = profile view
					Intent intent1 = new Intent(mactivity,
							UserProfileActivity.class);
					intent1.putExtra("userid", mainList.get(position).action_did_by);
					intent1.putExtra("ismatch", true);
					mactivity.startActivity(intent1);
					mactivity.overridePendingTransition(R.anim.slide_in_up, R.anim.stay);
				}else if (notification_type == 2) { // 2 = profile like
					Intent intent = new Intent(mactivity,
							FollowersActivity.class);
					mactivity.startActivity(intent);
				}else if (notification_type == 3) { // 3 = post chat
					Intent intent = new Intent(mactivity, ChatDetail.class);
					intent.putExtra("to_user_id", mainList.get(position).action_did_by);
					intent.putExtra("title", mainList.get(position).action_did_by_firstname);
					mactivity.startActivity(intent);
				}else if (notification_type == 4) { // 4 = post on chat room
					Intent intent = new Intent(mactivity, ChatDetail.class);
					intent.putExtra("chatroom_id", mainList.get(position).chatroom_id);
					intent.putExtra("title", mainList.get(position).chatroom_title);
					intent.putExtra("created_userid", mainList.get(position).chatroom_created_userid);
					intent.putExtra("invited_users", mainList.get(position).chatroom_invited_users +",You");
					mactivity.startActivity(intent);
				}
			}
		});

		viewHolder.img_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int nID = mainList.get(position).notificationId;
				mainList.remove(position);
				notifyDataSetChanged();
				NotificationActivity notiactivity = (NotificationActivity) mactivity;
				notiactivity.deleteNotification(nID);
			}
		});*/




        return convertView;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    public void Delete_Notification_Data(final int position){


        String URL = All_Constants_Urls.DELETE_NOTIFICATION;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());
        params.put(All_Constants_Urls.noti_id, Notifications_Id);


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
                        String message = response.optString("message");

                        if (success == 0){

                          //  Toast.makeText(mactivity, message, Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();

                        }else
                        if (success == 1){

                            Msg_list.remove(position);
                            notifyDataSetChanged();

                          //  Toast.makeText(mactivity, message, Toast.LENGTH_LONG).show();


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

                android.app.AlertDialog alert
                        = new android.app.AlertDialog.Builder(mactivity).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });


    }



}
