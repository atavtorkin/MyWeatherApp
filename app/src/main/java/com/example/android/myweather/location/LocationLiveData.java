package com.example.android.myweather.location;

import android.annotation.SuppressLint;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

/**
 * Created by av.tavtorkin
 * On 17.02.2018.
 */

public class LocationLiveData extends LiveData<Location> {

    private Context mContext;

    private FusedLocationProviderClient mClient;
    private LocationCallback mCallback;

    public LocationLiveData(Context context) {
        mContext = context;
    }

    /**
     * появился хотя бы один активный обзервер
     */
    @SuppressLint("MissingPermission")
    @Override
    protected void onActive() {
        super.onActive();

        Log.d("TEST", "Start to receive location updates");

        mClient = LocationServices.getFusedLocationProviderClient(mContext);

        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setFastestInterval(1000);
        request.setInterval(1000);

        mCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.d("TEST", "Received new location");
                super.onLocationResult(locationResult);
                postValue(locationResult.getLastLocation());
                mClient.removeLocationUpdates(mCallback);
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                Log.d("TEST", "Changed location availability " + (locationAvailability.isLocationAvailable() ? "true" : "false"));
            }
        };

        mClient.requestLocationUpdates(request, mCallback, null);
    }

    /**
     * больше нет ни одного активного обзервера
     */
    @Override
    protected void onInactive() {
        super.onInactive();
        Log.d("TEST", "Stop receive location updates");
        mClient.removeLocationUpdates(mCallback);
    }
}
