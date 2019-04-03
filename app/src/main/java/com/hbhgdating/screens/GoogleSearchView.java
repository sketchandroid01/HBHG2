package com.hbhgdating.screens;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hbhgdating.R;
import com.hbhgdating.adapter.googleSearchViewAdapter;
import com.hbhgdating.utils.All_Constants_Urls;
import com.hbhgdating.utils.Common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class GoogleSearchView extends Activity {
	GridView gridView;
	TextView txtEmpty, tv_cancel, txtLoadMore;
	//SharedPref sf;

	googleSearchViewAdapter gridAdapter;
	EditText inputSearch;
	String strUrl = "", strSearchText = "";
	int startIndex = 1;
	//private final OkHttpClient client = new OkHttpClient();
	RelativeLayout relLoadMore;
	URL url;
	ArrayList<String> google_search_val = new ArrayList<>();

	Dialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		//sf = new SharedPref(this);
		setContentView(R.layout.googlesearchview);


		strUrl = "https://www.googleapis.com/customsearch/v1?key="
				+ Common.googleSearchAPIKey + "&cx=" + Common.googleSearchCx
				+ "&searchType=image&fileType=jpg&alt=json&q=";

		setView();

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}


	}

	private void setView() {
		inputSearch = (EditText) findViewById(R.id.inputSearch);
		tv_cancel = (TextView) findViewById(R.id.tv_cancel);
		gridView = (GridView) findViewById(R.id.gridView);
		txtEmpty = (TextView) findViewById(R.id.empty);
		txtLoadMore = (TextView) findViewById(R.id.txtLoadMore);
		relLoadMore = (RelativeLayout) findViewById(R.id.relLoadMore);


		progressDialog = new Dialog(this, android.R.style.Theme_Translucent);
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progressDialog.getWindow().setContentView(R.layout.progressbar_pleasewait);
		progressDialog.setCancelable(false);


		gridView.setEmptyView(txtEmpty);
		gridView.setVisibility(View.GONE);
		txtEmpty.setVisibility(View.GONE);
		relLoadMore.setVisibility(View.GONE);
		tv_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				google_search_val.removeAll(google_search_val);
				inputSearch.setText("");
				if(gridAdapter!= null){
					gridAdapter.notifyDataSetChanged();
				}

				gridView.setVisibility(View.GONE);
				txtEmpty.setVisibility(View.GONE);
				relLoadMore.setVisibility(View.GONE);
			}
		});
		txtLoadMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				progressDialog.show();

				Log.d(All_Constants_Urls.TAG, "Load more");

				try {

					googleSearch();

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		inputSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
										  KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					String strText = v.getText().toString().trim();
					//	String strText1 = v.getText().toString().trim();
					if (strText.isEmpty()) {
						Common.customToast(GoogleSearchView.this, getResources().getString(
								R.string.entertext), Common.displayType.CENTER);
					} else {

						Log.d(All_Constants_Urls.TAG, "Load first");

						progressDialog.show();

						Common.hideSoftKeyboard(GoogleSearchView.this);
						strSearchText = strText;
						startIndex = 1;
						google_search_val.removeAll(google_search_val);


						try {

							googleSearch();

						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					return true;
				}
				return false;
			}
		});
	}

	private void googleSearch() throws IOException {

		String query = URLEncoder.encode(strSearchText, "utf-8");
		try {
			url = new URL("https://www.googleapis.com/customsearch/v1?key="
					+ Common.googleSearchAPIKey + "&cx="
					+ Common.googleSearchCx
					+ "&searchType=image&fileType=jpg&alt=json&q="
					+ query
					+ "&start="
					+ startIndex);

			Log.d("TAG", "url googleserch:>>>>>>>>>>>> " + url);

			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			conn.setRequestProperty("User-Agent","Mozilla/5.0 ( compatible ) ");
			conn.setRequestProperty("Accept","*/*");

			//int status = conn.getResponseCode();

			conn.setInstanceFollowRedirects(false);
			BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));


			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {


				if (output.contains("\"link\": \"")) {
					try {
						String link = output.substring(output.indexOf("\"link\": \"")
								+ ("\"link\": \"").length(), output.indexOf("\","));
						System.out.println(link);//Will print the google search links
						Log.d("TAG", "googleSearch link: " + link);


						google_search_val.add(link);
						gridAdapter = new googleSearchViewAdapter(this, google_search_val, strSearchText);
						gridView.setAdapter(gridAdapter);

						progressDialog.dismiss();



					} catch (Exception e) {

						progressDialog.dismiss();
						final String strResonse = e.toString();
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								Common.customToast(GoogleSearchView.this, strResonse, Common.displayType.CENTER);
							}
						});
					}


				}
			}conn.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				setData();


			}
		});


	}



	private void setData() {
		Log.d("TAG", "startIndex++++++++++ "+startIndex);
		startIndex = startIndex + 10;
		Log.d("TAG", "startIndex>>>>>>>>>>>> "+startIndex);
		if (startIndex >= 91)
			relLoadMore.setVisibility(View.GONE);
		else
			relLoadMore.setVisibility(View.VISIBLE);
		if (google_search_val.size() == 0)
			relLoadMore.setVisibility(View.GONE);

		gridView.setVisibility(View.VISIBLE);
		txtEmpty.setVisibility(View.VISIBLE);
		gridAdapter.notifyDataSetChanged();

		//progressDialog.dismiss();

	}






}
