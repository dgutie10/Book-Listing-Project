package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by diegog on 11/3/2016.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    public BookLoader(Context context){
        super (context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        return null;
    }
}
