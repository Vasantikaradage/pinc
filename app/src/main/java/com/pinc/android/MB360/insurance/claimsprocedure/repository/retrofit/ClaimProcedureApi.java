package com.pinc.android.MB360.insurance.claimsprocedure.repository.retrofit;

import com.pinc.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureEmergencyContactResponse;
import com.pinc.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureImageResponse;
import com.pinc.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureLayoutInstructionInfo;
import com.pinc.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureTextResponse;
import com.pinc.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimsProcedureLayoutInfoResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ClaimProcedureApi {

    @GET("ClaimProcedure/GetClaimProcLayoutInfo")
    Call<ClaimsProcedureLayoutInfoResponse> getClaimsProcedureLayoutInfo(@Query("GroupChildSrNo") String grpChildSrNo, @Query("OegrpBasInfSrNo") String oeGrpBasInfSrNo, @Query("Product") String product, @Query("LayoutOfClaim") String layoutOfClaim);

    @GET("ClaimProcedure/GetLoadClaimProcImagePath")
    Call<ClaimProcedureImageResponse> getClaimsProcedureImage(@Query("GroupChildSrNo") String grpChildSrNo, @Query("OegrpBasInfSrNo") String oeGrpBasInfSrNo, @Query("Product") String product, @Query("LayoutOfClaim") String layoutOfClaim);

    @GET("ClaimProcedure/GetClaimProcInstructionInfo")
    Call<ClaimProcedureLayoutInstructionInfo> getClaimProcedureLayoutInstructionInfo(@Query("GroupChildSrNo") String grpChildSrNo, @Query("OegrpBasInfSrNo") String oeGrpBasInfSrNo, @Query("Product") String product, @Query("LayoutOfClaim") String layoutOfClaim);

    @GET("ClaimProcedure/GetClaimProcTextPath")
    Call<ClaimProcedureTextResponse> getClaimProcedureText(@Query("GroupChildSrNo") String grpChildSrNo, @Query("OegrpBasInfSrNo") String oeGrpBasInfSrNo, @Query("Product") String product, @Query("LayoutOfClaim") String layoutOfClaim);

    @GET("ClaimProcedure/GetEmergencyContactNo")
    Call<ClaimProcedureEmergencyContactResponse> getClaimProcedureEmergencyResponse(@Query("TpaCode") String tpaCode);


    //Steps files (txt response)
    @GET("mybenefits/claimprocedures/{groupChildSrNo}/{group_oegrpBasInfoSrNo}/displayinstructions/{filename}")
    Call<String> getClaimProcedureTxtFileResponse(@Path("groupChildSrNo") String groupChildSrNo, @Path("group_oegrpBasInfoSrNo") String group_oegrpBasInfoSrNo, @Path("filename") String file_name);

    //Steps files (txt response) additional instructions
    @GET("mybenefits/claimprocedures/{groupChildSrNo}/{group_oegrpBasInfoSrNo}/displayadditionalinstructions/{filename}")
    Call<String> getClaimProcedureAdditionalTxtFileResponse(@Path("groupChildSrNo") String groupChildSrNo, @Path("group_oegrpBasInfoSrNo") String group_oegrpBasInfoSrNo, @Path("filename") String file_name);

}
