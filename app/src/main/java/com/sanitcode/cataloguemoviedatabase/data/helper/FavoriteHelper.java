package com.sanitcode.cataloguemoviedatabase.data.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sanitcode.cataloguemoviedatabase.data.local.FavoriteColumn;

public class FavoriteHelper {

    private static String TABLE_NAME = FavoriteColumn.TABLE_MOVIE;
    private Context context;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqliteDatabase;

    public FavoriteHelper(Context context) {
        this.context = context;
    }

    public FavoriteHelper open() throws SQLException {
        databaseHelper = new DatabaseHelper(context);
        sqliteDatabase = databaseHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        sqliteDatabase.close();
    }

    public Cursor queryProvider() {
        return sqliteDatabase.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                FavoriteColumn._ID + " DESC"
        );
    }

    public Cursor queryByIdProvider(String id) {
        return sqliteDatabase.query(TABLE_NAME, null
                , FavoriteColumn.FAVORITE_ID + " = ?"
                , new String[]{id}
                , null
                , null
                , null
                , null);
    }

    public long insertProvider(ContentValues values) {
        return sqliteDatabase.insert(
                TABLE_NAME,
                null,
                values);
    }

    public int updateProvider(String id, ContentValues values) {
        return sqliteDatabase.update(
                TABLE_NAME,
                values,
                FavoriteColumn._ID + " = ?",
                new String[]{id});
    }

    public int deleteProvider(String id) {
        return sqliteDatabase.delete(TABLE_NAME,
                FavoriteColumn.FAVORITE_ID + " = ?",
                new String[]{id});
    }
}
