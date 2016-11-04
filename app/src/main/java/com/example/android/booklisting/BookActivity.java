package com.example.android.booklisting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>>{

    //Global variables
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private static final String BOOK_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=3";
    public static final String LOG_TAG = BookActivity.class.getName();
    private NetworkInfo networkInfo;
    protected TextView emptyTextView;
    private BookAdapter bookAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(LOG_TAG,"onCreate initiated");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        ListView bookListView = (ListView) findViewById(R.id.list);

        bookListView.setEmptyView(emptyTextView);

        bookAdapter = new BookAdapter(this, new ArrayList<Book>());
        bookListView.setAdapter(bookAdapter);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        Log.e(LOG_TAG,"onCreateLoader initiated");
        return new BookLoader(this, BOOK_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        View progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

//        if (networkInfo != null && networkInfo.isConnected()){
//            emptyTextView.setText(R.string.no_books);
//        }else{
//            emptyTextView.setText(R.string.no_connection);
//        }

        bookAdapter.clear();
        if(data != null && !data.isEmpty())bookAdapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        bookAdapter.clear();
    }
}
