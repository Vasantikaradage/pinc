
package com.pinc.android.MB360.wellness.homehealthcare.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackageDM {

    @SerializedName("PKG_PRICE_MB")
    @Expose
    private String pkgPriceMb;

    public String getPkgPriceMb() {
        return pkgPriceMb;
    }

    public void setPkgPriceMb(String pkgPriceMb) {
        this.pkgPriceMb = pkgPriceMb;
    }

}
