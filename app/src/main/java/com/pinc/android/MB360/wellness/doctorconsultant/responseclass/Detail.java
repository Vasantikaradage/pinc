package com.pinc.android.MB360.wellness.doctorconsultant.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Detail {
    @SerializedName("PACKAGE_PLAN_SR_NO")
    @Expose
    private Integer packagePlanSrNo;
    @SerializedName("PKG_NAME")
    @Expose
    private String pkgName;
    @SerializedName("EXPERIANCE")
    @Expose
    private String experiance;
    @SerializedName("CATOGARY")
    @Expose
    private String catogary;
    @SerializedName("DC_PKG_PRICE")
    @Expose
    private String dcPkgPrice;
    @SerializedName("INCLUDE_GST")
    @Expose
    private String includeGst;
    @SerializedName("CALLS")
    @Expose
    private String calls;
    @SerializedName("VALIDITY_MONTH")
    @Expose
    private String validityMonth;
    @SerializedName("DEPENDENT")
    @Expose
    private String dependent;

    public Integer getPackagePlanSrNo() {
        return packagePlanSrNo;
    }

    public void setPackagePlanSrNo(Integer packagePlanSrNo) {
        this.packagePlanSrNo = packagePlanSrNo;
    }

    public String getPkgName() {
        return pkgName;
    }

    public void setPkgName(String pkgName) {
        this.pkgName = pkgName;
    }

    public String getExperiance() {
        return experiance;
    }

    public void setExperiance(String experiance) {
        this.experiance = experiance;
    }

    public String getCatogary() {
        return catogary;
    }

    public void setCatogary(String catogary) {
        this.catogary = catogary;
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

    public String getCalls() {
        return calls;
    }

    public void setCalls(String calls) {
        this.calls = calls;
    }

    public String getValidityMonth() {
        return validityMonth;
    }

    public void setValidityMonth(String validityMonth) {
        this.validityMonth = validityMonth;
    }

    public String getDependent() {
        return dependent;
    }

    public void setDependent(String dependent) {
        this.dependent = dependent;
    }

}
