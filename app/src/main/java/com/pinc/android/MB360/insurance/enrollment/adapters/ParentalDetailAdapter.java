package com.pinc.android.MB360.insurance.enrollment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.ItemEnrollmentDependantDetailsBinding;
import com.pinc.android.MB360.databinding.ItemParentalHeaderBinding;
import com.pinc.android.MB360.insurance.enrollment.interfaces.DependantHelper;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.ParentalModel;
import com.pinc.android.MB360.utilities.UtilMethods;

import java.util.List;

public class ParentalDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ParentalModel> parentList;
    Context context;
    DependantHelper dependantHelper;


    public ParentalDetailAdapter(List<ParentalModel> parentList, Context context, DependantHelper dependantHelper) {
        this.parentList = parentList;
        this.context = context;
        this.dependantHelper = dependantHelper;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            ItemEnrollmentDependantDetailsBinding dependantDetailsBinding = ItemEnrollmentDependantDetailsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ParentalDetailViewHolder(dependantDetailsBinding);

        } else {
            ItemParentalHeaderBinding parentalHeaderBinding = ItemParentalHeaderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new ParentalDetailHeaderViewHolder(parentalHeaderBinding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder.getItemViewType() == 0) {
            //header
            if (holder.getAdapterPosition() == 3) {
                ((ParentalDetailHeaderViewHolder) holder).binding.lblHeader.setText("Second Set of Parents - Sum Insured - Rs. 3,00,000/-");
            }

        } else {
            ParentalModel parental = parentList.get(position);
            if (parental.getIsAdded()) {
                //this is when parent added.

                ((ParentalDetailViewHolder) holder).binding.addNewIcon.setVisibility(View.GONE);
                ((ParentalDetailViewHolder) holder).binding.etAge.setText(String.format("%s Years", parental.getAge()));

                //edit image
                ((ParentalDetailViewHolder) holder).binding.rlSave.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pencil_svg));


                switch (parental.getRelationName().toLowerCase()) {

                    case "father":
                        ((ParentalDetailViewHolder) holder).binding.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.father));
                        ((ParentalDetailViewHolder) holder).binding.tvType.setText(UtilMethods.capitalizeFirstWord("father"));
                        break;
                    case "father-in-law":
                        ((ParentalDetailViewHolder) holder).binding.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.father));
                        ((ParentalDetailViewHolder) holder).binding.tvType.setText(UtilMethods.capitalizeFirstWord("father-in-law"));
                        break;
                    case "mother":
                        ((ParentalDetailViewHolder) holder).binding.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.mother));
                        ((ParentalDetailViewHolder) holder).binding.tvType.setText(UtilMethods.capitalizeFirstWord("mother"));
                        break;
                    case "mother-in-law":
                        ((ParentalDetailViewHolder) holder).binding.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.mother));
                        ((ParentalDetailViewHolder) holder).binding.tvType.setText(UtilMethods.capitalizeFirstWord("mother-in-law"));
                        break;
                }

                ((ParentalDetailViewHolder) holder).binding.relIcon.setVisibility(View.VISIBLE);
                ((ParentalDetailViewHolder) holder).binding.tvType.setVisibility(View.VISIBLE);

                //disable the sample relation
                ((ParentalDetailViewHolder) holder).binding.lblSampleRelation.setVisibility(View.GONE);

                //is differently abled
                if (parental.isDifferentlyAble()) {
                    ((ParentalDetailViewHolder) holder).binding.ivdiff.setVisibility(View.VISIBLE);
                    ((ParentalDetailViewHolder) holder).binding.ivdiffdoc.setVisibility(View.VISIBLE);

                    ((ParentalDetailViewHolder) holder).binding.ivdiffdoc.setOnClickListener(v -> {
                        Toast.makeText(context, "TODO: file show dialog", Toast.LENGTH_SHORT).show();
                    });

                } else {
                    ((ParentalDetailViewHolder) holder).binding.ivdiff.setVisibility(View.GONE);
                    ((ParentalDetailViewHolder) holder).binding.ivdiffdoc.setVisibility(View.GONE);
                }

                //make name visible
                ((ParentalDetailViewHolder) holder).binding.etName.setVisibility(View.VISIBLE);
                ((ParentalDetailViewHolder) holder).binding.etName.setText(parental.getName());

                //make age visible
                ((ParentalDetailViewHolder) holder).binding.etAge.setVisibility(View.VISIBLE);
                ((ParentalDetailViewHolder) holder).binding.etAge.setText("(" + parental.getAge() + " Years)");
                //make dob visible
                ((ParentalDetailViewHolder) holder).binding.etDOB.setVisibility(View.VISIBLE);
                ((ParentalDetailViewHolder) holder).binding.etDOB.setText(parental.getDateOfBirth());

                //edit
                ((ParentalDetailViewHolder) holder).binding.rlSave.setOnClickListener(v -> {
                    // TODO if editable
                    dependantHelper.onEditDependant(parental, position);
                });

                //TODO show premium

            } else {
                //this is when parent is not yet added.
                ((ParentalDetailViewHolder) holder).binding.ivtwins.setVisibility(View.GONE);
                ((ParentalDetailViewHolder) holder).binding.relIcon.setVisibility(View.GONE);
                ((ParentalDetailViewHolder) holder).binding.ivdiff.setVisibility(View.GONE);
                ((ParentalDetailViewHolder) holder).binding.ivdiffdoc.setVisibility(View.GONE);
                ((ParentalDetailViewHolder) holder).binding.etDOB.setText("");
                ((ParentalDetailViewHolder) holder).binding.etAge.setText("");
                ((ParentalDetailViewHolder) holder).binding.tvType.setText("");

                //edit image
                ((ParentalDetailViewHolder) holder).binding.rlSave.setImageDrawable(null);

                ((ParentalDetailViewHolder) holder).binding.etName.setText(parental.getRelationName());

                ((ParentalDetailViewHolder) holder).binding.completeView.setOnClickListener(v -> {
                    dependantHelper.onAddDependant(parental, position);
                });

            }


        }

    }

    @Override
    public int getItemCount() {
        return (parentList != null ? parentList.size() : 0);
    }

    @Override
    public int getItemViewType(int position) {
        return parentList.get(position).getHeader() ? 0 : 1;
    }

    public static class ParentalDetailViewHolder extends RecyclerView.ViewHolder {

        public ItemEnrollmentDependantDetailsBinding binding;

        public ParentalDetailViewHolder(@NonNull ItemEnrollmentDependantDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class ParentalDetailHeaderViewHolder extends RecyclerView.ViewHolder {
        public ItemParentalHeaderBinding binding;

        public ParentalDetailHeaderViewHolder(@NonNull ItemParentalHeaderBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
