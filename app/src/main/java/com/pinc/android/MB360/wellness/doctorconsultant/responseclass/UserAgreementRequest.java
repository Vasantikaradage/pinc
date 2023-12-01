package com.pinc.android.MB360.wellness.doctorconsultant.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAgreementRequest {
    @SerializedName("GroupChildSrNo")
    @Expose
    private String groupChildSrNo;
    @SerializedName("GroupCode")
    @Expose
    private String groupCode;
    @SerializedName("ExtEmpSrNo")
    @Expose
    private String extEmpSrNo;
    @SerializedName("VendorSrNo")
    @Expose
    private String vendorSrNo;

    public String getGroupChildSrNo() {
        return groupChildSrNo;
    }

    public void setGroupChildSrNo(String groupChildSrNo) {
        this.groupChildSrNo = groupChildSrNo;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getExtEmpSrNo() {
        return extEmpSrNo;
    }

    public void setExtEmpSrNo(String extEmpSrNo) {
        this.extEmpSrNo = extEmpSrNo;
    }

    public String getVendorSrNo() {
        return vendorSrNo;
    }

    public void setVendorSrNo(String vendorSrNo) {
        this.vendorSrNo = vendorSrNo;
    }

    @Override
    public String toString() {
        return "UserAgreementRequest{" +
                "groupChildSrNo='" + groupChildSrNo + '\'' +
                ", groupCode='" + groupCode + '\'' +
                ", extEmpSrNo='" + extEmpSrNo + '\'' +
                ", vendorSrNo='" + vendorSrNo + '\'' +
                '}';
    }
}
