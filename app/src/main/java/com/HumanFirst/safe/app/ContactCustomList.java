package com.HumanFirst.safe.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Vandan on 20-06-2014.
 */
public class ContactCustomList extends BaseAdapter {

        Context context;
        String name[];
        String phone_number[];

        private LayoutInflater layoutInflater;


        public ContactCustomList(Context context, String[] name, String[] phone_number) {
            super();
            this.context = context ;
            this.name = name ;
            this.phone_number = phone_number ;
            layoutInflater = LayoutInflater.from(context);

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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;


            if (convertView == null) {
                //Add the name of the custom listview
                convertView = layoutInflater.inflate(R.layout.single_list_updates, null);
                holder = new ViewHolder();

                //Instantiate four textviews
                holder.txtViewName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.txtViewPhone = (TextView) convertView.findViewById(R.id.tv_phonenumber);
                convertView.setTag(holder);
            }

            else
            {

                holder = (ViewHolder) convertView.getTag();

            }

            holder.txtViewName.setText(name[position]);
            holder.txtViewPhone.setText(phone_number[position]);

            return convertView;
        }

        private class ViewHolder {
            TextView txtViewName;
            TextView txtViewPhone;
        }


}
