package com.pinc.android.MB360.insurance.myclaims.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentMyClaimsBinding;
import com.pinc.android.MB360.insurance.myclaims.repository.MyClaimsViewModel;
import com.pinc.android.MB360.insurance.myclaims.responseclass.Claims;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.utilities.AppServerConstants;


public class MyClaimsFragment extends Fragment implements OnClaimSelected {

    FragmentMyClaimsBinding binding;
    View view;

    MyClaimsViewModel claimsViewModel;
    LoadSessionViewModel loadSessionViewModel;
    MyClaimsAdapter adapter;
    NavController navController;

    public MyClaimsFragment() {
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
        binding = FragmentMyClaimsBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        claimsViewModel = new ViewModelProvider(requireActivity()).get(MyClaimsViewModel.class);

        //to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();


        getClaims();


        claimsViewModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });


        return view;

    }


    private void getClaims() {
        claimsViewModel.getMyClaimsData().observe(getViewLifecycleOwner(), claims -> {
            if (claims != null) {
                if (claims.getStatus().equalsIgnoreCase(AppServerConstants.SUCCESS)) {

                } else {
                    //something happened in the server and the status is false
                    binding.emptyClaimsLayout.setVisibility(View.VISIBLE);
                    binding.noClaimsFoundText.setText(getString(R.string.something_went_wrong));
                }

                if (claims.getClaimsInformation().getClaims().isEmpty()) {
                    binding.emptyClaimsLayout.setVisibility(View.VISIBLE);
                    binding.noClaimsFoundText.setText(getString(R.string.no_claims_reported));
                } else {
                    adapter = new MyClaimsAdapter(claims.getClaimsInformation().getClaims(), requireContext(), this);
                    binding.myClaimsCycle.setAdapter(adapter);
                }
            } else {
                //claims returned as null most probably some error happened within the response
                binding.emptyClaimsLayout.setVisibility(View.VISIBLE);
                binding.noClaimsFoundText.setText(claimsViewModel.getErrorDescription().getValue() == null ? getString(R.string.something_went_wrong) : claimsViewModel.getErrorDescription().getValue())
                ;
            }
        });
    }


    @Override
    public void onClaimSelected(Claims claims) {

        claimsViewModel.setSelectedClaim(claims);
        NavDirections actions = MyClaimsFragmentDirections.actionMyClaimsFragmentToMyClaimsdetails(String.valueOf(claims.getClaimSrNo()));
        navController.navigate(actions);
    }
}