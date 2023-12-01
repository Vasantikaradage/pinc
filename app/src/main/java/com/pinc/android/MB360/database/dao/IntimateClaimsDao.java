package com.pinc.android.MB360.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pinc.android.MB360.insurance.claims.repository.responseclass.ClaimsResponse;
import com.pinc.android.MB360.insurance.claims.repository.responseclass.LoadPersonsIntimationResponse;

@Dao
public interface IntimateClaimsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertClaimData(ClaimsResponse claimsResponse);

    @Query("SELECT * FROM INTIMATE_CLAIMS WHERE oeGrpBasInfoSrNo=:oeGrpBasInfoSrNo")
    ClaimsResponse getClaims(String oeGrpBasInfoSrNo);

    //loadPerson
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLoadPerson(LoadPersonsIntimationResponse loadPersonsIntimationResponse);

    @Query("SELECT * FROM INTIMATE_CLAIMS_RELATION WHERE oeGrpBasInfoSrNo= :oeGrpBasInfoSrNo")
    LoadPersonsIntimationResponse getLoadPerson(String oeGrpBasInfoSrNo );
}
