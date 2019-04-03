package com.hbhgdating.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.desmond.squarecamera.CameraActivity;
import com.hbhgdating.Fb_Insta_Media.AllMediaFiles;
import com.hbhgdating.Fb_Insta_Media.ImageFilesOnly;
import com.hbhgdating.R;
import com.hbhgdating.screens.Facebook_Insta_Login;
import com.hbhgdating.screens.GoogleSearchView;
import com.hbhgdating.utils.Global_Class;
import com.hbhgdating.utils.SharedPref;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Developer on 5/12/17.
 */

public class Profile_Vp_Adapter_ extends PagerAdapter {


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

    public static final int RESULT_LOAD_IMAGE = 105;
    public static final int RESULT_FB_INSTA_Image = 108;
    private static final int GOOGLE_PHOTO_SELECTION = 2002;
    private static final int CAMERA_REQUEST = 2003;


    private CallbackInterface mCallback;
    File final_pic;


    AlertDialog alertDialog;
    String Image_key;
    String et_text;
    String Text_key;
   // Dialog_Name customize_Dia_Name;

    ImageView iv1,iv2,iv3,image1Play,image2Play,image3Play;

    SharedPref sharedPref;


    public interface CallbackInterface{

        /**
         * Callback invoked when clicked
         * @param position - the position
         * @param Image_key - the text to pass back
         */
        void onHandleSelection(int position, String Image_key, String et_text, String Text_key);
    }


    public Profile_Vp_Adapter_(Context context_, TextView imgEditUser_, ArrayList<HashMap<String, String>> List_Text_,
                               ArrayList<HashMap<String, Integer>> List_Image_, ArrayList<HashMap<String, String>> List_Image_Uri_,
                               ImageView iv1_,ImageView iv2_,ImageView iv3_,ImageView image1Play_,ImageView image2Play_,
                               ImageView image3Play_) {

        this.context = context_;
        inflater = LayoutInflater.from(context);
        this.imgEditUser = imgEditUser_;
        this.List_Text = List_Text_;
        this.List_Image = List_Image_;
        this.List_Image_Uri = List_Image_Uri_;
        this.iv1 =iv1_;
        this.iv2 =iv2_;
        this.iv3 =iv3_;
        this.image1Play =image1Play_;
        this.image2Play =image2Play_;
        this.image3Play =image3Play_;


        global_class = (Global_Class)context.getApplicationContext();

        sharedPref = new SharedPref(context);

        try{
            mCallback = (CallbackInterface) context;
        }catch(ClassCastException ex){
            //.. should log the error or throw and exception
            Log.e("TAG","Must implement the CallbackInterface in the Activity", ex);
        }


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


        this.txtMusic1 = (TextView) view.findViewById(R.id.txtMusic1);
        txtMusic2 = (TextView) view.findViewById(R.id.txtMusic2);
        txtMusic3 = (TextView) view.findViewById(R.id.txtMusic3);


        relMusic1 = (LinearLayout) view.findViewById(R.id.relMusic1);
        relMusic2 = (LinearLayout) view.findViewById(R.id.relMusic2);
        relMusic3 = (LinearLayout) view.findViewById(R.id.relMusic3);


        ///////////////////////////


        imgMusic1.setImageResource(List_Image.get(position).get("i1"));
        imgMusic2.setImageResource(List_Image.get(position).get("i2"));
        imgMusic3.setImageResource(List_Image.get(position).get("i3"));


        txtMusic1.setText(List_Text.get(position).get("t1"+position));
        txtMusic2.setText(List_Text.get(position).get("t2"+position));
        txtMusic3.setText(List_Text.get(position).get("t3"+position));


        if (List_Image_Uri.get(position).get("p1"+position)!= null){

            String selectedImage = List_Image_Uri.get(position).get("p1"+position);

            if (selectedImage.startsWith("http")){

                Picasso.with(context).load(selectedImage).error(R.mipmap.grid_bg_3).into( imgMusic1, new Callback() {
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

            }else{

                Uri uri = Uri.parse(selectedImage);

                InputStream image_stream = null;
                try {
                    image_stream = context.getContentResolver().openInputStream(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = BitmapFactory.decodeStream(image_stream);

                imgMusic1.setImageBitmap(bitmap);
                imgMusic1.setScaleType(ImageView.ScaleType.CENTER_CROP);


            }



        }else {


        }


        if (List_Image_Uri.get(position).get("p2"+position)!= null) {

            // Uri selectedImage = List_Image_Uri.get(position).get("p2"+position);

            String selectedImage = List_Image_Uri.get(position).get("p2" + position);

            if (selectedImage.startsWith("http")) {

                Picasso.with(context).load(selectedImage).error(R.mipmap.grid_bg_3).into( imgMusic2, new Callback() {
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


            } else  {

                Uri uri = Uri.parse(selectedImage);
                InputStream image_stream = null;


                try {
                    image_stream = context.getContentResolver().openInputStream(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


                Bitmap bitmap = BitmapFactory.decodeStream(image_stream);

                imgMusic2.setImageBitmap(bitmap);

                imgMusic2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        } else {


            }


            if (List_Image_Uri.get(position).get("p3" + position) != null) {

                String selectedImage = List_Image_Uri.get(position).get("p3" + position);


                if (selectedImage.startsWith("http")) {

                    Picasso.with(context).load(selectedImage).error(R.mipmap.grid_bg_3).into( imgMusic3, new Callback() {
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


                } else  {

                    Uri uri = Uri.parse(selectedImage);

                    InputStream image_stream = null;
                    try {
                        image_stream = context.getContentResolver().openInputStream(uri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(image_stream);

                    imgMusic3.setImageBitmap(bitmap);
                    imgMusic3.setScaleType(ImageView.ScaleType.CENTER_CROP);

                }


            }


        imgEditUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (global_class.isEdit) {

                    global_class.isEdit = false;
                    imgEditUser.setText("Edit");

                    image1Play.setImageResource(R.mipmap.rename_96);
                    image2Play.setImageResource(R.mipmap.rename_96);
                    image3Play.setImageResource(R.mipmap.rename_96);

                    iv2.setImageResource(R.mipmap.glass_tint);
                    iv3.setImageResource(R.mipmap.glass_tint);


                   // notifyDataSetChanged();


                }else {

                    global_class.isEdit = true;
                    imgEditUser.setText("Save");

                    image1Play.setImageResource(R.mipmap.edit);
                    image2Play.setImageResource(R.mipmap.edit);
                    image3Play.setImageResource(R.mipmap.edit);

                    iv2.setImageResource(R.mipmap.glass_tint);
                    iv3.setImageResource(R.mipmap.glass_tint);


                    //notifyDataSetChanged();

                }



            }
        });



        relMusic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (global_class.isEdit){

                    Image_key = "p1"+position;

                    et_text = txtMusic1.getText().toString();

                    Text_key = "t1"+position;

                    if(mCallback != null){
                        mCallback.onHandleSelection(position, Image_key, et_text, Text_key);
                    }


                    CustomDialog();
                }else{
                    ////////////////alertdialog///////////////
                }



            }
        });

        relMusic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (global_class.isEdit){

                    Image_key = "p2"+position;

                    et_text = txtMusic2.getText().toString();

                    Text_key = "t2"+position;

                    if(mCallback != null){
                        mCallback.onHandleSelection(position, Image_key, et_text, Text_key);
                    }

                    CustomDialog();
                }else{
                    ////////////////alertdialog///////////////
                }


            }
        });

        relMusic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (global_class.isEdit){

                    Image_key = "p3"+position;

                    et_text = txtMusic3.getText().toString();

                    Text_key = "t3"+position;

                    if(mCallback != null){
                        mCallback.onHandleSelection(position, Image_key, et_text, Text_key);
                    }


                    CustomDialog();

                }else{
                    ////////////////alertdialog///////////////

                }



            }
        });








        collection.addView(view, 0);
        return view;
    }

    public void  Dialog_image(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        // ...Irrelevant code for customizing the buttons and title
        // LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_img, null);
        dialogBuilder.setView(dialogView);


        ImageView img_dialog = (ImageView) dialogView.findViewById(R.id.img_dialog);



        alertDialog = dialogBuilder.create();
        alertDialog.show();



    }


    public void CustomDialog(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        // ...Irrelevant code for customizing the buttons and title
       // LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_dialog_img, null);
        dialogBuilder.setView(dialogView);

        final RelativeLayout attach_gallery = (RelativeLayout) dialogView.findViewById(R.id.attach_gallery);
        RelativeLayout rl_video = (RelativeLayout) dialogView.findViewById(R.id.rl_video);
        RelativeLayout insta_rl = (RelativeLayout) dialogView.findViewById(R.id.insta_rl);
        RelativeLayout rl_fb = (RelativeLayout) dialogView.findViewById(R.id.rl_fb);
        RelativeLayout rl_google_img = (RelativeLayout) dialogView.findViewById(R.id.rl_google_img);




        alertDialog = dialogBuilder.create();
        alertDialog.show();


        insta_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity origin = (Activity) context;

                if (sharedPref.isLOGIN_INSTA()){

                    Intent intent = new Intent(context, ImageFilesOnly.class);
                    intent.putExtra("key", "insta");
                    origin.startActivityForResult(intent, RESULT_FB_INSTA_Image);
                    alertDialog.dismiss();

                }else {

                   // Toast.makeText(getApplicationContext(), "You are not login with Instagram", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(context, Facebook_Insta_Login.class);
                    intent.putExtra("key", "insta");
                    context.startActivity(intent);
                    alertDialog.dismiss();

                }


            }
        });

        rl_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity origin = (Activity) context;

                if (sharedPref.isLOGIN_FB()){


                    Intent intent = new Intent(context, ImageFilesOnly.class);
                    intent.putExtra("key","fb");
                    origin.startActivityForResult(intent, RESULT_FB_INSTA_Image);
                    alertDialog.dismiss();

                }else {

                  //  Toast.makeText(getApplicationContext(), "You are not login with Facebook", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(context, Facebook_Insta_Login.class);
                    intent.putExtra("key", "fb");
                    context.startActivity(intent);
                    alertDialog.dismiss();
                }

            }
        });

        attach_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Activity origin = (Activity) context;

                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                origin.startActivityForResult(intent, RESULT_LOAD_IMAGE);
                alertDialog.dismiss();
            }
        });

        rl_google_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Activity origin = (Activity) context;
                Intent intent = new Intent(getApplicationContext(), GoogleSearchView.class);
                origin.startActivityForResult(intent, GOOGLE_PHOTO_SELECTION);
                alertDialog.dismiss();
            }
        });

        rl_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Activity origin = (Activity) context;
                Intent startCustomCameraIntent = new Intent(getApplicationContext(), CameraActivity.class);
                origin.startActivityForResult(startCustomCameraIntent, CAMERA_REQUEST);


                alertDialog.dismiss();

            }
        });



    }






        @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
    }
    /*
        public View getView(final int position, View convertView, ViewGroup parent) {


            return convertView;
        }
    */
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