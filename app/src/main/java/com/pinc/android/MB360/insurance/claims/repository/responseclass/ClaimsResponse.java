
package com.pinc.android.MB360.insurance.claims.repository.responseclass;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.pinc.android.MB360.database.converters.ClaimsInfoConverter;

@Entity(tableName = "INTIMATE_CLAIMS",indices = @Index(value = {"oeGrpBasInfoSrNo"},unique = true))
@TypeConverters(ClaimsInfoConverter.class)

public class ClaimsResponse {

    @SerializedName("Claimslist")
    private List<ClaimsInfo> claimslist = new ArrayList<>();
    @SerializedName("Result")
    @Expose
    private Result result;

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private  String oeGrpBasInfoSrNo;

    @NonNull
    public String getOeGrpBasInfoSrNo() {
        return oeGrpBasInfoSrNo;
    }

    public void setOeGrpBasInfoSrNo(@NonNull String oeGrpBasInfoSrNo) {
        this.oeGrpBasInfoSrNo = oeGrpBasInfoSrNo;
    }

    public List<ClaimsInfo> getClaimslist() {
        return claimslist;
    }

    public void setClaimslist(List<ClaimsInfo> claimslist) {
        this.claimslist = claimslist;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "ClaimsResponse{" +
                "claimslist=" + claimslist +
                ", result=" + result +
                '}';
    }
}
