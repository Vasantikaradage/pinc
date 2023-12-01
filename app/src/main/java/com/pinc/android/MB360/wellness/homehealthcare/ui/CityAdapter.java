package com.pinc.android.MB360.wellness.homehealthcare.ui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.City;
import com.pinc.android.MB360.wellness.homehealthcare.retrofit.CityInterface;

import java.util.ArrayList;
import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    List<City> cityList = new ArrayList<>();
    NavController navController;

    // city interface
    CityInterface cityInterface;

    City city;

    public CityAdapter(Context mContext, List<City> cityList, CityInterface cityInterface) {
        this.mContext = mContext;
        this.cityList = cityList;
        this.city = city;
        this.cityInterface = cityInterface;
//        this.diagnosticCenterSelected = diagnosticCenterSelected;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_city, parent, false);

        return new CityAdapter.CityViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        try {
            final City lstCity = cityList.get(position);

            ((CityViewHolder) holder).cityTV.setText(lstCity.getCity());

            ((CityViewHolder) holder).itemView.setOnClickListener(view -> {

                lstCity.setCity(""+((CityViewHolder) holder).cityTV.getText().toString());
//                ((CityViewHolder) holder).cityTV.setBackground(mContext.getDrawable(R.drawable.roundedsquare));//setButtonTintList is accessible directly on API>19

                ((CityViewHolder) holder).cityLL.getBackground().setColorFilter(mContext.getResources().getColor(R.color.greenlightbg2), PorterDuff.Mode.SRC_ATOP);
                ((CityViewHolder) holder).cityTV.setTextColor(mContext.getResources().getColor(R.color.white));

                cityInterface.getCity(cityList.get(position));
//                navController.navigateUp();
            });
//            ((DiagnosticCenterViewHolder) holder).scheduleHereLL.setOnClickListener(view -> {
//                diagnosticCenterSelected.onDiagnosticSelected(diagnosticCenterList.get(position));
//            });

        } catch (Exception e) {
            Log.e(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "Error ", e);
        }

    }

    @Override
    public int getItemCount() {
        if (cityList != null) {
            return cityList.size();
        } else {
            return 0;
        }
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder {

        TextView cityTV;
        LinearLayout cityLL;
        View v1;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);

            cityTV = itemView.findViewById(R.id.tv_city);
            cityLL = itemView.findViewById(R.id.llcity);

            v1 = itemView.findViewById(R.id.v1);

        }
    }
}
