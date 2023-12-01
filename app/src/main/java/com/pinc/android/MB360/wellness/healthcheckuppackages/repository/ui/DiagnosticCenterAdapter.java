package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.DiagnosticCenter;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.retrofit.OnDiagnosticCenterSelected;

import java.util.ArrayList;
import java.util.List;

public class DiagnosticCenterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<DiagnosticCenter> diagnosticCenterList = new ArrayList<>();
    OnDiagnosticCenterSelected diagnosticCenterSelected;

    public DiagnosticCenterAdapter(Context mContext, List<DiagnosticCenter> diagnosticCenterList, OnDiagnosticCenterSelected diagnosticCenterSelected) {
        this.diagnosticCenterList = diagnosticCenterList;
        this.mContext = mContext;
        this.diagnosticCenterSelected = diagnosticCenterSelected;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_hospital, parent, false);

        return new DiagnosticCenterAdapter.DiagnosticCenterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        try {
            final DiagnosticCenter lstDiagnosticCenter = diagnosticCenterList.get(position);

            ((DiagnosticCenterViewHolder) holder).hospitalName.setText(lstDiagnosticCenter.getName());
            ((DiagnosticCenterViewHolder) holder).hospitalAddress.setText(lstDiagnosticCenter.getAddress());

            ((DiagnosticCenterViewHolder) holder).scheduleHereLL.setOnClickListener(view -> {
                diagnosticCenterSelected.onDiagnosticSelected(diagnosticCenterList.get(position));
            });

        } catch (Exception e) {
            Log.e(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "Error ", e);
        }

    }

    @Override
    public int getItemCount() {
        if (diagnosticCenterList != null) {
            return diagnosticCenterList.size();
        } else {
            return 0;
        }
    }

    public static class DiagnosticCenterViewHolder extends RecyclerView.ViewHolder {

        TextView hospitalName, hospitalAddress;
        LinearLayout scheduleHereLL;
        View v1;

        public DiagnosticCenterViewHolder(@NonNull View itemView) {
            super(itemView);

            hospitalName = itemView.findViewById(R.id.tvhospitalname);
            hospitalAddress = itemView.findViewById(R.id.tvhospitaladdress);
            scheduleHereLL = itemView.findViewById(R.id.scheduleHereLL);

            v1 = itemView.findViewById(R.id.v1);

        }
    }
}
