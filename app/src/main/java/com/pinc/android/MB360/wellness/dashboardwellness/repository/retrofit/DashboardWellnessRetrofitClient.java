package com.pinc.android.MB360.wellness.dashboardwellness.repository.retrofit;

import android.util.Log;

import com.pinc.android.MB360.BuildConfig;
import com.pinc.android.MB360.utilities.AppServerConstants;
import com.pinc.android.MB360.utilities.LogTags;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DashboardWellnessRetrofitClient {

    private static DashboardWellnessRetrofitClient instance;
    DashboardWellnessApi dashboardWellnessApi;

    public DashboardWellnessRetrofitClient() {
        // constructor

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .header("Authorization",
                                    Credentials.basic(BuildConfig.BASIC_AUTH_USERNAME_WELLNESS, BuildConfig.BASIC_AUTH_PASSWORD_WELLNESS));

                    Request newRequest = builder.build();
                    Log.d(LogTags.WELLNESS_DASHBOARD_ACTIVITY, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL_WELLNESS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        dashboardWellnessApi = retrofit.create(DashboardWellnessApi.class);

    }

    public static synchronized DashboardWellnessRetrofitClient getInstance() {
        if (instance == null) {
            instance = new DashboardWellnessRetrofitClient();
        }
        return instance;
    }

    public DashboardWellnessApi getDashboardWellnessApi() {
        return dashboardWellnessApi;
    }
}
