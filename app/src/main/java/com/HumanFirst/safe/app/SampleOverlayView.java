package com.HumanFirst.safe.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;


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


        imgb.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("App" , "Button press");
            }
        });

        imgb.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.d("App" , "Button Long press");
                return false;
            }
        });

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
                    gps = new GPSTracker(getContext());

                    // check if GPS enabled
                    if (!gps.canGetLocation()) {
                        // can't get location
                        // GPS or Network is not enabled
                        // Here GPS settings have to be turned on programatically and then the location has to be taken

                    }

                    //Check if the current latitudes and longitudes are obtaines correctly(Non-Zero)
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
                    Log.d("Sharedprefs updated" , "" + latitude + longitude);

                    handler.postDelayed(this , 10000);

                }catch (Exception e){e.printStackTrace();}
            }
        };
        handler.postDelayed(call , 10000);
    }

    protected Handler getThread(){return handler;}

    @Override
    protected void refreshViews() {
/*        Toast.makeText(getContext(), "Waiting", Toast.LENGTH_LONG);
        Log.d("App" , "View refreshed");*/
    }

    @Override
    protected void onTouchEvent_Up(MotionEvent event) {
        /*Toast.makeText(getContext(), "Press", Toast.LENGTH_LONG);
        Log.d("App" , "Up");*/
    }

    @Override
    protected void onTouchEvent_Move(MotionEvent event) {
       /* Toast.makeText(getContext(), "Move", Toast.LENGTH_LONG);
        Log.d("App" , "Move Event");*/
    }

    @Override
    protected void onTouchEvent_Press(MotionEvent event) {

       /* Toast.makeText(getContext() , "Button Press Down" , Toast.LENGTH_LONG);
        Log.d("App" , "Event Press");*/
    }

    @Override
    public boolean onTouchEvent_LongPress() {
        /*Toast.makeText(getContext() , "Long Presses" , Toast.LENGTH_LONG);
        Log.d("App" , "Button Long press");*/
        return true;
    }
}
