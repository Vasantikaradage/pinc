package com.pinc.android.MB360.wellness.doctorconsultant.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAgreement {

    @SerializedName("IsDCAgree")
    @Expose
    private Boolean isDCAgree;
    @SerializedName("Message")
    @Expose
    private String message;

    public Boolean getIsDCAgree() {
        return isDCAgree;
    }

    public void setIsDCAgree(Boolean isDCAgree) {
        this.isDCAgree = isDCAgree;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "UserAgreement{" +
                "isDCAgree=" + isDCAgree +
                ", message='" + message + '\'' +
                '}';
    }
}
