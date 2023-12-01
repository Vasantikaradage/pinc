
package com.pinc.android.MB360.wellness.homehealthcare.responseclass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackageLTResponse {

    @SerializedName("Packages")
    @Expose
    private List<PackageLT> packageLTS = null;
    @SerializedName("message")
    @Expose
    private Message message;

    public List<PackageLT> getPackages() {
        return packageLTS;
    }

    public void setPackages(List<PackageLT> packageLTS) {
        this.packageLTS = packageLTS;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
