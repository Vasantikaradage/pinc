package com.pinc.android.MB360.wellness.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentWellnessHomeBinding;
import com.pinc.android.MB360.insurance.DashBoardActivity;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.ui.DashboardWellnessAdapter;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.ui.WellnessMenuClickListener;
import com.pinc.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;

import java.util.Objects;

public class WellnessHomeFragment extends Fragment implements WellnessMenuClickListener {

    FragmentWellnessHomeBinding binding;
    View view;
    NavController navController;

    //WellnessDashBoard ViewModel
    DashboardWellnessViewModel dashboardWellnessViewModel;
    LoadSessionViewModel loadSessionViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;
    HomeHealthCareViewModel homeHealthCareViewModel;


    DashboardWellnessAdapter adapter;

    public WellnessHomeFragment() {
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

        binding = FragmentWellnessHomeBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();

        //viewModel scoped in the fragment.
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel.class);
        homeHealthCareViewModel = new ViewModelProvider(requireActivity()).get(HomeHealthCareViewModel.class);


        //also setup recyclerView in the same function
        getDashboardWellness();

        dashboardWellnessViewModel.getLoadingState().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                showLoading();
            } else {
                hideLoading();
            }
        });

        //on clicked Wellness
        binding.titleMenu.rlTabInsurance.setOnClickListener(view1 -> {
            Intent insuranceIntent = new Intent(requireActivity(), DashBoardActivity.class);
            startActivity(insuranceIntent);
            requireActivity().finish();
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(false /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                Intent insuranceIntent = new Intent(requireActivity(), DashBoardActivity.class);
                startActivity(insuranceIntent);
                requireActivity().finish();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    private void getDashboardWellness() {

        loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {
            String agent = "app";

            dashboardWellnessViewModel.getEmployeeWellnessDetailsData().observe(requireActivity(), employeeCheckResponse -> {
                if (employeeCheckResponse.getExtGroupSrNo() != null) {
                    String extGroupSrno = employeeCheckResponse.getExtGroupSrNo();
                    dashboardWellnessViewModel.getDashboardWellness(agent, extGroupSrno);
                } else {
                    Toast.makeText(getContext(), "Wellness not available", Toast.LENGTH_SHORT).show();
                }
            });

        });

        dashboardWellnessViewModel.getDashboardWellnessData().observe(getViewLifecycleOwner(), dashboardServiceResponse -> {


            if (!dashboardServiceResponse.getMessage().getStatus()) {
                binding.messageTextView.setVisibility(View.VISIBLE);
                binding.messageTextView.setText("" + dashboardServiceResponse.getMessage().getMessage());
                hideLoading();
            } else {

                if (dashboardServiceResponse != null && dashboardServiceResponse.getMessage().getStatus()) {
                    adapter = new DashboardWellnessAdapter(requireContext(), dashboardServiceResponse.getServices(), this);
                    binding.wellnessDashboardRecyclerView.setAdapter(adapter);
                    adapter.notifyItemRangeChanged(0, dashboardServiceResponse.getServices().size());

                    binding.messageTextView.setVisibility(View.GONE);
                } else {
                    binding.messageTextView.setVisibility(View.VISIBLE);
                    binding.messageTextView.setText("Sorry, data not available");
                    hideLoading();
                }
            }


        });

    }


    private void showLoading() {
        binding.progressLayout.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        binding.progressLayout.setVisibility(View.GONE);
    }

    @Override
    public void onClickWellnessMenu(String serviceName) {

        homeHealthCareViewModel.setService(serviceName);
        switch (serviceName) {
            case "HEALTH CHECKUP":
                NavDirections actions = WellnessHomeFragmentDirections.actionWellnessHomeFragmentToHealthCheckupFragment();
                navController.navigate(actions);
                break;
            case "DOCTOR CONSULTATION":
                NavDirections dc = WellnessHomeFragmentDirections.actionWellnessHomeFragmentToDoctorPackagesExploreFragment();
                navController.navigate(dc);
                break;
            case "DENTAL":
            case "MEDICINE DELIVERY":
                break;
            default:
                NavDirections trained_attendant_action = WellnessHomeFragmentDirections.actionWellnessHomeFragmentToMembersFragment();
                navController.navigate(trained_attendant_action);
                break;
        }

    }

    private void exitWarning() {
        final Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.smslayout);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.nhborder));


        DisplayMetrics dm = new DisplayMetrics();

        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        dialog.getWindow().setLayout((int) (width * .6), WindowManager.LayoutParams.WRAP_CONTENT);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        dialog.getWindow().setAttributes(params);
        AppCompatTextView lblSMS = dialog.findViewById(R.id.lblSMS);
        final AppCompatEditText smsContact = dialog.findViewById(R.id.smsContact);
        AppCompatButton btnSubmit = dialog.findViewById(R.id.btnSubmit);
        AppCompatButton btnCancel = dialog.findViewById(R.id.btnCancel);
        AppCompatTextView lblSMSHeader = dialog.findViewById(R.id.lblSMSHeader);
        lblSMS.setText("PINC Insurance");
        lblSMSHeader.setText("Are you sure you want to Exit?");
        btnSubmit.setText("Exit");
        btnCancel.setText("Cancel");
        smsContact.setVisibility(View.GONE);

        btnSubmit.setOnClickListener(v -> {
            dialog.dismiss();
            requireActivity().finish();
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }
}
