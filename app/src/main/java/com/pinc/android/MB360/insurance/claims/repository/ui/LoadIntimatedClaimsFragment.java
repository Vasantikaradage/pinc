package com.pinc.android.MB360.insurance.claims.repository.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentLoadIntimatedClaimsBinding;
import com.pinc.android.MB360.insurance.claims.repository.ClaimsViewModel;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.insurance.repository.responseclass.GroupGMCPolicyEmployeeDatum;
import com.pinc.android.MB360.insurance.repository.responseclass.GroupGPAPolicyEmployeeDatum;
import com.pinc.android.MB360.insurance.repository.responseclass.GroupGTLPolicyEmployeeDatum;
import com.pinc.android.MB360.insurance.repository.selectedPolicyRepo.SelectedPolicyViewModel;


public class LoadIntimatedClaimsFragment extends Fragment {

    FragmentLoadIntimatedClaimsBinding binding;
    View view;
    LoadSessionViewModel loadSessionViewModel;
    SelectedPolicyViewModel selectedPolicyViewModel;
    ClaimsViewModel claimsViewModel;
    ClaimAdapter adapter;
    String PRODUCT_CODE = "GMC";


    public LoadIntimatedClaimsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        getClaims();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLoadIntimatedClaimsBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        selectedPolicyViewModel = new ViewModelProvider(requireActivity()).get(SelectedPolicyViewModel.class);
        claimsViewModel = new ViewModelProvider(this).get(ClaimsViewModel.class);


        claimsViewModel.getLoadingState().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                showLoading();
                binding.refreshClaims.setRefreshing(true);
            } else {
                hideLoading();
                binding.refreshClaims.setRefreshing(false);
            }
        });

        binding.refreshClaims.setOnRefreshListener(() -> getClaims());

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getClaims();
    }

    private void getClaims() {
        //to get the  claims data we need some parameters from load session values


        loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {
            try {

                PRODUCT_CODE = "GMC";
                String employeeSrNo = "";
                String groupChildSrvNo = "";
                String oeGrpBasInfSrNo = "";

                GroupGMCPolicyEmployeeDatum groupGMCPolicyEmployeeDatum;
                groupGMCPolicyEmployeeDatum = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0);

                //queries for claims
                employeeSrNo = groupGMCPolicyEmployeeDatum.getEmployeeSrNo();
                groupChildSrvNo = groupGMCPolicyEmployeeDatum.getGroupchildsrno();
                oeGrpBasInfSrNo = groupGMCPolicyEmployeeDatum.getOeGrpBasInfSrNo();


                claimsViewModel.getClaims(employeeSrNo, groupChildSrvNo, oeGrpBasInfSrNo);
            } catch (Exception e) {
                e.printStackTrace();
            }

        });


        claimsViewModel.getClaimsData().observe(getViewLifecycleOwner(), claimsResponse -> {

            if (claimsResponse.getClaimslist() != null) {

                if (claimsResponse.getClaimslist().isEmpty()) {
                    binding.errorLayout.setVisibility(View.VISIBLE);
                    binding.messageTextView.setText(getString(R.string.no_intimations_found));
                    binding.imgError.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.noclaim));
                } else {

                    adapter = new ClaimAdapter(requireContext(), claimsResponse.getClaimslist());
                    binding.claimsRecyclerView.setAdapter(adapter);
                    adapter.notifyItemRangeChanged(0, claimsResponse.getClaimslist().size());
                    binding.errorLayout.setVisibility(View.GONE);
                    binding.messageTextView.setText(getString(R.string.no_intimations_found));
                }
                if (!claimsResponse.getResult().getStatus()) {
                    binding.messageTextView.setVisibility(View.VISIBLE);
                    binding.messageTextView.setText("Error: " + claimsResponse.getResult().getMessage());
                    binding.errorLayout.setVisibility(View.VISIBLE);
                }
            } else {
                if (claimsResponse != null) {
                    binding.messageTextView.setVisibility(View.VISIBLE);
                    binding.messageTextView.setText(claimsResponse.getResult().getMessage());
                    binding.errorLayout.setVisibility(View.VISIBLE);

                } else {
                    binding.messageTextView.setVisibility(View.VISIBLE);
                    binding.messageTextView.setText(getString(R.string.something_went_wrong));
                    binding.errorLayout.setVisibility(View.VISIBLE);
                }
            }

        });
    }

    private void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
    }

    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

}