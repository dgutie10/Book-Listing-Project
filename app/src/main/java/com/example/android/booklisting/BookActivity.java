package com.example.android.booklisting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,LoaderManager.LoaderCallbacks<List<Book>> {

    //Global variables
    private static final int BOOK_LOADER_ID = 1;
    private static final String BOOK_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private String searchParam = "";
    private String maxResults = "&maxResults=20";
    public static final String LOG_TAG = BookActivity.class.getName();
    private NetworkInfo networkInfo;
    private TextView emptyTextView;
    private BookAdapter bookAdapter;
    private View progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        ListView bookListView = (ListView) findViewById(R.id.list);

        emptyTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(emptyTextView);

        bookAdapter = new BookAdapter(this, new ArrayList<Book>());
        bookListView.setAdapter(bookAdapter);

        progressBar = findViewById(R.id.progress_bar);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        LoaderManager loaderManager = getLoaderManager();
        Loader<List<Book>> listLoader = loaderManager.initLoader(BOOK_LOADER_ID, null, this);

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        if (searchParam == "") return new BookLoader(this, BOOK_REQUEST_URL+"android"+maxResults);
        return new BookLoader(this, BOOK_REQUEST_URL+searchParam+maxResults);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        progressBar.setVisibility(View.GONE);


        if (networkInfo != null && networkInfo.isConnected()){
            if (searchParam == ""){
                emptyTextView.setText(R.string.no_books);
            }else{
                emptyTextView.setText("");
            }
        }else{
            emptyTextView.setText(R.string.no_connection);
        }

        bookAdapter.clear();
        if(data != null && !data.isEmpty())bookAdapter.addAll(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        progressBar.setVisibility(View.VISIBLE);

        searchParam = query;
        getLoaderManager().restartLoader(BOOK_LOADER_ID,null,this);
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
        emptyTextView.setText("");
        progressBar.setVisibility(View.VISIBLE);
        searchParam = newText;
        getLoaderManager().restartLoader(BOOK_LOADER_ID,null,this);
        return false;
    }



    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        bookAdapter.clear();
    }
}
