package com.example.elsa;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MyHolder {

    TextView nameTxt, eventTxt;
    ImageView img;
    public MyHolder(View itemView) {


        nameTxt= (TextView) itemView.findViewById(R.id.editTextName);
        eventTxt= (TextView) itemView.findViewById(R.id.editText_post);
        img=(ImageView) itemView.findViewById(R.id.image_view);
    }
}
