package com.hbhgdating.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.hbhgdating.R;
import com.hbhgdating.utils.Common;
import com.hbhgdating.utils.SharedPref;

import java.util.ArrayList;

public class StickersView extends Activity {
	GridView gridView;
	SharedPref sf;
	ArrayList<String> stickersName;
	StickersGridViewAdapter gridAdapter;
	ImageView imgFree1, imgPaid1, imgPaid2, imgPaid3, imgPaid4;
	public BillingProcessor bp_main;
	public boolean readyToPurchase = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(LayoutParams.FLAG_KEEP_SCREEN_ON);
		setBilling();
		setContentView(R.layout.stickers);
		sf = new SharedPref(this);
		setView();
		selectTAB(1);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (bp_main != null)
			bp_main.release();
		super.onDestroy();
	}

	private void setView() {
		gridView = (GridView) findViewById(R.id.gridView);
		imgFree1 = (ImageView) findViewById(R.id.imgFree1);
		imgPaid1 = (ImageView) findViewById(R.id.imgPaid1);
		imgPaid2 = (ImageView) findViewById(R.id.imgPaid2);
		imgPaid3 = (ImageView) findViewById(R.id.imgPaid3);
		imgPaid4 = (ImageView) findViewById(R.id.imgPaid4);
		
		imgFree1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectTAB(1);
			}
		});
		imgPaid1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectTAB(2);
			}
		});
		imgPaid2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectTAB(3);
			}
		});
		imgPaid3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectTAB(4);
			}
		});
		imgPaid4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectTAB(5);
			}
		});
	}

	private void selectTAB(int tabposition) {
		imgFree1.setImageResource(R.drawable.sticker_icon_free1_deselected);
		imgPaid1.setImageResource(R.drawable.sticker_icon_paid1_deselected);
		imgPaid2.setImageResource(R.drawable.sticker_icon_paid2_deselected);
		imgPaid3.setImageResource(R.drawable.sticker_icon_paid3_deselected);
		imgPaid4.setImageResource(R.drawable.sticker_icon_paid4_deselected);
		stickersName = new ArrayList<String>();
		int count = 0;
		String strNameFirst = "", strNameLast = "";
		switch (tabposition) {
		case 1:
			imgFree1.setImageResource(R.drawable.sticker_icon_free1_selected);
			count = 14;
			strNameFirst = "sticker_";
			strNameLast = "_free1";
			break;
		case 2:
			imgPaid1.setImageResource(R.drawable.sticker_icon_paid1_selected);
			count = 26;
			strNameFirst = "sticker_";
			strNameLast = "_paid1";
			break;
		case 3:
			imgPaid2.setImageResource(R.drawable.sticker_icon_paid2_selected);
			count = 20;
			strNameFirst = "sticker_";
			strNameLast = "_paid2";
			break;
		case 4:
			imgPaid3.setImageResource(R.drawable.sticker_icon_paid3_selected);
			count = 20;
			strNameFirst = "sticker_";
			strNameLast = "_paid3";
			break;
		case 5:
			imgPaid4.setImageResource(R.drawable.sticker_icon_paid4_selected);
			count = 20;
			strNameFirst = "sticker_";
			strNameLast = "_paid4";
			break;
		}
		for (int i = 1; i <= count; i++) {
			stickersName.add(strNameFirst + i + strNameLast);
		}
		setData(tabposition);
	}

	private void setData(int Type) {
		gridAdapter = new StickersGridViewAdapter(StickersView.this, stickersName, Type);
		gridView.setAdapter(gridAdapter);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (!bp_main.handleActivityResult(requestCode, resultCode, data))
			super.onActivityResult(requestCode, resultCode, data);
	}

	// in -app billing start code here

	private void setBilling() {
		bp_main = new BillingProcessor(this, Common.LICENSE_KEY,
				new BillingProcessor.IBillingHandler() {
					@Override
					public void onProductPurchased(String productId,
							TransactionDetails details) {
						PurchasedProducts(productId);
					}

					@Override
					public void onBillingError(int errorCode, Throwable error) {
						// showToast("onBillingError: "
						// + Integer.toString(errorCode));
					}

					@Override
					public void onBillingInitialized() {
						// showToast("onBillingInitialized");
						readyToPurchase = true;
						// updateTextViews();
					}

					@Override
					public void onPurchaseHistoryRestored() {
						// showToast("onPurchaseHistoryRestored");
						for (String sku : bp_main.listOwnedProducts()) {
							PurchasedProducts(sku);
						}
						// for (String sku : bp.listOwnedSubscriptions())
						// Log.e(LOG_TAG, "Owned Subscription: " + sku);
					}
				});
	}

	private void PurchasedProducts(String productId) {
		if (productId.equalsIgnoreCase(getString(R.string.inapp_stickerpack1))) {
			sf.saveBoolPref("stickerpack1", true);
		} else if (productId
				.equalsIgnoreCase(getString(R.string.inapp_stickerpack2))) {
			sf.saveBoolPref("stickerpack2", true);
		}else if (productId
				.equalsIgnoreCase(getString(R.string.inapp_stickerpack3))) {
			sf.saveBoolPref("stickerpack3", true);
		}else if (productId
				.equalsIgnoreCase(getString(R.string.inapp_stickerpack4))) {
			sf.saveBoolPref("stickerpack4", true);
		}
	}
}
