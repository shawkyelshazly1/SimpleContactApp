package com.example.veronica.simplecontactapp;


import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.veronica.simplecontactapp.Data.ContactsContract;
import com.example.veronica.simplecontactapp.Data.CustomCursorAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllContactsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int LOADER_ID = 1;
    ListView contactsListView;
    CustomCursorAdapter mCustomCursorAdapter;

    public AllContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.all_contacts_fragment, container, false);

        contactsListView = mainView.findViewById(R.id.contacts_listView);
        mCustomCursorAdapter = new CustomCursorAdapter(getActivity(), null);
        contactsListView.setAdapter(mCustomCursorAdapter);
        getLoaderManager().initLoader(LOADER_ID, null, this);

        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent goView = new Intent(getActivity(), ViewContactActivity.class);
                Uri contactUri = ContentUris.withAppendedId(ContactsContract.ContactsEntry.CONTENT_URI, id);
                goView.setData(contactUri);
                startActivity(goView);
            }
        });


        return mainView;

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {ContactsContract.ContactsEntry._ID, ContactsContract.ContactsEntry.COLUMN_NUMBER, ContactsContract.ContactsEntry.COLUMN_NAME};
        return new CursorLoader(getActivity(), ContactsContract.ContactsEntry.CONTENT_URI, projection, null, null, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCustomCursorAdapter.swapCursor(cursor);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCustomCursorAdapter.swapCursor(null);
    }

}
