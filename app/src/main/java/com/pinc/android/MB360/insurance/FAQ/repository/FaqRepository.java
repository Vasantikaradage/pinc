package com.pinc.android.MB360.insurance.FAQ.repository;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.pinc.android.MB360.database.AppDatabase;
import com.pinc.android.MB360.database.dao.FaqDao;
import com.pinc.android.MB360.insurance.FAQ.repository.responseclass.FaqResponse;
import com.pinc.android.MB360.insurance.FAQ.repository.retrofit.FaqRetrofitClient;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.utilities.ResponseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaqRepository {

    private final MutableLiveData<FaqResponse> faqMutableLiveData;
    private final MutableLiveData<Boolean> errorState;
    private final MutableLiveData<Boolean> loadingState;
    Application application;
    FirebaseCrashlytics crashlytics;
    AppDatabase appDatabase;
    FaqDao faqDao;


    public FaqRepository(Application application) {
        this.faqMutableLiveData = new MutableLiveData<>();
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();
        this.application = application;
        crashlytics = FirebaseCrashlytics.getInstance();
        appDatabase=AppDatabase.getInstance(application);
        faqDao=appDatabase.faqDao();
    }

    public MutableLiveData<FaqResponse> getFaq(String grpChildSrNo, String oeGrpBasInfSrNo) {

        Call<FaqResponse> faqResponseCall = FaqRetrofitClient.getInstance().getFaqApi().getFaq(grpChildSrNo, oeGrpBasInfSrNo);

        faqResponseCall.enqueue(new Callback<FaqResponse>() {
            @Override
            public void onResponse(Call<FaqResponse> call, Response<FaqResponse> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.FAQ_ACTIVITY, "onResponse: " + response.body().toString());
                        faqMutableLiveData.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                        //id for database
                        response.body().setOeGrpBasInfoSrNo(oeGrpBasInfSrNo);

                        faqDao.insertFAQ(response.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.FAQ_ACTIVITY, "Error: ", e);
                        faqMutableLiveData.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                        Toast.makeText(application, "Something went wrong reason: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    ResponseException responseException = new ResponseException("Faq -> getFaq -> Response Code:- " + String.valueOf(response.code()) + "GroupChildSrNo:- " + grpChildSrNo + "OeGrpBasInfoSrNo:- " + oeGrpBasInfSrNo);
                    crashlytics.recordException(responseException);
                }
            }

            @Override
            public void onFailure(Call<FaqResponse> call, Throwable t) {

                Log.e(LogTags.FAQ_ACTIVITY, "Error: " + t.getLocalizedMessage());
                Toast.makeText(application, "Something went wrong, reason: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                try {

                    Log.d(LogTags.FAQ_ACTIVITY, "onFailure: " + faqDao.getFAQ(oeGrpBasInfSrNo).toString());
                    faqMutableLiveData.setValue(faqDao.getFAQ(oeGrpBasInfSrNo));
                    loadingState.setValue(false);
                    errorState.setValue(false);

                } catch (Exception e) {
                    e.printStackTrace();
                    faqMutableLiveData.setValue(null);
                    errorState.setValue(true);
                    loadingState.setValue(false);
                }
            }
        });


        return faqMutableLiveData;
    }


    public MutableLiveData<FaqResponse> getFaqData() {
        return faqMutableLiveData;
    }


    public MutableLiveData<Boolean> getErrorState() {
        return errorState;
    }

    public MutableLiveData<Boolean> getLoadingState() {
        return loadingState;
    }
}
