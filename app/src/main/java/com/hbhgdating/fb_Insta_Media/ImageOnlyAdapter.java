package com.hbhgdating.fb_Insta_Media;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.hbhgdating.R;
import com.hbhgdating.lazyload.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Developer on 9/15/17.
 */

public class ImageOnlyAdapter extends BaseAdapter {


    private Context context;
    private ArrayList<String> imageThumbList;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    SparseBooleanArray mSparseBooleanArray;


    CheckBox[] ckbDigits;



    public ImageOnlyAdapter(Context context_, ArrayList<String> imageThumbList) {
        inflater = (LayoutInflater) context_
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.context = context_;
        this.imageThumbList = imageThumbList;
        this.imageLoader = new ImageLoader(context);

        this.mSparseBooleanArray = new SparseBooleanArray();

        ckbDigits = new CheckBox[imageThumbList.size()];
    }


    public ArrayList<String> getCheckedItems() {
        ArrayList<String> mTempArry = new ArrayList<String>();

        for(int i=0;i<imageThumbList.size();i++) {
            if(mSparseBooleanArray.get(i)) {
                mTempArry.add(imageThumbList.get(i));
            }
        }

        return mTempArry;
    }





    @Override
    public int getCount() {
        return imageThumbList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.image_adapter_item, null);
        final ImageOnlyAdapter.Holder holder = new ImageOnlyAdapter.Holder();
        holder.ivPhoto = (ImageView) view.findViewById(R.id.ivImage);
        imageLoader.DisplayImage(imageThumbList.get(position), holder.ivPhoto);


        holder.mCheckBox = (CheckBox) view.findViewById(R.id.mCheckBox);
        holder.mCheckBox.setVisibility(View.GONE);




        return view;
    }

    private class Holder {
        private ImageView ivPhoto;
        private CheckBox mCheckBox;
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    private int geLength() {
        ArrayList<String> mTempArry = new ArrayList<String>();

        for(int i=0;i<imageThumbList.size();i++) {
            if(mSparseBooleanArray.get(i)) {
                mTempArry.add(imageThumbList.get(i));
            }
        }

        return mTempArry.size();
    }


}
