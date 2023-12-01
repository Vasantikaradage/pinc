package com.pinc.android.MB360.wellness.homehealthcare.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aminography.primecalendar.PrimeCalendar;
import com.pinc.android.MB360.R;
import com.pinc.android.MB360.monthpicker.MonthModel;

import java.util.ArrayList;

public class DatesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<PrimeCalendar> dateList = new ArrayList<>();
    View view;
    ArrayList<String> months = new ArrayList<>();
    ArrayList<MonthModel> monthList = new ArrayList<>();

    public DatesRecyclerViewAdapter(ArrayList<PrimeCalendar> dateList, ArrayList<MonthModel> monthList) {
        this.dateList = dateList;
        this.monthList = monthList;
        months.add("JAN");
        months.add("FEB");
        months.add("MAR");
        months.add("APR");
        months.add("MAY");
        months.add("JUN");
        months.add("JUL");
        months.add("AUG");
        months.add("SEP");
        months.add("OCT");
        months.add("NOV");
        months.add("DEC");
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dates_item, parent, false);
        return new DatesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (dateList.size() != 0) {
            ((DatesViewHolder) holder).dates.setText(String.valueOf(dateList.get(position).getDate()) + "-" + months.get(dateList.get(position).getMonth()) + "-" + String.valueOf(dateList.get(position).getYear()));
        } else if (monthList.size() != 0) {
            ((DatesViewHolder) holder).dates.setText(monthList.get(position).getMonth() + "-" + monthList.get(position).getYear());
        }
    }

    @Override
    public int getItemCount() {
        if (dateList.size() != 0) {
            return dateList.size();
        } else if (monthList.size() != 0) {
            return monthList.size();
        }
        return 0;
    }


    public static class DatesViewHolder extends RecyclerView.ViewHolder {

        TextView dates;

        public DatesViewHolder(@NonNull View itemView) {
            super(itemView);
            dates = itemView.findViewById(R.id.item_dates);

        }
    }

}
