
package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentHealthCheckup {

    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("PaymentStatus")
    @Expose
    private String paymentStatus;
    @SerializedName("DaignosticCenterName")
    @Expose
    private String daignosticCenterName;
    @SerializedName("DaignosticCenterCity")
    @Expose
    private String daignosticCenterCity;
    @SerializedName("DaignosticCenterAddress")
    @Expose
    private String daignosticCenterAddress;
    @SerializedName("Remark")
    @Expose
    private String remark;
    @SerializedName("ScheduleDates")
    @Expose
    private String scheduleDates;
    @SerializedName("OrderInfo")
    @Expose
    private String orderInfo;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("PackageSrNo")
    @Expose
    private String packageSrNo;
    @SerializedName("PackageName")
    @Expose
    private String packageName;
    @SerializedName("PersonSrNo")
    @Expose
    private String personSrNo;
    @SerializedName("Price")
    @Expose
    private String price;
    @SerializedName("CanCancel")
    @Expose
    private Boolean canCancel;
    @SerializedName("ApptSrNo")
    @Expose
    private String apptSrNo;
    @SerializedName("RejApptSrNo")
    @Expose
    private String rejApptSrNo;
    @SerializedName("ApptReschPaymentDone")
    @Expose
    private String apptReschPaymentDone;
    @SerializedName("ApptFreeReschFlag")
    @Expose
    private String apptFreeReschFlag;
    @SerializedName("PaymentFlag")
    @Expose
    private Integer paymentFlag;
    @SerializedName("SponserdBy")
    @Expose
    private String sponserdBy;
    @SerializedName("RelationId")
    @Expose
    private String relationId;
    @SerializedName("RelationName")
    @Expose
    private String relationName;
    @SerializedName("CancelationCharge")
    @Expose
    private String cancelationCharge;
    @SerializedName("RefundAmount")
    @Expose
    private String refundAmount;
    @SerializedName("RescheduleCharge")
    @Expose
    private String rescheduleCharge;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getDaignosticCenterName() {
        return daignosticCenterName;
    }

    public void setDaignosticCenterName(String daignosticCenterName) {
        this.daignosticCenterName = daignosticCenterName;
    }

    public String getDaignosticCenterCity() {
        return daignosticCenterCity;
    }

    public void setDaignosticCenterCity(String daignosticCenterCity) {
        this.daignosticCenterCity = daignosticCenterCity;
    }

    public String getDaignosticCenterAddress() {
        return daignosticCenterAddress;
    }

    public void setDaignosticCenterAddress(String daignosticCenterAddress) {
        this.daignosticCenterAddress = daignosticCenterAddress;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getScheduleDates() {
        return scheduleDates;
    }

    public void setScheduleDates(String scheduleDates) {
        this.scheduleDates = scheduleDates;
    }

    public String getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(String orderInfo) {
        this.orderInfo = orderInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPackageSrNo() {
        return packageSrNo;
    }

    public void setPackageSrNo(String packageSrNo) {
        this.packageSrNo = packageSrNo;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPersonSrNo() {
        return personSrNo;
    }

    public void setPersonSrNo(String personSrNo) {
        this.personSrNo = personSrNo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Boolean getCanCancel() {
        return canCancel;
    }

    public void setCanCancel(Boolean canCancel) {
        this.canCancel = canCancel;
    }

    public String getApptSrNo() {
        return apptSrNo;
    }

    public void setApptSrNo(String apptSrNo) {
        this.apptSrNo = apptSrNo;
    }

    public String getRejApptSrNo() {
        return rejApptSrNo;
    }

    public void setRejApptSrNo(String rejApptSrNo) {
        this.rejApptSrNo = rejApptSrNo;
    }

    public String getApptReschPaymentDone() {
        return apptReschPaymentDone;
    }

    public void setApptReschPaymentDone(String apptReschPaymentDone) {
        this.apptReschPaymentDone = apptReschPaymentDone;
    }

    public String getApptFreeReschFlag() {
        return apptFreeReschFlag;
    }

    public void setApptFreeReschFlag(String apptFreeReschFlag) {
        this.apptFreeReschFlag = apptFreeReschFlag;
    }

    public Integer getPaymentFlag() {
        return paymentFlag;
    }

    public void setPaymentFlag(Integer paymentFlag) {
        this.paymentFlag = paymentFlag;
    }

    public String getSponserdBy() {
        return sponserdBy;
    }

    public void setSponserdBy(String sponserdBy) {
        this.sponserdBy = sponserdBy;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getCancelationCharge() {
        return cancelationCharge;
    }

    public void setCancelationCharge(String cancelationCharge) {
        this.cancelationCharge = cancelationCharge;
    }

    public String getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(String refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getRescheduleCharge() {
        return rescheduleCharge;
    }

    public void setRescheduleCharge(String rescheduleCharge) {
        this.rescheduleCharge = rescheduleCharge;
    }

}
