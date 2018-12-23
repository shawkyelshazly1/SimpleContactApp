package com.example.veronica.simplecontactapp.Data;

import android.net.Uri;
import android.provider.BaseColumns;

public class ContactsContract {

    public static final String CONTENT_AUTHORITY = "com.example.veronica.simplecontactapp";
    public static final String CONTACTS_PATH = "contacts";
    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static abstract class ContactsEntry implements BaseColumns {
        public static final String TABLE_NAME = "contacts";
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, CONTACTS_PATH);
        public static final String _ID = "_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_FAVOURITE = "favourite";
        public static final int FAVOURITE = 1;
        public static final int NOT_FAVOURITE = 0;
    }

}
