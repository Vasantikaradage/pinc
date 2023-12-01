package com.pinc.android.MB360.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;


import com.pinc.android.MB360.BuildConfig;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AppLocalConstant {


    /**
     * in-app update config
     */
    public static final int UPDATE_CODE = 1806;

    /**
     * Comma separated Pattern.
     */
    public static final String NUMBER_FORMAT = "##,##,##0";

    /**
     * all the Strings are considered as keys for sharedPreferences
     **/
    //key for login
    public static String AUTH_PHONE_NUMBER = "AUTH_PHONE_NUMBER";
    public static String AUTH_EMAIL = "AUTH_EMAIL";
    public static String AUTH_LOGIN_ID = "AUTH_LOGIN_ID";
    public static String AUTH_FIRST_TIME_USER = "AUTH_FIRST_TIME_USER";
    public  static  String USER_AGREEMENT="USER_AGREEMENT";



    /* BASIC KEYS FOR CHECK */
    public static String AUTH_LOGIN_TYPE = "AUTH_LOGIN_TYPE";

    /* MAPS KEY */
    public static final String MAPS_KEY = BuildConfig.MAPS_API_KEY_V3;


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    //DATE OF BIRTH CONSTANT
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    //DATE OF BIRTH CONSTANT (DIGITS MONTH)
    public static final SimpleDateFormat DATE_FORMAT_DIGIT_MONTH = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);


    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //location permissions
    private static final int REQUEST_LOCATION = 2;

    private static String[] PERMISSIONS_LOCATION = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    /**
     * Checks if the app has permission to write to device storage
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


    /**
     * Checks if the app has permission to write to Location service
     * <p>
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */

    public static void verifyLocationPermissions(Activity activity) {
        // Check if we have location permissions
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION);
        int fine_permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);

        if (permission != PackageManager.PERMISSION_GRANTED || fine_permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_LOCATION,
                    REQUEST_LOCATION
            );
        }
    }


    public static void askDefaultPermissions(Activity activity) {
        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
        };

        ActivityCompat.requestPermissions(
                activity,
                PERMISSIONS,
                100
        );

    }
}

