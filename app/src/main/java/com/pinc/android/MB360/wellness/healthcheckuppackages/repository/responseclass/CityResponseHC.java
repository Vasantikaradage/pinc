
package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CityResponseHC {

    @SerializedName("CitiyList")
    @Expose
    private List<String> citiyList = null;
    @SerializedName("message")
    @Expose
    private MessageCityHC message;

    public List<String> getCitiyList() {
        return citiyList;
    }

    public void setCitiyList(List<String> citiyList) {
        this.citiyList = citiyList;
    }

    public MessageCityHC getMessage() {
        return message;
    }

    public void setMessage(MessageCityHC message) {
        this.message = message;
    }

}
