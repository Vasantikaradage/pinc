package com.pinc.android.MB360.insurance.enrollment.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Relation {
    @SerializedName("relation_name")
    @Expose
    private String relationName;
    @SerializedName("max_count")
    @Expose
    private Integer maxCount;
    @SerializedName("group")
    @Expose
    private String group;

    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @Override
    public String toString() {
        return "Relation{" +
                "relationName='" + relationName + '\'' +
                ", maxCount=" + maxCount +
                ", group='" + group + '\'' +
                '}';
    }
}
