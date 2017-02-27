package com.lednhatkhanh.thenewsreader;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lednhatkhanh.thenewsreader.models.Article;
import com.lednhatkhanh.thenewsreader.utils.DataUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by lednh on 2/27/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {
    private Context context;
    private ArrayList<Article> mArticlesList;

    private final NewsAdapterOnClickHandler mClickHandler;

    private static final String LOG_TAG = NewsAdapter.class.getSimpleName();

    public NewsAdapter(NewsAdapterOnClickHandler handler) {
        mClickHandler = handler;
    }

    public interface NewsAdapterOnClickHandler {
        void onClick(String title);
    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.news_list_item, viewGroup, false);
        return new NewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder newsAdapterViewHolder, int position) {
        Article articleAtPosition = mArticlesList.get(position);
        String formattedTime;

        try {
            formattedTime = DataUtils.convertUTCToLocalTime(articleAtPosition.getPublishedAt());
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, e.getLocalizedMessage());
            return;
        }

        newsAdapterViewHolder.mArticleTitleTextView.setText(articleAtPosition.getTitle());
        newsAdapterViewHolder.mArticleAuthorTextView.setText(articleAtPosition.getAuthor());
        newsAdapterViewHolder.mArticleDescriptionTextView.setText(articleAtPosition.getDescription());
        newsAdapterViewHolder.mArticlePublishedAtTextView.setText(formattedTime);
        if(articleAtPosition.getUrlToImage() == null) {
            newsAdapterViewHolder.mArticleImageView.setVisibility(View.INVISIBLE);
        } else {
            Picasso.with(context).load(articleAtPosition.getUrlToImage())
                    .fit()
                    .centerCrop()
                    .into(newsAdapterViewHolder.mArticleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mArticlesList == null ? 0 : mArticlesList.size();
    }

    public void setArticlesList(ArrayList<Article> articlesList) {
        this.mArticlesList = articlesList;
        notifyDataSetChanged();
    }

    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mArticleTitleTextView;
        public final TextView mArticleAuthorTextView;
        public final TextView mArticleDescriptionTextView;
        public final TextView mArticlePublishedAtTextView;

        public final ImageView mArticleImageView;

        public NewsAdapterViewHolder(View view) {
            super(view);

            mArticleTitleTextView = (TextView) view.findViewById(R.id.articleTitleTextView);
            mArticleAuthorTextView = (TextView) view.findViewById(R.id.articleAuthorTextView);
            mArticleDescriptionTextView = (TextView) view.findViewById(R.id.articleDescriptionTextView);
            mArticlePublishedAtTextView = (TextView) view.findViewById(R.id.articlePublishedAtTextView);

            mArticleImageView = (ImageView) view.findViewById(R.id.articleImageView);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String title = mArticlesList.get(adapterPosition).getTitle();
            mClickHandler.onClick(title);
        }
    }
}
