package com.pinc.android.MB360.wellness.homehealthcare.cancellation.responseclass;

import com.google.gson.annotations.SerializedName;

public class ApiStatus {
    @SerializedName("Message")
    private String strMessage;

    @SerializedName("Status")
    private Boolean boolStatus;

    public String getStrMessage() {
        return strMessage;
    }

    public void setStrMessage(String strMessage) {
        this.strMessage = strMessage;
    }

    public Boolean getBoolStatus() {
        return boolStatus;
    }

    public void setBoolStatus(Boolean boolStatus) {
        this.boolStatus = boolStatus;
    }
}
