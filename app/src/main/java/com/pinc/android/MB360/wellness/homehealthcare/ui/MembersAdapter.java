package com.pinc.android.MB360.wellness.homehealthcare.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.ItemHomeHealthCareMemberBinding;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.FamilyMember;

import java.util.List;

public class MembersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    List<FamilyMember> membersList;
    AddAddressListener addAddressListener;

    public MembersAdapter(Context context, List<FamilyMember> membersList, AddAddressListener addAddressListener) {
        this.context = context;
        this.membersList = membersList;
        this.addAddressListener = addAddressListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHomeHealthCareMemberBinding binding = ItemHomeHealthCareMemberBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MembersViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final FamilyMember lstMember = membersList.get(holder.getAdapterPosition());

        ((MembersViewHolder) holder).binding.txtName.setText(membersList.get(position).getPersonName());
        ((MembersViewHolder) holder).binding.txtRelations.setText(membersList.get(position).getRelationName());
        String age = "(" + membersList.get(position).getAge() + " years)";
        ((MembersViewHolder) holder).binding.txtdepandantAge.setText(age);
//
        ((MembersViewHolder) holder).binding.member.setOnClickListener(v -> {
                if (membersList.get(position).getIsAddressSaved().equals("0")) {
                    addAddressListener.getMember(membersList.get(position));

                } else {
                    addAddressListener.selectPackage(membersList.get(position));
                }
            });

//        if (membersList.get(position).isSelected()) {
//
//            ((MembersViewHolder) holder).binding.checkedRadio.setChecked(true);
//            if (membersList.get(position).getIsAddressSaved().equals("1")) {
//                ((MembersViewHolder) holder).binding.actionText.setText("Select Packages");
//                ((MembersViewHolder) holder).binding.btnSelect.setOnClickListener(view -> {
//                    addAddressListener.selectPackage(membersList.get(position));
//                });
//
//            }else {
//                ((MembersViewHolder) holder).binding.actionText.setText("Add Address");
//                ((MembersViewHolder) holder).binding.btnSelect.setOnClickListener(view -> {
//                    addAddressListener.getMember(membersList.get(position));
//                });
//            }
////
//
//        } else {
////            ((PersonAdapter.PackagesViewHolder) holder).llscheduleapp.setVisibility(View.GONE);
//            ((MembersViewHolder) holder).binding.checkedRadio.setChecked(false);
//            ((MembersViewHolder) holder).binding.actionText.setText("Select Member");
//        }

        ((MembersViewHolder) holder).binding.checkedRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setSelected(position, b);
//                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (membersList == null) {
            return 0;
        } else {
            return membersList.size();
        }
    }

    static class MembersViewHolder extends RecyclerView.ViewHolder {
        ItemHomeHealthCareMemberBinding binding;

        public MembersViewHolder(@NonNull ItemHomeHealthCareMemberBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private void setSelected(int pos, boolean check) {

//        data.getData().get(this.pos).getServicesName().get(pos).setSelected(check);
        membersList.get(pos).setSelected(check);
        notifyDataSetChanged();

    }
}
