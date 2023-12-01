
package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Person implements Parcelable {

    @SerializedName("PersonSRNo")
    @Expose
    private Integer personSRNo;
    @SerializedName("FamilySrNo")
    @Expose
    private Object familySrNo;
    @SerializedName("ExtPersonSRNo")
    @Expose
    private Integer extPersonSRNo;
    @SerializedName("IsBooking")
    @Expose
    private Boolean isBooking;
    @SerializedName("PaymentConfFlag")
    @Expose
    private Integer paymentConfFlag;
    @SerializedName("ApptSrInfoNo")
    @Expose
    private String apptSrInfoNo;
    @SerializedName("IsMobEmailConf")
    @Expose
    private Integer isMobEmailConf;
    @SerializedName("Price")
    @Expose
    private String price;
    @SerializedName("Amount")
    @Expose
    private Integer amount;
    @SerializedName("BookingStatus")
    @Expose
    private String bookingStatus;
    @SerializedName("CanBeDeletedFalg")
    @Expose
    private Integer canBeDeletedFalg;
    @SerializedName("SponserdBy")
    @Expose
    private String sponserdBy;
    @SerializedName("SponserdByFlag")
    @Expose
    private String sponserdByFlag;
    @SerializedName("PackageSrNo")
    @Expose
    private String packageSrNo;
    @SerializedName("PackageName")
    @Expose
    private Object packageName;
    @SerializedName("PersonName")
    @Expose
    private String personName;
    @SerializedName("Age")
    @Expose
    private String age;
    @SerializedName("DateOfBirth")
    @Expose
    private Object dateOfBirth;
    @SerializedName("EmailID")
    @Expose
    private String emailID;
    @SerializedName("CellPhoneNumber")
    @Expose
    private String cellPhoneNumber;
    @SerializedName("Gender")
    @Expose
    private String gender;
    @SerializedName("RelationID")
    @Expose
    private String relationID;
    @SerializedName("RelationName")
    @Expose
    private String relationName;
    @SerializedName("IsBooked")
    @Expose
    private Object isBooked;
    @SerializedName("IsChbChecked")
    @Expose
    private Boolean isChbChecked;
    @SerializedName("IsDisabled")
    @Expose
    private Boolean isDisabled;
    @SerializedName("AppointmentStatusBadge")
    @Expose
    private String appointmentStatusBadge;
    @SerializedName("paidNotScheduled")
    @Expose
    private String paidNotScheduled;
    @SerializedName("tooltip")
    @Expose
    private String tooltip;
    @SerializedName("IsSelectedInWellness")
    @Expose
    private Boolean isSelectedInWellness;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    private boolean selected=false;

    protected Person(Parcel in) {
        if (in.readByte() == 0) {
            personSRNo = null;
        } else {
            personSRNo = in.readInt();
        }
        if (in.readByte() == 0) {
            extPersonSRNo = null;
        } else {
            extPersonSRNo = in.readInt();
        }
        byte tmpIsBooking = in.readByte();
        isBooking = tmpIsBooking == 0 ? null : tmpIsBooking == 1;
        if (in.readByte() == 0) {
            paymentConfFlag = null;
        } else {
            paymentConfFlag = in.readInt();
        }
        apptSrInfoNo = in.readString();
        if (in.readByte() == 0) {
            isMobEmailConf = null;
        } else {
            isMobEmailConf = in.readInt();
        }
        price = in.readString();
        if (in.readByte() == 0) {
            amount = null;
        } else {
            amount = in.readInt();
        }
        bookingStatus = in.readString();
        if (in.readByte() == 0) {
            canBeDeletedFalg = null;
        } else {
            canBeDeletedFalg = in.readInt();
        }
        sponserdBy = in.readString();
        sponserdByFlag = in.readString();
        packageSrNo = in.readString();
        personName = in.readString();
        age = in.readString();
        emailID = in.readString();
        cellPhoneNumber = in.readString();
        gender = in.readString();
        relationID = in.readString();
        relationName = in.readString();
        byte tmpIsChbChecked = in.readByte();
        isChbChecked = tmpIsChbChecked == 0 ? null : tmpIsChbChecked == 1;
        byte tmpIsDisabled = in.readByte();
        isDisabled = tmpIsDisabled == 0 ? null : tmpIsDisabled == 1;
        appointmentStatusBadge = in.readString();
        paidNotScheduled = in.readString();
        tooltip = in.readString();
        byte tmpIsSelectedInWellness = in.readByte();
        isSelectedInWellness = tmpIsSelectedInWellness == 0 ? null : tmpIsSelectedInWellness == 1;
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    public Integer getPersonSRNo() {
        return personSRNo;
    }

    public void setPersonSRNo(Integer personSRNo) {
        this.personSRNo = personSRNo;
    }

    public Object getFamilySrNo() {
        return familySrNo;
    }

    public void setFamilySrNo(Object familySrNo) {
        this.familySrNo = familySrNo;
    }

    public Integer getExtPersonSRNo() {
        return extPersonSRNo;
    }

    public void setExtPersonSRNo(Integer extPersonSRNo) {
        this.extPersonSRNo = extPersonSRNo;
    }

    public Boolean getIsBooking() {
        return isBooking;
    }

    public void setIsBooking(Boolean isBooking) {
        this.isBooking = isBooking;
    }

    public Integer getPaymentConfFlag() {
        return paymentConfFlag;
    }

    public void setPaymentConfFlag(Integer paymentConfFlag) {
        this.paymentConfFlag = paymentConfFlag;
    }

    public String getApptSrInfoNo() {
        return apptSrInfoNo;
    }

    public void setApptSrInfoNo(String apptSrInfoNo) {
        this.apptSrInfoNo = apptSrInfoNo;
    }

    public Integer getIsMobEmailConf() {
        return isMobEmailConf;
    }

    public void setIsMobEmailConf(Integer isMobEmailConf) {
        this.isMobEmailConf = isMobEmailConf;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public Integer getCanBeDeletedFalg() {
        return canBeDeletedFalg;
    }

    public void setCanBeDeletedFalg(Integer canBeDeletedFalg) {
        this.canBeDeletedFalg = canBeDeletedFalg;
    }

    public String getSponserdBy() {
        return sponserdBy;
    }

    public void setSponserdBy(String sponserdBy) {
        this.sponserdBy = sponserdBy;
    }

    public String getSponserdByFlag() {
        return sponserdByFlag;
    }

    public void setSponserdByFlag(String sponserdByFlag) {
        this.sponserdByFlag = sponserdByFlag;
    }

    public String getPackageSrNo() {
        return packageSrNo;
    }

    public void setPackageSrNo(String packageSrNo) {
        this.packageSrNo = packageSrNo;
    }

    public Object getPackageName() {
        return packageName;
    }

    public void setPackageName(Object packageName) {
        this.packageName = packageName;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Object getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Object dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }

    public void setCellPhoneNumber(String cellPhoneNumber) {
        this.cellPhoneNumber = cellPhoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRelationID() {
        return relationID;
    }

    public void setRelationID(String relationID) {
        this.relationID = relationID;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public Object getIsBooked() {
        return isBooked;
    }

    public void setIsBooked(Object isBooked) {
        this.isBooked = isBooked;
    }

    public Boolean getIsChbChecked() {
        return isChbChecked;
    }

    public void setIsChbChecked(Boolean isChbChecked) {
        this.isChbChecked = isChbChecked;
    }

    public Boolean getIsDisabled() {
        return isDisabled;
    }

    public void setIsDisabled(Boolean isDisabled) {
        this.isDisabled = isDisabled;
    }

    public String getAppointmentStatusBadge() {
        return appointmentStatusBadge;
    }

    public void setAppointmentStatusBadge(String appointmentStatusBadge) {
        this.appointmentStatusBadge = appointmentStatusBadge;
    }

    public String getPaidNotScheduled() {
        return paidNotScheduled;
    }

    public void setPaidNotScheduled(String paidNotScheduled) {
        this.paidNotScheduled = paidNotScheduled;
    }

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public Boolean getIsSelectedInWellness() {
        return isSelectedInWellness;
    }

    public void setIsSelectedInWellness(Boolean isSelectedInWellness) {
        this.isSelectedInWellness = isSelectedInWellness;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        if (personSRNo == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(personSRNo);
        }
        if (extPersonSRNo == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(extPersonSRNo);
        }
        parcel.writeByte((byte) (isBooking == null ? 0 : isBooking ? 1 : 2));
        if (paymentConfFlag == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(paymentConfFlag);
        }
        parcel.writeString(apptSrInfoNo);
        if (isMobEmailConf == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(isMobEmailConf);
        }
        parcel.writeString(price);
        if (amount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(amount);
        }
        parcel.writeString(bookingStatus);
        if (canBeDeletedFalg == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(canBeDeletedFalg);
        }
        parcel.writeString(sponserdBy);
        parcel.writeString(sponserdByFlag);
        parcel.writeString(packageSrNo);
        parcel.writeString(personName);
        parcel.writeString(age);
        parcel.writeString(emailID);
        parcel.writeString(cellPhoneNumber);
        parcel.writeString(gender);
        parcel.writeString(relationID);
        parcel.writeString(relationName);
        parcel.writeByte((byte) (isChbChecked == null ? 0 : isChbChecked ? 1 : 2));
        parcel.writeByte((byte) (isDisabled == null ? 0 : isDisabled ? 1 : 2));
        parcel.writeString(appointmentStatusBadge);
        parcel.writeString(paidNotScheduled);
        parcel.writeString(tooltip);
        parcel.writeByte((byte) (isSelectedInWellness == null ? 0 : isSelectedInWellness ? 1 : 2));
    }
}
