
package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HealthCheckupUpdatePaymentRequest {

    @SerializedName("EmpIdNo")
    @Expose
    private String empIdNo;
    @SerializedName("ExtGroupSrNo")
    @Expose
    private String extGroupSrNo;
    @SerializedName("FamilySrNo")
    @Expose
    private String familySrNo;
    @SerializedName("OrderId")
    @Expose
    private String orderId;
    @SerializedName("OrderReferenceNumber")
    @Expose
    private String orderReferenceNumber;
    @SerializedName("PaymentId")
    @Expose
    private String paymentId;
    @SerializedName("Signature")
    @Expose
    private String signature;

    public String getEmpIdNo() {
        return empIdNo;
    }

    public void setEmpIdNo(String empIdNo) {
        this.empIdNo = empIdNo;
    }

    public String getExtGroupSrNo() {
        return extGroupSrNo;
    }

    public void setExtGroupSrNo(String extGroupSrNo) {
        this.extGroupSrNo = extGroupSrNo;
    }

    public String getFamilySrNo() {
        return familySrNo;
    }

    public void setFamilySrNo(String familySrNo) {
        this.familySrNo = familySrNo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderReferenceNumber() {
        return orderReferenceNumber;
    }

    public void setOrderReferenceNumber(String orderReferenceNumber) {
        this.orderReferenceNumber = orderReferenceNumber;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

}
