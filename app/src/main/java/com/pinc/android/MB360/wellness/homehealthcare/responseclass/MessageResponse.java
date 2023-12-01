package com.pinc.android.MB360.wellness.homehealthcare.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageResponse {
    @SerializedName("message")
    @Expose
    public Message message;

    public Message getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "MessageResponse{" +
                "message=" + message +
                '}';
    }
}
