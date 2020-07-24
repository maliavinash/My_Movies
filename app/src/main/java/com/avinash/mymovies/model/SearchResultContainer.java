package com.avinash.mymovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class SearchResultContainer {

    public static final String RESULT = "Search";
    @SerializedName(RESULT)
    private List<MovieDetail> searchResults;

    public List<MovieDetail> getSearchResults() {
        return searchResults;
    }

    public void setSearchResults(List<MovieDetail> searchResults) {
        this.searchResults = searchResults;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof SearchResultContainer)) return false;
        SearchResultContainer that = (SearchResultContainer) obj;
        return Objects.equals(getSearchResults(), that.getSearchResults());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSearchResults());
    }

    @Override
    public String toString() {
        return "SearchResultContainer{" +
                "searchResults=" + searchResults +
                '}';
    }
}
