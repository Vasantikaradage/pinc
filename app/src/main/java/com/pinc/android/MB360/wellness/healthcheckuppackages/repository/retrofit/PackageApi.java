package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.retrofit;




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

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface PackageApi {
    @GET("Wellness/GetFamilyMembers")
    Call<PackageResponse> getPackage(@Query("ExtGroupSrNo") String extGroupSrNo,
                                     @Query("GroupCode") String groupCode,
                                     @Query("EmpIdNo") String empIdNo);

    @GET("HealthCheckup/GetHealthCheckupCenter")
    Call<DiagnosticCenterResponse> getDiagnosticCenter(@Query("City") String city);

    @GET("HealthCheckup/GetPackageDetails")
    Call<PackageDetailsResponse>getPackageDetails(@Query("PackageSrNo") String packageSrNo);

    @POST("HealthCheckup/ScheduleAppointment")
    Call<MessageResponse> startSchedule(@Body ScheduleRequest scheduleRequest);

    @POST("Wellness/AddDependent")
    Call<MessageResponseDependent> postAddMember(@Body DependentRequest dependentRequest);

    @GET("Wellness/GetAllrelations")
    Call<AllRelationResponse> getAllRelation(@Query("FamilySrNo") String familySrNo);

    @GET("HealthCheckup/GetSummary")
    Call<SummaryResponse> getSummary(@Query("FamilySrNo") String familySrNo,
                                     @Query("GroupCode") String groupCode);

    @DELETE("Wellness/DeleteDependent")
    Call<MessageResponseDependentDelete> deleteMember(@Query("PersonSrNo") String personSrNo);

    @PUT("HealthCheckup/IsPersonAgree")
    Call<MessageResponseVerifyNo> putVerifyNo(@Body VerifyNoRequest verifyNoRequest);

    @PUT("HealthCheckup/CancelAppointment")
    Call<MessageResponseCancelAppointment> putCancelAppointment(@Body HealthCheckupCancelAppointmentRequest healthCheckupCancelAppointmentRequest);

    @GET("HealthCheckup/GetServicableCities")
    Call<CityResponseHC> getcitiesHC();

    @GET("HealthCheckup/GetScheduledAppointments")
    Call<HealthCheckupOngoingResponse> getOngoingAppointment(@Query("FamilySrNo") String familySrNo,
                                                             @Query("ExtGroupSrNo") String extGroupSrNo,
                                                             @Query("EmpIdNo") String empIdNo,
                                                             @Query("GroupCode") String groupCode);

    @PUT("HealthCheckup/UpdatePaymentDetails")
    Call<MessageResponseUpdatePayment> putUpdatePayment(@Body HealthCheckupUpdatePaymentRequest healthCheckupUpdatePaymentRequest);

}
