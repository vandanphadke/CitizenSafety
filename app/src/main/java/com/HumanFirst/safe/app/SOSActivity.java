package com.HumanFirst.safe.app;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

    EditText tvtest ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sos);

        tvtest = (EditText)findViewById(R.id.tvtest);

        gpsTracker = new GPSTracker(getApplicationContext() , 1);

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
                //Write the code to send a message and a mail after every 10 minutes




                handler.postDelayed(this , 10000);

            }catch (Exception e){e.printStackTrace();}
        }
    };
}
