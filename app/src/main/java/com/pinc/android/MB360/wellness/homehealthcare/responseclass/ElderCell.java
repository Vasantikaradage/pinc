package com.pinc.android.MB360.wellness.homehealthcare.responseclass;

public class ElderCell {
    String feature;
    int value;


    public ElderCell(String feature, int value) {
        this.feature = feature;
        this.value = value;
    }


    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ElderCell{" +
                "feature='" + feature + '\'' +
                ", value=" + value +
                '}';
    }
}
