package com.pinc.android.MB360.networkmanager;

import android.app.Application;


public class NetworkMonitoringApplication extends Application {

    public NetworkMonitoringUtil mNetworkMonitoringUtil;

    @Override
    public void onCreate() {
        super.onCreate();

        mNetworkMonitoringUtil = new NetworkMonitoringUtil(getApplicationContext());
        mNetworkMonitoringUtil.checkNetworkState();
        mNetworkMonitoringUtil.registerNetworkCallbackEvents();
    }
}
