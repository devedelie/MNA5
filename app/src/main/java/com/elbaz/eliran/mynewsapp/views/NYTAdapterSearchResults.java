package com.elbaz.eliran.mynewsapp.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.elbaz.eliran.mynewsapp.models.SearchModels.Doc;
import com.elbaz.eliran.mynewsapp.models.SearchModels.Response;
import com.elbaz.eliran.mynewsapp.R;

import java.util.List;

/**
 * Created by Eliran Elbaz on 01-Aug-19.
 */
public class NYTAdapterSearchResults extends RecyclerView.Adapter<NYTViewHolder> {

    //Data
    private List<Response> mResponses;
    private List<Doc> mDocs;
    Context mContext;
    // Glide object
    private RequestManager glide;

    public NYTAdapterSearchResults(List<Doc> docs, Context context, RequestManager glide) {
        mDocs = docs;
        mContext = context;
        this.glide = glide;
    }

    @NonNull
    @Override
    public NYTViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.recyclerview_item, viewGroup, false);

        return new NYTViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NYTViewHolder nytViewHolder, int i) {
        nytViewHolder.updateSearchResultsWithTitles(this.mDocs.get(i), this.glide);
    }

    @Override
    public int getItemCount() {
        return this.mDocs.size();
    }

    /**
     * A method to receive the correct title URL from its current position on the list
     */
    public String getUrl(int position) {
        return mDocs.get(position).getWebUrl();
    }
}
