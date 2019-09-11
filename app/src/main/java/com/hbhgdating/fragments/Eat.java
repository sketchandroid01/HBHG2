package com.hbhgdating.fragments;

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

public class Eat {


    public static View getView(Context context, ViewGroup collection) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.eat_layout, collection, false);

        Global_Class global_class = (Global_Class)context.getApplicationContext();



        TextView txtFood1 = (TextView) view.findViewById(R.id.txtFood1);
        TextView txtFood2 = (TextView) view.findViewById(R.id.txtFood2);
        TextView txtFood3 = (TextView) view.findViewById(R.id.txtFood3);

        if (global_class.isEdit) {


            txtFood1.setText("Add");
            txtFood2.setText("Add");
            txtFood3.setText("Add");

        } else {


            txtFood1.setText("food 1");
            txtFood2.setText("food 2");
            txtFood3.setText("food 3");

        }




        // return inflater.inflate(R.layout.eat_layout, collection, false);


        return view;

    }




}
