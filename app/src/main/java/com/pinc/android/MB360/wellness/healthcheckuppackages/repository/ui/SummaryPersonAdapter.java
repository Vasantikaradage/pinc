package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.utilities.UtilMethods;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.SelfSponseredPerson;

import java.util.ArrayList;
import java.util.List;

public class SummaryPersonAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<SelfSponseredPerson> selfSponseredPeopleList = new ArrayList<>();

    public SummaryPersonAdapter(Context mContext, List<SelfSponseredPerson> selfSponseredPersonList) {
        this.selfSponseredPeopleList = selfSponseredPersonList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_summary, parent, false);

        return new SummaryPersonAdapter.SummaryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        try {
            final SelfSponseredPerson lstSelfSponseredPerson = selfSponseredPeopleList.get(position);

            ((SummaryViewHolder) holder).nameTV.setText(lstSelfSponseredPerson.getName());
            ((SummaryViewHolder) holder).priceTV.setText("\u20B9 "+ UtilMethods.PriceFormat(lstSelfSponseredPerson.getPrice()));


        } catch (Exception e) {
            Log.e(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "Error ", e);
        }

    }

    @Override
    public int getItemCount() {
        if (selfSponseredPeopleList != null) {
            return selfSponseredPeopleList.size();
        } else {
            return 0;
        }
    }

    public static class SummaryViewHolder extends RecyclerView.ViewHolder {

        TextView nameTV, priceTV;
        View v1;

        public SummaryViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTV = itemView.findViewById(R.id.tvmembername);
            priceTV = itemView.findViewById(R.id.tvprice);

            v1 = itemView.findViewById(R.id.v1);

        }
    }
}
