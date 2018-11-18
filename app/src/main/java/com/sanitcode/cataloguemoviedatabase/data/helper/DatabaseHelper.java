package com.sanitcode.cataloguemoviedatabase.data.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sanitcode.cataloguemoviedatabase.data.local.FavoriteColumn;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "cataloguemoviedb";
    public static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_TABLE_MOVIE = String.format("CREATE TABLE %s"
                        + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        " %s TEXT NOT NULL," +
                        " %s TEXT NOT NULL," +
                        " %s TEXT NOT NULL)",
                FavoriteColumn.TABLE_MOVIE,
                FavoriteColumn._ID,
                FavoriteColumn.FAVORITE_ID,
                FavoriteColumn.FAVORITE_TITLE,
                FavoriteColumn.FAVORITE_IMAGE
        );
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MOVIE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteColumn.TABLE_MOVIE);
        onCreate(sqLiteDatabase);
    }
}
