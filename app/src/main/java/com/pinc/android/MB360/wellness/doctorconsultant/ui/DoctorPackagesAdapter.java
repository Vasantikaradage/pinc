package com.pinc.android.MB360.wellness.doctorconsultant.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.ItemPackageDetailsBinding;
import com.pinc.android.MB360.databinding.PackagePlanLayoutBinding;
import com.pinc.android.MB360.utilities.UtilMethods;
import com.pinc.android.MB360.wellness.doctorconsultant.responseclass.Detail;
import com.pinc.android.MB360.wellness.doctorconsultant.responseclass.DoctorPackages;

import java.util.ArrayList;
import java.util.List;

public class DoctorPackagesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<DoctorPackages> doctorPackagesList;
    Context context;
    OnPackageBuyListener onPackageBuyListener;


    public DoctorPackagesAdapter(List<DoctorPackages> doctorPackagesList, Context context, OnPackageBuyListener onPackageBuyListener) {
        this.doctorPackagesList = doctorPackagesList;
        this.context = context;
        this.onPackageBuyListener = onPackageBuyListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PackagePlanLayoutBinding binding = PackagePlanLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DoctorPackagesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DoctorPackages packages = doctorPackagesList.get(position);
        List<String> detailsList = new ArrayList<>();
        detailsList.add(packages.getDetails().get(0).getCalls() + " calls per user.");
        for (Detail packagesDetails : packages.getDetails()
        ) {

            detailsList.add(packagesDetails.getCatogary());
        }

        detailsList.add("Covers 1 user " + packages.getDetails().get(0).getDependent() + " dependants.");

        ((DoctorPackagesViewHolder) holder).binding.planName.setText(packages.getPkgName());
        ((DoctorPackagesViewHolder) holder).binding.validity.setText(packages.getDetails().get(0).getValidityMonth());

        if (packages.getDetails().get(0).getIncludeGst().equals("1")) {
            ((DoctorPackagesViewHolder) holder).binding.gst.setVisibility(View.VISIBLE);
        } else {
            ((DoctorPackagesViewHolder) holder).binding.gst.setVisibility(View.GONE);
        }
        ((DoctorPackagesViewHolder) holder).binding.validity.setText(context.getString(R.string.rs) + "" + UtilMethods.PriceFormat(packages.getDetails().get(0).getDcPkgPrice()));


        ((DoctorPackagesViewHolder) holder).binding.childRecyclerView.setAdapter(new DoctorDetailAdapter(detailsList, context, packages.getPackagePlanSrNo()));

        switch (packages.getPackagePlanSrNo()) {
            case "1":
                ((DoctorPackagesViewHolder) holder).binding.imageView5.setBackgroundResource(R.drawable.doctor_pack_one);
                ((DoctorPackagesViewHolder) holder).binding.buyPackage.setBackgroundResource(R.drawable.bronzeplan);

                break;
            case "2":
                ((DoctorPackagesViewHolder) holder).binding.imageView5.setBackgroundResource(R.drawable.doctor_pack2);
                ((DoctorPackagesViewHolder) holder).binding.buyPackage.setBackgroundResource(R.drawable.silverplan);
                break;
            case "3":
                ((DoctorPackagesViewHolder) holder).binding.imageView5.setBackgroundResource(R.drawable.doctor_package_banner_3);
                ((DoctorPackagesViewHolder) holder).binding.buyPackage.setBackgroundResource(R.drawable.goldplan);
                break;
            case "4":
                ((DoctorPackagesViewHolder) holder).binding.imageView5.setBackgroundResource(R.drawable.doctor_banner4);
                ((DoctorPackagesViewHolder) holder).binding.buyPackage.setBackgroundResource(R.drawable.plantinumplan);
                break;
            case "5":
                ((DoctorPackagesViewHolder) holder).binding.imageView5.setBackgroundResource(R.drawable.doctor_banner5);
                ((DoctorPackagesViewHolder) holder).binding.buyPackage.setBackgroundResource(R.drawable.titaniumplan);
                break;

            default:
                ((DoctorPackagesViewHolder) holder).binding.imageView5.setBackgroundResource(R.drawable.bronzeplan);
                ((DoctorPackagesViewHolder) holder).binding.buyPackage.setBackgroundResource(R.drawable.bronzeplan);
                break;
        }


        ((DoctorPackagesViewHolder) holder).binding.buyPackage.setOnClickListener(v -> {
            onPackageBuyListener.onPackageBuyListener(packages);
        });

    }

    @Override
    public int getItemCount() {
        if (doctorPackagesList != null) {
            return doctorPackagesList.size();
        } else {
            return 0;
        }
    }


    public static class DoctorPackagesViewHolder extends RecyclerView.ViewHolder {

        PackagePlanLayoutBinding binding;

        public DoctorPackagesViewHolder(@NonNull PackagePlanLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
