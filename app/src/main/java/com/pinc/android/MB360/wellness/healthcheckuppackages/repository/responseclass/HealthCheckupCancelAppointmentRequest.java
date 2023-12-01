
package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HealthCheckupCancelAppointmentRequest {

    @SerializedName("EmpIdNo")
    @Expose
    private String empIdNo;
    @SerializedName("FamilySrNo")
    @Expose
    private String familySrNo;
    @SerializedName("GroupSrNo")
    @Expose
    private String groupSrNo;
    @SerializedName("PersonSrNo")
    @Expose
    private String personSrNo;
    @SerializedName("Remark")
    @Expose
    private String remark;

    public String getEmpIdNo() {
        return empIdNo;
    }

    public void setEmpIdNo(String empIdNo) {
        this.empIdNo = empIdNo;
    }

    public String getFamilySrNo() {
        return familySrNo;
    }

    public void setFamilySrNo(String familySrNo) {
        this.familySrNo = familySrNo;
    }

    public String getGroupSrNo() {
        return groupSrNo;
    }

    public void setGroupSrNo(String groupSrNo) {
        this.groupSrNo = groupSrNo;
    }

    public String getPersonSrNo() {
        return personSrNo;
    }

    public void setPersonSrNo(String personSrNo) {
        this.personSrNo = personSrNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
