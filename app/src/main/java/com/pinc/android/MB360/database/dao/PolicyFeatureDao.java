package com.pinc.android.MB360.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import com.pinc.android.MB360.insurance.policyfeatures.repository.responseclass.PolicyFeaturesResponseOffline;

import java.util.List;

@Dao
public interface PolicyFeatureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPolicyFeature(PolicyFeaturesResponseOffline policyFeaturesResponse);

    @Query("SELECT * FROM POLICY_FEATURE WHERE oeGrpBasInfoSrNo = :oeGrpBasInfoSrNo")
    PolicyFeaturesResponseOffline getPolicyFeature(String oeGrpBasInfoSrNo);

    @Query("DELETE FROM POLICY_FEATURE")
    void deleteAllPolicyFeature();
}
