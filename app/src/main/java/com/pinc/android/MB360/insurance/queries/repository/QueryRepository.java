package com.pinc.android.MB360.insurance.queries.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.pinc.android.MB360.database.AppDatabase;
import com.pinc.android.MB360.database.dao.QueryDao;
import com.pinc.android.MB360.insurance.queries.repository.retrofit.QueryAddRetrofitClient;
import com.pinc.android.MB360.insurance.queries.repository.retrofit.QueryRetrofitClient;
import com.pinc.android.MB360.insurance.queries.responseclass.Message;
import com.pinc.android.MB360.insurance.queries.responseclass.QueryDetails;
import com.pinc.android.MB360.insurance.queries.responseclass.QueryResponse;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.utilities.ResponseException;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueryRepository {

    Application application;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;

    public final MutableLiveData<QueryResponse> queryResponse;
    public final MutableLiveData<QueryDetails> queryDetailsResponse;
    public final MutableLiveData<Message> markingResolve;
    public final MutableLiveData<Message> addQueryResponse;
    FirebaseCrashlytics crashlytics;

    public final MutableLiveData<Boolean> loadingDetailState;
    public final MutableLiveData<Boolean> errorDetailState;
    private AppDatabase appDatabase;
    private QueryDao queryDao;



    public QueryRepository(Application application) {
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>();
        this.application = application;
        this.queryResponse = new MutableLiveData<>();
        this.queryDetailsResponse = new MutableLiveData<>();
        this.markingResolve = new MutableLiveData<>();
        this.addQueryResponse = new MutableLiveData<>();
        crashlytics = FirebaseCrashlytics.getInstance();

        this.loadingDetailState = new MutableLiveData<>(true);
        this.errorDetailState = new MutableLiveData<>();
        appDatabase=AppDatabase.getInstance(application);
        queryDao=appDatabase.queryDao();
    }

    /**
     * this function  has the business logic for calling
     * and parsing the @Query response
     **/

    public MutableLiveData<QueryResponse> getQuery(String empSrNo) {
        Call<QueryResponse> queryCall = QueryRetrofitClient.getInstance().getQueryApi().getAllQueries(empSrNo);

        queryCall.enqueue(new Callback<QueryResponse>() {
            @Override
            public void onResponse(Call<QueryResponse> call, Response<QueryResponse> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                        queryResponse.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                        response.body().setEmpSrNo(empSrNo);
                        queryDao.insertQuery(response.body());

                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.QUERY_ACTIVITY, "Error: ", e);
                        queryResponse.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }
                } else {
                    Log.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                    queryResponse.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                    ResponseException responseException = new ResponseException("Queries -> getQueries -> Response Code:- " + String.valueOf(response.code()) + "EmployeeSrnNo:- " + empSrNo);
                    crashlytics.recordException(responseException);

                }
            }

            @Override
            public void onFailure(Call<QueryResponse> call, Throwable t) {
                Log.e(LogTags.QUERY_ACTIVITY, "OnFailure: " + t.getLocalizedMessage());
                t.printStackTrace();
                try {

                    Log.d(LogTags.QUERY_ACTIVITY, "onFailure: " + queryDao.getQueryData(empSrNo).toString());
                    queryResponse.setValue(queryDao.getQueryData(empSrNo));
                    loadingState.setValue(false);
                    errorState.setValue(false);

                } catch (Exception e) {
                    e.printStackTrace();
                    queryResponse.setValue(null);
                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }
        });
        return queryResponse;
    }

    public MutableLiveData<QueryDetails> getQueryDetails(String custQuerySrNo) {
        Call<QueryDetails> queryDetailsCall = QueryRetrofitClient.getInstance().getQueryApi().getQueryDetails(custQuerySrNo);

        queryDetailsCall.enqueue(new Callback<QueryDetails>() {
            @Override
            public void onResponse(Call<QueryDetails> call, Response<QueryDetails> response) {
                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                        queryDetailsResponse.setValue(response.body());
                        errorDetailState.setValue(false);
                        loadingDetailState.setValue(false);
                        response.body().setCustQuerySrNo(custQuerySrNo);
                        queryDao.insertQueryDetail(response.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.QUERY_ACTIVITY, "Error: ", e);
                        queryDetailsResponse.setValue(null);
                        errorDetailState.setValue(true);
                        loadingDetailState.setValue(false);
                    }
                } else {
                    Log.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                    queryDetailsResponse.setValue(response.body());
                    errorDetailState.setValue(true);
                    loadingDetailState.setValue(false);
                    ResponseException responseException = new ResponseException("Queries -> getQueryDetails -> Response Code:- " + String.valueOf(response.code()) + "custQuerySrNo:- " + custQuerySrNo);
                    crashlytics.recordException(responseException);
                }
            }

            @Override
            public void onFailure(Call<QueryDetails> call, Throwable t) {
                Log.e(LogTags.QUERY_ACTIVITY, "OnFailure: " + t.getLocalizedMessage());
                t.printStackTrace();
                try {

                    Log.d(LogTags.QUERY_ACTIVITY, "onFailure: " + queryDao.getQueryDeatils(custQuerySrNo).toString());
                    queryDao.getQueryDeatils(custQuerySrNo);
                    queryDetailsResponse.setValue(queryDao.getQueryDeatils(custQuerySrNo));
                    loadingState.setValue(false);
                    errorState.setValue(false);


                } catch (Exception e) {
                    e.printStackTrace();

                    loadingState.setValue(false);
                    errorState.setValue(true);
                }
            }
        });
        return queryDetailsResponse;
    }


    public MutableLiveData<QueryResponse> getQueryData() {
        return queryResponse;
    }

    public MutableLiveData<QueryDetails> getQueryDetailsData() {
        return queryDetailsResponse;
    }


    public LiveData<Boolean> getLoading() {
        return loadingState;
    }

    public LiveData<Boolean> getError() {
        return errorState;
    }

    public LiveData<Message> markResolve(String eqCustQrySrNo) {

        Call<Message> markCall = QueryRetrofitClient.getInstance().getQueryApi().markAsResolved(eqCustQrySrNo);

        markCall.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                        markingResolve.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.QUERY_ACTIVITY, "Error: ", e);
                        markingResolve.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }
                } else {
                    Log.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                    markingResolve.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);

                }

            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.e(LogTags.QUERY_ACTIVITY, "OnFailure: " + t.getLocalizedMessage());
                errorState.setValue(true);
                loadingState.setValue(false);
                markingResolve.setValue(null);
                t.printStackTrace();
            }
        });

        return markingResolve;

    }

    public LiveData<Message> addQuery(RequestBody requestBody) {

        Log.d(LogTags.QUERY_ACTIVITY, "addQuery: " + requestBody.contentType());
        Call<Message> replyQuery = QueryAddRetrofitClient.getInstance().getQueryApi().AddQuery(requestBody);

        replyQuery.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {


                if (response.code() == 200) {
                    try {
                        Log.d(LogTags.QUERY_ACTIVITY, "onResponse: " + response.body());
                        addQueryResponse.setValue(response.body());
                        errorState.setValue(false);
                        loadingState.setValue(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LogTags.QUERY_ACTIVITY, "Error: ", e);
                        markingResolve.setValue(null);
                        errorState.setValue(true);
                        loadingState.setValue(false);
                    }
                } else {
                    Log.e(LogTags.QUERY_ACTIVITY, "onResponse: ERROR " + response.code());
                   /* ResponseException responseException = new ResponseException("Query-> Add Query-> Response Code:- " + String.valueOf(response.code()) + "RequestBody:- " + requestBody.toString());
                    crashlytics.recordException(responseException);*/
                    addQueryResponse.setValue(response.body());
                    errorState.setValue(true);
                    loadingState.setValue(false);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                errorState.setValue(true);
                loadingState.setValue(false);
                queryResponse.setValue(null);
                t.printStackTrace();
            }
        });


        return addQueryResponse;
    }
    public void setLoadingFromFilePicker(){
        loadingDetailState.setValue(false);
    }

    public void resetDetails() {
        queryDetailsResponse.setValue(null);

    }
}
