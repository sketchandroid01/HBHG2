package com.hbhgdating.DatabaseLocal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.DateFormat;
import android.util.Log;

import com.hbhgdating.utils.All_Constants_Urls;
import com.hbhgdating.utils.Common;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ANDRIOD on 11/17/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    private SQLiteDatabase db;

    // initialize the database name...
    private static final String DATABASE_NAME = "HBHG_DB.db";
    // initialize the database version...
    private static final int DATABASE_VERSION = 1;

    // FB image table.......
    public static final String TABLE_FB = "table_fb";
    public static final String ID_FB = "id_fb";
    public static final String IMG_URL_FB = "img_url_fb";

    // FB video table .............
    public static final String TABLE_FB_VIDEO = "table_fb_video";
    public static final String ID_FB_VIDEO = "id_fb_video";
    public static final String THUMB_IMAGE_FB = "thumb_image_fb";
    public static final String VIDEO_URL_FB = "video_url_fb";

    // INSTA image table .............
    public static final String TABLE_INSTA = "table_insta";
    public static final String ID_INSTA = "id_insta";
    public static final String IMG_URL_INSTA = "img_url_insta";

   // INSTA video table .............
    public static final String TABLE_INSTA_VIDEO = "table_insta_video";
    public static final String ID_INSTA_VIDEO = "id_insta_video";
    public static final String THUMB_IMAGE_INSTA = "thumb_image_insta";
    public static final String VIDEO_URL_INSTA = "video_url_insta";

    ///////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////

    // My Matches Table .....
    public static final String TABLE_MY_MATCHES = "table_my_matches";
    public static final String MY_MATCHES_ID = "m_id";
    public static final String MY_MATCHES_USER_ID = "m_user_id";
    public static final String MY_MATCHES_DATA = "m_data";
    public static final String MY_MATCHES_INCOMING_MSG_TIME = "m_msg_time";
    public static final String CHAT_IS_NEW = "chat_is_new";


    // My Chat room Table .....
    public static final String TABLE_MY_CHATROOM = "table_my_chatroom";
    public static final String MY_CM_ID = "my_chatroom_id";
    public static final String MY_CHATROOM_DB_ID = "my_chatroom_db_id";
    public static final String MY_CHATROOM_DATA = "my_chatroom_data";
    public static final String MY_CHATROOM_INCOMING_MSG_TIME = "my_chatroom_msg_time";
    public static final String MY_CHATROOM_CHAT_IS_NEW = "my_chatroom_chat_is_new";



    // Featured Chatroom Table .....
    public static final String TABLE_FEATURED_CHATROOM = "TABLE_FEATURED_CHATROOM";
    public static final String F_CM_ID = "F_CM_ID";
    public static final String F_CHATROOM_DB_ID = "F_CHATROOM_DB_ID";
    public static final String F_CHATROOM_DATA = "F_CHATROOM_DATA";
    public static final String F_CHATROOM_INCOMING_MSG_TIME = "F_CHATROOM_INCOMING_MSG_TIME";
    public static final String F_CHATROOM_IS_NEW = "F_CHATROOM_IS_NEW";




    // 1 - 1 chat Table ....
    public static final String TABLE_SINGLE_CHAT = "table_single_chat";
    public static final String CHAT_ID = "chat_id";
    public static final String CHAT_WITH_ID = "chat_with_id";
    public static final String CHAT_DATA = "chat_data";


    // Group chat Table ....
    public static final String TABLE_GROUP_CHAT = "table_group_chat";
    public static final String GROUP_CHAT_ID = "group_chat_id";
    public static final String CHAT_ROOM_ID = "chat_room_id";
    public static final String GROUP_CHAT_DATA = "group_chat_data";
    public static final String GROUP_CHAT_IS_NEW = "group_chat_is_read";




    ///////////////////////////////////////////////////////////////////////////////////////

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        /////  CREATE_TABLE_FB
        String CREATE_TABLE_FB = "create table if not exists " + TABLE_FB + " ( "
                + ID_FB + " integer primary key autoincrement, "
                + IMG_URL_FB + " text not null);";

        db.execSQL(CREATE_TABLE_FB.trim());
        System.out.println(CREATE_TABLE_FB);
     //   Log.d("TAG", "CREATE_TABLE_PROFILE  > "+CREATE_TABLE_FB);


        ///////  CREATE_TABLE_FB_VIDEO
        String CREATE_TABLE_FB_VIDEO = "create table if not exists " + TABLE_FB_VIDEO + " ( "
                + ID_FB_VIDEO + " integer primary key autoincrement, "
                + THUMB_IMAGE_FB + " text not null, "
                + VIDEO_URL_FB + " text not null);";

        db.execSQL(CREATE_TABLE_FB_VIDEO.trim());
        System.out.println(CREATE_TABLE_FB_VIDEO);
    //    Log.d("TAG", "CREATE_TABLE_FB_VIDEO  > "+CREATE_TABLE_FB_VIDEO);


        //////  CREATE_TABLE_INSTA
        String CREATE_TABLE_INSTA = "create table if not exists " + TABLE_INSTA + " ( "
                + ID_INSTA + " integer primary key autoincrement, "
                + IMG_URL_INSTA + " text not null);";

        db.execSQL(CREATE_TABLE_INSTA.trim());
        System.out.println(CREATE_TABLE_INSTA);
     //   Log.d("TAG", "CREATE_TABLE_INSTA  > "+CREATE_TABLE_INSTA);


        ///////   CREATE_TABLE_INSTA_VIDEO
        String CREATE_TABLE_INSTA_VIDEO = "create table if not exists " + TABLE_INSTA_VIDEO + " ( "
                + ID_INSTA_VIDEO + " integer primary key autoincrement, "
                + THUMB_IMAGE_INSTA + " text not null, "
                + VIDEO_URL_INSTA + " text not null);";

        db.execSQL(CREATE_TABLE_INSTA_VIDEO.trim());
        System.out.println(CREATE_TABLE_INSTA_VIDEO);
     //   Log.d("TAG", "CREATE_TABLE_INSTA_VIDEO  > "+CREATE_TABLE_INSTA_VIDEO);


        /////////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////////////


        ///////   CREATE_TABLE_MY_MATCHES
        String CREATE_TABLE_MY_MATCHES = "create table if not exists " + TABLE_MY_MATCHES + " ( "
                + MY_MATCHES_ID + " integer primary key autoincrement, "
                + MY_MATCHES_USER_ID + " text not null, "
                + MY_MATCHES_DATA + " text not null, "
                + CHAT_IS_NEW + " text not null, "
                + MY_MATCHES_INCOMING_MSG_TIME + " text not null);";

        db.execSQL(CREATE_TABLE_MY_MATCHES.trim());
        System.out.println(CREATE_TABLE_MY_MATCHES);
     //   Log.d("TAG", "CREATE_TABLE_MY_MATCHES  > "+CREATE_TABLE_MY_MATCHES);


        ///////   CREATE_TABLE_CHATROOM
        String CREATE_TABLE_CHATROOM = "create table if not exists " + TABLE_MY_CHATROOM + " ( "
                + MY_CM_ID + " integer primary key autoincrement, "
                + MY_CHATROOM_DB_ID + " text not null, "
                + MY_CHATROOM_DATA + " text not null, "
                + MY_CHATROOM_INCOMING_MSG_TIME + " text not null, "
                + MY_CHATROOM_CHAT_IS_NEW + " text not null);";

        db.execSQL(CREATE_TABLE_CHATROOM.trim());
        System.out.println(CREATE_TABLE_CHATROOM);
     //   Log.d("TAG", "CREATE_TABLE_CHATROOM  > "+CREATE_TABLE_CHATROOM);


        ///////    CREATE_TABLE_SINGLE_CHAT
        String CREATE_TABLE_SINGLE_CHAT = "create table if not exists " + TABLE_SINGLE_CHAT + " ( "
                + CHAT_ID + " integer primary key autoincrement, "
                + CHAT_WITH_ID + " text not null, "
                + CHAT_DATA + " text not null);";

        db.execSQL(CREATE_TABLE_SINGLE_CHAT.trim());
        System.out.println(CREATE_TABLE_SINGLE_CHAT);
     //   Log.d("TAG", "CREATE_TABLE_SINGLE_CHAT  > "+CREATE_TABLE_SINGLE_CHAT);


        ///////    CREATE_TABLE_GROUP_CHAT
        String CREATE_TABLE_GROUP_CHAT = "create table if not exists " + TABLE_GROUP_CHAT + " ( "
                + GROUP_CHAT_ID + " integer primary key autoincrement, "
                + CHAT_ROOM_ID + " text not null, "
                + GROUP_CHAT_DATA + " text not null, "
                + GROUP_CHAT_IS_NEW + " text not null);";

        db.execSQL(CREATE_TABLE_GROUP_CHAT.trim());
        System.out.println(CREATE_TABLE_GROUP_CHAT);
     //   Log.d("TAG", "CREATE_TABLE_GROUP_CHAT  > "+CREATE_TABLE_GROUP_CHAT);



        ///////   CREATE_TABLE_FEATURED_CHATROOM
        String CREATE_TABLE_FEATURED_CHATROOM = "create table if not exists " + TABLE_FEATURED_CHATROOM + " ( "
                + F_CM_ID + " integer primary key autoincrement, "
                + F_CHATROOM_DB_ID + " text not null, "
                + F_CHATROOM_DATA + " text not null, "
                + F_CHATROOM_INCOMING_MSG_TIME + " text not null, "
                + F_CHATROOM_IS_NEW + " text not null);";

        db.execSQL(CREATE_TABLE_FEATURED_CHATROOM.trim());
        System.out.println(CREATE_TABLE_FEATURED_CHATROOM);
     //   Log.d("TAG", "CREATE_TABLE_CHATROOM  > "+CREATE_TABLE_FEATURED_CHATROOM);










    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FB);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FB_VIDEO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INSTA_VIDEO);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MY_MATCHES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MY_CHATROOM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SINGLE_CHAT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP_CHAT);

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEATURED_CHATROOM);


        // Create tables again
        onCreate(db);
    }


    ////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////

    //////////////////////// Data entry in DB

    //// My Matches ....

    public void entryMyMatches(MyMatchesData matchesData) {

        if (exists_MY_MATCHES_USER_ID(matchesData.getMY_MATCHES_USER_ID())){

            SQLiteDatabase dbase = this.getReadableDatabase();

            //WHERE clause
            String selection1 = DatabaseHelper.MY_MATCHES_USER_ID + " = ?" ;

            //WHERE clause arguments
            String[] selectionArgs1 = {matchesData.getMY_MATCHES_USER_ID()};


            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.MY_MATCHES_DATA, matchesData.getMY_MATCHES_DATA());

            dbase.update(DatabaseHelper.TABLE_MY_MATCHES, values, selection1 , selectionArgs1);

            dbase.close();

         //   Log.d(All_Constants_Urls.TAG, "data update successful");

        }else {

            SQLiteDatabase dbase = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.MY_MATCHES_USER_ID, matchesData.getMY_MATCHES_USER_ID());
            values.put(DatabaseHelper.MY_MATCHES_DATA, matchesData.getMY_MATCHES_DATA());
            values.put(DatabaseHelper.MY_MATCHES_INCOMING_MSG_TIME, matchesData.getMY_MATCHES_INCOMING_MSG_TIME());
            values.put(DatabaseHelper.CHAT_IS_NEW, matchesData.getCHAT_IS_NEW());

            dbase.insert(DatabaseHelper.TABLE_MY_MATCHES, null, values);
            dbase.close();

          //  Log.d(All_Constants_Urls.TAG, "TABLE_MY_MATCHES inserted value");
        }


    }

    public boolean exists_MY_MATCHES_USER_ID(String user_id) {

        SQLiteDatabase dbase = this.getReadableDatabase();

        String[] columns = { MY_MATCHES_USER_ID };
        String selection = MY_MATCHES_USER_ID + " = ?";
        String[] selectionArgs = { user_id };
        String limit = "1";

        Cursor cursor = dbase.query(TABLE_MY_MATCHES, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        dbase.close();

        return exists;
    }

    public void update_isNewMsg_TABLE_MY_MATCHES(String user_id) {

        SQLiteDatabase dbase = this.getReadableDatabase();

        //WHERE clause
        String selection1 = DatabaseHelper.MY_MATCHES_USER_ID + " = ?" ;

        //WHERE clause arguments
        String[] selectionArgs1 = {user_id};

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.CHAT_IS_NEW, "0");

        dbase.update(DatabaseHelper.TABLE_MY_MATCHES, values, selection1 , selectionArgs1);

        dbase.close();

      //  Log.d(All_Constants_Urls.TAG, "CHAT_IS_NEW update successful");
    }

    public ArrayList<MyMatchesData> getDataFromTABLE_MY_MATCHES(){

        MyMatchesData matchesData;
        ArrayList<MyMatchesData> dataArrayList = new ArrayList<>();
        SQLiteDatabase dbase = this.getReadableDatabase();

        String orderBy = DatabaseHelper.MY_MATCHES_INCOMING_MSG_TIME + " DESC";

        Cursor cursor = dbase.query(DatabaseHelper.TABLE_MY_MATCHES, null, null, null, null, null, orderBy);

       // Log.d(All_Constants_Urls.TAG, "TABLE_MY_MATCHES > "+cursor.getCount());


        while (cursor.moveToNext()){

            String MY_MATCHES_USER_ID = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MY_MATCHES_USER_ID));
            String MY_MATCHES_DATA = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MY_MATCHES_DATA));
            String MY_MATCHES_INCOMING_MSG_TIME = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MY_MATCHES_INCOMING_MSG_TIME));
            String CHAT_IS_NEW = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CHAT_IS_NEW));

            matchesData = new MyMatchesData();

            matchesData.setMY_MATCHES_USER_ID(MY_MATCHES_USER_ID);
            matchesData.setMY_MATCHES_DATA(MY_MATCHES_DATA);
            matchesData.setMY_MATCHES_INCOMING_MSG_TIME(MY_MATCHES_INCOMING_MSG_TIME);
            matchesData.setCHAT_IS_NEW(CHAT_IS_NEW);

            dataArrayList.add(matchesData);

        }

        cursor.close();
        dbase.close();

        return dataArrayList;

    }

    public void setMessageDateTime(String user_id, String is_seen){

        SQLiteDatabase dbase = this.getReadableDatabase();

        //WHERE clause
        String selection1 = DatabaseHelper.MY_MATCHES_USER_ID + " = ?" ;

        //WHERE clause arguments
        String[] selectionArgs1 = {user_id};

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.MY_MATCHES_INCOMING_MSG_TIME, convertDate());
        values.put(DatabaseHelper.CHAT_IS_NEW, is_seen);

        dbase.update(DatabaseHelper.TABLE_MY_MATCHES, values, selection1 , selectionArgs1);

        dbase.close();

       // Log.d(All_Constants_Urls.TAG, "time And is_seen update successful");

    }

///////////////////////////////////////
    //// My Chatroom ....

    public void entryMY_Chatrooms(ChatroomData chatroomData) {

        if (exists_MY_CHATROOM_DB_ID(chatroomData.getCHATROOM_DB_ID())){

            SQLiteDatabase dbase = this.getReadableDatabase();

            //WHERE clause
            String selection1 = DatabaseHelper.MY_CHATROOM_DB_ID + " = ?" ;

            //WHERE clause arguments
            String[] selectionArgs1 = {chatroomData.getCHATROOM_DB_ID()};


            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.MY_CHATROOM_DATA, chatroomData.getCHATROOM_DATA());

            dbase.update(DatabaseHelper.TABLE_MY_CHATROOM, values, selection1 , selectionArgs1);

            dbase.close();

          //  Log.d(All_Constants_Urls.TAG, "TABLE_MY_CHATROOM update successful");


        }else {

            SQLiteDatabase dbase = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.MY_CHATROOM_DB_ID, chatroomData.getCHATROOM_DB_ID());
            values.put(DatabaseHelper.MY_CHATROOM_DATA, chatroomData.getCHATROOM_DATA());
            values.put(DatabaseHelper.MY_CHATROOM_INCOMING_MSG_TIME, chatroomData.getCHATROOM_INCOMING_MSG_TIME());
            values.put(DatabaseHelper.MY_CHATROOM_CHAT_IS_NEW, chatroomData.getCHATROOM_CHAT_IS_NEW());

            dbase.insert(DatabaseHelper.TABLE_MY_CHATROOM, null, values);
            dbase.close();

          //  Log.d(All_Constants_Urls.TAG, "TABLE_MY_CHATROOM inserted value");
        }

    }

    public boolean exists_MY_CHATROOM_DB_ID(String chatroom_id) {

        SQLiteDatabase dbase = this.getReadableDatabase();

        String[] columns = { MY_CHATROOM_DB_ID };
        String selection = MY_CHATROOM_DB_ID + " = ?";
        String[] selectionArgs = { chatroom_id };
        String limit = "1";

        Cursor cursor = dbase.query(TABLE_MY_CHATROOM, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        dbase.close();

        return exists;
    }

    public void update_isNewMsg_TABLE_MY_CHATROOM(String chatroom_id) {

        SQLiteDatabase dbase = this.getReadableDatabase();

        //WHERE clause
        String selection1 = DatabaseHelper.MY_CHATROOM_DB_ID + " = ?" ;

        //WHERE clause arguments
        String[] selectionArgs1 = {chatroom_id};

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.MY_CHATROOM_CHAT_IS_NEW, "0");

        dbase.update(DatabaseHelper.TABLE_MY_CHATROOM, values, selection1 , selectionArgs1);

        dbase.close();

     //   Log.d(All_Constants_Urls.TAG, "CHAT_IS_NEW update successful");
    }

    public ArrayList<ChatroomData> getDataFromTABLE_MY_CHATROOM(){

        ChatroomData chatroomData;
        ArrayList<ChatroomData> dataArrayList = new ArrayList<>();
        SQLiteDatabase dbase = this.getReadableDatabase();

        String orderBy = DatabaseHelper.MY_CHATROOM_INCOMING_MSG_TIME + " DESC";;

        Cursor cursor = dbase.query(DatabaseHelper.TABLE_MY_CHATROOM, null, null, null, null, null, orderBy);

       // Log.d("TAG", "cursor.getCount()  > "+cursor.getCount());

        while (cursor.moveToNext()){

            String CHATROOM_DB_ID = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MY_CHATROOM_DB_ID));
            String CHATROOM_DATA = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MY_CHATROOM_DATA));
            String CHATROOM_INCOMING_MSG_TIME = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MY_CHATROOM_INCOMING_MSG_TIME));
            String CHATROOM_CHAT_IS_NEW = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MY_CHATROOM_CHAT_IS_NEW));

            chatroomData = new ChatroomData();

            chatroomData.setCHATROOM_DB_ID(CHATROOM_DB_ID);
            chatroomData.setCHATROOM_DATA(CHATROOM_DATA);
            chatroomData.setCHATROOM_INCOMING_MSG_TIME(CHATROOM_INCOMING_MSG_TIME);
            chatroomData.setCHATROOM_CHAT_IS_NEW(CHATROOM_CHAT_IS_NEW);

            dataArrayList.add(chatroomData);

        }
        cursor.close();
        dbase.close();

        return dataArrayList;

    }

    public void setMyChatroomMessageDateTime(String chatroom_id, String is_seen){

        SQLiteDatabase dbase = this.getReadableDatabase();

        //WHERE clause
        String selection1 = DatabaseHelper.MY_CHATROOM_DB_ID + " = ?" ;

        //WHERE clause arguments
        String[] selectionArgs1 = {chatroom_id};

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.MY_CHATROOM_INCOMING_MSG_TIME, convertDate());
        values.put(DatabaseHelper.MY_CHATROOM_CHAT_IS_NEW, is_seen);

        dbase.update(DatabaseHelper.TABLE_MY_CHATROOM, values, selection1 , selectionArgs1);

        dbase.close();

     //   Log.d(All_Constants_Urls.TAG, "time And is_seen update successful");

    }

    public String getChatroomData(String chatroom_id){

        String CHATROOM_DATA_ = "";

        SQLiteDatabase dbase = this.getReadableDatabase();

        //WHERE clause
        String selection1 = DatabaseHelper.MY_CHATROOM_DB_ID + " = ?" ;

        //WHERE clause arguments
        String[] selectionArgs1 = {chatroom_id};

        Cursor cursor = dbase.query(DatabaseHelper.TABLE_MY_CHATROOM, null, selection1, selectionArgs1, null, null, null);

     //   Log.d("TAG", "cursor.getCount()  > "+cursor.getCount());

        while (cursor.moveToNext()){

            CHATROOM_DATA_ = cursor.getString(cursor.getColumnIndex(DatabaseHelper.MY_CHATROOM_DATA));

        }
        cursor.close();
        dbase.close();


        return CHATROOM_DATA_;
    }


    //////////////////////////////////////////////
    //// Featured Chatroom...


    public void entryFeatured_Chatrooms(FeaturedChatroomData featuredChatroomData) {

        if (exists_F_CHATROOM_DB_ID(featuredChatroomData.getF_CHATROOM_DB_ID())){

            SQLiteDatabase dbase = this.getReadableDatabase();

            //WHERE clause
            String selection1 = DatabaseHelper.F_CHATROOM_DB_ID + " = ?" ;

            //WHERE clause arguments
            String[] selectionArgs1 = {featuredChatroomData.getF_CHATROOM_DB_ID()};


            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.F_CHATROOM_DATA, featuredChatroomData.getF_CHATROOM_DATA());

            dbase.update(DatabaseHelper.TABLE_FEATURED_CHATROOM, values, selection1 , selectionArgs1);

            dbase.close();

         //   Log.d(All_Constants_Urls.TAG, "TABLE_FEATURED_CHATROOM update successful");


        }else {

            SQLiteDatabase dbase = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(DatabaseHelper.F_CHATROOM_DB_ID, featuredChatroomData.getF_CHATROOM_DB_ID());
            values.put(DatabaseHelper.F_CHATROOM_DATA, featuredChatroomData.getF_CHATROOM_DATA());
            values.put(DatabaseHelper.F_CHATROOM_INCOMING_MSG_TIME, featuredChatroomData.getF_CHATROOM_INCOMING_MSG_TIME());
            values.put(DatabaseHelper.F_CHATROOM_IS_NEW, featuredChatroomData.getF_CHATROOM_IS_NEW());

            dbase.insert(DatabaseHelper.TABLE_FEATURED_CHATROOM, null, values);
            dbase.close();

         //   Log.d(All_Constants_Urls.TAG, "TABLE_FEATURED_CHATROOM inserted value");
        }

    }

    public boolean exists_F_CHATROOM_DB_ID(String chatroom_id) {

        SQLiteDatabase dbase = this.getReadableDatabase();

        String[] columns = { F_CHATROOM_DB_ID };
        String selection = F_CHATROOM_DB_ID + " = ?";
        String[] selectionArgs = { chatroom_id };
        String limit = "1";

        Cursor cursor = dbase.query(TABLE_FEATURED_CHATROOM, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        dbase.close();

        return exists;
    }

    public void update_isNeweF_CHATROOM(String chatroom_id) {

        SQLiteDatabase dbase = this.getReadableDatabase();

        //WHERE clause
        String selection1 = DatabaseHelper.F_CHATROOM_DB_ID + " = ?" ;

        //WHERE clause arguments
        String[] selectionArgs1 = {chatroom_id};

        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.F_CHATROOM_IS_NEW, "0");

        dbase.update(DatabaseHelper.TABLE_FEATURED_CHATROOM, values, selection1 , selectionArgs1);

        dbase.close();

      //  Log.d(All_Constants_Urls.TAG, "F_CHATROOM_IS_NEW update successful");
    }

    public ArrayList<FeaturedChatroomData> getDataFromTABLE_FEATURED_CHATROOM(){

        FeaturedChatroomData featuredChatroomData;
        ArrayList<FeaturedChatroomData> dataArrayList = new ArrayList<>();
        SQLiteDatabase dbase = this.getReadableDatabase();

        String orderBy = DatabaseHelper.F_CHATROOM_INCOMING_MSG_TIME + " DESC";

        Cursor cursor = dbase.query(DatabaseHelper.TABLE_FEATURED_CHATROOM, null, null, null, null, null, orderBy);

      //  Log.d("TAG", "cursor.getCount()  > "+cursor.getCount());

        while (cursor.moveToNext()){

            String F_CHATROOM_DB_ID_ = cursor.getString(cursor.getColumnIndex(DatabaseHelper.F_CHATROOM_DB_ID));
            String F_CHATROOM_DATA_ = cursor.getString(cursor.getColumnIndex(DatabaseHelper.F_CHATROOM_DATA));
            String F_CHATROOM_INCOMING_MSG_TIME_ = cursor.getString(cursor.getColumnIndex(DatabaseHelper.F_CHATROOM_INCOMING_MSG_TIME));
            String F_CHATROOM_IS_NEW_ = cursor.getString(cursor.getColumnIndex(DatabaseHelper.F_CHATROOM_IS_NEW));

            featuredChatroomData = new FeaturedChatroomData();

            featuredChatroomData.setF_CHATROOM_DB_ID(F_CHATROOM_DB_ID_);
            featuredChatroomData.setF_CHATROOM_DATA(F_CHATROOM_DATA_);
            featuredChatroomData.setF_CHATROOM_INCOMING_MSG_TIME(F_CHATROOM_INCOMING_MSG_TIME_);
            featuredChatroomData.setF_CHATROOM_IS_NEW(F_CHATROOM_IS_NEW_);

            dataArrayList.add(featuredChatroomData);

        }
        cursor.close();
        dbase.close();

        return dataArrayList;

    }


    /////////////////////////////////////////////////



    public void entrySingleMsg(SingleChatData singleChatData){

        SQLiteDatabase dbase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.CHAT_WITH_ID, singleChatData.getCHAT_WITH_ID());
        values.put(DatabaseHelper.CHAT_DATA, singleChatData.getCHAT_DATA());

        dbase.insert(DatabaseHelper.TABLE_SINGLE_CHAT, null, values);
        dbase.close();

        Log.d(All_Constants_Urls.TAG, "TABLE_SINGLE_CHAT inserted value");

    }


    public void entryChatroomMsg(GroupChatData groupChatData){

        SQLiteDatabase dbase = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DatabaseHelper.CHAT_ROOM_ID, groupChatData.getCHAT_ROOM_ID());
        values.put(DatabaseHelper.GROUP_CHAT_DATA, groupChatData.getGROUP_CHAT_DATA());
        values.put(DatabaseHelper.GROUP_CHAT_IS_NEW, groupChatData.getGROUP_CHAT_IS_NEW());

        dbase.insert(DatabaseHelper.TABLE_GROUP_CHAT, null, values);
        dbase.close();

        Log.d(All_Constants_Urls.TAG, "TABLE_GROUP_CHAT inserted value");

    }



    //////////////////////////////////////////////////////////////
    ///////////////////// get data from DB



    //// My Chatroom ....

    public ArrayList<SingleChatData> getDataFromTABLE_SINGLE_CHAT(String to_user_id){

        Log.d("TAG", "id  > "+to_user_id);

        SingleChatData singleChatData;
        ArrayList<SingleChatData> dataArrayList = new ArrayList<>();

        SQLiteDatabase dbase = this.getReadableDatabase();

        //WHERE clause
        String selection = DatabaseHelper.CHAT_WITH_ID + " = ?" ;

        //WHERE clause arguments
        String[] selectionArgs = {to_user_id};

        Cursor cursor = dbase.query(DatabaseHelper.TABLE_SINGLE_CHAT, null, selection, selectionArgs, null, null, null);

        Log.d("TAG", "cursor.getCount()  > "+cursor.getCount());

        while (cursor.moveToNext()){

            String CHAT_WITH_ID = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CHAT_WITH_ID));
            String CHAT_DATA = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CHAT_DATA));

            singleChatData = new SingleChatData();

            singleChatData.setCHAT_WITH_ID(CHAT_WITH_ID);
            singleChatData.setCHAT_DATA(CHAT_DATA);

            dataArrayList.add(singleChatData);

        }

        cursor.close();
        dbase.close();

        return dataArrayList;
    }

    public ArrayList<GroupChatData> getDataFromTABLE_GROUP_CHAT(String chatroom_id){

        Log.d("TAG", "chatroom_id  > "+chatroom_id);

        GroupChatData groupChatData;
        ArrayList<GroupChatData> dataArrayList = new ArrayList<>();

        SQLiteDatabase dbase = this.getReadableDatabase();

        //WHERE clause
        String selection = DatabaseHelper.CHAT_ROOM_ID + " = ?" ;

        //WHERE clause arguments
        String[] selectionArgs = {chatroom_id};

        Cursor cursor = dbase.query(DatabaseHelper.TABLE_GROUP_CHAT, null, selection, selectionArgs, null, null, null);

        Log.d("TAG", "cursor.getCount()  > "+cursor.getCount());

        while (cursor.moveToNext()){

            String CHAT_ROOM_ID = cursor.getString(cursor.getColumnIndex(DatabaseHelper.CHAT_ROOM_ID));
            String GROUP_CHAT_DATA = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GROUP_CHAT_DATA));
            String GROUP_CHAT_IS_NEW = cursor.getString(cursor.getColumnIndex(DatabaseHelper.GROUP_CHAT_IS_NEW));

            groupChatData = new GroupChatData();

            groupChatData.setCHAT_ROOM_ID(CHAT_WITH_ID);
            groupChatData.setGROUP_CHAT_DATA(CHAT_DATA);
            groupChatData.setGROUP_CHAT_IS_NEW(CHAT_IS_NEW);

            dataArrayList.add(groupChatData);

        }
        cursor.close();
        dbase.close();

        return dataArrayList;
    }






    public void deleteAllDataFromDB(){

        SQLiteDatabase dbase = this.getWritableDatabase();

        dbase.delete(DatabaseHelper.TABLE_MY_MATCHES, null, null);
        dbase.delete(DatabaseHelper.TABLE_FB_VIDEO, null, null);
        dbase.delete(DatabaseHelper.TABLE_INSTA, null, null);
        dbase.delete(DatabaseHelper.TABLE_INSTA_VIDEO, null, null);
        dbase.delete(DatabaseHelper.TABLE_MY_MATCHES, null, null);
        dbase.delete(DatabaseHelper.TABLE_MY_CHATROOM, null, null);
        dbase.delete(DatabaseHelper.TABLE_SINGLE_CHAT, null, null);
        dbase.delete(DatabaseHelper.TABLE_GROUP_CHAT, null, null);
        dbase.delete(DatabaseHelper.TABLE_FEATURED_CHATROOM, null, null);

        dbase.close();
    }















    //////////////////////////////////////////////////////////////////////////////////////////

    ///////////////// delete data from table

   /* public void delete_TABLE_MY_MATCHES(){
        SQLiteDatabase dbase = this.getWritableDatabase();
        dbase.delete(DatabaseHelper.TABLE_MY_MATCHES, null, null);
        dbase.close();
    }


    public void delete_TABLE_CHATROOM(){
        SQLiteDatabase dbase = this.getWritableDatabase();
        dbase.delete(DatabaseHelper.TABLE_CHATROOM, null, null);
        dbase.close();
    }

    public void delete_TABLE_SINGLE_CHAT(){
        SQLiteDatabase dbase = this.getWritableDatabase();
        dbase.delete(DatabaseHelper.TABLE_SINGLE_CHAT, null, null);
        dbase.close();
    }

    public void delete_TABLE_GROUP_CHAT(){
        SQLiteDatabase dbase = this.getWritableDatabase();
        dbase.delete(DatabaseHelper.TABLE_GROUP_CHAT, null, null);
        dbase.close();
    }*/

    /////////////////////
    /////////







    public static String convertDate() {
        long milliSeconds = System.currentTimeMillis();
        String dateFormat = "dd/MM/yyyy hh:mm:ss a";
        String dateFormat1 = "yyyy-MM-dd HH:mm:ss";
        return DateFormat.format(dateFormat1, milliSeconds).toString();
    }



    ///////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////

    public void setJSONDataInDB(String jsonData){

        //////////////////////
        //  entry in DB

        try {
            JSONObject response = new JSONObject(jsonData);

            /////////////////////
            /// My matches data

            MyMatchesData myMatchesData;

            JSONObject admin_details = response.getJSONObject(All_Constants_Urls.admin_details);
            if (admin_details != null){
                JSONObject basic_info = admin_details.getJSONObject(All_Constants_Urls.basic_info);

                myMatchesData = new MyMatchesData();

                myMatchesData.setMY_MATCHES_USER_ID(basic_info.optString("id"));
                myMatchesData.setMY_MATCHES_DATA(admin_details.toString());
                myMatchesData.setMY_MATCHES_INCOMING_MSG_TIME(convertDate());
                myMatchesData.setCHAT_IS_NEW("0");

                entryMyMatches(myMatchesData);

            }


            JSONArray data_array = response.getJSONArray(All_Constants_Urls.data);

            Log.d(All_Constants_Urls.TAG, "length - " +data_array.length());
            for (int i = 0; i < data_array.length(); i++){
                JSONObject object = data_array.getJSONObject(i);

                JSONObject basic_info = object.getJSONObject(All_Constants_Urls.basic_info);

                String UserId = basic_info.getString(All_Constants_Urls.id);

                myMatchesData = new MyMatchesData();

                myMatchesData.setMY_MATCHES_USER_ID(UserId);
                myMatchesData.setMY_MATCHES_DATA(object.toString());
                myMatchesData.setMY_MATCHES_INCOMING_MSG_TIME(convertDate());
                myMatchesData.setCHAT_IS_NEW("0");

                entryMyMatches(myMatchesData);


            }


            //////////////
            /// My chatroom data

            JSONArray my_chatrooms_array = response.getJSONArray(All_Constants_Urls.my_chatrooms);
            for (int i = 0; i < my_chatrooms_array.length(); i++){
                JSONObject object = my_chatrooms_array.getJSONObject(i);

                /*HashMap<String, String> hashmap = new HashMap();
                hashmap.put("id", object.optString("id"));
                hashmap.put("user_id", object.optString("user_id"));
                hashmap.put("title", object.optString("title"));
                hashmap.put("tags", object.optString("tags"));
                hashmap.put("content", object.optString("content"));
                hashmap.put("content_type", object.optString("content_type"));
                hashmap.put("content_img", object.optString("content_img"));
                hashmap.put("content_video", object.optString("content_video"));
                hashmap.put("content_video_img", object.optString("content_video_img"));
                hashmap.put("invited_users", object.optString("invited_users"));
                hashmap.put("create_date", object.optString("create_date"));
                hashmap.put("status", object.optString("status"));
                hashmap.put("count_chats", object.optString("count_chats"));
                hashmap.put("admin_name", object.optString("admin_name"));*/


                ChatroomData chatroomData = new ChatroomData();
                chatroomData.setCHATROOM_DB_ID(object.optString("id"));
                chatroomData.setCHATROOM_DATA(object.toString());
                chatroomData.setCHATROOM_INCOMING_MSG_TIME(convertDate());
                chatroomData.setCHATROOM_CHAT_IS_NEW("0");

                entryMY_Chatrooms(chatroomData);

            }



            JSONArray featured_chatrooms_array = response.getJSONArray(All_Constants_Urls.featured_chatrooms);
            for (int i = 0; i < featured_chatrooms_array.length(); i++){
                JSONObject object = featured_chatrooms_array.getJSONObject(i);

                /*HashMap<String, String> hashmap = new HashMap();
                hashmap.put("id", object.optString("id"));
                hashmap.put("user_id", object.optString("user_id"));
                hashmap.put("title", object.optString("title"));
                hashmap.put("tags", object.optString("tags"));
                hashmap.put("content", object.optString("content"));
                hashmap.put("content_type", object.optString("content_type"));
                hashmap.put("content_img", object.optString("content_img"));
                hashmap.put("content_video", object.optString("content_video"));
                hashmap.put("content_video_img", object.optString("content_video_img"));
                hashmap.put("invited_users", object.optString("invited_users"));
                hashmap.put("create_date", object.optString("create_date"));
                hashmap.put("status", object.optString("status"));
                hashmap.put("count_chats", object.optString("count_chats"));
                hashmap.put("admin_name", object.optString("admin_name"));*/

                FeaturedChatroomData featuredChatroomData = new FeaturedChatroomData();

                featuredChatroomData.setF_CHATROOM_DB_ID(object.optString("id"));
                featuredChatroomData.setF_CHATROOM_DATA(object.toString());
                featuredChatroomData.setF_CHATROOM_INCOMING_MSG_TIME(convertDate());
                featuredChatroomData.setF_CHATROOM_IS_NEW("1");

                entryFeatured_Chatrooms(featuredChatroomData);

            }






        }catch (Exception e){
            e.printStackTrace();
        }



    }



    //////////////////////// testtttttttinnngggggg


}
