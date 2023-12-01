package com.pinc.android.MB360.insurance.profile.repository;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;


import androidx.lifecycle.MutableLiveData;

import com.pinc.android.MB360.database.AppDatabase;
import com.pinc.android.MB360.database.dao.ProfileDao;
import com.pinc.android.MB360.insurance.profile.repository.retrofit.ProfileRetrofitClient;
import com.pinc.android.MB360.insurance.profile.response.Message;
import com.pinc.android.MB360.insurance.profile.response.ProfileResponse;
import com.pinc.android.MB360.utilities.FileUtil;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.utilities.MediaTypes;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {
    Application application;
    private final MutableLiveData<ProfileResponse> profileMutableLiveData;
    private final MutableLiveData<Boolean> error;
    private final MutableLiveData<Boolean> loading;
    private final MutableLiveData<Message> documentUploadResponse;
    private AppDatabase appDatabase;
    private ProfileDao profileDao;


    public ProfileRepository(Application application) {
        this.application = application;
        profileMutableLiveData = new MutableLiveData<>();
        this.error = new MutableLiveData<>(false);
        this.loading = new MutableLiveData<>(true);
        documentUploadResponse = new MutableLiveData<>();
        appDatabase=AppDatabase.getInstance(application);
        profileDao=appDatabase.profileDao();


    }


    public MutableLiveData<ProfileResponse> getProfile(String groupChild, String oeGrpBasInfoSrNo, String EmployeeSrNo) {

        Call<ProfileResponse> profileCall = ProfileRetrofitClient.getInstance().getProfileApi().getProfile(groupChild, oeGrpBasInfoSrNo, EmployeeSrNo);

        profileCall.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {

                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.PROFILE_ACTIVITY, "onResponse: " + response.body());
                        profileMutableLiveData.setValue(response.body());
                        loading.setValue(false);
                        error.setValue(false);
                        response.body().setOeGrpBasInfoSrNo(oeGrpBasInfoSrNo);

                        profileDao.insertProfile(response.body());



                    } catch (Exception e) {
                        e.printStackTrace();
                        profileMutableLiveData.setValue(null);
                        error.setValue(true);
                        loading.setValue(false);
                        Log.e(LogTags.PROFILE_ACTIVITY, "onCatch: ", e);
                    }

                } else {
                    Log.d(LogTags.PROFILE_ACTIVITY, "onResponse: " + response.body());
                    profileMutableLiveData.setValue(response.body());
                    error.setValue(true);
                    loading.setValue(false);
                }

            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {

                Log.e(LogTags.PROFILE_ACTIVITY, "onFailure: ", t);

                try {

                    Log.d(LogTags.PROFILE_ACTIVITY, "onFailure: " + profileDao.getProfile(oeGrpBasInfoSrNo).toString());
                    profileMutableLiveData.setValue(profileDao.getProfile(oeGrpBasInfoSrNo));
                    loading.setValue(false);
                    error.setValue(false);

                } catch (Exception e) {
                    e.printStackTrace();

                    loading.setValue(false);
                    error.setValue(true);
                }
            }
        });

        return profileMutableLiveData;
    }

    public MutableLiveData<ProfileResponse> getProfileData() {
        return profileMutableLiveData;
    }

    public MutableLiveData<Message> uploadDocuments(Uri fileUri, String groupChildSrNo, String oeGrpBasInfoSrNo, String employeeSrNo, String docType, String docNo) {

        try {
            File file = FileUtil.from(application.getApplicationContext(), fileUri);

            JSONObject map = new JSONObject();
            map.put("strGRoupChildSrno", groupChildSrNo);
            map.put("strOegrpbasinfsrno", oeGrpBasInfoSrNo);
            map.put("strEmpSrno", employeeSrNo);
            map.put("strDocType", docType);
            map.put("strDocNo", docNo);

            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            final MediaType MEDIA_TYPE = MediaTypes.fromFile(file);

            builder.addFormDataPart(file.getName(), FileUtil.getFileName(application.getApplicationContext(), Uri.fromFile(file)),
                    RequestBody.create(MEDIA_TYPE, file));

            builder.addFormDataPart("QueryData", map.toString());
            RequestBody uploadRequest = builder.build();

            Call<Message> uploadCall = ProfileRetrofitClient.getInstance().getProfileApi().uploadDocuments(uploadRequest);

            uploadCall.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {
                    try {
                        if (response.code() == 200) {
                            Log.d(LogTags.PROFILE_ACTIVITY, "OnResponse : " + response.body().toString());
                            documentUploadResponse.setValue(response.body());
                            loading.setValue(false);
                            error.setValue(false);

                        } else {
                            Log.d(LogTags.PROFILE_ACTIVITY, "OnResponse : " + response.toString());
                            documentUploadResponse.setValue(response.body());
                            error.setValue(false);
                            loading.setValue(false);
                            Toast.makeText(application, "Something went wrong! " + response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(LogTags.PROFILE_ACTIVITY, "CaughtError: ", e);
                        documentUploadResponse.setValue(null);
                        error.setValue(false);
                        loading.setValue(false);
                        Toast.makeText(application, "Something went wrong!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {
                    Log.e(LogTags.PROFILE_ACTIVITY, "onFailure: ", t);
                    documentUploadResponse.setValue(null);
                    error.setValue(false);
                    loading.setValue(false);
                    Toast.makeText(application, "Something went wrong!", Toast.LENGTH_SHORT).show();
                }
            });


        } catch (JSONException | IOException e) {
            e.printStackTrace();
            Log.e(LogTags.PROFILE_ACTIVITY, "CaughtError: ", e);
            documentUploadResponse.setValue(null);
            error.setValue(false);
            loading.setValue(false);
            Toast.makeText(application, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }


        return documentUploadResponse;
    }

    public MutableLiveData<Boolean> getError() {
        return error;
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }
}
