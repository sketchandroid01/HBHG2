package com.hbhgdating.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hbhgdating.R;
import com.hbhgdating.utils.Common;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class googleSearchViewAdapter extends BaseAdapter {

    private Activity mactivity;
    private LayoutInflater inflater = null;
    private ViewHolder viewHolder;
    ArrayList<String> google_search_val = new ArrayList<>();
    private String mCurrentPhotoPath;
    ProgressDialog progressDialog;
    String strSearchText;


    private static class ViewHolder {
        ImageView grid_item_image;
        ImageView image;
    }

    public googleSearchViewAdapter(Activity con, ArrayList<String> google_search_val, String strSearchText) {
        mactivity = con;
        progressDialog = new ProgressDialog(con);
        this.google_search_val = google_search_val;
        this.strSearchText = strSearchText;
        inflater = LayoutInflater.from(mactivity);

    }

    @Override
    public int getCount() {
        return google_search_val.size();
    }

    @Override
    public Object getItem(int arg0) {
        return arg0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.google_grid_item_layout, parent,
                    false);
            viewHolder = new ViewHolder();

            viewHolder.grid_item_image = (ImageView) convertView.findViewById(R.id.grid_item_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }



        Picasso.with(mactivity).load(google_search_val.get(position))
                .placeholder(R.mipmap.grid_bg_3) // optional
                .into(viewHolder.grid_item_image);


        viewHolder.grid_item_image.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                detail(v, position);
            }
        });

        return convertView;
    }

    private void detail(View view, int position) {
        String strSourceUrl = google_search_val.get(position);


        progressDialog.dismiss();
        Intent intent = new Intent();
        intent.putExtra("sourceurl", strSourceUrl);
        intent.putExtra("text_string",strSearchText);
        Log.d("TAG", "intent path : "+strSourceUrl);
        mactivity.setResult(mactivity.RESULT_OK, intent);
        mactivity.finish();

        //  new progressBack(strSourceUrl).execute();
    }

    public class ProgressBack extends AsyncTask<String, Void, String> {
        String strUrl;

        public ProgressBack(String strurl) {
            strUrl = strurl;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(mactivity, "",
                    mactivity.getString(R.string.fetchvideo), true);
        }

        @Override
        protected String doInBackground(String... arg0) {
            File photoFile = null;
            try {
                photoFile = createImageFileExtrenal();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            downloadFile(strUrl, photoFile);
            return "";
        }

        @SuppressWarnings("static-access")
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            Intent intent = new Intent();
            intent.putExtra("sourceurl", mCurrentPhotoPath);
            Log.d("TAG", "intent path : "+mCurrentPhotoPath);
            mactivity.setResult(mactivity.RESULT_OK, intent);
            mactivity.finish();
        }
    }

//    @SuppressLint("SimpleDateFormat")
//    private File createImageFileExtrenal() throws IOException {
//        String RootDir = Environment.getExternalStorageDirectory()
//                + File.separator + "Pictures";
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
//                .format(new Date());
//        String imageFileName = timeStamp + ".jpg";
//        File storageDir = new File(RootDir);
//        File imageFile = new File(storageDir, imageFileName);
//        mCurrentPhotoPath = imageFile.getAbsolutePath();
//        return imageFile;
//    }

    @SuppressLint("SimpleDateFormat")
    private File createImageFileExtrenal() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        String imageFileName = timeStamp + ".jpg";

        Log.d("TAG", "imageFileName: "+imageFileName);

        File storageDir = new File(Common.strTmpImageFolder);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File imageFile = new File(storageDir, imageFileName);
        mCurrentPhotoPath = imageFile.getAbsolutePath();
        Log.d("TAG", "mCurrentPhotoPath adapter : "+mCurrentPhotoPath);
        return imageFile;
    }



    private static void downloadFile(String url1, File outputFile) {
        try {
            URL url = new URL(url1);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            FileOutputStream fileOutput = new FileOutputStream(outputFile);
            InputStream inputStream = urlConnection.getInputStream();
            byte[] buffer = new byte[1024];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
            }
            fileOutput.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
