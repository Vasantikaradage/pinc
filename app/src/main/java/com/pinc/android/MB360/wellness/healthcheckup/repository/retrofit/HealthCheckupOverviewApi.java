package com.pinc.android.MB360.wellness.healthcheckup.repository.retrofit;



import com.pinc.android.MB360.wellness.healthcheckup.repository.responseclass.HealthCheckupOverviewResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HealthCheckupOverviewApi {
    @GET("Wellness/GetBasicInfo")
    Call<HealthCheckupOverviewResponse>getHealthCheckupOverview(@Query("ExternalGroupSrNo") String externalGroupSrNo,
                                                  @Query("Agent") String agent);
}
