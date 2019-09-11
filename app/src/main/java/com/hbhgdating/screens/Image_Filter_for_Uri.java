package com.hbhgdating.screens;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hbhgdating.R;
import com.hbhgdating.adapter.ImageAdapter;
import com.hbhgdating.utils.Initialize_Filter;
import com.hbhgdating.utils.Utility;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Developer on 5/30/17.
 */

public class Image_Filter_for_Uri extends AppCompatActivity {


    ViewPager viewpager;
    ImageView image;
    private Gallery mGallery;

    Bitmap src, myBitmap;
    LayoutInflater inflate;
    private static final int SELECT_PHOTO = 100;
    Bitmap origin_bitmap;
    ArrayList<String> List_Bitmap;
    ArrayList<Integer> Selected_code;
    ArrayList<Bitmap> Image_Bitmap_List;

    ArrayList<Bitmap> ViewPager_Image_Bitmap_List;
    ArrayList<String> Selected_Image_Uri;

    TextView btn_skip;
    TextView btn_done;
    int y;

  //  RelativeLayout ll_progressbar;

    Dialog progressDialog;


    Utility utility;
    String imgage_url;
    String first_image;


    ImageLoaderConfiguration config;
    public ImageLoader loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_filter_uri);
        inflate = LayoutInflater.from(this);

        utility = new Utility(this);

       //asyncDialog = (ProgressBar) findViewById(R.id.progressBar1);
       // ll_progressbar = (RelativeLayout)findViewById(R.id.ll_progressbar);
      //  ll_progressbar.setVisibility(View.GONE);

        progressDialog = new Dialog(this, android.R.style.Theme_Translucent);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setContentView(R.layout.progressbar_add_filter_video);
        progressDialog.setCanceledOnTouchOutside(false);



        image = (ImageView) findViewById(R.id.image);
        mGallery = (Gallery) findViewById(R.id.gallery_view);
        btn_skip = (TextView) findViewById(R.id.btn_skip);
        btn_skip.setVisibility(View.GONE);
        btn_done = (TextView) findViewById(R.id.btn_done);
        btn_done.setVisibility(View.GONE);
        viewpager = (ViewPager) findViewById(R.id.viewpager);

        List_Bitmap = new ArrayList<>();
        Selected_code = new ArrayList<>();
        Image_Bitmap_List = new ArrayList<>();
        Selected_Image_Uri = new ArrayList<>();
        ViewPager_Image_Bitmap_List = new ArrayList<>();

        long minRunningMemory = (1024*1024);
        Runtime runtime = Runtime.getRuntime();
        if(runtime.freeMemory()<minRunningMemory)
            System.gc();




        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                y = 0;

                Intent intent = new Intent(Image_Filter_for_Uri.this, Check_Video.class);
                intent.putExtra("code_uri_filter", y);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Image_Filter_for_Uri.this, Check_Video.class);
                intent.putExtra("code_uri_filter", y);
                setResult(RESULT_OK, intent);
                finish();
            }
        });



        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            String path = bundle.getString("image");

            //imgage_url = bundle.getString("image_url");

            Selected_Image_Uri = getIntent().getStringArrayListExtra("image_list");


            CustomPagerAdapter_Uri customPagerAdapter_uri = new CustomPagerAdapter_Uri(Image_Filter_for_Uri.this);
            viewpager.setAdapter(customPagerAdapter_uri);


            Uri selectedImage = Uri.fromFile(new File(path));
            Bitmap bmp = decodeUri(selectedImage);
            src = utility.getResizedBitmap(bmp, 280);


            new Demo_AsyncTask ().execute();

        }

    }


    private void setAdapterAndListener() {
        progressDialog.dismiss();
        mGallery.setAdapter(new ImageAdapter(this, Image_Bitmap_List));
        int center = Image_Bitmap_List.size()/2;
        //mGallery.setSelection(center);
        mGallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //mOriginalImageView.setImageBitmap(mBitmapArray[position]);
                if (position < 6){

                    viewpager.setAdapter(null);

                    y = Selected_code.get(position);

                    new Image_Filter_for_Uri.Filter_Task().execute();

                }else {

                    Pay_Dialog(Image_Bitmap_List.get(position));
                }

            }
        });
    }


    private class Demo_AsyncTask extends AsyncTask<Void, Void, Void> {

        Initialize_Filter initialize_filter;

        @Override
        protected void onPreExecute() {

            progressDialog.show();

            List_Bitmap.clear();


            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {


            for (int j = 1; j <= 12; j++){

                initialize_filter = new Initialize_Filter();

                Image_Bitmap_List.add(initialize_filter.updateFilter(src, j));
                Selected_code.add(j);

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //hide the dialog

            setAdapterAndListener();

            btn_skip.setVisibility(View.VISIBLE);
            btn_done.setVisibility(View.VISIBLE);


          //  ll_progressbar.setVisibility(View.GONE);

            progressDialog.dismiss();

            super.onPostExecute(result);
        }

    }


    /*private void saveBitmap(Bitmap bmp,String fileName, int code){

        Image_Bitmap_List.add(bmp);

        try {
            // imgMain.setImageBitmap(bmp);
            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/FilterImage" + fileName+".png");
            FileOutputStream fos = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG,90,fos);

            List_Bitmap.add(f.toString());

        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }*/



    private Bitmap decodeUri(Uri selectedImage)  {

        try {



           /* // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = 400;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;*/


            Bitmap bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));

            return utility.getResizedBitmap(bitmap, 480);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    ///// Pay dialog ....

    public void Pay_Dialog(Bitmap bitmap) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Image_Filter_for_Uri.this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.pay_dialog, null);
        alertDialogBuilder.setView(dialogView);

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();


        ImageView iv_icon = (ImageView) dialogView.findViewById(R.id.iv_icon);
        iv_icon.setImageBitmap(bitmap);

        Button bt_continue = (Button) dialogView.findViewById(R.id.bt_continue);

        bt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });




        // show it
        alertDialog.show();


    }


    ////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////

    ///////////// Viewpager Adapter ......


    public class CustomPagerAdapter_Uri extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter_Uri(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return Selected_Image_Uri.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.viewpager_image, container, false);

            ImageView image_item = (ImageView) itemView.findViewById(R.id.image_item);
            image_item.setScaleType(ImageView.ScaleType.CENTER_CROP);

            Uri image_uri = Uri.fromFile(new File(Selected_Image_Uri.get(position)));

            image_item.setImageURI(image_uri);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }


        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }


    }

    /////////////////////////////////////

    private class Filter_Task extends AsyncTask<Void, Void, Void> {

        Initialize_Filter initialize_filter;

        @Override
        protected void onPreExecute() {

           // ll_progressbar.setVisibility(View.VISIBLE);

            progressDialog.show();

            btn_skip.setVisibility(View.GONE);
            btn_done.setVisibility(View.GONE);


            ViewPager_Image_Bitmap_List.clear();


            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            for (int i = 0; i < Selected_Image_Uri.size(); i++){

                String file = (Selected_Image_Uri.get(i));

                Uri selectedImage = Uri.fromFile(new File(file));
                Bitmap bmp = decodeUri(selectedImage);

                initialize_filter = new Initialize_Filter();

                ViewPager_Image_Bitmap_List.add(initialize_filter.updateFilter(bmp, y));


            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //hide the dialog

            /// Show view pager

            CustomPagerAdapter_Bitmap customPagerAdapter_bitmap = new CustomPagerAdapter_Bitmap(Image_Filter_for_Uri.this);
            viewpager.setAdapter(customPagerAdapter_bitmap);
            customPagerAdapter_bitmap.notifyDataSetChanged();


            btn_skip.setVisibility(View.VISIBLE);
            btn_done.setVisibility(View.VISIBLE);


           // ll_progressbar.setVisibility(View.GONE);

            progressDialog.dismiss();

            super.onPostExecute(result);
        }

    }


    //////////////////////////////////////////////// Viewpager Adapter ......

    public class CustomPagerAdapter_Bitmap extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter_Bitmap(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return ViewPager_Image_Bitmap_List.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.viewpager_image, container, false);

            ImageView image_item = (ImageView) itemView.findViewById(R.id.image_item);
            image_item.setScaleType(ImageView.ScaleType.CENTER_CROP);

            image_item.setImageBitmap(ViewPager_Image_Bitmap_List.get(position));

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }


        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }


    }






}
