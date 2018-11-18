package com.sanitcode.cataloguemoviedatabase.ui;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sanitcode.cataloguemoviedatabase.BuildConfig;
import com.sanitcode.cataloguemoviedatabase.R;
import com.sanitcode.cataloguemoviedatabase.adapter.MovieAdapter;
import com.sanitcode.cataloguemoviedatabase.api.ApiCall;
import com.sanitcode.cataloguemoviedatabase.api.ApiClient;
import com.sanitcode.cataloguemoviedatabase.data.model.MovieFavorite;
import com.sanitcode.cataloguemoviedatabase.data.model.ResultItems;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sanitcode.cataloguemoviedatabase.BuildConfig.BASE_URL;

public class FavoriteActivity extends AppCompatActivity {
    private static final String INTENT_DETAIL = "detail";

    @BindView(R.id.rv_favorite)
    RecyclerView recyclerView;

    MovieAdapter movieAdapter;

    List<ResultItems> resultItemsList;
    ApiCall apiCall;
    Call<ResultItems> resultItemsCall;
    ResultItems resultItems;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        ButterKnife.bind(this);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.favorite));

        resultItemsList = new ArrayList<>();
        resultItems = new ResultItems();

        initView();
        ArrayList<MovieFavorite> movieFavoriteArrayList = getIntent()
                .getParcelableArrayListExtra(INTENT_DETAIL);
        for (MovieFavorite mF : movieFavoriteArrayList) {
            getFavoriteMovies(mF.getId());
        }
    }

    void initView() {
        movieAdapter = new MovieAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void getFavoriteMovies(String id) {
        apiCall = ApiClient.getClient(BASE_URL).create(ApiCall.class);
        resultItemsCall = apiCall.getMovieById(id, BuildConfig.APIKEY);

        resultItemsCall.enqueue(new Callback<ResultItems>() {
            @Override
            public void onResponse(@NonNull Call<ResultItems> call, @NonNull Response<ResultItems> response) {
                resultItemsList.add(response.body());
                movieAdapter.setMovieResult(resultItemsList);
                recyclerView.setAdapter(movieAdapter);
            }

            @Override
            public void onFailure(@NonNull Call<ResultItems> call, @NonNull Throwable t) {
                resultItems = null;
            }
        });
    }
}
