package com.HumanFirst.safe.app;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vandan on 20-06-2014.
 */
public class ContactCustomList extends BaseAdapter {

        Context context;
        String name[];
        String phone_number[];

        private LayoutInflater layoutInflater;

        DatabaseHandler db;


        public ContactCustomList(Context context, String[] name, String[] phone_number) {
            super();
            this.context = context ;
            this.name = name ;
            this.phone_number = phone_number ;
            layoutInflater = LayoutInflater.from(context);
            db = new DatabaseHandler(context);

        }
        @Override
        public int getCount() {
            return name.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;




            if (convertView == null) {
                //Add the name of the custom listview
                convertView = layoutInflater.inflate(R.layout.single_list_contacts, null);
                holder = new ViewHolder();

                //Instantiate four textviews
                holder.txtViewName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.txtViewPhone = (TextView) convertView.findViewById(R.id.tv_phonenumber);
                holder.delButton = (ImageButton) convertView.findViewById(R.id.delButton);
                convertView.setTag(holder);
            }

            else
            {

                holder = (ViewHolder) convertView.getTag();

            }

            holder.txtViewName.setText(name[position]);
            holder.txtViewPhone.setText(phone_number[position]);


            holder.delButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Position clicked " , "" + position);
                    String name1 = name[position];
                   

                    List<Contact> allContacts = db.getAllContacts();

                    for (Contact c : allContacts)
                    {
                        if (c.getName().equals(name1))
                            db.deleteContact(c);
                    }

                    allContacts = db.getAllContacts();

                    int i = 0 ;
                    name = new String[allContacts.size()];
                    phone_number = new String[allContacts.size()];

                    for (Contact c : allContacts)
                    {
                        name[i] = c.getName() ;
                        phone_number[i] = c.getPhone();
                        i++ ;
                    }

                    notifyDataSetChanged();
                }
            });

            return convertView;
        }

        private class ViewHolder {
            TextView txtViewName;
            TextView txtViewPhone;
            ImageButton delButton ;
        }

    private ArrayList<Contact> getListData() {
        // TODO Auto-generated method stub

        db = new DatabaseHandler(context);

        ArrayList<Contact> results = new ArrayList<Contact>();
        results.clear();
        List<Contact> contacts = db.getAllContacts();

        for (Contact cnt : contacts)
            results.add(cnt);

        return results;
    }


}
