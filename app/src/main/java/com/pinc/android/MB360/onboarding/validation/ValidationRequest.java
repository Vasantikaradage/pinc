package com.pinc.android.MB360.onboarding.validation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ValidationRequest {
    @SerializedName("mobileno")
    @Expose
    private String mobileno;
    @SerializedName("enteredotp")
    @Expose
    private Integer enteredotp;

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public Integer getEnteredotp() {
        return enteredotp;
    }

    public void setEnteredotp(Integer enteredotp) {
        this.enteredotp = enteredotp;
    }
}
