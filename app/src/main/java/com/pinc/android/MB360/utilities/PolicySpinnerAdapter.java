package com.pinc.android.MB360.utilities;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.insurance.repository.responseclass.GroupPolicy;
import com.pinc.android.MB360.insurance.repository.selectedPolicyRepo.responseclass.GroupPolicyData;

import java.util.List;


public class PolicySpinnerAdapter extends ArrayAdapter<GroupPolicyData> {
    public PolicySpinnerAdapter(@NonNull Context context, List<GroupPolicyData> policyList) {
        super(context, 0, policyList);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        //custom view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_spinner_policy, parent, false);
        }
        GroupPolicyData groupPolicyData = getItem(position);


        TextView policyNumber = convertView.findViewById(R.id.spinner_policy_number);
        policyNumber.setText(groupPolicyData.getPolicyNumber());

        return convertView;
    }

}
