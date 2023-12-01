package com.pinc.android.MB360.wellness.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentElderCareBinding;
import com.pinc.android.MB360.databinding.FragmentLongTermNursingBinding;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.Appointment;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.PackageEC;
import com.pinc.android.MB360.wellness.homehealthcare.ui.ElderCarePackagesAdaptor;
import com.pinc.android.MB360.wellness.homehealthcare.ui.OnElderCarePackagerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ElderCareFragment extends Fragment implements OnElderCarePackagerListener {
    FragmentElderCareBinding binding;
    View view;
    NavController navController;

    // View Model
    HomeHealthCareViewModel homeHealthCareViewModel;
    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;

    ArrayList<PackageEC> packagesList;

    ElderCarePackagesAdaptor adaptor;

    PackageEC appointment = new PackageEC();
    Appointment appointmentRequest = new Appointment();

    public ElderCareFragment() {
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
        binding = FragmentElderCareBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();

        //viewModel scoped in the fragment.
        homeHealthCareViewModel = new ViewModelProvider(requireActivity()).get(HomeHealthCareViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);

       appointment.setHhcNaCityMappSrNo((homeHealthCareViewModel.getSelectedCity().getValue().getSrno()) == null ? "2" : homeHealthCareViewModel.getSelectedCity().getValue().getSrno());

//        appointment.setHhcNaCityMappSrNo("1");

        //common request attributes
        appointmentRequest.setIs_rescheduled(0);

        //get person and family sr no
        dashboardWellnessViewModel.getEmployeeWellnessDetailsData().observe(getViewLifecycleOwner(), employeeCheckResponse -> {
            appointmentRequest.setFamily_sr_no(employeeCheckResponse.getExtFamilySrNo());
        });

        homeHealthCareViewModel.getSelectedPerson().observe(getViewLifecycleOwner(), person -> {
            appointmentRequest.setPerson_sr_no(person.getExtPersonSrNo());
            binding.dependantName.setText(person.getPersonName());
            String personage = person.getRelationName() + "(" + person.getAge() + " years)";
            binding.txtRelationAge.setText(personage);
        });

        //setting the service name
        appointmentRequest.setService(homeHealthCareViewModel.getService());

        //get the common packages!
        getNAPackages();

        //get filtered packages


     /*   binding.txtPackage1.setText("HEALTH PRIME PLAN (ANNUAL)");
        binding.txtPackage2.setText("HEALTH PRIME PLUS PLAN (ANNUAL)");*/


        return view;
    }

    private void getPackage() {
        List<PackageEC> packageECFiltered = new ArrayList<>();

        packageECFiltered = packagesList.stream().filter(naPackageEC ->
                (naPackageEC.getHhcNaCityMappSrNo().equals(appointment.getHhcNaCityMappSrNo())
                )).collect(Collectors.toList());

        if (!packageECFiltered.isEmpty()) {
            adaptor = new ElderCarePackagesAdaptor(packageECFiltered, requireContext(), this);
            binding.elderCareCycle.setAdapter(adaptor);
            // Log.d(LogTags.ELDER_CARE, "SelectedPackage: " + packageECFiltered.get(0));
            //   homeHealthCareViewModel.setAppointmentRequest(appointmentRequest);
            //   homeHealthCareViewModel.setSelectedEldercare(packageECFiltered.get(0));


        } else {
            appointmentRequest.setPrice("0");
            appointmentRequest.setTotal_price("0");

        }
        Log.d(LogTags.ELDER_CARE, "USER-APPOINTMENT: " + appointment);
        Log.d(LogTags.ELDER_CARE, "REQUEST: " + appointmentRequest);


    }

    private void getNAPackages() {
        homeHealthCareViewModel.getPackagesEC().observe(getViewLifecycleOwner(), packages -> {
            packagesList = (ArrayList<PackageEC>) packages.getPackages();
            getPackage();
        });
    }

    @Override
    public void onElderPackageSelected(PackageEC packageEC) {
        //this package to buy
        Log.d(LogTags.ELDER_CARE, "onElderPackageSelected: " + packageEC.toString());
        //setting the appointment request
        /*,*/
        appointmentRequest.setPackage_price_sr_no(Integer.parseInt(packageEC.getHhcEcPkgPricingSrNo()));
        appointmentRequest.setPrice(packageEC.getPkgPriceMb());
        appointmentRequest.setTotal_price(packageEC.getPkgPriceMb());
        appointmentRequest.setCount("1");

        homeHealthCareViewModel.setAppointmentRequest(appointmentRequest);

        //open summary fragment for elder care
        homeHealthCareViewModel.setSelectedEldercare(packageEC);

        NavDirections actions = ElderCareFragmentDirections.actionElderCareFragmentToHomeHealthSummaryFragment();
        navController.navigate(actions);
    }
}