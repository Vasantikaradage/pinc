package com.pinc.android.MB360.insurance.policyfeatures.repository.retrofit;

import android.util.Log;

import com.pinc.android.MB360.BuildConfig;
import com.pinc.android.MB360.utilities.AppServerConstants;
import com.pinc.android.MB360.utilities.LogTags;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PolicyFeaturesRetrofitClient {
    private static PolicyFeaturesRetrofitClient instance;
    PolicyFeaturesApi policyFeaturesApi;


    public PolicyFeaturesRetrofitClient() {
        // constructor

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .header("Authorization",
                                    Credentials.basic(BuildConfig.BASIC_AUTH_USERNAME, BuildConfig.BASIC_AUTH_PASSWORD));

                    Request newRequest = builder.build();
                    Log.d(LogTags.POLICY_FEATURES_ACTIVITY, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        policyFeaturesApi = retrofit.create(PolicyFeaturesApi.class);

    }

    public static synchronized PolicyFeaturesRetrofitClient getInstance() {
        if (instance == null) {
            instance = new PolicyFeaturesRetrofitClient();
        }
        return instance;
    }

    public PolicyFeaturesApi getPolicyFeaturesApi() {
        return policyFeaturesApi;
    }
}
