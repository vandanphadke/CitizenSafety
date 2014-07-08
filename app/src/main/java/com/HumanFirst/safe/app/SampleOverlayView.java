package com.HumanFirst.safe.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;


/**
 * Created by Vandan on 10-06-2014.
 */
public class SampleOverlayView extends OverlayView{
    private ImageButton imgb ;
    long lastDown  = 0 ;
    long lastDuration = 0 ;
    Handler handler ;
    Runnable call ;

    // GPSTracker class
    GPSTracker gps;

    public SampleOverlayView(OverlayService service) {
        super(service, R.layout.overlay, 1);
    }

    public int getGravity() {
        return Gravity.TOP + Gravity.RIGHT;
    }


    @Override
    protected void onInflateView() {
        imgb = (ImageButton) this.findViewById(R.id.imgbutton);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("GPS-Coordinates" , Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        //For service to be awake on locked phone
        //Acquire a partial wake lock

        PowerManager powerManager = (PowerManager)getContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK , "MyWakeLock");
        wakeLock.acquire();

        imgb.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN)
                    lastDown = System.currentTimeMillis();

                else if (event.getAction() == MotionEvent.ACTION_UP)
                    lastDuration = System.currentTimeMillis();

                if(lastDuration - lastDown > 5000 ) {
                    Log.d("EMERGENCY DETECTED", "EMERGENCY DETECTED");
                    //GO to the activity which keeps sendng text messages to selected members

                }
                Log.d("Time" , "" + lastDown + lastDuration);
                return true;
            }
        });

        final Handler handler = new Handler();
        final Runnable call = new Runnable() {
            @Override
            public void run() {
                try {
                    //Code to update GPS Position after regular intervals of time
                    //Also call the same handler here
                    Log.v("Updating Coordinates" , "Updating Coordinates");
                    // create class object
                    gps = new GPSTracker(getContext() , 0);

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
                    Toast.makeText(getContext() , latitude + longitude + "" , Toast.LENGTH_SHORT).show();
                    Log.d("Sharedprefs updated" , "" + latitude + longitude);

                    handler.postDelayed(this , 10000);

                }catch (Exception e){e.printStackTrace();}
            }
        };
        handler.postDelayed(call , 10000);
    }
}
