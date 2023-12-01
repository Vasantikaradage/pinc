package com.pinc.android.MB360.insurance.claimsprocedure.repository.retrofit;

import static com.pinc.android.MB360.BuildConfig.BASE_URL;
import static com.pinc.android.MB360.BuildConfig.BASIC_AUTH_PASSWORD;
import static com.pinc.android.MB360.BuildConfig.BASIC_AUTH_USERNAME;
import static com.pinc.android.MB360.BuildConfig.DOWNLAOD_BASE_URL;

import android.util.Log;

import com.pinc.android.MB360.utilities.LogTags;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ClaimProcedureRetrofitClient {

    private static ClaimProcedureRetrofitClient instance;
    private ClaimProcedureApi claimProcedureApi;

    private ClaimProcedureRetrofitClient() {
        //constructor

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .header("Authorization",
                                    Credentials.basic(BASIC_AUTH_USERNAME, BASIC_AUTH_PASSWORD));

                    Request newRequest = builder.build();
                    Log.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();


        //retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        claimProcedureApi = retrofit.create(ClaimProcedureApi.class);
    }

    private ClaimProcedureRetrofitClient(Boolean Download) {
        //constructor

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .header("Authorization",
                                    Credentials.basic(BASIC_AUTH_USERNAME, BASIC_AUTH_PASSWORD));

                    Request newRequest = builder.build();
                    Log.d(LogTags.CLAIMS_PROCEDURE_ACTIVITY, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();


        //retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(DOWNLAOD_BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build();
        claimProcedureApi = retrofit.create(ClaimProcedureApi.class);
    }

    public static synchronized ClaimProcedureRetrofitClient getInstance(Boolean download) {

        if (!download) {
            instance = new ClaimProcedureRetrofitClient();
        } else {
            instance = new ClaimProcedureRetrofitClient(true);
        }

        return instance;

    }

    public ClaimProcedureApi getClaimProcedureClient() {
        return claimProcedureApi;
    }
}


