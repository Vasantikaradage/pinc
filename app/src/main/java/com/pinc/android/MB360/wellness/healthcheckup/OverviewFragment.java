package com.pinc.android.MB360.wellness.healthcheckup;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentClaimsBinding;
import com.pinc.android.MB360.databinding.FragmentHealthCheckupBinding;
import com.pinc.android.MB360.databinding.FragmentOverviewBinding;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.pinc.android.MB360.wellness.healthcheckup.repository.HealthCheckupOverviewViewModel;


public class OverviewFragment extends Fragment {

    FragmentOverviewBinding binding;
    View view;
    NavController navController;

    //Helthcheckup ViewModel
    HealthCheckupOverviewViewModel healthCheckupOverviewViewModel;
    LoadSessionViewModel loadSessionViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;

    String externalGroupSrNo;

    String css_classes = "<head><style>@font-face { font-family:MyFont; src: url('file:///android_asset/font/Poppins-Regular.ttf');} .main-container{ text-align: justify; font-size:13px; font-family:MyFont; color:#696969;} ul.pretests { padding-left: 0px; }ul.pretests li{ color:#696969; line-height: 1;font-size : 13px; font-family:MyFont; text-align: justify; margin: 0px 0px 10px; }.main-container .text-center {text-align: center;}.main-container ul { padding-left:20px; }span.clearfix { color:#696969; font-size:13px; font-size:13px; font-family:MyFont;}.h1 {font-size: 24px;}.main-container h2.sbold {font-size: larger;}.sbold { font-weight: 400!important; }.main-container h2,.text-primary,.text-info { color: #0096d6; }.h1, .h2, .h3, h1, h2, h3 {margin-top: 20px;margin-bottom: 10px; } </style></head>";

    public OverviewFragment() {
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
        binding = FragmentOverviewBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        healthCheckupOverviewViewModel = new ViewModelProvider(OverviewFragment.this).get(HealthCheckupOverviewViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel.class);

        //also setup recyclerView in the same function
        getHealthCheckupOveriew();

        binding.doneCV.setOnClickListener(view -> {
            requireActivity().onBackPressed();
        });

        healthCheckupOverviewViewModel.getLoadingState().observe(getViewLifecycleOwner(), loading -> {

        });

        return view;
    }

    private void getHealthCheckupOveriew() {
        //to get the  policyFeatures data we need some parameters from load session values


            loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {

                externalGroupSrNo = ""+loadSessionResponse.getGroupInfoData().getGroupcode();
                String agent = "Android";
//
                healthCheckupOverviewViewModel.getHealthCheckupOverview(externalGroupSrNo,agent);



        });
//
//        String externalGroupSrNo = "NAYASA1";
//        String agent = "Android";
//
//        healthCheckupOverviewViewModel.getHealthCheckupOverview(externalGroupSrNo,agent);

        String name = externalGroupSrNo;

        healthCheckupOverviewViewModel.getHealthCheckupOverviewData().observe(getViewLifecycleOwner(), healthCheckupOverviewResponse -> {

            binding.wvOverview.loadDataWithBaseURL(null, healthCheckupOverviewResponse.getCompanyOverview().replace("Travel Container Services Limited",name + css_classes), "text/html", "UTF-8", "about:blank");

        });
    }
}