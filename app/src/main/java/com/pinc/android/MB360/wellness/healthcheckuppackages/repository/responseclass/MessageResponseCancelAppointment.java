package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageResponseCancelAppointment {

    @SerializedName("message")
    @Expose
    private Message message;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MessageResponseCancelAppointment{" +
                "message=" + message +
                '}';
    }
}
