package com.hbhgdating.services;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hbhgdating.chat.Chat_screen_new;
import com.hbhgdating.databaseLocal.DatabaseHelper;
import com.hbhgdating.databaseLocal.SingleChatData;
import com.hbhgdating.R;
import com.hbhgdating.from_notification.Get_Matches_msg_Noti;
import com.hbhgdating.from_notification.UserVideoShow_Noti;
import com.hbhgdating.screens.FavoriteActivity;
import com.hbhgdating.utils.All_Constants_Urls;
import com.hbhgdating.utils.Common;
import com.hbhgdating.utils.Global_Class;
import com.hbhgdating.utils.SharedPref;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;


/**
 * Created by ANDROID on 8/27/2016.
 */
public class MyFcmListenerService extends FirebaseMessagingService {

    private String TAG = All_Constants_Urls.TAG;
    private SharedPref sharedPref;
    private String chat_msg;
    private String not_id = null;
    private String user_chatroom_id = null;
    public static String noti_type = "0";
    private String notificationActivity = "com.hbhgdating.screens.NotificationActivity";
    private String favouriteActivity = "com.hbhgdating.screens.FavoriteActivity";
    private String chatActivity = "com.hbhgdating.chat.Chat_screen_new";
    private String matches_chat_screen = "com.hbhgdating.chat.Get_Matches_msg";
    private String chatroom_chat_screen = "com.hbhgdating.chat.Get_Chatroom_msg";
    private String Get_Matches_msg_Noti = "com.hbhgdating.from_notification.Get_Matches_msg_Noti";
    private String Get_Chatroom_msg_Noti = "com.hbhgdating.from_notification.Get_Chatroom_msg_Noti";
    private String is_seen_msg = "0";
    private String chatroom_details;

    private int badgeCount = 1;

    DatabaseHelper databaseHelper;
    Global_Class global_class;


    @Override
    public void onMessageReceived(RemoteMessage message){
        String from = message.getFrom();
        Map data = message.getData();
        Log.d(TAG, "received "+from);
        Log.d(TAG, "data "+data.toString());
        Log.d(TAG, "is Running = "+applicationInForeground());

        ShortcutBadger.applyCount(getApplicationContext(), badgeCount);
        badgeCount++;



        global_class = (Global_Class)getApplicationContext();
        sharedPref = new SharedPref(getApplicationContext());
        databaseHelper = new DatabaseHelper(getApplicationContext());

        workOnData(message);


        Log.d(TAG, "Activity = "+getForegroundActivity());


    }



    private void workOnData(RemoteMessage message){

        // String title = message.getData().get("title");
        String title = "HBHG";
        String body = message.getData().get("body");
        if (message.getData().get("chat_content") != null){
            chat_msg = message.getData().get("chat_content");


        }



        try{

            if (message.getData().get("type").matches("1")){

                noti_type = message.getData().get("type");

                if (message.getData().get("chatroom_details") != null){

                    chatroom_details = message.getData().get("chatroom_details");
                }


            }else if (message.getData().get("type").matches("4")){

                noti_type = message.getData().get("type");

                if (message.getData().get("not_id") != null){
                    not_id = message.getData().get("not_id");
                    user_chatroom_id = message.getData().get("not_id");

                }

            }else if (message.getData().get("type").matches("5")){

                noti_type = message.getData().get("type");

                if (message.getData().get("not_id") != null){
                    not_id = message.getData().get("not_id");
                    user_chatroom_id = message.getData().get("not_id");

                }
            }


            if (message.getData().get("liked_user_id") != null){
                user_chatroom_id = message.getData().get("liked_user_id");
                //list_user_chatroom_id.set(Integer.parseInt(not_id), user_chatroom_id);
            }


        }catch (NullPointerException e){
            e.printStackTrace();
        }


        Log.d(TAG, "type = "+message.getData().get("type"));

        if (message.getData().get("type") != null){

            Log.d(TAG, "AAAAAA = "+applicationInForeground());

            if (applicationInForeground()){

                checkType_OnForeground(Integer.parseInt(message.getData().get("type")), title, body);

            }else {

                checkType_OnShutdown(Integer.parseInt(message.getData().get("type")), title, body);

            }

        }else {

            sendNotification(message.getData().get("title"), message.getData().get("body"));

        }


        //sendToService(getApplicationContext(), body);


    }



    private void checkType_OnForeground(int type, String title, String body){

        switch(type) {
            case 1:  // for create chatroom

                entryFeaturedChatroom();

                sendNotificationCreateChatroom(title, body);

                if (getForegroundActivity().matches(chatActivity)){

                    refreshChatRooms(getApplicationContext(), body);

                }else {

                    sharedPref.save_My_Matches(null);

                }

                if (getForegroundActivity().matches(notificationActivity)){

                    refreshNotificationData(getApplicationContext(), body);

                }else {

                    sharedPref.save_Notification(null);

                }

                break;
            case 2:  // for delete chatroom

                if (getForegroundActivity().matches(chatActivity)){

                    refreshChatRooms(getApplicationContext(), body);

                }else {

                    sharedPref.save_My_Matches(null);

                }

                break;
            case 3:  // Add profile videos,  Add food,  Add interest, Add music

                if (getForegroundActivity().matches(favouriteActivity)){

                    refreshMymatchesData(getApplicationContext(), body);

                }else {

                    sharedPref.save_My_Matches(null);

                }

                break;
            case 4:  // 1-1 Chat

                Log.d(TAG, "user id = "+ user_chatroom_id);

                sendNotificationForSingleChat(title, body, user_chatroom_id);


                if (global_class.getChatting_user_id().equals("")){

                    is_seen_msg = "1";  // unseen

                }else {
                    if (global_class.getChatting_user_id().matches(user_chatroom_id)){
                        is_seen_msg = "0";  // seen
                    }else {
                        is_seen_msg = "1";  // unseen
                    }
                }

                entrySingleMsg(chat_msg);

                Log.d(TAG, "chat user id = "+ global_class.getChatting_user_id());
                Log.d(TAG, "user id = "+ user_chatroom_id);


                if (getForegroundActivity().matches(matches_chat_screen)){

                    if (global_class.getChatting_user_id().matches(user_chatroom_id)){
                        refreshChatData(getApplicationContext(), chat_msg);

                        clearNotification(getNotId());
                    }

                }else if (getForegroundActivity().matches(Get_Matches_msg_Noti)){

                    refreshChatDataNoti(getApplicationContext(), chat_msg);
                }

                if (getForegroundActivity().matches(notificationActivity)){

                    refreshNotificationData(getApplicationContext(), body);

                }else if (getForegroundActivity().matches(chatActivity)){

                    refreshChatRooms(getApplicationContext(), body);

                }else {

                    sharedPref.save_Notification(null);
                }



                break;
            case 5:  // Chatroom Chat

                Log.d(TAG, "user_chatroom_id = "+ user_chatroom_id);

                sendNotificationForChatroom(title, body, user_chatroom_id);


                if (global_class.getChatting_chatroom_id().equals("")){

                    is_seen_msg = "1";  // unseen

                }else {
                    if (global_class.getChatting_chatroom_id().matches(user_chatroom_id)){
                        is_seen_msg = "0";  // seen
                    }else {
                        is_seen_msg = "1";  // unseen
                    }
                }


                entryChatrooms(chat_msg);


                if (getForegroundActivity().matches(chatroom_chat_screen)){

                    if (global_class.getChatting_chatroom_id().matches(user_chatroom_id)){
                        refreshGroupChatData(getApplicationContext(), chat_msg);

                        clearNotification(getNotId());
                    }

                }else if (getForegroundActivity().matches(Get_Chatroom_msg_Noti)){

                    refreshChatroomDataNoti(getApplicationContext(), chat_msg);
                }




                if (getForegroundActivity().matches(notificationActivity)){

                    refreshNotificationData(getApplicationContext(), body);

                }else if (getForegroundActivity().matches(chatActivity)){

                    refreshChatRooms(getApplicationContext(), body);

                }else {

                    sharedPref.save_Notification(null);

                }

                break;

            case 6:  // Admin warning user

                sendNotification(title, body);

                if (getForegroundActivity().matches(notificationActivity)){

                    refreshNotificationData(getApplicationContext(), body);

                }else {

                    sharedPref.save_Notification(null);

                }

                break;

            case 7:  // Admin Folder Notification

                sendNotification(title, body);

                if (getForegroundActivity().matches(notificationActivity)){

                    refreshNotificationData(getApplicationContext(), body);

                }else {

                    sharedPref.save_Notification(null);

                }

                break;

            case 8:  //Like notification

           // {liked_user_id=24, body=sourav likes your profile, type=8, title=HBHG Profile Like, message=hello}
          //  {liked_user_id=24, body=Congrats , It’s a Match ! , type=8, title=HBHG Profile Like, message=hello}


                if (body.contains(Common.likes_your_profile)){

                    Log.d(TAG, Common.likes_your_profile);

                    sendNotificationForUser(title, body, user_chatroom_id);


                }else {

                    Log.d(TAG, body);

                    sendNotification(title, body);

                }



                if (getForegroundActivity().matches(notificationActivity)){

                    refreshNotificationData(getApplicationContext(), body);

                }else {

                    sharedPref.save_Notification(null);

                }

                if (getForegroundActivity().matches(favouriteActivity)){

                    refreshMymatchesData(getApplicationContext(), body);

                }else {

                    sharedPref.save_My_Matches(null);

                }

                break;
        }
    }


    private void checkType_OnShutdown(int type, String title, String body){

        switch(type) {
            case 1:  // for create chatroom

                sendNotificationCreateChatroom(title, body);

                entryFeaturedChatroom();

                sharedPref.save_My_Matches(null);


                break;
            case 2:  // for delete chatroom

                sharedPref.save_My_Matches(null);

                break;
            case 3:  // Add profile videos,  Add food,  Add interest, Add music

                sharedPref.save_My_Matches(null);

                break;
            case 4:  // 1-1 Chat

                sendNotificationForSingleChat(title, body, user_chatroom_id);

                if (global_class.getChatting_user_id().equals("")){

                    is_seen_msg = "1";  // unseen

                }else {

                    if (global_class.getChatting_user_id().matches(user_chatroom_id)){
                        is_seen_msg = "0";  // seen
                    }else {
                        is_seen_msg = "1";  // unseen
                    }
                }


                entrySingleMsg(chat_msg);



                if (getForegroundActivity().matches(matches_chat_screen)){

                    refreshChatData(getApplicationContext(), chat_msg);

                }else if (getForegroundActivity().matches(Get_Matches_msg_Noti)){

                    refreshChatDataNoti(getApplicationContext(), chat_msg);
                }


                refreshChatRooms(getApplicationContext(), body);


                break;
            case 5:  // Chatroom Chat

                Log.d(TAG, "user_chatroom_id = "+ user_chatroom_id);

                sendNotificationForChatroom(title, body, user_chatroom_id);


                if (global_class.getChatting_chatroom_id().equals("")){

                    is_seen_msg = "1";  // unseen

                }else {
                    if (global_class.getChatting_chatroom_id().matches(user_chatroom_id)){
                        is_seen_msg = "0";  // seen
                    }else {
                        is_seen_msg = "1";  // unseen
                    }
                }


                entryChatrooms(chat_msg);



                if (getForegroundActivity().matches(notificationActivity)){

                    refreshNotificationData(getApplicationContext(), body);

                }else if (getForegroundActivity().matches(chatActivity)){

                    refreshChatRooms(getApplicationContext(), body);

                }

                refreshGroupChatData(getApplicationContext(), chat_msg);


                break;
            case 6:  // Admin warning user

                sendNotification(title, body);

                refreshNotificationData(getApplicationContext(), body);


                break;
            case 7:  // Admin Folder Notification

                sendNotification(title, body);

                refreshNotificationData(getApplicationContext(), body);

                break;

            case 8:  //Like notification

                sendNotification(title, body);

                sharedPref.save_My_Matches(null);

                break;
        }


        sharedPref.save_Notification(null);
    }





    private void sendNotification(String messageTitle, String messageBody) {
        Intent intent = new Intent(this, FavoriteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0 /* request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long[] pattern = {400};
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =  new NotificationCompat.Builder(this)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setLights(Color.YELLOW, 1, 1)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            showNotificationForOreo(getApplicationContext(),
                    messageTitle, messageBody, intent);
        }else {
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                notificationManager.notify(getNotId(), notificationBuilder.build());
            } else {
                notificationManager.notify(getNotId(), notificationBuilder.build());
            }

        }



    }

    private void sendNotificationCreateChatroom(String messageTitle,
                                                String messageBody) {
        Intent intent = new Intent(this, Chat_screen_new.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0 /* request code */, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long[] pattern = {400};
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =  new NotificationCompat.Builder(this)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setLights(Color.YELLOW, 1, 1)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            showNotificationForOreo(getApplicationContext(),
                    messageTitle, messageBody, intent);
        }else {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                notificationManager.notify(getNotId(), notificationBuilder.build());
            } else {
                notificationManager.notify(getNotId(), notificationBuilder.build());
            }
        }



    }

    private void sendNotificationForUser(String messageTitle, String messageBody,
                                         String userid) {
        Intent intent = new Intent(this, UserVideoShow_Noti.class);
        intent.putExtra("id", userid);
        intent.setAction(userid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0 /* request code */, intent, PendingIntent.FLAG_ONE_SHOT);
        long[] pattern = {400};
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =  new NotificationCompat.Builder(this)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setLights(Color.YELLOW, 1, 1)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            showNotificationForOreo(getApplicationContext(),
                    messageTitle, messageBody, intent);
        }else {

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                notificationManager.notify(getNotId(), notificationBuilder.build());
            } else {
                notificationManager.notify(getNotId(), notificationBuilder.build());
            }
        }


    }

    private void sendNotificationForSingleChat(String messageTitle,
                                               String messageBody, String userid) {
        Intent intent = new Intent(this, Get_Matches_msg_Noti.class);
        intent.putExtra("id", userid);
        intent.setAction(userid);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0 /* request code */, intent, PendingIntent.FLAG_ONE_SHOT);
        long[] pattern = {400};
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =  new NotificationCompat.Builder(this)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setLights(Color.YELLOW, 1, 1)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            showNotificationForOreo(getApplicationContext(),
                    messageTitle, messageBody, intent);
        }else {

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                notificationManager.notify(getNotId(), notificationBuilder.build());
            } else {
                notificationManager.notify(getNotId(), notificationBuilder.build());
            }
        }



    }

    private void sendNotificationForChatroom(String messageTitle,
                                             String messageBody, String chatroom_id) {

        Intent intent = new Intent(this, com.hbhgdating.from_notification.Get_Chatroom_msg_Noti.class);
        intent.putExtra("id", chatroom_id);
        intent.setAction(chatroom_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0 /* request code */, intent, PendingIntent.FLAG_ONE_SHOT);
        long[] pattern = {400};
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =  new NotificationCompat.Builder(this)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setVibrate(pattern)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS)
                .setContentIntent(pendingIntent);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            showNotificationForOreo(getApplicationContext(),
                    messageTitle, messageBody, intent);
        }else {

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                notificationManager.notify(getNotId(), notificationBuilder.build());
            } else {
                notificationManager.notify(getNotId(), notificationBuilder.build());
            }

        }


        /*if (applicationInForeground()){
            notificationManager.cancel(getNotId());
        }*/

    }




    public void showNotificationForOreo(Context context, String title,
                                        String body, Intent intent) {

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "HBHG";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(getNotificationIcon())
                .setContentTitle(title)
                .setContentText(body);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0, PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(getNotId(), mBuilder.build());
    }




    private int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.hey_girl_boy : R.mipmap.hey_girl_boy;
    }

    private int getNotId(){
        if (not_id != null){
            return Integer.parseInt(not_id);
        }else {
            int id = (int) System.currentTimeMillis();
            return id;
        }
    }

    public class NotificationID {
        int id = (int) System.currentTimeMillis();
        public int getID() {
            return id;
        }
    }


    private String getRemainingTime() {
        String delegate = "hh:mm aaa";
      //  Log.d(TAG, "time = "+DateFormat.format(delegate, Calendar.getInstance().getTime()));

        return (String) DateFormat.format(delegate, Calendar.getInstance().getTime());
    }


    static void refreshChatRooms(Context context, String message) {
        Intent intent = new Intent(Common.Key_Chatrooms);
        //put whatever data you want to send, if any
        intent.putExtra("noti_type", noti_type);
        //send broadcast
        context.sendBroadcast(intent);
    }

    static void refreshMymatchesData(Context context, String message) {
        Intent intent = new Intent(Common.Key_Mymatches);
        //put whatever data you want to send, if any
        intent.putExtra("message", message);
        //send broadcast
        context.sendBroadcast(intent);
    }

    static void refreshNotificationData(Context context, String message) {
        Intent intent = new Intent(Common.Key_Notification);
        //put whatever data you want to send, if any
        intent.putExtra("message", message);
        //send broadcast
        context.sendBroadcast(intent);
    }

    static void refreshChatData(Context context, String message) {
        Intent intent = new Intent(Common.Key_SingleChat);
        //put whatever data you want to send, if any
        intent.putExtra("message", message);
        //send broadcast
        context.sendBroadcast(intent);
    }

    static void refreshChatDataNoti(Context context, String message) {
        Intent intent = new Intent(Common.Key_SingleChatNoti);
        //put whatever data you want to send, if any
        intent.putExtra("message", message);
        //send broadcast
        context.sendBroadcast(intent);
    }



    static void refreshGroupChatData(Context context, String message) {
        Intent intent = new Intent(Common.Key_GroupChat);
        //put whatever data you want to send, if any
        intent.putExtra("message", message);
        //send broadcast
        context.sendBroadcast(intent);
    }

    static void refreshChatroomDataNoti(Context context, String message) {
        Intent intent = new Intent(Common.Key_ChatroomNoti);
        //put whatever data you want to send, if any
        intent.putExtra("message", message);
        //send broadcast
        context.sendBroadcast(intent);
    }



    static void sendToService(Context context, String message) {
        Intent intent = new Intent(Common.Key_Service);
        //put whatever data you want to send, if any
        intent.putExtra("message", message);
        //send broadcast
        context.sendBroadcast(intent);
    }



    private boolean applicationInForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> services = activityManager.getRunningAppProcesses();
        boolean isActivityFound = false;

        if (services.get(0).processName.equalsIgnoreCase(getPackageName())) {
            isActivityFound = true;
        }

        return isActivityFound;
    }


    private String getForegroundActivity(){
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        //Log.d("TAG", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName());
        ComponentName componentInfo = taskInfo.get(0).topActivity;
        componentInfo.getPackageName();

        return taskInfo.get(0).topActivity.getClassName();
    }



    private void clearNotification(int idd){
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(idd);
    }


    ///////////////////////////////////////////////////
    ///////////////  Database work .....

    private void entrySingleMsg(String chat_msg){

        Log.d(TAG, "user id - "+ user_chatroom_id);

        SingleChatData singleChatData = new SingleChatData();

        singleChatData.setCHAT_WITH_ID(user_chatroom_id);
        singleChatData.setCHAT_DATA(chat_msg);

       // databaseHelper.entrySingleMsg(singleChatData);

        databaseHelper.setMessageDateTime(user_chatroom_id, is_seen_msg);

    }

    private void entryChatrooms(String chat_msg){

        databaseHelper.setMyChatroomMessageDateTime(user_chatroom_id, is_seen_msg);
    }


    private void entryFeaturedChatroom(){

        try {

            /*JSONObject object = new JSONObject(chatroom_details);

            if (object.getString("user_id").matches("1")){

                FeaturedChatroomData featuredChatroomData = new FeaturedChatroomData();
                featuredChatroomData.setF_CHATROOM_DB_ID(object.optString("id"));
                featuredChatroomData.setF_CHATROOM_DATA(object.toString());
                featuredChatroomData.setF_CHATROOM_INCOMING_MSG_TIME(convertDate());
                featuredChatroomData.setF_CHATROOM_IS_NEW("1");

                databaseHelper.entryFeatured_Chatrooms(featuredChatroomData);


            }else {




            }*/

        }catch (Exception e){
            e.printStackTrace();
        }


    }




    public static String convertDate() {
        long milliSeconds = System.currentTimeMillis();
        String dateFormat = "dd/MM/yyyy hh:mm:ss a";
        String dateFormat1 = "yyyy-MM-dd HH:mm:ss";
        return DateFormat.format(dateFormat1, milliSeconds).toString();
    }

}
