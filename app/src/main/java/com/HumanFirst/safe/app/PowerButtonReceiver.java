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

                if (uptime - lastDown <= 5000) {
                    countPowerOff = 0;
                    Intent i = new Intent(context, SOSActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                    context.stopService(new Intent(context, BackgroundService.class));
                }

                else {countPowerOff = 0;}
            }



       }
}
