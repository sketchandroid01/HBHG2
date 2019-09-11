package com.hbhgdating.fb_Insta_Media;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbhgdating.databaseLocal.DatabaseHelper;
import com.hbhgdating.R;
import com.hbhgdating.screens.ProfileActivity;
import com.hbhgdating.utils.Global_Class;
import com.hbhgdating.utils.Utility;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by Developer on 7/27/17.
 */

public class AllMediaFiles_2 extends AppCompatActivity {


    String TAG = "TAG";

    private GridView gvAllImages,gvAllVideo;
    private Context context;



    TextView tv_done;


    Global_Class global_class;
    LinearLayout ll_videos,ll_img;

    private DatabaseHelper dbhelper;
    ProgressDialog mProgressDialog;

    Utility utility;

    String video_url;
    String file_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_media_list_files);

        Log.d("TAG", "Eneter AllMediaFiles_2");
        Log.d("TAG", "Eneter AllMediaFiles_2");
        Log.d("TAG", "Eneter AllMediaFiles_2");
        Log.d("TAG", "Eneter AllMediaFiles_2");




        dbhelper = new DatabaseHelper(this);
        utility = new Utility(this);

        gvAllImages = (GridView) findViewById(R.id.gvAllImages);
        gvAllVideo = (GridView) findViewById(R.id.gvAllVideo);
        ll_videos = (LinearLayout)findViewById(R.id.ll_videos) ;
        ll_img = (LinearLayout)findViewById(R.id.ll_img);

        tv_done = (TextView) findViewById(R.id.tv_done);

        context = AllMediaFiles_2.this;
        global_class = (Global_Class)getApplicationContext();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Please wait...");


        Bundle b = getIntent().getExtras();

        if(b!=null)
        {
            String key =(String) b.get("key");

            /*if (key.equals("pics")){
                ll_videos.setVisibility(View.GONE);
            }else  if(key.equals("video")){
                ll_img.setVisibility(View.GONE);

            }else{
                ll_videos.setVisibility(View.VISIBLE);
            }*/


            if (key.equals("fb")){

                ll_videos.setVisibility(View.GONE);

                setImageGridAdapter_For_FB();

            }
            if (key.equals("insta")){

                setImageGridAdapter_For_Insta();
                setVideoGridAdapter_For_Insta();

            }



        }


    }



    @Override
    protected void onPause() {
        dbhelper.close();
        super.onPause();
    }


    private void setImageGridAdapter_For_FB() {

        mProgressDialog.show();

        Get_FB_Url_from_Database();

        final ImageGridListAdapter adapter = new ImageGridListAdapter(context, global_class.Image_Url_FB, tv_done);
        gvAllImages.setAdapter(adapter);

        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                global_class.Selected_Image_Url = adapter.getCheckedItems();

               // Log.d("TAG", "Selected_Image_Url = "+global_class.Selected_Image_Url);

                Intent intent = new Intent(AllMediaFiles_2.this, ProfileActivity.class);
                intent.putExtra("key", "image");
                setResult(RESULT_OK, intent);
                finish();

            }
        });

        mProgressDialog.dismiss();

    }



    private void setImageGridAdapter_For_Insta() {
        mProgressDialog.show();

        Get_INSTA_IMAGE_from_Database();

        final ImageGridListAdapter adapter = new ImageGridListAdapter(context, global_class.Image_Url_INSTA, tv_done);
        gvAllImages.setAdapter(adapter);



        tv_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Log.d("TAG", "Array = "+adapter.getCheckedItems());

                global_class.Selected_Image_Url = adapter.getCheckedItems();


                Intent intent = new Intent(AllMediaFiles_2.this, ProfileActivity.class);
                intent.putExtra("key", "image");
                setResult(RESULT_OK, intent);
                finish();



            }
        });

        mProgressDialog.dismiss();
    }

    private void  setVideoGridAdapter_For_Insta(){

        mProgressDialog.show();

        Get_INSTA_VIDEO_from_Database();

        VideoGridListAdapter adapter = new VideoGridListAdapter(context, global_class.Video_Url_INSTA);
        gvAllVideo.setAdapter(adapter);


        gvAllVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                video_url = global_class.Video_Url_INSTA.get(position).get("video_url");
                file_name = "video_insta";

                new AllMediaFiles_2.Download_asyncTask().execute();

            }
        });


        mProgressDialog.dismiss();
    }

    ////////////////
    ////////////////
    ////////////////  Get Data from database ....

    public void Get_FB_Url_from_Database(){

        global_class.Image_Url_FB.clear();


        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_FB;
        SQLiteDatabase db  = dbhelper.getReadableDatabase();
        Cursor cursor1     = db.rawQuery(selectQuery, null);

        if (cursor1.moveToFirst()) {
            do {
                String url = cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.IMG_URL_FB));

                global_class.Image_Url_FB.add(url);


            } while (cursor1.moveToNext());
        }



        db.close();
    }



    public void Get_INSTA_IMAGE_from_Database(){

        global_class.Image_Url_INSTA.clear();


        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_INSTA;
        SQLiteDatabase db  = dbhelper.getReadableDatabase();
        Cursor cursor1     = db.rawQuery(selectQuery, null);

        if (cursor1.moveToFirst()) {
            do {
                String url = cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.IMG_URL_INSTA));

                global_class.Image_Url_INSTA.add(url);


            } while (cursor1.moveToNext());
        }

        db.close();

    }


    public void Get_INSTA_VIDEO_from_Database(){

        global_class.Video_Url_INSTA.clear();


        String selectQuery = "SELECT  * FROM " + DatabaseHelper.TABLE_INSTA_VIDEO;
        SQLiteDatabase db  = dbhelper.getReadableDatabase();
        Cursor cursor1     = db.rawQuery(selectQuery, null);

        if (cursor1.moveToFirst()) {
            do {
                String ima_url = cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.THUMB_IMAGE_INSTA));
                String v_url = cursor1.getString(cursor1.getColumnIndex(DatabaseHelper.VIDEO_URL_INSTA));


                HashMap<String, String> map_insta = new HashMap<String, String>();
                map_insta.put("thumb_image", ima_url);
                map_insta.put("video_url", v_url);

                global_class.Video_Url_INSTA.add(map_insta);

               // Log.d("TAG", "ima_url = "+ima_url);

            } while (cursor1.moveToNext());
        }


        db.close();

    }





    private class Download_asyncTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog mProgressDialog;
        String download_video_file;

        @Override
        protected void onPreExecute() {

            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage("Downloading video...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(Void... arg0) {

            download_video_file = SaveVideoFromUrl(video_url, file_name);


            return null;
        }
        @Override
        protected void onPostExecute(Void result) {


            Intent intent = new Intent(AllMediaFiles_2.this, ProfileActivity.class);
            intent.putExtra("key", "video");
            intent.putExtra("file", download_video_file);
            setResult(RESULT_OK, intent);
            finish();



            mProgressDialog.dismiss();

            super.onPostExecute(result);
        }
    }

    public String SaveVideoFromUrl(String link, String filename){

        // Log.e("TAG", "link - "+link);

        File file = null;

        try{

            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            //   File dbDirectory = new File(utility.Make_Directory());

            file = new File(utility.Make_Directory(), filename+".mp4");

            InputStream inputStream = new BufferedInputStream(url.openStream());

            OutputStream fileOutput = new FileOutputStream(file);

          //  Log.e("TAG", "Path - "+file.toString());


            byte[] buffer = new byte[1024];
            int bufferLength;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);

            }


            fileOutput.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }


        return file.toString();

    }




}
