package com.sanitcode.cataloguemoviedatabase.ui;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.annotations.SerializedName;
import com.sanitcode.cataloguemoviedatabase.BuildConfig;
import com.sanitcode.cataloguemoviedatabase.R;
import com.sanitcode.cataloguemoviedatabase.data.local.DatabaseContract;
import com.sanitcode.cataloguemoviedatabase.data.helper.FavoriteHelper;
import com.sanitcode.cataloguemoviedatabase.data.model.ResultItems;
import com.sanitcode.cataloguemoviedatabase.data.local.FavoriteColumn;
import com.sanitcode.cataloguemoviedatabase.utils.DateTime;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

import static com.sanitcode.cataloguemoviedatabase.BuildConfig.BASE_BACKDROP_URL;

public class DetailActivity extends AppCompatActivity {
    private final static String MOVIE_DETAIL = "movie_detail";

    @BindView(R.id.overview_detail)
    TextView tvOverview;

    @BindView(R.id.backdrop_detail)
    ImageView img_backDrop;

    @BindView(R.id.releasedate_detail)
    TextView tvDate;

    @BindView(R.id.title_detail)
    TextView tvTitle;

    @BindView(R.id.poster_detail)
    ImageView img_poster;

    @BindView(R.id.rating_detail)
    TextView tvRating;

    @BindView(R.id.voteCount_detail)
    TextView voteCount;

    @BindView(R.id.tpopularity_detail)
    TextView popularity;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @BindView(R.id.btnshare_detail)
    Button btnShare;

    @SerializedName("id")
    String id;

    @SerializedName("title")
    String title;

    @SerializedName("image")
    String image;

    private Call<ResultItems> apiCall;
    private ResultItems resultItems;
    private FavoriteHelper favoriteHelper;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.detail_movie));
        }

        resultItems = getIntent().getParcelableExtra(MOVIE_DETAIL);
        updateImage(resultItems);
        id = resultItems.getId().toString();
        title = resultItems.getTitle();
        image = resultItems.getBackdropPath();

        favoriteSetIcon();
        favorite();

        btnShareMovie();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (apiCall != null) apiCall.cancel();
        if (favoriteHelper != null) favoriteHelper.close();
    }

    private void favorite() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFavorite(resultItems.getId().toString())) {
                    favotiteSave(view);
                } else {
                    favoriteDelete(view);
                }
            }
        });
    }

    private void favoriteSetIcon() {
        if (isFavorite(resultItems.getId().toString())) {
            if (floatingActionButton != null) {
                floatingActionButton.setImageResource(R.drawable.ic_favorite_black_24dp);
                Log.v("favorite", "" + resultItems.getId());
            } else {
                floatingActionButton.setImageResource(R.drawable.ic_favorite_border);
                Log.v("favorite null", "" + resultItems.getId());
            }
        }
    }

    private void favoriteDelete(View v) {
        Uri uri = DatabaseContract.CONTENT_URI;
        uri = uri.buildUpon().appendPath(id).build();
        Log.v("MovieDetail", "" + uri);

        getContentResolver().delete(uri, null, null);
        floatingActionButton.setImageResource(R.drawable.ic_favorite_border);
        Log.v("MovieDetail", uri.toString());
        Snackbar.make(v, getResources().getString(R.string.cv_delete), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void favotiteSave(View v) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteColumn.FAVORITE_ID, id);
        contentValues.put(FavoriteColumn.FAVORITE_TITLE, title);
        contentValues.put(FavoriteColumn.FAVORITE_IMAGE, image);
        getContentResolver().insert(DatabaseContract.CONTENT_URI, contentValues);
        floatingActionButton.setImageResource(R.drawable.ic_favorite_black_24dp);
        Snackbar.make(v, getResources().getString(R.string.cv_save), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


    @SuppressLint({"StringFormatInvalid", "SetTextI18n"})
    void updateImage(ResultItems movie) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(movie.getTitle());
        }

        Picasso.get()
                .load(BuildConfig.BASE_POSTER_URL + movie.getPosterPath())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(img_poster);

        Picasso.get()
                .load(BASE_BACKDROP_URL + movie.getBackdropPath())
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_image)
                .into(img_backDrop);
        tvTitle.setText(movie.getTitle());
        voteCount.setText(movie.getVoteCount());
        tvOverview.setText(movie.getOverview());
        tvDate.setText(DateTime.getDateDay(movie.getReleaseDate()));
        tvRating.setText(movie.getRating().toString());
        popularity.setText(movie.getPopularity().toString());
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isFavorite(String id) {
        String selection = " movie_id = ?";
        String[] selectionArgs = {id};
        String[] projection = {FavoriteColumn.FAVORITE_ID};
        Uri uri = DatabaseContract.CONTENT_URI;
        uri = uri.buildUpon().appendPath(id).build();

        Cursor cursor = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            cursor = getContentResolver().query(uri, projection,
                    selection, selectionArgs, null, null);
        }

        assert cursor != null;
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    private void btnShareMovie() {
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TITLE, resultItems.getTitle());
                intent.putExtra(Intent.EXTRA_SUBJECT, resultItems.getTitle());
                intent.putExtra(Intent.EXTRA_TEXT, resultItems.getTitle() + "\n\n" + resultItems.getOverview());
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.share)));
            }
        });
    }
}
