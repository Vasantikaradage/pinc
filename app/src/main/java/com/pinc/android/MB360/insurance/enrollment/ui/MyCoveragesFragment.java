package com.pinc.android.MB360.insurance.enrollment.ui;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentMyCoveragesBinding;
import com.pinc.android.MB360.insurance.enrollment.interfaces.ToolTipListener;
import com.pinc.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;
import com.pinc.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;
import com.pinc.android.MB360.insurance.enrollment.adapters.EnrollmentCoveragesAdapter;
import com.skydoves.balloon.ArrowPositionRules;
import com.skydoves.balloon.Balloon;
import com.skydoves.balloon.BalloonAnimation;
import com.skydoves.balloon.BalloonSizeSpec;


public class MyCoveragesFragment extends Fragment implements ToolTipListener {

    FragmentMyCoveragesBinding binding;
    View view;
    EnrollmentViewModel enrollmentViewModel;
    EnrollmentCoveragesAdapter adapter;
    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;


    public MyCoveragesFragment() {
        // Required empty public constructor
    }

    public MyCoveragesFragment(ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper) {
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
        binding = FragmentMyCoveragesBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        enrollmentViewModel = new ViewModelProvider(requireActivity()).get(EnrollmentViewModel.class);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //to hide summary option
        viewPagerNavigationMenuHelper.hideSummaryOption();


    }


    private void getCoverages() {
        enrollmentViewModel.getCoverages();

        enrollmentViewModel.getCoveragesData().observe(getViewLifecycleOwner(), coverages -> {
            if (coverages != null) {
                adapter = new EnrollmentCoveragesAdapter(requireContext(), coverages.getCoverages(), this);
                binding.coveragesCycle.setAdapter(adapter);
            }
        });
    }

    @Override
    public void OnToolTipListener(String text, View view, ViewGroup rootView) {
        Typeface typeface = getResources().getFont(R.font.poppinssemibold);

        Balloon balloon = new Balloon.Builder(requireContext())
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setText(text)
                .setTextTypeface(typeface)
                .setTextColorResource(R.color.white)
                .setTextSize(15f)
                .setTextGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setArrowSize(10)
                .setArrowPosition(0.5f)
                .setPadding(8)
                .setCornerRadius(8f)
                .setBackgroundColorResource(R.color.black)
                .setLifecycleOwner(getViewLifecycleOwner())
                .setBalloonAnimation(BalloonAnimation.FADE)
                .build();
        balloon.showAlignTop(view);
    }

    @Override
    public void onResume() {
        super.onResume();
       /* viewPagerNavigationMenuHelper.hideSummaryOption();
        viewPagerNavigationMenuHelper.showHomeButton();*/

        binding.coveragesCycle.setItemAnimator(new DefaultItemAnimator());
        getCoverages();
    }
}