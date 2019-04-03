package com.hbhgdating.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hbhgdating.R;
import com.hbhgdating.utils.Global_Class;

/**
 * Created by Developer on 5/12/17.
 */

public class Listen {

    public static View getView(Context context, ViewGroup collection) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);


        View view = inflater.inflate(R.layout.listen_layout, collection, false);

        Global_Class global_class = (Global_Class)context.getApplicationContext();

        TextView txtMusic1 = (TextView) view.findViewById(R.id.txtMusic1);
        TextView txtMusic2 = (TextView) view.findViewById(R.id.txtMusic2);
        TextView txtMusic3 = (TextView) view.findViewById(R.id.txtMusic3);

        if (global_class.isEdit) {


            txtMusic1.setText("Add");
            txtMusic2.setText("Add");
            txtMusic3.setText("Add");

        } else {

            txtMusic1.setText("music 1");
            txtMusic2.setText("music 2");
            txtMusic3.setText("music 3");

        }


        //return inflater.inflate(R.layout.listen_layout, collection, false);

        return view;
    }
}
