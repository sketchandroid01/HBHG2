package com.hbhgdating.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hbhgdating.R;
import com.hbhgdating.screens.Image_Filter_for_Uri;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
  private ArrayList<Bitmap> mBitmapArray;
  private Context context;
  private int itemBackground;
  private LayoutInflater inflate;

  public ImageAdapter(Context c, ArrayList<Bitmap> bitmapArray) {
    context = c;
    inflate = LayoutInflater.from(c);
    mBitmapArray = bitmapArray;
    TypedArray a = context.obtainStyledAttributes(R.styleable.MyGallery);
    itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
    a.recycle();
  }

  public int getCount() {
    return mBitmapArray.size();
  }

  public Object getItem(int position) {
    return position;
  }

  public long getItemId(int position) {
    return position;
  }

  public View getView(int position, View convertView, ViewGroup parent) {
    /*ImageView imageView = new ImageView(context);
    imageView.setImageBitmap(mBitmapArray.get(position));
    imageView.setLayoutParams(new Gallery.LayoutParams(400, 400));
    imageView.setBackgroundResource(itemBackground);*/


    View rootView = inflate.inflate(R.layout.single_image, null);

    ImageView imageview = (ImageView) rootView.findViewById(R.id.imageview);
    imageview.setImageBitmap(mBitmapArray.get(position));
    imageview.setBackgroundResource(itemBackground);
    RelativeLayout rl_lock = (RelativeLayout) rootView.findViewById(R.id.rl_lock);

    if (position < 6){

      rl_lock.setVisibility(View.GONE);

    }else {

      rl_lock.setVisibility(View.VISIBLE);
    }







    return rootView;
  }




}