
package com.pinc.android.MB360.wellness.homehealthcare.responseclass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackageDSResponse {

    @SerializedName("Packages")
    @Expose
    private List<PackageDS> packages = null;
    @SerializedName("message")
    @Expose
    private Message message;

    public List<PackageDS> getPackages() {
        return packages;
    }

    public void setPackages(List<PackageDS> packages) {
        this.packages = packages;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
