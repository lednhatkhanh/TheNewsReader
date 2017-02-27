package com.lednhatkhanh.thenewsreader;

import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lednhatkhanh.thenewsreader.databinding.NewsListItemBinding;
import com.lednhatkhanh.thenewsreader.models.Article;
import com.lednhatkhanh.thenewsreader.utils.DataUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by lednh on 2/27/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private final NewsAdapterOnClickHandler mClickHandler;
    private static final String LOG_TAG = NewsAdapter.class.getSimpleName();

    NewsAdapter(Context context, NewsAdapterOnClickHandler handler) {
        this.mContext = context;
        mClickHandler = handler;
    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.news_list_item, viewGroup, false);
        view.setFocusable(true);
        return new NewsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsAdapterViewHolder newsAdapterViewHolder, int position) {
        NewsListItemBinding newsListItemBinding = newsAdapterViewHolder.getBinding();
        mCursor.moveToPosition(position);

        String formattedTime;

        formattedTime = DataUtils
                .getReadableDateFormat(mCursor.getLong(MainActivity.INDEX_NEWS_PUBLISHED_AT));

        newsListItemBinding.articleTitleTextView
                .setText(mCursor.getString(MainActivity.INDEX_NEWS_TITLE));
        newsListItemBinding.articleAuthorTextView
                .setText(mCursor.getString(MainActivity.INDEX_NEWS_AUTHOR));
        newsListItemBinding.articleDescriptionTextView
                .setText(mCursor.getString(MainActivity.INDEX_NEWS_DESCRIPTION));
        newsListItemBinding.articlePublishedAtTextView.setText(formattedTime);

        if(mCursor.getString(MainActivity.INDEX_NEWS_URL_TO_IMAGE) == null) {
            newsListItemBinding.articleImageView.setVisibility(View.INVISIBLE);
        } else {
            Picasso.with(mContext)
                    .load(mCursor.getString(MainActivity.INDEX_NEWS_URL_TO_IMAGE))
                    .fit()
                    .centerCrop()
                    .into(newsListItemBinding.articleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    void swapCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    interface NewsAdapterOnClickHandler {
        void onClick(String title);
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
            mClickHandler.onClick("Placeholder....");
        }
    }
}
