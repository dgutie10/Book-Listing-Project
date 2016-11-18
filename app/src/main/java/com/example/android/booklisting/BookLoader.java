package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

import static com.example.android.booklisting.QueryUtil.LOG_TAG;

/**
 * Created by diegog on 11/3/2016.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private String url;

    public BookLoader(Context context, String url){
        super (context);
        url = url.replace(" ", "%20");
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        if (url ==null) return null;

        List<Book> result = QueryUtil.fetchBooks(url);

        return result;
    }
}
