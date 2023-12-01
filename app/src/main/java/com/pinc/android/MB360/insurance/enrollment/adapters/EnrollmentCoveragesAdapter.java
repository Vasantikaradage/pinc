package com.pinc.android.MB360.insurance.enrollment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.ItemEnrollmentMyCoverageBinding;
import com.pinc.android.MB360.insurance.enrollment.interfaces.ToolTipListener;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.Coverage;


import java.util.List;

public class EnrollmentCoveragesAdapter extends RecyclerView.Adapter<EnrollmentCoveragesAdapter.EnrollmentCoveragesViewHolder> {

    Context context;
    ToolTipListener toolTipListener;
    List<Coverage> coveragesList;
    String tooltipText = "";
    String atleastOneAlpha = ".*[a-zA-Z]+.*";


    public EnrollmentCoveragesAdapter(Context context, List<Coverage> coveragesList, ToolTipListener toolTipListener) {
        this.context = context;
        this.coveragesList = coveragesList;
        this.toolTipListener = toolTipListener;
    }

    @NonNull
    @Override
    public EnrollmentCoveragesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEnrollmentMyCoverageBinding binding = ItemEnrollmentMyCoverageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EnrollmentCoveragesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EnrollmentCoveragesViewHolder holder, int position) {
        Coverage coverage = coveragesList.get(position);

        //coverage title
        switch (coverage.getPolicyType()) {
            case "1":
                holder.binding.myCoverageTitle.setText(context.getString(R.string.ghi));

                //how to show sum insured
                if (coverage.getHowToShowSumInsured().equalsIgnoreCase("0")) {
                    holder.binding.sumInsured.setVisibility(View.VISIBLE);
                    holder.binding.sumInsured.setText(String.format("%s", coverage.getSumInsured()));
                } else {
                    holder.binding.sumInsured.setVisibility(View.VISIBLE);
                    holder.binding.sumInsured.setText(String.format("%s %s", context.getString(R.string.rs), coverage.getSumInsured()));
                    holder.binding.sumInsuredType.setText(String.format("(%s)", coverage.getSumInsureType()));
                }

                //to show premium
                if (coverage.getToShowPremium().equalsIgnoreCase("0")) {
                    holder.binding.premiumAmountTitle.setVisibility(View.GONE);
                    holder.binding.premiumAmount.setVisibility(View.GONE);
                    holder.binding.premiumAmountType.setVisibility(View.GONE);
                    holder.binding.premiumLayout.setVisibility(View.GONE);
                    holder.binding.coverageTooltipImage.setVisibility(View.GONE);

                } else {
                    holder.binding.premiumAmount.setVisibility(View.VISIBLE);
                    holder.binding.premiumAmount.setText(String.format("%s %s", context.getString(R.string.rs), coverage.getPremium()));
                    holder.binding.premiumAmountType.setText(String.format("(%s)", coverage.getPremiumType()));
                    holder.binding.premiumAmountTitle.setVisibility(View.VISIBLE);
                    holder.binding.premiumAmount.setVisibility(View.VISIBLE);
                    holder.binding.premiumAmountType.setVisibility(View.VISIBLE);
                    holder.binding.premiumLayout.setVisibility(View.VISIBLE);
                    holder.binding.coverageTooltipImage.setVisibility(View.VISIBLE);

                }

                break;
            case "2":
                holder.binding.myCoverageTitle.setText(context.getString(R.string.gpa));
                holder.binding.sumInsuredType.setVisibility(View.GONE);

                //how to show sum insured
                if (coverage.getHowToShowSumInsured().equalsIgnoreCase("0")) {
                    holder.binding.sumInsured.setVisibility(View.VISIBLE);
                    holder.binding.sumInsured.setText(String.format("%s", coverage.getSumInsured()));

                } else {
                    holder.binding.sumInsured.setVisibility(View.VISIBLE);
                    holder.binding.sumInsured.setText(String.format("%s %s", context.getString(R.string.rs), coverage.getSumInsured()));
                    holder.binding.sumInsuredType.setText(String.format("(%s)", coverage.getSumInsureType()));
                }

                //to show premium
                if (coverage.getToShowPremium().equalsIgnoreCase("0")) {
                    holder.binding.premiumAmountTitle.setVisibility(View.GONE);
                    holder.binding.premiumAmount.setVisibility(View.GONE);
                    holder.binding.premiumLayout.setVisibility(View.GONE);
                    holder.binding.coverageTooltipImage.setVisibility(View.GONE);
                } else {
                    holder.binding.premiumAmount.setVisibility(View.VISIBLE);
                    holder.binding.premiumAmount.setText(String.format("%s %s", context.getString(R.string.rs), coverage.getPremium()));

                    holder.binding.premiumAmountTitle.setVisibility(View.VISIBLE);
                    holder.binding.premiumAmount.setVisibility(View.VISIBLE);
                    holder.binding.premiumLayout.setVisibility(View.VISIBLE);
                    holder.binding.coverageTooltipImage.setVisibility(View.VISIBLE);

                }
                holder.binding.premiumAmountType.setVisibility(View.GONE);
                break;
            case "3":
                holder.binding.myCoverageTitle.setText(context.getString(R.string.gtl));
                holder.binding.sumInsuredType.setVisibility(View.GONE);

                //how to show sum insured
                if (coverage.getHowToShowSumInsured().equalsIgnoreCase("0")) {
                    holder.binding.sumInsured.setVisibility(View.VISIBLE);
                    holder.binding.sumInsured.setText(String.format("%s", coverage.getSumInsured()));
                } else {
                    holder.binding.sumInsured.setVisibility(View.VISIBLE);
                    holder.binding.sumInsured.setText(String.format("%s %s", context.getString(R.string.rs), coverage.getSumInsured()));
                    holder.binding.sumInsuredType.setText(String.format("(%s)", coverage.getSumInsureType()));
                }

                //to show premium
                if (coverage.getToShowPremium().equalsIgnoreCase("0")) {
                    holder.binding.premiumAmountTitle.setVisibility(View.GONE);
                    holder.binding.premiumAmount.setVisibility(View.GONE);
                    holder.binding.premiumLayout.setVisibility(View.GONE);
                    holder.binding.coverageTooltipImage.setVisibility(View.GONE);
                } else {
                    holder.binding.premiumAmount.setVisibility(View.VISIBLE);
                    holder.binding.premiumAmount.setText(String.format("%s %s", context.getString(R.string.rs), coverage.getPremium()));

                    holder.binding.premiumAmountTitle.setVisibility(View.VISIBLE);
                    holder.binding.premiumAmount.setVisibility(View.VISIBLE);
                    holder.binding.premiumLayout.setVisibility(View.VISIBLE);
                    holder.binding.coverageTooltipImage.setVisibility(View.VISIBLE);

                }
                holder.binding.premiumAmountType.setVisibility(View.GONE);

                break;
        }

        //tool tip
        if (coverage.getToShowEmployeeContribution().equalsIgnoreCase("0") && coverage.getEmployerContribution().equalsIgnoreCase("0")) {
            //hide tool tip
            holder.binding.coverageTooltipImage.setVisibility(View.GONE);
            tooltipText = "";

        } else if (coverage.getToShowEmployeeContribution().equalsIgnoreCase("0") &&
                coverage.getToShowEmployerContribution().equalsIgnoreCase("1")) {

            holder.binding.coverageTooltipImage.setOnClickListener(v -> {
                tooltipText = "  Premium Contribution" + "\n• Employer Contribution: " + coverage.getEmployerContribution();
                toolTipListener.OnToolTipListener(tooltipText, holder.binding.coverageTooltipImage, holder.binding.coverageItem);
            });

            //to show the Employee Contribution
        } else if (coverage.getToShowEmployeeContribution().equalsIgnoreCase("1") &&
                coverage.getToShowEmployerContribution().equalsIgnoreCase("0")) {

            holder.binding.coverageTooltipImage.setOnClickListener(v -> {
                tooltipText = "  Premium Contribution" + "\n• Employee Contribution: " + coverage.getEmployeeContribution();
                toolTipListener.OnToolTipListener(tooltipText, holder.binding.coverageTooltipImage, holder.binding.coverageItem);
            });

        } else if (coverage.getToShowEmployeeContribution().equalsIgnoreCase("1") &&
                coverage.getToShowEmployeeContribution().equalsIgnoreCase("1")) {


            holder.binding.coverageTooltipImage.setOnClickListener(v -> {
                tooltipText = "  Premium Contribution" + "\n• Employee Contribution: " + coverage.getEmployeeContribution() +
                        "\n• Employer Contribution: " + coverage.getEmployerContribution();
                toolTipListener.OnToolTipListener(tooltipText, holder.binding.coverageTooltipImage, holder.binding.coverageItem);
            });
        } else {
            holder.binding.coverageTooltipImage.setVisibility(View.GONE);
        }


    }


    @Override
    public int getItemCount() {
        return (coveragesList != null ? coveragesList.size() : 0);
    }


    public static class EnrollmentCoveragesViewHolder extends RecyclerView.ViewHolder {
        ItemEnrollmentMyCoverageBinding binding;

        public EnrollmentCoveragesViewHolder(@NonNull ItemEnrollmentMyCoverageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
