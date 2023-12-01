package com.pinc.android.MB360.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pinc.android.MB360.insurance.FAQ.repository.responseclass.FaqResponse;
import com.pinc.android.MB360.insurance.repository.responseclass.LoadSessionResponse;

@Dao
public interface LoadSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLoadSession(LoadSessionResponse loadSessionResponse);

    @Query("SELECT * FROM LOAD_SESSION")
    LoadSessionResponse getLoadSession();


}
