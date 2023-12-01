package com.pinc.android.MB360.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pinc.android.MB360.insurance.adminsetting.responseclass.AdminSettingResponse;

@Dao
public interface EnrollmentWindowCountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertEnrollmentWindowCount(AdminSettingResponse adminSettingResponse);

    @Query("SELECT * FROM ENROLLMENT_WINDOW_COUNT WHERE oeGrpBasInfoSrNo = :oeGrpBasInfoSrNo")
    AdminSettingResponse getEnrollmentCount(String oeGrpBasInfoSrNo);

}
