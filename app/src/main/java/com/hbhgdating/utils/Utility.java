package com.hbhgdating.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.text.format.DateFormat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Developer on 6/22/17.
 */

public class Utility {

    Context mContext;

    public Utility(Context context) {
        mContext = context;
    }


    public String getPathOfAppInternalStorage() {

        String FILE = Environment.getExternalStorageDirectory().getAbsolutePath().toString();

        //  return mContext.getApplicationContext().getFilesDir().getAbsolutePath();
        return FILE;
    }


    public File Make_Directory(){

        File dbDirectory = new File(getPathOfAppInternalStorage() + "/" + "HBHG");
        if(!dbDirectory.exists()) {
            dbDirectory.mkdir();
        }

        return dbDirectory;
    }


    public String get_HBHG_Directory() {
        String FILE = Make_Directory().toString();

        FILE = FILE + "/";


        Log.d("TAG", "dir = "+FILE);

        //  return mContext.getApplicationContext().getFilesDir().getAbsolutePath();
        return FILE;
    }


    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }

        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    public Bitmap decodeUri_640(Uri selectedImage)  {

        try {

            /*// Decode image size
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
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);*/


            Bitmap bitmap= BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(selectedImage));
            return getResizedBitmap(bitmap, 640);

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public String SaveImageFromUrl(String link, String title_string, int k){

        // Log.e("TAG", "link - "+link);

        File file = null;

        try{

            URL url = new URL(link);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            //   File dbDirectory = new File(utility.Make_Directory());

            file = new File(Make_Directory(), title_string + k +".jpg");

            InputStream inputStream = new BufferedInputStream(url.openStream());

            OutputStream fileOutput = new FileOutputStream(file);

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


        return file.toString();

    }


    public String getUserNameFromAllMyMatches(String data, String user_id){

        String userName = "";

        try {
            JSONObject response = new JSONObject(data);

            JSONArray data_array = response.getJSONArray(All_Constants_Urls.data);

            Log.d(All_Constants_Urls.TAG, "length - " +data_array.length());
            for (int i = 0; i < data_array.length(); i++){
                JSONObject object = data_array.getJSONObject(i);

                int is_blocked = object.optInt(All_Constants_Urls.is_blocked);
                if (is_blocked == 0){

                    JSONObject basic_info = object.getJSONObject(All_Constants_Urls.basic_info);
                    String ToUserID = basic_info.getString(All_Constants_Urls.id);

                    if (user_id.matches(ToUserID)){

                        userName = basic_info.getString(All_Constants_Urls.name);
                    }

                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return userName;
    }



    public String getConvertDate() {
        long milliSeconds = System.currentTimeMillis();
        String dateFormat = "dd/MM/yyyy hh:mm:ss a";
        String dateFormat1 = "YYYY-MM-DD HH:MM:SS";
        return DateFormat.format(dateFormat1, milliSeconds).toString();
    }
}
