package com.example.android.booklistingapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.*;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    private static final String LOG_TAG = MainActivity.class.getName();
    private static final String BOOKS_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final int BOOK_LOADER_ID = 1;
    private BookAdapter mAdapter;
    private TextView emptyStateView;
    View spinner;
    String queryTitle;
    EditText queryTitleView;
    ListView bookListView;
    LoaderManager loaderManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "test: Book Activity onCreate() called");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a new adapter that takes an empty list of books as input
        mAdapter = new BookAdapter(this, new ArrayList<Book>());
        bookListView = (ListView) findViewById(R.id.list);
        bookListView.setAdapter(mAdapter);

        emptyStateView = (TextView) findViewById(R.id.empty);
        bookListView.setEmptyView(emptyStateView);

        queryTitleView = (EditText) findViewById(R.id.search_title);
        Button searchButtonView = (Button) findViewById(R.id.search_button);

        spinner = findViewById(R.id.loading_spinner);
        spinner.setVisibility(View.GONE);

        if (isConnected()) {
            spinner.setVisibility(View.VISIBLE);
            loaderManager = getLoaderManager();
            Log.i(LOG_TAG, "test: calling initLoader()");
            loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);
        } else {
            emptyStateView.setText(R.string.no_internet_connection);
        }

        searchButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryTitle = queryTitleView.getText().toString();
                Log.i(LOG_TAG, "queryTitle: " + queryTitle);

                if (isConnected()) {
                    spinner.setVisibility(View.VISIBLE);
                    emptyStateView.setText("");

                    Log.i(LOG_TAG, "test: calling initLoader()");
                    loaderManager.restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                } else {
                    emptyStateView.setText(R.string.no_internet_connection);
                }
            }
        });

        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current book that was clicked on
                Book currentBook = (Book) mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookURL = Uri.parse(currentBook.getUrl());

                // Create a new intent to view the book URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookURL);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG, "test: onCreateLoader() called");

        if (queryTitle == null || queryTitle.isEmpty()) queryTitle = "test";
        String newUrl = BOOKS_URL + queryTitle.toLowerCase() + "&maxResults=30";
        return new BookLoader(this, newUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        emptyStateView.setText(R.string.no_books_found);
        spinner.setVisibility(View.GONE);

        Log.i(LOG_TAG, "test: onLoadFinished() called");

        // Clear the adapter of previous book data
        mAdapter.clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        } else {
            Log.i(LOG_TAG, "list of books is empty");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        Log.i(LOG_TAG, "test: onLoaderReset() called");
        mAdapter.clear();
    }

}

