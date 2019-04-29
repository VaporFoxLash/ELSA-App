package com.example.elsa;

import android.graphics.Bitmap;

public class Event_holder {
    String region;
    String names;
    Bitmap image;

    public Event_holder(String region, String names) {
        this.region = region;
        this.names = names;
//        this.image = image;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }
}
