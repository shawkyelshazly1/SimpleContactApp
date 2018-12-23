package com.example.veronica.simplecontactapp.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import com.example.veronica.simplecontactapp.Data.ContactsContract.ContactsEntry;

public class ContactsDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "contacts.db";
    public static final int DB_VERSION = 2;
    public static final String SQL_CREATE_TABLE_QUERY = "CREATE TABLE " + ContactsEntry.TABLE_NAME +
            "(" +
            ContactsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ContactsEntry.COLUMN_NAME + " TEXT NOT NULL, " +
            ContactsEntry.COLUMN_NUMBER + " TEXT NOT NULL);";

    public ContactsDbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("ALTER TABLE " + ContactsEntry.TABLE_NAME + " ADD COLUMN " + ContactsEntry.COLUMN_FAVOURITE + " INTEGER DEFAULT 0");
    }
}
