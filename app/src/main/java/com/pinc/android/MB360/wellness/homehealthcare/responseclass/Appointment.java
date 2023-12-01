package com.pinc.android.MB360.wellness.homehealthcare.responseclass;


/**
 * this class represent the request class for
 * the appointments in home health care services
 * xx
 */
public class Appointment {

   /*retrofit query part
    @Query("PersonSrNo") String strPersonSrno,
    @Query("FamilySrNo") String strFamilySrno,
    @Query("ISRescheduled") int intIsRescheduled,
    @Query("RejtApptSrNo") String intApptSrNo,
    @Query("PkgPriceSrNo") int intPkgPriceSrNo,
    @Query("Date_Condition") int intDateCond,
    @Query(value = "Date_pref", encoded = true) String strdatePref,
    @Query(value = "From_date", encoded = true) String strFromDate,
    @Query(value = "To_date", encoded = true) String strToDate,
    @Query("RescSrNo") int intStrRescSrNo,
    @Query("Remarks") String test*/

    String service;
    String person_sr_no;
    String family_sr_no;
    String rej_appt_sr_no;
    String date_preference;
    String from_date;
    String to_date;
    String remarks;
    String count;
    String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    String total_price;

    int is_rescheduled, package_price_sr_no, date_condition, reschedule_sr_no;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPerson_sr_no() {
        return person_sr_no;
    }

    public void setPerson_sr_no(String person_sr_no) {
        this.person_sr_no = person_sr_no;
    }

    public String getFamily_sr_no() {
        return family_sr_no;
    }

    public void setFamily_sr_no(String family_sr_no) {
        this.family_sr_no = family_sr_no;
    }

    public String getRej_appt_sr_no() {
        return rej_appt_sr_no;
    }

    public void setRej_appt_sr_no(String rej_appt_sr_no) {
        this.rej_appt_sr_no = rej_appt_sr_no;
    }

    public String getDate_preference() {
        return date_preference;
    }

    public void setDate_preference(String date_preference) {
        this.date_preference = date_preference;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public int getIs_rescheduled() {
        return is_rescheduled;
    }

    public void setIs_rescheduled(int is_rescheduled) {
        this.is_rescheduled = is_rescheduled;
    }

    public int getPackage_price_sr_no() {
        return package_price_sr_no;
    }

    public void setPackage_price_sr_no(int package_price_sr_no) {
        this.package_price_sr_no = package_price_sr_no;
    }

    public int getDate_condition() {
        return date_condition;
    }

    public void setDate_condition(int date_condition) {
        this.date_condition = date_condition;
    }

    public int getReschedule_sr_no() {
        return reschedule_sr_no;
    }

    public void setReschedule_sr_no(int reschedule_sr_no) {
        this.reschedule_sr_no = reschedule_sr_no;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "service='" + service + '\'' +
                ", person_sr_no='" + person_sr_no + '\'' +
                ", family_sr_no='" + family_sr_no + '\'' +
                ", rej_appt_sr_no='" + rej_appt_sr_no + '\'' +
                ", date_preference='" + date_preference + '\'' +
                ", from_date='" + from_date + '\'' +
                ", to_date='" + to_date + '\'' +
                ", remarks='" + remarks + '\'' +
                ", count='" + count + '\'' +
                ", price='" + price + '\'' +
                ", total_price='" + total_price + '\'' +
                ", is_rescheduled=" + is_rescheduled +
                ", package_price_sr_no=" + package_price_sr_no +
                ", date_condition=" + date_condition +
                ", reschedule_sr_no=" + reschedule_sr_no +
                '}';
    }
}
