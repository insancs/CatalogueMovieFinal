package com.sanitcode.cataloguemoviedatabase.data.local;

import android.net.Uri;
import static com.sanitcode.cataloguemoviedatabase.data.local.FavoriteColumn.TABLE_MOVIE;

public class DatabaseContract {

    public static final String AUTHORITY = "com.sanitcode.cataloguemoviedatabase";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(TABLE_MOVIE)
            .build();
}
