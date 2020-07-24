package com.avinash.mymovies.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.avinash.mymovies.MovieDetailsActivity;
import com.avinash.mymovies.R;
import com.avinash.mymovies.actions.StartNewActivityAction;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class BookmarkListAdapter extends RecyclerView.Adapter<BookmarkListAdapter.BookmarkElementViewHolder> {

    ArrayList<HashMap<String, String>> bookmarks;
    private int VIEW_TYPE_FIRST = 0;
    private int VIEW_TYPE_LAST = 1;
    private int VIEW_TYPE_MIDDLE = 2;
    private StartNewActivityAction mNewActivityAction;

    public BookmarkListAdapter(StartNewActivityAction newActivityAction, ArrayList<HashMap<String, String>> bookmarks) {
        this.mNewActivityAction = newActivityAction;
        this.bookmarks = bookmarks;
    }

    @NonNull
    @Override
    public BookmarkElementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = null;
        if (viewType == VIEW_TYPE_MIDDLE) {
            rootView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_result_view, parent, false);
        } else {
            rootView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_result_view_top, parent, false);
        }
        return new BookmarkElementViewHolder(rootView);
    }

    @Override
    public int getItemViewType(int position) {
        if (position > 1 && position != bookmarks.size() - 1) {
            return VIEW_TYPE_MIDDLE;
        } else if (position == bookmarks.size() - 1 && bookmarks.size() > 2) {
            return VIEW_TYPE_MIDDLE;
        } else {
            return VIEW_TYPE_FIRST;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkElementViewHolder holder, int position) {

        holder.mMovieTitleTextView.setText(bookmarks.get(position).get("Title"));
        holder.mMovieYearTextView.setText(bookmarks.get(position).get("Year"));
        Picasso.get().cancelRequest(holder.mMoviePosterImageView);
        Picasso.get().load(bookmarks.get(position).get("Poster")).into(holder.mMoviePosterImageView);
    }

    @Override
    public int getItemCount() {
        return bookmarks.size();
    }

    public void clearList() {
        this.bookmarks.clear();
        this.notifyDataSetChanged();
    }

    /**
     * Private class to hold view for each result
     */
    public class BookmarkElementViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mMoviePosterImageView;
        private TextView mMovieTitleTextView;
        private TextView mMovieYearTextView;

        public BookmarkElementViewHolder(View itemView) {
            super(itemView);
            this.mMovieTitleTextView = itemView.findViewById(R.id.omdb_movie_title);
            this.mMoviePosterImageView = itemView.findViewById(R.id.omdb_movie_poster);
            this.mMovieYearTextView = itemView.findViewById(R.id.omdb_movie_year);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (-1 != getAdapterPosition()) {
                Intent showMovieDetailsIntent = new Intent(view.getContext(), MovieDetailsActivity.class);
                showMovieDetailsIntent.putExtra(view.getContext().getString(R.string.intent_imdbid), bookmarks.get(getAdapterPosition()).get("imdbID"));
                mNewActivityAction.startNewActivityFromAdapter(showMovieDetailsIntent);
            }
        }
    }

}
