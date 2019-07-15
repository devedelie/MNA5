package com.elbaz.eliran.mynewsapp.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.elbaz.eliran.mynewsapp.Models.NYTNews;
import com.elbaz.eliran.mynewsapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Eliran Elbaz on 14-Jul-19.
 */
public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NYTNews> mNYTNewsList;

    // Constructor
    public NewsRecyclerViewAdapter(List<NYTNews> mNYTNews){
        this.mNYTNewsList = mNYTNews;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item, viewGroup, false);
        return new NewsItem(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        NYTNews nytNews = mNYTNewsList.get(i);
        //Get data
        ((NewsItem) viewHolder).title.setText(nytNews.getTitle());
        ((NewsItem) viewHolder).date.setText(nytNews.getPublishedDate());
        ((NewsItem) viewHolder).continent.setText(nytNews.getSection());
        ((NewsItem) viewHolder).continent.setText(nytNews.getSection());
        ((NewsItem) viewHolder).country.setText(nytNews.getSubsection());
        // Use Picasso for loading the image
        Picasso.get()
                .load(nytNews.getImageUrl())
                .resize(200,200)
                .centerCrop()
                .into(((NewsItem) viewHolder).image);

    }

    @Override
    public int getItemCount() {
        return mNYTNewsList.size();
    }


    public class NewsItem extends RecyclerView.ViewHolder {

        private TextView continent, country, date, title;
        ImageView image;

        public NewsItem(@NonNull View itemView) {
            super(itemView);
            continent = itemView.findViewById(R.id.item_continent);
            country = itemView.findViewById(R.id.item_country);
            date = itemView.findViewById(R.id.item_date);
            title = itemView.findViewById(R.id.item_content_title);
            // Check if needed...image ????? /////////////////////////////////////
            image = itemView.findViewById(R.id.image_placeholder);

        }
    }
}
