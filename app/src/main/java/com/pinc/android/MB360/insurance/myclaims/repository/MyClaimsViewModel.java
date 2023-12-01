package com.pinc.android.MB360.insurance.myclaims.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pinc.android.MB360.insurance.myclaims.responseclass.Claims;
import com.pinc.android.MB360.insurance.myclaims.responseclass.DocumentElement;
import com.pinc.android.MB360.insurance.myclaims.responseclass.claimsdetails.ClaimsDetails;

public class MyClaimsViewModel extends AndroidViewModel {
    MyClaimsRepository myClaimsRepository;


    public MyClaimsViewModel(@NonNull Application application) {
        super(application);
        myClaimsRepository = new MyClaimsRepository(application);
    }


    public LiveData<DocumentElement> getMyClaims(String dataRequest) {

        return myClaimsRepository.getMyClaims(dataRequest);
    }

    public LiveData<DocumentElement> getMyClaimsData() {
        return myClaimsRepository.getMyClaimsData();
    }


    public LiveData<ClaimsDetails> getMyClaimsDetails(String dataRequest,String claimSrNo) {

        return myClaimsRepository.getMyClaimsDetails(dataRequest,claimSrNo);
    }

    public LiveData<ClaimsDetails> getMyClaimsDetailsData() {
        return myClaimsRepository.getMyClaimsDetailsData();
    }


    public LiveData<Boolean> getLoading() {
        return myClaimsRepository.getLoading();
    }

    public LiveData<Boolean> getError() {
        return myClaimsRepository.getError();
    }


    public MutableLiveData<Claims> getSelectedClaim() {
        return myClaimsRepository.getSelectedClaim();
    }

    public void setSelectedClaim(Claims claims) {
        myClaimsRepository.setSelectedClaim(claims);
    }


    public MutableLiveData<String> getErrorDescription() {
        return myClaimsRepository.getErrorDescription();
    }

}
