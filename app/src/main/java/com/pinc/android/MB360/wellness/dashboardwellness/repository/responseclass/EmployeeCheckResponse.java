
package com.pinc.android.MB360.wellness.dashboardwellness.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmployeeCheckResponse {

    @SerializedName("ExtGroupSrNo")
    @Expose
    private String extGroupSrNo;
    @SerializedName("ExtFamilySrNo")
    @Expose
    private String extFamilySrNo;
    @SerializedName("OrderMasterNo")
    @Expose
    private String orderMasterNo;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("Status")
    @Expose
    private Boolean status;

    public String getExtGroupSrNo() {
        return extGroupSrNo;
    }

    public void setExtGroupSrNo(String extGroupSrNo) {
        this.extGroupSrNo = extGroupSrNo;
    }

    public String getExtFamilySrNo() {
        return extFamilySrNo;
    }

    public void setExtFamilySrNo(String extFamilySrNo) {
        this.extFamilySrNo = extFamilySrNo;
    }

    public String getOrderMasterNo() {
        return orderMasterNo;
    }

    public void setOrderMasterNo(String orderMasterNo) {
        this.orderMasterNo = orderMasterNo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "EmployeeCheckResponse{" +
                "extGroupSrNo='" + extGroupSrNo + '\'' +
                ", extFamilySrNo='" + extFamilySrNo + '\'' +
                ", orderMasterNo='" + orderMasterNo + '\'' +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
