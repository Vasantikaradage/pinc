
package com.pinc.android.MB360.wellness.homehealthcare.responseclass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackageECResponse {

    @SerializedName("Packages")
    @Expose
    private List<PackageEC> packages = null;
    @SerializedName("message")
    @Expose
    private Message message;

    public List<PackageEC> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageEC> packages) {
        this.packages = packages;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
