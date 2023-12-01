package com.pinc.android.MB360.insurance.ecards.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pinc.android.MB360.insurance.ecards.reponseclass.EcardResponse;
import com.pinc.android.MB360.insurance.ecards.reponseclass.EcardResponseJson;

import java.util.Map;

public class EcardViewModel extends AndroidViewModel {
    EcardRepository ecardRepository;

    public EcardViewModel(@NonNull Application application) {
        super(application);
        ecardRepository = new EcardRepository(application);
    }


    public LiveData<EcardResponse> getEcard(String dataRequest) {
        return ecardRepository.getEcard(dataRequest);
    }

    public LiveData<EcardResponse> getEcardData() {
        return ecardRepository.getEcardData();
    }

    public MutableLiveData<Boolean> getLoading() {
        return ecardRepository.getLoading();
    }

    public MutableLiveData<Boolean> getError() {
        return ecardRepository.getError();
    }

    public LiveData<EcardResponseJson> getEcardJson(Map<String, String> ecardMapOptions) {
        return ecardRepository.getEcardJson(ecardMapOptions);
    }


}
