package com.HumanFirst.safe.app;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class BackgroundService extends Service {
    public BackgroundService() {
    }

    // GPSTracker class
    GPSTracker gps;

    PowerButtonReceiver mReceiver ;

    Handler handler;

    SharedPreferences.Editor editor ;

    @Override
    public void onCreate() {
        super.onCreate();

        //Register the BroadCast receiver for detecting power button presses
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new PowerButtonReceiver();
        registerReceiver(mReceiver , filter);

        //Getting the gps ccordinates
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("GPS-Coordinates" , Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        gps = new GPSTracker(getApplicationContext());

        if (!gps.canGetLocation()) {
            // can't get location
            // GPS or Network is not enabled
            // Here GPS settings have to be turned on programatically and then the location has to be taken
            //No permission to enable GPS by 3rd party apps
            //So prompt user while starting the service to turn the GPS on
            gps.showSettingsAlert();


        }


        handler = new Handler();
        handler.postDelayed(call , 10000);
    }

    final Runnable call = new Runnable() {
        @Override
        public void run() {
            try {
                //Code to update GPS Position after regular intervals of time
                //Also call the same handler here
                Log.v("Updating Coordinates", "Updating Coordinates");
                // create class object
                gps = new GPSTracker(getApplicationContext());

                // check if GPS enabled
                if (!gps.canGetLocation()) {
                    // can't get location
                    // GPS or Network is not enabled
                    // Here GPS settings have to be turned on programatically and then the location has to be taken
                    //No permission to enable GPS by 3rd party apps
                    //So prompt user while starting the service to turn the GPS on
                    gps.showSettingsAlert();

                }

                //Check if the current latitudes and longitudes are obtained correctly(Non-Zero)
                double latitude = 0 ;
                double longitude = 0 ;

                while (latitude == 0 && longitude == 0 ) {

                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    Log.d("App" , "Still in loop");

                }
                //Write these values in the sharedpreferences
                editor.putFloat("GPS-lat" , (float) latitude);
                editor.putFloat("GPS-long" , (float) longitude);
                Toast.makeText(getApplicationContext(), latitude + longitude + "", Toast.LENGTH_SHORT).show();
                Log.d("Sharedprefs updated" , "" + latitude + longitude);

                handler.postDelayed(this , 10000);

            }catch (Exception e){e.printStackTrace();}
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        handler.removeCallbacks(call);
    }
}
