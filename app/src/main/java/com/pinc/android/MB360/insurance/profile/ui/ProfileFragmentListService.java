package com.pinc.android.MB360.insurance.profile.ui;

import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_EMAIL;
import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_LOGIN_ID;
import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_LOGIN_TYPE;
import static com.pinc.android.MB360.utilities.AppLocalConstant.AUTH_PHONE_NUMBER;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

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
import androidx.recyclerview.widget.DividerItemDecoration;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.pinc.android.MB360.BuildConfig;
import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentProfileListServiceBinding;
import com.pinc.android.MB360.insurance.HomeFragmentDirections;
import com.pinc.android.MB360.insurance.profile.repository.ProfileViewModel;
import com.pinc.android.MB360.insurance.profile.response.ProfileServiceModel;
import com.pinc.android.MB360.insurance.profile.ui.ProfileServiceAdapter;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.onboarding.SplashScreenActivity;
import com.pinc.android.MB360.utilities.UtilMethods;

import java.util.ArrayList;
import java.util.Objects;


public class ProfileFragmentListService extends Fragment implements ProfileServiceClickListener {
    FragmentProfileListServiceBinding binding;
    View view;
    LoadSessionViewModel loadSessionViewModel;
    ProfileViewModel profileViewModel;
    ProfileServiceAdapter adapter;
    NavController navController;
    Shimmer shimmer;
    ShimmerDrawable shimmerDrawable;

    public ProfileFragmentListService() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileListServiceBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        profileViewModel = new ViewModelProvider(requireActivity()).get(ProfileViewModel.class);

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();


        getProfile();


        //to view the profile
        binding.viewProfile.setOnClickListener(view -> {
            NavDirections actions = ProfileFragmentListServiceDirections.actionProfileFragmentListServiceToProfileFragment();
            navController.navigate(actions);
        });


        //to logout the user
        binding.profileLogout.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.smslayout);
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.nhborder));

            AppCompatTextView lblSMS = dialog.findViewById(R.id.lblSMS);
            final AppCompatEditText smsContact = dialog.findViewById(R.id.smsContact);
            AppCompatButton btnSubmit = dialog.findViewById(R.id.btnSubmit);
            AppCompatButton btnCancel = dialog.findViewById(R.id.btnCancel);
            AppCompatTextView lblSMSHeader = dialog.findViewById(R.id.lblSMSHeader);
            lblSMS.setText("PINC Insurance");
            lblSMSHeader.setText(R.string.logout_warning);
            btnSubmit.setText(getString(R.string.yes));
            btnCancel.setText(getString(R.string.no));
            smsContact.setVisibility(View.GONE);


            btnSubmit.setOnClickListener(v1 -> {
                UtilMethods.logout(requireActivity());
            });

            btnCancel.setOnClickListener(v2 -> {
                dialog.dismiss();
            });

            dialog.show();
        });

        //shimmer loader
        shimmer = new Shimmer.AlphaHighlightBuilder().setDuration(1000).setBaseAlpha(0.85f).setHighlightAlpha(0.6f).setDirection(Shimmer.Direction.LEFT_TO_RIGHT).setAutoStart(true).build();

        // This is the placeholder for the imageView
        shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);


        ;
        Log.d("DEBUG NAV", "onCreateView: " + navController.getCurrentDestination().getLabel());

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<ProfileServiceModel> menuList = new ArrayList<>();

        menuList.add(new ProfileServiceModel(ContextCompat.getDrawable(requireContext(), R.drawable.ic_my_coverage), requireContext().getString(R.string.my_coverages)));
        menuList.add(new ProfileServiceModel(ContextCompat.getDrawable(requireContext(), R.drawable.ic_my_claims), requireContext().getString(R.string.my_claims)));
        menuList.add(new ProfileServiceModel(ContextCompat.getDrawable(requireContext(), R.drawable.ic_intimate_claim), requireContext().getString(R.string.intimate_claims)));
        menuList.add(new ProfileServiceModel(ContextCompat.getDrawable(requireContext(), R.drawable.ic_provider_network), requireContext().getString(R.string.provider_network)));
        menuList.add(new ProfileServiceModel(ContextCompat.getDrawable(requireContext(), R.drawable.ic_my_query), requireContext().getString(R.string.my_queries)));
        menuList.add(new ProfileServiceModel(ContextCompat.getDrawable(requireContext(), R.drawable.ic_policy_features), requireContext().getString(R.string.policyfeatures)));
        menuList.add(new ProfileServiceModel(ContextCompat.getDrawable(requireContext(), R.drawable.ic_claim_procedures), requireContext().getString(R.string.claim_procedure)));
        menuList.add(new ProfileServiceModel(ContextCompat.getDrawable(requireContext(), R.drawable.ic_faq), requireContext().getString(R.string.faqs)));


        adapter = new ProfileServiceAdapter(requireContext(), menuList, this);

        binding.profileServiceCycle.setAdapter(adapter);


    }

    private void getProfile() {
        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {
            if (loadSessionResponse != null) {

                profileViewModel.getProfile(loadSessionResponse.getGroupInfoData().getGroupchildsrno(),
                        loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getOeGrpBasInfSrNo(),
                        loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeSrNo());

            }
        });

        profileViewModel.getProfileData().observe(getViewLifecycleOwner(), profileResponse -> {
            if (profileResponse != null) {
                Glide.with(requireContext())
                        .load(profileResponse.getUserProfileImageUrl())
                        .placeholder(shimmerDrawable)
                        .transform(new FitCenter(), new RoundedCorners(28))
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.userImage);

                binding.profileName.setText(profileResponse.getUserPersonalDetails().getEmployeeName());
                binding.profileDesignation.setText(profileResponse.getUserOfficialDetails().getDesignation());
                binding.appVersionName.setText(String.format("App Version: %s", BuildConfig.VERSION_NAME));
            }
        });


    }




    @Override
    public void onProfileMenuClicked(String profileMenu) {
        NavDirections actions;
        switch (profileMenu.toLowerCase()) {
            case "my coverages":
                actions = ProfileFragmentListServiceDirections.actionProfileFragmentListServiceToCoverages();
                navController.navigate(actions);
                break;
            case "my claims":
                actions = ProfileFragmentListServiceDirections.actionProfileFragmentListServiceToMyClaimsFragment();
                navController.navigate(actions);
                break;
            case "intimate claim":
                actions = ProfileFragmentListServiceDirections.actionProfileFragmentListServiceToClaimsFragment();
                navController.navigate(actions);
                break;
            case "provider network":
                actions = ProfileFragmentListServiceDirections.actionProfileFragmentListServiceToProviderNetwork();
                navController.navigate(actions);
                break;
            case "my queries":
                actions = ProfileFragmentListServiceDirections.actionProfileFragmentListServiceToMyQueriesFragment();
                navController.navigate(actions);
                break;
            case "policy features":
                actions = ProfileFragmentListServiceDirections.actionProfileFragmentListServiceToPolicyFeaturesFragment();
                navController.navigate(actions);
                break;
            case "claim procedure":
                actions = ProfileFragmentListServiceDirections.actionProfileFragmentListServiceToClaimsProcedureFragment();
                navController.navigate(actions);
                break;
            case "faqs":
                actions = ProfileFragmentListServiceDirections.actionProfileFragmentListServiceToFaqFragment();
                navController.navigate(actions);
                break;

        }
    }
}