package com.pinc.android.MB360.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pinc.android.MB360.insurance.escalations.repository.responseclass.EscalationsResponse;
@Dao
public interface EscalationsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEscalations(EscalationsResponse escalationsResponse);

    @Query("SELECT * FROM ESCALATION WHERE oeGrpBasInfoSrNo = :oeGrpBasInfoSrNo")
    EscalationsResponse getEscalations(String oeGrpBasInfoSrNo);

    @Query("DELETE FROM ESCALATION")
    void deleteAllEscalations();
}
