package com.pinc.android.MB360.wellness.homehealthcare.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pinc.android.MB360.databinding.ItemAppointmentBinding;
import com.pinc.android.MB360.utilities.UtilMethods;
import com.pinc.android.MB360.wellness.homehealthcare.cancellation.OnCancellation;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.ScheduledSummary;
import com.pinc.android.MB360.wellness.homehealthcare.retrofit.RescheduleInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder> {

    List<ScheduledSummary> summaryList;
    Context context;
    String serviceName;
    OnCancellation onCancellation;
    RescheduleInterface rescheduleInterface;

    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    private final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/M/yyyy", Locale.ENGLISH);
    private final SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);


    public SummaryAdapter(List<ScheduledSummary> summaryList, Context context, String serviceName, OnCancellation onCancellation, RescheduleInterface rescheduleInterface) {
        this.summaryList = summaryList;
        this.context = context;
        this.serviceName = serviceName;
        this.onCancellation = onCancellation;
        this.rescheduleInterface = rescheduleInterface;
    }

    @NonNull
    @Override
    public SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAppointmentBinding binding = ItemAppointmentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SummaryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryViewHolder holder, int position) {

        try {
            String strApptSrNo = "";
            holder.binding.txtDependantName.setText(summaryList.get(position).getAppntPerson());
            holder.binding.txtDependantAge.setText(summaryList.get(position).getAppntPersonAge() + " years");
            holder.binding.txtAppointReference.setText(summaryList.get(position).getHhcApptNaRefNo());

            Date strDate = simpleDateFormat3.parse(summaryList.get(position).getScheduledOn());
            String date = simpleDateFormat3.format(strDate);
            holder.binding.txtAppointmentScheduled.setText(date);

            // viewHolder.txtPackageDetails.setText(lstSummary.get(position).getStrPkgPrice());
            String str = summaryList.get(position).getDatePreference();
            if (str.contains(",")) {
                String myStr = str.replaceAll(",", "\n");
                holder.binding.tvappointmentdoneon.setText(myStr);
            } else holder.binding.tvappointmentdoneon.setText(str);
            holder.binding.tvDuration.setText(summaryList.get(position).getNaDurations());
            if (summaryList.get(position).getHhcNaRemarks() != null && !summaryList.get(position).getHhcNaRemarks().equals(""))
                holder.binding.tvRemarks.setText(summaryList.get(position).getHhcNaRemarks());
            else {
                holder.binding.tvRemarks.setVisibility(View.GONE);
                holder.binding.tvRemarkslabel.setVisibility(View.GONE);
            }
            strApptSrNo = summaryList.get(position).getAppntPersonSrNo();
            String strPrice = UtilMethods.PriceFormat(summaryList.get(position).getTotalPrice());
            holder.binding.tvTotalPrice.setText("\u20B9 " + strPrice);

            String strPackagePrice = UtilMethods.PriceFormat(summaryList.get(position).getPkgPrice());
            holder.binding.tvPackagePrice.setText("\u20B9 " + strPackagePrice);


            if (summaryList.get(position).getNaDurations().equals("Monthly")) {
                holder.binding.tvappointmentdoneonlabel.setText("Month Preference");
                holder.binding.tvappointmentdoneon.setText(summaryList.get(position).getFromDate());
                String strFromDate = summaryList.get(position).getFromDate();
                ArrayList<Date> dateArray = new ArrayList<Date>();
                SimpleDateFormat monthformat = new SimpleDateFormat("MMM-YY");
                if (strFromDate.contains(",")) {
                    StringTokenizer mystrToken = new StringTokenizer(strFromDate, ",");
                    while (mystrToken.hasMoreTokens()) {
                        String datesstr = mystrToken.nextToken();
                        dateArray.add(simpleDateFormat.parse(datesstr));
                        Log.d("DATESSS", datesstr);
                    }
                    StringBuilder finalDates = new StringBuilder();
                    for (Date dates : dateArray
                    ) {
                        finalDates.append(monthformat.format(dates)).append(",");
                    }

                    holder.binding.tvappointmentdoneon.setText(finalDates.toString().replaceFirst(".$", ""));
                }
            }

            if (summaryList.get(position).getNaDurations().equals("Monthly") && summaryList.get(position).getDateCondition().equals("DateRangewise")) {
                String DateString = summaryList.get(position).getFromDate() + " To " + summaryList.get(position).getToDate();
                holder.binding.tvappointmentdoneon.setText(DateString);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        holder.binding.llCancelOrder.setOnClickListener(v -> {
            onCancellation.cancelAppointment(summaryList.get(position).getHhcApptInfoSrNo(),position);
        });

        holder.binding.btnreschedule.setOnClickListener(view -> {

            rescheduleInterface.getAppointmentDetails(summaryList.get(position));
        });

    }

    @Override
    public int getItemCount() {
        if (summaryList != null) {
            return summaryList.size();
        } else {
            return 0;
        }
    }


    public static class SummaryViewHolder extends RecyclerView.ViewHolder {
        ItemAppointmentBinding binding;

        public SummaryViewHolder(@NonNull ItemAppointmentBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }

}
