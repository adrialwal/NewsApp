package com.example.adrialwalters.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * An {@link NewsAdapter} knows how to create a list item layout for each book
 * in the data source (a list of {@link News} objects).
 * <p>
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class NewsAdapter extends ArrayAdapter<News> {
    private static final String LOG_TAG = NewsAdapter.class.getName();

    /**
     * Constructs a new {@link NewsAdapter}.
     *
     * @param context of the app
     * @param news    is the list of news articles, which is the data source of the adapter
     */
    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    /**
     * Returns a list item view that displays information about the articles.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        // Find the book at the given position in the list of articles
        News newArticle = getItem(position);

        TextView title = (TextView) listItemView.findViewById(R.id.title);
        // Find the TextView with view ID title
        title.setText(newArticle.getTitle());

        TextView date = (TextView) listItemView.findViewById(R.id.date);
        // Find the TextView with view ID datePublished
        date.setText(newArticle.getDate());

        TextView section = (TextView) listItemView.findViewById(R.id.article_section);
        // Find the TextView with view ID datePublished
        section.setText(newArticle.getSection());

        return listItemView;
    }
}