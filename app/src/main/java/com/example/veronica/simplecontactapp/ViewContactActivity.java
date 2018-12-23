package com.example.veronica.simplecontactapp;

import android.app.LoaderManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.veronica.simplecontactapp.Data.ContactsContract.ContactsEntry;

public class ViewContactActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    TextView nameView;
    TextView numberView;
    ImageView favouriteStar;
    int favourite;
    Uri contactUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        nameView = findViewById(R.id.name_view);
        numberView = findViewById(R.id.number_view);
        favouriteStar = findViewById(R.id.favourite_star);

        Intent viewIntent = getIntent();
        contactUri = viewIntent.getData();

        getLoaderManager().initLoader(1, null, this);


        favouriteStar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (isFavourite()) {
                    favourite = 0;
                    updateContact(favourite);

                    Toast.makeText(ViewContactActivity.this, "Removed From Favourites", Toast.LENGTH_SHORT).show();
                } else {
                    favourite = 1;
                    updateContact(favourite);
                    Toast.makeText(ViewContactActivity.this, "Added To Favourites", Toast.LENGTH_SHORT).show();
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(ViewContactActivity.this);
                    Intent intent = new Intent(ViewContactActivity.this, ContactsActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(ViewContactActivity.this, 100, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    notificationBuilder.setSmallIcon(R.drawable.ic_notifications_black_24dp);
                    notificationBuilder.setContentTitle("News!");
                    notificationBuilder.setContentText("This is a great news");

                    notificationBuilder.setAutoCancel(true);
                    notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                    notificationBuilder.setContentIntent(pendingIntent);
                    Notification notification = notificationBuilder.build();
                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    manager.notify(100, notification);

                }

                resetLoader();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_contact:
                editContact();
                break;
            case R.id.delete_contact:
                deleteContact();
                break;
            case R.id.call_contact:
                callContact();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    //Call Contact Function
    private void callContact() {
        Intent callContact = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + numberView.getText().toString()));
        startActivity(callContact);
    }

    //Delete Contact Function
    private void deleteContact() {
        int contactsDeleted = getContentResolver().delete(contactUri, null, null);
        if (contactsDeleted != 0) {
            Toast.makeText(this, R.string.contact_deleted_msg, Toast.LENGTH_SHORT).show();
            Intent goHome = new Intent(ViewContactActivity.this, ContactsActivity.class);
            startActivity(goHome);
        } else {
            Toast.makeText(this, R.string.contact_deleted_failed_msg, Toast.LENGTH_SHORT).show();
        }
    }

    //Add or Remove Favourite From Contact
    private void updateContact(int favouriteValue) {
        ContentValues values = new ContentValues();
        values.put(ContactsEntry.COLUMN_FAVOURITE, favouriteValue);

        int contactsUpdated = getContentResolver().update(contactUri, values, null, null);
        if (contactsUpdated == 0) {
            Toast.makeText(this, "Failed to update contact", Toast.LENGTH_SHORT).show();
        }
    }

    //Check if Favourite or not, Update
    private boolean isFavourite() {
        if (favourite == 0) {
            return false;
        } else {
            return true;
        }
    }

    //Change Favourite Icon
    private void changeFavouriteIcon() {
        if (favourite == 0) {
            favouriteStar.setImageResource(R.drawable.ic_star_border_black_24dp);
        } else {
            favouriteStar.setImageResource(R.drawable.ic_star_black_24dp);
        }
    }

    //Edit Contact Function
    private void editContact() {
        Intent goEdit = new Intent(ViewContactActivity.this, AddOrEditContactActivity.class);
        goEdit.setData(contactUri);
        startActivity(goEdit);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {ContactsEntry._ID, ContactsEntry.COLUMN_NAME, ContactsEntry.COLUMN_NUMBER, ContactsEntry.COLUMN_FAVOURITE};

        return new CursorLoader(this, contactUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            nameView.setText(cursor.getString(cursor.getColumnIndex(ContactsEntry.COLUMN_NAME)));
            numberView.setText(cursor.getString(cursor.getColumnIndex(ContactsEntry.COLUMN_NUMBER)));
            favourite = cursor.getInt(cursor.getColumnIndex(ContactsEntry.COLUMN_FAVOURITE));
            changeFavouriteIcon();

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        nameView.setText("");
        numberView.setText("");
    }

    private void resetLoader() {
        getLoaderManager().restartLoader(1, null, this);
    }
}
