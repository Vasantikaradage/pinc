package com.pinc.android.MB360.wellness.homehealthcare.responseclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeHealthCarePackage {

    @SerializedName("HHC_PKG_PRICING")
    @Expose
    private String hhcPkgPricing;
    @SerializedName("HHC_NA_CITY_MAPP")
    @Expose
    private String hhcNaCityMapp;
    @SerializedName("HHC_NA_HOURS")
    @Expose
    private String hhcNaHours;
    @SerializedName("HHC_NA_DURATIONS")
    @Expose
    private String hhcNaDurations;
    @SerializedName("HHC_NA_NACOUNT")
    @Expose
    private String hhcNaNacount;
    @SerializedName("PKG_PRICE_MB")
    @Expose
    private String pkgPriceMb;

    public String getHhcPkgPricing() {
        return hhcPkgPricing;
    }

    public void setHhcPkgPricing(String hhcPkgPricing) {
        this.hhcPkgPricing = hhcPkgPricing;
    }

    public String getHhcNaCityMapp() {
        return hhcNaCityMapp;
    }

    public void setHhcNaCityMapp(String hhcNaCityMapp) {
        this.hhcNaCityMapp = hhcNaCityMapp;
    }

    public String getHhcNaHours() {
        return hhcNaHours;
    }

    public void setHhcNaHours(String hhcNaHours) {
        this.hhcNaHours = hhcNaHours;
    }

    public String getHhcNaDurations() {
        return hhcNaDurations;
    }

    public void setHhcNaDurations(String hhcNaDurations) {
        this.hhcNaDurations = hhcNaDurations;
    }

    public String getHhcNaNacount() {
        return hhcNaNacount;
    }

    public void setHhcNaNacount(String hhcNaNacount) {
        this.hhcNaNacount = hhcNaNacount;
    }

    public String getPkgPriceMb() {
        return pkgPriceMb;
    }

    public void setPkgPriceMb(String pkgPriceMb) {
        this.pkgPriceMb = pkgPriceMb;
    }

    @Override
    public String toString() {
        return "HomeHealthCarePackage{" +
                "hhcPkgPricing='" + hhcPkgPricing + '\'' +
                ", hhcNaCityMapp='" + hhcNaCityMapp + '\'' +
                ", hhcNaHours='" + hhcNaHours + '\'' +
                ", hhcNaDurations='" + hhcNaDurations + '\'' +
                ", hhcNaNacount='" + hhcNaNacount + '\'' +
                ", pkgPriceMb='" + pkgPriceMb + '\'' +
                '}';
    }
}
