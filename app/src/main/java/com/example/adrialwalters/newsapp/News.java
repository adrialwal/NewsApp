package com.example.adrialwalters.newsapp;

public class News {

    /**
     * The title of the news article
     */
    private String mTitle;

    /**
     * The section of the new article is part of
     */
    private String mSection;

    /**
     * The date the news article was published
     */
    private String mDate;

    /**
     * Website URL of the news article
     */
    private String mUrl;

    /**
     * Constructs a new {@link News} object.
     *
     * @param title   is the title of the news article
     * @param section is the section of the new article is part of
     * @param date    is the date the news article was published
     * @param url     is the website URL to find more details about the news article
     */
    public News(String title, String section, String date, String url) {
        mTitle = title;
        mSection = section;
        mDate = date;
        mUrl = url;
    }

    /**
     * Returns the title of the news article
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * Returns what section article is in
     */
    public String getSection() {
        return mSection;
    }

    /**
     * Returns the date the article was published
     */
    public String getDate() {
        return mDate;
    }

    /**
     * Returns the website url to find more information about the article
     */
    public String getUrl() {
        return mUrl;
    }
}
