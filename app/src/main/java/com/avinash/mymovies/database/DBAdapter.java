package com.avinash.mymovies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.avinash.mymovies.database.model.Bookmark;

public class DBAdapter {
    Context c;
    SQLiteDatabase db;
    DBHelper helper;

    public DBAdapter(Context c) {
        this.c = c;
        helper = new DBHelper(c);
    }

    //OPEN DB
    public void openDB() {
        try {
            db = helper.getWritableDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //CLOSE
    public void closeDB() {
        try {
            helper.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //SAVE OR INSERT
    public boolean add(String imdbID, String title, int year, String type, String poster) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(Bookmark.COLUMN_IMDB_ID, imdbID);
            cv.put(Bookmark.COLUMN_TITLE, title);
            cv.put(Bookmark.COLUMN_YEAR, year);
            cv.put(Bookmark.COLUMN_TYPE, type);
            cv.put(Bookmark.COLUMN_POSTER, poster);


            db.insert(Bookmark.TABLE_NAME, null, cv);

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    //RETRIEVE OR FILTERING
    public Cursor retrieve(String searchTerm) {
        String[] columns = {Bookmark.ID, Bookmark.COLUMN_IMDB_ID, Bookmark.COLUMN_TITLE, Bookmark.COLUMN_YEAR, Bookmark.COLUMN_TYPE, Bookmark.COLUMN_POSTER};
        Cursor c = null;
        if (searchTerm != null && searchTerm.length() > 0) {
            String sql = "SELECT * FROM " + Bookmark.TABLE_NAME + " WHERE " + Bookmark.COLUMN_TITLE + " LIKE '%" + searchTerm + "%'";
            c = db.rawQuery(sql, null);
            return c;
        }

        c = db.query(Bookmark.TABLE_NAME, columns, null, null, null, null, null);
        return c;
    }

    public Cursor retrievebyID(String searchTerm) {
        String[] columns = {Bookmark.ID, Bookmark.COLUMN_IMDB_ID, Bookmark.COLUMN_TITLE, Bookmark.COLUMN_YEAR, Bookmark.COLUMN_TYPE, Bookmark.COLUMN_POSTER};
        Cursor c = null;
        if (searchTerm != null && searchTerm.length() > 0) {
            String sql = "SELECT * FROM " + Bookmark.TABLE_NAME + " WHERE " + Bookmark.COLUMN_IMDB_ID + " LIKE '%" + searchTerm + "%'";
            c = db.rawQuery(sql, null);
            return c;
        }

        c = db.query(Bookmark.TABLE_NAME, columns, null, null, null, null, null);
        return c;
    }

    public boolean deleteBookmarkFromID(String id) {
        return db.delete(Bookmark.TABLE_NAME, Bookmark.COLUMN_IMDB_ID + "=?", new String[]{id}) > 0;
    }
}
