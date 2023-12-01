
package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Packages implements Parcelable {

    @SerializedName("PackageSrNo")
    @Expose
    private String packageSrNo;
    @SerializedName("PackageName")
    @Expose
    private String packageName;
    @SerializedName("IsAgeRestricted")
    @Expose
    private String isAgeRestricted;
    @SerializedName("AgeText")
    @Expose
    private String ageText;
    @SerializedName("MaxAge")
    @Expose
    private String maxAge;
    @SerializedName("MinAge")
    @Expose
    private String minAge;
    @SerializedName("IsGenderRestricted")
    @Expose
    private String isGenderRestricted;
    @SerializedName("GenderText")
    @Expose
    private String genderText;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("PackagePrice")
    @Expose
    private String packagePrice;
    @SerializedName("payment")
    @Expose
    private Object payment;
    @SerializedName("Persons")
    @Expose
    private List<Person> persons = null;
    @SerializedName("group")
    @Expose
    private Object group;

    protected Packages(Parcel in) {
        packageSrNo = in.readString();
        packageName = in.readString();
        isAgeRestricted = in.readString();
        ageText = in.readString();
        maxAge = in.readString();
        minAge = in.readString();
        isGenderRestricted = in.readString();
        genderText = in.readString();
        gender = in.readString();
        packagePrice = in.readString();
    }

    public static final Creator<Packages> CREATOR = new Creator<Packages>() {
        @Override
        public Packages createFromParcel(Parcel in) {
            return new Packages(in);
        }

        @Override
        public Packages[] newArray(int size) {
            return new Packages[size];
        }
    };

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

    public String getIsAgeRestricted() {
        return isAgeRestricted;
    }

    public void setIsAgeRestricted(String isAgeRestricted) {
        this.isAgeRestricted = isAgeRestricted;
    }

    public String getAgeText() {
        return ageText;
    }

    public void setAgeText(String ageText) {
        this.ageText = ageText;
    }

    public String getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(String maxAge) {
        this.maxAge = maxAge;
    }

    public String getMinAge() {
        return minAge;
    }

    public void setMinAge(String minAge) {
        this.minAge = minAge;
    }

    public String getIsGenderRestricted() {
        return isGenderRestricted;
    }

    public void setIsGenderRestricted(String isGenderRestricted) {
        this.isGenderRestricted = isGenderRestricted;
    }

    public String getGenderText() {
        return genderText;
    }

    public void setGenderText(String genderText) {
        this.genderText = genderText;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

    public Object getPayment() {
        return payment;
    }

    public void setPayment(Object payment) {
        this.payment = payment;
    }

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }

    public Object getGroup() {
        return group;
    }

    public void setGroup(Object group) {
        this.group = group;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(packageSrNo);
        parcel.writeString(packageName);
        parcel.writeString(isAgeRestricted);
        parcel.writeString(ageText);
        parcel.writeString(maxAge);
        parcel.writeString(minAge);
        parcel.writeString(isGenderRestricted);
        parcel.writeString(genderText);
        parcel.writeString(gender);
        parcel.writeString(packagePrice);
    }

    @Override
    public String toString() {
        return "Packages{" +
                "packageSrNo='" + packageSrNo + '\'' +
                ", packageName='" + packageName + '\'' +
                ", isAgeRestricted='" + isAgeRestricted + '\'' +
                ", ageText='" + ageText + '\'' +
                ", maxAge='" + maxAge + '\'' +
                ", minAge='" + minAge + '\'' +
                ", isGenderRestricted='" + isGenderRestricted + '\'' +
                ", genderText='" + genderText + '\'' +
                ", gender='" + gender + '\'' +
                ", packagePrice='" + packagePrice + '\'' +
                ", payment=" + payment +
                ", persons=" + persons +
                ", group=" + group +
                '}';
    }
}
