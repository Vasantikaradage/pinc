package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.PackageInfo;

import java.util.ArrayList;
import java.util.List;

public class PackageDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    List<PackageInfo> packageInfoList = new ArrayList<>();
    NavController navController;
    View view;

    public PackageDetailsAdapter(Context mContext, List<PackageInfo> packageInfoList) {
        this.packageInfoList = packageInfoList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_package_details, parent, false);

        return new PackageDetailsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        //  to navigate

//        Navigation.createNavigateOnClickListener(R.id.action_healthCheckupFragment_to_diagnosticFragment).onClick(holder.itemView);

        try {
            final PackageInfo lstPackageInfo = packageInfoList.get(position);

            ((PackageDetailsViewHolder) holder).testName.setText(lstPackageInfo.getName());
            ((PackageDetailsViewHolder) holder).testDetails.setText(lstPackageInfo.getDetail());

            ((PackageDetailsViewHolder) holder).arrowIV_minus.setVisibility(View.VISIBLE);

            ((PackageDetailsViewHolder) holder).arrowIV_minus.setOnClickListener(view -> {

                ((PackageDetailsViewHolder) holder).testDetails.setVisibility(View.GONE);
                ((PackageDetailsViewHolder) holder).arrowIV_plus.setVisibility(View.VISIBLE);
                ((PackageDetailsViewHolder) holder).arrowIV_minus.setVisibility(View.GONE);

            });

            ((PackageDetailsViewHolder) holder).arrowIV_plus.setOnClickListener(view -> {

                ((PackageDetailsViewHolder) holder).testDetails.setVisibility(View.VISIBLE);
                ((PackageDetailsViewHolder) holder).arrowIV_plus.setVisibility(View.GONE);
                ((PackageDetailsViewHolder) holder).arrowIV_minus.setVisibility(View.VISIBLE);

            });


//            ((PackageDetailsViewHolder) holder).scheduleIV.setOnClickListener(view -> {
//
////                navController.navigate(R.id.action_healthCheckupFragment_to_diagnosticFragment);
//                Navigation.createNavigateOnClickListener(R.id.action_healthCheckupFragment_to_diagnosticFragment).onClick(((PackagesViewHolder) holder).scheduleIV);
//
//            });

        } catch (Exception e) {
            Log.e(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "Error ", e);
        }

    }

    @Override
    public int getItemCount() {
        if (packageInfoList != null) {
            return packageInfoList.size();
        } else {
            return 0;
        }
    }

    public static class PackageDetailsViewHolder extends RecyclerView.ViewHolder {

         TextView testName, testDetails;
         ImageView arrowIV_plus,arrowIV_minus;
         View v1;

        public PackageDetailsViewHolder(@NonNull View itemView) {
            super(itemView);

            testName = itemView.findViewById(R.id.tvtest);
            testDetails = itemView.findViewById(R.id.tvtestdetails);
            arrowIV_plus = itemView.findViewById(R.id.iv_arrow);
            arrowIV_minus = itemView.findViewById(R.id.iv_arrow_minus);

            v1 = itemView.findViewById(R.id.v1);

        }
    }
}
