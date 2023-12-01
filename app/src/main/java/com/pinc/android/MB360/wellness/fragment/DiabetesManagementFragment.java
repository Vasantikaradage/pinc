package com.pinc.android.MB360.wellness.fragment;

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
import com.pinc.android.MB360.databinding.FragmentDiabetesManagementBinding;
import com.pinc.android.MB360.databinding.FragmentPhysiotherapyBinding;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.Appointment;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.PackageDM;

import java.util.ArrayList;


public class DiabetesManagementFragment extends Fragment {

    FragmentDiabetesManagementBinding binding;
    View view;
    NavController navController;

    // View Model
    HomeHealthCareViewModel homeHealthCareViewModel;
    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;

    ArrayList<PackageDM> packagesList;

    PackageDM appointment = new PackageDM();
    Appointment appointmentRequest = new Appointment();


    public DiabetesManagementFragment() {
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
        binding = FragmentDiabetesManagementBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();

        //viewModel scoped in the fragment.
        homeHealthCareViewModel = new ViewModelProvider(requireActivity()).get(HomeHealthCareViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);

//        appointment.setHhcNaCityMappSrNo((homeHealthCareViewModel.getSelectedCity().getValue().getSrno()) == null ? "2" : homeHealthCareViewModel.getSelectedCity().getValue().getSrno());

//        appointment.setHhcNaCityMappSrNo("1");



        //common request attributes
        appointmentRequest.setRej_appt_sr_no("-1");
        appointmentRequest.setIs_rescheduled(0);

        //initial date that changes later
        appointmentRequest.setFrom_date("01-01-1990");
        appointmentRequest.setTo_date("01-01-1990");
        appointmentRequest.setDate_preference("01-01-1990");

        //get person and familysr no
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




        return view;
    }

    private void getPackage() {
//        List<PackageDM> packageDMFiltered = new ArrayList<>();
//
//        packageDMFiltered = packagesList.stream().filter(naPackageDM ->
//                (naPackageDM.getPkgPriceMb().equals(appointment.getPkgPriceMb())
//                )).collect(Collectors.toList());
//
//        if (!packageDMFiltered.isEmpty()) {
//
//             Log.d(LogTags.DIABETES_MANAGEMENT_ACTIVITY, "SelectedPackage: " + packageDMFiltered.get(0));
//               homeHealthCareViewModel.setAppointmentRequest(appointmentRequest);
//               homeHealthCareViewModel.setSelectedDiabetesManagement(packageDMFiltered.get(0));
//               appointmentRequest.setPrice(packagesList.get(0).getPkgPriceMb());
//
//        } else {
////            appointmentRequest.setPrice("0");
////            appointmentRequest.setTotal_price("0");
//
//        }
//        Log.d(LogTags.DIABETES_MANAGEMENT_ACTIVITY, "USER-APPOINTMENT: " + appointment);
//        Log.d(LogTags.DIABETES_MANAGEMENT_ACTIVITY, "REQUEST: " + appointmentRequest);


        binding.btnSchedule.setOnClickListener(v -> {

            appointmentRequest.setPrice(packagesList.get(0).getPkgPriceMb());

            homeHealthCareViewModel.setSelectedDiabetesManagement(appointment);
            homeHealthCareViewModel.setAppointmentRequest(appointmentRequest);

            //navigate
            NavDirections actions = DiabetesManagementFragmentDirections.actionDiabetesManagementFragmentToHomeHealthSummaryFragment();
            navController.navigate(actions);

        });

    }

    private void getNAPackages() {
        homeHealthCareViewModel.getPackagesDM().observe(getViewLifecycleOwner(), packages -> {
            packagesList = (ArrayList<PackageDM>) packages.getPackages();
            getPackage();
        });
    }


}