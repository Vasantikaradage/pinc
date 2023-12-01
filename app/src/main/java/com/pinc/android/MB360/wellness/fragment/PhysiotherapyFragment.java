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
import com.pinc.android.MB360.databinding.FragmentLongTermNursingBinding;
import com.pinc.android.MB360.databinding.FragmentPhysiotherapyBinding;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.monthpicker.DatePickerTheme;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.Appointment;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.PackagePT;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class PhysiotherapyFragment extends Fragment {

    FragmentPhysiotherapyBinding binding;
    View view;
    NavController navController;

    // View Model
    HomeHealthCareViewModel homeHealthCareViewModel;
    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;

    ArrayList<PackagePT> packagesList;

    PackagePT appointment = new PackagePT();
    Appointment appointmentRequest = new Appointment();

    Calendar calendar;
    PrimeCalendar Date = new CivilCalendar();
    ArrayList<PrimeCalendar> dailDateList = new ArrayList<>();

    public static String SERVICE_COUNT_ONE = "1";
    public static String SERVICE_COUNT_TWO = "2";

    int DAY_COUNT = 10;

    public PhysiotherapyFragment() {
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
        binding = FragmentPhysiotherapyBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();

        //viewModel scoped in the fragment.
        homeHealthCareViewModel = new ViewModelProvider(requireActivity()).get(HomeHealthCareViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);

        //todo remove testing location
        appointment.setHhcCityMappSrNo((homeHealthCareViewModel.getSelectedCity().getValue().getSrno()) == null ? "2" : homeHealthCareViewModel.getSelectedCity().getValue().getSrno());

//        appointment.setHhcCityMappSrNo("1");

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

        //per Day
        binding.perDay.setOnClickListener(v -> {

            binding.perDay.setSelected(true);
            binding.tenDays.setSelected(false);

            binding.llMonthSelection.setVisibility(View.VISIBLE);

            binding.dailyDateLayout.setVisibility(View.VISIBLE);
            binding.txtDate1.setVisibility(View.VISIBLE);
            binding.dateRange.setVisibility(View.GONE);

            binding.txtToDate.setText("");
            binding.txtFromDate.setText("");
            binding.txtDate1.setText("");

            appointment.setCategory("PER DAY");
            appointment.setHhcPkgPricing(SERVICE_COUNT_ONE);

            // Date Condition
            appointmentRequest.setDate_condition(1);

            //filter package
            getPackage();
        });

        //ten Day
        binding.tenDays.setOnClickListener(v -> {

            binding.perDay.setSelected(false);
            binding.tenDays.setSelected(true);

            binding.llMonthSelection.setVisibility(View.VISIBLE);

            binding.dailyDateLayout.setVisibility(View.GONE);
            binding.txtDate1.setVisibility(View.GONE);
            binding.dateRange.setVisibility(View.VISIBLE);

            binding.txtToDate.setText("");
            binding.txtFromDate.setText("");
            binding.txtDate1.setText("");

            appointment.setCategory("10 DAYS");
            appointment.setHhcPkgPricing(SERVICE_COUNT_TWO);

            // Date Condition
            appointmentRequest.setDate_condition(2);

            //filter package
            getPackage();

            // 10 Day date range
            binding.txtFromDate.setOnClickListener(view -> {

                SingleDayPickCallback callback = fromDate -> {


                    Date = fromDate.clone();
                    SimpleDateFormat dateRangeFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                    Calendar toDate = Calendar.getInstance();
                    toDate.setTime(Date.getTime());
//                toDate.add(Calendar.MONTH, MONTH_COUNT);
                    toDate.add(Calendar.DAY_OF_MONTH, DAY_COUNT);


                    binding.txtFromDate.setText(dateRangeFormat.format(Date.getTime()));

                    binding.txtToDate.setText(dateRangeFormat.format(toDate.getTime()));

                    //set the appointment  request
                    appointmentRequest.setFrom_date(dateRangeFormat.format(Date.getTime()));
                    appointmentRequest.setTo_date(dateRangeFormat.format(toDate.getTime()));
                    appointmentRequest.setDate_preference("");

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


            });

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
                appointmentRequest.setFrom_date("");
                appointmentRequest.setTo_date("");
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
        List<PackagePT> packagePTFiltered = new ArrayList<>();

        packagePTFiltered = packagesList.stream().filter(naPackagePT ->
                (naPackagePT.getHhcPkgPricing().equals(appointment.getHhcPkgPricing())
                )).collect(Collectors.toList());

        if (!packagePTFiltered.isEmpty() && !binding.txtDate1.getText().toString().isEmpty() || !binding.txtFromDate.getText().toString().isEmpty()) {
            appointmentRequest.setPackage_price_sr_no(Integer.parseInt(packagePTFiltered.get(0).getHhcPkgPricing()));
            appointmentRequest.setPrice(packagesList.get(0).getPkgPriceMb());

            appointment.setHhcPkgPricing(packagePTFiltered.get(0).getHhcPkgPricing());
            appointment.setPkgPriceMb(packagePTFiltered.get(0).getPkgPriceMb());

            Log.d(LogTags.PHYSIOTHERAPY, "SelectedPackage: " + packagePTFiltered.get(0));


            homeHealthCareViewModel.setAppointmentRequest(appointmentRequest);
            homeHealthCareViewModel.setSelectedPhysiotherapy(packagePTFiltered.get(0));

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
        Log.d(LogTags.PHYSIOTHERAPY, "USER-APPOINTMENT: " + appointment);
        Log.d(LogTags.PHYSIOTHERAPY, "REQUEST: " + appointmentRequest);


        binding.btnReview.setOnClickListener(v -> {
            homeHealthCareViewModel.setSelectedPhysiotherapy(appointment);
            homeHealthCareViewModel.setAppointmentRequest(appointmentRequest);


            //navigate
            NavDirections actions = PhysiotherapyFragmentDirections.actionPhysiotherapyFragmentToHomeHealthSummaryFragment();
            navController.navigate(actions);
        });

    }

    private void getNAPackages() {
        homeHealthCareViewModel.getPackagesPT().observe(getViewLifecycleOwner(), packages -> {
            packagesList = (ArrayList<PackagePT>) packages.getPackages();
        });
    }

}