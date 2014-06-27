package com.HumanFirst.safe.app;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity{

    Button getgpsloc ;
    Button addcontacts ;
    Button seeContacts ;
    ToggleButton onoff ;
    DatabaseHandler db ;

    // GPSTracker class
    GPSTracker gps;

    private SensorManager mSensorManager;

    private ShakeEventListener mSensorListener;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            Uri uri = data.getData();
            if (uri != null){
                Cursor c = null ;
                try {
                    c = getContentResolver().query(uri, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,
                            ContactsContract.CommonDataKinds.Phone.TYPE , ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}, null, null, null);

                    if (c != null && c.moveToFirst()){
                        String number = c.getString(0);
                        int type = c.getInt(1);
                        String name = c.getString(2);
                        Toast.makeText(this ,"Contact added",Toast.LENGTH_LONG).show();


                        //Save contact to database
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                        ArrayList<Contact> contact_list = getListData();
                        int id = contact_list.size();
                        id = id + 1 ;
                        db.addContacts(new Contact(id , name , number , ""));
                        db.close();


                    }

                }finally {
                    if (c != null)
                        c.close();
                }
            }
        }
    }

    PowerButtonReceiver mReceiver ;

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getgpsloc = (Button) findViewById(R.id.getgpsloc);
        onoff = (ToggleButton) findViewById(R.id.onoffbutton);
        addcontacts = (Button)findViewById(R.id.addcontacts);
        seeContacts = (Button)findViewById(R.id.seeContacts);


        addcontacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                startActivityForResult(intent, 1);
            }
        });

        getgpsloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("GPS", "Getting coordinates");
                // create class object
                gps = new GPSTracker(MainActivity.this);

                // check if GPS enabled
                if (gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }
            }
        });

        onoff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Code for starting background service
                    Log.d("Service", "Starting Service");
                    //startService(new Intent(MainActivity.this, SampleOverlayService.class));
                    /*SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("OnOFF" , Context.MODE_PRIVATE);

                    final SharedPreferences.Editor editor = sharedPreferences.edit();
                    //Write these values in the sharedpreferences
                    editor.putBoolean("Onoff" , true);*/
                    /*IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
                    filter.addAction(Intent.ACTION_SCREEN_OFF);
                    mReceiver = new PowerButtonReceiver(MainActivity.this);
                    registerReceiver(mReceiver , filter);*/
                    startService(new Intent(MainActivity.this, BackgroundService.class));



                } else {

                    //stopService(new Intent(MainActivity.this, SampleOverlayService.class));
                    //Stop the thread created by the service
                    /*SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("OnOFF" , Context.MODE_PRIVATE);
                    final SharedPreferences.Editor editor = sharedPreferences.edit();
                    //Write these values in the sharedpreferences
                    editor.putBoolean("Onoff" , false);*/
                    stopService(new Intent(MainActivity.this, BackgroundService.class));
                }

            }
        });

        seeContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this , ContactActivity.class);
                startActivity(intent);

            }
        });

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                Toast.makeText(MainActivity.this, "Shake!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ArrayList<Contact> getListData() {
        // TODO Auto-generated method stub

        db = new DatabaseHandler(getApplicationContext());

        ArrayList<Contact> results = new ArrayList<Contact>();
        results.clear();
        List<Contact> contacts = db.getAllContacts();

        for (Contact cnt : contacts)
            results.add(cnt);

        return results;
    }
}
