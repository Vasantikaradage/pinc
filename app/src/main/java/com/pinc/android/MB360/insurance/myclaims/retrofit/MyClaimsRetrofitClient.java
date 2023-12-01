package com.pinc.android.MB360.insurance.myclaims.retrofit;

import android.util.Log;

import com.pinc.android.MB360.BuildConfig;
import com.pinc.android.MB360.utilities.AppServerConstants;
import com.pinc.android.MB360.utilities.LogTags;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MyClaimsRetrofitClient {
    private static MyClaimsRetrofitClient instance;
    MyClaimsApi myClaimsApi;


    public MyClaimsRetrofitClient() {
        // constructor

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/xml");


                    Request newRequest = builder.build();
                    Log.d(LogTags.CLAIM_ACTIVITY, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).readTimeout(3, TimeUnit.MINUTES).build();

        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL_MY_CLAIMS)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(client)
                .build();
        myClaimsApi = retrofit.create(MyClaimsApi.class);

    }

    public static synchronized MyClaimsRetrofitClient getInstance() {
        if (instance == null) {
            instance = new MyClaimsRetrofitClient();
        }
        return instance;
    }

    public MyClaimsApi getClaimsApi() {
        return myClaimsApi;
    }
}
