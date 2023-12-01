package com.pinc.android.MB360.insurance.myclaims.retrofit;

import com.pinc.android.MB360.insurance.myclaims.responseclass.DocumentElement;
import com.pinc.android.MB360.insurance.myclaims.responseclass.claimsdetails.ClaimsDetails;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MyClaimsApi {

    @POST("LoadEmployeeClaimsValues")
    Call<DocumentElement> getClaims(@Body RequestBody dataRequest);


    @POST("LoadDetailedClaimsValues")
    Call<ClaimsDetails> getClaimDetail(@Body RequestBody dataRequest);
}
