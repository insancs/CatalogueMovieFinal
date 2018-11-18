package com.sanitcode.cataloguemoviedatabase.fragment;


import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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

public class PopularFragment extends Fragment {

    @BindView(R.id.rv_popular)
    RecyclerView rvPopular;

    @BindView(R.id.progress_bar_popular)
    ProgressBar progressBar;

    List<ResultItems> movieList;
    MovieAdapter movieAdapter;

    ApiCall movieService;
    Call<Movie> movieCall;


    public PopularFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_popular, container, false);
        ButterKnife.bind(this, rootView);
        initView();
        getMovies();

        return rootView;
    }

    private void initView() {
        movieAdapter = new MovieAdapter(getActivity());
        rvPopular.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPopular.setHasFixedSize(true);
        rvPopular.setItemAnimator(new DefaultItemAnimator());
    }

    public void getMovies() {
        showProgressBar();

        movieList = new ArrayList<>();
        movieService = ApiClient.getClient(BASE_URL).create(ApiCall.class);
        movieCall = movieService.getPopularMovie(BuildConfig.APIKEY);
        movieCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.body() != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        movieList = Objects.requireNonNull(response.body()).getResults();
                    }
                }
                movieAdapter.setMovieResult(movieList);
                rvPopular.setAdapter(movieAdapter);
                hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
               loadFailed();
            }
        });
    }

    private void loadFailed() {
        Toast.makeText(getActivity(), getResources().getString(R.string.wrong_popular), Toast.LENGTH_SHORT).show();
        hideProgressBar();
    }

    void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList("popular_movie", new ArrayList<>(movieAdapter.getList()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ArrayList<ResultItems> list;
            list = savedInstanceState.getParcelableArrayList("popular_movie");
            movieAdapter.setMovieResult(list);
            rvPopular.setAdapter(movieAdapter);
        }
    }
}
