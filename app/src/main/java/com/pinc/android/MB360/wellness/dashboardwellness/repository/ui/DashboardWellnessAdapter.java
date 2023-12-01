package com.pinc.android.MB360.wellness.dashboardwellness.repository.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.responseclass.ServiceInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DashboardWellnessAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<ServiceInfo> serviceInfoList = new ArrayList<>();
    WellnessMenuClickListener onWellnessMenuClicked;


    public DashboardWellnessAdapter(Context mContext, List<ServiceInfo> serviceInfoList, WellnessMenuClickListener onWellnessMenuClicked) {
        this.serviceInfoList = serviceInfoList;
        this.mContext = mContext;
        this.onWellnessMenuClicked = onWellnessMenuClicked;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_wellness_dashboard, parent, false);

        return new DashboardWellnessViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        try {
            final ServiceInfo serviceName = serviceInfoList.get(position);
            int level = position + 1;
            String DashboardWellness = serviceName.getName();


            StringBuffer capBuffer = new StringBuffer();
            Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)",
                    Pattern.CASE_INSENSITIVE).matcher(serviceName.getName());

            while (capMatcher.find()) {
                capMatcher.appendReplacement(capBuffer,
                        capMatcher.group(1).toUpperCase() +
                                capMatcher.group(2).toLowerCase());
            }

            ((DashboardWellnessViewHolder) holder).tvSeviceName.setText(capMatcher.appendTail(capBuffer).toString());
            ((DashboardWellnessViewHolder) holder).rlservice.setOnClickListener(view -> onWellnessMenuClicked.onClickWellnessMenu(DashboardWellness));


            switch (serviceName.getName().toUpperCase()) {
                case "COVID 19 TEST":
                    ((DashboardWellnessViewHolder) holder).iv_service_icon.setImageResource(R.drawable.ic_coronavirus);
                    ((DashboardWellnessViewHolder) holder).info.setText("Available in 11 Cities");
                    break;

                case "HEALTH CHECKUP":
                    ((DashboardWellnessViewHolder) holder).iv_service_icon.setImageResource(R.drawable.ic_health_check);
                    ((DashboardWellnessViewHolder) holder).info.setText("Available in 192 Cities");
                    break;

                case "MEDICINE DELIVERY":
                    //       holder.rlservice.setVisibility(View.GONE);
                    ((DashboardWellnessViewHolder) holder).info.setText("Available in 11 Cities");
                    ((DashboardWellnessViewHolder) holder).iv_service_icon.setImageResource(R.drawable.ic_medicine_delivery);

                    break;

                case "DOCTOR CONSULTATION":
                    ((DashboardWellnessViewHolder) holder).info.setText("Available in 11 Cities");
                    ((DashboardWellnessViewHolder) holder).iv_service_icon.setImageResource(R.drawable.ic_doctos_consuktation);
                    break;
                case "DOCTOR SERVICES":
                    ((DashboardWellnessViewHolder) holder).info.setText("Available in 11 Cities");
                    ((DashboardWellnessViewHolder) holder).iv_service_icon.setImageResource(R.drawable.ic_doctor_visit);

                    break;

                case "DENTAL":
                    //     holder.rlservice.setVisibility(View.GONE);
                    ((DashboardWellnessViewHolder) holder).iv_service_icon.setImageResource(R.drawable.ic_dental);
                    ((DashboardWellnessViewHolder) holder).info.setText("Available in 11 Cities");


                    break;

                case "ONLINE COUNSELLOR":
                    ((DashboardWellnessViewHolder) holder).info.setText("Available in 11 Cities");
                    ((DashboardWellnessViewHolder) holder).iv_service_icon.setImageResource(R.drawable.ic_onlinecounsellor);

                    break;

                case "POST NATAL CARE":
                    ((DashboardWellnessViewHolder) holder).info.setText("Available in 11 Cities");
                    ((DashboardWellnessViewHolder) holder).iv_service_icon.setImageResource(R.drawable.ic_post_natal);
                    break;

                case "TRAINED ATTENDANT":
                    ((DashboardWellnessViewHolder) holder).info.setText("Available in 11 Cities");
                    ((DashboardWellnessViewHolder) holder).iv_service_icon.setImageResource(R.drawable.ic_trained_attendant);
                    break;

                case "PHYSIOTHERAPY":
                    ((DashboardWellnessViewHolder) holder).info.setText("Available in 11 Cities");
                    ((DashboardWellnessViewHolder) holder).iv_service_icon.setImageResource(R.drawable.ic_physiotheraphy);

                    break;

                case "ELDER CARE":
                    ((DashboardWellnessViewHolder) holder).info.setText("Available in 11 Cities");
                    ((DashboardWellnessViewHolder) holder).iv_service_icon.setImageResource(R.drawable.ic_elder_care);

                    break;

                case "DIABETES MANAGEMENT":
                    ((DashboardWellnessViewHolder) holder).info.setText("Available in 11 Cities");
                    ((DashboardWellnessViewHolder) holder).iv_service_icon.setImageResource(R.drawable.ic_diabetes_care);

                    break;

                case "SHORT TERM NURSING":
                    ((DashboardWellnessViewHolder) holder).info.setText("Available in 11 Cities");
                    ((DashboardWellnessViewHolder) holder).iv_service_icon.setImageResource(R.drawable.ic_short_term_nursing);

                    break;

                case "LONG TERM NURSING":
                    ((DashboardWellnessViewHolder) holder).info.setText("Available in 11 Cities");
                    ((DashboardWellnessViewHolder) holder).iv_service_icon.setImageResource(R.drawable.ic_long_term_nursing);

                    break;

                default:
                    ((DashboardWellnessViewHolder) holder).iv_service_icon.setImageResource(R.drawable.ic_homehealthcare);

                    break;

            }


        } catch (Exception e) {
            Log.e(LogTags.WELLNESS_DASHBOARD_ACTIVITY, "Error ", e);
        }

    }

    @Override
    public int getItemCount() {
        if (serviceInfoList != null) {
            return serviceInfoList.size();
        } else {
            return 0;
        }
    }

    public static class DashboardWellnessViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView tvSeviceName, info;
        ImageView iv_service_icon;
        RelativeLayout rlservice;

        public DashboardWellnessViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_service_icon = itemView.findViewById(R.id.iv_service_icon);
            tvSeviceName = itemView.findViewById(R.id.tv_service_name);
            rlservice = itemView.findViewById(R.id.rlservice);
            info = itemView.findViewById(R.id.tv_service_info);

        }
    }
}
