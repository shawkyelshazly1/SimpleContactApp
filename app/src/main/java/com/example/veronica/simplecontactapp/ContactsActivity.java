package com.example.veronica.simplecontactapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.veronica.simplecontactapp.Data.ContactsContract;

public class ContactsActivity extends AppCompatActivity {
    FloatingActionButton addContactButton;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                deleteAll();
                return true;
            case R.id.search_contact:
                Intent searchIntent = new Intent(ContactsActivity.this, SearchActivity.class);
                startActivity(searchIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_app);
        addContactButton = findViewById(R.id.add_contact_button);
        ViewPager viewPager = findViewById(R.id.main_viewPager);
        TabLayout tabs = findViewById(R.id.main_tabs);
        CustomViewPagerAdapter adapter = new CustomViewPagerAdapter(this, getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);

        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addActivityIntent = new Intent(getApplicationContext(), AddOrEditContactActivity.class);
                startActivity(addActivityIntent);
            }
        });

    }

    private void deleteAll() {
        int contactsDeleted = getContentResolver().delete(ContactsContract.ContactsEntry.CONTENT_URI, null, null);
        if (contactsDeleted != 0) {
            Toast.makeText(this, R.string.contacts_deleted_toas_msg, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.failed_delete_contacts_toast_msg, Toast.LENGTH_SHORT).show();
        }
    }
}




