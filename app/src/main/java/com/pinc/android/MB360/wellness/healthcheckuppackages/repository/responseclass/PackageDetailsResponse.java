
package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PackageDetailsResponse {

    @SerializedName("SpecificationList")
    @Expose
    private List<PackageInfo> specificationList = null;
    @SerializedName("message")
    @Expose
    private Message message;

    public List<PackageInfo> getSpecificationList() {
        return specificationList;
    }

    public void setSpecificationList(List<PackageInfo> specificationList) {
        this.specificationList = specificationList;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
