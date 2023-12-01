package com.pinc.android.MB360.wellness.doctorconsultant.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.wellness.doctorconsultant.responseclass.AcceptUserAgreement;
import com.pinc.android.MB360.wellness.doctorconsultant.responseclass.DcPackageResponse;
import com.pinc.android.MB360.wellness.doctorconsultant.responseclass.DoctorConsultantPackagesResponse;
import com.pinc.android.MB360.wellness.doctorconsultant.responseclass.UserAgreement;
import com.pinc.android.MB360.wellness.doctorconsultant.responseclass.UserAgreementRequest;
import com.pinc.android.MB360.wellness.doctorconsultant.retrofit.DoctorConsultationRetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * this class has the
 * business logic for DOCTOR Consultant
 * in users
 **/
public class DCRepository {

    public final MutableLiveData<DoctorConsultantPackagesResponse> packagesDCResponse;
    public final MutableLiveData<UserAgreement> userAgreedResponse;
    public final MutableLiveData<AcceptUserAgreement> acceptUserAgreementResponse;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    public final MutableLiveData<DcPackageResponse> buyDCPackageResponse;
    Application application;

    public DCRepository(Application application) {
        this.packagesDCResponse = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();
        this.application = application;
        this.userAgreedResponse = new MutableLiveData<>();
        this.acceptUserAgreementResponse = new MutableLiveData<>();
        this.buyDCPackageResponse = new MutableLiveData<>();

    }


    public MutableLiveData<DoctorConsultantPackagesResponse> getPackages(String empSrNo, String vendorSrNo) {
        Call<DoctorConsultantPackagesResponse> packageCall = DoctorConsultationRetrofitClient.getInstance().getDCApi().getDCPackages(empSrNo, vendorSrNo);

        packageCall.enqueue(new Callback<DoctorConsultantPackagesResponse>() {
            @Override
            public void onResponse(Call<DoctorConsultantPackagesResponse> call, Response<DoctorConsultantPackagesResponse> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.DOCTOR_CONSULTANT, "onResponse: " + response.body());
                        packagesDCResponse.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.DOCTOR_CONSULTANT, "ERROR: ", e);
                        packagesDCResponse.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    Log.d(LogTags.DOCTOR_CONSULTANT, "onResponse: FAILED" + response.code());
                    packagesDCResponse.setValue(null);
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<DoctorConsultantPackagesResponse> call, Throwable t) {
                Log.e(LogTags.DOCTOR_CONSULTANT, "Error: " + t.getLocalizedMessage());
                errorState.setValue(true);
                loadingState.setValue(false);
                Toast.makeText(application, "Something went wrong, reason: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

        return packagesDCResponse;
    }

    public MutableLiveData<DoctorConsultantPackagesResponse> getPackagesDCResponseData() {
        return packagesDCResponse;
    }


    public MutableLiveData<UserAgreement> isUserAgreed(String empSrNo, String vendorSrNo) {
        Call<UserAgreement> userAgreementCall = DoctorConsultationRetrofitClient.getInstance().getDCApi().isUserAgreed(empSrNo, vendorSrNo);

        userAgreementCall.enqueue(new Callback<UserAgreement>() {
            @Override
            public void onResponse(Call<UserAgreement> call, Response<UserAgreement> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.DOCTOR_CONSULTANT, "onResponse: " + response.body());
                        userAgreedResponse.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.DOCTOR_CONSULTANT, "ERROR: ", e);
                        userAgreedResponse.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    Log.d(LogTags.DOCTOR_CONSULTANT, "onResponse: FAILED " + response.code());
                    //to see why its error
                    userAgreedResponse.setValue(null);
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<UserAgreement> call, Throwable t) {
                Log.e(LogTags.DOCTOR_CONSULTANT, "OnFailed: FAILED", t);
                userAgreedResponse.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });

        return userAgreedResponse;
    }

    public MutableLiveData<UserAgreement> getUserAgreedData() {
        return userAgreedResponse;
    }


    public MutableLiveData<AcceptUserAgreement> acceptUserAgreement(UserAgreementRequest userAgreementRequest) {
        Log.d(LogTags.DOCTOR_CONSULTANT, "acceptUserAgreement: "+userAgreementRequest.toString());
        Call<AcceptUserAgreement> userAgreementCall = DoctorConsultationRetrofitClient.getInstance().getDCApi().acceptUserAgreement(userAgreementRequest);

        userAgreementCall.enqueue(new Callback<AcceptUserAgreement>() {
            @Override
            public void onResponse(Call<AcceptUserAgreement> call, Response<AcceptUserAgreement> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.DOCTOR_CONSULTANT, "onResponse: " + response.body());
                        acceptUserAgreementResponse.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.DOCTOR_CONSULTANT, "ERROR: ", e);
                        acceptUserAgreementResponse.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    Log.d(LogTags.DOCTOR_CONSULTANT, "onResponse: FAILED" + response.code());
                    acceptUserAgreementResponse.setValue(null);
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<AcceptUserAgreement> call, Throwable t) {
                Log.e(LogTags.DOCTOR_CONSULTANT, "OnFailed: FAILED", t);
                acceptUserAgreementResponse.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });

        return acceptUserAgreementResponse;
    }

    public MutableLiveData<AcceptUserAgreement> acceptedUserData() {
        return acceptUserAgreementResponse;
    }


    public MutableLiveData<DcPackageResponse> buyDcPackage(String personSrNo,
                                                           String empSrNo,
                                                           String packageSrNo) {
        Call<DcPackageResponse> buyPackageCall = DoctorConsultationRetrofitClient.getInstance().getDCApi().buyDCPackage(personSrNo, empSrNo, packageSrNo);

        buyPackageCall.enqueue(new Callback<DcPackageResponse>() {
            @Override
            public void onResponse(Call<DcPackageResponse> call, Response<DcPackageResponse> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.DOCTOR_CONSULTANT, "onResponse: " + response.body());
                        buyDCPackageResponse.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.DOCTOR_CONSULTANT, "ERROR: ", e);
                        buyDCPackageResponse.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    Log.d(LogTags.DOCTOR_CONSULTANT, "onResponse: FAILED" + response.code());
                    buyDCPackageResponse.setValue(null);
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<DcPackageResponse> call, Throwable t) {
                Log.e(LogTags.DOCTOR_CONSULTANT, "OnFailed: FAILED", t);
                buyDCPackageResponse.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });

        return buyDCPackageResponse;
    }

    public MutableLiveData<DcPackageResponse> buyPackageData() {
        return buyDCPackageResponse;
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return loadingState;
    }

    public MutableLiveData<Boolean> getErrorState() {
        return errorState;
    }
}
