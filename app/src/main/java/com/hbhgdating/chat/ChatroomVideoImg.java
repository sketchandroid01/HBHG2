package com.hbhgdating.chat;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.danikula.videocache.HttpProxyCacheServer;
import com.hbhgdating.R;
import com.hbhgdating.utils.All_Constants_Urls;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by Developer on 11/6/17.
 */

public class ChatroomVideoImg extends AppCompatActivity{

    //ScalableVideoView videoView;
    VideoView videoView;
    ImageView img_chatroom, back_full;

    MediaController mediaController;

    HttpProxyCacheServer proxy;

    public ImageLoader loader;
    DisplayImageOptions defaultOptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom_video_img);
        initViews();



    }

    public void initViews(){

        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.img1)
                .showImageOnLoading(R.drawable.img1)
                .showImageOnFail(R.drawable.img1)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
        loader = ImageLoader.getInstance();

        videoView = (VideoView) findViewById(R.id.videoView);
        img_chatroom = (ImageView) findViewById(R.id.img_chatroom);
        back_full = (ImageView) findViewById(R.id.back_full);
        mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            if (bundle.getInt("content_type") == 2){

                loader.displayImage(bundle.getString("content_img"), img_chatroom);

                videoView.setVisibility(View.GONE);
                img_chatroom.setVisibility(View.VISIBLE);

            }else if (bundle.getInt("content_type") == 4){

                playVideof1(bundle.getString("content_video"));

                videoView.setVisibility(View.VISIBLE);
                img_chatroom.setVisibility(View.GONE);

            }


        }



        back_full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    public void playVideof1(final String videoUri){

        Log.d("TAG", "playVideof1: "+videoUri);

        videoView.setMediaController(mediaController);

        //proxy = Global_Class.getProxy(this);
        //final String proxyUrl = proxy.getProxyUrl(videoUri);

        videoView.setVideoURI(Uri.parse(videoUri));
        videoView.requestFocus();
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {

            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {


                videoView.setVideoURI(Uri.parse(videoUri));

                videoView.requestFocus();
                //videoView_full.start();

            }
        });


        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.d(All_Constants_Urls.TAG, "setOnErrorListener ");
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        videoView.stopPlayback();
        super.onResume();
    }

    @Override
    protected void onPause() {
        videoView.stopPlayback();
        super.onPause();
    }


    public void onBackPressed(){
        finish();
        super.onBackPressed();
    }

}
