package com.example.elsa;

public class Upload {
    private String mName;
    private String mImageUrl;
    private String mPost;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String name, String imageUrl, String post) {
        if (name.trim().equals("")) {
            name = "No Name";
        }

        mName = name;
        mImageUrl = imageUrl;
        mPost = post;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getmPost() {
        return mPost;
    }

    public void setmPost(String mPost) {
        this.mPost = mPost;
    }
}
