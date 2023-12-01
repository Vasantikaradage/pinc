package com.pinc.android.MB360.insurance.claimsprocedure.repository;


import android.app.Application;
import android.util.Log;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pinc.android.MB360.database.AppDatabase;
import com.pinc.android.MB360.database.dao.ClaimProcedureDao;
import com.pinc.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureEmergencyContactResponse;
import com.pinc.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureImageResponse;
import com.pinc.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureLayoutInstructionInfo;
import com.pinc.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimProcedureTextResponse;
import com.pinc.android.MB360.insurance.claimsprocedure.repository.responseclass.ClaimsProcedureLayoutInfoResponse;
import com.pinc.android.MB360.insurance.claimsprocedure.repository.retrofit.ClaimProcedureRetrofitClient;
import com.pinc.android.MB360.utilities.LogTags;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * this class has the
 * business logic for Claim-Procedures
 * in users
 **/

public class ClaimProcedureRepository {

    private final MutableLiveData<ClaimsProcedureLayoutInfoResponse> claimsProcedureLayoutInfoResponseMutableLiveData;
    private final MutableLiveData<ClaimProcedureImageResponse> claimProcedureImageResponseMutableLiveData;
    private final MutableLiveData<ClaimProcedureLayoutInstructionInfo> claimProcedureLayoutInstructionInfoMutableLiveData;
    private final MutableLiveData<ClaimProcedureTextResponse> claimProcedureTextResponseMutableLiveData;
    private final MutableLiveData<ClaimProcedureEmergencyContactResponse> claimProcedureEmergencyContactResponseMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    public final MutableLiveData<String> claimProcedureTextFileData;
    public final MutableLiveData<String> claimStepsHtmlData;
    public final MutableLiveData<String> claimStepsHtmlAdditionalData;
    Application application;
    private AppDatabase appDatabase;
    private ClaimProcedureDao claimProcedureLayoutDao;


    public ClaimProcedureRepository(Application application) {
        this.claimProcedureEmergencyContactResponseMutableLiveData = new MutableLiveData<>();
        this.claimProcedureImageResponseMutableLiveData = new MutableLiveData<>();
        this.claimProcedureTextResponseMutableLiveData = new MutableLiveData<>();
        this.claimsProcedureLayoutInfoResponseMutableLiveData = new MutableLiveData<>();
        this.claimProcedureLayoutInstructionInfoMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();
        this.application = application;
        this.claimProcedureTextFileData = new MutableLiveData<>("");
        this.claimStepsHtmlData = new MutableLiveData<>("");
        this.claimStepsHtmlAdditionalData = new MutableLiveData<>("");
        appDatabase = AppDatabase.getInstance(application);
        claimProcedureLayoutDao = appDatabase.claimProcedureLayoutDao();

    }


    /**
     * this function  has the business logic for calling
     * and parsing the @Claim-Procedure response
     **/

    public MutableLiveData<ClaimsProcedureLayoutInfoResponse> getClaimsProcedureLayoutInfo(String grpChildSrNo, String oeGrpBasInfSrNo, String productCode, String layoutOfClaim) {
        Call<ClaimsProcedureLayoutInfoResponse> claimsProcedureLayoutInfoResponseCall = ClaimProcedureRetrofitClient.getInstance(false).getClaimProcedureClient().getClaimsProcedureLayoutInfo(grpChildSrNo, oeGrpBasInfSrNo, productCode, layoutOfClaim);

        claimsProcedureLayoutInfoResponseCall.enqueue(new Callback<ClaimsProcedureLayoutInfoResponse>() {
            @Override
            public void onResponse(Call<ClaimsProcedureLayoutInfoResponse> call, Response<ClaimsProcedureLayoutInfoResponse> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onResponse: " + response.body());
                        claimsProcedureLayoutInfoResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                        response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                        response.body().setLayoutOfClaim(layoutOfClaim);
                        claimProcedureLayoutDao.insertClaimsProcedureLayoutInfo(response.body());

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error getClaimsProcedureLayoutInfo : ", e);
                        claimsProcedureLayoutInfoResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }
                } else {
                    errorState.setValue(true);
                    loadingState.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<ClaimsProcedureLayoutInfoResponse> call, Throwable t) {
                Log.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getClaimsProcedureLayoutInfo" + t.getLocalizedMessage());
                t.printStackTrace();

                try {
                    Log.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onFailure: " + claimProcedureLayoutDao.getClaimsProcedureLayoutInfo(oeGrpBasInfSrNo, layoutOfClaim).toString());
                    claimsProcedureLayoutInfoResponseMutableLiveData.setValue(claimProcedureLayoutDao.getClaimsProcedureLayoutInfo(oeGrpBasInfSrNo, layoutOfClaim));
                    loadingState.setValue(false);
                    errorState.setValue(false);


                } catch (Exception e) {
                    e.printStackTrace();
                    claimsProcedureLayoutInfoResponseMutableLiveData.setValue(null);

                    loadingState.setValue(false);
                    errorState.setValue(true);


                }
            }
        });


        return claimsProcedureLayoutInfoResponseMutableLiveData;
    }

    public MutableLiveData<ClaimProcedureImageResponse> getClaimProcedureImage(String grpChildSrNo, String oeGrpBasInfSrNo, String productCode, String layoutOfClaim) {
        Call<ClaimProcedureImageResponse> callClaimProcedureImageResponseCall = ClaimProcedureRetrofitClient.getInstance(false).getClaimProcedureClient().getClaimsProcedureImage(grpChildSrNo, oeGrpBasInfSrNo, productCode, layoutOfClaim);

        callClaimProcedureImageResponseCall.enqueue(new Callback<ClaimProcedureImageResponse>() {
            @Override
            public void onResponse(Call<ClaimProcedureImageResponse> call, Response<ClaimProcedureImageResponse> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onResponse: " + response.body());
                        claimProcedureImageResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                        response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                        response.body().setLayoutOfClaim(layoutOfClaim);
                        claimProcedureLayoutDao.insertClaimProcedureImage(response.body());

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getClaimProcedureImage", e);
                        claimProcedureImageResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }
                } else {
                    errorState.setValue(true);
                    loadingState.setValue(false);
                }

            }

            @Override
            public void onFailure(Call<ClaimProcedureImageResponse> call, Throwable t) {

                Log.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getClaimProcedureImage" + t.getLocalizedMessage());
                try {
                    Log.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onFailure: " + claimProcedureLayoutDao.getClaimsProcedureImage(oeGrpBasInfSrNo, layoutOfClaim).toString());
                    claimProcedureImageResponseMutableLiveData.setValue(claimProcedureLayoutDao.getClaimsProcedureImage(oeGrpBasInfSrNo, layoutOfClaim));
                    loadingState.setValue(false);
                    errorState.setValue(false);


                } catch (Exception e) {
                    e.printStackTrace();
                    claimProcedureImageResponseMutableLiveData.setValue(null);

                    loadingState.setValue(false);
                    errorState.setValue(true);


                }
            }
        });

        return claimProcedureImageResponseMutableLiveData;
    }

    public MutableLiveData<ClaimProcedureLayoutInstructionInfo> getClaimProcedureInformation(String grpChildSrNo, String oeGrpBasInfSrNo, String productCode, String layoutOfClaim) {

        Call<ClaimProcedureLayoutInstructionInfo> callClaimsProcedureInstruction = ClaimProcedureRetrofitClient.getInstance(false).getClaimProcedureClient().getClaimProcedureLayoutInstructionInfo(grpChildSrNo, oeGrpBasInfSrNo, productCode, layoutOfClaim);

        callClaimsProcedureInstruction.enqueue(new Callback<ClaimProcedureLayoutInstructionInfo>() {
            @Override
            public void onResponse(Call<ClaimProcedureLayoutInstructionInfo> call, Response<ClaimProcedureLayoutInstructionInfo> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onResponse: " + response.body());
                        claimProcedureLayoutInstructionInfoMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                        response.body().setLayoutOfClaim(layoutOfClaim);
                        response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                        claimProcedureLayoutDao.insertClaimProcedureLayoutInstruction(response.body());


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getClaimProcedureInformation ", e);
                        claimProcedureLayoutInstructionInfoMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }
                } else {
                    errorState.setValue(true);
                    loadingState.setValue(false);

                }
            }

            @Override
            public void onFailure(Call<ClaimProcedureLayoutInstructionInfo> call, Throwable t) {
                t.printStackTrace();
                Log.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getClaimProcedureInformation " + t.getLocalizedMessage());

                try {
                    Log.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onFailure: " + claimProcedureLayoutDao.getClaimProcedureLayoutInstruction(oeGrpBasInfSrNo, layoutOfClaim).toString());
                    claimProcedureLayoutInstructionInfoMutableLiveData.setValue(claimProcedureLayoutDao.getClaimProcedureLayoutInstruction(oeGrpBasInfSrNo, layoutOfClaim));
                    loadingState.setValue(false);
                    errorState.setValue(false);

                } catch (Exception e) {
                    e.printStackTrace();
                    loadingState.setValue(false);
                    errorState.setValue(true);


                }
            }
        });

        return claimProcedureLayoutInstructionInfoMutableLiveData;
    }

    public MutableLiveData<ClaimProcedureTextResponse> getClaimProcedureText(String grpChildSrNo, String oeGrpBasInfSrNo, String productCode, String layoutOfClaim) {
        Call<ClaimProcedureTextResponse> callClaimProcedureText = ClaimProcedureRetrofitClient.getInstance(false).getClaimProcedureClient().getClaimProcedureText(grpChildSrNo, oeGrpBasInfSrNo, productCode, layoutOfClaim);
        callClaimProcedureText.enqueue(new Callback<ClaimProcedureTextResponse>() {
            @Override
            public void onResponse(Call<ClaimProcedureTextResponse> call, Response<ClaimProcedureTextResponse> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onResponse: " + response.body());
                        claimProcedureTextResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                        response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                        claimProcedureLayoutDao.insertClaimProcedureTextPath(response.body());


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getClaimProcedureText ", e);
                        claimProcedureTextResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }
                } else {
                    claimProcedureTextResponseMutableLiveData.setValue(null);
                    errorState.setValue(true);
                    loadingState.setValue(false);

                }
            }

            @Override
            public void onFailure(Call<ClaimProcedureTextResponse> call, Throwable t) {
                t.printStackTrace();

                Log.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getClaimProcedureText " + t.getLocalizedMessage());
                try {
                    Log.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onFailure: " + claimProcedureLayoutDao.getClaimProcedureTextPath(oeGrpBasInfSrNo).toString());
                    claimProcedureTextResponseMutableLiveData.setValue(claimProcedureLayoutDao.getClaimProcedureTextPath(oeGrpBasInfSrNo));
                    loadingState.setValue(false);
                    errorState.setValue(false);

                } catch (Exception e) {
                    e.printStackTrace();
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }
        });
        return claimProcedureTextResponseMutableLiveData;
    }

    public MutableLiveData<ClaimProcedureEmergencyContactResponse> getEmergencyContacts(String tpaCode) {

        Call<ClaimProcedureEmergencyContactResponse> callClaimProcedureEmergencyContact = ClaimProcedureRetrofitClient.getInstance(false).getClaimProcedureClient().getClaimProcedureEmergencyResponse(tpaCode);

        callClaimProcedureEmergencyContact.enqueue(new Callback<ClaimProcedureEmergencyContactResponse>() {
            @Override
            public void onResponse(Call<ClaimProcedureEmergencyContactResponse> call, Response<ClaimProcedureEmergencyContactResponse> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onResponse: " + response.body());
                        claimProcedureEmergencyContactResponseMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                        response.body().setTpa_code(tpaCode);
                        claimProcedureLayoutDao.insertClaimProcedureEmergencyContact(response.body());


                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getEmergencyContacts ", e);
                        claimProcedureEmergencyContactResponseMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }
                } else {
                    errorState.setValue(true);
                    loadingState.setValue(false);

                }
            }

            @Override
            public void onFailure(Call<ClaimProcedureEmergencyContactResponse> call, Throwable t) {
                Log.e(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "Error: getEmergencyContacts " + t.getLocalizedMessage());
                t.printStackTrace();

                try {
                    Log.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "onFailure: " + claimProcedureLayoutDao.getClaimsProcedureEmergencyContact(tpaCode).toString());
                    claimProcedureEmergencyContactResponseMutableLiveData.setValue(claimProcedureLayoutDao.getClaimsProcedureEmergencyContact(tpaCode));
                    loadingState.setValue(false);
                    errorState.setValue(false);

                } catch (Exception e) {
                    e.printStackTrace();
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }
        });

        return claimProcedureEmergencyContactResponseMutableLiveData;
    }


    public MutableLiveData<ClaimsProcedureLayoutInfoResponse> getClaimsProcedureLayoutInfoData() {
        return claimsProcedureLayoutInfoResponseMutableLiveData;
    }


    public MutableLiveData<ClaimProcedureImageResponse> getClaimProcedureImageData() {
        return claimProcedureImageResponseMutableLiveData;
    }

    public MutableLiveData<ClaimProcedureLayoutInstructionInfo> getClaimProcedureLayoutInstructionInfoData() {
        return claimProcedureLayoutInstructionInfoMutableLiveData;
    }

    public MutableLiveData<ClaimProcedureTextResponse> getClaimProcedureTextData() {
        return claimProcedureTextResponseMutableLiveData;
    }

    public MutableLiveData<ClaimProcedureEmergencyContactResponse> getClaimProcedureEmergencyContactData() {
        return claimProcedureEmergencyContactResponseMutableLiveData;
    }


    public LiveData<String> getClaimProcedureTextFileData() {
        return claimProcedureTextFileData;
    }


    public MutableLiveData<String> getClaimStepsHtmlData(String groupChildSrno, String group_oegrpbasInfoSrNo, String file_name) throws Exception {

        Call<String> claimProceduretxtCall = ClaimProcedureRetrofitClient.getInstance(true).getClaimProcedureClient().getClaimProcedureTxtFileResponse(groupChildSrno, group_oegrpbasInfoSrNo, file_name);


        claimProceduretxtCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    Log.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "STEPS HTML DATA : " + response.body());
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            claimStepsHtmlData.setValue(response.body().toString());
                        } else {
                            claimStepsHtmlData.setValue("");
                        }
                    } else {
                        claimStepsHtmlData.setValue("");
                    }

                } catch (Exception e) {
                    //something went wrong
                    claimStepsHtmlData.setValue("");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                claimStepsHtmlData.setValue("");
                t.printStackTrace();
            }
        });

        return claimStepsHtmlData;
    }

    public MutableLiveData<String> getClaimStepsHtmlDataObserver() {
        return claimStepsHtmlData;
    }

    public MutableLiveData<String> getClaimStepsHtmlAdditionalData(String groupChildSrno, String group_oegrpbasInfoSrNo, String file_name) throws Exception {

        Call<String> claimProceduretxtCall = ClaimProcedureRetrofitClient.getInstance(true).getClaimProcedureClient().getClaimProcedureAdditionalTxtFileResponse(groupChildSrno, group_oegrpbasInfoSrNo, file_name);


        claimProceduretxtCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                try {
                    Log.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "STEPS HTML DATA : " + response.body());
                    if (response.code() == 200) {
                        if (response.body() != null) {
                            claimStepsHtmlAdditionalData.setValue(response.body().toString());
                        } else {
                            claimStepsHtmlAdditionalData.setValue("");
                        }
                    } else {
                        claimStepsHtmlAdditionalData.setValue("");
                    }

                } catch (Exception e) {
                    //something went wrong
                    claimStepsHtmlAdditionalData.setValue("");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                claimStepsHtmlAdditionalData.setValue("");
                t.printStackTrace();
            }
        });

        return claimStepsHtmlAdditionalData;
    }

    public MutableLiveData<String> getClaimStepsHtmlAdditionalDataObserver() {
        return claimStepsHtmlAdditionalData;
    }

    public void setClaimsSteps(String htmlData) {
        Log.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "ClaimsStepsHTML: " + htmlData);
        claimStepsHtmlData.postValue(htmlData);
    }

    public void setClaimsAdditionalSteps(String htmlData) {
        Log.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, "ClaimsAdditionalStepsHTML: " + htmlData);
        claimStepsHtmlAdditionalData.postValue(htmlData);
    }

}

