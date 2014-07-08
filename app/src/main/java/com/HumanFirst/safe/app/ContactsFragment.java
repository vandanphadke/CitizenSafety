package com.HumanFirst.safe.app;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ContactsFragment extends ListFragment {

    String[] name;
    String[] phonenumber;

    ContactCustomList adapter;

    ArrayList<Contact> contact_list;
    List<Contact> list ;
    DatabaseHandler db;

    NotificationManager mNotificationManager;

    Button test ;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            Uri uri = data.getData();
            if (uri != null){
                Cursor c = null ;
                try {
                    c = getActivity().getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Phone.TYPE , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}, null, null, null);

                    if (c != null && c.moveToFirst()){
                        String number = c.getString(0);
                        int type = c.getInt(1);
                        String name1 = c.getString(2);
                        Toast.makeText(getActivity(), "Contact added", Toast.LENGTH_LONG).show();


                        //Save contact to database
                        DatabaseHandler db = new DatabaseHandler(getActivity().getApplicationContext());

                        ArrayList<Contact> contact_list = getListData();
                        int id = contact_list.size();
                        id = id + 2 ;
                        db.addContacts(new Contact(id , name1 , number , ""));

                        db.close();
                    }

                }finally {
                    if (c != null)
                        c.close();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_contacts:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, 1);
                return true ;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contacts_list , menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        contact_list = new ArrayList<Contact>();
        contact_list = getListData();

        test = (Button)getActivity().findViewById(R.id.test);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, 1);
            }
        });

        db = new DatabaseHandler(getActivity().getApplicationContext());

        //Clear notification when fragment started
        mNotificationManager = (NotificationManager)getActivity().getApplication() .getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager!=null) {
            mNotificationManager.cancelAll();
        }

        name = new String[contact_list.size()];
        phonenumber = new String[contact_list.size()];

        list = db.getAllContacts();

        int i = 0;

        for ( Contact contact : contact_list){

            name[i] = contact.getName();
            phonenumber[i] = contact.getPhone();
            i++ ;

        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new ContactCustomList(getActivity().getApplicationContext(), name, phonenumber);
        }

        setListAdapter(adapter);
    }

    /*@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contact_list = new ArrayList<Contact>();
        contact_list = getListData();

        test = (Button)getActivity().findViewById(R.id.test);

        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, 1);
            }
        });

        db = new DatabaseHandler(getActivity().getApplicationContext());

        //Clear notification when fragment started
        mNotificationManager = (NotificationManager)getActivity().getApplication() .getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager!=null) {
            mNotificationManager.cancelAll();
        }

        name = new String[contact_list.size()];
        phonenumber = new String[contact_list.size()];

        list = db.getAllContacts();

        int i = 0;

        for ( Contact contact : contact_list){

            name[i] = contact.getName();
            phonenumber[i] = contact.getPhone();
            i++ ;

        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new ContactCustomList(getActivity().getApplicationContext(), name, phonenumber);
        }

        setListAdapter(adapter);

    }*/

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
		return rootView;
	}

    private ArrayList<Contact> getListData() {
        // TODO Auto-generated method stub

        db = new DatabaseHandler(getActivity().getApplicationContext());

        ArrayList<Contact> results = new ArrayList<Contact>();
        results.clear();
        List<Contact> contacts = db.getAllContacts();

        for (Contact cnt : contacts)
            results.add(cnt);

        return results;
    }

}
