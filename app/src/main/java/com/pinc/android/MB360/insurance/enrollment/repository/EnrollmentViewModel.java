package com.pinc.android.MB360.insurance.enrollment.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.DependantDetailsResponse;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.DependantHelperModel;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.EmployeeResponse;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.EnrollmentSummaryResponse;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.InstructionResponse;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.MyCoveragesResponse;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.TopupResponse;

public class EnrollmentViewModel extends AndroidViewModel {

    EnrollmentRepository enrollmentRepository;

    public EnrollmentViewModel(@NonNull Application application) {
        super(application);
        enrollmentRepository = new EnrollmentRepository(application);
    }


    public LiveData<InstructionResponse> getInstructions() {
        return enrollmentRepository.getInstructionResponse();
    }

    public LiveData<InstructionResponse> getInstructionsData() {
        return enrollmentRepository.getInstructionResponseData();
    }

    public LiveData<MyCoveragesResponse> getCoverages() {
        return enrollmentRepository.getCoveragesResponse();
    }

    public LiveData<MyCoveragesResponse> getCoveragesData() {
        return enrollmentRepository.getCoveragesData();
    }

    public LiveData<EmployeeResponse> getEmployee() {
        return enrollmentRepository.getEmployeeResponse();
    }

    public LiveData<EmployeeResponse> getEmployeeData() {
        return enrollmentRepository.getEmployeeData();
    }

    public LiveData<DependantDetailsResponse> getDependantDetails() {
        return enrollmentRepository.getDependantDetail();
    }

    public LiveData<DependantDetailsResponse> getDependantDetailsData() {
        return enrollmentRepository.getDependantDetailData();
    }


    public LiveData<DependantDetailsResponse> getParental() {
        return enrollmentRepository.getParental();
    }

    public LiveData<DependantDetailsResponse> getParentalData() {
        return enrollmentRepository.getParentalData();
    }

    public LiveData<TopupResponse> getTopups() {
        return enrollmentRepository.getTopUps();
    }

    public LiveData<TopupResponse> getTopUpsData() {
        return enrollmentRepository.getTopUpsData();
    }

    public MutableLiveData<EnrollmentSummaryResponse> getSummary() {
        return enrollmentRepository.getSummary();
    }

    public MutableLiveData<EnrollmentSummaryResponse> getSummaryData() {
        return enrollmentRepository.getSummaryData();
    }


    public LiveData<Boolean> getLoading() {
        return enrollmentRepository.loadingState;
    }

    public LiveData<Boolean> getError() {
        return enrollmentRepository.errorState;
    }

    public LiveData<DependantHelperModel> getTwin1() {
        return enrollmentRepository.getTwin1();
    }

    public LiveData<DependantHelperModel> getTwin2() {
        return enrollmentRepository.getTwin2();
    }

    public void setTwin1(DependantHelperModel dependantHelperModel) {
        enrollmentRepository.setTwin1(dependantHelperModel);
    }

    public void setTwin2(DependantHelperModel dependantHelperModel) {
        enrollmentRepository.setTwin2(dependantHelperModel);
    }
}
