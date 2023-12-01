package com.pinc.android.MB360.wellness.healthcheckuppackages.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

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
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.MessageResponse;

public class PackagesViewModel extends AndroidViewModel {

    PackageRepository packageRepository;

    public PackagesViewModel(@NonNull Application application) {
        super(application);
        packageRepository = new PackageRepository(application);
    }

    public MutableLiveData<PackageResponse> getPackages(String extGroupSrNo, String groupCode, String empIdNo) {
        return packageRepository.getPackages(extGroupSrNo, groupCode, empIdNo);
    }

    public MutableLiveData<PackageResponse> getPackagesData() {
        return packageRepository.getPackagesData();
    }

    public MutableLiveData<DiagnosticCenterResponse> getDiagnosticCenter(String city) {
        return packageRepository.getDiagnosticCenter(city);
    }

    public MutableLiveData<DiagnosticCenterResponse> getDiagnosticCenterData() {
        return packageRepository.getDiagnosticCenterData();
    }

    public MutableLiveData<PackageDetailsResponse> getPackageDetails(String packageSrNo) {
        return packageRepository.getPackageDetails(packageSrNo);
    }

    public MutableLiveData<PackageDetailsResponse> getPackageDetailsData() {
        return packageRepository.getPackageDetailsData();
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return packageRepository.loadingState;
    }

    public MutableLiveData<Boolean> getErrorState() {
        return packageRepository.errorState;
    }


    public MutableLiveData<MessageResponse> scheduleHealthCheckup(ScheduleRequest scheduleRequest) {
        return packageRepository.scheduleHealthCheckup(scheduleRequest);
    }

    public MutableLiveData<MessageResponseDependent> dependent(DependentRequest dependentRequest) {
        return packageRepository.dependent(dependentRequest);
    }

    public MutableLiveData<AllRelationResponse> getAllRelation(String familySrNo) {
        return packageRepository.getAllRelation(familySrNo);
    }

    public MutableLiveData<AllRelationResponse> getAllRelationData() {
        return packageRepository.getAllRelationData();
    }

    public MutableLiveData<SummaryResponse> getSummary(String familySrNo, String groupCode) {
        return packageRepository.getSummary(familySrNo, groupCode);
    }

    public MutableLiveData<SummaryResponse> getSummaryData() {
        return packageRepository.getSummaryData();
    }

    public MutableLiveData<MessageResponseDependentDelete> dependentDelete(String personSrNo) {
        return packageRepository.dependentDelete(personSrNo);
    }

    public MutableLiveData<MessageResponseVerifyNo> verifyNo(VerifyNoRequest verifyNoRequest) {
        return packageRepository.verifyNo(verifyNoRequest);
    }

    public MutableLiveData<MessageResponseUpdatePayment> putUpdatePayment(HealthCheckupUpdatePaymentRequest healthCheckupUpdatePaymentRequest) {
        return packageRepository.putUpdatePayment(healthCheckupUpdatePaymentRequest);
    }

    public MutableLiveData<MessageResponseCancelAppointment> putCancelAppointment(HealthCheckupCancelAppointmentRequest healthCheckupCancelAppointmentRequest) {
        return packageRepository.putCancelAppointment(healthCheckupCancelAppointmentRequest);
    }

    public MutableLiveData<CityResponseHC> getCitiesHC() {
        return packageRepository.getCitiesHC();
    }

    public MutableLiveData<CityResponseHC> getCitiesData() {
        return packageRepository.getcitiesData();
    }

    public MutableLiveData<HealthCheckupOngoingResponse> getOngoingAppointment(String familySrNo, String extGroupSrNoString, String empIdNo, String groupCode) {
        return packageRepository.getOngoingAppointment(familySrNo, extGroupSrNoString, empIdNo, groupCode);
    }

    public MutableLiveData<HealthCheckupOngoingResponse> getOngoingAppointmentData() {
        return packageRepository.getOngoingAppointmentData();
    }

    public void resetCancelHC() {
        packageRepository.resetCancelHC();
    }

    public MutableLiveData<String> getCitiesLiveDataHC() {
        return packageRepository.getCitiesLiveDataHC();
    }

    public void setCitiesLiveDataHC(String city) {
        packageRepository.setCitiesLiveDataHC(city);
    }
}
