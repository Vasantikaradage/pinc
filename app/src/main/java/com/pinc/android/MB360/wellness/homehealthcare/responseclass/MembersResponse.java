package com.pinc.android.MB360.wellness.homehealthcare.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MembersResponse {
    @SerializedName("FamilyMembers")
    @Expose
    private List<FamilyMember> familyMembers = null;
    @SerializedName("message")
    @Expose
    private Message message;

    public List<FamilyMember> getFamilyMembers() {
        return familyMembers;
    }

    public void setFamilyMembers(List<FamilyMember> familyMembers) {
        this.familyMembers = familyMembers;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "MembersResponse{" +
                "familyMembers=" + familyMembers +
                ", message=" + message +
                '}';
    }
}
