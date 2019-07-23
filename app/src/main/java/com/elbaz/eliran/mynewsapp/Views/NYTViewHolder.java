package com.elbaz.eliran.mynewsapp.Views;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.elbaz.eliran.mynewsapp.Models.TopStoriesModels.Result;
import com.elbaz.eliran.mynewsapp.Models.MostPopularModels.ResultMostPopular;
import com.elbaz.eliran.mynewsapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by Eliran Elbaz on 18-Jul-19.
 */
public class NYTViewHolder extends RecyclerView.ViewHolder {
    View mView;
    String space;

    @BindView(R.id.item_content_title) TextView item_content_title;
    @BindView(R.id.item_continent) TextView item_continent;
    @BindView(R.id.item_country) TextView item_country;
    @BindView(R.id.item_date) TextView item_date;
    @BindView(R.id.image_placeholder) ImageView mImageView;

    public NYTViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    /**
     * Update TopStories titles views with text, date and image
     */
    public void updateTopStoriesWithTitles(Result titles, RequestManager glide){
        //Load the image of the title
        this.setImageForTopStories(titles, glide);
        // get news title
        this.item_content_title.setText(titles.getTitle());
        // get news continent of origin
        if(titles.getSubsection() == "" || titles.getSubsection()== null) {
            this.item_continent.setText(titles.getSection()+ "" );
        }else{
            this.item_continent.setText(titles.getSection()+ " > " );
        }
        // get news country of origin
        this.item_country.setText(titles.getSubsection());
        // get published date from the title, then convert the format and setText
        String fullDate = titles.getPublishedDate();
        String shortDate = convertDate(fullDate);
        this.item_date.setText(shortDate);
    }

    /**
     * Update MostPopular titles views with text, date and image
     */
    public void updateMostPopularWithTitles(ResultMostPopular titles, RequestManager glide){
        //Load the image of the title
        this.setImageForMostPopular(titles, glide);
        // get news title
        this.item_content_title.setText(titles.getTitle());
        // get news continent of origin
        if(titles.getSubsection() == "" || titles.getSubsection()== null) {
            this.item_continent.setText(titles.getSection()+ "" );
        }else{
            this.item_continent.setText(titles.getSection()+ " > " );
        }
        // get news country of origin
        this.item_country.setText(titles.getSubsection());
        // get published date from the title, then convert the format and setText
        String fullDate = titles.getPublishedDate();
        String shortDate = convertDate(fullDate);
        this.item_date.setText(shortDate);
    }

    /**
     * Set image for the UI (TopStories tabs)
     */
    private void setImageForTopStories(Result result, RequestManager glide){
        // Check if there is data in Multimedia
        if (result.getMultimedia() != null && !result.getMultimedia().isEmpty()){
            // Check if multimedia size is bigger than 0
            if(result.getMultimedia().size() > 0) {
                // Get 2nd image url from multimedia list result: 150X150 (possible between 0-4)
                String imageUrl = result.getMultimedia().get(1).getImageUrl();
                // Check if link starts with native or short URL
                if (imageUrl.startsWith("images")) {
                    imageUrl = "https://www.nytimes.com" + imageUrl;
                }
                // Glide - load the image from the fetched url
                glide.load(imageUrl).apply(RequestOptions.centerCropTransform()).into(mImageView);
            }else
                // if "result.getMultimedia()" is empty || null, set an alternative logo
                mImageView.setImageResource(R.drawable.nyt_logo);

            }
        }

    /**
     * Set image for the UI (MostPopular tab)
     */
    private void setImageForMostPopular (ResultMostPopular result, RequestManager glide){
        // Check if there is data in Multimedia
        if (result.getMedia() != null && !result.getMedia().isEmpty()){
            // Check if multimedia size is bigger than 0
            if(result.getMedia().size() > 0) {
                // Get 2nd image url from multimedia list result: 150X150 (possible between 0-4)
                String imageUrl = result.getMedia().get(0).getMediaMetadatumMostPopulars().get(1).getUrl();
                // Check if link starts with native or short URL
                if (imageUrl.startsWith("images")) {
                    imageUrl = "https://www.nytimes.com" + imageUrl;
                }
                // Glide - load the image from the fetched url
                glide.load(imageUrl).apply(RequestOptions.centerCropTransform()).into(mImageView);
            }else
                // if "result.getMultimedia()" is empty || null, set an alternative logo
                mImageView.setImageResource(R.drawable.nyt_logo);

        }
    }

    /**
     * Date format converter
     */
    String convertDate(String inputDate) {
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

}
