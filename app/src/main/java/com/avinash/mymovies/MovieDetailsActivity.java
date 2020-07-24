package com.avinash.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.avinash.mymovies.controller.SearchController;
import com.avinash.mymovies.model.MovieDetail;
import com.avinash.mymovies.utils.PreferenceServices;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {

    //Views

    ImageView mMoviePosterImageView, mMovieBookmarkImageView;
    TextView mMovieTitleTextView;
    TextView mMovieDescriptionTextView;
    ProgressBar mLoader;
    TextView mMovieReleaseDateTextView;
    TextView mMovieRuntimeTextView;
    TextView mMovieGenreTextView;
    TextView mMovieLanguageTextView;
    TextView mMovieCrewDirectorTextView;
    TextView mMovieCrewWriterTextView;
    TextView mMovieCrewActorsTextView;
    TextView mMovieRatingTextView;
    RatingBar mMovieRatingBar;

    /**
     * Model
     */
    private MovieDetail mMovieDetail;

    /**
     * Controllers
     */
    private SearchController mSearchController;

    private ArrayList<HashMap<String, String>> bookmarkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        PreferenceServices.init(this);
        bookmarkList = new ArrayList<>();
        mMoviePosterImageView = findViewById(R.id.movie_content_poster);
        mMovieBookmarkImageView = findViewById(R.id.movie_bookmark);
        mMovieTitleTextView = findViewById(R.id.movie_content_title);
        mMovieDescriptionTextView = findViewById(R.id.movie_content_description);
        mLoader = findViewById(R.id.movie_content_progressbar);
        mMovieReleaseDateTextView = findViewById(R.id.movie_content_released_description);
        mMovieRuntimeTextView = findViewById(R.id.movie_content_runtime_description);
        mMovieGenreTextView = findViewById(R.id.movie_content_genre_description);
        mMovieLanguageTextView = findViewById(R.id.movie_content_language_description);
        mMovieCrewDirectorTextView = findViewById(R.id.movie_crew_director_description);
        mMovieCrewWriterTextView = findViewById(R.id.movie_crew_writers_description);
        mMovieCrewActorsTextView = findViewById(R.id.movie_crew_actors_description);
        mMovieRatingTextView = findViewById(R.id.movie_content_rating_description);
        mMovieRatingBar = findViewById(R.id.movie_content_rating);

        mSearchController = new SearchController();

        if (null != getIntent() && null != getIntent().getStringExtra(getString(R.string.intent_imdbid))) {
            searchMovieDetailsById(getIntent().getStringExtra(getString(R.string.intent_imdbid)));
        } else {
            Toast.makeText(MovieDetailsActivity.this, R.string.movie_search_error_2, Toast.LENGTH_LONG).show();
        }


        //mMovieBookmarkImageView.setImageDrawable(getResources().getDrawable(R.drawable.unbookmark));
        //mMovieBookmarkImageView.setImageResource(R.drawable.unbookmark);





        mMovieBookmarkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!v.isActivated()) {
                    v.setActivated(true);

                    HashMap<String, String> map = new HashMap<>();
                    map.put("Title", mMovieDetail.getTitle());
                    map.put("Year", mMovieDetail.getYear());
                    map.put("imdbID", mMovieDetail.getImdbId());
                    map.put("Type", mMovieDetail.getType());
                    map.put("Poster", mMovieDetail.getPoster());

                    bookmarkList.add(map);

                    PreferenceServices.getInstance().setBookmarllist(bookmarkList);
                    Toast.makeText(getApplicationContext(), "Added to bookmark!", Toast.LENGTH_SHORT).show();
                } else {
                    v.setActivated(false);

                    Toast.makeText(getApplicationContext(), "Removed from bookmark!", Toast.LENGTH_SHORT).show();

                    for (int i = 0; i < bookmarkList.size(); i++) {
                        HashMap<String, String> map = bookmarkList.get(i);
                        if (map.get("Title").equals(mMovieDetail.getTitle())) {
                            bookmarkList.remove(map);
                            PreferenceServices.getInstance().clearAllPreferance();
                            PreferenceServices.getInstance().setBookmarllist(bookmarkList);
                        }
                    }

                }

            }
        });

    }


    private void getBookmarkList() {
        try {
            bookmarkList = PreferenceServices.getInstance().getBookmarllist();

            if (bookmarkList.size() > 0) {
                for (int i = 0; i < bookmarkList.size(); i++) {
                    HashMap<String, String> map = bookmarkList.get(i);
                    if (map.get("Title").equals(mMovieDetail.getTitle())) {
                        mMovieBookmarkImageView.setActivated(true);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("Error", e.toString());
        }
    }

    private void searchMovieDetailsById(String omdbId) {
        mSearchController.fetchMovieDetailsById(omdbId, new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                MovieDetail MovieDetail = response.body();
                if (null != MovieDetail) {
                    mMovieDetail = MovieDetail;
                    handleMovieDetailsLoaded();
                } else {
                    Toast.makeText(MovieDetailsActivity.this, R.string.movie_search_error_2, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                // TODO : Add logging
                Toast.makeText(MovieDetailsActivity.this, R.string.movie_search_error_2, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void handleMovieDetailsLoaded() {
        mLoader.setVisibility(View.GONE);
        mMovieTitleTextView.setText(new StringBuilder().append(mMovieDetail.getTitle()).append(getString(R.string.open_round_bracket)).append(mMovieDetail.getYear()).append(getString(R.string.close_round_bracket)));
        mMovieDescriptionTextView.setText(mMovieDetail.getPlot());
        mMovieReleaseDateTextView.setText(mMovieDetail.getReleased());
        mMovieRuntimeTextView.setText(mMovieDetail.getRuntime());
        mMovieGenreTextView.setText(mMovieDetail.getGenre());
        mMovieLanguageTextView.setText(mMovieDetail.getLanguage());
        mMovieCrewDirectorTextView.setText(mMovieDetail.getDirector());
        mMovieCrewWriterTextView.setText(mMovieDetail.getWriter());
        mMovieCrewActorsTextView.setText(mMovieDetail.getActors());
        mMovieRatingBar.setRating(mMovieDetail.getImdbRating());
        mMovieRatingTextView.setText(new StringBuilder().append(mMovieDetail.getImdbRating()).append(getString(R.string.forward_slash)).append(getString(R.string.ten)));
        mMovieRatingBar.setIsIndicator(true);
        Picasso.get().cancelRequest(mMoviePosterImageView);
        Picasso.get().load(mMovieDetail.getPoster()).into(mMoviePosterImageView);

        getBookmarkList();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}