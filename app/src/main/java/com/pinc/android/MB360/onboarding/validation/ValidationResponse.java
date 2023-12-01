package com.pinc.android.MB360.onboarding.validation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValidationResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("OTPValidatedInformation")
    @Expose
    private String oTPValidatedInformation;

    public String getStatus() {
        return status;
    }

    public String getOTPValidatedInformation() {
        return oTPValidatedInformation;
    }

    @Override
    public String toString() {
        return "ValidationResponse{" +
                "status='" + status + '\'' +
                ", oTPValidatedInformation='" + oTPValidatedInformation + '\'' +
                '}';
    }
}
