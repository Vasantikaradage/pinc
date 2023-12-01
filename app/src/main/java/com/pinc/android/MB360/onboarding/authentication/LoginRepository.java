package com.pinc.android.MB360.onboarding.authentication;


import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pinc.android.MB360.onboarding.validation.ValidationEmailRequest;
import com.pinc.android.MB360.onboarding.validation.ValidationRequest;
import com.pinc.android.MB360.onboarding.validation.ValidationResponse;
import com.pinc.android.MB360.utilities.LogTags;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * this class has the
 * business logic for logging
 * in users
 **/

public class LoginRepository {

    private final MutableLiveData<LoginResponse> loginResponseMutableLiveData;
    private final MutableLiveData<LoginIDResponse> loginIDResponseMutableLiveData;
    private final MutableLiveData<ValidationResponse> validationResponseMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    Application application;

    //todo set and use loading state

    public LoginRepository(Application application) {
        this.loginResponseMutableLiveData = new MutableLiveData<>();
        this.loginIDResponseMutableLiveData = new MutableLiveData<>();
        this.validationResponseMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(false);
        this.errorState = new MutableLiveData<>();
        this.application = application;

    }

    /**
     * this function  has the business logic for logging in users with mobile number
     **/
    public MutableLiveData<LoginResponse> loginWithMobileNumber(LoginRequest loginRequest) {
        loadingState.setValue(true);
        Log.d("LOGIN-ACTIVITY", loginRequest.getMobileno().toString());
        Call<LoginResponse> loginCall = LoginClient.getInstance().getLoginApi().RequestOTP(loginRequest);
        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200) {
                    try {
                        assert response.body() != null;
                        loginResponseMutableLiveData.postValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        loginResponseMutableLiveData.postValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    loginResponseMutableLiveData.setValue(null);
                    loadingState.setValue(false);
                    errorState.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                //todo show user why it failed
                loginResponseMutableLiveData.postValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
                Log.d(LogTags.LOGIN_ACTIVITY, "onFailure: " + t.getMessage());
            }
        });

        return loginResponseMutableLiveData;
    }

    /**
     * this function  has the business logic for logging in users with email
     **/
    public MutableLiveData<LoginResponse> loginWithEmail(LoginEmailRequest loginEmailRequest) {
        loadingState.setValue(true);
        Log.d(LogTags.LOGIN_ACTIVITY, loginEmailRequest.getOfficialemailId().toString());
        Call<LoginResponse> loginCall = LoginClient.getInstance().getLoginApi().RequestEmailOTP(loginEmailRequest);
        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.code() == 200) {
                    try {
                        assert response.body() != null;
                        Log.d(LogTags.LOGIN_ACTIVITY, "onResponse: " + response.body().toString());
                        loginResponseMutableLiveData.postValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        loginResponseMutableLiveData.postValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    loginResponseMutableLiveData.setValue(null);
                    loadingState.setValue(false);
                    errorState.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                //todo show user why it failed
                loginResponseMutableLiveData.postValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });

        return loginResponseMutableLiveData;
    }

    /**
     * this function  has the business logic for logging in users with id
     **/
    public MutableLiveData<LoginIDResponse> loginWithId(LoginIdRequest loginIdRequest) {
        loadingState.setValue(true);
        Log.d(LogTags.LOGIN_ACTIVITY, loginIdRequest.toString());

        Call<LoginIDResponse> loginCall = LoginClient.getInstance().getLoginApi().RequestLoginIdOTP(loginIdRequest);
        loginCall.enqueue(new Callback<LoginIDResponse>() {
            @Override
            public void onResponse(Call<LoginIDResponse> call, Response<LoginIDResponse> response) {
                if (response.code() == 200) {
                    try {
                        assert response.body() != null;
                        Log.d(LogTags.LOGIN_ACTIVITY, "onResponse: " + response.body().toString());
                        loginIDResponseMutableLiveData.postValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.LOGIN_ACTIVITY, "caught Error : ", e);
                        loginIDResponseMutableLiveData.postValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    Log.e(LogTags.LOGIN_ACTIVITY, "error : " + response.code());
                    loginIDResponseMutableLiveData.postValue(null);
                    loadingState.setValue(false);
                    errorState.setValue(true);

                }
            }

            @Override
            public void onFailure(Call<LoginIDResponse> call, Throwable t) {
                //todo show user why it failed

                loginIDResponseMutableLiveData.postValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });


        return loginIDResponseMutableLiveData;
    }

    /**
     * otp validation is done here
     **/
    public MutableLiveData<ValidationResponse> ValidateOTP(ValidationRequest validationRequest) {
        loadingState.setValue(true);
        Log.d("LOGIN-ACTIVITY", validationRequest.getMobileno().toString());
        Call<ValidationResponse> validationCall = LoginClient.getInstance().getLoginApi().ValidateOTP(validationRequest);

        validationCall.enqueue(new Callback<ValidationResponse>() {
            @Override
            public void onResponse(Call<ValidationResponse> call, Response<ValidationResponse> response) {
                if (response.code() == 200) {
                    try {
                        assert response.body() != null;
                        validationResponseMutableLiveData.postValue(response.body());
                        loadingState.setValue(false);
                    } catch (Exception e) {
                        validationResponseMutableLiveData.postValue(null);
                        loadingState.setValue(false);
                    }

                } else {
                    //TODO something is wrong
                }
            }

            @Override
            public void onFailure(Call<ValidationResponse> call, Throwable t) {
                //todo show user why it failed
                validationResponseMutableLiveData.postValue(null);
                loadingState.setValue(false);
            }
        });

        return validationResponseMutableLiveData;
    }

    public MutableLiveData<ValidationResponse> ValidateOTPWithEmail(ValidationEmailRequest validationEmailRequest) {
        loadingState.setValue(true);
        Log.d(LogTags.LOGIN_ACTIVITY, validationEmailRequest.getOfficialemailId().toString());
        Call<ValidationResponse> validationCall = LoginClient.getInstance().getLoginApi().ValidateEmailOTP(validationEmailRequest);

        validationCall.enqueue(new Callback<ValidationResponse>() {
            @Override
            public void onResponse(Call<ValidationResponse> call, Response<ValidationResponse> response) {
                if (response.code() == 200) {
                    try {
                        assert response.body() != null;
                        validationResponseMutableLiveData.postValue(response.body());
                        loadingState.setValue(false);
                    } catch (Exception e) {
                        loadingState.setValue(false);
                        validationResponseMutableLiveData.postValue(null);
                    }

                } else {
                    //TODO something is wrong
                }
            }

            @Override
            public void onFailure(Call<ValidationResponse> call, Throwable t) {
                //todo show user why it failed
                validationResponseMutableLiveData.postValue(null);
                loadingState.setValue(false);
            }
        });

        return validationResponseMutableLiveData;
    }

    public LiveData<LoginResponse> getLoginResponse() {
        return loginResponseMutableLiveData;
    }

    public LiveData<ValidationResponse> getValidationResponse() {
        return validationResponseMutableLiveData;
    }

    public LiveData<LoginIDResponse> getLoginIDResponse() {
        return loginIDResponseMutableLiveData;
    }

    public void resetValidationResponse() {
        validationResponseMutableLiveData.setValue(null);
    }

    public void resetLoginResponse() {
        loginResponseMutableLiveData.setValue(null);
    }
}

