package com.HumanFirst.safe.app;

import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.HumanFirst.safe.app.dummy.DummyContent;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the
 * interface.
 */
public class ContactFragment extends ListFragment {

    String[] name;
    String[] phonenumber;

    ContactCustomList adapter;

    ArrayList<Contact> contact_list;
    DatabaseHandler db;


    NotificationManager mNotificationManager;
    public ContactFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_updates, container,
                false);

        return rootView;

    }





    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        update_list = getListData();

        db = new DatabaseHandler(getActivity());

        //Clear notification when fragment started
        mNotificationManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager!=null) {
            mNotificationManager.cancelAll();
        }
        name = new String[update_list.size()];
        timestamp = new String[update_list.size()];
        head = new String[update_list.size()];
        body = new String[update_list.size()];

        int i = 0;

        event_list = db.getAllEvents();

        workshop_list =  db.getAllWorkshops();

        for ( UpdateObject update : update_list){

            if(update.getType().equals("1") || update.getType().equals("0")){
                for( EventObject e : event_list){

                    if( Integer.parseInt(update.getType()) == 0){
                        name[i] = "General Update";

                    }

                    else
                    {
                        if( e.getID() == Integer.parseInt(update.getEvent_Id())){
                            name[i] = e.getName();
                        }


                    }
                }
            }
            if(update.getType().equals("2")){
                for(WorkshopObject w : workshop_list){
                    if(Integer.parseInt(update.getEvent_Id()) == w.getID()){

                        name[i] = w.getName_of_Workshop() ;

                    }


                }


            }





            timestamp[i] = update.getTimeStamp();
            head[i] = update.getHead();
            body[i] = update.getBody();
            i++ ;

        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        } else {
            adapter = new UpdateCustomList(getActivity(), name, timestamp,
                    head, body);
        }
        ListView lv = (ListView)getActivity().findViewById(android.R.id.list);
        lv.setEmptyView(getActivity().findViewById(android.R.id.empty));
        setListAdapter(adapter);

    }


    private ArrayList<Contact> getListData() {
        // TODO Auto-generated method stub

        db = new DatabaseHandler(getActivity());

        ArrayList<UpdateObject> results = new ArrayList<UpdateObject>();
        results.clear();
        List<UpdateObject> updates = db.getAllUpdates();

        for (UpdateObject up : updates)
            results.add(up);

        return results;
    }



}
