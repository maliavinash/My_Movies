package com.avinash.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.avinash.mymovies.actions.StartNewActivityAction;
import com.avinash.mymovies.adapters.BookmarkListAdapter;
import com.avinash.mymovies.controller.SearchController;
import com.avinash.mymovies.database.model.Bookmark;
import com.avinash.mymovies.database.DBAdapter;
import com.avinash.mymovies.model.MovieDetail;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

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

    private MovieDetail mMovieDetail;

    private SearchController mSearchController;
    ArrayList<Bookmark> bookmarks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

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
                    AddToBookmarkTable(mMovieDetail.getImdbId(), mMovieDetail.getTitle(), Integer.parseInt(mMovieDetail.getYear()), mMovieDetail.getType(), mMovieDetail.getPoster());
                } else {
                    v.setActivated(false);
                    RemoveBookmarkTable(mMovieDetail.getImdbId());
                    //Toast.makeText(getApplicationContext(), "Removed from bookmark!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getBookMarks(String searchTerm) {
        try {
            bookmarks.clear();
            DBAdapter db = new DBAdapter(this);

            db.openDB();
            Cursor c = db.retrievebyID(searchTerm);
            //if the cursor has some data
            if (c.moveToFirst()) {
                //looping through all the records
                do {
                    //pushing each record in the employee list
                    bookmarks.add(new Bookmark(
                            c.getString(1),
                            c.getString(2),
                            c.getInt(3),
                            c.getString(4),
                            c.getString(5)
                    ));
                } while (c.moveToNext());
            }
            db.closeDB();
            Log.e("size", String.valueOf(bookmarks.size()));
            if (bookmarks.size() > 0) {
                mMovieBookmarkImageView.setActivated(true);
            } else {
                mMovieBookmarkImageView.setActivated(false);
            }
        } catch (Exception e) {
            //textView.setVisibility(View.VISIBLE);
            Log.e("Error", e.toString());
        }

    }

    private void AddToBookmarkTable(String imdbID, String Title, int Year, String Type, String Poster) {
        DBAdapter db = new DBAdapter(this);
        db.openDB();
        if (db.add(imdbID, Title, Year, Type, Poster)) {
            Toast.makeText(getApplicationContext(), "Added to bookmark!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Unable to Add Bookmark", Toast.LENGTH_SHORT).show();
        }
        db.closeDB();
    }

    private void RemoveBookmarkTable(String ID) {
        DBAdapter db = new DBAdapter(this);
        db.openDB();
        if (db.deleteBookmarkFromID(ID)) {
            Toast.makeText(getApplicationContext(), "bookmark deleted!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Unable to Delete Bookmark", Toast.LENGTH_SHORT).show();
        }
        db.closeDB();
    }

    private void searchMovieDetailsById(String Id) {
        mSearchController.fetchMovieDetailsById(Id, new Callback<MovieDetail>() {
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

        getBookMarks(mMovieDetail.getImdbId());
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