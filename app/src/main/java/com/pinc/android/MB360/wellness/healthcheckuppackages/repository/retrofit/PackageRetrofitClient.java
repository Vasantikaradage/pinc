package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.retrofit;

import android.util.Log;

import com.pinc.android.MB360.BuildConfig;
import com.pinc.android.MB360.utilities.AppServerConstants;
import com.pinc.android.MB360.utilities.LogTags;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PackageRetrofitClient {

    private static PackageRetrofitClient instance;
    PackageApi packageApi;

    public PackageRetrofitClient() {
        // constructor

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .header("Authorization",
                                    Credentials.basic(BuildConfig.BASIC_AUTH_USERNAME_WELLNESS, BuildConfig.BASIC_AUTH_PASSWORD_WELLNESS));

                    Request newRequest = builder.build();
                    Log.d(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL_WELLNESS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        packageApi = retrofit.create(PackageApi.class);

    }

    public static synchronized PackageRetrofitClient getInstance() {
        if (instance == null) {
            instance = new PackageRetrofitClient();
        }
        return instance;
    }

    public PackageApi getPackageApi() {
        return packageApi;
    }
}
