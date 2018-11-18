package com.sanitcode.cataloguemoviedatabase.data.local;

import android.database.Cursor;
import android.provider.BaseColumns;

public class FavoriteColumn implements BaseColumns {

    public static final String TABLE_MOVIE = "movie";
    public static String FAVORITE_ID = "movie_id";
    public static String FAVORITE_TITLE = "title";
    public static String FAVORITE_IMAGE = "image";

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }
}
