package com.pinc.android.MB360.insurance.coverages.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.pinc.android.MB360.database.AppDatabase;
import com.pinc.android.MB360.database.dao.CoveragesDao;
import com.pinc.android.MB360.insurance.coverages.repository.responseclass.CoverageDetailsResponse;
import com.pinc.android.MB360.insurance.coverages.repository.responseclass.CoverageResponse;
import com.pinc.android.MB360.insurance.coverages.repository.responseclass.CoveragesDatum;
import com.pinc.android.MB360.insurance.coverages.repository.retrofit.CoveragesRetrofitClient;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.utilities.ResponseException;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * this class has the
 * business logic for Coverages
 * in users
 **/
public class CoveragesRepository {

    private final MutableLiveData<CoverageResponse> coverageResponseMutableLiveData;
    public final MutableLiveData<CoverageDetailsResponse> coverageDetailsResponseMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    Application application;
    public final MutableLiveData<String> relationGroupLiveData;
    FirebaseCrashlytics crashlytics;
    private AppDatabase appDatabase;
    private CoveragesDao coveragesDao;



    public CoveragesRepository(Application application) {
        this.coverageResponseMutableLiveData = new MutableLiveData<>();
        this.coverageDetailsResponseMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();
        this.relationGroupLiveData = new MutableLiveData<>("");
        this.application = application;
        crashlytics = FirebaseCrashlytics.getInstance();
        appDatabase=AppDatabase.getInstance(application);
        coveragesDao=appDatabase.coverageDao();
    }

    /**
     * this function  has the business logic for calling
     * and parsing the @Coverage response
     **/
    public MutableLiveData<CoverageResponse> getCoveragesPolicyData(String groupChildSrNo, String oeGrpBasInfSrNo) {
        loadingState.setValue(true);
        Call<CoverageResponse> coverageResponseCall = CoveragesRetrofitClient.getInstance().getCoverageApi().getCoveragesPolicyData(groupChildSrNo, oeGrpBasInfSrNo);

        coverageResponseCall.enqueue(new Callback<CoverageResponse>() {
            @Override
            public void onResponse(Call<CoverageResponse> call, Response<CoverageResponse> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.COVERAGE_ACTIVITY, "onResponse: " + response.body());
                        coverageResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                        response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                        coveragesDao.insertCoverages(response.body());


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(LogTags.COVERAGE_ACTIVITY, "Response" + response.body());
                        Log.e(LogTags.COVERAGE_ACTIVITY, "Error: ", e);
                        coverageResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }
                } else {
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    ResponseException responseException = new ResponseException("Coverages -> getCoveragesData -> Response Code:- " + String.valueOf(response.code()) + "GroupChildSrNo:- " + groupChildSrNo + "OeGrpBasInfoSrNo:- " + oeGrpBasInfSrNo);
                    crashlytics.recordException(responseException);
                    // Toast.makeText(application, "Something went wrong Error Code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CoverageResponse> call, Throwable t) {

                try {

                    Log.d(LogTags.COVERAGE_ACTIVITY, "onFailure: " + coveragesDao.getCoverages(oeGrpBasInfSrNo).toString());
                    coverageResponseMutableLiveData.setValue(coveragesDao.getCoverages(oeGrpBasInfSrNo));
                    loadingState.setValue(false);
                    errorState.setValue(false);

                } catch (Exception e) {
                    e.printStackTrace();
                    loadingState.setValue(false);
                    errorState.setValue(true);


                }

            }
        });
        return coverageResponseMutableLiveData;
    }


    public MutableLiveData<CoverageDetailsResponse> getCoverageDetails(String groupChildSrNo, String oeGrpBasInfSrNo, String productType, String employeeSrNo) {
        loadingState.setValue(true);
        Call<CoverageDetailsResponse> coverageDetailsCall = CoveragesRetrofitClient.getInstance().getCoverageApi().getPolicyCoveragesDetails(groupChildSrNo, oeGrpBasInfSrNo, productType, employeeSrNo);

        coverageDetailsCall.enqueue(new Callback<CoverageDetailsResponse>() {
            @Override
            public void onResponse(Call<CoverageDetailsResponse> call, Response<CoverageDetailsResponse> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.COVERAGE_ACTIVITY, "onResponse: " + response.body());
                        coverageDetailsResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                        response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                        coveragesDao.insertCoverageDetail(response.body());

                    } catch (Exception e) {
                        Log.d(LogTags.COVERAGE_ACTIVITY, "Response: " + response.body());
                        e.printStackTrace();
                        Log.e(LogTags.COVERAGE_ACTIVITY, "Error: ", e);
                        coverageDetailsResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<CoverageDetailsResponse> call, Throwable t) {
                Log.e(LogTags.COVERAGE_ACTIVITY, "Error: " + t.getLocalizedMessage());

                try {

                    Log.d(LogTags.COVERAGE_ACTIVITY, "onFailure: " + coveragesDao.getCoverageDetails(oeGrpBasInfSrNo).toString());
                    coverageDetailsResponseMutableLiveData.setValue(coveragesDao.getCoverageDetails(oeGrpBasInfSrNo));
                    loadingState.setValue(false);
                    errorState.setValue(false);

                } catch (Exception e) {
                    e.printStackTrace();
                    loadingState.setValue(false);
                    errorState.setValue(true);


                }


            }
        });

        return coverageDetailsResponseMutableLiveData;
    }

    public MutableLiveData<CoverageResponse> getCoveragesData() {
        return coverageResponseMutableLiveData;
    }

    public MutableLiveData<CoverageDetailsResponse> getCoveragesDetailsData() {
        return coverageDetailsResponseMutableLiveData;
    }

    public String getRelationGroupDependant(String s) {
        //relation group count count
        int Son = 0;
        int Dt = 0;
        switch (s) {
            case "SPOUSE":
                return "Sp";
            case "SON":
                Son++;
                return "S" + Son;
            case "DAUGHTER":
                Dt++;
                return "D" + Dt;
            case "MOTHER-IN-LAW":
                return "MIL";
            case "FATHER-IN-LAW":
                return "FIL";
            default:
                return (s.isEmpty() ? "-" : s.substring(0, 1));
        }
    }

    public MutableLiveData<String> getRelationGroupData() {
        try {
            List<CoveragesDatum> coveragesDataList = coverageDetailsResponseMutableLiveData.getValue().getCoveragesData();
            StringBuilder relationGroup = new StringBuilder();

            for (CoveragesDatum coverageDetails : coveragesDataList
            ) {
                if (relationGroup.toString().isEmpty()) {
                    relationGroup.append(getRelationGroupDependant(coverageDetails.getRelation()));
                } else {
                    relationGroup.append("+").append(getRelationGroupDependant(coverageDetails.getRelation()));
                }
            }
            relationGroupLiveData.setValue(relationGroup.toString());
        } catch (Exception e) {
            relationGroupLiveData.setValue("-");
        }
        return relationGroupLiveData;
    }

}
