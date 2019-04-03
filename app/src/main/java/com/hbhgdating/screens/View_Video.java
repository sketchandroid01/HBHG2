package com.hbhgdating.screens;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.MediaController;
import android.widget.Switch;

import com.danikula.videocache.HttpProxyCacheServer;
import com.hbhgdating.R;
import com.hbhgdating.utils.All_Constants_Urls;
import com.hbhgdating.utils.Global_Class;
import com.hbhgdating.utils.ScalableVideoView;

/**
 * Created by Developer on 6/19/17.
 */
public class View_Video extends AppCompatActivity {

    ScalableVideoView videoView_full;
    Switch switch_;

    MediaController mediaController;

    HttpProxyCacheServer proxy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_video);


        videoView_full = (ScalableVideoView)findViewById(R.id.videoView_f1);
        videoView_full.setDisplayMode(ScalableVideoView.DisplayMode.ORIGINAL);
        mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView_full);





        Bundle extras = getIntent().getExtras();
        String video_path = extras.getString("video_view");

        Play_Videof1(video_path);




    }
    public void Play_Videof1(final String videoUri){

        Log.d("TAG", "playVideof1: "+videoUri);

        videoView_full.setMediaController(mediaController);

       // proxy = Global_Class.getProxy(this);
       // final String proxyUrl = proxy.getProxyUrl(videoUri);

        videoView_full.setVideoURI(Uri.parse(videoUri));
        videoView_full.requestFocus();
        videoView_full.start();

        videoView_full.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {

            }
        });

        videoView_full.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {


                videoView_full.setVideoURI(Uri.parse(videoUri));

                videoView_full.requestFocus();
                //videoView_full.start();

            }
        });


        videoView_full.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d(All_Constants_Urls.TAG, "setOnErrorListener ");
                return true;
            }
        });

    }
    @Override
    protected void onPause() {
        videoView_full.stopPlayback();
        super.onPause();
    }
    @Override
    protected void onResume() {
        videoView_full.start();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        videoView_full.stopPlayback();

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {

        Log.d("TAG", "onBackPressed in View_Video");
        videoView_full.stopPlayback();

        finish();

        super.onBackPressed();
    }
}
