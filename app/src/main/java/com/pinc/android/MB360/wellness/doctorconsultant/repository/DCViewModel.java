package com.pinc.android.MB360.wellness.doctorconsultant.repository;

import android.app.Application;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.pinc.android.MB360.wellness.doctorconsultant.responseclass.AcceptUserAgreement;
import com.pinc.android.MB360.wellness.doctorconsultant.responseclass.DcPackageResponse;
import com.pinc.android.MB360.wellness.doctorconsultant.responseclass.DoctorConsultantPackagesResponse;
import com.pinc.android.MB360.wellness.doctorconsultant.responseclass.UserAgreement;
import com.pinc.android.MB360.wellness.doctorconsultant.responseclass.UserAgreementRequest;


public class DCViewModel extends AndroidViewModel {

    DCRepository dcRepository;

    public DCViewModel(@NonNull Application application) {
        super(application);
        dcRepository = new DCRepository(application);
    }


    public MutableLiveData<DoctorConsultantPackagesResponse> getPackages(String empSrNo, String vendorSrNo) {

        return dcRepository.getPackages(empSrNo, vendorSrNo);
    }

    public MutableLiveData<DoctorConsultantPackagesResponse> getPackagesDCResponseData() {
        return dcRepository.getPackagesDCResponseData();
    }


    public MutableLiveData<UserAgreement> isUserAgreed(String empSrNo, String vendorSrNo) {
        return dcRepository.isUserAgreed(empSrNo, vendorSrNo);
    }

    public MutableLiveData<UserAgreement> getUserAgreedData() {
        return dcRepository.getUserAgreedData();
    }


    public MutableLiveData<AcceptUserAgreement> acceptUserAgreement(UserAgreementRequest userAgreementRequest) {

        return dcRepository.acceptUserAgreement(userAgreementRequest);
    }

    public MutableLiveData<AcceptUserAgreement> acceptedUserData() {
        return dcRepository.acceptedUserData();
    }


    public MutableLiveData<DcPackageResponse> buyDcPackage(String personSrNo,
                                                           String empSrNo,
                                                           String packageSrNo) {
        return dcRepository.buyDcPackage(personSrNo, empSrNo, packageSrNo);
    }

    public MutableLiveData<DcPackageResponse> buyPackageData() {
        return dcRepository.buyPackageData();
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return dcRepository.getLoadingState();
    }

    public MutableLiveData<Boolean> getErrorState() {
        return dcRepository.getErrorState();
    }


}
