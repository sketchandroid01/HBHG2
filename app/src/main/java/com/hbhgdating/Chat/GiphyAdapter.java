package com.hbhgdating.Chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hbhgdating.R;
import com.hbhgdating.utils.All_Constants_Urls;
import com.hbhgdating.utils.SharedPref;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

public class GiphyAdapter extends RecyclerView.Adapter<GiphyAdapter.ViewHolder> {

    ArrayList<HashMap<String,String>> final_list;
    ArrayList<ChatMessage> arrData = new ArrayList<ChatMessage>();
    SingleChatRoomDetailListViewAdapter adapter;
    Context context;
    public ImageLoader loader;
    DisplayImageOptions defaultOptions;
    String recieverid;
    SharedPref sharedPref;
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a");


    final String TAG = All_Constants_Urls.TAG;

    public GiphyAdapter(Context context, ArrayList<HashMap<String,String>> final_list, ArrayList<ChatMessage> arrData, SingleChatRoomDetailListViewAdapter adapter, String id) {
        super();
        this.context = context;
        this.final_list=final_list;
        this.arrData=arrData;
        this.adapter=adapter;
        this.recieverid=id;


        sharedPref = new SharedPref(context);
        defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisk(true).resetViewBeforeLoading(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();

        final ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(defaultOptions)
                .build();
        ImageLoader.getInstance().init(config);
        loader = ImageLoader.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.giphy_row, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        final String url = final_list.get(i).get("fixed_width_url");
        final String original_url = final_list.get(i).get("original_url");
        final String fixed_height_url = final_list.get(i).get("fixed_height_url");

        Glide.with(context).load(original_url).into(viewHolder.img);

        viewHolder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendGif(fixed_height_url);


            }
        });

    }

    @Override
    public int getItemCount() {
        return final_list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView img;

        public ViewHolder(View itemView) {
            super(itemView);
            img=(ImageView)itemView.findViewById(R.id.img);
        }

    }

    public void sendGif(String url) {
        Date today = Calendar.getInstance().getTime();
            final ChatMessage chatMessage = new ChatMessage("gif","me", "test",true);
            chatMessage.Date = dateFormat.format(today);
            chatMessage.body = url;
            arrData.add(chatMessage);
            adapter.notifyDataSetChanged();

        postChat(url);
    }



    public void postChat(String content){

        String URL="";
        URL = All_Constants_Urls.POSTCHAT;

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
        params.put(All_Constants_Urls.from_user_id, sharedPref.get_Use_Id());
        params.put(All_Constants_Urls.to_user_id, recieverid);
        params.put(All_Constants_Urls.content, content);
        params.put(All_Constants_Urls.msg_type, "5");


      //  Log.d(TAG ,"AsyncHttpClient URL- " + URL);
      //  Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


        int DEFAULT_TIMEOUT = 30 * 1000;
        client.setMaxRetriesAndTimeout(5 , DEFAULT_TIMEOUT);
        client.post(URL, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

              //  Log.d(TAG, "onSuccess- " + response.toString());

                if (response != null) {
                    try {


                        int success = response.optInt("success");

                        if (success == 0){

                            Toast.makeText(context, "Something error", Toast.LENGTH_LONG).show();



                        }else
                        if (success == 1){



                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }



            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                Log.d(TAG, "onFailure- " + res);


                android.app.AlertDialog alert = new android.app.AlertDialog.Builder(context).create();
                alert.setMessage("Server Error");
                alert.show();
            }


        });


    }

}

