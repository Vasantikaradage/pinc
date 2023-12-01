package com.pinc.android.MB360.insurance.enrollment.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.DependantDetailsResponse;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.DependantHelperModel;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.EmployeeResponse;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.EnrollmentSummaryResponse;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.InstructionResponse;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.MyCoveragesResponse;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.TopupResponse;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.utilities.UtilMethods;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class EnrollmentRepository {

    Application application;
    public final MutableLiveData<Boolean> loadingState;
    public final MutableLiveData<Boolean> errorState;
    public final MutableLiveData<InstructionResponse> instructionLiveData;
    public final MutableLiveData<MyCoveragesResponse> coveragesLiveData;
    public final MutableLiveData<EmployeeResponse> employeeLiveData;
    public final MutableLiveData<DependantDetailsResponse> dependantDetailLiveData;

    public MutableLiveData<EnrollmentSummaryResponse> summaryLiveData;

    public MutableLiveData<DependantHelperModel> twin1 = new MutableLiveData<>();
    public MutableLiveData<DependantHelperModel> twin2 = new MutableLiveData<>();

    public final MutableLiveData<DependantDetailsResponse> parentalData;
    public final MutableLiveData<TopupResponse> topupData;


    public EnrollmentRepository(Application application) {
        this.application = application;
        this.loadingState = new MutableLiveData<>(true);
        this.errorState = new MutableLiveData<>(false);
        this.instructionLiveData = new MutableLiveData<>();
        this.coveragesLiveData = new MutableLiveData<>();
        this.employeeLiveData = new MutableLiveData<>();
        this.dependantDetailLiveData = new MutableLiveData<>();
        this.parentalData = new MutableLiveData<>();
        this.topupData = new MutableLiveData<>();
        this.summaryLiveData = new MutableLiveData<>();
    }

    public LiveData<InstructionResponse> getInstructionResponse() {

        loadingState.setValue(false);
        //todo api call for instruction
        /**
         * once the api call is done we make change the
         * loading -> false
         * as we don't need the loading after the
         * successful api call
         * in this case (for testing we are getting the response
         * from the json file)
         * **/
        String response = UtilMethods.getAssetJsonData(application, "InstructionResponse.json");
        Type type = new TypeToken<InstructionResponse>() {
        }.getType();
        InstructionResponse instructionResponse = new Gson().fromJson(response, type);
        instructionLiveData.setValue(instructionResponse);
        Log.d(LogTags.ENROLLMENT, "getInstructionResponse: "+ instructionResponse.toString());

        return instructionLiveData;
    }


    public LiveData<InstructionResponse> getInstructionResponseData() {
        return instructionLiveData;
    }

    public LiveData<MyCoveragesResponse> getCoveragesResponse() {

        loadingState.setValue(false);
        //todo api call for Coverages
        /**
         * once the api call is done we make change the
         * loading -> false
         * as we don't need the loading after the
         * successful api call
         * in this case (for testing we are getting the response
         * from the json file)
         * **/
        String response = UtilMethods.getAssetJsonData(application, "CoveragesResponse.json");
        Type type = new TypeToken<MyCoveragesResponse>() {
        }.getType();
        MyCoveragesResponse coveragesResponse = new Gson().fromJson(response, type);
        coveragesLiveData.setValue(coveragesResponse);

        return coveragesLiveData;
    }

    public LiveData<MyCoveragesResponse> getCoveragesData() {
        return coveragesLiveData;
    }

    public LiveData<EmployeeResponse> getEmployeeResponse() {

        loadingState.setValue(false);
        //todo api call for Employee details

        /**
         * once the api call is done we make change the
         * loading -> false
         * as we don't need the loading after the
         * successful api call
         * in this case (for testing we are getting the response
         * from the json file)
         * */
        String response = UtilMethods.getAssetJsonData(application, "EmployeeResponse.json");
        Type type = new TypeToken<EmployeeResponse>() {
        }.getType();
        EmployeeResponse employeeResponse = new Gson().fromJson(response, type);
        employeeLiveData.setValue(employeeResponse);

        return employeeLiveData;
    }

    public LiveData<EmployeeResponse> getEmployeeData() {
        return employeeLiveData;
    }

    public MutableLiveData<DependantDetailsResponse> getDependantDetail() {
        loadingState.setValue(false);
        //todo api call for Employee details

        /**
         * once the api call is done we make change the
         * loading -> false
         * as we don't need the loading after the
         * successful api call
         * in this case (for testing we are getting the response
         * from the json file)
         * */
        String response = UtilMethods.getAssetJsonData(application, "RelationGroupResponse.json");
        Type type = new TypeToken<DependantDetailsResponse>() {
        }.getType();
        DependantDetailsResponse dependantDetailsResponse = new Gson().fromJson(response, type);
        dependantDetailLiveData.setValue(dependantDetailsResponse);

        return dependantDetailLiveData;
    }

    public MutableLiveData<DependantDetailsResponse> getDependantDetailData() {
        return dependantDetailLiveData;
    }


    public MutableLiveData<DependantDetailsResponse> getParental() {
        loadingState.setValue(false);
        //todo api call for Parental details

        /**
         * once the api call is done we make change the
         * loading -> false
         * as we don't need the loading after the
         * successful api call
         * in this case (for testing we are getting the response
         * from the json file)
         * */
        String response = UtilMethods.getAssetJsonData(application, "Parental.json");
        Type type = new TypeToken<DependantDetailsResponse>() {
        }.getType();
        DependantDetailsResponse dependantDetailsResponse = new Gson().fromJson(response, type);
        parentalData.setValue(dependantDetailsResponse);

        return parentalData;
    }

    public MutableLiveData<DependantDetailsResponse> getParentalData() {
        return parentalData;
    }

    public MutableLiveData<TopupResponse> getTopUps() {
        loadingState.setValue(false);
        //todo api call for Top ups details

        /**
         * once the api call is done we make change the
         * loading -> false
         * as we don't need the loading after the
         * successful api call
         * in this case (for testing we are getting the response
         * from the json file)
         * */
        String response = UtilMethods.getAssetJsonData(application, "TopUps.json");
        Type type = new TypeToken<TopupResponse>() {
        }.getType();
        TopupResponse topupResponse = new Gson().fromJson(response, type);
        topupData.setValue(topupResponse);

        return topupData;
    }


    public MutableLiveData<EnrollmentSummaryResponse> getSummary() {
        loadingState.setValue(false);
        //todo api call for summary of enrollment

        /**
         * once the api call is done we make change the
         * loading -> false
         * as we don't need the loading after the
         * successful api call
         * in this case (for testing we are getting the response
         * from the json file)
         * */
        String response = UtilMethods.getAssetJsonData(application, "EnrollmentSummary.json");
        Type type = new TypeToken<EnrollmentSummaryResponse>() {
        }.getType();
        EnrollmentSummaryResponse enrollmentSummaryResponse = new Gson().fromJson(response, type);
        summaryLiveData.setValue(enrollmentSummaryResponse);

        return summaryLiveData;
    }

    public MutableLiveData<EnrollmentSummaryResponse> getSummaryData() {
        return summaryLiveData;
    }

    public MutableLiveData<TopupResponse> getTopUpsData() {
        return topupData;
    }

    //twins details

    public MutableLiveData<DependantHelperModel> getTwin1() {
        return twin1;
    }

    public void setTwin1(DependantHelperModel twin1) {
        this.twin1.setValue(twin1);
    }

    public MutableLiveData<DependantHelperModel> getTwin2() {
        return twin2;
    }

    public void setTwin2(DependantHelperModel twin2) {
        this.twin2.setValue(twin2);
    }
}
