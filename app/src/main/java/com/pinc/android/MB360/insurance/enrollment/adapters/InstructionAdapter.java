package com.pinc.android.MB360.insurance.enrollment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.ItemEnrollmentInstructionBinding;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.Instruction;

import java.util.List;

public class InstructionAdapter extends RecyclerView.Adapter<InstructionAdapter.InstructionViewHolder> {

    Context context;
    List<Instruction> instructionList;
    String to_show;

    public InstructionAdapter(Context context, List<Instruction> instructionList, String data_to_show_for) {
        this.context = context;
        this.instructionList = instructionList;
        to_show = data_to_show_for;
    }

    @NonNull
    @Override
    public InstructionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEnrollmentInstructionBinding binding = ItemEnrollmentInstructionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new InstructionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionViewHolder holder, int position) {
        Animation instruction_animation = AnimationUtils.loadAnimation(context, R.anim.slide_from_right);

        //conditions regarding rendering are displayed here
        if (to_show.equalsIgnoreCase(instructionList.get(position).getGrade())) {

            holder.binding.itemInstruction.startAnimation(instruction_animation);
            holder.binding.itemInstruction.setVisibility(View.VISIBLE);
            holder.binding.instructionText.setText(instructionList.get(position).getInstructionText());
        } else {
            if (to_show.equalsIgnoreCase(instructionList.get(position).getDesignation())) {
                holder.binding.itemInstruction.startAnimation(instruction_animation);
                holder.binding.itemInstruction.setVisibility(View.VISIBLE);
                holder.binding.instructionText.setText(instructionList.get(position).getInstructionText());
            } else {
                if (to_show.equalsIgnoreCase("")) {
                    holder.binding.itemInstruction.startAnimation(instruction_animation);
                    holder.binding.itemInstruction.setVisibility(View.VISIBLE);
                    holder.binding.instructionText.setText(instructionList.get(position).getInstructionText());
                } else {
                    holder.binding.itemInstruction.setVisibility(View.GONE);
                }
            }
        }





     /*   if (DESIGNATION.equalsIgnoreCase(instructionList.get(position).getToShowDESIGNATION())){
            holder.binding.itemInstruction.setVisibility(View.VISIBLE);
            holder.binding.instructionText.setText(instructionList.get(position).getInstructionText());
        }else{
            holder.binding.itemInstruction.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return (instructionList != null ? instructionList.size() : 0);
    }

    public static class InstructionViewHolder extends RecyclerView.ViewHolder {
        ItemEnrollmentInstructionBinding binding;

        public InstructionViewHolder(@NonNull ItemEnrollmentInstructionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
