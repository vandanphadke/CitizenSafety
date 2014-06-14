package com.HumanFirst.safe.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by Vandan on 10-06-2014.
 */
public class SampleOverlayView extends OverlayView{
    private ImageButton imgb ;
    long lastDown  = 0 ;
    long lastDuration = 0 ;

    public SampleOverlayView(OverlayService service) {
        super(service, R.layout.overlay, 1);
    }

    public int getGravity() {
        return Gravity.TOP + Gravity.RIGHT;
    }

    @Override
    protected void onInflateView() {
        imgb = (ImageButton) this.findViewById(R.id.imgbutton);

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

    }

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
