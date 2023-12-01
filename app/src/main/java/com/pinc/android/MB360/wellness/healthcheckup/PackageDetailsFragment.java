package com.pinc.android.MB360.wellness.healthcheckup;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentPackageDetailsBinding;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.PackagesViewModel;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Packages;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.ui.PackageDetailsAdapter;


public class PackageDetailsFragment extends Fragment {

    FragmentPackageDetailsBinding binding;
    View view;
    NavController navController;
    Packages packages;

    //PackageLT ViewModel
    PackagesViewModel packagesViewModel;
    LoadSessionViewModel loadSessionViewModel;

    PackageDetailsAdapter adapter;

    public PackageDetailsFragment() {
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
        binding = FragmentPackageDetailsBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        // to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();

        //viewModel scoped in the fragment.
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        packagesViewModel = new ViewModelProvider(requireActivity()).get(PackagesViewModel.class);


        //getPackages() from parent fragment
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

        //here we get the packages
        packages = PackageDetailsFragmentArgs.fromBundle(getArguments()).getGetPackages();

        binding.packageNameTV.setText(""+packages.getPackageName());
        //also setup recyclerView in the same function
        getPackageDetails();
    }

    private void getPackageDetails() {
        //to get the  policyFeatures data we need some parameters from load session values

        loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {

            String packageSrNo = packages.getPackageSrNo();

            packagesViewModel.getPackageDetails(packageSrNo);
        });

        packagesViewModel.getPackageDetailsData().observe(getViewLifecycleOwner(), packageDetailsResponse -> {


            if (packageDetailsResponse != null) {
                adapter = new PackageDetailsAdapter(requireContext(), packageDetailsResponse.getSpecificationList());
                binding.packagedetailsRecyclerView.setAdapter(adapter);
                adapter.notifyItemRangeChanged(0, packageDetailsResponse.getSpecificationList().size());

                if (!packageDetailsResponse.getMessage().getStatus()) {
                    binding.messageTextView.setVisibility(View.VISIBLE);
                    binding.messageTextView.setText("" + packageDetailsResponse.getMessage().getMessage());
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

}