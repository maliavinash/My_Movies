package com.avinash.mymovies.database.model;


public class Bookmark {

    //DB
    public static final String DB_NAME = "MyMovies";
    public static final String TABLE_NAME = "bookmarks";
    public static final int DB_VERSION = 1;


    public static final String ID = "id";
    public static final String COLUMN_TITLE = "Title";
    public static final String COLUMN_IMDB_ID = "imdbID";
    public static final String COLUMN_POSTER = "Poster";
    public static final String COLUMN_TYPE = "Type";
    public static final String COLUMN_YEAR = "Year";

    private int id;
    private String imdbID;
    private String Title;
    private String Poster;
    private String Type;
    private int Year;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_IMDB_ID + " TEXT,"
                    + COLUMN_TITLE + " TEXT,"
                    + COLUMN_YEAR + " INTEGER,"
                    + COLUMN_TYPE + " TEXT,"
                    + COLUMN_POSTER + " TEXT"
                    + ")";

    //DROP  TB STMT
    public static final String DROP_TB = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public Bookmark() {
    }

    public Bookmark(String imdbID, String Title, int Year, String Type, String Poster) {
        this.imdbID = imdbID;
        this.Title = Title;
        this.Year = Year;
        this.Type = Type;
        this.Poster = Poster;
    }


    public String getImdbId() {
        return imdbID;
    }

    public void setImdbId(String imdbId) {
        this.imdbID = imdbId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        this.Title = title;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        this.Year = year;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String Poster) {
        this.Poster = Poster;
    }
}
