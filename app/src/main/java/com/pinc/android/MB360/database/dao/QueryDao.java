package com.pinc.android.MB360.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pinc.android.MB360.insurance.queries.responseclass.QueryDetails;
import com.pinc.android.MB360.insurance.queries.responseclass.QueryResponse;

@Dao
public interface QueryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQuery(QueryResponse QueryResponse);

    @Query("Select * from QUERY_TABLE WHERE empSrNo = :empSrNo")
    QueryResponse getQueryData(String empSrNo );



    // Query details
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertQueryDetail(QueryDetails queryDetails);

    @Query("SELECT  * FROM QUERY_DETAIL_LIST WHERE custQuerySrNo=:custQuerySrNo")
    QueryDetails getQueryDeatils(String custQuerySrNo);

}
