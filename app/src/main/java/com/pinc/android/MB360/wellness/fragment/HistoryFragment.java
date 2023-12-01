package com.pinc.android.MB360.wellness.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentHistoryBinding;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.PackagesViewModel;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.cancellationHC.OnCancellationHealthCheckup;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.rescheduleHC.OnRescheduleHealthCheckup;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.AppointmentHealthCheckup;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.HealthCheckupCancelAppointmentRequest;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Packages;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.responseclass.Person;
import com.pinc.android.MB360.wellness.healthcheckuppackages.repository.ui.SummaryHealthCheckupAdapter;
import com.pinc.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.cancellation.OnCancellation;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.ScheduledSummary;
import com.pinc.android.MB360.wellness.homehealthcare.retrofit.RescheduleInterface;
import com.pinc.android.MB360.wellness.homehealthcare.ui.SummaryAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class HistoryFragment extends Fragment implements OnCancellation, RescheduleInterface,
        OnCancellationHealthCheckup, OnRescheduleHealthCheckup {

    FragmentHistoryBinding binding;

    //change adapter regarding the page
    //adapters

    View view;

    ArrayList<AppointmentHealthCheckup> appointmentHealthCheckupList;

    //    View Models
    LoadSessionViewModel loadSessionViewModel;
    HomeHealthCareViewModel homeHealthCareViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;
    PackagesViewModel packagesViewModel;

    SummaryAdapter adapter;
    SummaryHealthCheckupAdapter adapter1;
    String extFamilySrNo = "";
    int canceled_appointment = 0;
    int total_summary = 0;

    NavController navController;

    Packages packages;
    Person person;

    HealthCheckupCancelAppointmentRequest healthCheckupCancelAppointmentRequest = new HealthCheckupCancelAppointmentRequest();

    String empIdNo;
    String groupCode;
    String familySrNo;
    String groupSrNo;

    ScheduledSummary scheduledSummary;


    public HistoryFragment() {
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
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        // View Models
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        homeHealthCareViewModel = new ViewModelProvider(requireActivity()).get(HomeHealthCareViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);
        packagesViewModel = new ViewModelProvider(requireActivity()).get(PackagesViewModel.class);

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();


        getSummary();

        homeHealthCareViewModel.getLoadingState().observe(getViewLifecycleOwner(), loading -> {
            if (loading) {
                binding.progressBar.setVisibility(View.VISIBLE);
            } else {
                binding.progressBar.setVisibility(View.GONE);
            }
        });

        loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {

            empIdNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeIdentificationNo();
            groupCode = loadSessionResponse.getGroupInfoData().getGroupcode();

            healthCheckupCancelAppointmentRequest.setEmpIdNo(empIdNo);

            dashboardWellnessViewModel.getEmployeeWellnessDetailsData().observe(getViewLifecycleOwner(), employeeCheckResponse -> {

                familySrNo = employeeCheckResponse.getExtFamilySrNo();
                groupSrNo = employeeCheckResponse.getExtGroupSrNo();

                healthCheckupCancelAppointmentRequest.setFamilySrNo(familySrNo);
                healthCheckupCancelAppointmentRequest.setGroupSrNo(groupSrNo);

                packagesViewModel.getOngoingAppointment(familySrNo, groupSrNo, empIdNo, groupCode).observe(getViewLifecycleOwner(), healthCheckupOngoingResponse -> {

                    String personSrNo = healthCheckupOngoingResponse.getAppointmentList().get(0).getPersonSrNo();

                    healthCheckupCancelAppointmentRequest.setPersonSrNo(personSrNo);
                    healthCheckupCancelAppointmentRequest.setRemark("");

                });

            });
        });

        return view;
    }

    private void getSummary() {
        if (homeHealthCareViewModel.getService().equalsIgnoreCase("health checkup")) {

            //todo himanshu health check up ongoing list

            loadSessionViewModel.getLoadSessionData().observe(getViewLifecycleOwner(), loadSessionResponse -> {

                empIdNo = loadSessionResponse.getGroupPoliciesEmployees().get(0).getGroupGMCPolicyEmployeeData().get(0).getEmployeeIdentificationNo();
                groupCode = loadSessionResponse.getGroupInfoData().getGroupcode();

                healthCheckupCancelAppointmentRequest.setEmpIdNo(empIdNo);

                dashboardWellnessViewModel.getEmployeeWellnessDetailsData().observe(getViewLifecycleOwner(), employeeCheckResponse -> {

                    familySrNo = employeeCheckResponse.getExtFamilySrNo();
                    groupSrNo = employeeCheckResponse.getExtGroupSrNo();

                    healthCheckupCancelAppointmentRequest.setFamilySrNo(familySrNo);
                    healthCheckupCancelAppointmentRequest.setGroupSrNo(groupSrNo);

                    packagesViewModel.getOngoingAppointment(familySrNo, groupSrNo, empIdNo, groupCode).observe(getViewLifecycleOwner(), healthCheckupOngoingResponse -> {
                        total_summary = healthCheckupOngoingResponse.getAppointmentList().size();
                        if (total_summary > 0) {

                            binding.ongoingCycle.setVisibility(View.VISIBLE);
                            binding.noHistoryImage.setVisibility(View.GONE);
                            adapter1 = new SummaryHealthCheckupAdapter(healthCheckupOngoingResponse.getAppointmentList(), requireContext(), this, this);
                            binding.ongoingCycle.setAdapter(adapter1);
                        }
                    });

                });
            });


        } else {
            dashboardWellnessViewModel.getEmployeeWellnessDetailsData().observe(getViewLifecycleOwner(), employeeCheckResponse -> {
                homeHealthCareViewModel.getSummaryFromServer(employeeCheckResponse.getExtFamilySrNo()).observe(getViewLifecycleOwner(), summaryResponse -> {
                    total_summary = summaryResponse.getScheduledSummary().size();
                    if (total_summary > 0) {
                        binding.ongoingCycle.setVisibility(View.VISIBLE);
                        binding.noHistoryImage.setVisibility(View.GONE);
                        adapter = new SummaryAdapter(summaryResponse.getScheduledSummary(), requireContext(), homeHealthCareViewModel.getService(), this, this);
                        binding.ongoingCycle.setAdapter(adapter);
                    } else {
                        binding.noHistoryImage.setVisibility(View.VISIBLE);
                        binding.ongoingCycle.setVisibility(View.GONE);
                    }
                });
            });
        }

    }

    @Override
    public void cancelAppointment(String ApptInfoSrNo, int position) {
        //this is for home health care appointment cancellation
        canceled_appointment++;

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_appt_cancellation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        TextView tvcancel = dialog.findViewById(R.id.tvcancel);
        ImageView iv_close = dialog.findViewById(R.id.iv_close);
        CardView btnschedule = dialog.findViewById(R.id.btnreschedule);

        tvcancel.setOnClickListener(v -> {
            homeHealthCareViewModel.cancelAppointment(ApptInfoSrNo).observe(getViewLifecycleOwner(), cancelResponse -> {
                if (cancelResponse.getApiStatus().getBoolStatus()) {
                    getSummary();
                    dialog.dismiss();
                } else {
                    Toast.makeText(requireContext(), "Cannot Cancel the Appointment!", Toast.LENGTH_SHORT).show();
                }
            });

        });

        btnschedule.setOnClickListener(v -> {
            dialog.dismiss();
            //reschedule here


        });

        iv_close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public void getAppointmentDetails(ScheduledSummary scheduledSummary) {

        //navigate
        NavDirections actions = OngoingFragmentDirections.actionOngoingFragmentToRescheduleFragment(scheduledSummary);
        navController.navigate(actions);
    }

    @Override
    public void cancelAppointmentHC(int position) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_appt_cancellation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        TextView tvcancel = dialog.findViewById(R.id.tvcancel);
        ImageView iv_close = dialog.findViewById(R.id.iv_close);
        CardView btnschedule = dialog.findViewById(R.id.btnreschedule);

        tvcancel.setOnClickListener(v -> {

            packagesViewModel.putCancelAppointment(healthCheckupCancelAppointmentRequest).observe(getViewLifecycleOwner(), cancel -> {

                if (cancel.getMessage().getStatus() != null && cancel.getMessage().getStatus()) {
                    dialog.dismiss();
                    //to update the recyclerview (health checkup adapter) adapter after cancellation
                    getSummary();
                    // packagesViewModel.resetCancelHC();
                }
            });
        });

        btnschedule.setOnClickListener(v -> {
            dialog.dismiss();
            //reschedule here

        });

        iv_close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }


    @Override
    public void resscheduleAppointmentHC(AppointmentHealthCheckup appointmentHealthCheckup) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_appt_reschedule);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        ImageView iv_close = dialog.findViewById(R.id.iv_close);
        Button btnreschedule = dialog.findViewById(R.id.btnreschedule);

        btnreschedule.setOnClickListener(v -> {
            dialog.dismiss();
            //reschedule here
            rescheduleHealthCheckUP(appointmentHealthCheckup);
        });

        iv_close.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }

    private void rescheduleHealthCheckUP(AppointmentHealthCheckup appointmentHealthCheckup /** from ongoing fragment (health check-Up) **/) {
        //here we reschedule the health check-up appointment
        packagesViewModel.getPackagesData().observe(getViewLifecycleOwner(), packageResponse -> {
            if (packageResponse != null) {
                List<Packages> healthPackages = packageResponse.getPackagesList();
                //this will hold our reschedule-able health package
                List<Packages> filteredPackage = new ArrayList<>();

                filteredPackage = healthPackages.stream().filter(selectedPackage -> (
                        selectedPackage.getPackageSrNo().equalsIgnoreCase(appointmentHealthCheckup.getPackageSrNo()))).collect(Collectors.toList());

                if (filteredPackage.isEmpty()) {
                    //here we got no package serial.
                    //show error
                } else {
                    //here we got the package
                    packages = filteredPackage.get(0);

                    List<Person> filteredPerson = new ArrayList<>();
                    filteredPerson = packages.getPersons().stream().filter(selectedPerson -> (
                            selectedPerson.getPersonSRNo() == Integer.parseInt(appointmentHealthCheckup.getPersonSrNo())
                    )).collect(Collectors.toList());

                    if (filteredPerson.isEmpty()) {
                        //here we got no person.
                        //show error
                    } else {
                        //we got the person
                        person = filteredPerson.get(0);
                    }

                    NavDirections action = OngoingFragmentDirections.actionOngoingFragmentToDiagnosticFragment(packages, person).setIsReschedule(true).setIsPaid(appointmentHealthCheckup.getPaymentFlag().equals("0") ? false : true ).setIsOldApptInfoSrNo(appointmentHealthCheckup.getApptSrNo());
                    navController.navigate(action);
                }

            } else {
                // here we get no packages so we can call fro packages here
                //todo call the packages again
            }
        });

    }



}