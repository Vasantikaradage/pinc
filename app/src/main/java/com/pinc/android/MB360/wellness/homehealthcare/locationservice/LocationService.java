package com.pinc.android.MB360.wellness.homehealthcare.locationservice;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.pinc.android.MB360.utilities.AppLocalConstant;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;
import java.util.Locale;

/**
 * @apiNote {@link LocationService }
 * will return users location especially city.
 **/
public class LocationService {
    String city = null;
    private FusedLocationProviderClient mFusedLocationClient;
    Context context;
    Activity activity;

    public LocationService(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public String getCity() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        final Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            mFusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                Log.d("city", "getCity: " + location.toString());
                if (location != null) {
                    try {
                        List<Address> latlng = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        city = latlng.get(0).getLocality();
                        Log.i("city", "onSuccess: " + city);
                        Toast.makeText(context, "City :" + city, Toast.LENGTH_SHORT).show();


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.i("city", "onSuccess: " + e.toString());
                    }
                }
            }).addOnFailureListener(e -> Toast.makeText(context, "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show());

        } else {
            AppLocalConstant.verifyLocationPermissions(activity);
        }
        return city;
    }


}
