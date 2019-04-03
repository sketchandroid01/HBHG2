package com.hbhgdating.adapter;

import android.app.Activity;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbhgdating.R;
import com.hbhgdating.utils.All_Constants_Urls;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Developer on 10/23/17.
 */

public class Favourite_Search_Adapter extends BaseAdapter{

    private Activity mactivity;
    private LayoutInflater inflater = null;
    private Favourite_Search_Adapter.ViewHolder viewHolder;
    public static MediaPlayer mediaPlayer;
    ArrayList<String> My_matches_list;

    private static class ViewHolder {
        ImageView list_item_image;
        TextView list_item_textname, list_item_textage;
        RelativeLayout relMain;
    }

    public Favourite_Search_Adapter(Activity con, ArrayList<String> my_list) {
        mactivity = con;
        this.My_matches_list = my_list;
        inflater = LayoutInflater.from(mactivity);

    }

    @Override
    public int getCount() {
        return My_matches_list.size();
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
            convertView = inflater.inflate(R.layout.list_item_layout, parent,
                    false);
            viewHolder = new Favourite_Search_Adapter.ViewHolder();

            viewHolder.list_item_image = (ImageView) convertView
                    .findViewById(R.id.list_item_image);
            viewHolder.list_item_textname = (TextView) convertView
                    .findViewById(R.id.list_item_textname);
            viewHolder.list_item_textage = (TextView) convertView
                    .findViewById(R.id.list_item_textage);
            viewHolder.relMain = (RelativeLayout) convertView
                    .findViewById(R.id.relMain);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (Favourite_Search_Adapter.ViewHolder) convertView.getTag();

        }

        //viewHolder.list_item_image .setImageResource(mThumbIds[position]);
        //viewHolder.list_item_image.setScaleType(ImageView.ScaleType.CENTER_CROP);
		/*Picasso.with(mactivity)
				.load(mainList.get(position).profileImage)
				.placeholder(R.drawable.men_intv).fit().centerCrop() // optional
				.into(viewHolder.list_item_image);
		viewHolder.list_item_textname.setText(mainList.get(position).firstName);
		viewHolder.list_item_textage.setText(mainList.get(position).age + ", "+ mainList.get(position).gender);*/


        try{

            JSONObject object = new JSONObject(My_matches_list.get(position));

            JSONObject basic_info = object.getJSONObject(All_Constants_Urls.basic_info);

            ////////////

            String name = basic_info.getString(All_Constants_Urls.name);
            String age = basic_info.getString(All_Constants_Urls.age);
            String strGender = basic_info.getString(All_Constants_Urls.gender);
            String strSexual = basic_info.getString(All_Constants_Urls.sexual_orientation);
            String city = basic_info.getString(All_Constants_Urls.city);
            String country = basic_info.getString(All_Constants_Urls.country);
            String profile_image = basic_info.getString(All_Constants_Urls.profile_image);


            viewHolder.list_item_textname.setText(name);
            viewHolder.list_item_textage.setText("Age : "+age);

            //viewHolder.list_item_image .setImageResource(mThumbIds[1]);

            Picasso.with(mactivity).load(profile_image).error(R.mipmap.grid_bg_3).into( viewHolder.list_item_image, new Callback() {
                @Override
                public void onSuccess() {
                    //  Log.d("TAG", "onSuccess");
                }

                @Override
                public void onError() {
                    //  Toast.makeText(mactivity, "An error occurred", Toast.LENGTH_SHORT).show();
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }


        return convertView;
    }


    private Integer[] mThumbIds = {
            R.drawable.ss1, R.drawable.ss2};








}
