package com.pinc.android.MB360.wellness.healthcheckup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentDiagnosticBinding;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.utilities.UtilMethods;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.PackagesViewModel;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.DiagnosticCenter;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Packages;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Person;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.retrofit.OnDiagnosticCenterSelected;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.ui.DiagnosticCenterAdapter;
import com.pinc.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DiagnosticFragment extends Fragment implements OnDiagnosticCenterSelected {

    FragmentDiagnosticBinding binding;
    View view;
    NavController navController;
    Packages packages;
    Person person;
    boolean isReschedule;
    boolean isPaid;
    String oldApptInfoSrNo;

    //static string
    String city = null;

    // PackageLT View Model
    PackagesViewModel packagesViewModel;
    LoadSessionViewModel loadSessionViewModel;
    HomeHealthCareViewModel homeHealthCareViewModel;


    //to search through list
    List<DiagnosticCenter> diagnosticCenters = new ArrayList<>();

    //  Adapter
    DiagnosticCenterAdapter adapter;


    public DiagnosticFragment() {
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
        binding = FragmentDiagnosticBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        // to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();

        // viewmodel scope in fragment
        packagesViewModel = new ViewModelProvider(requireActivity()).get(PackagesViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        homeHealthCareViewModel = new ViewModelProvider(requireActivity()).get(HomeHealthCareViewModel.class);

        getDiagnosticCenter();

        //searching logic
        binding.etSearchText.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchDiagnosticCenter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchDiagnosticCenter(newText);
                return false;
            }
        });


        packagesViewModel.getLoadingState().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                showLoading();
            } else {
                hideLoading();
            }
        });


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //getting person and packages from previous screen
        packages = DiagnosticFragmentArgs.fromBundle(getArguments()).getGetPackages();
        person = DiagnosticFragmentArgs.fromBundle(getArguments()).getGetPerson();
        isReschedule = DiagnosticFragmentArgs.fromBundle(getArguments()).getIsReschedule();
        isPaid = DiagnosticFragmentArgs.fromBundle(getArguments()).getIsPaid();
        oldApptInfoSrNo = DiagnosticFragmentArgs.fromBundle(getArguments()).getIsOldApptInfoSrNo();


    }

    private void getDiagnosticCenter() {

        loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {

            packagesViewModel.getCitiesLiveDataHC().observe(getViewLifecycleOwner(), cityLive -> {
                if (cityLive != null) {
                    city = cityLive;
                    packagesViewModel.getDiagnosticCenter(city);
                } else {
                    //todo show the dialogue box (cities)
                    Log.e(LogTags.HEALTH_CHECKUP_ACTIVITY, "getDiagnosticCenter: NULL");
                }
            });


        });

        packagesViewModel.getDiagnosticCenterData().observe(getViewLifecycleOwner(), diagnosticCenterResponse -> {

            if (diagnosticCenterResponse != null) {

                adapter = new DiagnosticCenterAdapter(requireActivity(), diagnosticCenterResponse.getDaignosticCenterList(), this);
                binding.hospitalRecyclerView.setAdapter(adapter);
                adapter.notifyItemRangeChanged(0, diagnosticCenterResponse.getDaignosticCenterList().size());

                if (diagnosticCenterResponse != null) {
                    diagnosticCenters = diagnosticCenterResponse.getDaignosticCenterList();
                    binding.tvDiagCount.setText(Html.fromHtml(String.format("Total <b> <font color='black'> %s </font> </b> Diagnostic Center", UtilMethods.PriceFormat(String.valueOf(diagnosticCenterResponse.getDaignosticCenterList().size())))));
                }

                if (!diagnosticCenterResponse.getMessage().getStatus()) {
                    binding.messageTextView.setVisibility(View.VISIBLE);
                    binding.messageTextView.setText("" + diagnosticCenterResponse.getMessage().getMessage());
                }

            } else {
                binding.messageTextView.setVisibility(View.VISIBLE);
                binding.messageTextView.setText("Sorry, data not available");
            }
        });
    }

    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
    }

    private void searchDiagnosticCenter(String query) {
        if (!query.isEmpty()) {
            List<DiagnosticCenter> filteredCenters = new ArrayList<>();
            filteredCenters = diagnosticCenters.stream().filter(diagnosticCenter -> (
                    diagnosticCenter.getName().toLowerCase().contains(query.toLowerCase()) || diagnosticCenter.getAddress().toLowerCase().contains(query.toLowerCase()) || diagnosticCenter.getPincode().contains(query)
            )).collect(Collectors.toList());
            if (diagnosticCenters.isEmpty()) {
                //no diagnostic center found

            } else {
                adapter = new DiagnosticCenterAdapter(requireContext(), filteredCenters, this);
                binding.hospitalRecyclerView.setAdapter(adapter);
                //change count to filtered list

                binding.tvDiagCount.setText(Html.fromHtml(String.format("Total <b> <font color='black'> %s </font> </b> Diagnostic Center", UtilMethods.PriceFormat(String.valueOf(filteredCenters.size())))));

            }
        } else {
            adapter = new DiagnosticCenterAdapter(requireContext(), diagnosticCenters, this);
            binding.hospitalRecyclerView.setAdapter(adapter);
            binding.tvDiagCount.setText(Html.fromHtml(String.format("Total <b> <font color='black'> %s </font> </b> Diagnostic Center", UtilMethods.PriceFormat(String.valueOf(diagnosticCenters.size())))));

        }
    }

    @Override
    public void onDiagnosticSelected(DiagnosticCenter diagnosticCenter) {

        NavDirections action = DiagnosticFragmentDirections.actionDiagnosticFragmentToPrefDateFragment(packages, person, diagnosticCenter, oldApptInfoSrNo).setIsReschedule(isReschedule).setIsPaid(isPaid);
        navController.navigate(action);

    }
}