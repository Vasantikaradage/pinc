package com.pinc.android.MB360.insurance.coverages.repository.retrofit;

import android.util.Log;

import com.pinc.android.MB360.BuildConfig;
import com.pinc.android.MB360.utilities.AppServerConstants;
import com.pinc.android.MB360.utilities.LogTags;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoveragesRetrofitClient {

    private static CoveragesRetrofitClient instance;
    CoveragesApi coveragesApi;


    public CoveragesRetrofitClient() { // constructor

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .header("Authorization",
                                    Credentials.basic(BuildConfig.BASIC_AUTH_USERNAME, BuildConfig.BASIC_AUTH_PASSWORD));

                    Request newRequest = builder.build();
                    Log.d(LogTags.COVERAGE_ACTIVITY, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        coveragesApi = retrofit.create(CoveragesApi.class);
    }

    public static synchronized CoveragesRetrofitClient getInstance() {
        if (instance == null) {
            instance = new CoveragesRetrofitClient();
        }
        return instance;
    }

    public CoveragesApi getCoverageApi() {
        return coveragesApi;
    }
}
