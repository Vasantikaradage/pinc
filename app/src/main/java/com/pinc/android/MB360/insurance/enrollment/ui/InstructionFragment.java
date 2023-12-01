package com.pinc.android.MB360.insurance.enrollment.ui;



import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentInstructionBinding;
import com.pinc.android.MB360.insurance.enrollment.adapters.InstructionAdapter;
import com.pinc.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;
import com.pinc.android.MB360.insurance.enrollment.repository.EnrollmentViewModel;



public class InstructionFragment extends Fragment {

    FragmentInstructionBinding binding;
    View view;
    EnrollmentViewModel enrollmentViewModel;
    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;


    public InstructionFragment() {
        // Required empty public constructor
    }

    public InstructionFragment(ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper) {
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
        binding = FragmentInstructionBinding.inflate(inflater, container, false);
        view = binding.getRoot();


        //view-model
        enrollmentViewModel = new ViewModelProvider(requireActivity()).get(EnrollmentViewModel.class);

        //to hide summary option
        viewPagerNavigationMenuHelper.hideSummaryOption();

        //introduction-overlay
        /**
         * if user arrives in enrollment for the first time
         then the overlay should display, else overlay must be
         disabled.
         */

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(requireActivity().getPackageName(), Context.MODE_PRIVATE);
        boolean enrollmentArrival = sharedPreferences.getBoolean(getString(R.string.instructions), false);

        if (enrollmentArrival) {
            binding.introductionOverlay.getRoot().setVisibility(View.GONE);
        } else {
            binding.introductionOverlay.getRoot().setVisibility(View.VISIBLE);
        }

        Animation animation = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_left);

        binding.introductionOverlay.frntImg.startAnimation(animation);


        binding.introductionOverlay.btnCloseOverlay.setOnClickListener(v -> {
            SharedPreferences settings = requireActivity().getSharedPreferences(requireActivity().getPackageName(), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();

            // Store the introduction state  to SharedPreferences
            //setting to true (user has clicked the overlay)
            editor.putBoolean(getString(R.string.instructions), true);
            editor.apply();

            binding.introductionOverlay.getRoot().setVisibility(View.GONE);

            editor.putBoolean(getString(R.string.instructions), false);
            editor.apply();
        });


        enrollmentViewModel.getInstructionsData().observe(getViewLifecycleOwner(), instructionResponse -> {
            InstructionAdapter adapter = new InstructionAdapter(requireContext(), instructionResponse.getInstructions(),
                    instructionResponse.getDataToShowFor() != null ?
                            instructionResponse.getDataToShowFor().isEmpty() ?
                                    "" : instructionResponse.getDataToShowFor() : "");
            binding.instructionCycle.setAdapter(adapter);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
      /*  viewPagerNavigationMenuHelper.hideSummaryOption();
        viewPagerNavigationMenuHelper.showHomeButton();*/
    }
}