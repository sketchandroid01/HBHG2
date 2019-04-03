package com.hbhgdating.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPref {
	Context mCtx;
	private static String strPref = "";
	private static String strPrefInsta = "InstaPref";

	public String strUserID = "UserID";
	public String struserislogined = "userislogined";
	public static String KEY_ALPHA_ORDER = "alphaorder";


	public String Pref_key = "Login_info";

	public String Pref_key_data = "project_data";


	SharedPreferences pref1;
	SharedPreferences.Editor editor1;

	SharedPreferences prefInsta;
	SharedPreferences.Editor editorInsta;


	SharedPreferences pref_login;
	SharedPreferences.Editor editor_login;


	SharedPreferences pref_data;
	SharedPreferences.Editor editor_data;


	Global_Class global_class;

	int PRIVATE_MODE = 0;
	private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
	private static final String IS_LOGIN = "IslOGIN";
	private static final String _ID = "_ID";
	private static final String NAME = "NAME";
	private static final String EMAIL = "EMAIL";
	private static final String GENDER = "GENDER";
	private static final String BIRTHDAY = "BIRTHDAY";
	private static final String LOCATION = "LOCATION";
	private static final String AGE = "AGE";
	private static final String Login_Via = "Login_Via";
	private static final String USERANME = "USERANME";
	private static final String ORIENTATION = "ORIENTATION";
	private static final String LOCATION_GPS = "LOCATION_GPS";

	private static final String SAVE_VIDEO = "SAVE_VIDEO";

	private static final String User_Data = "User_Data";


	///  New part .....

	public SharedPref(Context mContext) {
		this.mCtx = mContext;
		strPref = mCtx.getApplicationContext().getPackageName();

		pref1 = mCtx.getSharedPreferences(strPref, PRIVATE_MODE);
		editor1 = pref1.edit();

		prefInsta = mCtx.getSharedPreferences(strPrefInsta, PRIVATE_MODE);
		editorInsta = prefInsta.edit();


		pref_login = mCtx.getSharedPreferences(Pref_key, PRIVATE_MODE);
		editor_login = pref_login.edit();

		pref_data = mCtx.getSharedPreferences(Pref_key_data, PRIVATE_MODE);
		editor_data = pref_data.edit();




		global_class = (Global_Class)mCtx.getApplicationContext();

	}


	// Log out ...

	public void clearData(){

		editor1.clear();
		editor1.commit();

		editorInsta.clear();
		editorInsta.commit();

		editor_login.clear();
		editor_login.commit();

		editor_data.clear();
		editor_data.commit();

	}

	///  New part .....


	public void setFirstTimeLaunch(boolean isFirstTime) {
		editor1.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
		editor1.commit();
	}

	public boolean isFirstTimeLaunch() {
		return pref1.getBoolean(IS_FIRST_TIME_LAUNCH, true);
	}



	public void set_FB_LoginInfo(String login_via, boolean isLogin,String fb_id, String name, String email, String age,
								 String gender, String dob, String location){

		editor1.putString(Login_Via, login_via);
		//editor1.putBoolean(IS_LOGIN, isLogin);
		editor1.putString(_ID, fb_id);
		editor1.putString(NAME, name);
		editor1.putString(EMAIL, email);
		editor1.putString(GENDER, gender);
		editor1.putString(BIRTHDAY, dob);
		editor1.putString(LOCATION, location);
		editor1.putString(AGE, age);

		editor1.commit();

	}


	public void set_LOGIN_FB(boolean isLogin) {
		editor1.putBoolean(IS_LOGIN, isLogin);
		editor1.commit();
	}

	public boolean isLOGIN_FB() {
		return pref1.getBoolean(IS_LOGIN, false);
	}




	public void Set_FB_InfoToGlobal(){

	//	global_class.setLogin_via(pref1.getString(Login_Via, ""));

		global_class.setFB_profile_id(pref1.getString(_ID, ""));
		global_class.setFB_profile_name(pref1.getString(NAME, ""));
		global_class.setFB_profile_email(pref1.getString(EMAIL, ""));

		global_class.setFB_profile_gender(pref1.getString(GENDER, ""));
		global_class.setFB_profile_birthday(pref1.getString(BIRTHDAY, ""));
		global_class.setFB_profile_location(pref1.getString(LOCATION, ""));
		global_class.setFB_profile_age(Integer.parseInt(pref1.getString(AGE, "0")));


	}

	public  void Set_Orentation_Info(String orentation){
		editor1.putString(ORIENTATION, orentation);
		editor1.commit();
	}

	public  void Set_Orentation_to_Global(){
	global_class.setOrientation(pref1.getString(ORIENTATION,""));
	}

	public  void Set_LOCATION_GPS_Info(String location_gps){
		editor1.putString(LOCATION_GPS, location_gps);
		editor1.commit();
	}

	public  void Set_LOCATION_GPS_to_Global(){
	global_class.setLocation_gps(pref1.getString(LOCATION_GPS,""));
	}


	public void Remove_FB_Data_Logout(){

		editor1.clear();
		editor1.commit();

	}



	public void getLogin_ViaTo_Global(){

		global_class.setLogin_via(pref1.getString(Login_Via, ""));

	}



	////// Insta ...


	public void set_INSTA_LoginInfo(String login_via, boolean isLogin, String insta_id, String name,
									String useranem, String location){

		editor1.putString(Login_Via, login_via);
		editor1.commit();

		//editorInsta.putBoolean(IS_LOGIN, isLogin);
		editorInsta.putString(_ID, insta_id);
		editorInsta.putString(NAME, name);
		editorInsta.putString(USERANME, useranem);
		editorInsta.putString(LOCATION, location);

		editorInsta.commit();

	}

	public void set_LOGIN_INSTA(boolean isLogin) {
		editorInsta.putBoolean(IS_LOGIN, isLogin);
		editorInsta.commit();
	}

	public boolean isLOGIN_INSTA() {
		return prefInsta.getBoolean(IS_LOGIN, false);
	}


	public void Set_INSTA_InfoToGlobal(){

		global_class.setInsta_name(prefInsta.getString(NAME, ""));


		String full_name = global_class.getInsta_name();
		String lastName = "";
		String firstName= "";
		if(full_name.split(" ").length>1){

			lastName = full_name.substring(full_name.lastIndexOf(" ")+1);
			firstName = full_name.substring(0, full_name.lastIndexOf(' '));
			global_class.Insta_first_name = firstName;

			Log.d("TAG","lastName>>>>   "+lastName);
			Log.d("TAG","firstName>>>>   "+firstName);
		}
		else{
			firstName = full_name;
			Log.d("TAG","firstName else>>>>   "+firstName);

			global_class.Insta_first_name = firstName;
		}



		//global_class.setLogin_via(prefInsta.getString(Login_Via, ""));

		global_class.setInsta_Profile_id(prefInsta.getString(_ID, ""));

	}


	public void Remove_INSTA_Data_Logout(){

		editorInsta.clear();
		editorInsta.commit();

	}




	public void set_SAVE_VIDEO(boolean is_save){

		editor1.putBoolean(SAVE_VIDEO, is_save);
		editor1.commit();

	}

	public boolean isSAVE_VIDEO() {
		return pref1.getBoolean(SAVE_VIDEO, false);
	}


	public void ResetLoginStatus_for_Both(){

		editor1.putBoolean(IS_LOGIN, false);
		editor1.putString(Login_Via, "");
		editor1.commit();

		editorInsta.putBoolean(IS_LOGIN, false);
		editorInsta.putString(Login_Via, "");
		editorInsta.commit();
	}




	///////////////////////////////// Save login info ..............




	public void save_Login_Info(String user_data){

		editor_login.putString(User_Data, user_data);
		editor_login.commit();


	}

	public String get_Login_Info(){

		return pref_login.getString(User_Data, null);

	}


	//////////////

	public static final String User_Id = "User_Id";

	public void save_Use_Id(String user_id){

		editor_login.putString(User_Id, user_id);
		editor_login.commit();

	}

	public String get_Use_Id(){

		return pref_login.getString(User_Id, null);

	}

	////////////////////////////////////////////////////////////////////////
	////////////////////   Data .............

	// Remove data from ...

	public void remove_Data(){

		editor_data.clear();
		editor_data.commit();

	}

	//////////////////////

	public static final String User_Videos = "User_Videos";

	public void Save_Profile_Videos(String videos){

		editor_data.putString(User_Videos, videos);
		editor_data.commit();

	}

	public String get_Profile_Videos(){

		return pref_data.getString(User_Videos, null);

	}

	////////////

	public static final String User_Full_Info = "User_Full_Info";

	public void Save_Profile_Full_Info(String videos){

		editor_data.putString(User_Full_Info, videos);
		editor_data.commit();

	}

	public String get_Profile_Full_Info(){

		return pref_data.getString(User_Full_Info, null);

	}

	////////

	public static final String My_matches = "My_matches";

	public void save_My_Matches(String data){

		editor_data.putString(My_matches, data);
		editor_data.commit();

	}

	public String get_My_Matches(){

		return pref_data.getString(My_matches, null);

	}


	///////////

	public static final String notification = "notification";

	public void save_Notification(String data){

		editor_data.putString(notification, data);
		editor_data.commit();

	}

	public String get_Notification(){

		return pref_data.getString(notification, null);

	}


	////////////

	public void uploadLATLNGValue(boolean boo){

		editor_login.putBoolean("latlng", boo);
		editor_login.commit();
	}

	public boolean isLatLngUpload(){
		return pref_login.getBoolean("latlng", false);
	}











	//////////////////////////////////  Old part   ///////////////////////////////////////////////////////

	public void saveStringPref(String key, String value) {
		SharedPreferences myPrefs = mCtx.getSharedPreferences(strPref,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = myPrefs.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void saveIntegerPref(String key, Integer value) {
		SharedPreferences myPrefs = mCtx.getSharedPreferences(strPref,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = myPrefs.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public void saveLongPref(String key, long value) {
		SharedPreferences myPrefs = mCtx.getSharedPreferences(strPref,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = myPrefs.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public void saveBoolPref(String key, boolean value) {
		SharedPreferences myPrefs = mCtx.getSharedPreferences(strPref,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = myPrefs.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public void saveFirstTime() {
		SharedPreferences myPrefs = mCtx.getSharedPreferences(strPref,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = myPrefs.edit();
		editor.putBoolean("FirstTime", false);
		editor.commit();
	}

	public boolean getFirstTime() {
		SharedPreferences myPrefs = mCtx.getSharedPreferences(strPref,
				Context.MODE_PRIVATE);
		return myPrefs.getBoolean("FirstTime", true);
	}

	public boolean getBoolPref(String key) {
		SharedPreferences myPrefs = mCtx.getSharedPreferences(strPref,
				Context.MODE_PRIVATE);
		return myPrefs.getBoolean(key, false);
	}

	public String getStringPref(String key) {
		SharedPreferences myPrefs = mCtx.getSharedPreferences(strPref,
				Context.MODE_PRIVATE);
		return myPrefs.getString(key, "");
	}

	public Integer getIntegerPref(String key) {
		SharedPreferences myPrefs = mCtx.getSharedPreferences(strPref,
				Context.MODE_PRIVATE);
		return myPrefs.getInt(key, 0);
	}

	public long getLongPref(String key) {
		SharedPreferences myPrefs = mCtx.getSharedPreferences(strPref,
				Context.MODE_PRIVATE);
		return myPrefs.getLong(key, 0);
	}

	public Integer getIntegerPrefForRemember(String key) {
		SharedPreferences myPrefs = mCtx.getSharedPreferences(strPref,
				Context.MODE_PRIVATE);
		return myPrefs.getInt(key, 2);
	}

	public void removePref(String key) {
		SharedPreferences myPrefs = mCtx.getSharedPreferences(strPref,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = myPrefs.edit();
		editor.remove(key);
		editor.commit();
	}

}