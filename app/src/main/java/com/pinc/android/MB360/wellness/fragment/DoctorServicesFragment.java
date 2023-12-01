package com.pinc.android.MB360.wellness.fragment;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aminography.primecalendar.PrimeCalendar;
import com.aminography.primecalendar.civil.CivilCalendar;
import com.aminography.primedatepicker.picker.PrimeDatePicker;
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback;
import com.aminography.primedatepicker.picker.theme.LightThemeFactory;
import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentDoctorServicesBinding;
import com.pinc.android.MB360.databinding.FragmentLongTermNursingBinding;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.monthpicker.DatePickerTheme;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.Appointment;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.PackageDS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;


public class DoctorServicesFragment extends Fragment {

    FragmentDoctorServicesBinding binding;
    View view;
    NavController navController;

    // View Model
    HomeHealthCareViewModel homeHealthCareViewModel;
    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;

    ArrayList<PackageDS> packagesList;

    PackageDS appointment = new PackageDS();
    Appointment appointmentRequest = new Appointment();

    Calendar calendar;
    PrimeCalendar Date = new CivilCalendar();
    ArrayList<PrimeCalendar> dailDateList = new ArrayList<>();

    public static String SERVICE_COUNT_ONE = "1";

    public DoctorServicesFragment() {
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
        binding = FragmentDoctorServicesBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();

        //viewModel scoped in the fragment.
        homeHealthCareViewModel = new ViewModelProvider(requireActivity()).get(HomeHealthCareViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);

        appointment.setHhcCityMappSrNo((homeHealthCareViewModel.getSelectedCity().getValue().getSrno()) == null ? "2" : homeHealthCareViewModel.getSelectedCity().getValue().getSrno());

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

        // to disable sundays as that library doesn't supports sundays disable;
        PrimeCalendar sunday;
        List<PrimeCalendar> weekends = new ArrayList<>();
        int weeks = 1000;
        for (int i = 0; i < (weeks * 7); i = i + 7) {
            sunday = new CivilCalendar();
            sunday.add(Calendar.DAY_OF_YEAR, (Calendar.SUNDAY - sunday.get(Calendar.DAY_OF_WEEK) + 7 + i));
            weekends.add(sunday);
        }

        //category
        binding.mbbsTV.setOnClickListener(v -> {

            binding.mbbsTV.setSelected(true);

            appointment.setCategory("M.B.B.S / Physician");
            appointment.setHhcPkgPricing(SERVICE_COUNT_ONE);

            binding.mainDailyDateLayout.setVisibility(View.VISIBLE);

            //filter package
            getPackage();
        });


        //calendars
        binding.dailyDateLayout.setOnClickListener(v -> {

            Calendar calendar = Calendar.getInstance();
            SingleDayPickCallback callback = fromDate -> {

                Date = fromDate.clone();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                StringBuilder dateString = new StringBuilder();
                StringBuilder displayDateBuilder = new StringBuilder();
                for (PrimeCalendar date : dailDateList) {
                    dateString.append(dateFormat.format(date.getTime())).append(",");
                    displayDateBuilder.append(dateFormat.format(date.getTime())).append("\n");
                }
                String date = dateString.toString().replaceFirst(".$", "");

//                appointmentRequest.setCount(String.valueOf(dailDateList.size()));

                binding.txtDate1.setText(dateFormat.format(Date.getTime()));
                appointmentRequest.setDate_preference(binding.txtDate1.getText().toString());
                //filter package
                getPackage();


            };
            PrimeCalendar today = new CivilCalendar();
            PrimeCalendar tomorrow = new CivilCalendar();
            tomorrow.add(Calendar.DATE, 1);


            LightThemeFactory themeFactory = DatePickerTheme.Companion.getTheme();

            PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(today)
                    .pickSingleDay(callback)
                    .initiallyPickedSingleDay(Date)
                    .disabledDays(weekends)
                    .minPossibleDate(today)
                    .applyTheme(themeFactory)
                    .build();

            datePicker.show(requireActivity().getSupportFragmentManager(), "SOME_TAG");
//
//
//


        });
        return view;
    }

    private void getPackage() {
        List<PackageDS> packageDSFiltered = new ArrayList<>();

        packageDSFiltered = packagesList.stream().filter(naPackageDS ->
                (naPackageDS.getHhcPkgPricing().equals(appointment.getHhcPkgPricing())
                )).collect(Collectors.toList());

        if (!packageDSFiltered.isEmpty() && !binding.txtDate1.getText().toString().isEmpty()) {
            appointmentRequest.setPackage_price_sr_no(Integer.parseInt(packageDSFiltered.get(0).getHhcPkgPricing()));
            appointmentRequest.setPrice(packagesList.get(0).getPkgPriceMb());

            appointment.setHhcPkgPricing(packageDSFiltered.get(0).getHhcPkgPricing());
            appointment.setPkgPriceMb(packageDSFiltered.get(0).getPkgPriceMb());

            Log.d(LogTags.DOCTOR_SERVICES, "SelectedPackage: " + packageDSFiltered.get(0));


            homeHealthCareViewModel.setAppointmentRequest(appointmentRequest);
            homeHealthCareViewModel.setSelectedDoctorServices(packageDSFiltered.get(0));

            binding.btnReview.setEnabled(true);
            binding.btnReview.setClickable(true);
            binding.btnReview.setFocusable(true);
            binding.btnReview.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.greenlightbg2));
            binding.btnReview.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare));

        } else {
            appointmentRequest.setPrice("0");
            appointmentRequest.setTotal_price("0");
            binding.btnReview.setEnabled(false);
            binding.btnReview.setClickable(false);
            binding.btnReview.setFocusable(false);
            binding.btnReview.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey1));
            binding.btnReview.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.roundedsquare));
        }
        Log.d(LogTags.DOCTOR_SERVICES, "USER-APPOINTMENT: " + appointment);
        Log.d(LogTags.DOCTOR_SERVICES, "REQUEST: " + appointmentRequest);


        binding.btnReview.setOnClickListener(v -> {
            homeHealthCareViewModel.setSelectedDoctorServices(appointment);
            homeHealthCareViewModel.setAppointmentRequest(appointmentRequest);


            //navigate
            NavDirections actions = DoctorServicesFragmentDirections.actionDoctorServicesFragmentToHomeHealthSummaryFragment();
            navController.navigate(actions);
        });

    }

    private void getNAPackages() {
        homeHealthCareViewModel.getPackagesDS().observe(getViewLifecycleOwner(), packages -> {
            packagesList = (ArrayList<PackageDS>) packages.getPackages();
        });
    }
}