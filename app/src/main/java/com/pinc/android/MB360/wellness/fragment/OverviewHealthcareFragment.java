package com.pinc.android.MB360.wellness.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import com.pinc.android.MB360.databinding.FragmentOverviewBinding;
import com.pinc.android.MB360.databinding.FragmentOverviewHealthcareBinding;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.OverviewHealthcare;

import java.util.ArrayList;


public class OverviewHealthcareFragment extends Fragment {

    FragmentOverviewHealthcareBinding binding;
    View view;
    NavController navController;

    //Helthcare ViewModel
    HomeHealthCareViewModel homeHealthCareViewModel;
    LoadSessionViewModel loadSessionViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;

    ArrayList<OverviewHealthcare> overviewHealthcares;

    private final String css_classes = "<head><style>@font-face { font-family:MyFont; src: url('file:///android_asset/font/Poppins-Regular.ttf');} .main-container{ text-align: justify; font-size:13px; font-family:MyFont; color:#696969;} ul { padding-left: 0px; }ul li{ color:#696969; line-height: 1;font-size : 13px; font-family:MyFont; text-align: justify; margin: 0px 0px 10px; }.main-container .text-center {text-align: center;}.main-container ul { padding-left:20px; }span.clearfix { color:#696969; font-size:13px; font-size:13px; font-family:MyFont;}.h1 {font-size: 24px;}.main-container h2.sbold {font-size: larger;}.sbold { font-weight: 400!important; }.main-container h2,.text-primary,.text-info { color: #0096d6; }.h1, .h2, .h3, h1, h2, h3 {margin-top: 20px;margin-bottom: 10px; } </style></head>";

    public OverviewHealthcareFragment() {
        // Required empty public constructor
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
        binding = FragmentOverviewHealthcareBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        homeHealthCareViewModel = new ViewModelProvider(OverviewHealthcareFragment.this).get(HomeHealthCareViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel.class);

        //also setup recyclerView in the same function
        getHealthcareOveriew();

        binding.doneCV.setOnClickListener(view -> {
            requireActivity().onBackPressed();
        });

        homeHealthCareViewModel.getLoadingState().observe(getViewLifecycleOwner(), loading -> {

        });

        return view;
    }

    private void getHealthcareOveriew() {
        //to get the  policyFeatures data we need some parameters from load session values


            loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {
//                String wellSrNo = "17";
////
//                homeHealthCareViewModel.getHealthcareOverview(wellSrNo);


        });

        String wellSrNo = "17";

        homeHealthCareViewModel.getHealthcareOverview(wellSrNo);


        homeHealthCareViewModel.getHealthcareOverviewData().observe(getViewLifecycleOwner(), healthcareOverviewResponse -> {
//            String css_classes = "<head><style>@font-face { font-family:MyFont; src: url('file:///android_asset/font/Poppins-Regular.ttf');} .main-container{ text-align: justify; font-size:13px; font-family:MyFont; color:#696969;} ul.pretests { padding-left: 0px; }ul.pretests li{ color:#696969; line-height: 1;font-size : 13px; font-family:MyFont; text-align: justify; margin: 0px 0px 10px; }.main-container .text-center {text-align: center;}.main-container ul { padding-left:20px; }span.clearfix { color:#696969; font-size:13px; font-size:13px; font-family:MyFont;}.h1 {font-size: 24px;}.main-container h2.sbold {font-size: larger;}.sbold { font-weight: 400!important; }.main-container h2,.text-primary,.text-info { color: #0096d6; }.h1, .h2, .h3, h1, h2, h3 {margin-top: 20px;margin-bottom: 10px; } </style></head>";

//            binding.wvOverview.loadDataWithBaseURL(null, healthcareOverviewResponse.getOverview(), "text/html", "UTF-8", "about:blank");

            binding.wvOverview.loadDataWithBaseURL("", healthcareOverviewResponse.getOverview().get(0).getOverview() + css_classes, "text/html", "utf-8", null);
            
        });
    }
}