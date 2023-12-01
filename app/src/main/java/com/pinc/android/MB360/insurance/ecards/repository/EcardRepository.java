package com.pinc.android.MB360.insurance.ecards.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pinc.android.MB360.insurance.ecards.reponseclass.EcardResponse;
import com.pinc.android.MB360.insurance.ecards.reponseclass.EcardResponseJson;
import com.pinc.android.MB360.insurance.ecards.retrofit.EcardRetrofitClient;
import com.pinc.android.MB360.insurance.ecards.retrofit.EcardRetrofitClientJson;
import com.pinc.android.MB360.utilities.LogTags;

import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EcardRepository {
    Application application;
    private final MutableLiveData<EcardResponse> eCardResponseMutableLiveData;
    MutableLiveData<Boolean> loading = new MutableLiveData<>(true);
    MutableLiveData<Boolean> error = new MutableLiveData<>(false);


    public EcardRepository(Application application) {
        this.application = application;
        this.eCardResponseMutableLiveData = new MutableLiveData<>();
    }


    public LiveData<EcardResponse> getEcard(String dataRequest) {
        MutableLiveData<EcardResponse> eCardResponseMutableLiveData = new MutableLiveData<>();
        loading.setValue(true);
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/xml"), dataRequest);
        Call<EcardResponse> ecardResponseCall = EcardRetrofitClient.getInstance().getEcardApi().getEcard(requestBody);
        ecardResponseCall.enqueue(new Callback<EcardResponse>() {
            @Override
            public void onResponse(Call<EcardResponse> call, Response<EcardResponse> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.E_CARD_ACTIVITY, "onResponse: " + response.body());
                        eCardResponseMutableLiveData.setValue(response.body());
                        loading.setValue(false);
                        error.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        eCardResponseMutableLiveData.setValue(null);
                        loading.setValue(false);
                        error.setValue(false);

                    }
                } else {
                    Log.d(LogTags.E_CARD_ACTIVITY, "onResponse: " + response.body());
                    eCardResponseMutableLiveData.setValue(response.body());
                    loading.setValue(false);
                    error.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<EcardResponse> call, Throwable t) {
                Log.e(LogTags.E_CARD_ACTIVITY, "onFailure: ", t);
                eCardResponseMutableLiveData.setValue(null);
                loading.setValue(false);
                error.setValue(true);
                //  Toast.makeText(application, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
        return eCardResponseMutableLiveData;
    }


    public LiveData<EcardResponse> getEcardData() {
        return eCardResponseMutableLiveData;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public MutableLiveData<Boolean> getError() {
        return error;
    }

    public LiveData<EcardResponseJson> getEcardJson(Map<String, String> ecardMapOptions) {
        MutableLiveData<EcardResponseJson> eCardResponseMutableLiveDataJSON = new MutableLiveData<>();
        loading.setValue(true);

        Call<EcardResponseJson> ecardResponseCall = EcardRetrofitClientJson.getInstance().getEcardApi().getEcardJSON(ecardMapOptions);
        ecardResponseCall.enqueue(new Callback<EcardResponseJson>() {
            @Override
            public void onResponse(Call<EcardResponseJson> call, Response<EcardResponseJson> response) {
                if (response.code() == 200) {
                    try {
                        Log.d("E_CARD_ACTIVITY", "onResponse: " + response.body());
                        eCardResponseMutableLiveDataJSON.setValue(response.body());
                        loading.setValue(false);
                        error.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        eCardResponseMutableLiveDataJSON.setValue(null);
                        loading.setValue(false);
                        error.setValue(false);

                    }
                } else {
                    Log.d("E_CARD_ACTIVITY", "onResponse: " + response.body());
                    eCardResponseMutableLiveDataJSON.setValue(response.body());
                    loading.setValue(false);
                    error.setValue(true);
                }
            }

            @Override
            public void onFailure(Call<EcardResponseJson> call, Throwable t) {
                Log.e("E_CARD_ACTIVITY", "onFailure: ", t);
                eCardResponseMutableLiveDataJSON.setValue(null);
                loading.setValue(false);
                error.setValue(true);
                //  Toast.makeText(application, "Something went wrong", Toast.LENGTH_SHORT).show();

            }
        });
        return eCardResponseMutableLiveDataJSON;
    }

}
