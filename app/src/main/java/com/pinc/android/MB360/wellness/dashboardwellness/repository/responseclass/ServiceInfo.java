
package com.pinc.android.MB360.wellness.dashboardwellness.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceInfo {

    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("Position")
    @Expose
    private String position;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "ServiceInfo{" +
                "name='" + name + '\'' +
                ", position='" + position + '\'' +
                '}';
    }
}
