package com.pinc.android.MB360.wellness.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

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
import com.pinc.android.MB360.databinding.FragmentRescheduleBinding;
import com.pinc.android.MB360.monthpicker.DatePickerTheme;
import com.pinc.android.MB360.monthpicker.GetMonthsFromDialog;
import com.pinc.android.MB360.monthpicker.MonthModel;
import com.pinc.android.MB360.monthpicker.MonthPickerDialog;
import com.pinc.android.MB360.wellness.homehealthcare.responseclass.ScheduledSummary;
import com.pinc.android.MB360.wellness.homehealthcare.ui.DatesRecyclerViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class RescheduleFragment extends Fragment implements GetMonthsFromDialog {

    FragmentRescheduleBinding binding;
    View view;

    Calendar calendar;
    PrimeCalendar startDate = new CivilCalendar();

    DatesRecyclerViewAdapter adapter;
    ArrayList<PrimeCalendar> dailDateList = new ArrayList<>();
    ArrayList<MonthModel> monthDateList = new ArrayList<>();


    public static String DAILY = "DAILY";
    public static String WEEKLY = "WEEKLY";
    public static String MONTHLY = "MONTHLY";

    private ScheduledSummary scheduledSummary;

    int MONTH_COUNT = 1;

    NavController navController;

    public RescheduleFragment() {
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
        binding = FragmentRescheduleBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        //To Navigate
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_wellness);
        navController = navHostFragment.getNavController();


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

        //duration buttons
        binding.btnMonthly.setOnClickListener(v -> {

            //appointment request
//            appointmentRequest.setDate_condition(2);

            binding.btnMonthly.setSelected(true);
            binding.btnDaily.setSelected(false);
            binding.mainDailyDateLayout.setVisibility(View.GONE);
            binding.monthPolicy.setVisibility(View.VISIBLE);

            dailDateList.clear();
            adapter.notifyItemRangeChanged(0, dailDateList.size());


            //appointment
//            appointment.setHhcNaDurations(MONTHLY);

            //filter package
//            getPackage();


        });
        binding.btnDaily.setOnClickListener(v -> {

            //appointment request
//            appointmentRequest.setDate_condition(1);

            binding.btnDaily.setSelected(true);
            binding.btnMonthly.setSelected(false);
            binding.mainDailyDateLayout.setVisibility(View.VISIBLE);
            binding.monthPolicy.setVisibility(View.GONE);
            monthDateList.clear();
            adapter.notifyItemRangeChanged(0, monthDateList.size());

            //appointment
//            appointment.setHhcNaDurations(DAILY);

            //filter package
//            getPackage();

        });


        //dates layouts
        //date radio
        binding.radioDates.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {

                //appointment request
//                appointmentRequest.setDate_condition(3);

                monthDateList.clear();

                binding.rlMonthCount.setVisibility(View.VISIBLE);
                binding.llMonthSelection.setVisibility(View.GONE);

                //filter package
//                getPackage();

            }
        });
        binding.radioMonth.setOnCheckedChangeListener((compoundButton, checked) -> {
            if (checked) {

                //appointment request
//                appointmentRequest.setDate_condition(2);

                binding.rlMonthCount.setVisibility(View.GONE);
                binding.llMonthSelection.setVisibility(View.VISIBLE);

                //filter package
//                getPackage();
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
//                appointmentRequest.setDate_preference(date);
//                appointmentRequest.setCount(String.valueOf(dailDateList.size()));
//                appointmentRequest.setFrom_date("01-01-1900");
//                appointmentRequest.setTo_date("01-01-1900");

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
//            getPackage();
        });

        binding.llMonthSelection.setOnClickListener(view -> {
            MonthPickerDialog monthPickerDialog = new MonthPickerDialog(getActivity(), this, monthDateList);
            monthPickerDialog.showMonthDialogue();
            //filter package
//            getPackage();
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
//                appointmentRequest.setFrom_date(dateRangeFormat.format(startDate.getTime()));
//                appointmentRequest.setTo_date(dateRangeFormat.format(toDate.getTime()));

                //filter package
//                getPackage();
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
//            appointmentRequest.setCount(String.valueOf(MONTH_COUNT));
            Calendar toDate = Calendar.getInstance();
            toDate.setTime(startDate.getTime());
            toDate.add(Calendar.MONTH, MONTH_COUNT);
            SimpleDateFormat dateRangeFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            binding.txtToDate.setText(dateRangeFormat.format(toDate.getTime()));
            binding.txtFromDate.setText(dateRangeFormat.format(startDate.getTime()));


            //setting the date
//            appointmentRequest.setFrom_date(dateRangeFormat.format(startDate.getTime()));
//            appointmentRequest.setTo_date(dateRangeFormat.format(toDate.getTime()));


            //filter package
//            getPackage();
        });
        binding.btnSubtract.setOnClickListener(v -> {
            if (MONTH_COUNT > 1) {
                MONTH_COUNT--;
//                appointmentRequest.setCount(String.valueOf(MONTH_COUNT));
                binding.txtCount.setText(String.valueOf(MONTH_COUNT));
                Calendar toDate = Calendar.getInstance();
                toDate.setTime(startDate.getTime());
                toDate.add(Calendar.MONTH, MONTH_COUNT);
                SimpleDateFormat dateRangeFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                binding.txtToDate.setText(dateRangeFormat.format(toDate.getTime()));
                binding.txtFromDate.setText(dateRangeFormat.format(startDate.getTime()));
            }

            //filter package
//            getPackage();
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

            scheduledSummary = RescheduleFragmentArgs.fromBundle(getArguments()).getGetRescheduleSummary();

            binding.txtPackageDetails.setText(scheduledSummary.getAppntPerson() + " (Age : " + scheduledSummary.getAppntPersonAge() + ")");

            if(scheduledSummary.getNaDurations().equalsIgnoreCase("Monthly")){

                binding.btnDaily.setSelected(false);
                binding.btnMonthly.setSelected(true);
                binding.mainDailyDateLayout.setVisibility(View.GONE);
                binding.monthPolicy.setVisibility(View.VISIBLE);


            }else{

                binding.btnDaily.setSelected(true);
                binding.btnMonthly.setSelected(false);
                binding.mainDailyDateLayout.setVisibility(View.VISIBLE);
                binding.monthPolicy.setVisibility(View.GONE);
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

//        appointmentRequest.setFrom_date(date);
//        appointmentRequest.setTo_date(dateto);
//        appointmentRequest.setDate_preference("01-01-1990");
//        appointmentRequest.setCount(String.valueOf(monthList.size()));

    }
}