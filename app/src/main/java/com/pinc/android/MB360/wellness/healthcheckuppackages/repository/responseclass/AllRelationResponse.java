
package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllRelationResponse {

    @SerializedName("message")
    @Expose
    private Message message;
    @SerializedName("Relations")
    @Expose
    private List<Relation> relations = null;
    @SerializedName("ServerDate")
    @Expose
    private ServerDate serverDate;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<Relation> getRelations() {
        return relations;
    }

    public void setRelations(List<Relation> relations) {
        this.relations = relations;
    }

    public ServerDate getServerDate() {
        return serverDate;
    }

    public void setServerDate(ServerDate serverDate) {
        this.serverDate = serverDate;
    }

}
