
package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Summary {

    @SerializedName("SelfSponseredPerson")
    @Expose
    private List<SelfSponseredPerson> selfSponseredPerson = null;
    @SerializedName("CompanySponseredPerson")
    @Expose
    private List<Object> companySponseredPerson = null;
    @SerializedName("ShowConfirmButton")
    @Expose
    private Boolean showConfirmButton;
    @SerializedName("Youpay")
    @Expose
    private String youpay;
    @SerializedName("paid")
    @Expose
    private String paid;
    @SerializedName("Total")
    @Expose
    private String total;

    public List<SelfSponseredPerson> getSelfSponseredPerson() {
        return selfSponseredPerson;
    }

    public void setSelfSponseredPerson(List<SelfSponseredPerson> selfSponseredPerson) {
        this.selfSponseredPerson = selfSponseredPerson;
    }

    public List<Object> getCompanySponseredPerson() {
        return companySponseredPerson;
    }

    public void setCompanySponseredPerson(List<Object> companySponseredPerson) {
        this.companySponseredPerson = companySponseredPerson;
    }

    public Boolean getShowConfirmButton() {
        return showConfirmButton;
    }

    public void setShowConfirmButton(Boolean showConfirmButton) {
        this.showConfirmButton = showConfirmButton;
    }

    public String getYoupay() {
        return youpay;
    }

    public void setYoupay(String youpay) {
        this.youpay = youpay;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}
