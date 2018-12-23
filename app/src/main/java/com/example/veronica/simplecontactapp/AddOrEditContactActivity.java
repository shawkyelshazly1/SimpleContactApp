package com.example.veronica.simplecontactapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.veronica.simplecontactapp.Data.ContactsContract;

public class AddOrEditContactActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    Uri mContactUri;
    EditText addNameView;
    EditText addNumberView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_or_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_contact_menu_item:
                saveContact();
                break;
            default:
                throw new IllegalArgumentException("Failed to add Contact");
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        //Initializing the Views in the activity
        addNameView = findViewById(R.id.add_name_view);
        addNumberView = findViewById(R.id.add_number_view);

        //Checking what intent brought us here & assigning it is Uri from the data if it has one already or not
        Intent editOrViewIntent = getIntent();
        mContactUri = editOrViewIntent.getData();

        //Changing Activity Title whither we are here to add or edit a contact
        if (mContactUri != null) {
            //Setting title to "Edit Contact" if we are coming from an activity with data contains "Uri"
            setTitle(R.string.edit_contact_activity_title);
            getLoaderManager().initLoader(1, null, this);
        } else {
            //Setting the title to "Add Contact" if we are coming from an activity through intent with no data
            setTitle(R.string.add_contact_activity_title);
        }


    }

    private void saveContact() {
        //Getting Fields Values
        String name = addNameView.getText().toString().trim();
        String number = addNumberView.getText().toString().trim();

        //Checking if fields are empty or not
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(number)) {
            //Toasting that fields cannot be Empty
            Toast.makeText(this, R.string.empty_field_toast_msg, Toast.LENGTH_SHORT).show();
        } else {
            //Storing values in contentValues
            ContentValues contentValues = new ContentValues();
            contentValues.put(ContactsContract.ContactsEntry.COLUMN_NAME, name);
            contentValues.put(ContactsContract.ContactsEntry.COLUMN_NUMBER, number);

            //Checking the Uri to see if we are adding new one or updating an existing one
            if (mContactUri != null) {
                int contactsUpdated = getContentResolver().update(mContactUri, contentValues, null, null);
                if (contactsUpdated != 0) {
                    Toast.makeText(this, R.string.contact_updated_taost_msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.failed_updating_contact_toast_msg, Toast.LENGTH_SHORT).show();
                }
            } else {
                mContactUri = getContentResolver().insert(ContactsContract.ContactsEntry.CONTENT_URI, contentValues);
                if (mContactUri != null) {
                    Toast.makeText(this, R.string.add_new_user_toast_msg, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, R.string.failed_toadd_user, Toast.LENGTH_SHORT).show();
                }
            }
            Intent goView = new Intent(AddOrEditContactActivity.this, ViewContactActivity.class);
            goView.setData(mContactUri);
            startActivity(goView);

        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {ContactsContract.ContactsEntry._ID, ContactsContract.ContactsEntry.COLUMN_NAME, ContactsContract.ContactsEntry.COLUMN_NUMBER};
        return new CursorLoader(this, mContactUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            addNameView.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.ContactsEntry.COLUMN_NAME)));
            addNumberView.setText(cursor.getString(cursor.getColumnIndex(ContactsContract.ContactsEntry.COLUMN_NUMBER)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        addNumberView.setText("");
        addNameView.setText("");
    }
}
