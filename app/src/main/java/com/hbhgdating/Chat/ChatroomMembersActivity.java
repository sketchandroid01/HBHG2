package com.hbhgdating.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hbhgdating.R;
import com.hbhgdating.from_notification.UserVideoShow_Noti;
import com.hbhgdating.screens.UserProfileActivity;
import com.hbhgdating.utils.All_Constants_Urls;
import com.hbhgdating.utils.Global_Class;
import com.hbhgdating.utils.SharedPref;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Developer on 11/3/17.
 */

public class ChatroomMembersActivity extends AppCompatActivity{

    ImageView imgBack;
    TextView txtTitle;
    ListView lv_members;
    ArrayList<String> list_members;
    ArrayList<String> list_is_matches;
    ArrayList<String> list_userId;
    ChatMembAdapter chatMembAdapter;
    String admin_id;

    Global_Class global_class;
    SharedPref sharedPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom_members);

        initViews();





    }

    public void initViews(){
        imgBack = (ImageView) findViewById(R.id.imgBack);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        lv_members = (ListView) findViewById(R.id.lv_members);

        global_class = (Global_Class)getApplicationContext();
        sharedPref = new SharedPref(this);

        txtTitle.setText(global_class.getSelected_chatroom_name());

        list_members = new ArrayList<>();
        list_is_matches = new ArrayList<>();
        list_userId = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            String data = bundle.getString("data");
            admin_id = bundle.getString("admin_id");


            try{
                JSONObject object = new JSONObject(data);

                JSONArray user_info = object.getJSONArray("user_info");
                for (int i = 0; i < user_info.length(); i++){
                    JSONObject object1 = user_info.getJSONObject(i);

                    JSONObject basic_info = object1.getJSONObject(All_Constants_Urls.basic_info);
                    String is_my_match = basic_info.getString("is_my_match");
                    String id = basic_info.getString("id");

                    list_userId.add(id);
                    list_is_matches.add(is_my_match);
                    list_members.add(object1.toString());

                }

                chatMembAdapter = new ChatMembAdapter(ChatroomMembersActivity.this, list_members,
                        list_userId, list_is_matches, admin_id);
                lv_members.setAdapter(chatMembAdapter);


            }catch (Exception e){
                e.printStackTrace();
            }

        }


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed();
            }
        });




    }




}
