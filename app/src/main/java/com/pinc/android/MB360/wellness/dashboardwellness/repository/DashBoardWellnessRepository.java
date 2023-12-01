package com.pinc.android.MB360.wellness.dashboardwellness.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.responseclass.DashboardServiceResponse;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.responseclass.EmployeeCheckResponse;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.retrofit.DashboardWellnessRetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * this class has the
 * business logic for CLaims {@link DashBoardWellnessRepository }
 * in users
 **/
public class DashBoardWellnessRepository {

    private final MutableLiveData<DashboardServiceResponse> dashboardServiceResponseMutableLiveData;
    private final MutableLiveData<EmployeeCheckResponse> employeeCheckResponseMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    Application application;


    public DashBoardWellnessRepository(Application application) {
        this.dashboardServiceResponseMutableLiveData = new MutableLiveData<>();
        this.employeeCheckResponseMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();
        this.application = application;
    }

    /**
     * this function  has the business logic for calling
     * and parsing the @PolicyFeatures {@link DashboardServiceResponse } response
     **/

    public MutableLiveData<DashboardServiceResponse> getDashboardWellness(String agent, String groupChildSrNo) {

        if (dashboardServiceResponseMutableLiveData.getValue() == null) {
            Call<DashboardServiceResponse> dashboardServiceCall = DashboardWellnessRetrofitClient.getInstance().getDashboardWellnessApi().getDashboardService(agent, groupChildSrNo);
            dashboardServiceCall.enqueue(new Callback<DashboardServiceResponse>() {
                @Override
                public void onResponse(Call<DashboardServiceResponse> call, Response<DashboardServiceResponse> response) {
                    if (response.code() == 200) {
                        try {
                            Log.d(LogTags.WELLNESS_DASHBOARD_ACTIVITY, "onResponse: " + response.body().toString());
                            dashboardServiceResponseMutableLiveData.setValue(response.body());
                            errorState.setValue(false);
                            loadingState.setValue(false);

                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(LogTags.WELLNESS_DASHBOARD_ACTIVITY, "Error: ", e);
                            dashboardServiceResponseMutableLiveData.setValue(null);
                            errorState.setValue(true);
                            loadingState.setValue(false);
                            Toast.makeText(application, "Something went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        dashboardServiceResponseMutableLiveData.setValue(response.body());
                        Toast.makeText(application, "Something went wrong error code: " + response.code(), Toast.LENGTH_LONG).show();

                    }
                }

                @Override
                public void onFailure(Call<DashboardServiceResponse> call, Throwable t) {

                    errorState.setValue(true);
                    loadingState.setValue(false);
                    Toast.makeText(application, "Something went wrong : " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else {

        }

        return dashboardServiceResponseMutableLiveData;
    }

    public MutableLiveData<DashboardServiceResponse> getDashboardWellnessData() {
        return dashboardServiceResponseMutableLiveData;

    }

    public MutableLiveData<EmployeeCheckResponse> getEmployeeWellnessDetails(String empIdNo, String groupCode) {

        Call<EmployeeCheckResponse> getEmployeeCheckResponse = DashboardWellnessRetrofitClient.getInstance().getDashboardWellnessApi().getEmployeeWellnessDetails(empIdNo, groupCode);
        getEmployeeCheckResponse.enqueue(new Callback<EmployeeCheckResponse>() {
            @Override
            public void onResponse(Call<EmployeeCheckResponse> call, Response<EmployeeCheckResponse> response) {

                if (response.code() == 200) {
                    try {

                        Log.d(LogTags.WELLNESS_DASHBOARD_ACTIVITY, "onResponse: " + response.body().toString());
                        employeeCheckResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.WELLNESS_DASHBOARD_ACTIVITY, "Error: ", e);
                        employeeCheckResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        Toast.makeText(application, "Something went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    Toast.makeText(application, "Something went wrong error code: " + response.code(), Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<EmployeeCheckResponse> call, Throwable t) {

                errorState.setValue(true);
                loadingState.setValue(false);
                Toast.makeText(application, "Something Went wrong : " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        return employeeCheckResponseMutableLiveData;
    }

    public MutableLiveData<EmployeeCheckResponse> getEmployeeWellnessDetailsData() {

        return employeeCheckResponseMutableLiveData;
    }
}
