package com.pinc.android.MB360.insurance.hospitalnetwork.retrofit;

import android.content.Context;
import android.util.Log;

import com.pinc.android.MB360.BuildConfig;
import com.pinc.android.MB360.utilities.LogTags;

import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HospitalNetworkRetrofitClientJson {
    private static HospitalNetworkRetrofitClientJson instance;
    HospitalNetworkApi hospitalNetworkApi;


    public HospitalNetworkRetrofitClientJson(Context context) {


        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .header("Authorization",
                                    Credentials.basic(BuildConfig.BASIC_AUTH_USERNAME, BuildConfig.BASIC_AUTH_PASSWORD));


                    Request newRequest = builder.build();
                    Log.d(LogTags.HOSPITAL_NETWORK, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).readTimeout(15, TimeUnit.MINUTES)
                .build();

        //retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL_NETWORK_HOSPITAL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        hospitalNetworkApi = retrofit.create(HospitalNetworkApi.class);

    }

    public static synchronized HospitalNetworkRetrofitClientJson getInstance(Context context) {
        if (instance == null) {
            instance = new HospitalNetworkRetrofitClientJson(context);
        }
        return instance;
    }

    public HospitalNetworkApi getHospitalNetworkApi() {
        return hospitalNetworkApi;
    }
}


