package com.example.android.newsapp;

public class News {
    private String mHeadline;
    private String mAuthor;
    private String mDate;
    private String mSection;
    private String mUrl;

    public News (String headline, String author, String date, String section, String url){
        mHeadline = headline;
        mAuthor = author;
        mDate = date;
        mSection = section;
        mUrl = url;
    }

    public String getHeadline() { return mHeadline; }

    public String getAuthor() { return mAuthor; }

    public String getDate() { return mDate; }

    public String getSection() { return mSection; }

    public String getUrl() { return mUrl; }
}
