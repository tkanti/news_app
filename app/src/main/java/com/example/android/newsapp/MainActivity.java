package com.example.android.newsapp;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<News>>, SwipeRefreshLayout.OnRefreshListener {
    private static NewsAdapter adapter;
    private static int LOADER_ID = 1;
    SwipeRefreshLayout swipe;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipe = findViewById(R.id.swipe_refresh);
        swipe.setOnRefreshListener(this);

        ListView listView = findViewById(R.id.list_view);
        adapter = new NewsAdapter(this);
        listView.setAdapter(adapter);
        NewsAdapterOnItemClickListener listener =
                new NewsAdapterOnItemClickListener(this, adapter);
        listView.setOnItemClickListener(listener);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(mEmptyStateTextView);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> data) {
        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.nothing_to_show);

        swipe.setRefreshing(false);
        if (data != null) {
            adapter.setNotifyOnChange(false);
            adapter.clear();
            adapter.setNotifyOnChange(true);
            adapter.addAll(data);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this);
    }

    @Override
    public void onRefresh() {
        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) { }
}