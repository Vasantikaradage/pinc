package com.pinc.android.MB360.wellness.homehealthcare.retrofit;

import android.util.Log;

import com.pinc.android.MB360.BuildConfig;
import com.pinc.android.MB360.utilities.AppServerConstants;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.wellness.homehealthcare.cancellation.CancellationApi;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeHealthCareRetrofitClient {

    private static HomeHealthCareRetrofitClient instance;
    HomeHealthCareApi homeHealthCareApi;
    HomeHealthCareSummaryApi homeHealthCareSummaryApi;
    CancellationApi cancellationApi;

    public HomeHealthCareRetrofitClient() { // constructor

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .header("Authorization",
                                    Credentials.basic(BuildConfig.BASIC_AUTH_USERNAME_WELLNESS, BuildConfig.BASIC_AUTH_PASSWORD_WELLNESS));

                    Request newRequest = builder.build();
                    Log.d(LogTags.GET_MEMBERS, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL_WELLNESS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        homeHealthCareApi = retrofit.create(HomeHealthCareApi.class);
        homeHealthCareSummaryApi = retrofit.create(HomeHealthCareSummaryApi.class);
        cancellationApi = retrofit.create(CancellationApi.class);
    }

    public static synchronized HomeHealthCareRetrofitClient getInstance() {
        if (instance == null) {
            instance = new HomeHealthCareRetrofitClient();
        }
        return instance;
    }

    public HomeHealthCareApi getHomeHealthCareApi() {
        return homeHealthCareApi;
    }

    public HomeHealthCareSummaryApi getSummaryApi() {
        return homeHealthCareSummaryApi;
    }

    public CancellationApi getCancellationApi() {
        return cancellationApi;
    }
}
