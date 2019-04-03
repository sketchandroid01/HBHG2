package com.hbhgdating.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class Common {



	public static final String Key_Mymatches = "getMymatchesAndChatrooms";
	public static final String Key_Chatrooms = "getChatrooms";
	public static final String Key_Notification = "getNotification";
	public static final String Key_SingleChat = "getSingleChat";
	public static final String Key_GroupChat = "getGroupChat";
	public static final String Key_Service = "Key_Service";

	public static final String Key_SingleChatNoti = "getSingleChatNoti";
	public static final String Key_ChatroomNoti = "getChatroomNoti";

	public static final String likes_your_profile = "likes your profile";


	public static final int DEFAULT_TIMEOUT = 8 * 1000;
	public static final int DEFAULT_TIMEOUT_30 = 30 * 1000;
	public static final int MAXIMUM_RETRY = 3;

	// PUT YOUR MERCHANT KEY HERE;
	public static final long VIDEO_TIMEFRAME_IMAGE=7000;
    public static final int REQUEST_WRITE_STORAGE = 112;
	public static final String GCM_SENDERID = "46370260957";// live
	public static final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq8HZROKNE3/Kv3NEqPC3TrvVMlmiEe/lKAe+XIjVnRc1AOfZqtkhgbTjwn7iDE+FBSqCoy2FJBDoMQdIORjDQ1LpIZJJ4fjduVH3CFW5LBuc7P64vLzi+5Q6nxQ6d8vha4x38bwCx7LASdr0V0LLX3vsniH8lX5hajHtsF+8bFlnabg+Pq4FT5G1nJff7NLNzJHWxbjMVomJ7Y9JSHivvjPivcQlYbrBLziKabJrM/hahUlrcL8NEeLbP1PQuprMZt5D0RTJu1Hqh9rb33Be8UGJaAiOzkenWGweLgNWfIbe1ekrng3Ydk8p717FvfMgm4whkMlz9FyCXMeskMrRgQIDAQAB";
	public static final String googleSearchAPIKey = "AIzaSyC5Mf_LqMqcMwiLAlgx3EG7VJ_FjKU_9GM";
	public static final String googleSearchCx = "014852980787483500975:in98jqthluy";
	public static final int TimerMilisecond = 5000; // 5 sec
	public static final long VideoLength = 7000;
	public static final long gifYes = 1700;
	public static final long gifNo = 2500;
	public static final long loader_time = 2000;
	public static final List<String> permissionNeeds = Arrays.asList("email",
			"public_profile", "user_birthday", "user_videos", "user_photos",
			"user_location");

	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);
	}

	public enum displayType {
		CENTER, TOP, BOTTOM
	}

	public static void customToast(Activity activity, String strMessage,
								   displayType display) {
		Toast toast = Toast.makeText(activity, strMessage, Toast.LENGTH_LONG);
		if (displayType.TOP == display) {
			toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 100);
		} else if (displayType.BOTTOM == display) {
			toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 100);
		} else {
			toast.setGravity(Gravity.CENTER, 0, 0);
		}
		toast.show();
	}

    public static void showMessageOKCancel(Context ctx, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ctx)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public static void showMessageOK(Context ctx, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ctx)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }

	public static final String strMainFolder = Environment.getExternalStorageDirectory()
			+ File.separator + "HBHG";
	public static final String strImageCompressFolder = strMainFolder + File.separator + "hbhgimg"
			+ File.separator + "hbhgcompress";
	public static final String strVideoFolder = strMainFolder + File.separator + "hbhgvid";
	public static final String strVideoTrimFolder = strMainFolder + File.separator + "hbhgtrimvid";
	public static final String strTmpImageFolder = Environment.getExternalStorageDirectory()
			+ File.separator + "Pictures";
	public static final String strTmpVideoFolder = Environment.getExternalStorageDirectory()
			+ File.separator + "Video";
}
