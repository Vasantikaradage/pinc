package com.pinc.android.MB360.wellness.homehealthcare.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PackageResponse {
    @SerializedName("Packages")
    @Expose
    private List<HomeHealthCarePackage> packages = null;
    @SerializedName("message")
    @Expose
    private Message message;

    public List<HomeHealthCarePackage> getPackages() {
        return packages;
    }

    public void setPackages(List<HomeHealthCarePackage> packages) {
        this.packages = packages;
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
                "packages=" + packages +
                ", message=" + message +
                '}';
    }
}
