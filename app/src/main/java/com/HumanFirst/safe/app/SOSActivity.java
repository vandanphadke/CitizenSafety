package com.HumanFirst.safe.app;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*

  This activity will be called only if an emergency is detected .
  Sound a shrill alarm in Oncreate
  Take the GPS coordinates of the user in 5mins each and send them by constructing a
  Google Maps link to the guardian contacts

*/
public class SOSActivity extends ActionBarActivity {

    Handler handler;

    GPSTracker gpsTracker ;

    double latitude;
    double longitude;

    String MapUrl ;
    List<Contact> sendContacts ;
    EditText tvtest ;

    DatabaseHandler db ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        tvtest = (EditText)findViewById(R.id.tvtest);

        gpsTracker = new GPSTracker(getApplicationContext() , 1);
        db = new DatabaseHandler(getApplicationContext());

        //Populate contacts list
        sendContacts  = db.getAllContacts();

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        //Generated Google Maps URl
        MapUrl = "https://maps.google.com/maps?q=" + latitude + "," + longitude;

        //Toast here for testing
        Toast.makeText(getApplicationContext() , MapUrl , Toast.LENGTH_LONG).show();
        //Send Text Message here
        tvtest.setText(MapUrl);


        handler = new Handler();
        handler.postDelayed(call , 10000);

        //Send first text message by generating a google map link

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(call);
    }

    final Runnable call = new Runnable() {
        @Override
        public void run() {
            try {

                for (Contact c : sendContacts) {
                    //Loop through all the contacts

                    //Write the code to send a message and a mail after every 10 minutes
                    /*SmsManager smsManager = SmsManager.getDefault();

                    smsManager.sendTextMessage("Phone numbers here", null, "I am in danger , \n Click to see my location on google maps " +
                            "\n" +
                            MapUrl, null, null);*/

                }
                handler.postDelayed(this , 10000);

            }catch (Exception e){e.printStackTrace();}
        }
    };
}
