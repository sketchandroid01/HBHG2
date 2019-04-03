package com.hbhgdating.screens;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

import com.hbhgdating.R;

/**
 * Created by Developer on 4/28/17.
 */
public class CustomDialogEdit extends Dialog{

    RelativeLayout attach_gallery;
    private Context context;

    public CustomDialogEdit(Context context) {
        super(context);

        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_custom_dialog);


        attach_gallery = (RelativeLayout) findViewById(R.id.attach_gallery);
        attach_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                final int ACTIVITY_SELECT_IMAGE = 1234;
                //(Check_Video)context.startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
            }
        });


    }








    }
