package com.pinc.android.MB360.wellness.dashboardwellness.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.pinc.android.MB360.wellness.dashboardwellness.repository.responseclass.DashboardServiceResponse;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.responseclass.EmployeeCheckResponse;

public class DashboardWellnessViewModel extends AndroidViewModel {

    DashBoardWellnessRepository dashBoardWellnessRepository;

    public DashboardWellnessViewModel(@NonNull Application application) {
        super(application);
        dashBoardWellnessRepository = new DashBoardWellnessRepository(application);
    }

    public MutableLiveData<DashboardServiceResponse> getDashboardWellness(String agent, String grpChildSrNo) {
        return dashBoardWellnessRepository.getDashboardWellness(agent, grpChildSrNo);
    }

    public MutableLiveData<EmployeeCheckResponse> getEmployeeWellnessDetails(String empIdNo, String groupCode){
     return dashBoardWellnessRepository.getEmployeeWellnessDetails(empIdNo, groupCode);
    }

    public  MutableLiveData<EmployeeCheckResponse> getEmployeeWellnessDetailsData(){
        return dashBoardWellnessRepository.getEmployeeWellnessDetailsData();
    }

    public MutableLiveData<DashboardServiceResponse> getDashboardWellnessData() {
        return dashBoardWellnessRepository.getDashboardWellnessData();
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return dashBoardWellnessRepository.loadingState;
    }

    public MutableLiveData<Boolean> getErrorState() {
        return dashBoardWellnessRepository.errorState;
    }
}
