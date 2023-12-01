package com.pinc.android.MB360.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pinc.android.MB360.insurance.FAQ.repository.responseclass.FaqResponse;
@Dao
public interface FaqDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFAQ(FaqResponse faqResponse);

    @Query("SELECT * FROM FAQ WHERE oeGrpBasInfoSrNo = :oeGrpBasInfoSrNo")
    FaqResponse getFAQ(String oeGrpBasInfoSrNo);

    @Query("DELETE FROM FAQ")
    void deleteAllFAQ();
}
