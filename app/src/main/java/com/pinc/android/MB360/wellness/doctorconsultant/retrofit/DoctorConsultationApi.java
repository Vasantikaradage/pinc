package com.pinc.android.MB360.wellness.doctorconsultant.retrofit;

import com.pinc.android.MB360.wellness.doctorconsultant.responseclass.AcceptUserAgreement;
import com.pinc.android.MB360.wellness.doctorconsultant.responseclass.DcPackageResponse;
import com.pinc.android.MB360.wellness.doctorconsultant.responseclass.DoctorConsultantPackagesResponse;
import com.pinc.android.MB360.wellness.doctorconsultant.responseclass.UserAgreement;
import com.pinc.android.MB360.wellness.doctorconsultant.responseclass.UserAgreementRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DoctorConsultationApi {

    @GET("DoctorConsultation/GetIsDCTermsAgreed")
    Call<UserAgreement> isUserAgreed(@Query("EmployeeSrNo") String empSrNo,
                                     @Query("VendorSrNo") String vendorSrNo);

    @POST("DoctorConsultation/InsertDCTermsAgree")
    Call<AcceptUserAgreement> acceptUserAgreement(@Body UserAgreementRequest userAgreementRequest);


    @GET("DoctorConsultation/GetEmployeeDCPackages")
    Call<DoctorConsultantPackagesResponse> getDCPackages(@Query("EmployeeSrNo") String empSrNo,
                                                         @Query("VendorSrNo") String vendorSrNo);

    @GET("DoctorConsultation/BuyDCPackage")
    Call<DcPackageResponse> buyDCPackage(@Query("PersonSrNo") String personSrNo,
                                         @Query("EmployeeSrNo") String empSrNo,
                                         @Query("PackageSrNo") String packageSrNo);

}
