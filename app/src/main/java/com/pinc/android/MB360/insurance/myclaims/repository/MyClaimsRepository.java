package com.pinc.android.MB360.insurance.myclaims.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.database.AppDatabase;
import com.pinc.android.MB360.database.dao.MyClaimsDao;
import com.pinc.android.MB360.insurance.myclaims.responseclass.Claims;
import com.pinc.android.MB360.insurance.myclaims.responseclass.DocumentElement;
import com.pinc.android.MB360.insurance.myclaims.responseclass.claimsdetails.ClaimsDetails;
import com.pinc.android.MB360.insurance.myclaims.retrofit.MyClaimsRetrofitClient;
import com.pinc.android.MB360.utilities.LogTags;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyClaimsRepository {

    MutableLiveData<Boolean> loading = new MutableLiveData<>(true);
    MutableLiveData<Boolean> error = new MutableLiveData<>(false);
    MutableLiveData<String> errorDescription = new MutableLiveData<>(null);


    Application application;
    private final MutableLiveData<DocumentElement> myClaimsResponse;
    private final MutableLiveData<ClaimsDetails> myClaimsDetailsResponse;
    public MutableLiveData<Claims> selectedClaim = new MutableLiveData<>();

    private AppDatabase appDatabase;
    private MyClaimsDao documentElementDao;



    public MyClaimsRepository(Application application) {
        myClaimsResponse = new MutableLiveData<>();
        myClaimsDetailsResponse = new MutableLiveData<>();
        this.application = application;
        appDatabase=AppDatabase.getInstance(application);
        documentElementDao= appDatabase.documentElementDao();

    }

    /**
     * this function  has the business logic for calling
     * and parsing the claims XML response
     **/
    public LiveData<DocumentElement> getMyClaims(String dataRequest) {
        if (myClaimsResponse.getValue() == null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("text/xml"), dataRequest);
            Call<DocumentElement> myClaimsCall = MyClaimsRetrofitClient.getInstance().getClaimsApi().getClaims(requestBody);
            myClaimsCall.enqueue(new Callback<DocumentElement>() {
                @Override
                public void onResponse(Call<DocumentElement> call, Response<DocumentElement> response) {
                    if (response.code() == 200) {
                        try {
                            Log.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.body());
                            myClaimsResponse.setValue(response.body());
                            loading.setValue(false);
                            error.setValue(false);
                           // response.body().setDataRequest(dataRequest);
                           documentElementDao.insertDocumentElement(response.body());
                        } catch (Exception e) {
                            e.printStackTrace();
                            myClaimsResponse.setValue(null);
                            errorDescription.setValue(application.getApplicationContext().getString(R.string.something_went_wrong));
                            loading.setValue(false);
                            error.setValue(false);

                        }
                    } else {
                        Log.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.code());
                        myClaimsResponse.setValue(response.body());
                        loading.setValue(false);
                        error.setValue(true);
                        errorDescription.setValue(null);
                    }
                }

                @Override
                public void onFailure(Call<DocumentElement> call, Throwable t) {
                    Log.e(LogTags.CLAIM_ACTIVITY, "onFailure: ", t);
                   try {

                        Log.d(LogTags.CLAIM_ACTIVITY, "onFailure: " + documentElementDao.getDocumentElement().toString());
                        myClaimsResponse.setValue(documentElementDao.getDocumentElement());
                        loading.setValue(false);
                        error.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                      //  myClaimsResponse.setValue(null);
                        loading.setValue(false);
                        error.setValue(true);
                    }
                    //errorDescription.setValue(application.getApplicationContext().getString(R.string.something_went_wrong));

                }
            });
        } else {
            //we already have the data
        }
        return myClaimsResponse;
    }

    /**
     * this function  has the business logic for calling
     * and parsing the claims Details XML response
     **/
    public LiveData<ClaimsDetails> getMyClaimsDetails(String dataRequest,String claimSrNo) {

        RequestBody requestBody = RequestBody.create(MediaType.parse("text/xml"), dataRequest);
        Call<ClaimsDetails> myClaimsCall = MyClaimsRetrofitClient.getInstance().getClaimsApi().getClaimDetail(requestBody);
        myClaimsCall.enqueue(new Callback<ClaimsDetails>() {
            @Override
            public void onResponse(Call<ClaimsDetails> call, Response<ClaimsDetails> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.CLAIM_ACTIVITY, "dataRequest: " + dataRequest);
                        Log.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.body());
                        response.body().setClaimSrNo(claimSrNo);
                        myClaimsDetailsResponse.setValue(response.body());
                        loading.setValue(false);
                        error.setValue(false);



                        documentElementDao.insertClaimDetails(response.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                        myClaimsResponse.setValue(null);
                        loading.setValue(false);
                        error.setValue(false);

                    }
                } else {
                    Log.d(LogTags.CLAIM_ACTIVITY, "onResponse: " + response.code());
                    myClaimsDetailsResponse.setValue(response.body());
                    loading.setValue(false);
                    error.setValue(true);
                    errorDescription.setValue("Some thing went wrong \nError Code : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ClaimsDetails> call, Throwable t) {
                Log.e(LogTags.CLAIM_ACTIVITY, "onFailure: ", t);
                try {

                    Log.d(LogTags.CLAIM_ACTIVITY, "onFailure: " + documentElementDao.getClaimDeatils(claimSrNo).toString());
                    myClaimsDetailsResponse.setValue(documentElementDao.getClaimDeatils(claimSrNo));
                    loading.setValue(false);
                    error.setValue(false);

                } catch (Exception e) {
                    e.printStackTrace();
                    //  myClaimsResponse.setValue(null);
                    loading.setValue(false);
                    error.setValue(true);
                }

            }
        });
        return myClaimsDetailsResponse;
    }

    public LiveData<DocumentElement> getMyClaimsData() {
        return myClaimsResponse;
    }

    public LiveData<ClaimsDetails> getMyClaimsDetailsData() {
        return myClaimsDetailsResponse;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public MutableLiveData<Boolean> getError() {
        return error;
    }

    public MutableLiveData<Claims> getSelectedClaim() {
        return selectedClaim;
    }

    public void setSelectedClaim(Claims claims) {
        selectedClaim.setValue(claims);
    }


    public MutableLiveData<String> getErrorDescription() {
        return errorDescription;
    }
}
