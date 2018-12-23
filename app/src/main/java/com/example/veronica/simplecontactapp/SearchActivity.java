package com.example.veronica.simplecontactapp;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.example.veronica.simplecontactapp.Data.ContactsContract;
import com.example.veronica.simplecontactapp.Data.CustomCursorAdapter;

public class SearchActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static String searchQuery = null;
    EditText searchBar;
    CustomCursorAdapter customCursorAdapter;

    @Override
    protected void onStop() {
        super.onStop();
        searchQuery = null;
        resetLoader();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchBar = findViewById(R.id.search_bar);
        ListView mainListView = findViewById(R.id.contacts_listView);
        customCursorAdapter = new CustomCursorAdapter(this, null);
        mainListView.setAdapter(customCursorAdapter);


        getLoaderManager().initLoader(1, null, this);

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchQuery = searchBar.getText().toString();
                if (searchQuery.matches(".*[a-zA-Z0-9].*")) {
                    resetLoader();
                } else {
                    searchQuery = null;
                    resetLoader();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posittion, long id) {
                Intent goView = new Intent(SearchActivity.this, ViewContactActivity.class);
                Uri mCurrentUri = ContentUris.withAppendedId(ContactsContract.ContactsEntry.CONTENT_URI, id);
                goView.setData(mCurrentUri);
                startActivity(goView);
            }
        });
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {ContactsContract.ContactsEntry._ID, ContactsContract.ContactsEntry.COLUMN_NAME, ContactsContract.ContactsEntry.COLUMN_NUMBER};
        String selection = ContactsContract.ContactsEntry.COLUMN_NAME + " LIKE ?" + " OR " + ContactsContract.ContactsEntry.COLUMN_NUMBER + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + String.valueOf(SearchActivity.searchQuery) + "%", "%" + String.valueOf(SearchActivity.searchQuery) + "%"};
        return new CursorLoader(this, ContactsContract.ContactsEntry.CONTENT_URI, projection, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        customCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        customCursorAdapter.swapCursor(null);
    }

    private void resetLoader() {
        getLoaderManager().restartLoader(1, null, this);
    }
}
