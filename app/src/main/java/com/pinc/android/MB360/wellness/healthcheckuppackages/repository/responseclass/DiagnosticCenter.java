
package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiagnosticCenter implements Parcelable {

    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("CenterSrNo")
    @Expose
    private Integer centerSrNo;
    @SerializedName("Address")
    @Expose
    private String address;
    @SerializedName("Location")
    @Expose
    private String location;
    @SerializedName("Pincode")
    @Expose
    private String pincode;

    protected DiagnosticCenter(Parcel in) {
        city = in.readString();
        name = in.readString();
        if (in.readByte() == 0) {
            centerSrNo = null;
        } else {
            centerSrNo = in.readInt();
        }
        address = in.readString();
        location = in.readString();
        pincode = in.readString();
    }

    public static final Creator<DiagnosticCenter> CREATOR = new Creator<DiagnosticCenter>() {
        @Override
        public DiagnosticCenter createFromParcel(Parcel in) {
            return new DiagnosticCenter(in);
        }

        @Override
        public DiagnosticCenter[] newArray(int size) {
            return new DiagnosticCenter[size];
        }
    };

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCenterSrNo() {
        return centerSrNo;
    }

    public void setCenterSrNo(Integer centerSrNo) {
        this.centerSrNo = centerSrNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(city);
        parcel.writeString(name);
        if (centerSrNo == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(centerSrNo);
        }
        parcel.writeString(address);
        parcel.writeString(location);
        parcel.writeString(pincode);
    }
}
