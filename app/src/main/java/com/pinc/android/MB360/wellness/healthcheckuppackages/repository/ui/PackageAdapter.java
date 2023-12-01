package com.pinc.android.MB360.wellness.healthcheckuppackages.repository.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.utilities.UtilMethods;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Packages;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Person;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.retrofit.PackageInterface;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.retrofit.PersonDelete;

import java.util.ArrayList;
import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    PackageInterface packageInterface;
    List<Packages> packagesList = new ArrayList<>();
    PersonDelete personDelete;
    // Person List RecyclerView Adapter
    PersonAdapter adapter;

    public PackageAdapter(Context mContext, List<Packages> packagesList, PackageInterface packageInterface, PersonDelete personDelete) {
        this.packagesList = packagesList;
        this.mContext = mContext;
        this.packageInterface = packageInterface;
        this.personDelete = personDelete;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_package, parent, false);

        return new PackagesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        try {
            final Packages lstPackages = packagesList.get(position);

            String price = String.valueOf(lstPackages.getPackagePrice()).replace("\u20B9 ", "");

            ((PackagesViewHolder) holder).packageName.setText(lstPackages.getPackageName());
//            ((PackagesViewHolder) holder).packagePrice.setText(lstPackages.getPackagePrice());
            ((PackagesViewHolder) holder).packagePrice.setText("\u20B9 " + UtilMethods.PriceFormat(price));
            ((PackagesViewHolder) holder).gender.setText(lstPackages.getGender());


            if (lstPackages.getGender().equals("MALE")) {

                ((PackagesViewHolder) holder).genderLL.setBackgroundColor(ContextCompat.getColor(mContext, R.color.display_male_label_color_package));

            } else if (lstPackages.getGender().equals("FEMALE")) {

                ((PackagesViewHolder) holder).genderLL.setBackgroundColor(ContextCompat.getColor(mContext, R.color.display_female_label_color_package));

            } else {

                ((PackagesViewHolder) holder).genderLL.setBackgroundColor(ContextCompat.getColor(mContext, R.color.display_both_label_color_package));

            }


            adapter = new PersonAdapter(mContext, packagesList.get(position).getPersons(), packagesList.get(position), packageInterface, personDelete);
            ((PackagesViewHolder) holder).personRecyclerView.setAdapter(adapter);
            adapter.notifyItemRangeChanged(0, packagesList.get(position).getPersons().size());


            //  TO Navigate
            ((PackagesViewHolder) holder).packageInclude.setOnClickListener(view -> {

//                navController.navigate(R.id.action_healthCheckupFragment_to_diagnosticFragment);
//                Navigation.createNavigateOnClickListener(R.id.action_healthCheckupFragment_to_packageDetailsFragment).onClick(((PackagesViewHolder) holder).packageInclude);
                packageInterface.getPackageDetails(packagesList.get(position));

            });


        } catch (Exception e) {

            Log.e(LogTags.HEALTH_CHECKUP_PACKAGE_ACTIVITY, "Error ", e);
        }

    }

    @Override
    public int getItemCount() {
        if (packagesList != null) {
            return packagesList.size();
        } else {
            return 0;
        }
    }

    public void personRemoved(int packagePosition,int position,Person person) {
        //here we inform the adapter that person is removed.
        notifyItemChanged(packagePosition);
        adapter.personRemoved(position,person);

    }


    public static class PackagesViewHolder extends RecyclerView.ViewHolder {

        TextView packageName, packagePrice, packageFor, gender, packageInclude;
        RecyclerView personRecyclerView;
        LinearLayout genderLL;
        View v1;

        public PackagesViewHolder(@NonNull View itemView) {
            super(itemView);

            packageName = itemView.findViewById(R.id.tvpkgname);
            packagePrice = itemView.findViewById(R.id.tvpkgprice);
            packageFor = itemView.findViewById(R.id.tvpckgfor);
            gender = itemView.findViewById(R.id.tvgender);
            packageInclude = itemView.findViewById(R.id.packageIncludeTV);
            personRecyclerView = itemView.findViewById(R.id.rvapplicablepersons);
            genderLL = itemView.findViewById(R.id.genderLL);

            v1 = itemView.findViewById(R.id.v1);

        }
    }
}
