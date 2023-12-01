
package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SummaryResponse {

    @SerializedName("Summary")
    @Expose
    private Summary summary;
    @SerializedName("message")
    @Expose
    private Message message;

    public Summary getSummary() {
        return summary;
    }

    public void setSummary(Summary summary) {
        this.summary = summary;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

}
