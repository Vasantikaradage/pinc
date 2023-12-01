package com.pinc.android.MB360.insurance.enrollment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentEnrollmentHomeBinding;
import com.pinc.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;
import com.pinc.android.MB360.insurance.enrollment.ui.EnrollmentViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class EnrollmentHomeFragment extends Fragment implements ViewPagerNavigationMenuHelper {

    FragmentEnrollmentHomeBinding binding;
    View view;
    EnrollmentViewPagerAdapter adapter;
    NavController navController;
    int counterPageScroll = 0;
    boolean isLastPageSwiped = false;

    //tabs item
    int[] drawableItems = new int[]{
            R.drawable.ic_intro_icon,
            R.drawable.ic_instructions_icon,
            R.drawable.ic_coverage_icon,
            R.drawable.ic_employee_icon,
            R.drawable.ic_dependants_icon,
            R.drawable.ic_parents_icon,
            R.drawable.ic_gmc_icon,
            R.drawable.ic_gpa_icon,
            R.drawable.ic_gtl_icon,
            R.drawable.ic_hc_icon_1,
            R.drawable.ic_male_employee,
            R.drawable.daughter2
    };

    public EnrollmentHomeFragment() {
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
        binding = FragmentEnrollmentHomeBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        adapter = new EnrollmentViewPagerAdapter(getChildFragmentManager(), getLifecycle(), this);


        //to navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        ImageView[] tabOne = new ImageView[adapter.getItemCount()];
        binding.enrollmentViewPager.setAdapter(adapter);


        //on home clicked (go-back)
        binding.exitEnrollment.setOnClickListener(v -> {
            //navigation to back
            requireActivity().onBackPressed();
        });

        binding.summaryEnrollment.setOnClickListener(v -> {
            //navigate to summary screen
            NavDirections actions = EnrollmentHomeFragmentDirections.actionEnrollmentHomeFragmentToEnrollmentSummaryFragment();
            navController.navigate(actions);
        });


        //Tab items
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(binding.tabitems, binding.enrollmentViewPager, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                tabOne[position] = (ImageView) LayoutInflater.from(requireContext()).inflate(R.layout.tab_icons, null);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        100, 100);
                tabOne[position].setImageResource(drawableItems[position]);
                tabOne[position].setLayoutParams(layoutParams);
                tabOne[position].setScaleType(ImageView.ScaleType.FIT_CENTER);
                // changing the tab names to  be done here
                // tab.setText("Tab " + (position + 1));
                tab.setCustomView(tabOne[position]);

            }
        });
        //attaching the tab-layout to view pager.
        tabLayoutMediator.attach();

        //previous page
        binding.prevPage.setOnClickListener(v -> {
            //previous page
            binding.enrollmentViewPager.setCurrentItem(binding.enrollmentViewPager.getCurrentItem() - 1, true);
        });

        //next page
        binding.nextPage.setOnClickListener(v -> {
            //next page
            if (adapter.getItemCount() - 1 == binding.enrollmentViewPager.getCurrentItem()) {
                //navigate to summary screen
                NavDirections actions = EnrollmentHomeFragmentDirections.actionEnrollmentHomeFragmentToEnrollmentSummaryFragment();
                navController.navigate(actions);
            } else {
                binding.enrollmentViewPager.setCurrentItem(binding.enrollmentViewPager.getCurrentItem() + 1, true);
            }
        });


        binding.enrollmentViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                showNavigationButtons();
                switch (position) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        binding.exitEnrollment.setClickable(true);
                        binding.summaryEnrollment.setClickable(true);
                        binding.nextPage.setClickable(true);
                        binding.prevPage.setClickable(true);
                        onShowMenu();
                        binding.nextPage.setVisibility(View.VISIBLE);
                        binding.prevPage.setVisibility(View.VISIBLE);
                        hideSummaryOption();
                        showHomeButton();
                        break;

                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                        binding.exitEnrollment.setClickable(true);
                        binding.summaryEnrollment.setClickable(true);
                        binding.nextPage.setClickable(true);
                        binding.prevPage.setClickable(true);
                        binding.nextPage.setVisibility(View.VISIBLE);
                        binding.prevPage.setVisibility(View.VISIBLE);
                        onShowMenu();
                        showSummaryOption();
                        showHomeButton();
                        break;
                 /*   case 9:
                        binding.exitEnrollment.setClickable(false);
                        binding.summaryEnrollment.setClickable(false);
                        binding.nextPage.setClickable(false);
                        binding.prevPage.setClickable(false);
                        onShowMenu();
                        hideNavigationButtons();
                        hideSummaryOption();
                        hideHomeButton();
                        break;*/
                    default:
                        binding.exitEnrollment.setClickable(true);
                        binding.summaryEnrollment.setClickable(true);
                        binding.nextPage.setClickable(true);
                        binding.prevPage.setClickable(true);
                        onShowMenu();
                        showHomeButton();
                        showNavigationButtons();
                }


            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
            }
        });


        return view;
    }

    public void showNavigationButtons() {
        if (binding.tabitems.getVisibility() == View.GONE) {
            Animation animShow = AnimationUtils.loadAnimation(requireContext(), R.anim.view_show);
            binding.tabitems.startAnimation(animShow);

            binding.prevPage.startAnimation(animShow);
            binding.nextPage.startAnimation(animShow);
            binding.tabitems.setVisibility(View.VISIBLE);
            binding.prevPage.setVisibility(View.VISIBLE);
            binding.nextPage.setVisibility(View.VISIBLE);
        }
    }

    public void hideNavigationButtons() {

        Animation animShow = AnimationUtils.loadAnimation(requireContext(), R.anim.view_hide);
        if (binding.tabitems.getVisibility() == View.VISIBLE) {
            binding.tabitems.startAnimation(animShow);
            binding.nextPage.startAnimation(animShow);
            binding.prevPage.startAnimation(animShow);

            binding.tabitems.setVisibility(View.GONE);
            binding.prevPage.setVisibility(View.GONE);
            binding.nextPage.setVisibility(View.GONE);
        }


    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onShowMenu() {
        showNavigationButtons();
    }

    @Override
    public void onHideMenu() {
        hideNavigationButtons();
    }

    @Override
    public void showSummaryOption() {
        binding.summaryEnrollment.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSummaryOption() {
        binding.summaryEnrollment.setVisibility(View.GONE);
    }

    @Override
    public void showHomeButton() {
        binding.exitEnrollment.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideHomeButton() {
        binding.exitEnrollment.setVisibility(View.GONE);
    }

    @Override
    public void nextPage() {
        binding.enrollmentViewPager.setCurrentItem(binding.enrollmentViewPager.getCurrentItem() + 1);
    }

    @Override
    public void previousPage() {
        binding.enrollmentViewPager.setCurrentItem(binding.enrollmentViewPager.getCurrentItem() - 1);
    }

    @Override
    public void onResume() {
        super.onResume();
        isLastPageSwiped = false;
    }
}