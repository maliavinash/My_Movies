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
import com.avinash.mymovies.model.MovieDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.SearchResultElementViewHolder> {

    private List<MovieDetail> movieDetails;
    private int VIEW_TYPE_FIRST = 0;
    private int VIEW_TYPE_LAST = 1;
    private int VIEW_TYPE_MIDDLE = 2;

    private StartNewActivityAction mNewActivityAction;

    public SearchResultsAdapter(StartNewActivityAction newActivityAction) {
        this.mNewActivityAction = newActivityAction;
        this.movieDetails = new ArrayList<>(200);
    }

    @NonNull
    @Override
    public SearchResultElementViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = null;
        if (viewType == VIEW_TYPE_MIDDLE) {
            rootView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_result_view, parent, false);
        }
        else if (viewType == VIEW_TYPE_LAST) {
            rootView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_result_view_bottom, parent, false);
        }
        else {
            rootView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.search_result_view_top, parent, false);
        }
        return new SearchResultElementViewHolder(rootView);
    }

    @Override
    public int getItemViewType(int position) {
        if (position > 1 && position != movieDetails.size()-1) {
            return VIEW_TYPE_MIDDLE;
        }
        else if (position == movieDetails.size()-1) {
            return VIEW_TYPE_LAST;
        }
        else {
            return VIEW_TYPE_FIRST;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultElementViewHolder holder, int position) {

        holder.mMovieTitleTextView.setText(movieDetails.get(position).getTitle());
        holder.mMovieYearTextView.setText(movieDetails.get(position).getYear());
        Picasso.get().cancelRequest(holder.mMoviePosterImageView);
        Picasso.get().load(movieDetails.get(position).getPoster()).into(holder.mMoviePosterImageView);
    }

    @Override
    public int getItemCount() {
        return movieDetails.size();
    }

    public void addSearchResults(List<MovieDetail> searchResults) {
        this.movieDetails.addAll(searchResults);
        notifyItemInserted(movieDetails.size() - searchResults.size());
    }

    public void clearList() {
        this.movieDetails.clear();
        this.notifyDataSetChanged();
    }

    /**
     * Private class to hold view for each result
     */
    public class SearchResultElementViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mMoviePosterImageView;
        private TextView mMovieTitleTextView;
        private TextView mMovieYearTextView;

        public SearchResultElementViewHolder(View itemView) {
            super(itemView);
            this.mMovieTitleTextView = itemView.findViewById(R.id.omdb_movie_title);
            this.mMoviePosterImageView = itemView.findViewById(R.id.omdb_movie_poster);
            this.mMovieYearTextView = itemView.findViewById(R.id.omdb_movie_year);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (-1!=getAdapterPosition()) {
                Intent showMovieDetailsIntent = new Intent(view.getContext(), MovieDetailsActivity.class);
                showMovieDetailsIntent.putExtra(view.getContext().getString(R.string.intent_imdbid), movieDetails.get(getAdapterPosition()).getImdbId());
                mNewActivityAction.startNewActivityFromAdapter(showMovieDetailsIntent);
            }
        }
    }

}
