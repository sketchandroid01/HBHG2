package com.hbhgdating.screens;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.webkit.WebView;
import android.widget.ImageView;

import com.hbhgdating.R;

public class Dialog_Terms extends Dialog implements OnClickListener {

	WebView webview1;
	ImageView imgClose;

	@SuppressLint("SetJavaScriptEnabled")
	public Dialog_Terms(Context context) {
		super(context, android.R.style.Theme_Translucent);
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().getDecorView().setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
		setContentView(R.layout.dialoag_terms);
		imgClose = (ImageView) findViewById(R.id.imgClose);
		webview1 = (WebView) findViewById(R.id.webview1);
		webview1.getSettings().setJavaScriptEnabled(true);
		webview1.getSettings().setBuiltInZoomControls(true);
		webview1.getSettings().setSupportZoom(true);
		webview1.setInitialScale(100);
		webview1.loadUrl("file:///android_asset/terms.htm");
		imgClose.setOnClickListener(this);
	}

	public void onClick(View v) {
		dismiss();
	}
}
