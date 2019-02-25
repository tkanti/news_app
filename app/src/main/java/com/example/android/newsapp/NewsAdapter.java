package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Context context) {
        super(context, -1, new ArrayList<News>());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView headline = (TextView) convertView.findViewById(R.id.headline);
        headline.setText(currentNews.getHeadline());

        TextView author = (TextView) convertView.findViewById(R.id.author);
        author.setText(currentNews.getAuthor());

        TextView date = (TextView) convertView.findViewById(R.id.date);
        date.setText(currentNews.getDate());

        TextView section = (TextView) convertView.findViewById(R.id.section);
        section.setText(currentNews.getSection());

        return convertView;
    }
}
