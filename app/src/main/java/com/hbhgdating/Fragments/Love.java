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

public class Love {

    public static View getView(Context context, ViewGroup collection) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);


        View view = inflater.inflate(R.layout.loves_layout, collection, false);

        Global_Class global_class = (Global_Class)context.getApplicationContext();


        TextView txtInterest1 = (TextView) view.findViewById(R.id.txtInterest1);
        TextView txtInterest2 = (TextView) view.findViewById(R.id.txtInterest2);
        TextView txtInterest3 = (TextView) view.findViewById(R.id.txtInterest3);

        if (global_class.isEdit) {


            txtInterest1.setText("Add");
            txtInterest2.setText("Add");
            txtInterest3.setText("Add");

        } else {


            txtInterest1.setText("Interest 1");
            txtInterest2.setText("Interest 2");
            txtInterest3.setText("Interest 3");

        }



       // return inflater.inflate(R.layout.loves_layout, collection, false);

        return view;
    }
}
