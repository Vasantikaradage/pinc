package com.pinc.android.MB360.wellness.dashboardwellness.repository.retrofit;



import com.pinc.android.MB360.wellness.dashboardwellness.repository.responseclass.DashboardServiceResponse;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.responseclass.EmployeeCheckResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DashboardWellnessApi {
    @GET("Wellness/GetDashboardLinks")
    Call<DashboardServiceResponse> getDashboardService(@Query("Agent") String agent,
                                             @Query("GroupChildSrNo") String grpChildSrNo);

    @GET("Wellness/IsEmployeeDetailsPresent")
    Call<EmployeeCheckResponse> getEmployeeWellnessDetails(@Query("EmpIdNo") String empIdNo,
                                                    @Query("GroupCode") String groupCode);
}