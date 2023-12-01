
package com.pinc.android.MB360.wellness.homehealthcare.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackageST {

    @SerializedName("PKG_PRICE_MB")
    @Expose
    private String pkgPriceMb;
    @SerializedName("HHC_PKG_PRICING")
    @Expose
    private String hhcPkgPricing;

    public String getPkgPriceMb() {
        return pkgPriceMb;
    }

    public void setPkgPriceMb(String pkgPriceMb) {
        this.pkgPriceMb = pkgPriceMb;
    }

    public String getHhcPkgPricing() {
        return hhcPkgPricing;
    }

    public void setHhcPkgPricing(String hhcPkgPricing) {
        this.hhcPkgPricing = hhcPkgPricing;
    }


    @Override
    public String toString() {
        return "PackageST{" +
                "pkgPriceMb='" + pkgPriceMb + '\'' +
                ", hhcPkgPricing='" + hhcPkgPricing + '\'' +
                '}';
    }
}
