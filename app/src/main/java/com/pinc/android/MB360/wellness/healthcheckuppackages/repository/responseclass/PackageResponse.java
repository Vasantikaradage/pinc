
package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackageResponse {

    @SerializedName("PackagesList")
    @Expose
    private List<Packages> packagesList = null;
    @SerializedName("message")
    @Expose
    private Message message;

    public List<Packages> getPackagesList() {
        return packagesList;
    }

    public void setPackagesList(List<Packages> packagesList) {
        this.packagesList = packagesList;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "PackageResponse{" +
                "packagesList=" + packagesList +
                ", message=" + message +
                '}';
    }
}
