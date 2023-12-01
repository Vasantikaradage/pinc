
package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HealthCheckupOngoingResponse {

    @SerializedName("AppointmentList")
    @Expose
    private List<AppointmentHealthCheckup> appointmentList = null;
    @SerializedName("message")
    @Expose
    private Message message;

    public List<AppointmentHealthCheckup> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<AppointmentHealthCheckup> appointmentList) {
        this.appointmentList = appointmentList;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
