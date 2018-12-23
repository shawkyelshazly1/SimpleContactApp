package com.example.veronica.simplecontactapp.Data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.veronica.simplecontactapp.Data.ContactsContract.ContactsEntry;
import com.example.veronica.simplecontactapp.R;

public class CustomCursorAdapter extends CursorAdapter {
    public CustomCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.contact_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameView = view.findViewById(R.id.name_textView);
        TextView numberView = view.findViewById(R.id.number_textView);

        nameView.setText(cursor.getString(cursor.getColumnIndex(ContactsEntry.COLUMN_NAME)));
        numberView.setText(cursor.getString(cursor.getColumnIndex(ContactsEntry.COLUMN_NUMBER)));


    }
}
