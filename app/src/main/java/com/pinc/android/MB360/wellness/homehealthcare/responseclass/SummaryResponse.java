package com.pinc.android.MB360.wellness.homehealthcare.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SummaryResponse {
    @SerializedName("ScheduledSummary")
    @Expose
    private List<ScheduledSummary> scheduledSummary = null;
    @SerializedName("message")
    @Expose
    private Message message;

    public List<ScheduledSummary> getScheduledSummary() {
        return scheduledSummary;
    }

    public void setScheduledSummary(List<ScheduledSummary> scheduledSummary) {
        this.scheduledSummary = scheduledSummary;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SummaryResponse{" +
                "scheduledSummary=" + scheduledSummary +
                ", message=" + message +
                '}';
    }
}
