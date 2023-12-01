package com.pinc.android.MB360.wellness.doctorconsultant.retrofit;

import android.util.Log;

import com.pinc.android.MB360.BuildConfig;
import com.pinc.android.MB360.utilities.AppServerConstants;
import com.pinc.android.MB360.utilities.LogTags;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DoctorConsultationRetrofitClient {
    private static DoctorConsultationRetrofitClient instance;
    DoctorConsultationApi doctorConsultationApi;

    public DoctorConsultationRetrofitClient() {//constructor

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request originalRequest = chain.request();

                    Request.Builder builder = originalRequest.newBuilder()
                            .addHeader("Content-Type", "application/json")
                            .header("Authorization",
                                    Credentials.basic(BuildConfig.BASIC_AUTH_USERNAME_WELLNESS, BuildConfig.BASIC_AUTH_PASSWORD_WELLNESS));

                    Request newRequest = builder.build();
                    Log.d(LogTags.DOCTOR_CONSULTANT, newRequest.url().toString());

                    return chain.proceed(newRequest);
                }).build();

        //retrofit object
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL_WELLNESS)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        doctorConsultationApi = retrofit.create(DoctorConsultationApi.class);

    }

    public static synchronized DoctorConsultationRetrofitClient getInstance() {
        if (instance == null) {
            instance = new DoctorConsultationRetrofitClient();
        }
        return instance;

    }

    public DoctorConsultationApi getDCApi() {
        return doctorConsultationApi;
    }
}
