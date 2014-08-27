package com.HumanFirst.safe.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class ScreamFragment extends Fragment {

    ToggleButton startApp ;
    boolean isGpsOn ;

    // Declaring a Location Manager
    protected LocationManager locationManager;

    GPSTracker gpsTracker ;

    /*ProgressDialog progressDialog ;*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().stopService(new Intent(getActivity() , BackgroundService.class));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        startApp = (ToggleButton)  getActivity().findViewById(R.id.startApp);

        locationManager = (LocationManager) getActivity().getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        isGpsOn = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);

        gpsTracker = new GPSTracker(getActivity().getApplicationContext() , 0);

        startApp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    isGpsOn = locationManager
                            .isProviderEnabled(LocationManager.GPS_PROVIDER);

                    if (isGpsOn) {
                        getActivity().startService(new Intent(getActivity(), BackgroundService.class));
                    }
                    else {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        getActivity().startActivity(intent);
                        startApp.setChecked(false);
                    }
                }
                else {

                    getActivity().stopService(new Intent(getActivity() , BackgroundService.class));
                }
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        startApp = (ToggleButton)  getActivity().findViewById(R.id.startApp);
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_scream, container, false);
		
		return rootView;
	}
}
