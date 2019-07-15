package com.elbaz.eliran.mynewsapp.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NYTNews {

    @SerializedName("section")
    @Expose
    private String section;
    @SerializedName("subsection")
    @Expose
    private String subsection;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("abstract")
    @Expose
    private String _abstract;
    @SerializedName("url")
    @Expose
    private String pageUrl;
    @SerializedName("published_date")
    @Expose
    private String publishedDate;
    @SerializedName("short_url")
    @Expose
    private String shortUrl;

    @SerializedName("url")
    @Expose
    private String imageUrl;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("copyright")
    @Expose
    private String copyright;

    public NYTNews(String section, String subsection, String title, String _abstract, String pageUrl, String publishedDate, String shortUrl, String imageUrl, String caption, String copyright) {
        this.section = section;
        this.subsection = subsection;
        this.title = title;
        this._abstract = _abstract;
        this.pageUrl = pageUrl;
        this.publishedDate = publishedDate;
        this.shortUrl = shortUrl;
        this.imageUrl = imageUrl;
        this.caption = caption;
        this.copyright = copyright;
    }

    public String getSection() {
        return section;
    }

    public String getSubsection() {
        return subsection;
    }

    public String getTitle() {
        return title;
    }

    public String get_abstract() {
        return _abstract;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public String getImageUrl() {
        if (imageUrl.equals("") || imageUrl.isEmpty()){
            return imageUrl = null;
        }else {
            return imageUrl;
        }
    }

    public String getCaption() {
        return caption;
    }

    public String getCopyright() {
        return copyright;
    }
}