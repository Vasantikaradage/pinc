
package com.pinc.android.MB360.wellness.homehealthcare.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackageEC {

    @SerializedName("HHC_EC_PKG_PRICING_SR_NO")
    @Expose
    private String hhcEcPkgPricingSrNo;
    @SerializedName("HHC_NA_CITY_MAPP_SR_NO")
    @Expose
    private String hhcNaCityMappSrNo;
    @SerializedName("CATEGORY")
    @Expose
    private String category;
    @SerializedName("PKG_PRICE_MB")
    @Expose
    private String pkgPriceMb;

    public String getHhcEcPkgPricingSrNo() {
        return hhcEcPkgPricingSrNo;
    }

    public void setHhcEcPkgPricingSrNo(String hhcEcPkgPricingSrNo) {
        this.hhcEcPkgPricingSrNo = hhcEcPkgPricingSrNo;
    }

    public String getHhcNaCityMappSrNo() {
        return hhcNaCityMappSrNo;
    }

    public void setHhcNaCityMappSrNo(String hhcNaCityMappSrNo) {
        this.hhcNaCityMappSrNo = hhcNaCityMappSrNo;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPkgPriceMb() {
        return pkgPriceMb;
    }

    public void setPkgPriceMb(String pkgPriceMb) {
        this.pkgPriceMb = pkgPriceMb;
    }

    @Override
    public String toString() {

        return "PackageEC{" +
                "hhcEcPkgPricingSrNo='" + hhcEcPkgPricingSrNo + '\'' +
                ", hhcNaCityMappSrNo='" + hhcNaCityMappSrNo + '\'' +
                ", category='" + category + '\'' +
                ", pkgPriceMb='" + pkgPriceMb + '\'' +
                '}';
    }
}
