
package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Relation {

    @SerializedName("RelationId")
    @Expose
    private String relationId;
    @SerializedName("RelationName")
    @Expose
    private String relationName;

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    @Override
    public String toString() {
        return "Relation{" +
                "relationId='" + relationId + '\'' +
                ", relationName='" + relationName + '\'' +
                '}';
    }
}
