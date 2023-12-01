package com.pinc.android.MB360.insurance.repository.retrofit;


import android.util.Log;

import com.pinc.android.MB360.BuildConfig;
import com.pinc.android.MB360.utilities.AppServerConstants;
import com.pinc.android.MB360.utilities.LogTags;

import java.util.concurrent.TimeUnit;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoadSessionRetrofitClient {

    private static LoadSessionRetrofitClient instance;
    private final LoadSessionApi loadSessionApi;


    private LoadSessionRetrofitClient() {//constructor

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
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        loadSessionApi = retrofit.create(LoadSessionApi.class);

    }
    public static synchronized LoadSessionRetrofitClient getInstance() {
        if (instance == null) {
            instance = new LoadSessionRetrofitClient();
        }
        return instance;

    }

    public LoadSessionApi getLoadSessionApi() {
        return loadSessionApi;
    }

}
