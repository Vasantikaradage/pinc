package com.pinc.android.MB360.insurance.escalations.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.pinc.android.MB360.database.AppDatabase;
import com.pinc.android.MB360.database.dao.EscalationsDao;
import com.pinc.android.MB360.insurance.escalations.repository.responseclass.EscalationsResponse;
import com.pinc.android.MB360.insurance.escalations.repository.retrofit.EscalationsRetrofitClient;
import com.pinc.android.MB360.utilities.LogTags;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * this class has the
 * business logic for Escalations {@link EscalationRepository }
 * in users
 **/
public class EscalationRepository {

    private final MutableLiveData<EscalationsResponse> escalationsMutableLiveData;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    Application application;
    private AppDatabase appDatabase;
    private EscalationsDao escalationsDao;



    public EscalationRepository(Application application) {
        this.escalationsMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();
        this.application = application;
        appDatabase=AppDatabase.getInstance(application);
        escalationsDao= appDatabase.escalationsDao();
    }

    /**
     * this function  has the business logic for calling
     * and parsing the {@see getEscalations } {@link EscalationsResponse } response
     **/

    public MutableLiveData<EscalationsResponse> getEscalations(String groupChildSrNo, String oeGrpBasInfSrNo) {

        Call<EscalationsResponse> escalationsCall = EscalationsRetrofitClient.getInstance().getEscalationApi().getEscalations(groupChildSrNo, oeGrpBasInfSrNo);
        escalationsCall.enqueue(new Callback<EscalationsResponse>() {
            @Override
            public void onResponse(Call<EscalationsResponse> call, Response<EscalationsResponse> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.ESCALATION_ACTIVITY, "onResponse: " + response.body().toString());
                        escalationsMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                        response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);
                        escalationsDao.insertEscalations(response.body());

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.ESCALATION_ACTIVITY, "Error: ", e);
                        escalationsMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        Toast.makeText(application, "Something went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    Toast.makeText(application, "Something went wrong error code: " + response.code(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<EscalationsResponse> call, Throwable t) {
                try {

                    Log.d(LogTags.FAQ_ACTIVITY, "onFailure: " + escalationsDao.getEscalations(oeGrpBasInfSrNo).toString());
                    escalationsMutableLiveData.setValue(escalationsDao.getEscalations(oeGrpBasInfSrNo));
                    loadingState.setValue(false);
                    errorState.setValue(false);

                } catch (Exception e) {
                    e.printStackTrace();
                    escalationsMutableLiveData.setValue(null);

                    loadingState.setValue(false);
                    errorState.setValue(true);


                }


            }
        });

        return escalationsMutableLiveData;
    }

    public MutableLiveData<EscalationsResponse> getEscalationsData() {
        return escalationsMutableLiveData;

    }
}
