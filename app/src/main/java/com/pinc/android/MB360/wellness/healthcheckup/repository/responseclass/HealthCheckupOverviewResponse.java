
package com.pinc.android.MB360.wellness.healthcheckup.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HealthCheckupOverviewResponse {

    @SerializedName("CompanyOverview")
    @Expose
    private String companyOverview;
    @SerializedName("PreTestPolicy")
    @Expose
    private String preTestPolicy;
    @SerializedName("RefundPolicy")
    @Expose
    private String refundPolicy;
    @SerializedName("RefCanPolicy")
    @Expose
    private String refCanPolicy;
    @SerializedName("Status")
    @Expose
    private Boolean status;
    @SerializedName("Message")
    @Expose
    private String message;

    public String getCompanyOverview() {
        return companyOverview;
    }

    public void setCompanyOverview(String companyOverview) {
        this.companyOverview = companyOverview;
    }

    public String getPreTestPolicy() {
        return preTestPolicy;
    }

    public void setPreTestPolicy(String preTestPolicy) {
        this.preTestPolicy = preTestPolicy;
    }

    public String getRefundPolicy() {
        return refundPolicy;
    }

    public void setRefundPolicy(String refundPolicy) {
        this.refundPolicy = refundPolicy;
    }

    public String getRefCanPolicy() {
        return refCanPolicy;
    }

    public void setRefCanPolicy(String refCanPolicy) {
        this.refCanPolicy = refCanPolicy;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
