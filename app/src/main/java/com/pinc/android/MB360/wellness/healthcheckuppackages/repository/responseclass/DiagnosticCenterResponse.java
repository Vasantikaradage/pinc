
package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DiagnosticCenterResponse {

    @SerializedName("DaignosticCenterList")
    @Expose
    private List<DiagnosticCenter> daignosticCenterList = null;
    @SerializedName("ServerDate")
    @Expose
    private ServerDate serverDate;
    @SerializedName("message")
    @Expose
    private Message message;

    public List<DiagnosticCenter> getDaignosticCenterList() {
        return daignosticCenterList;
    }

    public void setDaignosticCenterList(List<DiagnosticCenter> daignosticCenterList) {
        this.daignosticCenterList = daignosticCenterList;
    }

    public ServerDate getServerDate() {
        return serverDate;
    }

    public void setServerDate(ServerDate serverDate) {
        this.serverDate = serverDate;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
