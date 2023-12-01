package com.pinc.android.MB360.wellness.homehealthcare.locationservice;

import android.location.Address;

import java.util.List;

public interface OnGeocoderFinishedListener {
    public abstract void onFinished(List<Address> results);
}
