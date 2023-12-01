package com.pinc.android.MB360.wellness.healthcheckup;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentHealthCheckupBinding;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.utilities.BadgesDrawable;
import com.pinc.android.MB360.utilities.UtilMethods;
import com.pinc.android.MB360.wellness.WellnessDashBoardActivity;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.PackagesViewModel;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Packages;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Person;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.retrofit.PackageInterface;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.retrofit.PersonDelete;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.ui.PackageAdapter;


public class HealthCheckupFragment extends Fragment implements PackageInterface, PersonDelete {

    FragmentHealthCheckupBinding binding;
    View view;
    NavController navController;


    //PackageLT ViewModel
    PackagesViewModel packagesViewModel;
    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;

    PackageAdapter adapter;

    LayerDrawable icon;

    public HealthCheckupFragment() {
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
        binding = FragmentHealthCheckupBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //To Navigate

        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);

        navController = navHostFragment.getNavController();


        //viewModel scoped in the fragment.
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);
        packagesViewModel = new ViewModelProvider(requireActivity()).get(PackagesViewModel.class);

        // Overview
        binding.btnOverview.setOnClickListener(view -> {

            NavDirections actions = HealthCheckupFragmentDirections.actionHealthCheckupFragmentToOverviewFragment();
            navController.navigate(actions);
        });

        // Pre-Test-Request
        binding.btnPreTest.setOnClickListener(view -> {

            NavDirections actions = HealthCheckupFragmentDirections.actionHealthCheckupFragmentToPretestreqFragment();
            navController.navigate(actions);
        });

        // Terms&Condition
        binding.btnTandC.setOnClickListener(view -> {

            NavDirections actions = HealthCheckupFragmentDirections.actionHealthCheckupFragmentToTcFragment();
            navController.navigate(actions);
        });

        ((WellnessDashBoardActivity) getActivity()).getSupportActionBar().setLogo(null);
        ((WellnessDashBoardActivity) getActivity()).getSupportActionBar().setDisplayUseLogoEnabled(false);
        ((WellnessDashBoardActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        binding.rlcartlayout.setOnClickListener(view -> {

            NavDirections actions = HealthCheckupFragmentDirections.actionHealthCheckupFragmentToSummaryFragment();
            navController.navigate(actions);
        });


        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {
            dashboardWellnessViewModel.getEmployeeWellnessDetailsData().observe(getViewLifecycleOwner(), employeeCheckResponse -> {

                String familySrNo = employeeCheckResponse.getExtFamilySrNo();
                String groupCode = loadSessionResponse.getGroupInfoData().getGroupcode();

                packagesViewModel.getSummary(familySrNo, groupCode);

            });
        });

        setHasOptionsMenu(true);

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
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_wellness_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem item = menu.findItem(R.id.cartIT);
        icon = (LayerDrawable) item.getIcon();
//        set count on badge
        packagesViewModel.getSummaryData().observe(getViewLifecycleOwner(), summaryResponse -> {
            setBadgeCount(getContext(), icon, String.valueOf(summaryResponse.getSummary().getSelfSponseredPerson().size()));
        });

        getPackage();

    }

    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgesDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_cart);
        if (reuse instanceof BadgesDrawable) {
            badge = (BadgesDrawable) reuse;
        } else {
            badge = new BadgesDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_cart, badge);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.cartIT) {

            NavDirections actions = HealthCheckupFragmentDirections.actionHealthCheckupFragmentToSummaryFragment();
            navController.navigate(actions);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getPackage() {
        //to get the  policyFeatures data we need some parameters from load session values

        loadSessionViewModel.getLoadSessionData().observe(requireActivity(), loadSessionResponse -> {

            String groupCode = "" + loadSessionResponse.getGroupInfoData().getGroupcode();
            String empIdNo = "" + loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeIdentificationNo();

            dashboardWellnessViewModel.getEmployeeWellnessDetailsData().observe(requireActivity(), employeeCheckResponse -> {
                if (employeeCheckResponse.getExtGroupSrNo() != null) {
                    String extGroupSrno = employeeCheckResponse.getExtGroupSrNo();
                    packagesViewModel.getPackages(extGroupSrno, groupCode, empIdNo);
                } else {
                    Toast.makeText(getContext(), "package not available", Toast.LENGTH_SHORT).show();
                }
            });
        });

        packagesViewModel.getPackagesData().observe(getViewLifecycleOwner(), packageResponse -> {

            if (packageResponse != null) {
                adapter = new PackageAdapter(requireContext(), packageResponse.getPackagesList(), this, this);
                binding.packageRecyclerView.setAdapter(adapter);
                adapter.notifyItemRangeChanged(0, packageResponse.getPackagesList().size());

                if (!packageResponse.getMessage().getStatus()) {
                    binding.messageTextView.setVisibility(View.VISIBLE);
                    binding.messageTextView.setText("" + packageResponse.getMessage().getMessage());
                }

            } else {
                binding.messageTextView.setVisibility(View.VISIBLE);
                binding.messageTextView.setText("Sorry, data not available");
            }
        });

        packagesViewModel.getSummaryData().observe(getViewLifecycleOwner(), summaryResponse -> {


            if (summaryResponse != null) {

                binding.tvitemcount.setText(String.format("%s Items", UtilMethods.PriceFormat(String.valueOf(summaryResponse.getSummary().getSelfSponseredPerson().size()))));
                binding.tvtotalamount.setText("\u20B9 " + UtilMethods.PriceFormat(summaryResponse.getSummary().getYoupay()));

                // Badge Count
                setBadgeCount(getContext(), icon, String.valueOf(summaryResponse.getSummary().getSelfSponseredPerson().size()));
            }

        });


    }

    private void showLoading() {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        binding.progressBar.setVisibility(View.GONE);
    }

    @Override
    public void getPackageDetails(Packages packages) {
        NavDirections action = HealthCheckupFragmentDirections.actionHealthCheckupFragmentToPackageDetailsFragment(packages);
        navController.navigate(action);
    }

    @Override
    public void scheduleAppointment(Packages packages, Person person) {

        NavDirections action = HealthCheckupFragmentDirections.actionHealthCheckupFragmentToDiagnosticFragment(packages, person);
        navController.navigate(action);

    }

    @Override
    public void personDelete(int packagePosition, String personSrNo, int position, Person person) {
        //removing person from the list
        adapter.personRemoved(packagePosition, position, person);
        packagesViewModel.dependentDelete(personSrNo).observe(getViewLifecycleOwner(), messageResponseDependentDelete -> {
            if (messageResponseDependentDelete.getStatus()) {
                Toast.makeText(requireContext(), "" + messageResponseDependentDelete.getMessage(), Toast.LENGTH_SHORT).show();
                //refresh call
                // TODO: 7/20/2022 @HIMANSHU UPDATE THE VALUES

            } else {
                Toast.makeText(requireContext(), "" + messageResponseDependentDelete.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}