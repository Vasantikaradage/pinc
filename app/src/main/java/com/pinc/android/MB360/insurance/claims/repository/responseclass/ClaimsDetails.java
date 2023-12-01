package com.pinc.android.MB360.insurance.claims.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClaimsDetails {

    @SerializedName("CLAIMANT")
    @Expose
    private String claimant;
    @SerializedName("EMPLOYEE_NAME")
    @Expose
    private String employeeName;
    @SerializedName("EMPLOYEE_NO")
    @Expose
    private String employeeNo;
    @SerializedName("NAME_OF_HOSPITAL")
    @Expose
    private String nameOfHospital;
    @SerializedName("DOA_LIKELYDOA")
    @Expose
    private String doaLikelydoa;
    @SerializedName("DIAGNOSIS_OR_AILMENT")
    @Expose
    private String diagnosisOrAilment;
    @SerializedName("CLAIM_AMOUNT")
    @Expose
    private String claimAmount;
    @SerializedName("DATE_OF_INTIMATION")
    @Expose
    private String dateOfIntimation;
    @SerializedName("CLAIM_INTIMATION_NO")
    @Expose
    private String claimIntimationNo;

    public String getClaimant() {
        return claimant;
    }

    public void setClaimant(String claimant) {
        this.claimant = claimant;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeNo() {
        return employeeNo;
    }

    public void setEmployeeNo(String employeeNo) {
        this.employeeNo = employeeNo;
    }

    public String getNameOfHospital() {
        return nameOfHospital;
    }

    public void setNameOfHospital(String nameOfHospital) {
        this.nameOfHospital = nameOfHospital;
    }

    public String getDoaLikelydoa() {
        return doaLikelydoa;
    }

    public void setDoaLikelydoa(String doaLikelydoa) {
        this.doaLikelydoa = doaLikelydoa;
    }

    public String getDiagnosisOrAilment() {
        return diagnosisOrAilment;
    }

    public void setDiagnosisOrAilment(String diagnosisOrAilment) {
        this.diagnosisOrAilment = diagnosisOrAilment;
    }

    public String getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(String claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getDateOfIntimation() {
        return dateOfIntimation;
    }

    public void setDateOfIntimation(String dateOfIntimation) {
        this.dateOfIntimation = dateOfIntimation;
    }

    public String getClaimIntimationNo() {
        return claimIntimationNo;
    }

    public void setClaimIntimationNo(String claimIntimationNo) {
        this.claimIntimationNo = claimIntimationNo;
    }
}
