package com.pinc.android.MB360.insurance.utilities.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.pinc.android.MB360.database.AppDatabase;
import com.pinc.android.MB360.database.dao.UtilitiesDao;
import com.pinc.android.MB360.insurance.utilities.repository.responseclass.UtilitiesResponse;
import com.pinc.android.MB360.insurance.utilities.repository.retrofit.UtilitiesRetrofitClient;
import com.pinc.android.MB360.utilities.LogTags;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * this class has the
 * business logic for Utility reports
 * in users
 **/
public class UtilitiesRepository {

    private final MutableLiveData<UtilitiesResponse> utilitiesMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    Application application;
    private AppDatabase appDatabase;
    private UtilitiesDao utilitiesDao;

    public UtilitiesRepository(Application application) {
        this.utilitiesMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();
        this.application = application;
        appDatabase=AppDatabase.getInstance(application);
        utilitiesDao=appDatabase.utilitiesDao();
    }

    /**
     * this function  has the business logic for calling
     * and parsing the @Utilities response
     **/

    public MutableLiveData<UtilitiesResponse> getUtilities(String grpChildSrNo, String oeGrpBasInfSrNo) {

        Call<UtilitiesResponse> utilitiesCall = UtilitiesRetrofitClient.getInstance().getUtilitiesApi().getUtilities(grpChildSrNo, oeGrpBasInfSrNo);
        utilitiesCall.enqueue(new Callback<UtilitiesResponse>() {
            @Override
            public void onResponse(Call<UtilitiesResponse> call, Response<UtilitiesResponse> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.LOAD_SESSIONS, "onResponse: " + response.body());
                        utilitiesMutableLiveData.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                        response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                        utilitiesDao.insertUtilities(response.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.LOAD_SESSIONS, "ERROR: ", e);
                        utilitiesMutableLiveData.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    Log.d(LogTags.LOAD_SESSIONS, "onResponse: FAILED" + response.code());
                    utilitiesMutableLiveData.setValue(null);
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<UtilitiesResponse> call, Throwable t) {
                Log.e(LogTags.UTILITIES_ACTIVITY, "Error: " + t.getLocalizedMessage());
                Toast.makeText(application, "Something went wrong, reason: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.e(LogTags.UTILITIES_ACTIVITY, "Error: " + t.getLocalizedMessage());
                try {
                    Toast.makeText(application, "Something Went wrong, reason: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    utilitiesMutableLiveData.setValue(utilitiesDao.getUtilities(oeGrpBasInfSrNo));
                    loadingState.setValue(false);
                    errorState.setValue(false);
                }catch (Exception e)
                {
                    utilitiesMutableLiveData.setValue(null);
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }
        });
        return utilitiesMutableLiveData;
    }

    public MutableLiveData<UtilitiesResponse> getUtilitiesData() {
        return utilitiesMutableLiveData;
    }
}
