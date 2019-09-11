package com.hbhgdating.chat;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hbhgdating.BuildConfig;
import com.hbhgdating.R;
import com.hbhgdating.trimmer.TrimmerActivity;
import com.hbhgdating.utils.All_Constants_Urls;
import com.hbhgdating.utils.Common;
import com.hbhgdating.utils.Global_Class;
import com.hbhgdating.utils.SharedPref;
import com.hbhgdating.utils.Utility;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ssl.SSLSocketFactory;
import life.knowledge4.videotrimmer.utils.FileUtils;

public class CreateChatRoomActivity extends Activity {
	//RoundRelativeLayout relInvite, relGoLive;
	EditText edt_name, edt_tags, edt_weblink;
	TextView add_video_tv,invite,golive;
	RelativeLayout relative1, rl_button;
	ImageView img1;
	File video2upload;
	File video_thumb2upload;
	Boolean videoadded=false;
    SharedPref sharedPref;
	private Dialog progressDialog;

	AlertDialog alertDialog;
	Global_Class global_class;
	ArrayList<HashMap<String, String>> arl;
    private static final int VIDEO_CAPTURE = 101;
    private static final int PICK_VIDEO_REQUEST = 102;
	private static final int TRIMMER_ACTIVITY_RESULT_CODE = 103;
	RelativeLayout rl_invite;
    ListView listview;
    Utility utility;
	ListAdapterFriends adapterFriends;
	int sending_code;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.create_chat_room);

		progressDialog = new Dialog(this, android.R.style.Theme_Translucent);
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progressDialog.getWindow().setContentView(R.layout.progressbar_pleasewait);
		progressDialog.setCanceledOnTouchOutside(false);

		sharedPref = new SharedPref(this);
        utility = new Utility(this);

		arl = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("matches");


		setView();


		adapterFriends=new ListAdapterFriends(this,arl);
        listview.setAdapter(adapterFriends);

	}

	private void setView() {
		relative1 = (RelativeLayout) findViewById(R.id.relative1);
		rl_invite = (RelativeLayout) findViewById(R.id.rl_invite);
		rl_button = (RelativeLayout) findViewById(R.id.rl_button);
		edt_name = (EditText) findViewById(R.id.edt_name);
		edt_tags = (EditText) findViewById(R.id.edt_tags);
		img1 = (ImageView) findViewById(R.id.img1);
		//imgBack = (ImageView) findViewById(R.id.imgBack);
		add_video_tv = (TextView) findViewById(R.id.add_video_tv);
		invite = (TextView) findViewById(R.id.invite);
		golive = (TextView) findViewById(R.id.golive);
        listview = (ListView) findViewById(R.id.listview);
		global_class = (Global_Class) this.getApplicationContext();


		invite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (!edt_name.getText().toString().isEmpty()) {

					if (!edt_tags.getText().toString().isEmpty()) {

						if (videoadded) {

							rl_invite.setVisibility(View.VISIBLE);
						}else {

							Toast.makeText(CreateChatRoomActivity.this,"Add a video first",Toast.LENGTH_SHORT).show();
						}

					}else {

						Toast.makeText(CreateChatRoomActivity.this, "Enter Tags for chatroom", Toast.LENGTH_SHORT).show();

					}
				}else {
					Toast.makeText(CreateChatRoomActivity.this, "Enter Name for chatroom", Toast.LENGTH_SHORT).show();

				}


			}
		});
		golive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {


					if (adapterFriends.getSelectedString().size() < 2) {

						Toast.makeText(CreateChatRoomActivity.this, "Select atleast 2 friends", Toast.LENGTH_SHORT).show();

					} else {

						String frnd_2_inv = "";
						for (int i = 0; i < adapterFriends.getSelectedString().size(); i++) {

							if (i == adapterFriends.getSelectedString().size()) {
								frnd_2_inv = frnd_2_inv + adapterFriends.getSelectedString().get(i);
							} else {
								frnd_2_inv = frnd_2_inv + adapterFriends.getSelectedString().get(i) + ",";
							}
						}

						createChatroom(frnd_2_inv);
						rl_invite.setVisibility(View.GONE);

					}


			}
		});

		relative1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				customDialog();
			}
		});

	}


	public void customDialog() {

		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CreateChatRoomActivity.this);
		// ...Irrelevant code for customizing the buttons and title
		LayoutInflater inflater = CreateChatRoomActivity.this.getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.add_dialog_img, null);
		dialogBuilder.setView(dialogView);

		RelativeLayout attach_gallery = (RelativeLayout) dialogView.findViewById(R.id.attach_gallery);
		RelativeLayout rl_video = (RelativeLayout) dialogView.findViewById(R.id.rl_video);
		RelativeLayout insta_rl = (RelativeLayout) dialogView.findViewById(R.id.insta_rl);
		RelativeLayout rl_fb = (RelativeLayout) dialogView.findViewById(R.id.rl_fb);
		RelativeLayout rl_google_img = (RelativeLayout) dialogView.findViewById(R.id.rl_google_img);


		insta_rl.setVisibility(View.GONE);
		rl_fb.setVisibility(View.GONE);
		rl_google_img.setVisibility(View.GONE);


		alertDialog = dialogBuilder.create();
		alertDialog.show();


        attach_gallery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setTypeAndNormalize("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST);

				alertDialog.dismiss();

            }
        });

        rl_video.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {


                            File mediaFile = new File(utility.get_HBHG_Directory() + "/My_video.mp4");

                            Log.d("TAG", "mediaFile = "+mediaFile);

                            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                            if (mediaFile != null){

								video2upload = mediaFile;

                                takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 7);
                                takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);

                                //takeVideoIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, (3*1024*1024));


								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
									takeVideoIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
									Uri contentUri = FileProvider.getUriForFile(getApplicationContext(),
											BuildConfig.APPLICATION_ID + ".provider", mediaFile);
									takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
								} else {
									takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mediaFile));
								}

								startActivityForResult(takeVideoIntent, VIDEO_CAPTURE);

                            }

                            alertDialog.dismiss();

                        }else {

                            Toast.makeText(getApplicationContext(), "No camera on device", Toast.LENGTH_LONG).show();

                        }


                    }



                });



	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == VIDEO_CAPTURE) {
			if (resultCode == RESULT_OK) {

				rl_button.setVisibility(View.GONE);

				sending_code = 4;
                if (data != null) {

					video2upload = new File(getRealPathFromURI(data.getData()));
					videoadded = true;
					getFrames(data.getData().getPath());

                } else {
					videoadded = true;
					getFrames(video2upload.getPath());
				}

            }else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(this, "Video recording cancelled",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "Failed to record video",
						Toast.LENGTH_LONG).show();
			}
        }
        else if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK){

			rl_button.setVisibility(View.GONE);

			sending_code = 4;

            Uri selectedImageUri = data.getData();

            // OI FILE Manager
            String filemanagerstring = selectedImageUri.getPath();

            Log.d("TAG", "path = "+filemanagerstring);

            Intent intent = new Intent(CreateChatRoomActivity.this, TrimmerActivity.class);
            intent.putExtra("path", FileUtils.getPath(this, selectedImageUri));
            startActivityForResult(intent, TRIMMER_ACTIVITY_RESULT_CODE);


        }
        else if (requestCode == TRIMMER_ACTIVITY_RESULT_CODE && resultCode == RESULT_OK){

			rl_button.setVisibility(View.GONE);

            String path_uri = data.getExtras().getString("uri");
            Uri myUri = Uri.parse(path_uri);


			video2upload=new File(getRealPathFromURI(myUri));
			videoadded=true;
			getFrames(path_uri);
        }
	}


	public void createChatroom(String frnds){


		progressDialog.show();


		String URL = All_Constants_Urls.addchatroom;
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();

		params.put(All_Constants_Urls.user_id, sharedPref.get_Use_Id());
		params.put(All_Constants_Urls.Token, All_Constants_Urls.TOKEN_FIXED);
		params.put(All_Constants_Urls.title,edt_name.getText().toString());
		params.put(All_Constants_Urls.tags,edt_tags.getText().toString() );
		params.put(All_Constants_Urls.content, "");
		params.put(All_Constants_Urls.invited_users, frnds);

		if (sending_code == 2){

			params.put(All_Constants_Urls.msg_type, sending_code);
			try{

				params.put(All_Constants_Urls.content_media, video_thumb2upload);

			}catch (FileNotFoundException e){
				e.printStackTrace();
			}

		}else if (sending_code == 4){
			params.put(All_Constants_Urls.msg_type, sending_code);
			try{
				params.put(All_Constants_Urls.snap_image, video_thumb2upload);
				params.put(All_Constants_Urls.video,video2upload);
			}catch (FileNotFoundException e){
				e.printStackTrace();
			}

		}

		final String TAG = All_Constants_Urls.TAG;

		//Log.d(TAG ,"AsyncHttpClient URL- " + URL);
		//Log.d(TAG ,"AsyncHttpClient PARAM - " + params.toString());


		client.setSSLSocketFactory(
				new SSLSocketFactory(Common.getSslContext(),
						SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER));

		client.setMaxRetriesAndTimeout(Common.MAXIMUM_RETRY , 40*1000);
		client.post(URL, params, new JsonHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

				//Log.d(TAG, "onSuccess- " + response.toString());

				if (response != null) {
					try {

						int success = response.optInt("success");

						if (success == 0){

							Toast.makeText(getApplicationContext(), "Something error", Toast.LENGTH_LONG).show();

							progressDialog.dismiss();

						}else{

							global_class.setIf_action_on_chatroom_create(true);

							progressDialog.dismiss();

							AlertDialog alert= new AlertDialog.Builder(CreateChatRoomActivity.this)
									.setCancelable(false)
									.setMessage("Chatroom created successfully!")
									.setPositiveButton("OK", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											dialog.cancel();
											finish();
										}
									}).show();

						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}



			@Override
			public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
				Log.d(TAG, "onFailure- " + res);

				progressDialog.dismiss();

				android.app.AlertDialog alert = new android.app.AlertDialog.Builder(CreateChatRoomActivity.this).create();
				alert.setMessage("Server Error");
				alert.show();
			}


		});


	}

	private String getRealPathFromURI(Uri contentURI) {
		String result = "";
		try {
			Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
			if (cursor == null) { // Source is Dropbox or other similar local file path
				result = contentURI.getPath();
			} else {
				cursor.moveToFirst();
				int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
				result = cursor.getString(idx); // Exception raised HERE
				cursor.close(); }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public void getFrames(String path){
		try {

			MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

			mediaMetadataRetriever.setDataSource(path);
			Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(1000); //unit in microsecond

			img1.setImageBitmap(bmFrame);


			try {

				// imgMain.setImageBitmap(bmp);
				File f = new File(utility.get_HBHG_Directory() + "video_thumb" + ".jpg");
				FileOutputStream fos = new FileOutputStream(f);
				bmFrame.compress(Bitmap.CompressFormat.JPEG, 90, fos);

				video_thumb2upload = f;

			} catch (Exception ex) {
				ex.printStackTrace();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onDestroy() {
		freeMemory();
		super.onDestroy();
	}

	public void freeMemory(){
		System.runFinalization();
		Runtime.getRuntime().gc();
		System.gc();
	}

}
