package com.pinc.android.MB360.insurance.repository;


import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.pinc.android.MB360.database.AppDatabase;
import com.pinc.android.MB360.database.dao.LoadSessionDao;
import com.pinc.android.MB360.insurance.repository.responseclass.LoadSessionResponse;
import com.pinc.android.MB360.insurance.repository.retrofit.LoadSessionRetrofitClient;
import com.pinc.android.MB360.insurance.repository.retrofit.LoadSessionRetrofitClientV2;
import com.pinc.android.MB360.utilities.LogTags;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * this class has the
 * business logic for Loading sessions
 * in users
 **/
public class LoadSessionRepository {
    private final MutableLiveData<LoadSessionResponse> loadSessionResponseMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    public final MutableLiveData<Boolean> reloginState;
    private final int MAX_RETRY_CALL = 3;
    private int RETRIES = 1;

    Application application;
    AppDatabase appDatabase;
    LoadSessionDao loadSessionDao;


    public LoadSessionRepository(Application application) {
        this.loadSessionResponseMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();
        this.application = application;
        this.reloginState = new MutableLiveData<>(false);
        appDatabase = AppDatabase.getInstance(application);
        loadSessionDao = appDatabase.loadSessionDao();
    }

    /**
     * this function  has the business logic for calling
     * and parsing the @load-sessions response
     **/

    //with phone number
    public MutableLiveData<LoadSessionResponse> loadSessionWithPhoneNumber(String mobileNumber) {

        Call<LoadSessionResponse> loadSessionCall = LoadSessionRetrofitClient.getInstance().getLoadSessionApi().loadSessionWithPhoneNumber(mobileNumber);
        loadingState.setValue(true);
        loadSessionCall.enqueue(new Callback<LoadSessionResponse>() {
            @Override
            public void onResponse(Call<LoadSessionResponse> call, Response<LoadSessionResponse> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.LOAD_SESSIONS, "onResponse: " + response.body());
                        loadSessionResponseMutableLiveData.setValue(response.body());
                        loadSessionDao.insertLoadSession(response.body());

                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.LOAD_SESSIONS, "ERROR: ", e);
                        loadSessionResponseMutableLiveData.setValue(null);
                        reloginState.setValue(true);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    Log.d(LogTags.LOAD_SESSIONS, "LoadSessionWithPhoneNumber : ERROR CODE " + response.code());
                    loadSessionResponseMutableLiveData.setValue(null);
                    loadingState.setValue(false);
                    errorState.setValue(true);
                    reloginState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<LoadSessionResponse> call, Throwable t) {
                Log.e(LogTags.LOAD_SESSIONS, "Error: " + t.getLocalizedMessage());
                if (RETRIES < MAX_RETRY_CALL) {
                    loadingState.setValue(true);
                    errorState.setValue(false);
                    RETRIES++;
                    call.clone().enqueue(this);
                } else {
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    Toast.makeText(application, "Something went wrong, reason: " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                }

                try {

                    Log.d(LogTags.LOAD_SESSIONS, "onFailure: " + loadSessionDao.getLoadSession().toString());
                    loadSessionResponseMutableLiveData.setValue(loadSessionDao.getLoadSession());
                    loadingState.setValue(false);
                    errorState.setValue(false);

                } catch (Exception e) {
                    e.printStackTrace();
                    loadSessionResponseMutableLiveData.setValue(null);
                    reloginState.setValue(true);
                    loadingState.setValue(false);
                    errorState.setValue(true);


                }
            }
        });

        return loadSessionResponseMutableLiveData;
    }

    //with email
    public MutableLiveData<LoadSessionResponse> loadSessionWithEmail(String email) {

        Call<LoadSessionResponse> loadSessionCall = LoadSessionRetrofitClient.getInstance().getLoadSessionApi().loadSessionWithEmail(email);
        loadingState.setValue(true);
        loadSessionCall.enqueue(new Callback<LoadSessionResponse>() {
            @Override
            public void onResponse(Call<LoadSessionResponse> call, Response<LoadSessionResponse> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.LOAD_SESSIONS, "onResponse: " + response.body());
                        loadSessionResponseMutableLiveData.setValue(response.body());

                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.LOAD_SESSIONS, "ERROR: ", e);
                        reloginState.setValue(true);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    Log.d(LogTags.LOAD_SESSIONS, "onResponse: FAILED " + response.code());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    reloginState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<LoadSessionResponse> call, Throwable t) {
                Log.e(LogTags.LOAD_SESSIONS, "Error: " + t.getLocalizedMessage());
                errorState.setValue(true);
                loadingState.setValue(false);
                Toast.makeText(application, "Something went wrong, reason: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                if (RETRIES < MAX_RETRY_CALL) {
                    RETRIES++;
                    call.clone().enqueue(this);
                }
            }
        });
        return loadSessionResponseMutableLiveData;
    }


    public MutableLiveData<LoadSessionResponse> loadSessionID(String loginID) {

        Call<LoadSessionResponse> loadSessionCall = LoadSessionRetrofitClientV2.getInstance().getLoadSessionApi().loadSessionWithID(loginID);
        loadingState.setValue(true);
        loadSessionCall.enqueue(new Callback<LoadSessionResponse>() {
            @Override
            public void onResponse(Call<LoadSessionResponse> call, Response<LoadSessionResponse> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.LOAD_SESSIONS, "onResponse: " + response.body());
                        loadSessionResponseMutableLiveData.setValue(response.body());

                        loadingState.setValue(false);
                        errorState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.LOAD_SESSIONS, "ERROR: ", e);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                        reloginState.setValue(true);
                    }
                } else {
                    Log.d(LogTags.LOAD_SESSIONS, "onResponse: FAILED " + response.code());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    reloginState.setValue(true);
                    //if status code is 500 try to send the user for new login credentials.
                    if (response.code() == 500) {
                        errorState.setValue(false);
                        loadingState.setValue(false);
                        reloginState.setValue(true);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoadSessionResponse> call, Throwable t) {
                Log.e(LogTags.LOAD_SESSIONS, "Error: " + t.getLocalizedMessage());
                errorState.setValue(true);
                loadingState.setValue(false);
                Toast.makeText(application, "Something went wrong, reason: " + t.getLocalizedMessage() + "\nretrying to get the data(" + String.valueOf(RETRIES) + ")", Toast.LENGTH_SHORT).show();
                if (RETRIES < MAX_RETRY_CALL) {
                    RETRIES++;
                    call.clone().enqueue(this);
                    loadingState.setValue(true);
                } else {
                    reloginState.setValue(true);
                }
            }
        });
        return loadSessionResponseMutableLiveData;
    }


    public MutableLiveData<LoadSessionResponse> getLoadSessionResponseMutableLiveData() {
        return loadSessionResponseMutableLiveData;
    }


}
