package com.pinc.android.MB360.wellness.doctorconsultant.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DoctorPackages {
    @SerializedName("PACKAGE_PLAN_SR_NO")
    @Expose
    private String packagePlanSrNo;
    @SerializedName("PKG_NAME")
    @Expose
    private String pkgName;
    @SerializedName("DC_PKG_PRICE")
    @Expose
    private String dcPkgPrice;
    @SerializedName("INCLUDE_GST")
    @Expose
    private String includeGst;
    @SerializedName("PKG_EMP_MAPP_SR_NO")
    @Expose
    private String pkgEmpMappSrNo;
    @SerializedName("PKG_PLAN_SR_NO")
    @Expose
    private String pkgPlanSrNo;
    @SerializedName("EXT_EMPLOYEE_SR_NO")
    @Expose
    private String extEmployeeSrNo;
    @SerializedName("UNIQUEID")
    @Expose
    private String uniqueid;
    @SerializedName("EXT_PER_SR_NO")
    @Expose
    private String extPerSrNo;
    @SerializedName("PERSON_NAME")
    @Expose
    private String personName;
    @SerializedName("RELATIONID")
    @Expose
    private String relationid;
    @SerializedName("CELLPHONE_NUMBER")
    @Expose
    private String cellphoneNumber;
    @SerializedName("MOVEMENT_DONE_ON")
    @Expose
    private String movementDoneOn;
    @SerializedName("STATUS")
    @Expose
    private String status;
    @SerializedName("details")
    @Expose
    private List<Detail> details = null;

    public String getPackagePlanSrNo() {
        return packagePlanSrNo;
    }

    public void setPackagePlanSrNo(String packagePlanSrNo) {
        this.packagePlanSrNo = packagePlanSrNo;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getDcPkgPrice() {
        return dcPkgPrice;
    }

    public void setDcPkgPrice(String dcPkgPrice) {
        this.dcPkgPrice = dcPkgPrice;
    }

    public String getIncludeGst() {
        return includeGst;
    }

    public void setIncludeGst(String includeGst) {
        this.includeGst = includeGst;
    }

    public String getPkgEmpMappSrNo() {
        return pkgEmpMappSrNo;
    }

    public void setPkgEmpMappSrNo(String pkgEmpMappSrNo) {
        this.pkgEmpMappSrNo = pkgEmpMappSrNo;
    }

    public String getPkgPlanSrNo() {
        return pkgPlanSrNo;
    }

    public void setPkgPlanSrNo(String pkgPlanSrNo) {
        this.pkgPlanSrNo = pkgPlanSrNo;
    }

    public String getExtEmployeeSrNo() {
        return extEmployeeSrNo;
    }

    public void setExtEmployeeSrNo(String extEmployeeSrNo) {
        this.extEmployeeSrNo = extEmployeeSrNo;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public String getExtPerSrNo() {
        return extPerSrNo;
    }

    public void setExtPerSrNo(String extPerSrNo) {
        this.extPerSrNo = extPerSrNo;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getRelationid() {
        return relationid;
    }

    public void setRelationid(String relationid) {
        this.relationid = relationid;
    }

    public String getCellphoneNumber() {
        return cellphoneNumber;
    }

    public void setCellphoneNumber(String cellphoneNumber) {
        this.cellphoneNumber = cellphoneNumber;
    }

    public String getMovementDoneOn() {
        return movementDoneOn;
    }

    public void setMovementDoneOn(String movementDoneOn) {
        this.movementDoneOn = movementDoneOn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }
}
