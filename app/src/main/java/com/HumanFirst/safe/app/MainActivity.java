package com.HumanFirst.safe.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;


public class MainActivity extends ActionBarActivity{

    Button getgpsloc ;
    Button addcontacts ;
    Button seeContacts ;
    ToggleButton onoff ;


    // GPSTracker class
    GPSTracker gps;

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
                        Toast.makeText(this ,type + ":" + number + ":" + name,Toast.LENGTH_LONG).show();

                        //Save contact to database


                    }

                }finally {
                    if (c != null)
                        c.close();
                }
            }
        }
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
                    startService(new Intent(MainActivity.this, SampleOverlayService.class));

                } else {

                    stopService(new Intent(MainActivity.this, SampleOverlayService.class));
                    //Stop the thread created by the service
                }

            }
        });

        seeContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
