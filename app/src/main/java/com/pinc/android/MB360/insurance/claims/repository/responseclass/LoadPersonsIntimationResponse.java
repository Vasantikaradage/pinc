package com.pinc.android.MB360.insurance.claims.repository.responseclass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pinc.android.MB360.database.converters.LoadPersonConverter;

import java.util.ArrayList;
import java.util.List;
@Entity(tableName = "INTIMATE_CLAIMS_RELATION", indices =  @Index(value = {"oeGrpBasInfoSrNo"}, unique = true))
@TypeConverters(LoadPersonConverter.class)
public class LoadPersonsIntimationResponse {

    @SerializedName("ClaimBeneficiary")
    @Expose
    private List<ClaimBeneficiary> claimBeneficiary = new ArrayList<>();
    @SerializedName("Result")
    @Expose
    private Result result;

    public List<ClaimBeneficiary> getClaimBeneficiary() {
        return claimBeneficiary;
    }

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String oeGrpBasInfoSrNo;

    @NonNull
    public String getOeGrpBasInfoSrNo() {
        return oeGrpBasInfoSrNo;
    }

    public void setOeGrpBasInfoSrNo(@NonNull String oeGrpBasInfoSrNo) {
        this.oeGrpBasInfoSrNo = oeGrpBasInfoSrNo;
    }

    public void setClaimBeneficiary(List<ClaimBeneficiary> claimBeneficiary) {
        this.claimBeneficiary = claimBeneficiary;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "LoadPersonsIntimationResponse{" +
                "claimBeneficiary=" + claimBeneficiary +
                ", result=" + result +
                '}';
    }
}
