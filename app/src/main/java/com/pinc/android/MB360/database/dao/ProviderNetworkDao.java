package com.pinc.android.MB360.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pinc.android.MB360.insurance.hospitalnetwork.reponseclass.DocumentElementCount;
import com.pinc.android.MB360.insurance.hospitalnetwork.responseclassV1.HospitalInformation;

import java.util.List;

import io.reactivex.Maybe;


@Dao
public interface ProviderNetworkDao {

    //hospital Data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHospitalDetails(List<HospitalInformation> Hospitals);

    @Query("SELECT * FROM  PROVIDER_NETWORK")
    Maybe<List<HospitalInformation>> getHospitals();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDocumentElementCount(DocumentElementCount documentElementCount);

    @Query("SELECT * FROM HOSPITAL_COUNT WHERE oeGrpBasInfoSrNo =:oeGrpBasInfoSrNo")
    DocumentElementCount getDocumentElementCount(String oeGrpBasInfoSrNo);

    @Query("DELETE FROM PROVIDER_NETWORK")
    void deleteAllHospital();



}
