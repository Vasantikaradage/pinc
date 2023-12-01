package com.pinc.android.MB360.onboarding.authentication;

import com.pinc.android.MB360.onboarding.validation.ValidationEmailRequest;
import com.pinc.android.MB360.onboarding.validation.ValidationRequest;
import com.pinc.android.MB360.onboarding.validation.ValidationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginApi {

    @POST("Login/RequestOTP")
        //endpoint
    Call<LoginResponse> RequestOTP(
            @Body LoginRequest mobileNumber
    );

    @POST("Login/RequestOTP")
        //endpoint
    Call<LoginResponse> RequestEmailOTP(
            @Body LoginEmailRequest emailId
    );

    @POST("Login/ValidateLogin")
        //endpoint
    Call<LoginIDResponse> RequestLoginIdOTP(
            @Body LoginIdRequest loginIdRequest
    );


    @POST("Login/ValidateOTP")
        //endpoint
    Call<ValidationResponse> ValidateOTP(
            @Body ValidationRequest mobileNumber
    );

    @POST("Login/ValidateOTP")
        //endpoint
    Call<ValidationResponse> ValidateEmailOTP(
            @Body ValidationEmailRequest validationEmailRequest
    );
}
