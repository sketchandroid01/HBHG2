package com.hbhgdating.adapter;

import android.app.Activity;
import android.app.Dialog;
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
import com.hbhgdating.screens.MainActivity;
import com.hbhgdating.utils.All_Constants_Urls;
import com.hbhgdating.utils.Common;
import com.hbhgdating.utils.Global_Class;
import com.hbhgdating.utils.SharedPref;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;

/**
 * Created by Developer on 10/11/17.
 */

public class Blocked_User_Adapter extends BaseAdapter {

    private Activity mactivity;
    private LayoutInflater inflater = null;
    private Blocked_User_Adapter.ViewHolder viewHolder;
    ArrayList<HashMap<String, String>> Blocked_list;
    Dialog progressDialog;
    int is_blocked = 1;
    SharedPref sharedPref;
    Global_Class global_class;

    private static class ViewHolder {
        ImageView iv_user,iv_remove;
        TextView tv_name, tv_age, tv_orientation,tv_location;
    }

    public Blocked_User_Adapter(Activity con, ArrayList<HashMap<String, String>> blocked_list) {
        mactivity = con;
        this.Blocked_list = blocked_list;
        inflater = LayoutInflater.from(mactivity);

        sharedPref = new SharedPref(mactivity);
        global_class = (Global_Class)mactivity.getApplicationContext();

        progressDialog = new Dialog(mactivity, android.R.style.Theme_Translucent);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setContentView(R.layout.progressbar_pleasewait);
        progressDialog.setCancelable(false);


    }

    @Override
    public int getCount() {
        return Blocked_list.size();
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
            convertView = inflater.inflate(R.layout.bloked_user_list_item, parent, false);
            viewHolder = new Blocked_User_Adapter.ViewHolder();

            viewHolder.iv_user = (ImageView) convertView.findViewById(R.id.iv_user);
            viewHolder.iv_remove = (ImageView) convertView.findViewById(R.id.iv_remove);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_age = (TextView) convertView.findViewById(R.id.tv_age);
            viewHolder.tv_orientation = (TextView) convertView.findViewById(R.id.tv_orientation);
            viewHolder.tv_location = (TextView) convertView.findViewById(R.id.tv_location);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (Blocked_User_Adapter.ViewHolder) convertView.getTag();

        }


        viewHolder.tv_name.setText(Blocked_list.get(position).get(All_Constants_Urls.name));
        viewHolder.tv_age.setText(Blocked_list.get(position).get(All_Constants_Urls.age)+" years");

        String gender_s = "";
        if (Blocked_list.get(position).get(All_Constants_Urls.gender).equals("m")){
            gender_s = "Male";
        }else {
            gender_s = "Female";
        }

        viewHolder.tv_orientation.setText(gender_s+", "
                +Blocked_list.get(position).get(All_Constants_Urls.sexual_orientation));
        viewHolder.tv_location.setText(Blocked_list.get(position).get(All_Constants_Urls.city)+", "
                +Blocked_list.get(position).get(All_Constants_Urls.country));

        Picasso.with(mactivity).load(Blocked_list.get(position).get(All_Constants_Urls.profile_image)).error(R.mipmap.grid_bg_3).into( viewHolder.iv_user, new Callback() {
            @Override
            public void onSuccess() {
                //  Log.d("TAG", "onSuccess");
            }

            @Override
            public void onError() {
                //  Toast.makeText(mactivity, "An error occurred", Toast.LENGTH_SHORT).show();
            }
        });


        viewHolder.iv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BlockDialogBox(Blocked_list.get(position).get(All_Constants_Urls.id), position);

            }
        });


        return convertView;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    private void BlockDialogBox(final String blo_userid, final int pos) {
        String strMessage = "";
        /*if (is_blocked == 0) {
            strMessage = "Are you sure you want to block this user?";
        } else {
            strMessage = "Are you sure you want to unblock this user?";
        }*/

        strMessage = "Are you sure you want to unblock this user?";

        new MaterialDialog.Builder(mactivity)
                .title(R.string.app_name)
                .content(strMessage)
                .positiveText(R.string.ok)
                .negativeText(R.string.cancelcaps)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        try {

                            Block_Unblock_User(blo_userid, pos);

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


    public void Block_Unblock_User(String blo_userid, final int pos){


        progressDialog.show();


        String URL = All_Constants_Urls.BLOCK_USER;
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());
        params.put(All_Constants_Urls.block_user_id, blo_userid);


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

                            Toast.makeText(mactivity, message, Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();


                        }else
                        if (success == 1){

                            global_class.setIf_action_on_block_screen(true);

                            Blocked_list.remove(pos);
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
