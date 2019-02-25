package com.example.android.newsapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;

public class NewsAdapterOnItemClickListener implements AdapterView.OnItemClickListener {

    private static NewsAdapter newsAdapter;
    private MainActivity mainActivity;
    public NewsAdapterOnItemClickListener(MainActivity mainActivity, NewsAdapter newsAdapter){
        this.mainActivity = mainActivity;
        this.newsAdapter = newsAdapter;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        News news = newsAdapter.getItem(position);
        String url = news.getUrl();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        mainActivity.startActivity(intent);
    }
}
