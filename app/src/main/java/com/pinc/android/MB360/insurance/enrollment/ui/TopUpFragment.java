package com.pinc.android.MB360.insurance.enrollment.ui;


import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentGmcTopUpBinding;
import com.pinc.android.MB360.insurance.enrollment.adapters.TopUpsAdapter;
import com.pinc.android.MB360.insurance.enrollment.interfaces.TopUpSelected;
import com.pinc.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;
import com.pinc.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;
import com.pinc.android.MB360.insurance.enrollment.repository.responseclass.TopSumInsuredsValue;
import com.pinc.android.MB360.utilities.SwipeToDeleteCallback;
import com.pinc.android.MB360.utilities.TopUpItemTouchHelper;


import java.text.MessageFormat;
import java.util.List;


public class TopUpFragment extends Fragment implements TopUpSelected, SwipeToDeleteCallback.RecyclerItemTouchHelperListener {

    FragmentGmcTopUpBinding binding;
    View view;
    EnrollmentViewModel enrollmentViewModel;
    TopUpsAdapter adapter;
    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;
    List<TopSumInsuredsValue> topUpList;
    String page = "";

    public TopUpFragment() {
        // Required empty public constructor
    }

    public TopUpFragment(ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper, String page) {
        // Required empty public constructor
        this.viewPagerNavigationMenuHelper = viewPagerNavigationMenuHelper;
        this.page = page;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentGmcTopUpBinding.inflate(getLayoutInflater(), container, false);
        view = binding.getRoot();
        enrollmentViewModel = new ViewModelProvider(requireActivity()).get(EnrollmentViewModel.class);

        binding.lblInst.setText(MessageFormat.format(getString(R.string.lblTopupQuery), page));

        switch (page) {
            case "GMC":
                binding.titleIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_gmctopup));
                binding.lblTopupType.setText("GMC Top-Up");
                //get the topupsdata
                getTopUP(page);
                break;
            case "GPA":
                binding.titleIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_gpa_sheild));
                binding.lblTopupType.setText("GPA Top-Up");
                //get the topupsdata
                getTopUP(page);
                break;
            case "GTL":
                binding.titleIcon.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_gtl_sheild));
                binding.lblTopupType.setText("GTL Top-Up");
                //get the topupsdata
                getTopUP(page);
                break;
        }


        return view;
    }

    private void getTopUP(String type_of_policy) {
        enrollmentViewModel.getTopups();
        enrollmentViewModel.getTopUpsData().observe(getViewLifecycleOwner(), topUpData -> {
            if (topUpData != null) {
                switch (type_of_policy) {
                    case "GMC":
                        topUpList = topUpData.getSumInsuredData().getEnrollTopupOptions().getTopupSumInsuredClsData().getGMCTopupOptionsData().get(0).getTopSumInsuredsValues();
                        adapter = new TopUpsAdapter(requireContext(), topUpList, this);
                        binding.spnTopUpamt.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        ItemTouchHelper.SimpleCallback itemTouchHelperCallbackGMC = new TopUpItemTouchHelper(0,
                                ItemTouchHelper.LEFT, this, topUpList);
                        new ItemTouchHelper(itemTouchHelperCallbackGMC).attachToRecyclerView(binding.spnTopUpamt);
                        break;
                    case "GPA":
                        topUpList = topUpData.getSumInsuredData().getEnrollTopupOptions().getTopupSumInsuredClsData().getGPATopupOptionsData().get(0).getTopSumInsuredsValues();
                        adapter = new TopUpsAdapter(requireContext(), topUpList, this);
                        binding.spnTopUpamt.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        ItemTouchHelper.SimpleCallback itemTouchHelperCallbackGPA = new TopUpItemTouchHelper(0,
                                ItemTouchHelper.LEFT, this, topUpList);
                        new ItemTouchHelper(itemTouchHelperCallbackGPA).attachToRecyclerView(binding.spnTopUpamt);
                        break;
                    case "GTL":
                        topUpList = topUpData.getSumInsuredData().getEnrollTopupOptions().getTopupSumInsuredClsData().getGTLTopupOptionsData().get(0).getTopSumInsuredsValues();
                        adapter = new TopUpsAdapter(requireContext(), topUpList, this);
                        binding.spnTopUpamt.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        ItemTouchHelper.SimpleCallback itemTouchHelperCallbackGTL = new TopUpItemTouchHelper(0,
                                ItemTouchHelper.LEFT, this, topUpList);
                        new ItemTouchHelper(itemTouchHelperCallbackGTL).attachToRecyclerView(binding.spnTopUpamt);
                        break;
                }

            }
        });


    }

    @Override
    public void OnTopUpSelected(TopSumInsuredsValue topUp, int position) {
        //here we mock the data that user selected the
        for (TopSumInsuredsValue topUpValue : topUpList
        ) {
            if (!topUpValue.equals(topUp)) {
                topUpValue.setOpted("NO");
                adapter.notifyItemChanged(topUpList.indexOf(topUpValue));
            }
        }

        topUpList.get(position).setOpted("YES");
        adapter.notifyItemChanged(position);

    }

    @Override
    public void onResume() {
        super.onResume();
      /*  viewPagerNavigationMenuHelper.showSummaryOption();
        viewPagerNavigationMenuHelper.showHomeButton();*/
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        topUpList.get(position).setOpted("NO");
        adapter.notifyItemChanged(position);
    }
}