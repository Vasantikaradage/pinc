
package com.pinc.android.MB360.wellness.homehealthcare.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackageDS {

    @SerializedName("HHC_PKG_PRICING")
    @Expose
    private String hhcPkgPricing;
    @SerializedName("HHC_CITY_MAPP_SR_NO")
    @Expose
    private String hhcCityMappSrNo;
    @SerializedName("CATEGORY")
    @Expose
    private String category;
    @SerializedName("PKG_PRICE_MB")
    @Expose
    private String pkgPriceMb;

    public String getHhcPkgPricing() {
        return hhcPkgPricing;
    }

    public void setHhcPkgPricing(String hhcPkgPricing) {
        this.hhcPkgPricing = hhcPkgPricing;
    }

    public String getHhcCityMappSrNo() {
        return hhcCityMappSrNo;
    }

    public void setHhcCityMappSrNo(String hhcCityMappSrNo) {
        this.hhcCityMappSrNo = hhcCityMappSrNo;
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
        return "PackageDS{" +
                "hhcPkgPricing='" + hhcPkgPricing + '\'' +
                ", hhcCityMappSrNo='" + hhcCityMappSrNo + '\'' +
                ", category='" + category + '\'' +
                ", pkgPriceMb='" + pkgPriceMb + '\'' +
                '}';
    }
}
