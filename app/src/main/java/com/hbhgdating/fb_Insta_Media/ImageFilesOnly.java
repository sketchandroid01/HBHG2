package com.hbhgdating.fb_Insta_Media;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.hbhgdating.databaseLocal.DatabaseHelper;
import com.hbhgdating.R;
import com.hbhgdating.screens.ProfileActivity;
import com.hbhgdating.utils.Global_Class;
import com.hbhgdating.utils.Utility;

/**
 * Created by Developer on 9/15/17.
 */

public class ImageFilesOnly extends AppCompatActivity{



    String TAG = "TAG";

    private GridView gvAllImages;
    private Context context;



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
        setContentView(R.layout.image_files_only);

        Log.d("TAG", "Eneter AllMediaFiles");
        Log.d("TAG", "Eneter AllMediaFiles");


        dbhelper = new DatabaseHelper(this);
        utility = new Utility(this);

        gvAllImages = (GridView) findViewById(R.id.gvAllImages);
        ll_videos = (LinearLayout)findViewById(R.id.ll_videos) ;
        ll_img = (LinearLayout)findViewById(R.id.ll_img);


        context = ImageFilesOnly.this;
        global_class = (Global_Class)getApplicationContext();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Please wait...");


        Bundle b = getIntent().getExtras();

        if(b!=null)
        {
            String key =(String) b.get("key");


            if (key.equals("fb")){

                setImageGridAdapter_For_FB();

            }
            if (key.equals("insta")){

                setImageGridAdapter_For_Insta();

            }



        }







    }


    private void setImageGridAdapter_For_FB() {

        mProgressDialog.show();

        Get_FB_Url_from_Database();

        final ImageOnlyAdapter adapter = new ImageOnlyAdapter(context, global_class.Image_Url_FB);
        gvAllImages.setAdapter(adapter);

        gvAllImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(ImageFilesOnly.this, ProfileActivity.class);
                intent.putExtra("sourceurl", global_class.Image_Url_FB.get(position));
                setResult(RESULT_OK, intent);
                finish();

            }
        });



        mProgressDialog.dismiss();

    }



    private void setImageGridAdapter_For_Insta() {
        mProgressDialog.show();

        Get_INSTA_IMAGE_from_Database();

        final ImageOnlyAdapter adapter = new ImageOnlyAdapter(context, global_class.Image_Url_INSTA);
        gvAllImages.setAdapter(adapter);

        gvAllImages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ImageFilesOnly.this, ProfileActivity.class);
                intent.putExtra("sourceurl", global_class.Image_Url_INSTA.get(position));
                setResult(RESULT_OK, intent);
                finish();

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


    }








}
