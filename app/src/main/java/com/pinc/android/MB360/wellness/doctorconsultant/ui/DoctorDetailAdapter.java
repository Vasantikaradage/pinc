package com.pinc.android.MB360.wellness.doctorconsultant.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.ItemDetailsDoctorBinding;

import java.util.List;

public class DoctorDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<String> detailsList;
    Context context;
    String PlanNumber;

    public DoctorDetailAdapter(List<String> detailsList, Context context, String PlanNumber) {
        this.detailsList = detailsList;
        this.context = context;
        this.PlanNumber = PlanNumber;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDetailsDoctorBinding binding = ItemDetailsDoctorBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new DoctorDetailAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        switch(PlanNumber) {
            case "1":

                ((DoctorDetailAdapterViewHolder)holder).binding.categoryImageView.setImageResource(R.drawable.doctor_tick_icon);
                break;
            case "2":

                ((DoctorDetailAdapterViewHolder)holder).binding.categoryImageView.setImageResource(R.drawable.doctor_pack_icon2);
                break;
            case "3":

                ((DoctorDetailAdapterViewHolder)holder).binding.categoryImageView.setImageResource(R.drawable.doctor_tick_icon_package_3);
                break;
            case "4":

                ((DoctorDetailAdapterViewHolder)holder).binding.categoryImageView.setImageResource(R.drawable.doctor_tick3);
                break;
            case "5":
                ((DoctorDetailAdapterViewHolder)holder).binding.categoryImageView.setImageResource(R.drawable.doctor_tick4);
                break;

            default:

                ((DoctorDetailAdapterViewHolder)holder).binding.categoryImageView.setImageResource(R.drawable.doctor_tick_icon);
                break;
        }


        ((DoctorDetailAdapterViewHolder)holder).binding.categoryTextView.setText(detailsList.get(position));



    }

    @Override
    public int getItemCount() {
        if (detailsList != null){
            return detailsList.size();
        }else {
            return 0;
        }
    }


    public static class DoctorDetailAdapterViewHolder extends RecyclerView.ViewHolder {
        ItemDetailsDoctorBinding binding;

        public DoctorDetailAdapterViewHolder(@NonNull ItemDetailsDoctorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
