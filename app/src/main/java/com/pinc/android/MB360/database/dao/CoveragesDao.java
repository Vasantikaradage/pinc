package com.pinc.android.MB360.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pinc.android.MB360.insurance.coverages.repository.responseclass.CoverageDetailsResponse;
import com.pinc.android.MB360.insurance.coverages.repository.responseclass.CoverageResponse;


@Dao
public interface CoveragesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCoverages(CoverageResponse coverageResponse);

    @Query("SELECT * FROM COVERAGE WHERE oeGrpBasInfoSrNo = :oeGrpBasInfoSrNo")
    CoverageResponse getCoverages(String oeGrpBasInfoSrNo);

    @Query("DELETE FROM COVERAGE")
    void deleteAllCoverage();


    //coverage details
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCoverageDetail(CoverageDetailsResponse coverageDetailsResponse);

    @Query("SELECT * FROM COVERAGE_DETAILS WHERE oeGrpBasInfoSrNo=:oeGrpBasInfoSrNo")
    CoverageDetailsResponse getCoverageDetails(String oeGrpBasInfoSrNo);


}
