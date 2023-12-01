package com.pinc.android.MB360.insurance.enrollment.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentEmployeeDetailsBinding;
import com.pinc.android.MB360.insurance.enrollment.adapters.EmployeeDetailsAdapter;
import com.pinc.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;
import com.pinc.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;
import com.pinc.android.MB360.utilities.LogTags;


public class EmployeeDetailsFragment extends Fragment {

    FragmentEmployeeDetailsBinding binding;
    View view;
    EnrollmentViewModel enrollmentViewModel;
    EmployeeDetailsAdapter adapter;
    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;


    public EmployeeDetailsFragment() {
        // Required empty public constructor
    }

    public EmployeeDetailsFragment(ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper) {
        this.viewPagerNavigationMenuHelper = viewPagerNavigationMenuHelper;

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
        binding = FragmentEmployeeDetailsBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        enrollmentViewModel = new ViewModelProvider(requireActivity()).get(EnrollmentViewModel.class);



        getEmployeeDetails();


        return view;

    }

    private void getEmployeeDetails() {
        enrollmentViewModel.getEmployee();

        enrollmentViewModel.getEmployeeData().observe(getViewLifecycleOwner(), employeeResponse -> {
            if (employeeResponse != null) {
                adapter = new EmployeeDetailsAdapter(requireActivity(), employeeResponse.getEmployeeDetails());

                GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false);
                layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {

                        return (employeeResponse.getEmployeeDetails().get(position).getFieldName().length() < 12 ? 1 : 2);//span condition here
                    }
                });
                binding.employeeDetailsCycle.setLayoutManager(layoutManager);
                binding.employeeDetailsCycle.setAdapter(adapter);

            } else {
                Log.e(LogTags.ENROLLMENT, "getEmployeeDetails: Null");
            }

            binding.loadingBar.setVisibility(View.GONE);
        });
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
       /* viewPagerNavigationMenuHelper.hideSummaryOption();
        viewPagerNavigationMenuHelper.showHomeButton();*/
    }
}