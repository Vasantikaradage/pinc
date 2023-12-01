package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.ItemOngoingHealthcheckupBinding;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.cancellationHC.OnCancellationHealthCheckup;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.rescheduleHC.OnRescheduleHealthCheckup;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.AppointmentHealthCheckup;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Packages;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.retrofit.PackageInterface;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class SummaryHealthCheckupAdapter extends RecyclerView.Adapter<SummaryHealthCheckupAdapter.SummaryViewHolder> {

    List<AppointmentHealthCheckup> appointmentHealthCheckupList;
    Context context;
    String serviceName;
    OnCancellationHealthCheckup onCancellationHealthCheckup;
    OnRescheduleHealthCheckup onRescheduleHealthCheckup;

    PackageInterface packageInterface;
    Packages packages;
    View view;

    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
    private final SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/M/yyyy", Locale.ENGLISH);
    private final SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);


    public SummaryHealthCheckupAdapter(List<AppointmentHealthCheckup> appointmentHealthCheckupList, Context context,
                                       OnCancellationHealthCheckup onCancellationHealthCheckup, OnRescheduleHealthCheckup onRescheduleHealthCheckup) {
        this.appointmentHealthCheckupList = appointmentHealthCheckupList;
        this.context = context;
        this.serviceName = serviceName;
        this.onCancellationHealthCheckup = onCancellationHealthCheckup;
        this.onRescheduleHealthCheckup = onRescheduleHealthCheckup;
    }

    @NonNull
    @Override
    public SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOngoingHealthcheckupBinding binding = ItemOngoingHealthcheckupBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SummaryViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryViewHolder holder, int position) {

        try {
            String strApptSrNo = "";
            holder.binding.txtName.setText(appointmentHealthCheckupList.get(position).getName());
            holder.binding.tvstatus.setText(appointmentHealthCheckupList.get(position).getStatus());
            holder.binding.tvhospitalname.setText(appointmentHealthCheckupList.get(position).getDaignosticCenterName());
            holder.binding.tvhospitaladdress.setText(appointmentHealthCheckupList.get(position).getDaignosticCenterAddress());

            holder.binding.tvstatus.setText(MessageFormat.format("{0}{1}",
                    appointmentHealthCheckupList.get(position).getStatus().substring(0, 1).toUpperCase(),
                    appointmentHealthCheckupList.get(position).getStatus().substring(1).toLowerCase()));

            if (TextUtils.equals( appointmentHealthCheckupList.get(position).getSponserdBy().toLowerCase(), "company sponsored")) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    holder.binding.tvPackagenameorange.setText(Html.fromHtml("Sponsored",
                            Html.FROM_HTML_MODE_COMPACT));
                else
                    holder.binding.tvPackagenameorange.setText(Html.fromHtml("Sponsored"));

                holder.binding.tvPackagenameorange.setVisibility(View.VISIBLE);
                holder.binding.tvPackagenameorangeback.setVisibility(View.VISIBLE);

            } else {
                holder.binding.tvPackagenameorange.setVisibility(View.GONE);
                holder.binding.tvPackagenameorangeback.setVisibility(View.GONE);
            }


            switch (appointmentHealthCheckupList.get(position).getStatus()) {
                case "SCHEDULED": {
                    holder.binding.view.setBackgroundColor(context.getResources().getColor(R.color.statusschedule));
                    holder.binding.tvstatus.setTextColor(context.getResources().getColor(R.color.statusschedule));
                    holder.binding.tvstatus.getBackground().setColorFilter(context.getResources().getColor(R.color.statusschedulelight),
                            PorterDuff.Mode.SRC_ATOP);
                }
                break;
                case "CONFIRMED": {
                    holder.binding.view.setBackgroundColor(context.getResources().getColor(R.color.statusconfirm));
                    holder.binding.tvstatus.setTextColor(context.getResources().getColor(R.color.statusconfirm));
                    holder.binding.tvstatus.getBackground().setColorFilter(context.getResources().getColor(R.color.statusconfirmlight),
                            PorterDuff.Mode.SRC_ATOP);
                }
                break;
                case "APPOINTMENT DONE": {
                    holder.binding.tvstatus.setText("Completed".substring(0, 1).toUpperCase() + "Completed".substring(1).toLowerCase());
                    holder.binding.view.setBackgroundColor(context.getResources().getColor(R.color.statuscomplete));
                    holder.binding.tvstatus.setTextColor(context.getResources().getColor(R.color.statuscomplete));
                    holder.binding.tvstatus.getBackground().setColorFilter(context.getResources().getColor(R.color.statuscompletelight), PorterDuff.Mode.SRC_ATOP);
                }
                break;
                case "REJECTED": {
                    holder.binding.view.setBackgroundColor(context.getResources().getColor(R.color.statusreject));
                    holder.binding.tvstatus.setTextColor(context.getResources().getColor(R.color.statusreject));
                    holder.binding.tvstatus.getBackground().setColorFilter(context.getResources().getColor(R.color.statusrejectlight), PorterDuff.Mode.SRC_ATOP);
                }
                break;
                case "APPOINTMENT NOT DONE": {
                    holder.binding.tvstatus.setText("No Show".substring(0, 1).toUpperCase() + "No Show".substring(1).toLowerCase());
                    holder.binding.view.setBackgroundColor(context.getResources().getColor(R.color.statusnoshow));
                    holder.binding.tvstatus.setTextColor(context.getResources().getColor(R.color.statusnoshow));
                    holder.binding.tvstatus.getBackground().setColorFilter(context.getResources().getColor(R.color.statusnoshowlight), PorterDuff.Mode.SRC_ATOP);
                }
                break;
                default:
                    break;
            }


            String scheduledate = appointmentHealthCheckupList.get(position).getScheduleDates();

            if(appointmentHealthCheckupList.get(position).getStatus().equalsIgnoreCase("SCHEDULED")){
                holder.binding.tvscheduledon.setVisibility(View.VISIBLE);
                holder.binding.llscheduleapp.setVisibility(View.VISIBLE);
                holder.binding.tvschedates.setVisibility(View.VISIBLE);
                holder.binding.view1.setVisibility(View.VISIBLE);
                holder.binding.llreschedulecancel.setVisibility(View.VISIBLE);
                holder.binding.llappconfirmed.setVisibility(View.GONE);
                holder.binding.tvrejectreason.setVisibility(View.GONE);
                holder.binding.tvappointmentdoneon1.setVisibility(View.GONE);
                holder.binding.tvpaymentnotdone.setVisibility(View.GONE);

                holder.binding.tvappointmentdoneon.setText(Html.fromHtml(appointmentHealthCheckupList.get(position).getRemark(), Html.FROM_HTML_MODE_COMPACT));

                if (scheduledate.contains(",")) {
                    String myStr = scheduledate.replaceAll(",", "\n");
                    holder.binding.tvschedates.setText(Html.fromHtml(myStr, Html.FROM_HTML_MODE_COMPACT));
                } else holder.binding.tvschedates.setText(Html.fromHtml(scheduledate, Html.FROM_HTML_MODE_COMPACT));

                if (appointmentHealthCheckupList.get(position).getPaymentFlag() == 0 && !TextUtils.equals(appointmentHealthCheckupList.get(position).getSponserdBy().toLowerCase(), "company sponsored")) {
                    holder.binding.tvpaymentnotdone.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                        holder.binding.tvpaymentnotdone.setText(Html.fromHtml(appointmentHealthCheckupList.get(position).getOrderInfo(), Html.FROM_HTML_MODE_COMPACT));
                    else
                        holder.binding.tvpaymentnotdone.setText(Html.fromHtml(appointmentHealthCheckupList.get(position).getOrderInfo()));

                }//Unpaid-Schedule
                else {
                    if (!TextUtils.equals(appointmentHealthCheckupList.get(position).getSponserdBy().toLowerCase(), "company sponsored")) {
                        holder.binding.tvappointmentdoneon1.setVisibility(View.VISIBLE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            holder.binding.tvappointmentdoneon1.setText(Html.fromHtml(appointmentHealthCheckupList.get(position).getOrderInfo(), Html.FROM_HTML_MODE_COMPACT));
                        else
                            holder.binding.tvappointmentdoneon1.setText(Html.fromHtml(appointmentHealthCheckupList.get(position).getOrderInfo()));
                    }
                }//Paid Schedule
            } //schedule
            else if(appointmentHealthCheckupList.get(position).getStatus().equalsIgnoreCase("CONFIRMED")){

                holder.binding.tvapponintmentRefnonremarks.setVisibility(View.VISIBLE);
                holder.binding.tvordernonpaymentmadeon.setVisibility(View.VISIBLE);
                holder.binding.llscheduleapp.setVisibility(View.GONE);
                holder.binding.llappconfirmed.setVisibility(View.VISIBLE);
                holder.binding.tvschedates.setVisibility(View.VISIBLE);
                holder.binding.tvrejectreason.setVisibility(View.GONE);
                holder.binding.llreschedulecancel.setVisibility(View.VISIBLE);
                holder.binding.tvscheduledon.setVisibility(View.GONE);
                holder.binding.tvpaymentnotdone.setVisibility(View.GONE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    if (scheduledate.contains(",")) {
                        String myStr = scheduledate.replaceAll(",", "\n");
                        holder.binding.tvschedates.setText(Html.fromHtml(myStr, Html.FROM_HTML_MODE_COMPACT));
                    } else holder.binding.tvschedates.setText(Html.fromHtml(scheduledate, Html.FROM_HTML_MODE_COMPACT));

                else
                if (scheduledate.contains(",")) {
                    String myStr = scheduledate.replaceAll(",", "\n");
                    holder.binding.tvschedates.setText(Html.fromHtml(myStr, Html.FROM_HTML_MODE_COMPACT));
                } else holder.binding.tvschedates.setText(Html.fromHtml(scheduledate, Html.FROM_HTML_MODE_COMPACT));


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    holder.binding.tvapponintmentRefnonremarks.setText(Html.fromHtml(appointmentHealthCheckupList.get(position).getRemark(),
                            Html.FROM_HTML_MODE_COMPACT));
                else
                    holder.binding.tvapponintmentRefnonremarks.setText(Html.fromHtml(appointmentHealthCheckupList.get(position).getRemark()));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    holder.binding.tvappointmentdoneon.setText(Html.fromHtml("Appointment Date - " + scheduledate, Html.FROM_HTML_MODE_COMPACT));
                else
                    holder.binding.tvappointmentdoneon.setText(Html.fromHtml("Appointment Date - " + appointmentHealthCheckupList.get(position).getScheduleDates()));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    holder.binding.tvordernonpaymentmadeon.setText(Html.fromHtml(appointmentHealthCheckupList.get(position).getOrderInfo(), Html.FROM_HTML_MODE_COMPACT));
                else
                    holder.binding.tvordernonpaymentmadeon.setText(Html.fromHtml(appointmentHealthCheckupList.get(position).getOrderInfo()));

//                if (appointmentHealthCheckupList.get(position).getPaymentFlag() == 1) {
//                    if (appointmentHealthCheckupList.get(position).CanCancel()) {
//                        holder.binding.llreschedulecancel.setVisibility(View.VISIBLE);
//                        holder.binding.view1.setVisibility(View.VISIBLE);
//                    } else {
//                        holder.binding.llreschedulecancel.setVisibility(View.GONE);
//                        holder.binding.view1.setVisibility(View.GONE);
//                    }
//                }

            }//confirm
            else if(appointmentHealthCheckupList.get(position).getStatus().equalsIgnoreCase("APPOINTMENT DONE")){

                holder.binding.tvapponintmentRefnonremarks.setVisibility(View.VISIBLE);
                holder.binding.tvordernonpaymentmadeon.setVisibility(View.VISIBLE);
                holder.binding.llscheduleapp.setVisibility(View.GONE);
                holder.binding.llappconfirmed.setVisibility(View.VISIBLE);
                holder.binding.tvschedates.setVisibility(View.VISIBLE);
                holder.binding.tvscheduledon.setVisibility(View.GONE);
                holder.binding.tvpaymentnotdone.setVisibility(View.GONE);
                holder.binding.llreschedulecancel.setVisibility(View.GONE);
                holder.binding.tvrejectreason.setVisibility(View.GONE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    holder.binding.tvschedates.setText(Html.fromHtml(scheduledate, Html.FROM_HTML_MODE_COMPACT));
                else
                    holder.binding.tvschedates.setText(Html.fromHtml(scheduledate));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    holder.binding.tvapponintmentRefnonremarks.setText(Html.fromHtml(appointmentHealthCheckupList.get(position).getRemark(), Html.FROM_HTML_MODE_COMPACT));
                else
                    holder.binding.tvapponintmentRefnonremarks.setText(Html.fromHtml(appointmentHealthCheckupList.get(position).getRemark()));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    holder.binding.tvappointmentdoneon.setText(Html.fromHtml("Appointment Date - " + scheduledate, Html.FROM_HTML_MODE_COMPACT));
                else
                    holder.binding.tvappointmentdoneon.setText(Html.fromHtml("Appointment Date - " + appointmentHealthCheckupList.get(position).getScheduleDates()));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    holder.binding.tvordernonpaymentmadeon.setText(Html.fromHtml(appointmentHealthCheckupList.get(position).getOrderInfo(), Html.FROM_HTML_MODE_COMPACT));
                else
                    holder.binding.tvordernonpaymentmadeon.setText(Html.fromHtml(appointmentHealthCheckupList.get(position).getOrderInfo()));

            }//appointment done
            else if(appointmentHealthCheckupList.get(position).getStatus().equalsIgnoreCase("REJECTED")){

                holder.binding.tvappointmentdoneon1.setVisibility(View.VISIBLE);
                holder.binding.tvrejectreason.setVisibility(View.VISIBLE);
                holder.binding.tvschedates.setVisibility(View.VISIBLE);
                holder.binding.llscheduleapp.setVisibility(View.VISIBLE);
                holder.binding.view1.setVisibility(View.VISIBLE);
                holder.binding.tvscheduledon.setVisibility(View.GONE);
                holder.binding.llreschedulecancel.setVisibility(View.VISIBLE);
                holder.binding.tvpaymentnotdone.setVisibility(View.GONE);
                holder.binding.llappconfirmed.setVisibility(View.GONE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    holder.binding.tvrejectreason.setText(Html.fromHtml(appointmentHealthCheckupList.get(position).getRemark(), Html.FROM_HTML_MODE_COMPACT));
                else
                    holder.binding.tvrejectreason.setText(Html.fromHtml(appointmentHealthCheckupList.get(position).getRemark()));

                String text = "<!DOCTYPE html>\n" +
                        "<html><body><span style='text-decoration: line-through; color: #DA3939;'>" + scheduledate.trim() + "</span></body></html>";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    holder.binding.tvschedates.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
                else
                    holder.binding.tvschedates.setText(Html.fromHtml(text));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    holder.binding.tvappointmentdoneon1.setText(Html.fromHtml(appointmentHealthCheckupList.get(position).getOrderInfo(), Html.FROM_HTML_MODE_COMPACT));
                else
                    holder.binding.tvappointmentdoneon1.setText(Html.fromHtml(appointmentHealthCheckupList.get(position).getOrderInfo()));

            }//reject
            else if(appointmentHealthCheckupList.get(position).getStatus().equalsIgnoreCase("APPOINTMENT NOT DONE")){

                holder.binding.llreschedulecancel.setVisibility(View.VISIBLE);
                holder.binding.view1.setVisibility(View.VISIBLE);
                holder.binding.tvrejectreason.setVisibility(View.VISIBLE);
                holder.binding.tvschedates.setVisibility(View.VISIBLE);
                holder.binding.llscheduleapp.setVisibility(View.VISIBLE);
                holder.binding.llappconfirmed.setVisibility(View.GONE);
                holder.binding.tvpaymentnotdone.setVisibility(View.GONE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    holder.binding.tvrejectreason.setText(Html.fromHtml(appointmentHealthCheckupList.get(position).getRemark(), Html.FROM_HTML_MODE_COMPACT));
                else
                    holder.binding.tvrejectreason.setText(Html.fromHtml(appointmentHealthCheckupList.get(position).getRemark()));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    holder.binding.tvschedates.setText(Html.fromHtml(scheduledate, Html.FROM_HTML_MODE_COMPACT));
                else
                    holder.binding.tvschedates.setText(Html.fromHtml(scheduledate));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    holder.binding.tvappointmentdoneon1.setText(Html.fromHtml(appointmentHealthCheckupList.get(position).getOrderInfo(), Html.FROM_HTML_MODE_COMPACT));
                else
                    holder.binding.tvappointmentdoneon1.setText(Html.fromHtml(appointmentHealthCheckupList.get(position).getOrderInfo()));
            } //No show

            } catch (Exception e) {
            e.printStackTrace();
        }


        holder.binding.cancelLL.setOnClickListener(v -> {
            onCancellationHealthCheckup.cancelAppointmentHC(position);
        });

        holder.binding.rescheduleLL.setOnClickListener(view -> {

//            scheduleAppointment(Packages packages, Person person)
//            onRescheduleHealthCheckup.resscheduleAppointmentHC(appointmentHealthCheckupList.get(position));
        });

    }

    @Override
    public int getItemCount() {
        if (appointmentHealthCheckupList != null) {
            return appointmentHealthCheckupList.size();
        } else {
            return 0;
        }
    }


    public static class SummaryViewHolder extends RecyclerView.ViewHolder {
        ItemOngoingHealthcheckupBinding binding;

        public SummaryViewHolder(@NonNull ItemOngoingHealthcheckupBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }

}
