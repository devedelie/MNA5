package com.elbaz.eliran.mynewsapp.Views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.elbaz.eliran.mynewsapp.Models.MostPopularModels.ResultMostPopular;
import com.elbaz.eliran.mynewsapp.R;

import java.util.List;

/**
 * Created by Eliran Elbaz on 18-Jul-19.
 */
public class NYTAdapterMostPopular extends RecyclerView.Adapter<NYTViewHolder> {

    // DATA
    private List<ResultMostPopular> mResultMostPopulars;
    Context mContext;
    // Glide object
    private RequestManager glide;

    public NYTAdapterMostPopular(List<ResultMostPopular> results, Context context, RequestManager glide) {
        this.mResultMostPopulars = results;
        this.mContext = context;
        this.glide = glide;
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
        nytViewHolder.updateMostPopularWithTitles(this.mResultMostPopulars.get(i), this.glide);
    }

    @Override
    public int getItemCount() {
        return this.mResultMostPopulars.size();
    }

    public String getUrl(int position) {
        return mResultMostPopulars.get(position).getUrl();
    }
}
