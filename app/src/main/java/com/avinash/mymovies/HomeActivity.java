package com.avinash.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.avinash.mymovies.actions.StartNewActivityAction;
import com.avinash.mymovies.adapters.BookmarkListAdapter;
import com.avinash.mymovies.adapters.SearchResultsAdapter;
import com.avinash.mymovies.controller.SearchController;
import com.avinash.mymovies.model.SearchResultContainer;
import com.avinash.mymovies.utils.PreferenceServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private ArrayList<HashMap<String, String>> bookmarkList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        PreferenceServices.init(this);
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

        getBookmarks();
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
        getBookmarks();
    }

    private void getBookmarks() {
        try {
            bookmarkList = PreferenceServices.getInstance().getBookmarllist();
            if (bookmarkList.size() > 0) {
                mMovieResultRecyclerView.setVisibility(View.GONE);
                mBookmarksRecyclerView.setVisibility(View.VISIBLE);
                textView.setVisibility(View.GONE);
                mBookmarkListAdapter = new BookmarkListAdapter(new StartNewActivityAction() {
                    @Override
                    public void startNewActivityFromAdapter(Intent intent) {
                        startActivity(intent);
                    }
                }, bookmarkList);
                mBookmarksRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

                mBookmarksRecyclerView.setAdapter(mBookmarkListAdapter);
            } else {
                bookmarkList = new ArrayList<>();
                mMovieResultRecyclerView.setVisibility(View.GONE);
                mBookmarksRecyclerView.setVisibility(View.GONE);
                textView.setVisibility(View.VISIBLE);
            }

        } catch (Exception e) {
            textView.setVisibility(View.VISIBLE);
        }
    }

    //Private class to implement movie search functionality
    private class OnMovieSearchedListener implements SearchView.OnQueryTextListener {
        @Override
        public boolean onQueryTextSubmit(String searchedTerm) {
            // Checking if search criteria has changed
            mBookmarksRecyclerView.setVisibility(View.GONE);
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

            if (s.length() == 0) {
                mSearchResultsAdapter.clearList();
                getBookmarks();

            } else {
                mMovieResultRecyclerView.setVisibility(View.GONE);
                mBookmarksRecyclerView.setVisibility(View.GONE);
            }

            return false;
        }


    }
}