package com.pinc.android.MB360.insurance.claims.repository;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.viewpager2.widget.ViewPager2;

import com.pinc.android.MB360.databinding.FragmentClaimsBinding;
import com.pinc.android.MB360.insurance.claims.repository.ui.ClaimIntimationListener;
import com.pinc.android.MB360.insurance.claims.repository.ui.IntimationViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class ClaimsFragment extends Fragment implements ClaimIntimationListener {

    FragmentClaimsBinding binding;
    View view;
    NavController navController;
    //initially to get the employee serial number form selected policy
    String PRODUCT_CODE = "GMC";
    IntimationViewPagerAdapter viewPagerAdapter;


    public ClaimsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentClaimsBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //viewModel scoped in the fragment.

        //ViewPager
        setupViewPager();

        return view;
    }

    private void setupViewPager() {
        viewPagerAdapter = new IntimationViewPagerAdapter(getChildFragmentManager(), getLifecycle(), this);

        binding.intimateClaimsViewPager.setAdapter(viewPagerAdapter);
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("Intimated Claim"));
        binding.tabs.viewPagerTabs.addTab(binding.tabs.viewPagerTabs.newTab().setText("Intimate Now"));

        binding.tabs.viewPagerTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.intimateClaimsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.intimateClaimsViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.tabs.viewPagerTabs.selectTab(binding.tabs.viewPagerTabs.getTabAt(position));
            }
        });


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onClaimIntimatedListener() {
        //need to refresh the claims.
        binding.tabs.viewPagerTabs.selectTab(binding.tabs.viewPagerTabs.getTabAt(0));

    }



}