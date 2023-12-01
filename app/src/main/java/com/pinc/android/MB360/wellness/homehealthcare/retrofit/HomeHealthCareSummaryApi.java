package com.pinc.android.MB360.wellness.homehealthcare.retrofit;

import com.pinc.android.MB360.wellness.homehealthcare.responseclass.SummaryResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HomeHealthCareSummaryApi {

    @GET("HomeHealthCare/GetSummary")
    Call<SummaryResponse> getTrainedAttendantSummary(
            @Query("FamilySrNo") String strFamilySrNo);

    @GET("HomeHealthCare/GetSummaryLT")
    Call<SummaryResponse> getLongTermSummary(
            @Query("FamilySrNo") String strFamilySrNo);

    @GET("HomeHealthCare/GetSummaryDS")
    Call<SummaryResponse> getDoctorServicesSummary(
            @Query("FamilySrNo") String strFamilySrNo);

    @GET("HomeHealthCare/GetSummaryPT")
    Call<SummaryResponse> getPhysioTherapySummary(
            @Query("FamilySrNo") String strFamilySrNo);

    @GET("HomeHealthCare/GetSummaryST")
    Call<SummaryResponse> getShortTermSummary(
            @Query("FamilySrNo") String strFamilySrNo);

    @GET("HomeHealthCare/GetSummaryNC")
    Call<SummaryResponse> getPostNatalSummary(
            @Query("FamilySrNo") String strFamilySrNo);

    @GET("HomeHealthCare/GetSummaryDM")
    Call<SummaryResponse> getDiabetesManagmentSummary(
            @Query("FamilySrNo") String strFamilySrNo);


}
