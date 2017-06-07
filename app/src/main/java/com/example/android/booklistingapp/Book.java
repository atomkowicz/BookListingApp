package com.example.android.booklistingapp;

public class Book {

    private String mTitle;
    private String mAuthor;
    private String mPublisher;
    private String mUrl;

    public Book(String title, String author, String publisher, String url) {
        mTitle = title;
        mAuthor = author;
        mPublisher = publisher;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getPublisher() {
        return mPublisher;
    }

    public String getUrl() {return mUrl;}
}
