package com.pinc.android.MB360.insurance.repository.retrofit;

import android.util.Log;

import com.pinc.android.MB360.BuildConfig;
import com.pinc.android.MB360.utilities.LogTags;

import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoadSessionRetrofitClientV2 {

    private static LoadSessionRetrofitClientV2 instance;
    private final LoadSessionApi loadSessionApi;


    private LoadSessionRetrofitClientV2() {//constructor

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .header("Authorization",
                                    Credentials.basic(BuildConfig.BASIC_AUTH_USERNAME, BuildConfig.BASIC_AUTH_PASSWORD));

                    Request newRequest = builder.build();
                    Log.d(LogTags.LOAD_SESSIONS, newRequest.url().toString());

                    return chain.proceed(newRequest);
                })
                .readTimeout(5, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();


        //retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL_OTP)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        loadSessionApi = retrofit.create(LoadSessionApi.class);

    }

    public static synchronized LoadSessionRetrofitClientV2 getInstance() {
        if (instance == null) {
            instance = new LoadSessionRetrofitClientV2();
        }
        return instance;

    }

    public LoadSessionApi getLoadSessionApi() {
        return loadSessionApi;
    }

}


