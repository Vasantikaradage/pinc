package com.pinc.android.MB360.insurance.policyfeatures.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.pinc.android.MB360.database.AppDatabase;
import com.pinc.android.MB360.database.dao.PolicyFeatureDao;
import com.pinc.android.MB360.insurance.policyfeatures.repository.responseclass.PolicyFeaturesResponse;
import com.pinc.android.MB360.insurance.policyfeatures.repository.responseclass.PolicyFeaturesResponseOffline;
import com.pinc.android.MB360.insurance.policyfeatures.repository.retrofit.PolicyFeaturesRetrofitClient;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.utilities.ResponseException;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * this class has the
 * business logic for Policy-Features
 * in users
 **/
public class PolicyFeaturesRepository {
    private final MutableLiveData<List<PolicyFeaturesResponse>> policyFeaturesResponseMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    Application application;
    FirebaseCrashlytics crashlytics;
    private AppDatabase appDatabase;
    private PolicyFeatureDao policyFeatureDao;

    public PolicyFeaturesRepository(Application application) {
        this.policyFeaturesResponseMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();
        this.application = application;
        crashlytics = FirebaseCrashlytics.getInstance();
        appDatabase = AppDatabase.getInstance(application);
        policyFeatureDao = appDatabase.policyFeatureDao();
    }

    /**
     * this function  has the business logic for calling
     * and parsing the @PolicyFeatures {@link PolicyFeaturesResponse } response
     **/

    public MutableLiveData<List<PolicyFeaturesResponse>> getPolicyFeatures(String grpChildSrNo, String oeGrpBasInfSrNo, String productType) {
        loadingState.setValue(true);
        Call<List<PolicyFeaturesResponse>> policyFeaturesResponseCall = PolicyFeaturesRetrofitClient.getInstance().getPolicyFeaturesApi().getPolicyFeatures(grpChildSrNo, oeGrpBasInfSrNo, productType);
        policyFeaturesResponseCall.enqueue(new Callback<List<PolicyFeaturesResponse>>() {
            @Override
            public void onResponse(Call<List<PolicyFeaturesResponse>> call, Response<List<PolicyFeaturesResponse>> response) {
                int code = response.code();
                if (code == 200) {
                    try {
                        Log.d(LogTags.POLICY_FEATURES_ACTIVITY, "onResponse: " + response.body().toString());
                        policyFeaturesResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);

                        PolicyFeaturesResponseOffline policyFeaturesResponseOffline = new PolicyFeaturesResponseOffline();
                        policyFeaturesResponseOffline.setPolicyFeaturesResponseList(response.body());
                        policyFeaturesResponseOffline.setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);

                        policyFeatureDao.insertPolicyFeature(policyFeaturesResponseOffline);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.POLICY_FEATURES_ACTIVITY, "Error: ", e);
                        policyFeaturesResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        Toast.makeText(application, "Something went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //throwing the issues in firebase
                    ResponseException exception = new ResponseException(LogTags.POLICY_FEATURES_ACTIVITY + " : Response Code : " + response.code() + " | Message : " + response.message());
                    Throwable throwable = new Throwable(exception);
                    crashlytics.recordException(throwable);

                    //here we can make user see the issue
                    //Toast.makeText(application, "Something went wrong Error reason: " + response.message(), Toast.LENGTH_SHORT).show();

                    //local debug
                    Log.e(LogTags.POLICY_FEATURES_ACTIVITY, "Error from server: " + response.errorBody().toString());
                    errorState.setValue(true);
                    loadingState.setValue(false);

                }
            }

            @Override
            public void onFailure(Call<List<PolicyFeaturesResponse>> call, Throwable t) {
                Log.e(LogTags.POLICY_FEATURES_ACTIVITY, "Error: " + t.getLocalizedMessage());
                errorState.setValue(true);
                loadingState.setValue(false);
                Toast.makeText(application, "Something Went wrong, reason: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                try {

                    Log.d(LogTags.FAQ_ACTIVITY, "onFailure: " + policyFeatureDao.getPolicyFeature(oeGrpBasInfSrNo).toString());
                    policyFeaturesResponseMutableLiveData.setValue(policyFeatureDao.getPolicyFeature(oeGrpBasInfSrNo).getPolicyFeaturesResponseList());


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return policyFeaturesResponseMutableLiveData;
    }

    public MutableLiveData<List<PolicyFeaturesResponse>> getPolicyFeaturesData() {
        return policyFeaturesResponseMutableLiveData;

    }

}
