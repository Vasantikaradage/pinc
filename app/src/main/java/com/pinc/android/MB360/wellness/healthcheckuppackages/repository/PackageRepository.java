package com.pinc.android.MB360.wellness.healthcheckuppackages.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.utilities.ResponseException;
import com.pinc.android.MB360.wellness.healthcheckup.repository.requestclass.DependentRequest;
import com.pinc.android.MB360.wellness.healthcheckup.repository.responseclass.MessageResponseDependent;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.requestclass.ScheduleRequest;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.AllRelationResponse;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.CityResponseHC;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.DiagnosticCenterResponse;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.HealthCheckupCancelAppointmentRequest;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.HealthCheckupOngoingResponse;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.HealthCheckupUpdatePaymentRequest;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.MessageResponseCancelAppointment;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.MessageResponseDependentDelete;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.MessageResponseUpdatePayment;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.MessageResponseVerifyNo;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.PackageDetailsResponse;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.PackageResponse;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.SummaryResponse;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.VerifyNoRequest;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.retrofit.PackageRetrofitClient;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.MessageResponse;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * this class has the
 * business logic for CLaims {@link PackageRepository }
 * in users
 **/
public class PackageRepository {

    private final MutableLiveData<PackageResponse> packageMutableLiveData;
    private final MutableLiveData<DiagnosticCenterResponse> diagnosticCenterResponseMutableLiveData;
    private final MutableLiveData<PackageDetailsResponse> packageDetailsResponseMutableLiveData;
    private final MutableLiveData<MessageResponse> messageResponseMutableLiveData;
    private final MutableLiveData<MessageResponseDependent> messageResponseDependentMutableLiveData;
    private final MutableLiveData<AllRelationResponse> allRelationResponseMutableLiveData;
    private final MutableLiveData<SummaryResponse> summaryResponseMutableLiveData;
    private final MutableLiveData<MessageResponseDependentDelete> messageResponseDependentDeleteMutableLiveData;
    private final MutableLiveData<MessageResponseVerifyNo> messageResponseVerifyNoMutableLiveData;
    private final MutableLiveData<MessageResponseUpdatePayment> messageResponseUpdatePaymentMutableLiveData;
    private final MutableLiveData<MessageResponseCancelAppointment> messageResponseCancelAppointmentMutableLiveData;
    private final MutableLiveData<CityResponseHC> cityResponseMutableLiveData;
    private final MutableLiveData<HealthCheckupOngoingResponse> healthCheckupOngoingResponseMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    Application application;
    FirebaseCrashlytics crashlytics;


    //health Check Up cities
    public  MutableLiveData<String> citiesLiveDataHC;


    public PackageRepository(Application application) {
        this.packageMutableLiveData = new MutableLiveData<>();
        this.diagnosticCenterResponseMutableLiveData = new MutableLiveData<>();
        this.packageDetailsResponseMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();
        this.application = application;
        messageResponseMutableLiveData = new MutableLiveData<>();
        messageResponseDependentMutableLiveData = new MutableLiveData<>();
        allRelationResponseMutableLiveData = new MutableLiveData<>();
        summaryResponseMutableLiveData = new MutableLiveData<>();
        messageResponseDependentDeleteMutableLiveData = new MutableLiveData<>();
        messageResponseVerifyNoMutableLiveData = new MutableLiveData<>();
        messageResponseUpdatePaymentMutableLiveData = new MutableLiveData<>();
        messageResponseCancelAppointmentMutableLiveData = new MutableLiveData<>();
        healthCheckupOngoingResponseMutableLiveData = new MutableLiveData<>();
        this.cityResponseMutableLiveData = new MutableLiveData<>();
        this.citiesLiveDataHC = new MutableLiveData<>();
        crashlytics = FirebaseCrashlytics.getInstance();
    }

    /**
     * this function  has the business logic for calling
     * and parsing the @PolicyFeatures {@link PackageResponse } response
     **/

    public MutableLiveData<PackageResponse> getPackages(String extGroupSrNo, String groupCode, String empIdNo) {
        loadingState.setValue(true);
        if (packageDetailsResponseMutableLiveData.getValue() == null) {
            Call<PackageResponse> packagesCall = PackageRetrofitClient.getInstance().getPackageApi().getPackage(extGroupSrNo, groupCode, empIdNo);
            packagesCall.enqueue(new Callback<PackageResponse>() {
                @Override
                public void onResponse(Call<PackageResponse> call, Response<PackageResponse> response) {
                    if (response.code() == 200 && response.body().getMessage().getStatus()) {
                        try {
                            Log.d(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "onResponse: " + response.body().toString());
                            packageMutableLiveData.setValue(response.body());
                            errorState.setValue(false);
                            loadingState.setValue(false);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "Error: ", e);
                            packageMutableLiveData.setValue(null);
                            errorState.setValue(true);
                            loadingState.setValue(false);
                            Toast.makeText(application, "Something went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.body().getMessage() != null && !response.body().getMessage().getStatus()) {
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        Toast.makeText(application, "Something went wrong: " + response.body().getMessage().getMessage(), Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<PackageResponse> call, Throwable t) {

                    Log.e(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "Error: " + t.getLocalizedMessage());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    throwFirebaseError(t);

                }
            });
        }

        return packageMutableLiveData;
    }

    private void throwFirebaseError(Throwable t) {
        //throwing the issues in firebase
        ResponseException exception = new ResponseException(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY + " : Response Code : " + " | Message " + t.getLocalizedMessage());
        Throwable throwable = new Throwable(exception);
        crashlytics.recordException(throwable);
    }

    public MutableLiveData<PackageResponse> getPackagesData() {
        return packageMutableLiveData;

    }

    public MutableLiveData<DiagnosticCenterResponse> getDiagnosticCenter(String city) {
        loadingState.setValue(true);
        Call<DiagnosticCenterResponse> diagnosticCenterResponseCall = PackageRetrofitClient.getInstance().getPackageApi().getDiagnosticCenter(city);
        diagnosticCenterResponseCall.enqueue(new Callback<DiagnosticCenterResponse>() {
            @Override
            public void onResponse(Call<DiagnosticCenterResponse> call, Response<DiagnosticCenterResponse> response) {

                if (response.code() == 200 && response.body().getMessage().getStatus()) {
                    try {
                        Log.d(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "onResponse: " + response.body().toString());
                        diagnosticCenterResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "Error: ", e);
                        diagnosticCenterResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        Toast.makeText(application, "Something went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.body().getMessage() != null && !response.body().getMessage().getStatus()) {
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    Log.d(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "onError: " + response.body().toString());

                    Toast.makeText(application, "Something went wrong: " + response.body().getMessage().getMessage(), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<DiagnosticCenterResponse> call, Throwable t) {

            }
        });

        return diagnosticCenterResponseMutableLiveData;
    }

    public MutableLiveData<DiagnosticCenterResponse> getDiagnosticCenterData() {
        return diagnosticCenterResponseMutableLiveData;
    }

    public MutableLiveData<PackageDetailsResponse> getPackageDetails(String packageSrNo) {
        loadingState.setValue(true);
        Call<PackageDetailsResponse> packageDetailsResponseCall = PackageRetrofitClient.getInstance().getPackageApi().getPackageDetails(packageSrNo);
        packageDetailsResponseCall.enqueue(new Callback<PackageDetailsResponse>() {
            @Override
            public void onResponse(Call<PackageDetailsResponse> call, Response<PackageDetailsResponse> response) {

                if (response.code() == 200 && response.body().getMessage().getStatus()) {
                    try {
                        Log.d(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "onResponse: " + response.body().toString());
                        packageDetailsResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "Error: ", e);
                        packageDetailsResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        Toast.makeText(application, "Something went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.body().getMessage() != null && !response.body().getMessage().getStatus()) {
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    Toast.makeText(application, "Something went wrong: " + response.body().getMessage().getMessage(), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<PackageDetailsResponse> call, Throwable t) {

            }
        });

        return packageDetailsResponseMutableLiveData;
    }

    public MutableLiveData<PackageDetailsResponse> getPackageDetailsData() {
        return packageDetailsResponseMutableLiveData;
    }

    public MutableLiveData<MessageResponse> scheduleHealthCheckup(ScheduleRequest scheduleRequest) {
        loadingState.setValue(true);
        Call<MessageResponse> scheduleCall = PackageRetrofitClient.getInstance().getPackageApi().startSchedule(scheduleRequest);

        scheduleCall.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                        messageResponseMutableLiveData.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        messageResponseMutableLiveData.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    Log.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                    messageResponseMutableLiveData.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Log.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "onFailure: ", t);
                messageResponseMutableLiveData.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });
        return messageResponseMutableLiveData;
    }

    public MutableLiveData<MessageResponseDependent> dependent(DependentRequest dependentRequest) {
        loadingState.setValue(true);
        Call<MessageResponseDependent> dependentCall = PackageRetrofitClient.getInstance().getPackageApi().postAddMember(dependentRequest);

        dependentCall.enqueue(new Callback<MessageResponseDependent>() {
            @Override
            public void onResponse(Call<MessageResponseDependent> call, Response<MessageResponseDependent> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                        messageResponseDependentMutableLiveData.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        messageResponseDependentMutableLiveData.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    Log.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                    messageResponseDependentMutableLiveData.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<MessageResponseDependent> call, Throwable t) {
                Log.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "onFailure: ", t);
                messageResponseDependentMutableLiveData.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });
        return messageResponseDependentMutableLiveData;
    }

    public MutableLiveData<AllRelationResponse> getAllRelation(String familySrNo) {
        loadingState.setValue(true);
        Call<AllRelationResponse> allRelationResponseCall = PackageRetrofitClient.getInstance().getPackageApi().getAllRelation(familySrNo);
        allRelationResponseCall.enqueue(new Callback<AllRelationResponse>() {
            @Override
            public void onResponse(Call<AllRelationResponse> call, Response<AllRelationResponse> response) {

                if (response.code() == 200 && response.body().getMessage().getStatus()) {
                    try {
                        Log.d(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "onResponse: " + response.body().toString());
                        allRelationResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "Error: ", e);
                        allRelationResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        Toast.makeText(application, "Something went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.body().getMessage() != null && !response.body().getMessage().getStatus()) {
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    Toast.makeText(application, "Something went wrong: " + response.body().getMessage().getMessage(), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<AllRelationResponse> call, Throwable t) {

            }
        });

        return allRelationResponseMutableLiveData;
    }

    public MutableLiveData<AllRelationResponse> getAllRelationData() {
        return allRelationResponseMutableLiveData;
    }

    public MutableLiveData<SummaryResponse> getSummary(String familySrNo, String groupCode) {
        loadingState.setValue(true);
        Call<SummaryResponse> summaryResponseCall = PackageRetrofitClient.getInstance().getPackageApi().getSummary(familySrNo, groupCode);
        summaryResponseCall.enqueue(new Callback<SummaryResponse>() {
            @Override
            public void onResponse(Call<SummaryResponse> call, Response<SummaryResponse> response) {

                if (response.code() == 200 && response.body().getMessage().getStatus()) {
                    try {
                        Log.d(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "onResponse: " + response.body().toString());
                        summaryResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "Error: ", e);
                        summaryResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
//                        Toast.makeText(application, "Something Went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else if (response.body().getMessage() != null && !response.body().getMessage().getStatus()) {
                    errorState.setValue(true);
                    loadingState.setValue(false);
//                    Toast.makeText(application, "Something Went wrong: " + response.body().getMessage().getMessage(), Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<SummaryResponse> call, Throwable t) {

            }
        });

        return summaryResponseMutableLiveData;
    }

    public MutableLiveData<SummaryResponse> getSummaryData() {

        return summaryResponseMutableLiveData;
    }

    public MutableLiveData<MessageResponseDependentDelete> dependentDelete(String personSrNo) {
        loadingState.setValue(true);
        Call<MessageResponseDependentDelete> dependentDeleteCall = PackageRetrofitClient.getInstance().getPackageApi().deleteMember(personSrNo);

        dependentDeleteCall.enqueue(new Callback<MessageResponseDependentDelete>() {
            @Override
            public void onResponse(Call<MessageResponseDependentDelete> call, Response<MessageResponseDependentDelete> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                        messageResponseDependentDeleteMutableLiveData.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        messageResponseDependentDeleteMutableLiveData.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    Log.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                    messageResponseDependentDeleteMutableLiveData.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<MessageResponseDependentDelete> call, Throwable t) {
                Log.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "onFailure: ", t);
                messageResponseDependentMutableLiveData.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });
        return messageResponseDependentDeleteMutableLiveData;
    }

    public MutableLiveData<MessageResponseVerifyNo> verifyNo(VerifyNoRequest verifyNoRequest) {
        loadingState.setValue(true);
        Call<MessageResponseVerifyNo> verifyNoCall = PackageRetrofitClient.getInstance().getPackageApi().putVerifyNo(verifyNoRequest);

        verifyNoCall.enqueue(new Callback<MessageResponseVerifyNo>() {
            @Override
            public void onResponse(Call<MessageResponseVerifyNo> call, Response<MessageResponseVerifyNo> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                        messageResponseVerifyNoMutableLiveData.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        messageResponseVerifyNoMutableLiveData.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    Log.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                    messageResponseVerifyNoMutableLiveData.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<MessageResponseVerifyNo> call, Throwable t) {
                Log.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "onFailure: ", t);
                messageResponseVerifyNoMutableLiveData.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });
        return messageResponseVerifyNoMutableLiveData;
    }

    public MutableLiveData<MessageResponseUpdatePayment> putUpdatePayment(HealthCheckupUpdatePaymentRequest healthCheckupUpdatePaymentRequest) {
        loadingState.setValue(true);
        Call<MessageResponseUpdatePayment> updatePaymentCall = PackageRetrofitClient.getInstance().getPackageApi().putUpdatePayment(healthCheckupUpdatePaymentRequest);

        updatePaymentCall.enqueue(new Callback<MessageResponseUpdatePayment>() {
            @Override
            public void onResponse(Call<MessageResponseUpdatePayment> call, Response<MessageResponseUpdatePayment> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                        messageResponseUpdatePaymentMutableLiveData.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        messageResponseUpdatePaymentMutableLiveData.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    Log.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                    messageResponseUpdatePaymentMutableLiveData.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<MessageResponseUpdatePayment> call, Throwable t) {
                Log.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "onFailure: ", t);
                messageResponseUpdatePaymentMutableLiveData.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });
        return messageResponseUpdatePaymentMutableLiveData;
    }

    public MutableLiveData<MessageResponseCancelAppointment> putCancelAppointment(HealthCheckupCancelAppointmentRequest healthCheckupCancelAppointmentRequest) {
        loadingState.setValue(true);
        Call<MessageResponseCancelAppointment> cancelAppointmentCall = PackageRetrofitClient.getInstance().getPackageApi().putCancelAppointment(healthCheckupCancelAppointmentRequest);

        cancelAppointmentCall.enqueue(new Callback<MessageResponseCancelAppointment>() {
            @Override
            public void onResponse(Call<MessageResponseCancelAppointment> call, Response<MessageResponseCancelAppointment> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                        messageResponseCancelAppointmentMutableLiveData.setValue(response.body());
                        loadingState.setValue(false);
                        errorState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        messageResponseCancelAppointmentMutableLiveData.setValue(null);
                        loadingState.setValue(false);
                        errorState.setValue(true);
                    }
                } else {
                    Log.d(LogTags.HEALTH_CHECKUP_ACTIVITY, "onResponse: " + response.body());
                    messageResponseCancelAppointmentMutableLiveData.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<MessageResponseCancelAppointment> call, Throwable t) {
                Log.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "onFailure: ", t);
                messageResponseCancelAppointmentMutableLiveData.setValue(null);
                loadingState.setValue(false);
                errorState.setValue(true);
            }
        });
        return messageResponseCancelAppointmentMutableLiveData;
    }


    public MutableLiveData<CityResponseHC> getCitiesHC() {
        loadingState.setValue(true);
        if (cityResponseMutableLiveData.getValue() == null) {
            Call<CityResponseHC> citycall = PackageRetrofitClient.getInstance().getPackageApi().getcitiesHC();
            citycall.enqueue(new Callback<CityResponseHC>() {
                @Override
                public void onResponse(Call<CityResponseHC> call, Response<CityResponseHC> response) {
                    if (response.code() == 200 && response.body().getMessage().getStatus()) {
                        try {
                            Log.d(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "onResponse: " + response.body().toString());
                            cityResponseMutableLiveData.setValue(response.body());
                            errorState.setValue(false);
                            loadingState.setValue(false);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "Error: ", e);
                            cityResponseMutableLiveData.setValue(null);
                            errorState.setValue(true);
                            loadingState.setValue(false);
                            Toast.makeText(application, "Something went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else if (response.body().getMessage() != null && !response.body().getMessage().getStatus()) {
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        Toast.makeText(application, "Something went wrong: " + response.body().getMessage().getMessage(), Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<CityResponseHC> call, Throwable t) {

                }
            });
        }

        return cityResponseMutableLiveData;
    }

    public MutableLiveData<CityResponseHC> getcitiesData() {
        return cityResponseMutableLiveData;

    }

    public MutableLiveData<HealthCheckupOngoingResponse> getOngoingAppointment(String familySrNo, String extGroupSrNoString, String empIdNo, String groupCode) {
        loadingState.setValue(true);
        Call<HealthCheckupOngoingResponse> healthCheckupOngoingResponseCall = PackageRetrofitClient.getInstance().getPackageApi().getOngoingAppointment(familySrNo, extGroupSrNoString, empIdNo, groupCode);
        healthCheckupOngoingResponseCall.enqueue(new Callback<HealthCheckupOngoingResponse>() {
            @Override
            public void onResponse(Call<HealthCheckupOngoingResponse> call, Response<HealthCheckupOngoingResponse> response) {

                if (response.code() == 200 && response.body().getMessage().getStatus()) {
                    try {
                        Log.d(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "onResponse: " + response.body().toString());
                        healthCheckupOngoingResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "Error: ", e);
                        healthCheckupOngoingResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
//                        Toast.makeText(application, "Something Went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        
                    }
                } else if (response.body().getMessage() != null && !response.body().getMessage().getStatus()) {
                    errorState.setValue(true);
                    loadingState.setValue(false);
//                    Toast.makeText(application, "Something Went wrong: " + response.body().getMessage().getMessage(), Toast.LENGTH_LONG).show();

                } else {
//                    Toast.makeText(application, "Something Went wrong: " + response.body().getMessage().getMessage(), Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<HealthCheckupOngoingResponse> call, Throwable t) {

            }
        });

        return healthCheckupOngoingResponseMutableLiveData;
    }

    public MutableLiveData<HealthCheckupOngoingResponse> getOngoingAppointmentData() {

        return healthCheckupOngoingResponseMutableLiveData;
    }

    public void resetCancelHC() {
        messageResponseCancelAppointmentMutableLiveData.setValue(null);
    }

    public MutableLiveData<String> getCitiesLiveDataHC() {
        return citiesLiveDataHC;
    }

    public void setCitiesLiveDataHC(String city) {
        citiesLiveDataHC.setValue(city);
    }
}
