
package com.pinc.android.MB360.wellness.homehealthcare.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PackageLT {

    @SerializedName("HHC_PKG_PRICING")
    @Expose
    private String hhcPkgPricing;
    @SerializedName("HHC_LT_CITY_MAPP")
    @Expose
    private String hhcLtCityMapp;
    @SerializedName("HHC_LT_HOURS")
    @Expose
    private String hhcLtHours;
    @SerializedName("HHC_LT_DURATIONS")
    @Expose
    private String hhcLtDurations;
    @SerializedName("HHC_LT_NACOUNT")
    @Expose
    private String hhcLtNacount;
    @SerializedName("HHC_LT_CATEGORY")
    @Expose
    private String hhcLtCategory;
    @SerializedName("PKG_PRICE_MB")
    @Expose
    private String pkgPriceMb;

    public String getHhcPkgPricing() {
        return hhcPkgPricing;
    }

    public void setHhcPkgPricing(String hhcPkgPricing) {
        this.hhcPkgPricing = hhcPkgPricing;
    }

    public String getHhcLtCityMapp() {
        return hhcLtCityMapp;
    }

    public void setHhcLtCityMapp(String hhcLtCityMapp) {
        this.hhcLtCityMapp = hhcLtCityMapp;
    }

    public String getHhcLtHours() {
        return hhcLtHours;
    }

    public void setHhcLtHours(String hhcLtHours) {
        this.hhcLtHours = hhcLtHours;
    }

    public String getHhcLtDurations() {
        return hhcLtDurations;
    }

    public void setHhcLtDurations(String hhcLtDurations) {
        this.hhcLtDurations = hhcLtDurations;
    }

    public String getHhcLtNacount() {
        return hhcLtNacount;
    }

    public void setHhcLtNacount(String hhcLtNacount) {
        this.hhcLtNacount = hhcLtNacount;
    }

    public String getHhcLtCategory() {
        return hhcLtCategory;
    }

    public void setHhcLtCategory(String hhcLtCategory) {
        this.hhcLtCategory = hhcLtCategory;
    }

    public String getPkgPriceMb() {
        return pkgPriceMb;
    }

    public void setPkgPriceMb(String pkgPriceMb) {
        this.pkgPriceMb = pkgPriceMb;
    }

}
