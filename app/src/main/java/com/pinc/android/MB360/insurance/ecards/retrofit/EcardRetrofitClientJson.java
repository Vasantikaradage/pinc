package com.pinc.android.MB360.insurance.ecards.retrofit;


import android.content.Context;
import android.util.Log;


import com.pinc.android.MB360.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EcardRetrofitClientJson {
    private static EcardRetrofitClientJson instance;
    EcardApi ecardApi;

    public EcardRetrofitClientJson() {//constructor

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/json");


                    Request newRequest = builder.build();
                    Log.d("E_CARD_ACTIVITY", newRequest.url().toString());

                    return chain.proceed(newRequest);
                })
                .readTimeout(1, TimeUnit.MINUTES)
                .build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL_OTP)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        ecardApi = retrofit.create(EcardApi.class);
    }

    public static synchronized EcardRetrofitClientJson getInstance() {
        if (instance == null) {
            instance = new EcardRetrofitClientJson();
        }
        return instance;
    }


    public EcardApi getEcardApi() {
        return ecardApi;
    }

}

