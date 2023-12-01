
package com.pinc.android.MB360.wellness.homehealthcare.responseclass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackagePTResponse {

    @SerializedName("Packages")
    @Expose
    private List<PackagePT> packages = null;
    @SerializedName("message")
    @Expose
    private Message message;

    public List<PackagePT> getPackages() {
        return packages;
    }

    public void setPackages(List<PackagePT> packages) {
        this.packages = packages;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
