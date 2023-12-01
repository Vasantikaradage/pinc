package com.pinc.android.MB360.wellness.healthcheckup.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.pinc.android.MB360.wellness.healthcheckup.repository.responseclass.HealthCheckupOverviewResponse;

public class HealthCheckupOverviewViewModel extends AndroidViewModel {

    HealthCheckupOverviewRepository healthCheckupOverviewRepository;

    public HealthCheckupOverviewViewModel(@NonNull Application application) {
        super(application);
        healthCheckupOverviewRepository = new HealthCheckupOverviewRepository(application);
    }

    public MutableLiveData<HealthCheckupOverviewResponse> getHealthCheckupOverview(String externalGroupSrNo, String agent) {
        return healthCheckupOverviewRepository.getHealthCheckupOverview(externalGroupSrNo, agent);
    }

    public MutableLiveData<HealthCheckupOverviewResponse> getHealthCheckupOverviewData() {
        return healthCheckupOverviewRepository.getHealthCheckupOverviewData();
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return healthCheckupOverviewRepository.loadingState;
    }

    public MutableLiveData<Boolean> getErrorState() {
        return healthCheckupOverviewRepository.errorState;
    }
}
