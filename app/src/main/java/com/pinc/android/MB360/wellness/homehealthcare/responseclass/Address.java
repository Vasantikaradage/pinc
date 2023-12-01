package com.pinc.android.MB360.wellness.homehealthcare.responseclass;

public class Address {
    private String strLine1;

    private String strLine2;

    private String strLandmark;

    private String strCity;

    private String strState;

    private String strPincode;

    private String strMobileNumber;

    private String strEmail;

    private String WellSrno;

    private String strPersonSrno;

    public String getStrLine1() {
        return strLine1;
    }

    public void setStrLine1(String strLine1) {
        this.strLine1 = strLine1;
    }

    public String getStrLine2() {
        return strLine2;
    }

    public void setStrLine2(String strLine2) {
        this.strLine2 = strLine2;
    }

    public String getStrLandmark() {
        return strLandmark;
    }

    public void setStrLandmark(String strLandmark) {
        this.strLandmark = strLandmark;
    }

    public String getStrCity() {
        return strCity;
    }

    public void setStrCity(String strCity) {
        this.strCity = strCity;
    }

    public String getStrState() {
        return strState;
    }

    public void setStrState(String strState) {
        this.strState = strState;
    }

    public String getStrPincode() {
        return strPincode;
    }

    public void setStrPincode(String strPincode) {
        this.strPincode = strPincode;
    }

    public String getStrMobileNumber() {
        return strMobileNumber;
    }

    public void setStrMobileNumber(String strMobileNumber) {
        this.strMobileNumber = strMobileNumber;
    }

    public String getStrEmail() {
        return strEmail;
    }

    public void setStrEmail(String strEmail) {
        this.strEmail = strEmail;
    }

    public String getWellSrno() {
        return WellSrno;
    }

    public void setWellSrno(String wellSrno) {
        WellSrno = wellSrno;
    }

    public String getStrPersonSrno() {
        return strPersonSrno;
    }

    public void setStrPersonSrno(String strPersonSrno) {
        this.strPersonSrno = strPersonSrno;
    }

    @Override
    public String toString() {
        return "Address{" +
                "strLine1='" + strLine1 + '\'' +
                ", strLine2='" + strLine2 + '\'' +
                ", strLandmark='" + strLandmark + '\'' +
                ", strCity='" + strCity + '\'' +
                ", strState='" + strState + '\'' +
                ", strPincode='" + strPincode + '\'' +
                ", strMobileNumber='" + strMobileNumber + '\'' +
                ", strEmail='" + strEmail + '\'' +
                ", WellSrno='" + WellSrno + '\'' +
                ", strPersonSrno='" + strPersonSrno + '\'' +
                '}';
    }
}
