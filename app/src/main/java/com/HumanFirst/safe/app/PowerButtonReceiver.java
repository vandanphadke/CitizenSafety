package com.HumanFirst.safe.app;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

public class PowerButtonReceiver extends BroadcastReceiver {

    static int countPowerOff = 0 ;
    long lastDown = 0 ;
    long uptime = 0 ;
    private Activity activity = null ;
    public PowerButtonReceiver(Activity activity) {
        this.activity = activity ;
    }

    public PowerButtonReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        Log.v("onReceive", "Power Button is Pressed");
        if (countPowerOff == 0 )
            lastDown = System.currentTimeMillis();


            if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF ) || intent.getAction().equals(Intent.ACTION_SCREEN_ON) )
                countPowerOff++;



            if (countPowerOff == 4) {
                uptime = System.currentTimeMillis();

                if (uptime - lastDown <= 5000)
                    Log.d("Power Button", "Pressed 4 times ");
                    Log.d("Emergency Detected", "Emergency Detected");
                    countPowerOff = 0 ;
            }


            //}
       }
}
