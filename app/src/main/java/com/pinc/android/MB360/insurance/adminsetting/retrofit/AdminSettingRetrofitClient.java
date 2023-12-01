package com.pinc.android.MB360.insurance.adminsetting.retrofit;

import android.util.Log;

import com.pinc.android.MB360.BuildConfig;
import com.pinc.android.MB360.utilities.LogTags;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AdminSettingRetrofitClient {
    private static AdminSettingRetrofitClient instance;
    AdminSettingApi adminSettingApi;

    public AdminSettingRetrofitClient() {
        // constructor

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .header("Authorization",
                                    Credentials.basic(BuildConfig.BASIC_AUTH_USERNAME, BuildConfig.BASIC_AUTH_PASSWORD));

                    Request newRequest = builder.build();
                    Log.d(LogTags.CLAIM_ACTIVITY, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        adminSettingApi = retrofit.create(AdminSettingApi.class);

    }

    public static synchronized AdminSettingRetrofitClient getInstance() {
        if (instance == null) {
            instance = new AdminSettingRetrofitClient();
        }
        return instance;
    }

    public AdminSettingApi getAdminSettingApi() {
        return adminSettingApi;
    }
}


