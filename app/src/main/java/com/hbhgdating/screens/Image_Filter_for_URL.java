package com.hbhgdating.screens;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hbhgdating.R;
import com.hbhgdating.adapter.ImageAdapter;
import com.hbhgdating.slider.SliderLayout;
import com.hbhgdating.utils.Global_Class;
import com.hbhgdating.utils.Initialize_Filter;
import com.hbhgdating.utils.Utility;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import net.alhazmy13.imagefilter.ImageFilter;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Developer on 6/22/17.
 */

public class Image_Filter_for_URL extends AppCompatActivity {




    ViewPager viewpager;
    SliderLayout slider_layout;
    private Gallery mGallery;

    ImageView image;
    Bitmap src, myBitmap;

    LayoutInflater inflate;
    Button btn_pick_img;

    private static final int SELECT_PHOTO = 100;
   // Bitmap origin_bitmap;
    ArrayList<String> List_Bitmap;
    ArrayList<Integer> Selected_code;

    ArrayList<Bitmap> Image_Bitmap_List;
    ArrayList<Bitmap> ViewPager_Image_Bitmap_List;

    ArrayList<String> Selected_Image_Url;
    ArrayList<String> List_Downloaded_file;

    TextView btn_skip;
    TextView btn_done;
    int y = 0;


    ProgressBar asyncDialog;
    RelativeLayout ll_progressbar;
    Dialog progressDialog;


    Utility utility;
    String imgage_url;
    String first_image;


    ImageLoaderConfiguration config;
    public ImageLoader loader;


    Global_Class global_class;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_filter_url);
        inflate = LayoutInflater.from(this);

        utility = new Utility(this);
        global_class = (Global_Class)getApplicationContext();

        //asyncDialog = (ProgressBar) findViewById(R.id.progressBar1);
        ll_progressbar = (RelativeLayout)findViewById(R.id.ll_progressbar);
        ll_progressbar.setVisibility(View.GONE);

        progressDialog = new Dialog(this, android.R.style.Theme_Translucent);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.getWindow().setContentView(R.layout.progressbar_add_filter_video);
        progressDialog.setCancelable(false);



        image = (ImageView) findViewById(R.id.image);

        mGallery = (Gallery) findViewById(R.id.gallery_view);
        //ll_view = (LinearLayout) findViewById(R.id.ll_view);
        btn_skip = (TextView) findViewById(R.id.btn_skip);
        btn_skip.setVisibility(View.GONE);
        btn_done = (TextView) findViewById(R.id.btn_done);
        btn_done.setVisibility(View.GONE);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        slider_layout = (SliderLayout) findViewById(R.id.slider_layout);

        List_Bitmap = new ArrayList<>();
        Selected_code = new ArrayList<>();
        Image_Bitmap_List = new ArrayList<>();
        Selected_Image_Url = new ArrayList<>();
        ViewPager_Image_Bitmap_List = new ArrayList<>();
        List_Downloaded_file = new ArrayList<>();

        long minRunningMemory = (1024*1024);
        Runtime runtime = Runtime.getRuntime();
        if(runtime.freeMemory()<minRunningMemory)
            System.gc();

        config = new ImageLoaderConfiguration.Builder(Image_Filter_for_URL.this)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();
        ImageLoader.getInstance().init(config);
        loader = ImageLoader.getInstance();


        btn_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                global_class.List_Bitmap_for_ProfileActivity = Image_Bitmap_List;

                y = 0;

               // Intent intent = new Intent(Image_Filter_for_URL.this, Check_Video.class);
                Intent intent = new Intent();
                intent.putExtra("code_uri_filter", y);
                setResult(RESULT_OK, intent);
                finish();

            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                global_class.List_Bitmap_for_ProfileActivity = ViewPager_Image_Bitmap_List;

               // Intent intent = new Intent(Image_Filter_for_URL.this, Check_Video.class);
                Intent intent = new Intent();
                intent.putExtra("code_url_filter", y);
                setResult(RESULT_OK, intent);
                finish();


            }
        });


        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            imgage_url = bundle.getString("image_url");

            Selected_Image_Url = getIntent().getStringArrayListExtra("image_list");

            Log.d("TAG", "Selected_Image_Url size = "+Selected_Image_Url.size());

            Download_asyncTask.execute();


            CustomPagerAdapter_Url customPagerAdapter_url = new CustomPagerAdapter_Url(Image_Filter_for_URL.this);
            viewpager.setAdapter(customPagerAdapter_url);

        }



    }



    public void SaveImageFromUrl(String link){

        try{

            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();

         //   File dbDirectory = new File(utility.Make_Directory());

            File file = new File(utility.Make_Directory(), "IMG_"+ "Demo" + ".jpg");

            InputStream inputStream = new BufferedInputStream(url.openStream());

            OutputStream fileOutput = new FileOutputStream(file);

            first_image = file.toString();
            Log.e("TAG", "Path - "+file.toString());

            byte[] buffer = new byte[1024];
            int bufferLength;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
            }
            fileOutput.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }


        Uri selectedImage = Uri.fromFile(new File(first_image));
        Bitmap bmp = decodeUri(selectedImage);
       // origin_bitmap = decodeUri(selectedImage);

        src = utility.getResizedBitmap(bmp, 280);;


    }


    AsyncTask Download_asyncTask = new AsyncTask() {

        ProgressDialog mProgressDialog;

        Initialize_Filter initialize_filter;

        @Override
        protected void onPreExecute() {

          //  ll_progressbar.setVisibility(View.VISIBLE);

            progressDialog.show();

            Image_Bitmap_List.clear();

        }
        @Override
        protected Object doInBackground(Object... params) {

            SaveImageFromUrl(imgage_url);


            for (int j = 1; j <= 12; j++){

                initialize_filter = new Initialize_Filter();

                Image_Bitmap_List.add(initialize_filter.updateFilter(src, j));
                Selected_code.add(j);


            }



            return null;
        }
        @Override
        protected void onPostExecute(Object o) {

            image.setImageBitmap(src);


           /* for (int i = 0; i < Image_Bitmap_List.size(); i++){

                ll_view.addView(getView_(i, Image_Bitmap_List.get(i)));
            }*/

            setAdapterAndListener();


            btn_skip.setVisibility(View.VISIBLE);
            btn_done.setVisibility(View.VISIBLE);

          //  ll_progressbar.setVisibility(View.GONE);

            progressDialog.dismiss();

        }
    };



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

                    new Filter_Task().execute();

                }else {

                    Pay_Dialog(Image_Bitmap_List.get(position));
                }

            }
        });
    }




    View getView_(final int x, final Bitmap bitmap) {

        View rootView = inflate.inflate(R.layout.single_image, null);

        ImageView imageview = (ImageView) rootView.findViewById(R.id.imageview);
        RelativeLayout rl_lock = (RelativeLayout) rootView.findViewById(R.id.rl_lock);

        if (x < 6){
            rl_lock.setVisibility(View.GONE);
        }else {
            rl_lock.setVisibility(View.VISIBLE);
        }

        imageview.setImageBitmap(bitmap);


        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (x < 6){

                    viewpager.setAdapter(null);

                    y = Selected_code.get(x);

                    new Filter_Task().execute();

                }else {

                    Pay_Dialog(bitmap);

                }


            }
        });

        return rootView;

    }



    private Bitmap decodeUri(Uri selectedImage)  {

        try {

            // Decode image size
           /* BitmapFactory.Options o = new BitmapFactory.Options();
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
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);*/


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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Image_Filter_for_URL.this);

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



    /////////////////////////////////////////////////
    ////////////////////////////////////

    /////////// Adapter for url image ....

    public class CustomPagerAdapter_Url extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter_Url(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return Selected_Image_Url.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.viewpager_image, container, false);

            ImageView image_item = (ImageView) itemView.findViewById(R.id.image_item);


            loader.displayImage(Selected_Image_Url.get(position), image_item);


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




    ////////////////////////////////////////////
    /////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////


    private class Filter_Task extends AsyncTask<Void, Void, Void> {


        ProgressDialog asyncDialog = new ProgressDialog(Image_Filter_for_URL.this);
        String typeStatus;
        Initialize_Filter initialize_filter;


        @Override
        protected void onPreExecute() {


           // ll_progressbar.setVisibility(View.VISIBLE);

            progressDialog.show();

            initialize_filter = new Initialize_Filter();

            btn_skip.setVisibility(View.GONE);
            btn_done.setVisibility(View.GONE);

            ViewPager_Image_Bitmap_List.clear();


            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            if (List_Downloaded_file.size() == 0){

                for (int i = 0; i < Selected_Image_Url.size(); i++){

                    String file = SaveImageFromUrl_2(Selected_Image_Url.get(i), i);

                    List_Downloaded_file.add(file);

                    Uri selectedImage = Uri.fromFile(new File(file));
                    Bitmap bmp = decodeUri(selectedImage);

                    ViewPager_Image_Bitmap_List.add(initialize_filter.updateFilter(bmp, y));


                }

            }else {

                for (int i = 0; i < List_Downloaded_file.size(); i++){

                    Uri selectedImage = Uri.fromFile(new File(List_Downloaded_file.get(i)));
                    Bitmap bmp = decodeUri(selectedImage);

                    ViewPager_Image_Bitmap_List.add(initialize_filter.updateFilter(bmp, y));

                }

            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //hide the dialog

            /// Show view pager

            CustomPagerAdapter_Bitmap customPagerAdapter_bitmap = new CustomPagerAdapter_Bitmap(Image_Filter_for_URL.this);
            viewpager.setAdapter(customPagerAdapter_bitmap);
            customPagerAdapter_bitmap.notifyDataSetChanged();

          //  ll_progressbar.setVisibility(View.GONE);

            progressDialog.dismiss();

            btn_skip.setVisibility(View.VISIBLE);
            btn_done.setVisibility(View.VISIBLE);


            super.onPostExecute(result);
        }

    }


    public String SaveImageFromUrl_2(String link, int c){

        String string_file = null;

        try{

            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            //   File dbDirectory = new File(utility.Make_Directory());

            File file = new File(utility.Make_Directory(), "IMG_"+ "Demo" + c + ".jpg");

            InputStream inputStream = new BufferedInputStream(url.openStream());

            OutputStream fileOutput = new FileOutputStream(file);

            string_file = file.toString();
            Log.e("TAG", "Path - "+file.toString());

            byte[] buffer = new byte[1024];
            int bufferLength;

            while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
                fileOutput.write(buffer, 0, bufferLength);
            }
            fileOutput.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }



        return string_file;
    }


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
