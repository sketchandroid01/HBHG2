package com.hbhgdating.screens;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.hbhgdating.R;
import com.hbhgdating.utils.All_Constants_Urls;
import com.hbhgdating.utils.Common;
import com.hbhgdating.utils.Global_Class;
import com.hbhgdating.utils.SharedPref;
import com.hbhgdating.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cz.msebera.android.httpclient.Header;


/**
 * Created by Developer on 4/28/17.
 */
public class ImHappyDialog extends Dialog{

    ImageView img_btn_cal;

    SimpleDateFormat simpleDateFormat;
    Calendar myCalendar = Calendar.getInstance();
    TextView edt_dob;
    String date_to_send;
    private int mYear, mMonth, mDay;
    RadioGroup select_gender;
    String  gender = null;
    LinearLayout ll3,ll4;
    RelativeLayout rl9,m_orient_1,m_orient_2,m_orient_3,f_orient_1,f_orient_2,f_orient_3;
    ImageView  tick_1,tick_2,tick_3,tick_4,tick_5,tick_6;
    EditText et_name;
    Global_Class global_class;
    RadioButton rb1,rb2;
    String check_orientation = "";
    private Context context;
    SharedPref sharedPref;

    ArrayList<File> File_array;
    ArrayList<String> Urls_array;

    File profile_image;

    boolean is_video = false;
    boolean is_file = false;

    File videoFile;

    Dialog progressDialog;

    public ImHappyDialog(Context mcontext) {
        super(mcontext);
        this.context = mcontext;

        global_class = (Global_Class)context.getApplicationContext();
        sharedPref = new SharedPref(context);

        progressDialog = new Dialog(context, android.R.style.Theme_Translucent);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setContentView(R.layout.progressbar_creating_profile);
        progressDialog.setCancelable(false);



        File_array = new ArrayList<>();
        Urls_array = new ArrayList<>();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.im_happy_custom_dialog);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        Log.d("TAG", "p_iamge = "+global_class.Profile_Image);

        if (global_class.Final_Image_To_Upload.size() != 0){

            is_video = false;

            for (int i = 0; i < global_class.Final_Image_To_Upload.size(); i++){

                if (global_class.Final_Image_To_Upload.get(i).startsWith("http")){

                    is_file = false;

                    Urls_array.add(global_class.Final_Image_To_Upload.get(i));

                    Log.d("TAG", "Urllsls = "+Urls_array.get(i));

                }else {

                    is_file = true;

                    File imageFile = new File(getRealPathFromURI(Uri.parse(global_class.Final_Image_To_Upload.get(i))));
                    Log.d("TAG", "File = "+imageFile);
                    File_array.add(imageFile);
                }


            }

        }else {

            is_video = true;

            videoFile = new File(getRealPathFromURI(Uri.parse(global_class.getFinal_Video_To_upload())));
        }


        img_btn_cal = (ImageView)findViewById(R.id.img_btn_cal);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        edt_dob= (TextView) findViewById(R.id.edt_dob);
        rb1= (RadioButton)findViewById(R.id.rb1);
        rb2= (RadioButton)findViewById(R.id.rb2);

        et_name = (EditText)findViewById(R.id.et_name);




        rl9 = (RelativeLayout)findViewById(R.id.rl9);

        select_gender = (RadioGroup) findViewById(R.id.rg1);
        ll3 = (LinearLayout)findViewById(R.id.ll3);
        ll4 = (LinearLayout)findViewById(R.id.ll4);

        m_orient_1 = (RelativeLayout)findViewById(R.id.m_orient_1);
        m_orient_2 = (RelativeLayout)findViewById(R.id.m_orient_2);
        m_orient_3 = (RelativeLayout)findViewById(R.id.m_orient_3);
        f_orient_1 = (RelativeLayout)findViewById(R.id.f_orient_1);
        f_orient_2 = (RelativeLayout)findViewById(R.id.f_orient_2);
        f_orient_3 = (RelativeLayout)findViewById(R.id.f_orient_3);

        tick_1 = (ImageView)findViewById(R.id.tick_1);
        tick_2 = (ImageView)findViewById(R.id.tick_2);
        tick_3 = (ImageView)findViewById(R.id.tick_3);
        tick_4 = (ImageView)findViewById(R.id.tick_4);
        tick_5 = (ImageView)findViewById(R.id.tick_5);
        tick_6 = (ImageView)findViewById(R.id.tick_6);



        tick_1.setVisibility(View.GONE);
        tick_2.setVisibility(View.GONE);
        tick_3.setVisibility(View.GONE);
        tick_4.setVisibility(View.GONE);
        tick_5.setVisibility(View.GONE);
        tick_6.setVisibility(View.GONE);




        ll3.setVisibility(View.GONE);
        ll4.setVisibility(View.GONE);


        m_orient_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_orientation = "Straight";
                tick_1.setVisibility(View.VISIBLE);
                tick_2.setVisibility(View.GONE);
                tick_3.setVisibility(View.GONE);
                tick_4.setVisibility(View.GONE);
                tick_5.setVisibility(View.GONE);
                tick_6.setVisibility(View.GONE);

                sharedPref.Set_Orentation_Info(check_orientation);
                sharedPref.Set_Orentation_to_Global();

            }
        });
        m_orient_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_orientation = "Gay";
                tick_1.setVisibility(View.GONE);
                tick_2.setVisibility(View.VISIBLE);
                tick_3.setVisibility(View.GONE);
                tick_4.setVisibility(View.GONE);
                tick_5.setVisibility(View.GONE);
                tick_6.setVisibility(View.GONE);
                sharedPref.Set_Orentation_Info(check_orientation);
                sharedPref.Set_Orentation_to_Global();

            }
        });
        m_orient_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_orientation = "Bisexual";
                tick_1.setVisibility(View.GONE);
                tick_2.setVisibility(View.GONE);
                tick_3.setVisibility(View.VISIBLE);
                tick_4.setVisibility(View.GONE);
                tick_5.setVisibility(View.GONE);
                tick_6.setVisibility(View.GONE);
                sharedPref.Set_Orentation_Info(check_orientation);
                sharedPref.Set_Orentation_to_Global();

            }
        });
        f_orient_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_orientation = "Straight";
                tick_1.setVisibility(View.GONE);
                tick_2.setVisibility(View.GONE);
                tick_3.setVisibility(View.GONE);
                tick_4.setVisibility(View.VISIBLE);
                tick_5.setVisibility(View.GONE);
                tick_6.setVisibility(View.GONE);
                sharedPref.Set_Orentation_Info(check_orientation);
                sharedPref.Set_Orentation_to_Global();

            }
        });
        f_orient_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_orientation = "Lesbian";
                tick_1.setVisibility(View.GONE);
                tick_2.setVisibility(View.GONE);
                tick_3.setVisibility(View.GONE);
                tick_4.setVisibility(View.GONE);
                tick_5.setVisibility(View.VISIBLE);
                tick_6.setVisibility(View.GONE);
                sharedPref.Set_Orentation_Info(check_orientation);
                sharedPref.Set_Orentation_to_Global();


            }
        });
        f_orient_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_orientation = "Bisexual";
                tick_1.setVisibility(View.GONE);
                tick_2.setVisibility(View.GONE);
                tick_3.setVisibility(View.GONE);
                tick_4.setVisibility(View.GONE);
                tick_5.setVisibility(View.GONE);
                tick_6.setVisibility(View.VISIBLE);
                sharedPref.Set_Orentation_Info(check_orientation);
                sharedPref.Set_Orentation_to_Global();

            }
        });




        if(global_class.login_via.equals("fb")) {

            et_name.setText(global_class.FB_profile_first_name);
            edt_dob.setText(global_class.FB_profile_birthday);
            img_btn_cal.setVisibility(View.GONE);
            et_name.setEnabled(false);
            edt_dob.setEnabled(false);



            try {

                //SimpleDateFormat from_df = new SimpleDateFormat("MMM d, yyyy");
                //Date birthdate = from_df.parse(global_class.FB_profile_birthday);
                // Date format change ...
                String input_date_Pattern = "MMM d, yyyy";
                String output_date_Pattern = "yyy-MM-dd";
                SimpleDateFormat input_date_Format = new SimpleDateFormat(input_date_Pattern);
                SimpleDateFormat output_date_Format = new SimpleDateFormat(output_date_Pattern);
                Date date = null;
                String str_date = null;

                date = input_date_Format.parse(global_class.FB_profile_birthday);
                date_to_send = output_date_Format.format(date);

                Log.d("TAG","date_to_send = "+date_to_send);

            } catch (ParseException e) {
                e.printStackTrace();
            }





        }else if(global_class.login_via.equals("insta")){

            et_name.setText(global_class.Insta_first_name);
            img_btn_cal.setVisibility(View.VISIBLE);

            edt_dob.setEnabled(true);
        }
        et_name.setSelection(et_name.getText().length()); /////// set cursor to end





        select_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                Log.d("TAG",""+i);
                if (i == R.id.rb1) {
                    gender = "M";
                    ll4.setVisibility(View.GONE);
                    ll3.setVisibility(View.VISIBLE);


                } else  if(i == R.id.rb2){
                    gender = "F";
                    ll3.setVisibility(View.GONE);
                    ll4.setVisibility(View.VISIBLE);


                }

            }
        });

        if(global_class.FB_profile_gender.equals("male")){

            rb1.setChecked(true);
            rb2.setChecked(false);
            rb2.setVisibility(View.GONE);
        }else  if (global_class.FB_profile_gender.equals("female")){
            rb2.setChecked(true);
            rb1.setChecked(false);
            rb1.setVisibility(View.GONE);
        }


        rl9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (edt_dob.getText().length()!=0) {
                        if (gender!=null) {
                            if (check_orientation.equals("")){
                                Toast.makeText(getContext(),"Please select your orientation.",Toast.LENGTH_SHORT).show();
                            }else {

                                Log.d("TAG","login");
                                Log.d("TAG","login");
                                Log.d("TAG","login");
                                Log.d("TAG","login");


                                progressDialog.show();


                                if (is_file){

                                    new AsyncTask_ProfilePictureDownload().execute();

                                }else
                                if (is_video){

                                    new AsyncTask_ProfilePictureDownload().execute();

                                }else {

                                    new AsyncTask_Download().execute();

                                }


                            }
                        }else { Toast.makeText(getContext(),"Please select gender.",Toast.LENGTH_SHORT).show();}

                    }else {Toast.makeText(getContext(),"Please enter DOB.",Toast.LENGTH_SHORT).show();}


            }
        });


        img_btn_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                Calendar minAdultAge = Calendar.getInstance();
                minAdultAge.add(Calendar.YEAR, -18);


                DatePickerDialog dpd = new DatePickerDialog(getContext(), datepicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                String today_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                Date d = null;

                try {
                    d = simpleDateFormat.parse(today_date);
                } catch (java.text.ParseException e) {
                    e.printStackTrace();
                }


                //dpd.getDatePicker().setMaxDate(d.getTime());
                dpd.getDatePicker().setMaxDate(minAdultAge.getTimeInMillis());


                dpd.show();
            }

            DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener(){
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    String myFormat = "MMM dd, yyyy";
                    String sendFormat = "yyyy-MM-dd";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    SimpleDateFormat sdf1 = new SimpleDateFormat(sendFormat, Locale.US);
                    date_to_send = sdf1.format(myCalendar.getTime());
                    String date_to_show = sdf.format(myCalendar.getTime());
                    Log.d("TAG", date_to_send + "\n" + date_to_show);
                    edt_dob.setText(date_to_show);



                    Log.d("TAG", "year = "+year);
                    Log.d("TAG", "monthOfYear = "+monthOfYear);
                    Log.d("TAG", "dayOfMonth = "+dayOfMonth);

                    try {

                        //Date date = simpleDateFormat.parse(date_to_show);
                        //Log.d("TAG", "Age insta = "+getAge(date));

                        SimpleDateFormat from_df = new SimpleDateFormat("MMM dd, yyyy");
                        Date birthdate = from_df.parse(date_to_show);

                        Log.d("TAG", "Age insta = "+getAge(birthdate));


                    } catch (java.text.ParseException e) {
                        e.printStackTrace();
                    }

                }
            };

        });




    }


    private class AsyncTask_Download extends AsyncTask<Void, Void, Void> {

        Utility utility;



        @Override
        protected void onPreExecute() {

            utility = new Utility(context);

            File_array = new ArrayList<>();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            for (int i = 0; i < Urls_array.size(); i++){

                String file = utility.SaveImageFromUrl(Urls_array.get(i), "P_IMG" , i);

                Log.d(All_Constants_Urls.TAG, "P_IMG = "+file);

                File_array.add(new File(getRealPathFromURI(Uri.parse(file))));

            }

            if (!global_class.Profile_Image.isEmpty()){

                String file = utility.SaveImageFromUrl(global_class.Profile_Image, "PROFILE_IMG" , 111);

                Log.d(All_Constants_Urls.TAG, "PROFILE_IMG = "+file);

                profile_image = new File(getRealPathFromURI(Uri.parse(file)));
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {


            login();

            super.onPostExecute(result);
        }

    }


    private class AsyncTask_ProfilePictureDownload extends AsyncTask<Void, Void, Void> {

        Utility utility;


        @Override
        protected void onPreExecute() {

            utility = new Utility(context);

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            if (!global_class.Profile_Image.isEmpty()){

                String file = utility.SaveImageFromUrl(global_class.Profile_Image, "PROFILE_IMG" , 111);

                Log.d(All_Constants_Urls.TAG, "PROFILE_IMG = "+file);

                profile_image = new File(getRealPathFromURI(Uri.parse(file)));
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            login();

            super.onPostExecute(result);
        }

    }



    public static int getAge(Date dateOfBirth) {

        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();

        int age = 0;

        birthDate.setTime(dateOfBirth);
        if (birthDate.after(today)) {
            throw new IllegalArgumentException("Can't be born in the future");
        }

        age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        // If birth date is greater than todays date (after 2 days adjustment of leap year) then decrement age one year
        if ( (birthDate.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR) > 3) ||
                (birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH ))){
            age--;

            // If birth date and todays date are of same month and birth day of month is greater than todays day of month then decrement age
        }else if ((birthDate.get(Calendar.MONTH) == today.get(Calendar.MONTH )) &&
                (birthDate.get(Calendar.DAY_OF_MONTH) > today.get(Calendar.DAY_OF_MONTH ))){
            age--;
        }

        return age;
    }


    public void login(){

        final String TAG = All_Constants_Urls.TAG;

        sharedPref.remove_Data();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(All_Constants_Urls.TAG, "Refreshed token: " + refreshedToken);


        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        if (global_class.getLogin_via().equals("fb")){

            params.put(All_Constants_Urls.facebook_id, global_class.getFB_profile_id());

            String full_name = global_class.getFB_profile_name();
            String[] arr = full_name.split(" ");

            params.put(All_Constants_Urls.first_name, arr[0].trim());
            params.put(All_Constants_Urls.last_name, arr[1].trim());

        }else {

            params.put(All_Constants_Urls.instagram_id, global_class.getInsta_Profile_id());

            String full_name = global_class.getInsta_name();
            String[] arr = full_name.split(" ");

            if (arr.length == 1){
                params.put(All_Constants_Urls.first_name, arr[0].trim());
                params.put(All_Constants_Urls.last_name, "");
            }else if (arr.length == 2){
                params.put(All_Constants_Urls.first_name, arr[0].trim());
                params.put(All_Constants_Urls.last_name, arr[1].trim());
            }


        }


        params.put(All_Constants_Urls.device_type, All_Constants_Urls.DEVICE_TYPE);
        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.device_token, FirebaseInstanceId.getInstance().getToken());

        params.put(All_Constants_Urls.gender, gender);
        params.put(All_Constants_Urls.sexual_orientation, check_orientation);
        params.put(All_Constants_Urls.city, global_class.CITY);
        params.put(All_Constants_Urls.country, global_class.COUNTRY);
        params.put(All_Constants_Urls.dob, date_to_send);
        params.put(All_Constants_Urls.allow_push, "yes");


        try{

            params.put(All_Constants_Urls.profile_image, profile_image);


            for (int i = 0; i < File_array.size(); i++){

                params.put(All_Constants_Urls.image_video+"["+i+"]", File_array.get(i));

                Log.d(TAG ,"File - "+ i +" - " +File_array.get(i));
            }

            if (is_video){

                params.put(All_Constants_Urls.video, videoFile);
            }



        }catch (FileNotFoundException e){
            e.printStackTrace();
        }




        Log.d(TAG ,"AsyncHttpClient URL- " + All_Constants_Urls.LOGIN);
        Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


        client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , 30*1000);
        client.post(All_Constants_Urls.LOGIN, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                Log.d(TAG, "onSuccess- " + response.toString());

                if (response != null) {
                    try {


                        int success = response.optInt("success");
                        String message = response.optString("message");

                        if (success == 0){

                           // Toast.makeText(context, ""+message, Toast.LENGTH_LONG).show();


                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("HBHG");
                            builder.setMessage(message);
                            builder.setPositiveButton("OK", new OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    dismiss();
                                    //System.exit(0);
                                }
                            });
                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();


                            progressDialog.dismiss();


                        }else
                        if (success == 1){

                            Toast.makeText(context, "login Successful", Toast.LENGTH_LONG).show();


                            JSONObject obj_user_data = response.getJSONObject("user_data");

                            sharedPref.save_Login_Info(obj_user_data.toString());

                            sharedPref.save_Use_Id(obj_user_data.getString(All_Constants_Urls.id));


                            sharedPref.set_SAVE_VIDEO(true);



                            int is_new_user = response.optInt("is_new_user");
                            if (is_new_user == 1){

                                dismiss();

                                Intent myIntent = new Intent(getContext(),ProfileActivity.class);
                                context.startActivity(myIntent);
                                Check_Video.check_video.finish();


                            }else {

                                dismiss();

                                Intent myIntent = new Intent(getContext(),MainActivity.class);
                                context.startActivity(myIntent);
                                Check_Video.check_video.finish();


                            }

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

                AlertDialog alert = new AlertDialog.Builder(context).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });

    }


    private String getRealPathFromURI(Uri contentURI) {
        String result = "";
        try {
            Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
            if (cursor == null) { // Source is Dropbox or other similar local file path
                result = contentURI.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx); // Exception raised HERE
                cursor.close(); }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    private void Option_Dialog(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.selection_dialog, null);
        dialogBuilder.setView(dialogView);

        TextView tv_option_1 = (TextView) dialogView.findViewById(R.id.tv_option_1);
        TextView tv_option_2 = (TextView) dialogView.findViewById(R.id.tv_option_2);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();


        tv_option_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(getContext(),ProfileActivity.class);
                getContext().startActivity(myIntent);
                Check_Video.check_video.finish();

                dismiss();
                alertDialog.dismiss();

            }
        });

        tv_option_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(getContext(),MainActivity.class);
                getContext().startActivity(myIntent);
                Check_Video.check_video.finish();

                dismiss();
                alertDialog.dismiss();

            }
        });






    }







}
