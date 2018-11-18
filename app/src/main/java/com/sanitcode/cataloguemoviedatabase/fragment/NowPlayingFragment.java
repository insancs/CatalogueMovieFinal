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
import com.sanitcode.cataloguemoviedatabase.data.model.Movie;
import com.sanitcode.cataloguemoviedatabase.data.model.ResultItems;
import com.sanitcode.cataloguemoviedatabase.api.ApiClient;
import com.sanitcode.cataloguemoviedatabase.api.ApiCall;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.sanitcode.cataloguemoviedatabase.BuildConfig.BASE_URL;

public class NowPlayingFragment extends Fragment {

    public NowPlayingFragment() {
        // Required empty public constructor
    }

    @BindView(R.id.rv_nowplaying)
    RecyclerView rvNowPlaying;

    @BindView(R.id.progress_bar_now)
    ProgressBar progressBar;

    List<ResultItems> resultItems;

    MovieAdapter movieAdapter;

    ApiCall apiCall;
    Call<Movie> movieCall;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_now_playing, container, false);
        ButterKnife.bind(this, rootView);
        initView();
        getMovies();

        return rootView;
    }

    void initView() {
        movieAdapter = new MovieAdapter(getActivity());
        rvNowPlaying.setLayoutManager(new LinearLayoutManager(getContext()));
        rvNowPlaying.setHasFixedSize(true);
        rvNowPlaying.setItemAnimator(new DefaultItemAnimator());
    }

    private void getMovies() {
        showProgressBar();

        apiCall = ApiClient.getClient(BASE_URL).create(ApiCall.class);
        movieCall = apiCall.getNowPlayingMovie(BuildConfig.APIKEY);
        resultItems = new ArrayList<>();
        movieCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.body() != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        resultItems = Objects.requireNonNull(response.body()).getResults();
                    }
                }
                movieAdapter.setMovieResult(resultItems);
                rvNowPlaying.setAdapter(movieAdapter);
                hideProgressBar();
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                loadFailed();
            }
        });
    }

    private void loadFailed() {
        Toast.makeText(getActivity(), getResources().getString(R.string.wrong_nowplaying), Toast.LENGTH_SHORT).show();
        hideProgressBar();
    }

    void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }
    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }*/

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelableArrayList("now_movie", new ArrayList<>(movieAdapter.getList()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            ArrayList<ResultItems> list;
            list = savedInstanceState.getParcelableArrayList("now_movie");
            movieAdapter.setMovieResult(list);
            rvNowPlaying.setAdapter(movieAdapter);
        }
    }
}
