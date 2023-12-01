package com.pinc.android.MB360.insurance.enrollment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.TopuplayooutBinding;
import com.pinc.android.MB360.insurance.enrollment.interfaces.TopUpSelected;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.TopSumInsuredsValue;


import java.util.List;

public class TopUpsAdapter extends RecyclerView.Adapter<TopUpsAdapter.TopUpsViewHolder> {
    Context context;
    List<TopSumInsuredsValue> topUpList;
    TopUpSelected topUpSelected;

    public TopUpsAdapter(Context context, List<TopSumInsuredsValue> topUpList, TopUpSelected topUpSelected) {
        this.context = context;
        this.topUpList = topUpList;
        this.topUpSelected = topUpSelected;
    }

    @NonNull
    @Override
    public TopUpsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TopuplayooutBinding binding = TopuplayooutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TopUpsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TopUpsViewHolder holder, int position) {
        //bind top data here

        TopSumInsuredsValue topUp = topUpList.get(position);
        holder.binding.lblOptions.setText("option " + (position + 1));
        holder.binding.topupAmt.setText(String.format("%s %s", context.getString(R.string.rs), topUp.getTSumInsured()));
        holder.binding.premiumTxt.setText(String.format("Premium %s %s", context.getString(R.string.rs), topUp.getTSumInsuredPremium()));

        if (topUp.getOpted().equalsIgnoreCase("yes")) {
            holder.binding.checkedRadio.setVisibility(View.VISIBLE);
            holder.binding.chkbox.setChecked(true);
        } else {
            holder.binding.checkedRadio.setVisibility(View.INVISIBLE);
            holder.binding.chkbox.setChecked(false);
        }
        holder.binding.completeView.setOnClickListener(v -> {
            topUpSelected.OnTopUpSelected(topUp, position);
        });
    }

    @Override
    public int getItemCount() {
        return (topUpList != null ? topUpList.size() : 0);
    }

    public static class TopUpsViewHolder extends RecyclerView.ViewHolder {
        public TopuplayooutBinding binding;

        public TopUpsViewHolder(@NonNull TopuplayooutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}
