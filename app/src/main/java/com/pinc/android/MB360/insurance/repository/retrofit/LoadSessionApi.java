package com.pinc.android.MB360.insurance.repository.retrofit;

import com.pinc.android.MB360.insurance.repository.responseclass.LoadSessionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoadSessionApi {

    @POST("Login/LoadSessionValues")
    Call<LoadSessionResponse> loadSessionWithPhoneNumber(
            @Query("mobileno") String phoneNumber
    );

    @POST("Login/LoadSessionValuesWithEmailD")
    Call<LoadSessionResponse> loadSessionWithEmail(
            @Query(value = "EmailId", encoded = true) String emailId
    );

    @POST("Login/LoadSessionByUniqueID")
    Call<LoadSessionResponse> loadSessionWithID(
            @Query("LoginId") String loginId
    );

}
