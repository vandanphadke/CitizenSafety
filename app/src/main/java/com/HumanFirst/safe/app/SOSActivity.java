package com.HumanFirst.safe.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import java.util.Timer;
import java.util.TimerTask;


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


    private static final int GPS_TIME_INTERVAL = 1000; // get gps location every 1 sec

    private static final int GPS_DISTANCE = 100; // set the distance value in meter

    /*
       for frequently getting current position then above object value set to 0 for both you will get continues location but it drown the battery
    */


    LocationManager locationManager;
    double latitude;
    double longitude;

    String MapUrl;
    List<Contact> sendContacts;

    private Window wind;

    DatabaseHandler db;

    Location mCurrentLocation;

    LocationManager lm ;

    SharedPreferences.Editor editor ;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        gpsTracker = new GPSTracker(this, 1);
        db = new DatabaseHandler(this);

        //Ask if user has given SOS Signal
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,GPS_TIME_INTERVAL,GPS_DISTANCE,this);

        //SharedPreferences having the GPS coordinates

        sharedPreferences = getApplicationContext().getSharedPreferences("GPS-Coordinates" , Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //First open an alertdialogue to notify whether the SOS has been raised or not
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set title
        alertDialogBuilder.setTitle("SOS Signal Detected!!!");

        // set dialog message
        alertDialogBuilder
                .setMessage("Click yes to send Messages or no to cancel!")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing

                        dialog.cancel();
                        finish();
                    }
                });

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (alertDialog.isShowing())
                    alertDialog.dismiss();
            }
        },12000);



        //Populate contacts list
        sendContacts = db.getAllContacts();

        latitude = gpsTracker.getLatitude();
        longitude = gpsTracker.getLongitude();

        //Generated Google Maps URl
        MapUrl = "https://maps.google.com/maps?q=" + latitude + "," + longitude;

        //Toast here for testing
        Toast.makeText(getApplicationContext(), MapUrl, Toast.LENGTH_LONG).show();

        handler = new Handler();

        handler.postDelayed(call , 120000);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(call);
    }

    final Runnable call = new Runnable() {
        @Override
        public void run() {

            //The location will be continuously updated in the shared proferences
            //So fetch the location from there only

            //Get the lat and long stored in shared prefs
            float latitude = sharedPreferences.getFloat("GPS-lat",0);
            float longitude = sharedPreferences.getFloat("GPS-long",0);


            Toast.makeText(getApplicationContext(), "Lat:" + latitude + "Long:" + longitude , Toast.LENGTH_SHORT).show();

            for (Contact c : sendContacts) {
                //Loop through all the contacts

                //Write the code to send a message and a mail after every 10 minutes
                //Fetch the locations from the shared preferences
                SmsManager smsManager = SmsManager.getDefault();

                //Generated Google Maps URl
                MapUrl = "https://maps.google.com/maps?q=" + latitude + "," + longitude ;


                String message = "I am in Danger , Click the below link to see my location on the mao.\n" + MapUrl;

                Log.d("Final Message",message);
                smsManager.sendTextMessage("Phone numbers here", null, "I am in danger , \n Click to see my location on google maps " +
                        "\n" +  MapUrl,null,null);


            }

            handler.postDelayed(call, 120000);
        }
    };


    @Override
    public void onLocationChanged(Location location) {

        if (location.getLatitude() != 0 && location.getLongitude() != 0) {
            //Write the GPS values  in the sharedpreferences

            editor.putFloat("GPS-lat", (float) location.getLatitude());
            editor.putFloat("GPS-long", (float) location.getLongitude());
            editor.commit();

        }
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

