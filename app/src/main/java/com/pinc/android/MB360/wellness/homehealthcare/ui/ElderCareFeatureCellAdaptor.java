package com.pinc.android.MB360.wellness.homehealthcare.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pinc.android.MB360.databinding.ElderFeaturesCellBinding;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.ElderCell;

import java.util.List;

public class ElderCareFeatureCellAdaptor extends RecyclerView.Adapter<ElderCareFeatureCellAdaptor.ElderCareFeatureViewHolder> {

    List<ElderCell> elderCellList;

    public ElderCareFeatureCellAdaptor(List<ElderCell> elderCellList) {
        this.elderCellList = elderCellList;
    }

    @NonNull
    @Override
    public ElderCareFeatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ElderFeaturesCellBinding binding = ElderFeaturesCellBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ElderCareFeatureViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ElderCareFeatureViewHolder holder, int position) {
        holder.binding.txtFeatureName.setText(elderCellList.get(position).getFeature());

        if (elderCellList.get(position).getValue() == 0) {
            holder.binding.availablityImage.setVisibility(View.VISIBLE);
            holder.binding.availablityText.setVisibility(View.GONE);
        } else {
            holder.binding.availablityImage.setVisibility(View.GONE);
            holder.binding.availablityText.setVisibility(View.VISIBLE);
            holder.binding.availablityText.setText(String.valueOf(elderCellList.get(position).getValue()));
        }
    }

    @Override
    public int getItemCount() {
        if (elderCellList != null) {
            return elderCellList.size();
        } else {
            return 0;
        }
    }

    public static class ElderCareFeatureViewHolder extends RecyclerView.ViewHolder {
        ElderFeaturesCellBinding binding;

        public ElderCareFeatureViewHolder(@NonNull ElderFeaturesCellBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
