package com.HumanFirst.safe.app;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
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
public class SOSActivity extends FragmentActivity implements LocationListener {

    Handler handler;

    GPSTracker gpsTracker;

    Location gpslocation = null;

    LocationManager locMan;


    private static final int GPS_TIME_INTERVAL = 60000; // get gps location every 1 min

    private static final int GPS_DISTANCE = 1000; // set the distance value in meter

    /*
       for frequently getting current position then above object value set to 0 for both you will get continues location but it drown the battery
    */


    LocationManager locationManager;
    double latitude;
    double longitude;

    String MapUrl;
    List<Contact> sendContacts;
    EditText tvtest;

    private Window wind;

    DatabaseHandler db;

    Location mCurrentLocation;

    LocationManager lm ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        //Unlock this phone and bring activity to top of window
        /*wind = this.getWindow();
        wind.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        wind.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        wind.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);*/

        tvtest = (EditText) findViewById(R.id.tvtest);

        gpsTracker = new GPSTracker(getApplicationContext(), 1);
        db = new DatabaseHandler(getApplicationContext());

        //Ask if user has given SOS Signal
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, GPS_TIME_INTERVAL, GPS_TIME_INTERVAL, this);


        //Populate contacts list
        sendContacts = db.getAllContacts();

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        //Generated Google Maps URl
        MapUrl = "https://maps.google.com/maps?q=" + latitude + "," + longitude;

        //Toast here for testing
        Toast.makeText(getApplicationContext(), MapUrl, Toast.LENGTH_LONG).show();
        //Send Text Message here
        tvtest.setText(MapUrl);

        Log.d("1", "Reached here");
        handler = new Handler();
        //handler.postDelayed(call , 120000);
        handler.postDelayed(call, 5000);
        Log.d("2", "Reached here");

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
            Log.d("3", "Reached here");
            double[] loc = new double[2];

            //Code for getting GPS location fast


            Log.d("Location", "Long:" + loc[1] + "Lat:" + loc[0]);
            Toast.makeText(getApplicationContext(), "Long:" + loc[1] + "Lat:" + loc[0], Toast.LENGTH_SHORT).show();

            for (Contact c : sendContacts) {
                //Loop through all the contacts


                //Write the code to send a message and a mail after every 10 minutes
                /*SmsManager smsManager = SmsManager.getDefault();

                smsManager.sendTextMessage("Phone numbers here", null, "I am in danger , \n Click to see my location on google maps " +
                            "\n" +
                            MapUrl, null, null);*/


                //handler.postDelayed(this , 120000);


            }

            handler.postDelayed(call, 5000);
        }
    };


    @Override
    public void onLocationChanged(Location location) {

        tvtest.setText(location.getLatitude() + " ADBDS" + location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

