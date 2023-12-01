package com.pinc.android.MB360.insurance.claims.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimsDetailsResponse {

    @SerializedName("Detail")
    @Expose
    private ClaimsDetails detail;
    @SerializedName("Result")
    @Expose
    private Result result;

    public ClaimsDetails getDetail() {
        return detail;
    }

    public void setDetail(ClaimsDetails detail) {
        this.detail = detail;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

}
