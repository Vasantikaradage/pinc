
package com.pinc.android.MB360.wellness.homehealthcare.responseclass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HealthcareOverviewResponse {

    @SerializedName("Overview")
    @Expose
    private List<OverviewHealthcare> overview = null;

    @SerializedName("message")
    @Expose
    private Message message;

    public List<OverviewHealthcare> getOverview() {
        return overview;
    }

    public void setOverview(List<OverviewHealthcare> overview) {
        this.overview = overview;
    }


    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }


}


