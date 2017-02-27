package com.lednhatkhanh.thenewsreader;

import android.content.ClipData.Item;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lednhatkhanh.thenewsreader.databinding.NewsListItemBinding;
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

    NewsAdapter(NewsAdapterOnClickHandler handler) {
        mClickHandler = handler;
    }

    interface NewsAdapterOnClickHandler {
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
        NewsListItemBinding newsListItemBinding = newsAdapterViewHolder.getBinding();
        Article articleAtPosition = mArticlesList.get(position);
        String formattedTime;

        try {
            formattedTime = DataUtils.convertUTCToLocalTime(articleAtPosition.getPublishedAt());
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, e.getLocalizedMessage());
            return;
        }


        newsListItemBinding.articleTitleTextView.setText(articleAtPosition.getTitle());
        newsListItemBinding.articleAuthorTextView.setText(articleAtPosition.getAuthor());
        newsListItemBinding.articleDescriptionTextView.setText(articleAtPosition.getDescription());
        newsListItemBinding.articlePublishedAtTextView.setText(formattedTime);

        if(articleAtPosition.getUrlToImage() == null) {
            newsListItemBinding.articleImageView.setVisibility(View.INVISIBLE);
        } else {
            Picasso.with(context).load(articleAtPosition.getUrlToImage())
                    .fit()
                    .centerCrop()
                    .into(newsListItemBinding.articleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mArticlesList == null ? 0 : mArticlesList.size();
    }

    void setArticlesList(ArrayList<Article> articlesList) {
        this.mArticlesList = articlesList;
        notifyDataSetChanged();
    }

    class NewsAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        NewsListItemBinding mBinding;

        NewsAdapterViewHolder(View view) {
            super(view);

            mBinding = DataBindingUtil.bind(view);

            mBinding.getRoot().setOnClickListener(this);
        }

        NewsListItemBinding getBinding() {
            return mBinding;
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String title = mArticlesList.get(adapterPosition).getTitle();
            mClickHandler.onClick(title);
        }
    }
}
