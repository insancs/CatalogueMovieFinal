package com.sanitcode.cataloguemoviedatabase.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sanitcode.cataloguemoviedatabase.BuildConfig;
import com.sanitcode.cataloguemoviedatabase.R;
import com.sanitcode.cataloguemoviedatabase.ui.DetailActivity;
import com.sanitcode.cataloguemoviedatabase.data.model.ResultItems;
import com.sanitcode.cataloguemoviedatabase.utils.DateTime;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieViewHolder extends RecyclerView.ViewHolder {
    private final static String MOVIE_DETAIL = "movie_detail";

    @BindView(R.id.poster)
    ImageView image_poster;
    @BindView(R.id.btn_share)
    Button buttonShare;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.btn_detail)
    Button buttonDetail;
    @BindView(R.id.releasedate)
    TextView releaseDate;
    @BindView(R.id.overview)
    TextView overview;

    private Context context;

    MovieViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    void bindView(final ResultItems resultItems) {
        title.setText(resultItems.getTitle());
        overview.setText(resultItems.getOverview());
        releaseDate.setText(DateTime.getDateDay(resultItems.getReleaseDate()));
        Picasso.get()
                .load(BuildConfig.BASE_POSTER_URL + resultItems.getPosterPath())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(image_poster);

        buttonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(buttonDetail.getContext(), DetailActivity.class);
                intent.putExtra(MOVIE_DETAIL, resultItems);
                buttonDetail.getContext().startActivity(intent);
            }
        });
        buttonShare.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StringFormatInvalid")
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TITLE, resultItems.getTitle());
                intent.putExtra(Intent.EXTRA_SUBJECT, resultItems.getTitle());
                intent.putExtra(Intent.EXTRA_TEXT, resultItems.getTitle() + "\n\n" + resultItems.getOverview());
                itemView.getContext().startActivity(Intent.createChooser(intent, itemView.getResources().getString(R.string.share)));
            }
        });

    }
}
