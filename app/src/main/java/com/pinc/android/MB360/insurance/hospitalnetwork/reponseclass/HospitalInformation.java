package com.pinc.android.MB360.insurance.hospitalnetwork.reponseclass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.pinc.android.MB360.database.converters.hospitalConverters.HospitalConverter;

import org.simpleframework.xml.ElementList;

import java.util.ArrayList;
import java.util.List;


public class HospitalInformation {
    @ElementList(entry = "Hospitals", inline = true, required = false)
    @TypeConverters(HospitalConverter.class)
    public List<Hospitals> Hospitals = new ArrayList<>();


    public List<Hospitals> getHospitals() {
        return Hospitals;
    }

    public void setHospitals(List<com.pinc.android.MB360.insurance.hospitalnetwork.reponseclass.Hospitals> hospitals) {
        Hospitals = hospitals;
    }

    @Override
    public String toString() {
        return "HospitalInformation{" +
                "Hospitals=" + Hospitals +
                '}';
    }
}
