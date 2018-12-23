package com.example.veronica.simplecontactapp.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.example.veronica.simplecontactapp.Data.ContactsContract.ContactsEntry;
import com.example.veronica.simplecontactapp.SearchActivity;

public class ContactsProvider extends ContentProvider {
    public static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final int CONTACTS = 100;
    public static final int CONTACTS_ID = 101;
    public static final int CONTACTS_NAME = 102;
    public static final int CONTACTS_FEAVOURITE = 103;

    static {
        mUriMatcher.addURI(ContactsContract.CONTENT_AUTHORITY, ContactsContract.CONTACTS_PATH, CONTACTS);
        mUriMatcher.addURI(ContactsContract.CONTENT_AUTHORITY, ContactsContract.CONTACTS_PATH + "/#", CONTACTS_ID);
        mUriMatcher.addURI(ContactsContract.CONTENT_AUTHORITY, ContactsContract.CONTACTS_PATH + ContactsEntry.COLUMN_NAME, CONTACTS_NAME);
        mUriMatcher.addURI(ContactsContract.CONTENT_AUTHORITY, ContactsContract.CONTACTS_PATH + ContactsEntry.COLUMN_FAVOURITE, CONTACTS_FEAVOURITE);
    }

    ContactsDbHelper mContactsDbHelper;

    @Override
    public boolean onCreate() {
        mContactsDbHelper = new ContactsDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mContactsDbHelper.getReadableDatabase();
        Cursor mCursor = null;
        int match = mUriMatcher.match(uri);
        switch (match) {
            case CONTACTS:
                mCursor = database.query(ContactsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CONTACTS_ID:
                selection = ContactsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                mCursor = database.query(ContactsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CONTACTS_NAME:
                selection = ContactsContract.ContactsEntry.COLUMN_NAME + " LIKE ?" + " OR " + ContactsEntry.COLUMN_NUMBER + " LIKE ?";
                selectionArgs = new String[]{"%" + String.valueOf(SearchActivity.searchQuery) + "%", "%" + String.valueOf(SearchActivity.searchQuery) + "%"};
                mCursor = database.query(ContactsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case CONTACTS_FEAVOURITE:
                selection = ContactsContract.ContactsEntry.COLUMN_FAVOURITE + "=?";
                selectionArgs = new String[]{String.valueOf(ContactsContract.ContactsEntry.FAVOURITE)};
                mCursor = database.query(ContactsEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
            default:
                throw new IllegalArgumentException("Failed loading Cursor");
        }
        mCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return mCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case CONTACTS:
                return insertContact(uri, contentValues);
            default:
                throw new IllegalArgumentException("Failed to insert Item");
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mContactsDbHelper.getWritableDatabase();
        int contactsDeleted;
        int match = mUriMatcher.match(uri);
        switch (match) {
            case CONTACTS:
                contactsDeleted = database.delete(ContactsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CONTACTS_ID:
                selection = ContactsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                contactsDeleted = database.delete(ContactsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Failed to delete Item");
        }
        if (contactsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);

        }
        return contactsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match = mUriMatcher.match(uri);
        switch (match) {
            case CONTACTS:
                return updateContact(uri, contentValues, selection, selectionArgs);
            case CONTACTS_ID:
                selection = ContactsEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateContact(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Failed to update item");
        }
    }

    private Uri insertContact(Uri uri, ContentValues values) {
        String name = values.getAsString(ContactsEntry.COLUMN_NAME);
        String number = values.getAsString(ContactsEntry.COLUMN_NUMBER);
        if (name == "") {
            throw new IllegalArgumentException("Name cannot be Empty");
        }
        if (number == "") {
            throw new IllegalArgumentException("Number cannot be Empty");
        }
        SQLiteDatabase database = mContactsDbHelper.getWritableDatabase();
        long id = database.insert(ContactsEntry.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e("ContactsProvider", "Failed to insert");
        } else {
            getContext().getContentResolver().notifyChange(uri, null);
        }


        return ContentUris.withAppendedId(uri, id);
    }

    private int updateContact(Uri uri, ContentValues contentValues, String selection, @Nullable String[] selectionArgs) {
        int contactsUpdated;
        SQLiteDatabase database = mContactsDbHelper.getWritableDatabase();
        if (contentValues.containsKey(ContactsEntry.COLUMN_NAME)) {
            String name = contentValues.getAsString(ContactsEntry.COLUMN_NAME);
            if (TextUtils.isEmpty(name)) {
                throw new IllegalArgumentException("Name Cannot Be Empty");
            }
        }
        if (contentValues.containsKey(ContactsEntry.COLUMN_NUMBER)) {
            String number = contentValues.getAsString(ContactsEntry.COLUMN_NUMBER);
            if (TextUtils.isEmpty(number)) {
                throw new IllegalArgumentException("Number Cannot Be Empty");
            }
        }

        contactsUpdated = database.update(ContactsEntry.TABLE_NAME, contentValues, selection, selectionArgs);
        if (contactsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return contactsUpdated;

    }

}
