package com.example.elsa;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;


public class PicassoClient {

    public  static  void downloadimg(Context c, String url, ImageView img)
    {
        if (url!=null && url.length()>0)
        {
            Picasso.get().load(url).placeholder(R.drawable.ic_person_black_24dp).into(img);

        }else
        {
            Picasso.get().load(R.drawable.ic_person_black_24dp).into(img);
        }
    }


}
