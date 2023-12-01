
package com.pinc.android.MB360.wellness.homehealthcare.responseclass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackageNCResponse {

    @SerializedName("Packages")
    @Expose
    private List<PackageNC> packages = null;
    @SerializedName("message")
    @Expose
    private Message message;

    public List<PackageNC> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageNC> packages) {
        this.packages = packages;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
