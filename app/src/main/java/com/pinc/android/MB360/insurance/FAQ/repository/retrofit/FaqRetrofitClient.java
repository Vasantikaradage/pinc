package com.pinc.android.MB360.insurance.FAQ.repository.retrofit;

import android.util.Log;

import com.pinc.android.MB360.BuildConfig;
import com.pinc.android.MB360.utilities.AppServerConstants;
import com.pinc.android.MB360.utilities.LogTags;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FaqRetrofitClient {

    private static FaqRetrofitClient instance;
    FaqApi faqApi;


    public FaqRetrofitClient() { //constructor

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .header("Authorization",
                                    Credentials.basic(BuildConfig.BASIC_AUTH_USERNAME, BuildConfig.BASIC_AUTH_PASSWORD));

                    Request newRequest = builder.build();
                    Log.d(LogTags.FAQ_ACTIVITY, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();
        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        faqApi = retrofit.create(FaqApi.class);
    }

    public static synchronized FaqRetrofitClient getInstance() {
        if (instance == null) {
            instance = new FaqRetrofitClient();
        }
        return instance;
    }
    public FaqApi getFaqApi() {
        return faqApi;
    }
}
