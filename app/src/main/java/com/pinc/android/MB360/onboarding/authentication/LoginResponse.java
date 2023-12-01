package com.pinc.android.MB360.onboarding.authentication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("OTPStatusInformation")
    @Expose
    private String oTPStatusInformation;

    public String getStatus() {
        return status;
    }

    public String getOTPStatusInformation() {
        return oTPStatusInformation;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setoTPStatusInformation(String oTPStatusInformation) {
        this.oTPStatusInformation = oTPStatusInformation;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "status='" + status + '\'' +
                ", oTPStatusInformation='" + oTPStatusInformation + '\'' +
                '}';
    }
}
