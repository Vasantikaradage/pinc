package com.pinc.android.MB360.insurance.claims.repository.retrofit;


import com.pinc.android.MB360.insurance.claims.repository.requestclass.NewIntimateRequest;
import com.pinc.android.MB360.insurance.claims.repository.responseclass.ClaimsDetailsResponse;
import com.pinc.android.MB360.insurance.claims.repository.responseclass.ClaimsResponse;
import com.pinc.android.MB360.insurance.claims.repository.responseclass.LoadPersonsIntimationResponse;
import com.pinc.android.MB360.insurance.claims.repository.responseclass.Result;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ClaimsApi {
    @GET("IntimateClaim/LoadIntimatedClaims")
    Call<ClaimsResponse> getClaims(@Query("EmployeeSrNo") String employeeSrNo,
                                   @Query("GroupChildSrNo") String grpChildSrNo,
                                   @Query("OegrpBasInfSrNo") String oeGrpBasInfSrNo);

    @GET("")
    Call<ClaimsDetailsResponse> getIntimatedClaimsInfo(@Query("IntimationSrNo") String employeeSrNo);

    @GET("IntimateClaim/LoadPersonsForIntimation")
    Call<LoadPersonsIntimationResponse> getPersons(@Query("EmployeeSrNo") String employeeSrNo,
                                                   @Query("GroupChildSrNo") String grpChildSrNo,
                                                   @Query("OegrpBasInfSrNo") String oeGrpBasInfSrNo);

    @POST("IntimateClaim/NewIntimateClaim")
    Call<Result> startIntimation(@Body NewIntimateRequest newIntimateRequest);

}
