package com.avinash.mymovies.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.avinash.mymovies.database.model.Bookmark;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, Bookmark.DB_NAME, null, Bookmark.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(Bookmark.CREATE_TABLE);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Bookmark.DROP_TB);
        onCreate(db);
    }
}
