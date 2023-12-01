package com.pinc.android.MB360.insurance.hospitalnetwork.repository;

import android.app.Application;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pinc.android.MB360.BuildConfig;
import com.pinc.android.MB360.database.AppDatabase;
import com.pinc.android.MB360.database.dao.ProviderNetworkDao;
import com.pinc.android.MB360.insurance.hospitalnetwork.reponseclass.PlacesResponse;
import com.pinc.android.MB360.insurance.hospitalnetwork.responseclassV1.HospitalCountResponse;
import com.pinc.android.MB360.insurance.hospitalnetwork.responseclassV1.HospitalResponse;
import com.pinc.android.MB360.insurance.hospitalnetwork.responseclassV1.Message;
import com.pinc.android.MB360.insurance.hospitalnetwork.retrofit.HospitalNetworkRetrofitClientJson;
import com.pinc.android.MB360.utilities.LogTags;

import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HospitalNetworkRepository {
    private final MutableLiveData<HospitalResponse> hospitalResponse;
    MutableLiveData<HospitalCountResponse> hospitalCount;
    MutableLiveData<Boolean> loading = new MutableLiveData<>(true);
    MutableLiveData<Boolean> error = new MutableLiveData<>(false);
    Application application;
    MutableLiveData<PlacesResponse> hospitalPlaces = new MutableLiveData<>();
    private final AppDatabase appDatabase;
    private final ProviderNetworkDao documentElementCountDao;
    public final MutableLiveData<Boolean> reloginState;


    public HospitalNetworkRepository(Application application) {
        hospitalResponse = new MutableLiveData<>();
        hospitalCount = new MutableLiveData<>();
        this.application = application;
        appDatabase = AppDatabase.getInstance(application);
        documentElementCountDao = appDatabase.documentElementCountDao();
        this.reloginState = new MutableLiveData<>(false);

    }


    public LiveData<HospitalResponse> getHospitals(String groupChildSrNo, String oeGrpBasInfoSrNo, String searchText) {


        Call<HospitalResponse> hospitalResponseCall = HospitalNetworkRetrofitClientJson
                .getInstance(application.getApplicationContext())
                .getHospitalNetworkApi()
                .getHospitalsData(groupChildSrNo, oeGrpBasInfoSrNo, searchText);


        try {

            documentElementCountDao.getHospitals()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(new MaybeObserver<>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(List<com.pinc.android.MB360.insurance.hospitalnetwork.responseclassV1.HospitalInformation> providerNetworkData) {
                            if (!providerNetworkData.isEmpty()) {
                                Log.d(LogTags.HOSPITAL_NETWORK, "getHospitalsOffline onSuccess: success.");
                                HospitalResponse providerNetworkData1 = new HospitalResponse();
                                Message message = new Message();
                                message.setMessage(BuildConfig.SUCCESS);
                                message.setStatus(true);
                                providerNetworkData1.setMessage(message);
                                providerNetworkData1.setHospitalInformation(providerNetworkData);
                                hospitalResponse.setValue(providerNetworkData1);
                                loading.setValue(false);
                                error.setValue(false);
                            }


                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(LogTags.HOSPITAL_NETWORK, "getHospitalsOffline onError: error.", e);

                        }

                        @Override
                        public void onComplete() {
                            Log.d(LogTags.HOSPITAL_NETWORK, "getHospitalsOffline onComplete: Done.");
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }


        try {

            hospitalResponseCall.enqueue(new Callback<>() {

                @Override
                public void onResponse(Call<HospitalResponse> callload, Response<HospitalResponse> response) {
                    Log.d(LogTags.LOAD_SESSIONS, "onResponse: " + response);
                    if (response.code() == 200) {
                        try {


                            Log.d(LogTags.HOSPITAL_NETWORK, "onResponse: " + response.body());
                            hospitalResponse.setValue(response.body());
                            loading.setValue(false);
                            error.setValue(false);

                            if (response.body().getHospitalInformation() != null) {

                                Observable<HospitalResponse> observable;
                                observable = io.reactivex.Observable.just(response.body());
                                observable.subscribeOn(Schedulers.io())
                                        .subscribe(new Observer<>() {
                                            @Override
                                            public void onSubscribe(@NonNull Disposable d) {

                                            }

                                            @Override
                                            public void onNext(@NonNull HospitalResponse providerNetworkData) {
                                                //delete first
                                                documentElementCountDao.deleteAllHospital();
                                                //Insert here
                                                documentElementCountDao.insertHospitalDetails(providerNetworkData.getHospitalInformation());

                                            }

                                            @Override
                                            public void onError(@NonNull Throwable e) {
                                                Log.e("Error", "Error at" + e);
                                            }

                                            @Override
                                            public void onComplete() {
                                                Log.d(LogTags.HOSPITAL_NETWORK, "Successfully saved Hospital data");
                                            }
                                        });
                            } else {
                                //response is null
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            hospitalResponse.setValue(null);
                            loading.setValue(false);
                            error.setValue(false);


                        }
                    } else {
                        Log.d(LogTags.LOAD_SESSIONS, "onResponse: FAILED" + response.code());
                        hospitalResponse.setValue(null);
                        loading.setValue(false);
                        error.setValue(true);
                    }
                }

                @Override
                public void onFailure(Call<HospitalResponse> call, Throwable t) {

                    Log.e("E_CARD_ACTIVITY", "onFailure: ", t);
                    hospitalResponse.setValue(null);
                    loading.setValue(false);
                    error.setValue(true);
                    //  Toast.makeText(application, "Something went wrong", Toast.LENGTH_SHORT).show();
                }


            });

        } catch (Exception e) {
            e.printStackTrace();
        }


        return hospitalResponse;
    }

    public LiveData<HospitalCountResponse> getHospitalCount(String grpChildSrNo, String oeGrpBasInfoSrNo, String searchText) {

        try {

            Call<HospitalCountResponse> hospitalCountCall = HospitalNetworkRetrofitClientJson
                    .getInstance(application.getApplicationContext())
                    .getHospitalNetworkApi()
                    .getHospitalsCountData(grpChildSrNo, oeGrpBasInfoSrNo, searchText);

            hospitalCountCall.enqueue(new Callback<HospitalCountResponse>() {

                @Override
                public void onResponse(Call<HospitalCountResponse> callload, Response<HospitalCountResponse> response) {
                    Log.d(LogTags.LOAD_SESSIONS, "onResponse: " + response);
                    if (response.code() == 200) {
                        try {
                            Log.d(LogTags.HOSPITAL_NETWORK, "COUNT 200 responseCode: " + response.body());
                            hospitalCount.setValue(response.body());
                            /* response.body().setOeGrpBasInfoSrNo(oeGrpBasInfoSrNo); */
                            /* documentElementCountDao.insertDocumentElementCount(response.body()); */

                        } catch (Exception e) {
                            e.printStackTrace();
                            hospitalResponse.setValue(null);
                        }
                    } else {
                        Log.d(LogTags.HOSPITAL_NETWORK, "onResponse: FAILED" + response.code());
                        hospitalCount.setValue(null);
                        loading.setValue(false);
                        error.setValue(true);
                    }
                }

                @Override
                public void onFailure(Call<HospitalCountResponse> call, Throwable t) {

                    Log.e("HOSPITAL_ACTIVITY", "onFailure: ", t);
                    hospitalCount.setValue(null);
                    loading.setValue(false);
                    error.setValue(true);
                    //  Toast.makeText(application, "Something went wrong", Toast.LENGTH_SHORT).show();
                }


            });


        } catch (Exception e) {
            e.printStackTrace();
            hospitalCount.setValue(null);
            loading.setValue(false);
            error.setValue(false);
        }
        return hospitalCount;
    }

    public LiveData<HospitalCountResponse> getHospitalsCountData() {
        return hospitalCount;
    }

    public LiveData<HospitalResponse> getHospitalsData() {
        return hospitalResponse;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public MutableLiveData<Boolean> getError() {
        return error;
    }


    public String getAPIKEY() {
        String API_KEY = null;
        try {
            ActivityInfo ai = application.getApplicationContext().getPackageManager().getActivityInfo(new ComponentName("com.google.android.geo.API_KEY", String.valueOf(1)), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            if (bundle != null) {
                String apiKey = bundle.getString("com.google.android.geo.API_KEY");
                API_KEY = apiKey;
            }
        } catch (
                PackageManager.NameNotFoundException e) {
            Log.e(LogTags.HOSPITAL_NETWORK, "getAPIKEY: ", e);
        } catch (
                NullPointerException e) {
            Log.e(LogTags.HOSPITAL_NETWORK, "Failed to load meta-data, NullPointer: " + e.getMessage());
        }

        return API_KEY;

    }

    private void showToast(Runnable action) {
        new Handler(Looper.getMainLooper()).post(action);
    }

}
