package com.avinash.mymovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class MovieDetail {

    public static final String IMDBID = "imdbID";
    @SerializedName(IMDBID)
    private String imdbId;

    public static final String TITLE = "Title";
    @SerializedName(TITLE)
    private String title;

    public static final String YEAR = "Year";
    @SerializedName(YEAR)
    private String year;

    public static final String TYPE = "Type";
    @SerializedName(TYPE)
    private String type;

    public static final String POSTER = "Poster";
    @SerializedName(POSTER)
    private String poster;

    public static final String RATED = "Rated";
    @SerializedName(RATED)
    private String rated;

    public static final String RELEASED = "Released";
    @SerializedName(RELEASED)
    private String released;

    public static final String RUNTIME = "Runtime";
    @SerializedName(RUNTIME)
    private String runtime;

    public static final String GENRE = "Genre";
    @SerializedName(GENRE)
    private String genre;

    public static final String DIRECTOR = "Director";
    @SerializedName(DIRECTOR)
    private String director;

    public static final String WRITER = "Writer";
    @SerializedName(WRITER)
    private String writer;

    public static final String ACTORS = "Actors";
    @SerializedName(ACTORS)
    private String actors;

    public static final String PLOT = "Plot";
    @SerializedName(PLOT)
    private String plot;

    public static final String LANGUAGE = "Language";
    @SerializedName(LANGUAGE)
    private String language;

    public static final String COUNTRY = "Country";
    @SerializedName(COUNTRY)
    private String country;

    public static final String IMDB_RATING = "imdbRating";
    @SerializedName(IMDB_RATING)
    private Float imdbRating;

    public static final String RESPONSE = "Response";
    @SerializedName(RESPONSE)
    private String response;

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getRated() {
        return rated;
    }

    public void setRated(String rated) {
        this.rated = rated;
    }

    public String getReleased() {
        return released;
    }

    public void setReleased(String released) {
        this.released = released;
    }

    public String getRuntime() {
        return runtime;
    }

    public void setRuntime(String runtime) {
        this.runtime = runtime;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Float getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(Float imdbRating) {
        this.imdbRating = imdbRating;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public MovieDetail() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MovieDetail)) return false;
        MovieDetail that = (MovieDetail) o;
        return Objects.equals(getImdbId(), that.getImdbId()) &&
                Objects.equals(getTitle(), that.getTitle()) &&
                Objects.equals(getYear(), that.getYear()) &&
                Objects.equals(getType(), that.getType()) &&
                Objects.equals(getPoster(), that.getPoster()) &&
                Objects.equals(getRated(), that.getRated()) &&
                Objects.equals(getReleased(), that.getReleased()) &&
                Objects.equals(getRuntime(), that.getRuntime()) &&
                Objects.equals(getGenre(), that.getGenre()) &&
                Objects.equals(getDirector(), that.getDirector()) &&
                Objects.equals(getWriter(), that.getWriter()) &&
                Objects.equals(getActors(), that.getActors()) &&
                Objects.equals(getPlot(), that.getPlot()) &&
                Objects.equals(getLanguage(), that.getLanguage()) &&
                Objects.equals(getCountry(), that.getCountry()) &&
                Objects.equals(getImdbRating(), that.getImdbRating()) &&
                Objects.equals(getResponse(), that.getResponse());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getImdbId(), getTitle(), getYear(), getType(), getPoster(), getRated(), getReleased(), getRuntime(), getGenre(), getDirector(), getWriter(), getActors(), getPlot(), getLanguage(), getCountry(), getImdbRating(), getResponse());
    }

    @Override
    public String toString() {
        return "OMDBMovieDetail{" +
                "imdbId='" + imdbId + '\'' +
                ", title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", type='" + type + '\'' +
                ", poster='" + poster + '\'' +
                ", rated='" + rated + '\'' +
                ", released='" + released + '\'' +
                ", runtime='" + runtime + '\'' +
                ", genre='" + genre + '\'' +
                ", director='" + director + '\'' +
                ", writer='" + writer + '\'' +
                ", actors='" + actors + '\'' +
                ", plot='" + plot + '\'' +
                ", language='" + language + '\'' +
                ", country='" + country + '\'' +
                ", imdbRating=" + imdbRating +
                ", response='" + response + '\'' +
                '}';
    }
}
