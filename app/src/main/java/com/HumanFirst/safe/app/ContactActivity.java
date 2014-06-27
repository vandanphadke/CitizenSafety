package com.HumanFirst.safe.app;

import android.app.ListActivity;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class ContactActivity extends ListActivity {

    String[] name;
    String[] phonenumber;

    ContactCustomList adapter;

    ArrayList<Contact> contact_list;
    List<Contact> list ;
    DatabaseHandler db;

    NotificationManager mNotificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        contact_list = getListData();

        db = new DatabaseHandler(getApplicationContext());

        //Clear notification when fragment started
        mNotificationManager = (NotificationManager)getApplication().getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager!=null) {
            mNotificationManager.cancelAll();
        }

        name = new String[contact_list.size()];
        phonenumber = new String[contact_list.size()];

        int i = 0;

        list = db.getAllContacts();

        for ( Contact contact : contact_list){

            name[i] = contact.getName();
            phonenumber[i] = contact.getPhone();
            i++ ;

        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new ContactCustomList(getApplicationContext(), name, phonenumber);
        }

        setListAdapter(adapter);
    }


    private ArrayList<Contact> getListData() {
        // TODO Auto-generated method stub

        db = new DatabaseHandler(getApplicationContext());

        ArrayList<Contact> results = new ArrayList<Contact>();
        results.clear();
        List<Contact> contacts = db.getAllContacts();

        for (Contact cnt : contacts)
            results.add(cnt);

        return results;
    }
}
