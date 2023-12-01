package com.pinc.android.MB360.onboarding.authentication;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pinc.android.MB360.onboarding.validation.ValidationEmailRequest;
import com.pinc.android.MB360.onboarding.validation.ValidationRequest;
import com.pinc.android.MB360.onboarding.validation.ValidationResponse;

public class LoginViewModel extends AndroidViewModel {

    LoginRepository loginRepository;


    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginRepository = new LoginRepository(application);
    }

    public MutableLiveData<LoginResponse> loginWithMobileNumber(LoginRequest loginRequest) {
        return loginRepository.loginWithMobileNumber(loginRequest);
    }

    public MutableLiveData<LoginResponse> loginWithEmail(LoginEmailRequest loginEmailRequest) {
        return loginRepository.loginWithEmail(loginEmailRequest);
    }

    public MutableLiveData<LoginIDResponse> loginWithId(LoginIdRequest loginIdRequest){
        return loginRepository.loginWithId(loginIdRequest);
    }

    public MutableLiveData<ValidationResponse> validateOTP(ValidationRequest validationRequest) {
        return loginRepository.ValidateOTP(validationRequest);
    }

    public MutableLiveData<ValidationResponse> validateEmailOTP(ValidationEmailRequest validationEmailRequest) {
        return loginRepository.ValidateOTPWithEmail(validationEmailRequest);
    }

    public LiveData<LoginResponse> getLoginResponse() {
        return loginRepository.getLoginResponse();
    }

    public LiveData<LoginIDResponse> getLoginIDResponse() {
        return loginRepository.getLoginIDResponse();
    }
    public LiveData<ValidationResponse> getValidationResponse() {
        return loginRepository.getValidationResponse();
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return loginRepository.loadingState;
    }

    public MutableLiveData<Boolean> getErrorState() {
        return loginRepository.errorState;
    }


    public void resetOtpLoginResponse(){
        loginRepository.resetValidationResponse();
    }

    public void resetLoginResponse(){
        loginRepository.resetLoginResponse();
    }

}
