
package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyNoRequest {

    @SerializedName("EmailId")
    @Expose
    private String emailId;
    @SerializedName("MobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("PersonSrNo")
    @Expose
    private String personSrNo;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPersonSrNo() {
        return personSrNo;
    }

    public void setPersonSrNo(String personSrNo) {
        this.personSrNo = personSrNo;
    }

    @Override
    public String toString() {
        return "VerifyNoRequest{" +
                "emailId='" + emailId + '\'' +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", personSrNo='" + personSrNo + '\'' +
                '}';
    }
}
