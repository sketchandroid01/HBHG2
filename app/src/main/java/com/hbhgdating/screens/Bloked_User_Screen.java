package com.hbhgdating.screens;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.hbhgdating.R;
import com.hbhgdating.adapter.Blocked_User_Adapter;
import com.hbhgdating.utils.All_Constants_Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Developer on 10/11/17.
 */

public class Bloked_User_Screen extends AppCompatActivity {

    ImageView imgBack;
    ListView lv_blokeduser;
    String BlockedUser_Data;
    ArrayList<HashMap<String, String>> List_Blocked_User;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bloked_user_list);

        imgBack = (ImageView) findViewById(R.id.imgBack);
        lv_blokeduser = (ListView) findViewById(R.id.lv_blokeduser);

        List_Blocked_User = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            BlockedUser_Data = bundle.getString("data");

            try{

                JSONObject object = new JSONObject(BlockedUser_Data);

                JSONArray data_array = object.getJSONArray(All_Constants_Urls.data);
                for (int i = 0; i < data_array.length(); i++){
                    JSONObject object1 = data_array.getJSONObject(i);

                    JSONObject obj_basic_info = object1.getJSONObject(All_Constants_Urls.basic_info);

                    String id = obj_basic_info.optString(All_Constants_Urls.id);
                    String name = obj_basic_info.optString(All_Constants_Urls.name);
                    String gender = obj_basic_info.optString(All_Constants_Urls.gender);
                    String age = obj_basic_info.optString(All_Constants_Urls.age);
                    String sexual_orientation = obj_basic_info.optString(All_Constants_Urls.sexual_orientation);
                    String city = obj_basic_info.optString(All_Constants_Urls.city);
                    String country = obj_basic_info.optString(All_Constants_Urls.country);
                    String profile_image = obj_basic_info.optString(All_Constants_Urls.profile_image);

                    HashMap<String, String> map = new HashMap<>();
                    map.put(All_Constants_Urls.id, id);
                    map.put(All_Constants_Urls.name, name);
                    map.put(All_Constants_Urls.gender, gender);
                    map.put(All_Constants_Urls.age, age);
                    map.put(All_Constants_Urls.sexual_orientation, sexual_orientation);
                    map.put(All_Constants_Urls.city, city);
                    map.put(All_Constants_Urls.country, country);
                    map.put(All_Constants_Urls.profile_image, profile_image);

                    List_Blocked_User.add(map);

                }




            }catch (Exception e){
                e.printStackTrace();
            }


            Blocked_User_Adapter blocked_user_adapter = new Blocked_User_Adapter(Bloked_User_Screen.this, List_Blocked_User);
            lv_blokeduser.setAdapter(blocked_user_adapter);

        }



        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
