package com.hbhgdating.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbhgdating.R;
import com.hbhgdating.utils.Global_Class;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Developer on 5/12/17.
 */

public class Userprofile_Vp_Adapter extends PagerAdapter {


    private Context context;

    Global_Class global_class;
    TextView imgEditUser;

    View view;

    boolean initial;


    LayoutInflater inflater;

    ArrayList<HashMap<String, String>> List_Text;
    ArrayList<HashMap<String, Integer>> List_Image;
    ArrayList<HashMap<String, String>> List_Image_Uri;

    ImageView imgMusic1,imgMusic2,imgMusic3;
    TextView txtMusic1,txtMusic2,txtMusic3;
    LinearLayout relMusic1, relMusic2, relMusic3;


    ImageView image1Play,image2Play,image3Play;


    public Userprofile_Vp_Adapter(Context context_, ArrayList<HashMap<String, String>> List_Text_,
                                  ArrayList<HashMap<String, Integer>> List_Image_,
                                  ArrayList<HashMap<String, String>> List_Image_Uri_,
                                  ImageView image1Play_,ImageView image2Play_,
                                  ImageView image3Play_) {

        this.context = context_;
        inflater = LayoutInflater.from(context);

        this.List_Text = List_Text_;

        this.List_Image = List_Image_;
        this.List_Image_Uri = List_Image_Uri_;

        this.image1Play =image1Play_;
        this.image2Play =image2Play_;
        this.image3Play =image3Play_;


        global_class = (Global_Class)context.getApplicationContext();



    }


    @Override
    public int getCount() {
        return List_Text.size();
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    @Override
    public Object instantiateItem(ViewGroup collection, final int position) {

        view = inflater.inflate(R.layout.listen_layout, null);


        imgMusic1 = (ImageView) view.findViewById(R.id.imgMusic1);
        imgMusic2 = (ImageView) view.findViewById(R.id.imgMusic2);
        imgMusic3 = (ImageView) view.findViewById(R.id.imgMusic3);


        txtMusic1 = (TextView) view.findViewById(R.id.txtMusic1);
        txtMusic2 = (TextView) view.findViewById(R.id.txtMusic2);
        txtMusic3 = (TextView) view.findViewById(R.id.txtMusic3);


        relMusic1 = (LinearLayout) view.findViewById(R.id.relMusic1);
        relMusic2 = (LinearLayout) view.findViewById(R.id.relMusic2);
        relMusic3 = (LinearLayout) view.findViewById(R.id.relMusic3);


        ///////////////////////////


        //imgMusic1.setImageResource(List_Image.get(position).get("i1"));
        //imgMusic2.setImageResource(List_Image.get(position).get("i2"));
       // imgMusic3.setImageResource(List_Image.get(position).get("i3"));


        txtMusic1.setText(List_Text.get(position).get("t1" + position));
        txtMusic2.setText(List_Text.get(position).get("t2" + position));
        txtMusic3.setText(List_Text.get(position).get("t3" + position));




        if (List_Image_Uri.get(position).get("p1"+position)!= null) {

            String selectedImage = List_Image_Uri.get(position).get("p1" + position);

            if (selectedImage.startsWith("http")) {

                Picasso.with(context).load(selectedImage).error(R.mipmap.grid_bg_3).into(imgMusic1, new Callback() {
                    @Override
                    public void onSuccess() {
                        //  Log.d("TAG", "onSuccess");
                    }

                    @Override
                    public void onError() {
                        //  Toast.makeText(mactivity, "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
                imgMusic1.setScaleType(ImageView.ScaleType.CENTER_CROP);

            }

        }


        if (List_Image_Uri.get(position).get("p2"+position)!= null) {

            String selectedImage = List_Image_Uri.get(position).get("p2" + position);

            if (selectedImage.startsWith("http")) {

                Picasso.with(context).load(selectedImage).error(R.mipmap.grid_bg_3).into(imgMusic2, new Callback() {
                    @Override
                    public void onSuccess() {
                        //  Log.d("TAG", "onSuccess");
                    }

                    @Override
                    public void onError() {
                        //  Toast.makeText(mactivity, "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                });

                imgMusic2.setScaleType(ImageView.ScaleType.CENTER_CROP);

            }

        }


        if (List_Image_Uri.get(position).get("p3"+position)!= null) {

            String selectedImage = List_Image_Uri.get(position).get("p3" + position);

            if (selectedImage.startsWith("http")) {

                Picasso.with(context).load(selectedImage).error(R.mipmap.grid_bg_3).into(imgMusic3, new Callback() {
                    @Override
                    public void onSuccess() {
                        //  Log.d("TAG", "onSuccess");
                    }

                    @Override
                    public void onError() {
                        //  Toast.makeText(mactivity, "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
                imgMusic3.setScaleType(ImageView.ScaleType.CENTER_CROP);

            }

        }



        collection.addView(view, 0);
        return view;

    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }

    @Override
    public void startUpdate(View arg0) {
    }

    @Override
    public void finishUpdate(View arg0) {
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }


    @Override
    public void notifyDataSetChanged() {

        super.notifyDataSetChanged();



    }








}
