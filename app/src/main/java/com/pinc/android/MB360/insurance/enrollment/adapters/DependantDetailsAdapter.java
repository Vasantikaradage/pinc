package com.pinc.android.MB360.insurance.enrollment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aminography.primecalendar.PrimeCalendar;
import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.ItemEnrollmentDependantDetailsBinding;
import com.pinc.android.MB360.insurance.enrollment.interfaces.DependantHelper;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.DependantHelperModel;
import com.pinc.android.MB360.utilities.UtilMethods;

import java.util.Calendar;
import java.util.List;

public class DependantDetailsAdapter extends RecyclerView.Adapter<DependantDetailsAdapter.DependantDetailsViewHolder> {


    List<DependantHelperModel> dependantHelperModelList;
    Context context;
    DependantHelper dependantHelper;


    public DependantDetailsAdapter(List<DependantHelperModel> relationList, Context context, DependantHelper dependantHelper) {
        this.dependantHelperModelList = relationList;
        this.context = context;
        this.dependantHelper = dependantHelper;
    }

    @NonNull
    @Override
    public DependantDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemEnrollmentDependantDetailsBinding binding = ItemEnrollmentDependantDetailsBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new DependantDetailsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull DependantDetailsViewHolder holder, int position) {
        //check for dependant to be added or  to be edited
        //slide to delete is always available
        //we need to fill the empty cards -> then only we can delete.
        DependantHelperModel dependantHelperModel = dependantHelperModelList.get(position);

        holder.binding.etName.setText(dependantHelperModel.getRelationName());


        //edit image
        holder.binding.rlSave.setImageDrawable(null);
        if (dependantHelperModel.getIsAdded()) {
            //disable the add button
            holder.binding.addNewIcon.setVisibility(View.GONE);
            holder.binding.etAge.setText(String.format("%s Years", dependantHelperModel.getAge()));

            //edit image
            holder.binding.rlSave.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pencil_svg));

            //setting the relation icon
            switch (dependantHelperModel.getRelationName().toLowerCase()) {
                case "father":
                    holder.binding.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.father));
                    holder.binding.tvType.setText(UtilMethods.capitalizeFirstWord("father"));
                    break;
                case "father-in-law":
                    holder.binding.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.father));
                    holder.binding.tvType.setText(UtilMethods.capitalizeFirstWord("father-in-law"));
                    break;
                case "mother":
                    holder.binding.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.mother));
                    holder.binding.tvType.setText(UtilMethods.capitalizeFirstWord("mother-in-law"));
                    break;
                case "mother-in-law":
                    holder.binding.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.mother));
                    holder.binding.tvType.setText(UtilMethods.capitalizeFirstWord("mother"));
                    break;
                case "wife":
                    holder.binding.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.wife));
                    holder.binding.tvType.setText(UtilMethods.capitalizeFirstWord("wife"));
                    break;
                case "daughter":
                    holder.binding.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.daughter));
                    holder.binding.tvType.setText(UtilMethods.capitalizeFirstWord("daughter"));
                    break;
                case "son":
                    holder.binding.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.son));
                    holder.binding.tvType.setText(UtilMethods.capitalizeFirstWord("son"));
                    break;
                case "employee":
                    holder.binding.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.husband));
                    holder.binding.tvType.setText(UtilMethods.capitalizeFirstWord("employee"));
                    break;
                case "spouse":
                    holder.binding.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.husband));
                    holder.binding.tvType.setText(UtilMethods.capitalizeFirstWord("spouse"));
                    break;
                case "twins":
                    if (dependantHelperModel.getGender().equalsIgnoreCase("MALE")) {
                        holder.binding.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.son));
                        holder.binding.tvType.setText(UtilMethods.capitalizeFirstWord("son"));
                    } else {
                        holder.binding.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.daughter));
                        holder.binding.tvType.setText(UtilMethods.capitalizeFirstWord("daughter"));
                    }
                    holder.binding.ivtwins.setVisibility(View.VISIBLE);
                    holder.binding.etAge.setVisibility(View.VISIBLE);
                    holder.binding.etAge.setText("(" + dependantHelperModel.getAge() + " Years)");
                    break;
                case "other":
                    holder.binding.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.other));
                    holder.binding.tvType.setText(UtilMethods.capitalizeFirstWord("other"));
                    break;
                case "partner":
                    holder.binding.relIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.other));
                    holder.binding.tvType.setText(UtilMethods.capitalizeFirstWord("partner"));
                    break;
            }
            holder.binding.relIcon.setVisibility(View.VISIBLE);
            holder.binding.tvType.setVisibility(View.VISIBLE);

            //disable the sample relation
            holder.binding.lblSampleRelation.setVisibility(View.GONE);

            //is differently abled
            if (dependantHelperModel.isDifferentlyAble()) {
                holder.binding.ivdiff.setVisibility(View.VISIBLE);
                holder.binding.ivdiffdoc.setVisibility(View.VISIBLE);

                holder.binding.ivdiffdoc.setOnClickListener(v -> {
                    Toast.makeText(context, "TODO: file show dialog", Toast.LENGTH_SHORT).show();
                });

            } else {
                holder.binding.ivdiff.setVisibility(View.GONE);
                holder.binding.ivdiffdoc.setVisibility(View.GONE);
            }


            //make name visible
            holder.binding.etName.setVisibility(View.VISIBLE);
            holder.binding.etName.setText(dependantHelperModel.getName());

            //make age visible
            holder.binding.etAge.setVisibility(View.VISIBLE);
            holder.binding.etAge.setText("(" + dependantHelperModel.getAge() + " Years)");
            //make dob visible
            holder.binding.etDOB.setVisibility(View.VISIBLE);
            holder.binding.etDOB.setText(dependantHelperModel.getDateOfBirth());

            //edit
            holder.binding.rlSave.setOnClickListener(v -> {
                if (dependantHelperModel.isCanEdit()) {
                    if (dependantHelperModel.getRelationName().equalsIgnoreCase("twins")) {
                        //edit twin invoker
                        //we need to get the twins here. to edit the card
                        dependantHelper.onEditTwin(position);

                    }
                    dependantHelper.onEditDependant(dependantHelperModel, position);
                } else {
                    //TODO show feedback that user cant be edited
                }
            });

            //TODO show premium


        } else {

            //dependant when not added.
            holder.binding.addNewIcon.setVisibility(View.VISIBLE);
            holder.binding.ivdiffdoc.setVisibility(View.GONE);
            holder.binding.ivdiff.setVisibility(View.GONE);
            holder.binding.ivtwins.setVisibility(View.GONE);
            holder.binding.relIcon.setVisibility(View.GONE);
            holder.binding.etDOB.setText("");
            holder.binding.etAge.setText("");
            holder.binding.tvType.setText("");

            //edit image
            holder.binding.rlSave.setImageDrawable(null);


            holder.binding.completeView.setOnClickListener(view -> {
                dependantHelper.onAddDependant(dependantHelperModel, position);
            });

        }
    }

    private String getAge(PrimeCalendar date_of_birth) {
        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - date_of_birth.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < date_of_birth.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);

        return ageInt.toString();
    }

    @Override
    public int getItemCount() {
        return (dependantHelperModelList != null ? dependantHelperModelList.size() : 0);
    }


    public void itemClicked(int position) {

    }

    public static class DependantDetailsViewHolder extends RecyclerView.ViewHolder {
        public ItemEnrollmentDependantDetailsBinding binding;

        public DependantDetailsViewHolder(@NonNull ItemEnrollmentDependantDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
