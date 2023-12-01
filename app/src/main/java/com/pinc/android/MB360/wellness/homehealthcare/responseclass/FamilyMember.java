package com.pinc.android.MB360.wellness.homehealthcare.responseclass;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FamilyMember implements Parcelable {

    @SerializedName("PERSON_NAME")
    @Expose
    private String personName;
    @SerializedName("DOB")
    @Expose
    private String dob;
    @SerializedName("AGE")
    @Expose
    private String age;
    @SerializedName("GENDER")
    @Expose
    private String gender;
    @SerializedName("RELATIONID")
    @Expose
    private String relationid;
    @SerializedName("RELATION_NAME")
    @Expose
    private String relationName;
    @SerializedName("EXT_EMPLOYEE_SR_NO")
    @Expose
    private String extEmployeeSrNo;
    @SerializedName("EXT_PERSON_SR_NO")
    @Expose
    private String extPersonSrNo;
    @SerializedName("CELLPHONE_NUMBER")
    @Expose
    private String cellphoneNumber;
    @SerializedName("EMAIL_ID")
    @Expose
    private String emailId;
    @SerializedName("IS_ADDRESS_SAVED")
    @Expose
    private String isAddressSaved;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private boolean selected=false;

    protected FamilyMember(Parcel in) {
        personName = in.readString();
        dob = in.readString();
        age = in.readString();
        gender = in.readString();
        relationid = in.readString();
        relationName = in.readString();
        extEmployeeSrNo = in.readString();
        extPersonSrNo = in.readString();
        cellphoneNumber = in.readString();
        emailId = in.readString();
        isAddressSaved = in.readString();
    }

    public static final Creator<FamilyMember> CREATOR = new Creator<FamilyMember>() {
        @Override
        public FamilyMember createFromParcel(Parcel in) {
            return new FamilyMember(in);
        }

        @Override
        public FamilyMember[] newArray(int size) {
            return new FamilyMember[size];
        }
    };

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRelationid() {
        return relationid;
    }

    public void setRelationid(String relationid) {
        this.relationid = relationid;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public String getExtEmployeeSrNo() {
        return extEmployeeSrNo;
    }

    public void setExtEmployeeSrNo(String extEmployeeSrNo) {
        this.extEmployeeSrNo = extEmployeeSrNo;
    }

    public String getExtPersonSrNo() {
        return extPersonSrNo;
    }

    public void setExtPersonSrNo(String extPersonSrNo) {
        this.extPersonSrNo = extPersonSrNo;
    }

    public String getCellphoneNumber() {
        return cellphoneNumber;
    }

    public void setCellphoneNumber(String cellphoneNumber) {
        this.cellphoneNumber = cellphoneNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getIsAddressSaved() {
        return isAddressSaved;
    }

    public void setIsAddressSaved(String isAddressSaved) {
        this.isAddressSaved = isAddressSaved;
    }

    @Override
    public String toString() {
        return "FamilyMember{" +
                "personName='" + personName + '\'' +
                ", dob='" + dob + '\'' +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", relationid='" + relationid + '\'' +
                ", relationName='" + relationName + '\'' +
                ", extEmployeeSrNo='" + extEmployeeSrNo + '\'' +
                ", extPersonSrNo='" + extPersonSrNo + '\'' +
                ", cellphoneNumber='" + cellphoneNumber + '\'' +
                ", emailId='" + emailId + '\'' +
                ", isAddressSaved='" + isAddressSaved + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(personName);
        parcel.writeString(dob);
        parcel.writeString(age);
        parcel.writeString(gender);
        parcel.writeString(relationid);
        parcel.writeString(relationName);
        parcel.writeString(extEmployeeSrNo);
        parcel.writeString(extPersonSrNo);
        parcel.writeString(cellphoneNumber);
        parcel.writeString(emailId);
        parcel.writeString(isAddressSaved);
    }
}

