package com.pinc.android.MB360.wellness.homehealthcare.cancellation;

import com.pinc.android.MB360.wellness.homehealthcare.cancellation.responseclass.CancelResponse;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CancellationApi {

    @POST("HomeHealthCare/CancelAppointment")
    Call<CancelResponse> funcCancelNAAppointment(@Query("ApptInfoSrNo") String strApptSrno);

    /*http://mybenefits360.in/mbapi/api/v1/HomeHealthCare/CancelAppointmentLT?ApptInfoSrNo=47*/
    @POST("HomeHealthCare/CancelAppointmentLT")
    Call<CancelResponse> funcCancelLTAppointment(@Query("ApptInfoSrNo") String strApptSrno);

    @POST("HomeHealthCare/CancelAppointmentDS")
    Call<CancelResponse> funcCancelDSAppointment(@Query("ApptInfoSrNo") String strApptSrno);

    @POST("HomeHealthCare/CancelAppointmentPT")
    Call<CancelResponse> funcCancelPTAppointment(@Query("ApptInfoSrNo") String strApptSrno);

    @POST("HomeHealthCare/CancelAppointmentST")
    Call<CancelResponse> funcCancelSTAppointment(@Query("ApptInfoSrNo") String strApptSrno);

    @POST("HomeHealthCare/CancelAppointmentDM")
    Call<CancelResponse> funcCancelDMAppointment(@Query("ApptInfoSrNo") String strApptSrno);

    @POST("HomeHealthCare/CancelAppointmentEC")
    Call<CancelResponse> funcCancelECAppointment(@Query("ApptInfoSrNo") String strApptSrno);

    @POST("HomeHealthCare/CancelAppointmentNC")
    Call<CancelResponse> funcCancelNCAppointment(@Query("ApptInfoSrNo") String strApptSrno);

}
