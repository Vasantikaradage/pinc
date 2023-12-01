
package com.pinc.android.MB360.wellness.dashboardwellness.repository.responseclass;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashboardServiceResponse {

    @SerializedName("Services")
    @Expose
    private List<ServiceInfo> services = null;
    @SerializedName("message")
    @Expose
    private Message message;

    public List<ServiceInfo> getServices() {
        return services;
    }

    public void setServices(List<ServiceInfo> services) {
        this.services = services;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "DashboardServiceResponse{" +
                "services=" + services +
                ", message=" + message +
                '}';
    }
}
