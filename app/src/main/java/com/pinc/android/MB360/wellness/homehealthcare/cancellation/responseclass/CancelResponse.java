package com.pinc.android.MB360.wellness.homehealthcare.cancellation.responseclass;

import com.google.gson.annotations.SerializedName;

public class CancelResponse {
    @SerializedName("message")
    private ApiStatus apiStatus;

    public ApiStatus getApiStatus() {
        return apiStatus;
    }

    public void setApiStatus(ApiStatus apiStatus) {
        this.apiStatus = apiStatus;
    }
}
