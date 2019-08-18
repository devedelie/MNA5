package com.elbaz.eliran.mynewsapp.models.MostPopularModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Eliran Elbaz on 20-Jul-19.
 */
public class MediumMostPopular {
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("subtype")
    @Expose
    private String subtype;
    @SerializedName("caption")
    @Expose
    private String caption;
    @SerializedName("copyright")
    @Expose
    private String copyright;
    @SerializedName("approved_for_syndication")
    @Expose
    private Integer approvedForSyndication;
    @SerializedName("media-metadata")
    @Expose
    private List<MediaMetadatumMostPopular> mMediaMetadatumMostPopulars = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public Integer getApprovedForSyndication() {
        return approvedForSyndication;
    }

    public void setApprovedForSyndication(Integer approvedForSyndication) {
        this.approvedForSyndication = approvedForSyndication;
    }

    public List<MediaMetadatumMostPopular> getMediaMetadatumMostPopulars() {
        return mMediaMetadatumMostPopulars;
    }

    public void setMediaMetadatumMostPopulars(List<MediaMetadatumMostPopular> mediaMetadatumMostPopulars) {
        this.mMediaMetadatumMostPopulars = mediaMetadatumMostPopulars;
    }

}
