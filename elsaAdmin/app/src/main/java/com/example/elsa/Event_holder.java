package com.example.elsa;

import android.graphics.Bitmap;

public class Event_holder {
    String title;
    String event_details;
    Bitmap image;

    public Event_holder(Bitmap image, String title, String event_details) {
        this.title = title;
        this.event_details = event_details;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEvent_details() {
        return event_details;
    }

    public void setEvent_details(String event_details) {
        this.event_details = event_details;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
