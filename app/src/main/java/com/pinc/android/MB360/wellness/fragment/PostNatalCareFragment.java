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
import com.pinc.android.MB360.databinding.FragmentPostNatalCareBinding;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.monthpicker.DatePickerTheme;
import com.pinc.android.MB360.monthpicker.MonthModel;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.Appointment;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.PackageNC;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PostNatalCareFragment extends Fragment {
    FragmentPostNatalCareBinding binding;
    View view;
    NavController navController;

    // View Model
    HomeHealthCareViewModel homeHealthCareViewModel;
    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;

    ArrayList<PackageNC> packagesList;

    PackageNC appointment = new PackageNC();
    Appointment appointmentRequest = new Appointment();

    Calendar calendar;
    PrimeCalendar Date = new CivilCalendar();
    ArrayList<PrimeCalendar> dailDateList = new ArrayList<>();
    public static String MONTHLY = "MONTHLY";
    int MONTH_COUNT = 30;
    int DAY_COUNT = 15;
    ArrayList<MonthModel> monthDateList = new ArrayList<>();

    public static String SERVICE_COUNT_ONE = "1";
    public static String SERVICE_COUNT_TWO = "13";
    public static String SERVICE_COUNT_THREE = "20";

    public PostNatalCareFragment() {
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
        binding = FragmentPostNatalCareBinding.inflate(inflater, container, false);
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

//        appointment.setHhcCityMappSrNo("17");

        //common request attributes
        appointmentRequest.setRej_appt_sr_no("-1");
        appointmentRequest.setIs_rescheduled(0);

        //initial date that changes later
//        appointmentRequest.setFrom_date("01-01-1990");
//        appointmentRequest.setTo_date("01-01-1990");
//        appointmentRequest.setDate_preference("01-01-1990");

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
        binding.btnPerDay.setOnClickListener(v -> {

            binding.btnPerDay.setSelected(true);
            binding.btnFifteenDays.setSelected(false);
            binding.btnThirtyDays.setSelected(false);

            //appointment
            appointment.setHhcPkgPricing(SERVICE_COUNT_ONE);
            appointment.setCategory("PER DAY");

            binding.llMonthSelection.setVisibility(View.VISIBLE);

            binding.dailyDateLayout.setVisibility(View.VISIBLE);
            binding.txtDate1.setVisibility(View.VISIBLE);
            binding.dateRange.setVisibility(View.GONE);

            binding.txtToDate.setText("");
            binding.txtFromDate.setText("");
            binding.txtDate1.setText("");

            // Date Condition
            appointmentRequest.setDate_condition(1);

            //filter package
            getPackage();
        });

        //fifteen Day
        binding.btnFifteenDays.setOnClickListener(v -> {

            binding.btnPerDay.setSelected(false);
            binding.btnFifteenDays.setSelected(true);
            binding.btnThirtyDays.setSelected(false);

            //appointment
            appointment.setHhcPkgPricing(SERVICE_COUNT_TWO);
            appointment.setCategory("15 DAYS");

            // Date Condition
            appointmentRequest.setDate_condition(2);

            binding.llMonthSelection.setVisibility(View.VISIBLE);

            binding.dailyDateLayout.setVisibility(View.GONE);
            binding.txtDate1.setVisibility(View.GONE);
            binding.dateRange.setVisibility(View.VISIBLE);

            binding.txtToDate.setText("");
            binding.txtFromDate.setText("");
            binding.txtDate1.setText("");

            //filter package
            getPackage();

            // 15 Day date range
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

//        //duration buttons
//        binding.btnThirtyDays.setOnClickListener(v -> {
//
//            //appointment request
//            appointmentRequest.setDate_condition(2);
//
//            binding.btnPerDay.setSelected(false);
//            binding.btnFifteenDays.setSelected(false);
//            binding.btnThirtyDays.setSelected(true);
//
//            dailDateList.clear();
////            adapter.notifyItemRangeChanged(0, dailDateList.size());
//
//            appointmentRequest.setDate_condition(2);
//
//
////            //appointment
//            appointment.setCategory(MONTHLY);
//
//            //filter package
//            getPackage();
//
//
//        });

        //thirty Day
        binding.btnThirtyDays.setOnClickListener(v -> {

            binding.btnPerDay.setSelected(false);
            binding.btnFifteenDays.setSelected(false);
            binding.btnThirtyDays.setSelected(true);

            //appointment
            appointment.setHhcPkgPricing(SERVICE_COUNT_THREE);
            appointment.setCategory("30 DAYS");

            binding.llMonthSelection.setVisibility(View.VISIBLE);

            binding.dailyDateLayout.setVisibility(View.GONE);
            binding.txtDate1.setVisibility(View.GONE);
            binding.dateRange.setVisibility(View.VISIBLE);
            binding.txtToDate.setText("");
            binding.txtFromDate.setText("");
            binding.txtDate1.setText("");

            // Date Condition
            appointmentRequest.setDate_condition(3);

            //filter package
            getPackage();

            // 30 Day date range
            binding.txtFromDate.setOnClickListener(view -> {

                SingleDayPickCallback callback = fromDate -> {


                    Date = fromDate.clone();
                    SimpleDateFormat dateRangeFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                    Calendar toDate = Calendar.getInstance();
                    toDate.setTime(Date.getTime());
//                toDate.add(Calendar.MONTH, MONTH_COUNT);
                    toDate.add(Calendar.DAY_OF_MONTH, MONTH_COUNT);


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


//        binding.llMonthSelection.setOnClickListener(view -> {
//            MonthPickerDialog monthPickerDialog = new MonthPickerDialog(getActivity(), this, monthDateList);
//            monthPickerDialog.showMonthDialogue();
//            //filter package
//            getPackage();
//        });

//        //DateRange Text
//        binding.txtFromDate.setText("Start Date");
//        binding.txtToDate.setText("End Date");


//date range system






        //calendar From Date
//        binding.txtWeekFromDate.setOnClickListener(v -> {
//
//            Calendar calendar = Calendar.getInstance();
//            SingleDayPickCallback callback = fromDate -> {
//
//                Date = fromDate.clone();
//                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//
//                StringBuilder dateString = new StringBuilder();
//                StringBuilder displayDateBuilder = new StringBuilder();
//                for (PrimeCalendar date : dailDateList) {
//                    dateString.append(dateFormat.format(date.getTime())).append(",");
//                    displayDateBuilder.append(dateFormat.format(date.getTime())).append("\n");
//                }
//                String date = dateString.toString().replaceFirst(".$", "");
//                appointmentRequest.setDate_preference(date);
////                appointmentRequest.setCount(String.valueOf(dailDateList.size()));
//
//                binding.fromdateTV.setText(dateFormat.format(Date.getTime()));
//            };
//            PrimeCalendar today = new CivilCalendar();
//            PrimeCalendar tomorrow = new CivilCalendar();
//            tomorrow.add(Calendar.DATE, 1);
//
//
//            LightThemeFactory themeFactory = DatePickerTheme.Companion.getTheme();
//
//            PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(today)
//                    .pickSingleDay(callback)
//                    .initiallyPickedSingleDay(Date)
//                    .disabledDays(weekends)
//                    .minPossibleDate(today)
//                    .applyTheme(themeFactory)
//                    .build();
//
//            datePicker.show(requireActivity().getSupportFragmentManager(), "SOME_TAG");
////
////
////
//            //filter package
//            getPackage();
//        });

        //calendar To Date
//        binding.txtWeekToDate.setOnClickListener(v -> {
//
//            Calendar calendar = Calendar.getInstance();
//            SingleDayPickCallback callback = fromDate -> {
//
//                Date = fromDate.clone();
//                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//
//                StringBuilder dateString = new StringBuilder();
//                StringBuilder displayDateBuilder = new StringBuilder();
//                for (PrimeCalendar date : dailDateList) {
//                    dateString.append(dateFormat.format(date.getTime())).append(",");
//                    displayDateBuilder.append(dateFormat.format(date.getTime())).append("\n");
//                }
//                String date = dateString.toString().replaceFirst(".$", "");
//                appointmentRequest.setDate_preference(date);
////                appointmentRequest.setCount(String.valueOf(dailDateList.size()));
//
//                binding.todateTV.setText(dateFormat.format(Date.getTime()));
//            };
//            PrimeCalendar today = new CivilCalendar();
//            PrimeCalendar tomorrow = new CivilCalendar();
//            tomorrow.add(Calendar.DATE, 1);
//
//
//            LightThemeFactory themeFactory = DatePickerTheme.Companion.getTheme();
//
//            PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(today)
//                    .pickSingleDay(callback)
//                    .initiallyPickedSingleDay(Date)
//                    .disabledDays(weekends)
//                    .minPossibleDate(today)
//                    .applyTheme(themeFactory)
//                    .build();
//
//            datePicker.show(requireActivity().getSupportFragmentManager(), "SOME_TAG");
////
////
////
//            //filter package
//            getPackage();
//        });

        return view;
    }

    private void getPackage() {
        List<PackageNC> packageNCFiltered = new ArrayList<>();

        packageNCFiltered = packagesList.stream().filter(naPackageNC ->
                (naPackageNC.getHhcPkgPricing().equals(appointment.getHhcPkgPricing())
                )).collect(Collectors.toList());

        if (!packageNCFiltered.isEmpty() && !binding.txtDate1.getText().toString().isEmpty() || !binding.txtFromDate.getText().toString().isEmpty()) {
            appointmentRequest.setPackage_price_sr_no(Integer.parseInt(packageNCFiltered.get(0).getHhcPkgPricing()));
            appointmentRequest.setPrice(packagesList.get(0).getPkgPriceMb());

            appointment.setHhcPkgPricing(packageNCFiltered.get(0).getHhcPkgPricing());
            appointment.setPkgPriceMb(packageNCFiltered.get(0).getPkgPriceMb());

            Log.d(LogTags.POST_NATAL, "SelectedPackage: " + packageNCFiltered.get(0));


            homeHealthCareViewModel.setAppointmentRequest(appointmentRequest);
            homeHealthCareViewModel.setSelectedPostNatal(packageNCFiltered.get(0));

            if (!binding.txtFromDate.equals("") && !binding.txtToDate.equals(""))
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
        Log.d(LogTags.POST_NATAL, "USER-APPOINTMENT: " + appointment);
        Log.d(LogTags.POST_NATAL, "REQUEST: " + appointmentRequest);


        binding.btnReview.setOnClickListener(v -> {
            homeHealthCareViewModel.setSelectedPostNatal(appointment);
            homeHealthCareViewModel.setAppointmentRequest(appointmentRequest);

            //navigate
            NavDirections actions = PostNatalCareFragmentDirections.actionPostNatalCareFragmentToHomeHealthSummaryFragment();
            navController.navigate(actions);
        });
    }

    private void getNAPackages() {
        homeHealthCareViewModel.getPackagesNC().observe(getViewLifecycleOwner(), packages -> {
            packagesList = (ArrayList<PackageNC>) packages.getPackages();
        });
    }

}