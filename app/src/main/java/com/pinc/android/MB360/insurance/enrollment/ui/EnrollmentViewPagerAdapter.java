package com.pinc.android.MB360.insurance.enrollment.ui;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.pinc.android.MB360.insurance.enrollment.interfaces.ViewPagerNavigationMenuHelper;

public class EnrollmentViewPagerAdapter extends FragmentStateAdapter {
    ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper;

    public EnrollmentViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, ViewPagerNavigationMenuHelper viewPagerNavigationMenuHelper) {
        super(fragmentManager, lifecycle);
        this.viewPagerNavigationMenuHelper = viewPagerNavigationMenuHelper;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new EnrollmentWelcomeFragment(viewPagerNavigationMenuHelper);
            case 1:
                return new InstructionFragment(viewPagerNavigationMenuHelper);
            case 2:
                return new MyCoveragesFragment(viewPagerNavigationMenuHelper);
            case 3:
                return new EmployeeDetailsFragment(viewPagerNavigationMenuHelper);
            case 4:
                return new DependantDetailsFragment(viewPagerNavigationMenuHelper);
            case 5:
                return new ParentalDetailsFragment(viewPagerNavigationMenuHelper);
            case 6:
                return new TopUpFragment(viewPagerNavigationMenuHelper, "GMC");
            case 7:
                return new TopUpFragment(viewPagerNavigationMenuHelper, "GPA");
            case 8:
                return new TopUpFragment(viewPagerNavigationMenuHelper, "GTL");
          /*  case 9:
                return new EnrollmentSummaryFragment(viewPagerNavigationMenuHelper);*/
            default:
                //error due to the viewpager postion
                return null;
        }

    }

    @Override
    public int getItemCount() {
        return 9;
    }
}
