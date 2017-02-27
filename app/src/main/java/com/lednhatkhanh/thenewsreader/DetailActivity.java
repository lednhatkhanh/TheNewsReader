package com.lednhatkhanh.thenewsreader;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lednhatkhanh.thenewsreader.databinding.ActivityDetailBinding;

public class DetailActivity extends AppCompatActivity {

    ActivityDetailBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Intent intent = getIntent();
        if(intent != null) {
            if(intent.hasExtra(Intent.EXTRA_TEXT)) {
                String title = intent.getStringExtra(Intent.EXTRA_TEXT);
                mBinding.detailTextView.setText(title);
            }
        }
    }
}
