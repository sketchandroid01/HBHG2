package com.hbhgdating.Trimmer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.hbhgdating.R;
import com.hbhgdating.utils.Utility;

import life.knowledge4.videotrimmer.K4LVideoTrimmer;
import life.knowledge4.videotrimmer.interfaces.OnK4LVideoListener;
import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener;


/**
 * Created by Developer on 6/13/17.
 */

public class TrimmerActivity extends AppCompatActivity implements OnTrimVideoListener, OnK4LVideoListener {

    K4LVideoTrimmer videoTrimmer;
    private ProgressDialog mProgressDialog;

    Utility utility;

    String destination_path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trimmer_activity);

        videoTrimmer = ((K4LVideoTrimmer) findViewById(R.id.timeLine));

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Trimming...");

        long startTime = System.currentTimeMillis();

        utility = new Utility(this);
        destination_path = utility.get_HBHG_Directory();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            String path = bundle.getString("path");

            if (videoTrimmer != null) {

                videoTrimmer.setMaxDuration(7);
                videoTrimmer.setOnTrimVideoListener(this);
                videoTrimmer.setOnK4LVideoListener(this);
                videoTrimmer.setDestinationPath(destination_path);
                videoTrimmer.setVideoURI(Uri.parse(path));
                videoTrimmer.setVideoInformationVisibility(true);

            }

        }






    }

    @Override
    public void onTrimStarted() {
        mProgressDialog.show();
    }

    @Override
    public void getResult(final Uri uri) {
        mProgressDialog.cancel();

        Log.d("TAG", "Video save at - "+uri.getPath());

        Intent intent = new Intent();
        intent.putExtra("uri", uri.getPath());
        setResult(RESULT_OK, intent);
        finish();


    }

    @Override
    public void cancelAction() {
        mProgressDialog.cancel();
        videoTrimmer.destroy();
        finish();
    }

    @Override
    public void onError(final String message) {
        mProgressDialog.cancel();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(TrimmerActivity.this, message, Toast.LENGTH_SHORT).show();

                Log.e("TAG", "onError = " +message);
            }
        });
    }

    @Override
    public void onVideoPrepared() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
               // Toast.makeText(TrimmerActivity.this, "onVideoPrepared", Toast.LENGTH_SHORT).show();
            }
        });
    }

}