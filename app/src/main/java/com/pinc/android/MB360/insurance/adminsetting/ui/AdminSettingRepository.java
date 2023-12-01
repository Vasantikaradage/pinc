package com.pinc.android.MB360.insurance.adminsetting.ui;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.pinc.android.MB360.database.AppDatabase;
import com.pinc.android.MB360.database.dao.EnrollmentWindowCountDao;
import com.pinc.android.MB360.insurance.adminsetting.responseclass.AdminSettingResponse;
import com.pinc.android.MB360.insurance.adminsetting.retrofit.AdminSettingRetrofitClient;
import com.pinc.android.MB360.utilities.LogTags;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminSettingRepository {
    private final MutableLiveData<AdminSettingResponse> adminSettingViewModelMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    FirebaseCrashlytics crashlytics;
    Application application;
    private AppDatabase appDatabase;
    private EnrollmentWindowCountDao enrollmentWindowCountDao;


    public AdminSettingRepository(Application application) {
        this.adminSettingViewModelMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();
        crashlytics = FirebaseCrashlytics.getInstance();
        this.application = application;
        appDatabase=AppDatabase.getInstance(application);
        enrollmentWindowCountDao=appDatabase.enrollmentWindowCountDao();


    }

    public MutableLiveData<AdminSettingResponse> getAdminSettingDetailsData() {
        return adminSettingViewModelMutableLiveData;
    }



    public MutableLiveData<AdminSettingResponse> getAdminSettingData(String groupChildSrNo, String oeGrpBasInfSrNo) {

        Call<AdminSettingResponse> claimsCall = AdminSettingRetrofitClient.getInstance().getAdminSettingApi().getAdminSettingData(groupChildSrNo, oeGrpBasInfSrNo);
        claimsCall.enqueue(new Callback<AdminSettingResponse>() {
            @Override
            public void onResponse(Call<AdminSettingResponse> call, Response<AdminSettingResponse> response) {
                if (response.code() == 200 ) {
                    try {
                        Log.d("", "onResponse: " + response.body().toString());
                        adminSettingViewModelMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                        response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                        enrollmentWindowCountDao.insertEnrollmentWindowCount(response.body());


                    } catch (Exception e) {
                        e.printStackTrace();
                        adminSettingViewModelMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        Toast.makeText(application, "Something went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<AdminSettingResponse> call, Throwable t) {
                t.printStackTrace();
                try {

                    Log.d("", "onFailure: " + enrollmentWindowCountDao.getEnrollmentCount(oeGrpBasInfSrNo).toString());
                    adminSettingViewModelMutableLiveData.setValue(enrollmentWindowCountDao.getEnrollmentCount(oeGrpBasInfSrNo));
                    loadingState.setValue(false);
                    errorState.setValue(false);

                } catch (Exception e) {
                    e.printStackTrace();
                    adminSettingViewModelMutableLiveData.setValue(null);

                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }
        });

        return adminSettingViewModelMutableLiveData;
    }
}
