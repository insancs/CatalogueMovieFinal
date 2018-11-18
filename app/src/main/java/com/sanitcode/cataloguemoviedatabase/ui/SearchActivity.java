package com.sanitcode.cataloguemoviedatabase.ui;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;


import com.sanitcode.cataloguemoviedatabase.BuildConfig;
import com.sanitcode.cataloguemoviedatabase.R;
import com.sanitcode.cataloguemoviedatabase.adapter.MovieAdapter;
import com.sanitcode.cataloguemoviedatabase.api.ApiCall;
import com.sanitcode.cataloguemoviedatabase.api.ApiClient;
import com.sanitcode.cataloguemoviedatabase.data.model.Movie;
import com.sanitcode.cataloguemoviedatabase.data.model.ResultItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sanitcode.cataloguemoviedatabase.BuildConfig.BASE_URL;

public class SearchActivity extends AppCompatActivity {
    private static final String INTENT_TAG = "tag";
    private static final String INTENT_SEARCH = "intent_search";

    @BindView(R.id.rv_search)
    RecyclerView rvSearch;

    @BindView(R.id.toolbar_search)
    Toolbar toolbarSearch;

    MovieAdapter movieAdapter;

    List<ResultItems> resultItemsList;
    ApiCall apiCall;
    Call<Movie> movieCall;
    ResultItems resultItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setSupportActionBar(toolbarSearch);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.search_title));

        resultItemsList = new ArrayList<>();
        resultItems = new ResultItems();

        if (getIntent() != null) {
            if (getIntent().getStringExtra(INTENT_TAG).equals("search")) {
                String s = getIntent().getStringExtra(INTENT_SEARCH);
                initView();
                getMovies(s);
            }
        }
    }

    void initView() {
        movieAdapter = new MovieAdapter(this);
        rvSearch.setLayoutManager(new LinearLayoutManager(this));
        rvSearch.setHasFixedSize(true);
        rvSearch.setItemAnimator(new DefaultItemAnimator());
    }

    private void getMovies(final String q) {
        apiCall = ApiClient.getClient(BASE_URL).create(ApiCall.class);
        movieCall = apiCall.getMovieBySearch(q, BuildConfig.APIKEY);
        movieCall.enqueue(new Callback<Movie>() {
            @SuppressLint("StringFormatInvalid")
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.body() != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        resultItemsList = Objects.requireNonNull(response.body()).getResults();
                        Objects.requireNonNull(getSupportActionBar()).setSubtitle(getString(R.string.keywords,
                                Objects.requireNonNull(response.body())
                                        .getTotalResults().toString(), q));
                    }
                }
                movieAdapter.setMovieResult(resultItemsList);
                rvSearch.setAdapter(movieAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                Toast.makeText(SearchActivity.this, getResources().getString(R.string.wrong_search)
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

}

