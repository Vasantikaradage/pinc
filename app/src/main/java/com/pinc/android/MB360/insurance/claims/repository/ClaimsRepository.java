package com.pinc.android.MB360.insurance.claims.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.pinc.android.MB360.database.AppDatabase;
import com.pinc.android.MB360.database.dao.IntimateClaimsDao;
import com.pinc.android.MB360.insurance.claims.repository.requestclass.NewIntimateRequest;
import com.pinc.android.MB360.insurance.claims.repository.responseclass.ClaimsResponse;
import com.pinc.android.MB360.insurance.claims.repository.responseclass.LoadPersonsIntimationResponse;
import com.pinc.android.MB360.insurance.claims.repository.responseclass.Result;
import com.pinc.android.MB360.insurance.claims.repository.retrofit.ClaimsRetrofitClient;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.utilities.ResponseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * this class has the
 * business logic for Claims {@link ClaimsRepository }
 * in users
 **/
public class ClaimsRepository {

    private final MutableLiveData<ClaimsResponse> claimsMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    public final MutableLiveData<LoadPersonsIntimationResponse> personsMutableLiveData;
    FirebaseCrashlytics crashlytics;
    Application application;
    private AppDatabase  appDatabase;
    private IntimateClaimsDao claimsDao;


    public ClaimsRepository(Application application) {
        this.claimsMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();
        this.personsMutableLiveData = new MutableLiveData<>();
        crashlytics = FirebaseCrashlytics.getInstance();
        this.application = application;
        appDatabase=AppDatabase.getInstance(application);
        claimsDao=appDatabase.cLaimsDao();

    }

    /**
     * this function  has the business logic for calling
     * and parsing the @ClaimsResponse {@link ClaimsResponse } response
     **/

    public MutableLiveData<ClaimsResponse> getClaims(String employeeSrNo, String groupChildSrNo, String oeGrpBasInfSrNo) {

        Call<ClaimsResponse> claimsCall = ClaimsRetrofitClient.getInstance().getClaimsApi().getClaims(employeeSrNo, groupChildSrNo, oeGrpBasInfSrNo);
        claimsCall.enqueue(new Callback<ClaimsResponse>() {
            @Override
            public void onResponse(Call<ClaimsResponse> call, Response<ClaimsResponse> response) {
                if (response.code() == 200 && response.body().getClaimslist() != null) {
                    try {
                        Log.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.body().toString());
                        claimsMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                        response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                        claimsDao.insertClaimData(response.body());

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.CLAIM_ACTIVITY, "Error: ", e);
                        claimsMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        Toast.makeText(application, "Something went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.body().getResult() != null && !response.body().getResult().getStatus()) {
                    claimsMutableLiveData.setValue(response.body());
                    Log.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.body().toString());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    // Toast.makeText(application, "Something Went wrong: " + response.body().getResult().getMessage(), Toast.LENGTH_LONG).show();
                    ResponseException responseException = new ResponseException("Claims -> getClaims -> Response Code:- " + String.valueOf(response.code()) + "GroupChildSrNo:- " + groupChildSrNo + "OeGrpBasInfoSrNo:- " + oeGrpBasInfSrNo + "employeeSrno:- " + employeeSrNo);
                    crashlytics.recordException(responseException);
                }
            }

            @Override
            public void onFailure(Call<ClaimsResponse> call, Throwable t) {
                t.printStackTrace();

                try {

                    Log.d(LogTags.CLAIM_ACTIVITY, "onFailure: " + claimsDao.getClaims(oeGrpBasInfSrNo).toString());
                    claimsMutableLiveData.setValue(claimsDao.getClaims(oeGrpBasInfSrNo));
                    loadingState.setValue(false);
                    errorState.setValue(false);


                } catch (Exception e) {
                    e.printStackTrace();
                    loadingState.setValue(false);
                    errorState.setValue(true);

                }
            }

        });

        return claimsMutableLiveData;
    }

    //spinner data loading the intifate for
    public MutableLiveData<LoadPersonsIntimationResponse> getPersons(String empSrNo, String grpChildSrNo, String oeGrpBAsInfoSrNo) {

        Call<LoadPersonsIntimationResponse> callGetPersons = ClaimsRetrofitClient.getInstance().getClaimsApi().getPersons(empSrNo, grpChildSrNo, oeGrpBAsInfoSrNo);
        callGetPersons.enqueue(new Callback<LoadPersonsIntimationResponse>() {
            @Override
            public void onResponse(Call<LoadPersonsIntimationResponse> call, Response<LoadPersonsIntimationResponse> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.body());
                        personsMutableLiveData.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                        response.body().setOeGrpBasInfoSrNo(oeGrpBAsInfoSrNo);
                        claimsDao.insertLoadPerson(response.body());


                    } catch (Exception e) {
                        personsMutableLiveData.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);

                    }
                } else {
                    personsMutableLiveData.setValue(response.body());
                    loadingState.setValue(false);
                    errorState.setValue(true);
                    ResponseException responseException = new ResponseException("IntimationClaims -> loadPersonsForIntimation -> Response Code:- " + String.valueOf(response.code()) + "GroupChildSrNo:- " + grpChildSrNo + "OeGrpBasInfoSrNo:- " + oeGrpBAsInfoSrNo + "employeeSrno:- " + empSrNo);
                    crashlytics.recordException(responseException);
                }
            }

            @Override
            public void onFailure(Call<LoadPersonsIntimationResponse> call, Throwable t) {
                try {

                    Log.d(LogTags.CLAIM_ACTIVITY, "onFailure: " + claimsDao.getLoadPerson(oeGrpBAsInfoSrNo).toString());
                    personsMutableLiveData.setValue(claimsDao.getLoadPerson(oeGrpBAsInfoSrNo));
                    loadingState.setValue(false);
                    errorState.setValue(false);


                } catch (Exception e) {
                    e.printStackTrace();
                    claimsMutableLiveData.setValue(null);
                    loadingState.setValue(false);
                    errorState.setValue(true);

                }
            }
        });
        return personsMutableLiveData;
    }

    public MutableLiveData<LoadPersonsIntimationResponse> getPersonData() {
        return personsMutableLiveData;
    }


    public MutableLiveData<ClaimsResponse> getClaimsData() {
        return claimsMutableLiveData;

    }


    public MutableLiveData<Result> startIntimation(NewIntimateRequest newIntimateRequest) {
        final MutableLiveData<Result> resultLiveData = new MutableLiveData<>();

        Call<Result> startIntimationCall = ClaimsRetrofitClient.getInstance().getClaimsApi().startIntimation(newIntimateRequest);

        startIntimationCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.body());
                        resultLiveData.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        resultLiveData.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    loadingState.setValue(false);
                    errorState.setValue(true);
                    resultLiveData.setValue(response.body());
                    ResponseException responseException = new ResponseException("IntimateClaims -> startIntimation -> Response Code:- " + String.valueOf(response.code()) + "new intimate request:- " + newIntimateRequest.toString());
                    crashlytics.recordException(responseException);
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                loadingState.setValue(false);
                errorState.setValue(true);
                resultLiveData.setValue(null);
            }
        });

        return resultLiveData;
    }


    /**
     * this function  has the business logic for calling
     * and parsing the @ClaimsResponse {@link ClaimsResponse } response
     **/


}
