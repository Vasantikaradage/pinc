package com.pinc.android.MB360.onboarding.authentication;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginEmailRequest {

    @SerializedName("officialemailId")
    @Expose

    private String officialemailId;

    public String getOfficialemailId() {
        return officialemailId;
    }

    public void setOfficialemailId(String officialemailId) {
        this.officialemailId = officialemailId;
    }
}
