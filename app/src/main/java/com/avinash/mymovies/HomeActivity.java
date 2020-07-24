package com.avinash.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.avinash.mymovies.actions.StartNewActivityAction;
import com.avinash.mymovies.adapters.BookmarkListAdapter;
import com.avinash.mymovies.adapters.SearchResultsAdapter;
import com.avinash.mymovies.controller.SearchController;
import com.avinash.mymovies.database.DBAdapter;
import com.avinash.mymovies.database.model.Bookmark;
import com.avinash.mymovies.model.SearchResultContainer;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    // Views

    SearchView mMovieSearchView;
    RecyclerView mMovieResultRecyclerView, mBookmarksRecyclerView;
    TextView textView;

    //Controllers

    private SearchController mSearchController;
    private SearchResultsAdapter mSearchResultsAdapter;
    private BookmarkListAdapter mBookmarkListAdapter;

    //Page count
    private int mPageCount = 1;
    private String mSearchCriteria;
    ArrayList<Bookmark> bookmarks = new ArrayList<>();
    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mMovieSearchView = findViewById(R.id.movie_search_view);
        mMovieResultRecyclerView = findViewById(R.id.movie_search_result_view);
        mBookmarksRecyclerView = findViewById(R.id.movie_bookmark_view);
        textView = findViewById(R.id.message);

        mSearchController = new SearchController();
        mSearchResultsAdapter = new SearchResultsAdapter(new StartNewActivityAction() {
            @Override
            public void startNewActivityFromAdapter(Intent intent) {
                startActivity(intent);
            }
        });


        initializeViews();
    }

    private void initializeViews() {

        getBookMarks(null);
        // Adding listener
        mMovieSearchView.setOnQueryTextListener(new OnMovieSearchedListener());

        // Adding adapter to recyclerview
        mMovieResultRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        mMovieResultRecyclerView.setAdapter(mSearchResultsAdapter);

        // Adding infinite scroll listener
        mMovieResultRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    mPageCount++;
                    searchMoviesByPage(mPageCount);
                }
            }
        });

    }

    private void searchMoviesByPage(int pageNumber) {
        mSearchController.searchMoviesByName(mSearchCriteria, getApplicationContext().getString(R.string.movie), String.valueOf(pageNumber), new Callback<SearchResultContainer>() {
            @Override
            public void onResponse(Call<SearchResultContainer> call, Response<SearchResultContainer> response) {
                SearchResultContainer searchResultContainer = response.body();
                if (null != searchResultContainer && null != searchResultContainer.getSearchResults()) {
                    mSearchResultsAdapter.addSearchResults(searchResultContainer.getSearchResults());
                }
            }

            @Override
            public void onFailure(Call<SearchResultContainer> call, Throwable t) {
                Toast.makeText(HomeActivity.this, R.string.movie_search_error_1, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (null != imm) {
            imm.hideSoftInputFromWindow(mMovieSearchView.getWindowToken(), 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBookMarks(null);
        mMovieSearchView.setQuery("", false);
        mMovieSearchView.clearFocus();
    }

    private void getBookMarks(String searchTerm) {
        try {
            bookmarks.clear();
            Log.e("Size", String.valueOf(bookmarks.size()));
            DBAdapter db = new DBAdapter(this);

            db.openDB();

            Cursor c = db.retrieve(searchTerm);

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

            Log.e("Size", String.valueOf(bookmarks.size()));
            if (bookmarks.size() > 0) {
                textView.setVisibility(View.GONE);
                mBookmarksRecyclerView.setVisibility(View.VISIBLE);
                mMovieResultRecyclerView.setVisibility(View.GONE);

                arrayList = new ArrayList<>();
                for (int i = 0; i < bookmarks.size(); i++) {
                    HashMap<String, String> myObjectAsDict = new HashMap<>();
                    myObjectAsDict.put("imdbID", bookmarks.get(i).getImdbId());
                    myObjectAsDict.put("Title", bookmarks.get(i).getTitle());
                    myObjectAsDict.put("Poster", bookmarks.get(i).getPoster());
                    myObjectAsDict.put("Type", bookmarks.get(i).getType());
                    myObjectAsDict.put("Year", String.valueOf(bookmarks.get(i).getYear()));
                    arrayList.add(myObjectAsDict);
                }

                mBookmarkListAdapter = new BookmarkListAdapter(new StartNewActivityAction() {
                    @Override
                    public void startNewActivityFromAdapter(Intent intent) {
                        startActivity(intent);
                    }
                }, arrayList);

                mBookmarksRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                mBookmarksRecyclerView.setAdapter(mBookmarkListAdapter);
            } else {
                textView.setVisibility(View.VISIBLE);
                mBookmarksRecyclerView.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            //textView.setVisibility(View.VISIBLE);
            Log.e("Error", e.toString());
        }

    }

    //Private class to implement movie search functionality
    private class OnMovieSearchedListener implements SearchView.OnQueryTextListener {
        @Override
        public boolean onQueryTextSubmit(String searchedTerm) {
            // Checking if search criteria has changed
            mBookmarksRecyclerView.setVisibility(View.GONE);
            textView.setVisibility(View.GONE);
            mMovieResultRecyclerView.setVisibility(View.VISIBLE);
            mSearchResultsAdapter.clearList();
            mSearchCriteria = searchedTerm.trim();
            mPageCount = 1;
            searchMoviesByPage(mPageCount);
            hideKeyboard();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            textView.setVisibility(View.GONE);
            getBookMarks(s);
            if (s.length() == 0) {
                mSearchResultsAdapter.clearList();


            } else {
                mMovieResultRecyclerView.setVisibility(View.GONE);
                //mBookmarksRecyclerView.setVisibility(View.GONE);
            }

            return false;
        }


    }
}