package com.pinc.android.MB360.insurance.queries.repository.retrofit;

import android.util.Log;

import com.pinc.android.MB360.BuildConfig;
import com.pinc.android.MB360.utilities.AppServerConstants;
import com.pinc.android.MB360.utilities.LogTags;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QueryAddRetrofitClient {
    private static QueryAddRetrofitClient instance;
    QueryApi queryApi;

    public QueryAddRetrofitClient() {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                           // .addHeader("Content-Type", "multipart/form-data")
                            .header("Authorization",
                                    Credentials.basic(BuildConfig.BASIC_AUTH_USERNAME, BuildConfig.BASIC_AUTH_PASSWORD));

                    Request newRequest = builder.build();
                    Log.d(LogTags.QUERY_ACTIVITY, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();
        ///retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        queryApi = retrofit.create(QueryApi.class);
    }

    public static synchronized QueryAddRetrofitClient getInstance() {
        if (instance == null) {
            instance = new QueryAddRetrofitClient();
        }
        return instance;
    }

    public QueryApi getQueryApi() {
        return queryApi;
    }
}
