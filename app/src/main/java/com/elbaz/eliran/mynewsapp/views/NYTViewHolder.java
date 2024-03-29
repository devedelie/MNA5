package com.elbaz.eliran.mynewsapp.views;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.elbaz.eliran.mynewsapp.models.Constants;
import com.elbaz.eliran.mynewsapp.models.MostPopularModels.ResultMostPopular;
import com.elbaz.eliran.mynewsapp.models.SearchModels.Doc;
import com.elbaz.eliran.mynewsapp.models.TopStoriesModels.Result;
import com.elbaz.eliran.mynewsapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;

import static android.content.ContentValues.TAG;

/**
 * Created by Eliran Elbaz on 18-Jul-19.
 */
public class NYTViewHolder extends RecyclerView.ViewHolder {
    String separator;

    @BindView(R.id.item_content_title) TextView item_content_title;
    @BindView(R.id.item_section) TextView item_section;
    @BindView(R.id.item_subsection) TextView item_subsection;
    @BindView(R.id.item_date) TextView item_date;
    @BindView(R.id.image_placeholder) ImageView mImageView;

    public NYTViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    /**
     * Methods to update recyclerView item with titles views, text, date and image
     */
    public void updateTopStoriesWithTitles(Result titles, RequestManager glide){
        //Load the image of the title
        this.setImageForTopStories(titles, glide);
        // get news title
        this.item_content_title.setText(titles.getTitle());
        // get news section of origin
        this.item_section.setText(titles.getSection());
        // Set separator(>) if subsection is available
        needSeparator(titles.getSubsection().isEmpty() || titles.getSubsection()== null);
//        Log.d(TAG, "updateTopStoriesWithTitles_subsection: " + titles.getSubsection());
        // get news sub-section
        this.item_subsection.setText(separator + titles.getSubsection());
        // get published date from the title, then convert the format and setText
        String fullDate = titles.getPublishedDate();
        String shortDate = convertDate(fullDate);
        this.item_date.setText(shortDate);
    }

    public void updateMostPopularWithTitles(ResultMostPopular titles, RequestManager glide){
        //Load the image of the title
        this.setImageForMostPopular(titles, glide);
        // get news title
        this.item_content_title.setText(titles.getTitle());
        // get news section of origin
        this.item_section.setText(titles.getSection());
        String fullDate = titles.getPublishedDate();
        String shortDate = convertDate(fullDate);
        this.item_date.setText(shortDate);
    }

    public void updateSearchResultsWithTitles(Doc titles, RequestManager glide){
                //Load the image of the title
        this.setImageForSearchResults(titles, glide);
        // get news title
        this.item_content_title.setText(titles.getSnippet());
        // get news section of origin
        this.item_section.setText(titles.getNewsDesk());
        //// Set separator(>) if subsection is available
        needSeparator(titles.getSectionName().isEmpty() || titles.getSectionName()== null);
        // get news sub-section
        this.item_subsection.setText(separator + titles.getSectionName());
        // get published date from the title, then convert the format and setText
        String fullDate = titles.getPubDate();
        String shortDate = convertDate(fullDate);
        this.item_date.setText(shortDate);
    }

    /**
     * Methods to set image for the UI (TopStories, Most popular and SearchResults)
     */
    private void setImageForTopStories(Result result, RequestManager glide){
        // Check if there is data in Multimedia
        if (result.getMultimedia() == null || result.getMultimedia().isEmpty()){
            // if "result.getMultimedia()" is empty || null, set an alternative logo
            mImageView.setImageResource(R.drawable.nyt_logo);
            }else{
            // Check if multimedia size is bigger than 0
            if(result.getMultimedia().size() > 0) {
                // Get 2nd image url from multimedia list result: 150X150 (possible between 0-4)
                String imageUrl = result.getMultimedia().get(1).getImageUrl();
                // Check if link starts with native or short URL
                if (imageUrl.startsWith("images")) {
                    imageUrl = "https://www.nytimes.com/" + imageUrl;
                }
                // Glide - load the image from the fetched url
                glide.load(imageUrl).apply(RequestOptions.centerCropTransform()).into(mImageView);
            }
        }
    }

    private void setImageForSearchResults(Doc doc, RequestManager glide){
        // Check if there is data in Multimedia
        if (doc.getMultimedia() == null || doc.getMultimedia().isEmpty()){
            // if "result.getMultimedia()" is empty || null, set an alternative logo
            mImageView.setImageResource(R.drawable.nyt_logo);
            }else{
            // Check if multimedia size is bigger than 0
            if(doc.getMultimedia().size() > 0) {
                // Get 2nd image url from multimedia list result: 150X150 (possible between 0-4)
                String imageUrl = doc.getMultimedia().get(0).getUrl();
                // Check if link starts with native or short URL
                if (imageUrl.startsWith("images")) {
                    imageUrl = "https://www.nytimes.com/" + imageUrl;
                }
                // Glide - load the image from the fetched url
                glide.load(imageUrl).apply(RequestOptions.centerCropTransform()).into(mImageView);
            }
        }
    }

    private void setImageForMostPopular (ResultMostPopular result, RequestManager glide){
        // Check if there is data in Multimedia
        if (result.getMedia() == null || result.getMedia().isEmpty()){
            // if "result.getMultimedia()" is empty || null, set an alternative logo
            mImageView.setImageResource(R.drawable.nyt_logo);
            }else{
            // Check if multimedia size is bigger than 0
            if(result.getMedia().size() > 0) {
                // Get 2nd image url from multimedia list result: 150X150 (possible between 0-4)
                String imageUrl = result.getMedia().get(0).getMediaMetadatumMostPopulars().get(1).getUrl();
                // Check if link starts with native or short URL
                if (imageUrl.startsWith("images")) {
                    imageUrl = "https://www.nytimes.com/" + imageUrl;
                }
                // Glide - load the image from the fetched url
                glide.load(imageUrl).apply(RequestOptions.centerCropTransform()).into(mImageView);
            }
        }
    }

    // Date format converter
    public static String convertDate(String inputDate) {
        DateFormat theDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = theDateFormat.parse(inputDate);
        } catch (ParseException parseException) {
            // Date is invalid
            Log.e(TAG, "convertDate: parse error while converting the Date" );
        } catch(Exception exception) {
            // Generic catch.
            Log.e(TAG, "convertDate: generic error while converting the Date" );
        }
        theDateFormat = new SimpleDateFormat("MMM dd, yyyy");

        return theDateFormat.format(date);
    }

    // Set separator if needed
    private void needSeparator (boolean emptySubsection){ // section separator ">"
        if (emptySubsection){ separator = ""; }
        else{ separator = Constants.SEPARATOR; }
    }
}
