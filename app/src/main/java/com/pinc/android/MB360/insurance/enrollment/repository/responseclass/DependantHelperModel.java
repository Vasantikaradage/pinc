package com.pinc.android.MB360.insurance.enrollment.repository.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;

public class DependantHelperModel {
    @SerializedName("relation_name")
    @Expose
    private String relationName;
    @SerializedName("max_count")
    @Expose
    private Integer maxCount;
    @SerializedName("group")
    @Expose
    private String group;
    @SerializedName("is_added")
    @Expose
    private Boolean isAdded;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("date_of_birth")
    @Expose
    private String dateOfBirth;
    @SerializedName("isdifferentlyAble")
    @Expose
    private Boolean isDifferentlyAble;
    @SerializedName("age")
    @Expose
    private String age;
    private String gender;
    private File document;
    boolean canEdit;
    boolean canDelete;


    public DependantHelperModel(String relationName, Integer maxCount, String group, Boolean isAdded,
                                String name,
                                String dateOfBirth, Boolean isDifferentlyAble, File document, String age, Boolean canEdit, Boolean canDelete, String gender) {
        this.relationName = relationName;
        this.maxCount = maxCount;
        this.group = group;
        this.isAdded = isAdded;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.isDifferentlyAble = isDifferentlyAble;
        this.document = document;
        this.age = age;
        this.canEdit = true;
        this.canDelete = false;
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

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

    public Boolean getIsAdded() {
        return isAdded;
    }

    public void setIsAdded(Boolean isAdded) {
        this.isAdded = isAdded;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Boolean isDifferentlyAble() {
        return isDifferentlyAble;
    }

    public void setIsDifferentlyAble(Boolean isdifferentlyAble) {
        this.isDifferentlyAble = isdifferentlyAble;
    }

    public File getDocument() {
        return document;
    }

    public void setDocument(File document) {
        this.document = document;
    }

    @Override
    public String toString() {
        return "DependantHelperModel{" +
                "relationName='" + relationName + '\'' +
                ", maxCount=" + maxCount +
                ", group='" + group + '\'' +
                ", isAdded=" + isAdded +
                ", name='" + name + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", isDifferentlyAble=" + isDifferentlyAble +
                ", age='" + age + '\'' +
                ", gender='" + gender + '\'' +
                ", document=" + document +
                ", canEdit=" + canEdit +
                ", canDelete=" + canDelete +
                '}';
    }
}
