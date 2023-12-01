package com.pinc.android.MB360.onboarding;

import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_EMAIL;
import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_FIRST_TIME_USER;
import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_LOGIN_ID;
import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_PHONE_NUMBER;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.insurance.DashBoardActivity;
import com.pinc.android.MB360.onboarding.authentication.LoginActivity;
import com.pinc.android.MB360.onboarding.walkthrough.WalkThroughActivity;
import com.pinc.android.MB360.utilities.AesEncryption;
import com.pinc.android.MB360.utilities.AppLocalConstant;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.utilities.ResponseException;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.nio.charset.StandardCharsets;

@SuppressLint("CustomSplashScreen")

/** @splash-screen google has new custom api for splash screen
we can update this thing later **/

public class SplashScreenActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String LOGIN_TYPE = AUTH_PHONE_NUMBER;

    //update related variables
    Task<AppUpdateInfo> appUpdateInfoTask;
    AppUpdateManager appUpdateManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        // Handle the splash screen transition.
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /**check for new updates for the app from play store **/
        checkForUpdates();


/** we must check for account authentication available locally
 *  so that we can call @loadSession api for downloading updated content
 * **/

        /** if number is present in @getExistingAccount() then we have to navigate to homepage after load sessions are being called
         if number is not present we have to navigate to login page **/
        //if user comes for the first time
        if (firstTimeUser()) {
            Intent walkThroughIntent = new Intent(this, WalkThroughActivity.class);
            startActivity(walkThroughIntent);
        } else if (getExistingAccount() != null) {
            splashScreen.setKeepOnScreenCondition(() -> true);
            Intent dashboardIntent = new Intent(this, DashBoardActivity.class);
            startActivity(dashboardIntent);
        } else {
            //Navigate to login page (LOGIN-PAGE)
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }

        finish();
    }

    private boolean firstTimeUser() {
        sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(AUTH_FIRST_TIME_USER, true);
    }

    private void checkForUpdates() {
        //this function check the updates and throws the download activity
        appUpdateManager = AppUpdateManagerFactory.create(this);

        // Returns an intent object that you use to check for an update.
        appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            Log.d(LogTags.UPDATE, "CHECKING FOR UPDATE.....");

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE// This applies an immediate update
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.IMMEDIATE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            AppLocalConstant.UPDATE_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                    FirebaseCrashlytics crashlytics;
                    crashlytics = FirebaseCrashlytics.getInstance();
                    ResponseException exception = new ResponseException(LogTags.UPDATE + " : " + " | Message " + e.getLocalizedMessage());
                    Throwable throwable = new Throwable(exception);
                    crashlytics.recordException(throwable);

                }
            }
        }).addOnFailureListener(e -> {
            getExistingAccount();
        });


    }

    //this will return the number if number is already logged-in once!
    //since we don't have any authentication locally.
    private String getExistingAccount() {
        sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String phone_number = sharedPreferences.getString(AUTH_PHONE_NUMBER, null);
        String email = sharedPreferences.getString(AUTH_EMAIL, null);
        String loginid = sharedPreferences.getString(AUTH_LOGIN_ID, null);
        if (phone_number != null) {
            LOGIN_TYPE = AUTH_PHONE_NUMBER;
            return phone_number;
        } else if (email != null) {
            LOGIN_TYPE = AUTH_EMAIL;
            return email;
        } else if (loginid != null) {
            LOGIN_TYPE = AUTH_LOGIN_ID;
            return loginid;
        } else {
            return null;
        }
    }

    @Override
    protected void onStop() {
        if (appUpdateManager != null) {
            //   appUpdateManager.unregisterListener(installStateUpdatedListener);
            super.onStop();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // Or 'AppUpdateType.FLEXIBLE' for flexible updates.
                            AppUpdateType.IMMEDIATE,
                            // The current activity making the update request.
                            this,
                            // Include a request code to later monitor this update request.
                            AppLocalConstant.UPDATE_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppLocalConstant.UPDATE_CODE) {
            if (resultCode != RESULT_OK) {
                Log.d(LogTags.UPDATE, "onActivityResult: Update flow failed! Result code: " + resultCode);
                // If the update is cancelled or fails,
                // you can request to start the update again.
                finish();
            }
        }
    }
}