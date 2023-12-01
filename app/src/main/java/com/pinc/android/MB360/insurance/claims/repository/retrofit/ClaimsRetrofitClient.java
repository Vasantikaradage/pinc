package com.pinc.android.MB360.insurance.claims.repository.retrofit;

import android.util.Log;

import com.pinc.android.MB360.BuildConfig;
import com.pinc.android.MB360.utilities.AppServerConstants;
import com.pinc.android.MB360.utilities.LogTags;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClaimsRetrofitClient {

    private static ClaimsRetrofitClient instance;
    ClaimsApi claimsApi;

    public ClaimsRetrofitClient() {
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
        claimsApi = retrofit.create(ClaimsApi.class);

    }

    public static synchronized ClaimsRetrofitClient getInstance() {
        if (instance == null) {
            instance = new ClaimsRetrofitClient();
        }
        return instance;
    }

    public ClaimsApi getClaimsApi() {
        return claimsApi;
    }
}
