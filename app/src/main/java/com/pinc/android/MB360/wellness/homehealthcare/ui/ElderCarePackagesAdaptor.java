package com.pinc.android.MB360.wellness.homehealthcare.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.ItemElderCareBinding;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.utilities.UtilMethods;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.ElderCell;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.PackageEC;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class ElderCarePackagesAdaptor extends RecyclerView.Adapter<ElderCarePackagesAdaptor.ElderCarePackageViewHolder> {


    List<PackageEC> packageECList;
    Context context;
    OnElderCarePackagerListener onElderCarePackagerListener;
    ElderCareFeatureCellAdaptor adaptor;

    public ElderCarePackagesAdaptor(List<PackageEC> packageECList, Context context, OnElderCarePackagerListener onElderCarePackagerListener) {
        this.packageECList = packageECList;
        this.context = context;
        this.onElderCarePackagerListener = onElderCarePackagerListener;
    }

    @NonNull
    @Override
    public ElderCarePackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemElderCareBinding binding = ItemElderCareBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ElderCarePackageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ElderCarePackageViewHolder holder, int position) {

        holder.binding.txtPackage1.setText(packageECList.get(position).getCategory());
        holder.binding.txtPrice.setText(MessageFormat.format("{0} {1}", context.getString(R.string.rs), UtilMethods.PriceFormat(packageECList.get(position).getPkgPriceMb())));

        Log.d(LogTags.ELDER_CARE, "onBindViewHolder: " + packageECList.get(position).getCategory());

        if (packageECList.get(position).getCategory().equals("HEALTH PRIME PLAN (ANNUAL)")) {
            List<ElderCell> elderCellList = new ArrayList<>();
            //adding the feature items here
            elderCellList.add(new ElderCell("COMPREHENSIVE GERIATRIC ASSESSMENT@ HOME", 0));
            elderCellList.add(new ElderCell("COMPREHENSIVE HEALTH CHECK (71+PARAMETERS)", 0));
            elderCellList.add(new ElderCell("DEDICATED HEALTH MANAGER 24X7 ON CALL", 0));
            elderCellList.add(new ElderCell("DOCTOR VISITS", 4));
            elderCellList.add(new ElderCell("HEALTH MANAGER VISITS", 6));
            elderCellList.add(new ElderCell("PHYSIOTHERAPY VISITS", 4));
            elderCellList.add(new ElderCell("NUTRITION TELE CONSULTATION", 1));
            elderCellList.add(new ElderCell("UNLIMITED DOCTOR TELE CONSULTATION", 0));
            elderCellList.add(new ElderCell("GLUCOMETER WITH 20 STRIPS", 0));
            elderCellList.add(new ElderCell("HOSPITAL INFORMATION AND ASSISTANCE", 0));
            elderCellList.add(new ElderCell("SOCIAL ENGAGEMENT", 0));

            //setting up the recyclerView
            adaptor = new ElderCareFeatureCellAdaptor(elderCellList);
            holder.binding.rvFeatureItems.setAdapter(adaptor);
            holder.binding.rvFeatureItems.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        } else {
            List<ElderCell> elderCellList = new ArrayList<>();
            //adding the feature items here
            elderCellList.add(new ElderCell("COMPREHENSIVE GERIATRIC ASSESSMENT@ HOME", 0));
            elderCellList.add(new ElderCell("COMPREHENSIVE HEALTH CHECK (71+PARAMETERS)", 0));
            elderCellList.add(new ElderCell("DEDICATED HEALTH MANAGER 24X7 ON CALL", 0));
            elderCellList.add(new ElderCell("DOCTOR VISITS", 12));
            elderCellList.add(new ElderCell("HEALTH MANAGER VISITS", 12));
            elderCellList.add(new ElderCell("PHYSIOTHERAPY VISITS", 0));
            elderCellList.add(new ElderCell("NUTRITION TELE CONSULTATION", 1));
            elderCellList.add(new ElderCell("UNLIMITED DOCTOR TELE CONSULTATION", 0));
            elderCellList.add(new ElderCell("GLUCOMETER WITH 20 STRIPS", 0));
            elderCellList.add(new ElderCell("HOSPITAL INFORMATION AND ASSISTANCE", 0));
            elderCellList.add(new ElderCell("SOCIAL ENGAGEMENT", 0));

            //setting up the recyclerView
            adaptor = new ElderCareFeatureCellAdaptor(elderCellList);
            holder.binding.rvFeatureItems.setAdapter(adaptor);
            holder.binding.rvFeatureItems.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        }

        holder.binding.btnSelect.setOnClickListener(v -> {
            onElderCarePackagerListener.onElderPackageSelected(packageECList.get(position));
        });
        holder.binding.dropDown.setRotation(180f);
        holder.binding.dropDown.setOnClickListener(v -> {
            if (holder.binding.rvFeatureItems.getVisibility() == View.VISIBLE) {
                holder.binding.rvFeatureItems.setVisibility(View.GONE);
                holder.binding.dropDown.setRotation(0f);

            } else {
                holder.binding.rvFeatureItems.setVisibility(View.VISIBLE);
                holder.binding.dropDown.setRotation(180f);

            }

        });

    }

    @Override
    public int getItemCount() {
        if (packageECList != null) {
            return packageECList.size();
        } else {
            return 0;
        }
    }

    public static class ElderCarePackageViewHolder extends RecyclerView.ViewHolder {
        ItemElderCareBinding binding;

        public ElderCarePackageViewHolder(@NonNull ItemElderCareBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
