package com.hbhgdating.Chat;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.hbhgdating.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by Developer on 9/21/17.
 */

public class ChatImageFullScreen extends AppCompatActivity {

    ImageView back,imageView;
    public ImageLoader loader;
    DisplayImageOptions defaultOptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_img_full);

        imageView=(ImageView)findViewById(R.id.img_full_screen);
        back=(ImageView)findViewById(R.id.back_full);



        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.img1)
                .showImageOnLoading(R.drawable.img1)
                .showImageOnFail(R.drawable.img1)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
        loader = ImageLoader.getInstance();


        if (getIntent().getStringExtra("key").matches("loaded")){


                loader.displayImage(getIntent().getStringExtra("img"),imageView, defaultOptions);
        }else {

            imageView.setImageURI(Uri.parse(getIntent().getStringExtra("img")));
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
