package com.elbaz.eliran.mynewsapp.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.elbaz.eliran.mynewsapp.Models.TopStoriesModels.Result;
import com.elbaz.eliran.mynewsapp.R;

import java.util.List;

/**
 * Created by Eliran Elbaz on 18-Jul-19.
 */
public class NYTAdapter extends RecyclerView.Adapter<NYTViewHolder> {

    // DATA
    private List<Result> mResults;
    Context mContext;
    // Glide object
    private RequestManager glide;

    public NYTAdapter(List<Result> results, Context context, RequestManager glide) {
        this.mResults = results;
        this.mContext = context;
        this.glide = glide;
    }

    /**
     *  A method which will return, based on a position, the corresponding title URL from the list.
     */
    public Result getURLInPosition(int position){
        return this.mResults.get(position);
    }

    @NonNull
    @Override
    public NYTViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recyclerview_item, viewGroup, false);

        return new NYTViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NYTViewHolder nytViewHolder, int i) {
        nytViewHolder.updateTopStoriesWithTitles(this.mResults.get(i), this.glide);

    }

    @Override
    public int getItemCount() {
        return this.mResults.size();
    }



    public String getUrl(int position) {
        return mResults.get(position).getUrl();
    }
}
