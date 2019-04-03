package com.hbhgdating.Fb_Insta_Media;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hbhgdating.R;
import com.hbhgdating.lazyload.ImageLoader;
import com.hbhgdating.utils.Global_Class;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Developer on 5/4/17.
 */

public class VideoGridListAdapter extends BaseAdapter {
     private Context context;
    private ArrayList<HashMap<String, String>> imageThumbList;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;

    Global_Class global_class;

    public VideoGridListAdapter(Context context, ArrayList<HashMap<String, String>> imageThumbList) {
        this.context = context;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageThumbList = imageThumbList;
        this.imageLoader = new ImageLoader(context);

        global_class = (Global_Class)context.getApplicationContext();
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
    public View getView(final int position, View convertView, ViewGroup parent) {


        View view = inflater.inflate(R.layout.video_adapter_item, null);
        VideoGridListAdapter.Holder holder = new VideoGridListAdapter.Holder();
        holder.iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
        imageLoader.DisplayImage(imageThumbList.get(position).get("thumb_image"), holder.iv_thumb);



        return view;
    }

    private class Holder {
        private ImageView iv_thumb;
    }

}
