
package com.pinc.android.MB360.wellness.healthcheckup.repository.requestclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DependentRequest {

    @SerializedName("Age")
    @Expose
    private String age;
    @SerializedName("DateOfBirth")
    @Expose
    private String dateOfBirth;
    @SerializedName("FamilySrNo")
    @Expose
    private String familySrNo;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("PersonName")
    @Expose
    private String personName;
    @SerializedName("RelationID")
    @Expose
    private String relationID;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFamilySrNo() {
        return familySrNo;
    }

    public void setFamilySrNo(String familySrNo) {
        this.familySrNo = familySrNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getRelationID() {
        return relationID;
    }

    public void setRelationID(String relationID) {
        this.relationID = relationID;
    }

}
