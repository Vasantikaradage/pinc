package com.pinc.android.MB360.wellness.healthcheckup.repository.retrofit;

import android.util.Log;

import com.pinc.android.MB360.BuildConfig;
import com.pinc.android.MB360.utilities.AppServerConstants;
import com.pinc.android.MB360.utilities.LogTags;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HealthCheckupOverviewRetrofitClient {

    private static HealthCheckupOverviewRetrofitClient instance;
    HealthCheckupOverviewApi healthCheckupOverviewApi;

    public HealthCheckupOverviewRetrofitClient() {
        // constructor

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .header("Authorization",
                                    Credentials.basic(BuildConfig.BASIC_AUTH_USERNAME_WELLNESS, BuildConfig.BASIC_AUTH_PASSWORD_WELLNESS));

                    Request newRequest = builder.build();
                    Log.d(LogTags.HEALTH_CHECKUP_ACTIVITY, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL_WELLNESS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        healthCheckupOverviewApi = retrofit.create(HealthCheckupOverviewApi.class);

    }

    public static synchronized HealthCheckupOverviewRetrofitClient getInstance() {
        if (instance == null) {
            instance = new HealthCheckupOverviewRetrofitClient();
        }
        return instance;
    }

    public HealthCheckupOverviewApi getHealthCheckupOverviewApi() {
        return healthCheckupOverviewApi;
    }
}
