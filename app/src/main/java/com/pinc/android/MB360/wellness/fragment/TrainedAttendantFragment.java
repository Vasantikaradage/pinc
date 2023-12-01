package com.pinc.android.MB360.wellness.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback;
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback;
import com.aminography.primedatepicker.picker.theme.LightThemeFactory;
import com.pinc.android.MB360.R;
import com.pinc.android.MB360.databinding.FragmentTrainedAttendedBinding;
import com.pinc.android.MB360.insurance.repository.LoadSessionViewModel;
import com.pinc.android.MB360.monthpicker.DatePickerTheme;
import com.pinc.android.MB360.monthpicker.GetMonthsFromDialog;
import com.pinc.android.MB360.monthpicker.MonthModel;
import com.pinc.android.MB360.monthpicker.MonthPickerDialog;
import com.pinc.android.MB360.utilities.LogTags;
import com.pinc.android.MB360.wellness.dashboardwellness.repository.DashboardWellnessViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.HomeHealthCareViewModel;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.Appointment;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.HomeHealthCarePackage;
import com.pinc.android.MB360.wellness.homehealthcare.ui.DatesRecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class TrainedAttendantFragment extends Fragment implements GetMonthsFromDialog {

    FragmentTrainedAttendedBinding binding;
    View view;

    HomeHealthCareViewModel homeHealthCareViewModel;
    LoadSessionViewModel loadSessionViewModel;
    DashboardWellnessViewModel dashboardWellnessViewModel;

    ArrayList<HomeHealthCarePackage> packagesList;

    HomeHealthCarePackage appointment = new HomeHealthCarePackage();
    Appointment appointmentRequest = new Appointment();


    Calendar calendar;
    PrimeCalendar startDate = new CivilCalendar();

    DatesRecyclerViewAdapter adapter;
    ArrayList<PrimeCalendar> dailDateList = new ArrayList<>();
    ArrayList<MonthModel> monthDateList = new ArrayList<>();


    public static String DAILY = "DAILY";
    public static String WEEKLY = "WEEKLY";
    public static String MONTHLY = "MONTHLY";

    public static String TWELVE_HOURS = "12";
    public static String TWENTY_FOURS_HOURS = "24";
    public static String NURSING_COUNT_ONE = "1";
    public static String NURSING_COUNT_TWO = "2";

    int MONTH_COUNT = 1;

    NavController navController;

    public TrainedAttendantFragment() {
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
        binding = FragmentTrainedAttendedBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        homeHealthCareViewModel = new ViewModelProvider(requireActivity()).get(HomeHealthCareViewModel.class);
        loadSessionViewModel = new ViewModelProvider(requireActivity()).get(LoadSessionViewModel.class);
        dashboardWellnessViewModel = new ViewModelProvider(requireActivity()).get(DashboardWellnessViewModel.class);

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();


        //todo remove testing location
//        appointment.setHhcNaCityMapp((homeHealthCareViewModel.getSelectedCity().getValue().getSrno()) == null ? "11" : homeHealthCareViewModel.getSelectedCity().getValue().getSrno());

        appointment.setHhcNaCityMapp(homeHealthCareViewModel.getSelectedCity().getValue() != null ? "11" : homeHealthCareViewModel.getSelectedCity().getValue().getCity());

//        appointment.setHhcNaCityMapp("1");

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


        //logic for inputs

        //todo bottom navigation invisible

        adapter = new DatesRecyclerViewAdapter(dailDateList, monthDateList);
        binding.datesCycle.setAdapter(adapter);


        // to disable sundays as that library doesn't supports sundays disable;
        PrimeCalendar sunday;
        List<PrimeCalendar> weekends = new ArrayList<>();
        int weeks = 1000;
        for (int i = 0; i < (weeks * 7); i = i + 7) {
            sunday = new CivilCalendar();
            sunday.add(Calendar.DAY_OF_YEAR, (Calendar.SUNDAY - sunday.get(Calendar.DAY_OF_WEEK) + 7 + i));
            weekends.add(sunday);
        }


        // hours btn
        binding.btnTimetwelve.setOnClickListener(view -> {

            binding.btnTimetwelve.setSelected(true);
            binding.btnTime24.setSelected(false);
            //nursing one by default
            //nursing 2 disabled
            binding.btnNursingCount2.setEnabled(false);
            binding.btnNursingCount1.setEnabled(true);
            binding.btnNursingCount1.setSelected(true);


            //appointments
            appointment.setHhcNaHours(TWELVE_HOURS);
            appointment.setHhcNaNacount(NURSING_COUNT_ONE);
            //filter package
            getPackage();
        });

        binding.btnTime24.setOnClickListener(view -> {
            appointment.setHhcNaHours(TWENTY_FOURS_HOURS);
            binding.btnTimetwelve.setSelected(false);
            binding.btnTime24.setSelected(true);
            //nursing one by default
            //nursing 2 disabled
            binding.btnNursingCount1.setEnabled(true);
            binding.btnNursingCount1.setSelected(true);
            binding.btnNursingCount2.setEnabled(true);
            binding.btnNursingCount2.setSelected(false);

            //appointment
            appointment.setHhcNaHours(TWENTY_FOURS_HOURS);

            //filter package
            getPackage();
        });


        //nursing count
        binding.btnNursingCount1.setOnClickListener(v -> {

            binding.btnNursingCount1.setEnabled(true);
            binding.btnNursingCount1.setSelected(true);
            binding.btnNursingCount2.setSelected(false);

            //appointment
            appointment.setHhcNaNacount(NURSING_COUNT_ONE);

            //filter package
            getPackage();
        });
        binding.btnNursingCount2.setOnClickListener(v -> {

            binding.btnNursingCount2.setEnabled(true);
            binding.btnNursingCount2.setSelected(true);
            binding.btnNursingCount1.setSelected(false);

            //appointment
            appointment.setHhcNaNacount(NURSING_COUNT_TWO);

            //filter package
            getPackage();

        });

        //duration buttons
        binding.btnMonthly.setOnClickListener(v -> {

            //appointment request
            appointmentRequest.setDate_condition(2);

            binding.btnMonthly.setSelected(true);
            binding.btnDaily.setSelected(false);
            binding.mainDailyDateLayout.setVisibility(View.GONE);
            binding.monthPolicy.setVisibility(View.VISIBLE);

            dailDateList.clear();
            adapter.notifyItemRangeChanged(0, dailDateList.size());

            //appointment
            appointment.setHhcNaDurations(MONTHLY);

            //filter package
            getPackage();


        });
        binding.btnDaily.setOnClickListener(v -> {

            //appointment request
            appointmentRequest.setDate_condition(1);

            binding.btnDaily.setSelected(true);
            binding.btnMonthly.setSelected(false);
            binding.mainDailyDateLayout.setVisibility(View.VISIBLE);
            binding.monthPolicy.setVisibility(View.GONE);
            monthDateList.clear();
            adapter.notifyItemRangeChanged(0, monthDateList.size());

            //appointment
            appointment.setHhcNaDurations(DAILY);

            //filter package
            getPackage();

        });


        //dates layouts
        //date radio
        binding.radioDates.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {

                //appointment request
                appointmentRequest.setDate_condition(3);

                monthDateList.clear();

                binding.rlMonthCount.setVisibility(View.VISIBLE);
                binding.llMonthSelection.setVisibility(View.GONE);

                //filter package
                getPackage();

            }
        });
        binding.radioMonth.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {

                //appointment request
                appointmentRequest.setDate_condition(2);

                binding.rlMonthCount.setVisibility(View.GONE);
                binding.llMonthSelection.setVisibility(View.VISIBLE);

                //filter package
                getPackage();
            }
        });


        //calendars
        binding.dailyDateLayout.setOnClickListener(v -> {

            MultipleDaysPickCallback callback = multipleDays -> {
                dailDateList.clear();
                dailDateList.addAll(multipleDays);
                adapter.notifyItemRangeChanged(0, dailDateList.size());


                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
                StringBuilder dateString = new StringBuilder();
                StringBuilder displayDateBuilder = new StringBuilder();
                for (PrimeCalendar date : dailDateList) {
                    dateString.append(dateFormat.format(date.getTime())).append(",");
                    displayDateBuilder.append(dateFormat.format(date.getTime())).append("\n");
                }
                String date = dateString.toString().replaceFirst(".$", "");
                appointmentRequest.setDate_preference(date);
                appointmentRequest.setCount(String.valueOf(dailDateList.size()));
                appointmentRequest.setFrom_date("01-01-1900");
                appointmentRequest.setTo_date("01-01-1900");

            };
            PrimeCalendar today = new CivilCalendar();
            PrimeCalendar tomorrow = new CivilCalendar();
            tomorrow.add(Calendar.DATE, 1);

            LightThemeFactory themeFactory = DatePickerTheme.Companion.getTheme();

            PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(today)
                    .pickMultipleDays(callback)
                    .initiallyPickedMultipleDays(dailDateList)
                    .disabledDays(weekends)
                    .minPossibleDate(today)
                    .applyTheme(themeFactory)
                    .build();
            datePicker.show(requireActivity().getSupportFragmentManager(), "SOME_TAG");
            //filter package
            getPackage();
        });
        binding.llMonthSelection.setOnClickListener(view -> {
            MonthPickerDialog monthPickerDialog = new MonthPickerDialog(getActivity(), this, monthDateList);
            monthPickerDialog.showMonthDialogue();
            //filter package
            getPackage();
        });


        //DateRange Text
        binding.txtFromDate.setText("Start Date");
        binding.txtToDate.setText("End Date");

        //date range system
        binding.txtFromDate.setOnClickListener(view -> {

            SingleDayPickCallback callback = fromDate -> {


                startDate = fromDate.clone();
                SimpleDateFormat dateRangeFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                Calendar toDate = Calendar.getInstance();
                toDate.setTime(startDate.getTime());
                toDate.add(Calendar.MONTH, MONTH_COUNT);


                binding.txtFromDate.setText(dateRangeFormat.format(startDate.getTime()));

                binding.txtToDate.setText(dateRangeFormat.format(toDate.getTime()));

                //set the appointment  request
                appointmentRequest.setFrom_date(dateRangeFormat.format(startDate.getTime()));
                appointmentRequest.setTo_date(dateRangeFormat.format(toDate.getTime()));

                //filter package
                getPackage();
            };

            PrimeCalendar today = new CivilCalendar();
            PrimeCalendar tomorrow = new CivilCalendar();
            tomorrow.add(Calendar.DATE, 1);


            LightThemeFactory themeFactory = DatePickerTheme.Companion.getTheme();

            PrimeDatePicker datePicker = PrimeDatePicker.Companion.dialogWith(today)
                    .pickSingleDay(callback)
                    .initiallyPickedSingleDay(startDate)
                    .disabledDays(weekends)
                    .minPossibleDate(today)
                    .applyTheme(themeFactory)
                    .build();

            datePicker.show(requireActivity().getSupportFragmentManager(), "SOME_TAG");


        });

        binding.btnAdd.setOnClickListener(v -> {
            MONTH_COUNT++;
            binding.txtCount.setText(String.valueOf(MONTH_COUNT));
            appointmentRequest.setCount(String.valueOf(MONTH_COUNT));
            Calendar toDate = Calendar.getInstance();
            toDate.setTime(startDate.getTime());
            toDate.add(Calendar.MONTH, MONTH_COUNT);
            SimpleDateFormat dateRangeFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            binding.txtToDate.setText(dateRangeFormat.format(toDate.getTime()));
            binding.txtFromDate.setText(dateRangeFormat.format(startDate.getTime()));


            //setting the date
            appointmentRequest.setFrom_date(dateRangeFormat.format(startDate.getTime()));
            appointmentRequest.setTo_date(dateRangeFormat.format(toDate.getTime()));


            //filter package
            getPackage();
        });
        binding.btnSubtract.setOnClickListener(v -> {
            if (MONTH_COUNT > 1) {
                MONTH_COUNT--;
                appointmentRequest.setCount(String.valueOf(MONTH_COUNT));
                binding.txtCount.setText(String.valueOf(MONTH_COUNT));
                Calendar toDate = Calendar.getInstance();
                toDate.setTime(startDate.getTime());
                toDate.add(Calendar.MONTH, MONTH_COUNT);
                SimpleDateFormat dateRangeFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                binding.txtToDate.setText(dateRangeFormat.format(toDate.getTime()));
                binding.txtFromDate.setText(dateRangeFormat.format(startDate.getTime()));
            }

            //filter package
            getPackage();
        });


        return view;

    }

    private void getPackage() {
        List<HomeHealthCarePackage> packageFiltered = new ArrayList<>();

        packageFiltered = packagesList.stream().filter(naPackage ->
                (naPackage.getHhcNaCityMapp().equals(appointment.getHhcNaCityMapp()) &&
                        naPackage.getHhcNaDurations().equals(appointment.getHhcNaDurations()) &&
                        naPackage.getHhcNaNacount().equals(appointment.getHhcNaNacount()) &&
                        naPackage.getHhcNaHours().equals(appointment.getHhcNaHours())
                )).collect(Collectors.toList());

        if (!packageFiltered.isEmpty()) {
            appointmentRequest.setPackage_price_sr_no(Integer.parseInt(packageFiltered.get(0).getHhcPkgPricing()));
            appointmentRequest.setPrice(packagesList.get(0).getPkgPriceMb());

            appointment.setHhcPkgPricing(packageFiltered.get(0).getHhcPkgPricing());
            appointment.setPkgPriceMb(packageFiltered.get(0).getPkgPriceMb());

            Log.d(LogTags.TRAINED_ATTENDANT, "SelectedPackage: " + packageFiltered.get(0));


            homeHealthCareViewModel.setAppointmentRequest(appointmentRequest);
            homeHealthCareViewModel.setSelectedTrainedAttendantPackage(packageFiltered.get(0));

            binding.btnReview.setEnabled(true);
            binding.btnReview.setClickable(true);
            binding.btnReview.setFocusable(true);
            binding.btnReview.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.greenlightbg2));

        } else {
            appointmentRequest.setPrice("0");
            appointmentRequest.setTotal_price("0");
            binding.btnReview.setEnabled(false);
            binding.btnReview.setClickable(false);
            binding.btnReview.setFocusable(false);
            binding.btnReview.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey1));
        }
        Log.d(LogTags.TRAINED_ATTENDANT, "USER-APPOINTMENT: " + appointment);
        Log.d(LogTags.TRAINED_ATTENDANT, "REQUEST: " + appointmentRequest);


        binding.btnReview.setOnClickListener(v -> {
            homeHealthCareViewModel.setSelectedTrainedAttendantPackage(appointment);
            homeHealthCareViewModel.setAppointmentRequest(appointmentRequest);


            //navigate
            NavDirections actions = TrainedAttendantFragmentDirections.actionTrainedAttendedFragmentToHomeHealthSummaryFragment();
            navController.navigate(actions);
        });

    }

    private void getNAPackages() {
        homeHealthCareViewModel.getPackages().observe(getViewLifecycleOwner(), packages -> {
            packagesList = (ArrayList<HomeHealthCarePackage>) packages.getPackages();
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (appointment != null) {
            if (appointment.getHhcNaHours() != null && appointment.getHhcNaHours().equals(TWELVE_HOURS)) {
                binding.btnTimetwelve.performClick();
            } else if (appointment.getHhcNaHours() != null && appointment.getHhcNaHours().equals(TWENTY_FOURS_HOURS)) {
                binding.btnTime24.performClick();
            }
            if (appointment.getHhcNaNacount() != null && appointment.getHhcNaNacount().equals(NURSING_COUNT_ONE)) {
                binding.btnNursingCount1.performClick();
            } else if (appointment.getHhcNaNacount() != null && appointment.getHhcNaNacount().equals(NURSING_COUNT_TWO)) {
                binding.btnNursingCount2.performClick();
            }
            if (appointment.getHhcNaDurations() != null && appointment.getHhcNaDurations().equals(DAILY)) {
                binding.btnDaily.performClick();
            } else if (appointment.getHhcNaDurations() != null && appointment.getHhcNaDurations().equals(MONTHLY)) {
                binding.btnMonthly.performClick();
            }

        }
    }

    @Override
    public void getMonths(ArrayList<MonthModel> monthList) {
        monthDateList.clear();
        monthDateList.addAll(monthList);
        adapter.notifyItemRangeChanged(0, monthList.size());

        String toDate = "01-01-1990";
        String fromDate = "01-01-1990";
        Calendar monthCalendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
        StringBuilder displayDateBuilder = new StringBuilder();
        StringBuilder dateString = new StringBuilder();
        StringBuilder dateStringto = new StringBuilder();
        for (MonthModel date : monthDateList) {

            String displayDate = date.getMonth() + "-" + date.getYear();
            displayDateBuilder.append(displayDate).append("\n");

            String dateFromStringMonth = "01-" + date.getMonth() + "-" + date.getYear();
            //dateString.append(dateFormat.format(dateStringMonth)).append(",");
            try {

                Calendar fromCalendar = Calendar.getInstance();
                fromCalendar.setTime(dateFormat.parse(dateFromStringMonth));

                Calendar toCalendar = Calendar.getInstance();
                toCalendar.setTime(dateFormat.parse(dateFromStringMonth));
                toCalendar.set(Calendar.DATE, toCalendar.getActualMaximum(Calendar.DATE));

                dateString.append(dateFormat.format(fromCalendar.getTime())).append(",");
                dateStringto.append(dateFormat.format(toCalendar.getTime())).append(",");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String date = dateString.toString().replaceFirst(".$", "");
        String dateto = dateStringto.toString().replaceFirst(".$", "");

        appointmentRequest.setFrom_date(date);
        appointmentRequest.setTo_date(dateto);
        appointmentRequest.setDate_preference("01-01-1990");
        appointmentRequest.setCount(String.valueOf(monthList.size()));

    }

}